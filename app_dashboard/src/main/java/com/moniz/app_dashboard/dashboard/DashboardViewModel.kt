package com.moniz.app_dashboard.dashboard

import android.system.Os.close
import androidx.annotation.StringRes
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.constraintlayout.motion.utils.ViewState
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.nio.file.Files.copy

@HiltViewModel(assistedFactory = DashboardViewModel.Factory::class)
internal class DashboardViewModel @AssistedInject constructor(
    private val getAccountUseCase: GetAccountUseCase,
    private val editAccountAliasUseCase: EditAccountAliasUseCase,
    private val eventBus: EventBus,
    @Assisted("accountId") private val accountId: String
) : BaseViewModel<DashboardViewModel.ComposeState>(initialState = ComposeState()) {

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("accountId") accountId: String
        ): DashboardViewModel
    }

    val nicknameTextState = TextFieldState("")

    override suspend fun initOnSubscribe() {
        if (state.account == null) {
            updateState { copy(viewState = ViewState.Loading) }
            val account = getAccountUseCase(accountId).getOrNull() ?: return close()
            val displayName = account.displayName
            if (displayName.isNotBlank()) nicknameTextState.setTextAndPlaceCursorAtEnd(displayName)
            updateState {
                copy(
                    account = account,
                    originalNickname = displayName,
                    showRemoveBtn = account.nickname?.isNotBlank() == true,
                    viewState = ViewState.Input
                )
            }
        }
    }

    private fun currentNickname(): String = nicknameTextState.text.toString()

    fun onInputNicknameValidation(passed: Boolean) =
        updateState { copy(inputNicknameValidation = passed) }

    fun onBackPressed() {
        if (currentNickname() != state.originalNickname) {
            updateState { copy(showUnsavedChangesDialog = triggered) }
        } else {
            close()
        }
    }

    fun onSaveNickname() {
        val nickname = currentNickname()
        if (!state.inputNicknameValidation) return
        if (nickname == state.originalNickname && nickname.isNotBlank()) {
            assumeSavedAndClose()
            return
        }
        if (nickname.isBlank()) {
            updateState { copy(showEmptyNicknameDialog = triggered) }
            return
        }
        performSave(nickname)
    }

    fun onRemoveClicked() = updateState { copy(showRemoveDialog = triggered) }

    fun onDismissRemoveDialog() = updateState { copy(showRemoveDialog = consumed) }

    fun onConfirmRemove() {
        onDismissRemoveDialog()
        performRemove()
    }

    fun onDismissEmptyNicknameDialog() = updateState { copy(showEmptyNicknameDialog = consumed) }

    fun onConfirmAcceptDefault() {
        onDismissEmptyNicknameDialog()
        performSave(null)
    }

    fun onSaveAndExit() {
        val nickname = currentNickname()
        onDismissUnsavedDialog()
        if (!state.inputNicknameValidation) return
        performSave(nickname)
    }

    fun onExitWithoutSaving() {
        onDismissUnsavedDialog()
        close()
    }

    fun onDismissUnsavedDialog() = updateState { copy(showUnsavedChangesDialog = consumed) }

    private fun assumeSavedAndClose() {
        viewModelScope.launch {
            eventBus.notify(
                Event.Snackbar(
                    text = StringResource.ResId(string.edit_account_nickname_saved_toast),
                    config = Event.Snackbar.Config.Success
                )
            )
            close()
        }
    }

    private fun performSave(nickname: String?) = performUpdate(
        nickname = nickname,
        successMessageResId = string.edit_account_nickname_saved_toast,
        errorTitleResId = string.edit_account_nickname_error_title,
        errorDescriptionResId = string.edit_account_nickname_error_description
    )

    private fun performRemove() = performUpdate(
        nickname = null,
        successMessageResId = string.edit_account_nickname_removed_toast,
        errorTitleResId = string.edit_account_nickname_remove_error_title,
        errorDescriptionResId = string.edit_account_nickname_remove_error_description
    )

    private fun performUpdate(
        nickname: String?,
        @StringRes successMessageResId: Int,
        @StringRes errorTitleResId: Int,
        @StringRes errorDescriptionResId: Int
    ) {
        val account = state.account ?: return
        viewModelScope.launch {
            updateState { copy(viewState = ViewState.Loading) }
            editAccountAliasUseCase(
                EditAccountAliasUseCase.Params(
                    accountId = account.accountId,
                    alias = nickname
                )
            ).fold(onSuccess = {
                eventBus.notify(
                    Event.Snackbar(
                        text = StringResource.ResId(successMessageResId),
                        config = Event.Snackbar.Config.Success
                    )
                )
                close()
            }, onFailure = {
                if (it is AppError) {
                    eventBus.notify(
                        Event.ErrorEvent.Resource(
                            titleResId = errorTitleResId,
                            descriptionResId = errorDescriptionResId
                        )
                    )
                }
                updateState { copy(viewState = ViewState.Input) }
            })
        }
    }

    data class ComposeState(
        override val viewState: ViewState = ViewState.Input,
        val originalNickname: String = "",
        val account: AccountBase? = null,
        val showRemoveBtn: Boolean = false,
        val showRemoveDialog: StateEvent = consumed,
        val showEmptyNicknameDialog: StateEvent = consumed,
        val showUnsavedChangesDialog: StateEvent = consumed,
        val inputNicknameValidation: Boolean = true
    ) : ScreenState
}
