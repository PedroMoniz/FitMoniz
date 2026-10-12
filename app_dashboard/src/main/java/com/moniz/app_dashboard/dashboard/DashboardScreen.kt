package com.moniz.app_dashboard.dashboard

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.parcelize.Parcelize

@Parcelize
class EditAccountNicknameScreen(val accountId: String) : BaseScreen {

    @Composable
    override fun Content() {
        val viewModel =
            hiltViewModel<DashboardViewModel, DashboardViewModel.Factory>(creationCallback = {
                it.create(accountId = accountId)
            })
        val composeState: DashboardViewModel.ComposeState by viewModel.collectComposeState()

        HandleDialogs(viewModel, composeState)

        EditAccountNicknameView(
            state = composeState,
            nicknameTextState = viewModel.nicknameTextState,
            onClose = viewModel::onBackPressed,
            onNext = viewModel::onSaveNickname,
            onRemove = viewModel::onRemoveClicked,
            onInputNicknameValidation = viewModel::onInputNicknameValidation
        )
    }
}

@Composable
private fun HandleDialogs(
    viewModel: DashboardViewModel,
    composeState: DashboardViewModel.ComposeState
) {
    DialogEventEffect(event = composeState.showRemoveDialog) {
        BulletDialog(
            title = stringResource(id = string.edit_account_nickname_remove_dialog_title),
            description = stringResource(
                id = string.edit_account_nickname_remove_dialog_description,
                composeState.originalNickname
            ),
            confirmBtnText = stringResource(id = string.edit_account_nickname_remove_dialog_confirm),
            dismissBtnText = stringResource(id = string.edit_account_nickname_remove_dialog_dismiss),
            onConfirm = viewModel::onConfirmRemove,
            onDismiss = viewModel::onDismissRemoveDialog
        )
    }
    DialogEventEffect(event = composeState.showEmptyNicknameDialog) {
        BulletDialog(
            title = stringResource(id = string.edit_account_nickname_empty_dialog_title),
            description = stringResource(id = string.edit_account_nickname_empty_dialog_description),
            confirmBtnText = stringResource(id = string.edit_account_nickname_empty_dialog_accept_btn),
            dismissBtnText = stringResource(id = string.edit_account_nickname_empty_dialog_back_btn),
            onConfirm = viewModel::onConfirmAcceptDefault,
            onDismiss = viewModel::onDismissEmptyNicknameDialog
        )
    }
    DialogEventEffect(event = composeState.showUnsavedChangesDialog) {
        BulletDialog(
            config = BulletDialog2Config(
                title = stringResource(id = string.edit_account_nickname_unsaved_dialog_title),
                description = stringResource(id = string.edit_account_nickname_unsaved_dialog_description),
                confirmButton = BulletDialog2Config.ButtonConfig(
                    text = stringResource(id = string.edit_account_nickname_unsaved_dialog_save_exit)
                ),
                neutralButton = BulletDialog2Config.ButtonConfig(
                    text = stringResource(id = string.edit_account_nickname_unsaved_dialog_cancel)
                ),
                dismissButton = BulletDialog2Config.ButtonConfig(
                    text = stringResource(id = string.edit_account_nickname_unsaved_dialog_exit),
                    buttonConfig = B8MainButtonConfig.DangerText
                )
            ),
            onConfirm = viewModel::onSaveAndExit,
            onNeutral = viewModel::onDismissUnsavedDialog,
            onDismiss = viewModel::onExitWithoutSaving
        )
    }
}
