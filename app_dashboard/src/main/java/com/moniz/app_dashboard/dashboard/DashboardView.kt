package com.moniz.app_dashboard.dashboard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource

@Composable
internal fun EditAccountNicknameView(
    state: DashboardViewModel.ComposeState,
    nicknameTextState: TextFieldState,
    onClose: () -> Unit,
    onNext: () -> Unit,
    onRemove: () -> Unit,
    onInputNicknameValidation: (Boolean) -> Unit
) {
    SimpleScaffold(
        toolbar = ToolbarUiState.Simple(
            navigationButton = ToolbarButtonUiState.ToolbarButtonIconUiState(
                testTag = testTag(TestTagsConfig.BACK_BTN),
                icon = drawable.ic_retroceder_24px,
                description = "",
                onClick = onClose
            )
        ),
        mainMessageState = MainMessageUiState(
            title = stringResource(string.edit_account_nickname_title),
            message = stringResource(string.edit_account_nickname_subtitle)
        ),
        useContentHorizontalPadding = true,
        showLoading = if (state.viewState is ViewState.Loading) LoadingUiState() else null
    ) {
        Input(
            modifier = Modifier
                .padding(top = BulletTheme.dimensions.size4)
                .fillMaxWidth(),
            state = InputUiState.Text(
                testTag = testTag(TestTagsConfig.INPUT),
                config = InputTextConfig(
                    label = stringResource(id = string.edit_account_nickname_input_label),
                    format = InputTextConfig.Format.LETTERS_DIGITS_DASH_UNDERSCORE,
                    counter = InputTextConfig.Counter.Limit.Max(maxLength = NICKNAME_MAX_SIZE),
                    mandatory = InputTextConfig.Mandatory.OPTIONAL,
                    supportingText = ""
                ),
                textState = nicknameTextState,
                validationPassed = onInputNicknameValidation
            )
        )
        Spacer(modifier = Modifier.weight(1f))
        B8MainButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = BulletTheme.dimensions.size5),
            state = B8MainButtonUiState(
                testTag = testTag(TestTagsConfig.MAIN_PRIMARY_BTN),
                text = stringResource(id = string.edit_account_nickname_save_btn),
                buttonConfig = B8MainButtonConfig.Primary,
                onClick = onNext
            )
        )
        if (state.showRemoveBtn) {
            B8MainButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = BulletTheme.dimensions.size5),
                state = B8MainButtonUiState(
                    testTag = testTag(TestTagsConfig.MAIN_SECONDARY_BTN),
                    text = stringResource(id = string.edit_account_nickname_remove_btn),
                    buttonConfig = B8MainButtonConfig.DangerText,
                    onClick = onRemove
                )
            )
        }
    }
}

@BulletPreview
@Composable
private fun EditAccountNicknameViewPreview() {
    BulletTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            EditAccountNicknameView(
                state = DashboardViewModel.ComposeState(
                    viewState = ViewState.Input
                ),
                nicknameTextState = TextFieldState(),
                onClose = {},
                onNext = {},
                onRemove = {},
                onInputNicknameValidation = {}
            )
        }
    }
}

@BulletPreview
@Composable
private fun EditAccountNicknameFilledPreview() {
    BulletTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            EditAccountNicknameView(
                state = DashboardViewModel.ComposeState(
                    viewState = ViewState.Input
                ),
                nicknameTextState = TextFieldState("Conta Principal"),
                onClose = {},
                onNext = {},
                onRemove = {},
                onInputNicknameValidation = {}
            )
        }
    }
}

@BulletPreview
@Composable
private fun EditAccountNicknameWithRemovePreview() {
    BulletTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            EditAccountNicknameView(
                state = DashboardViewModel.ComposeState(
                    viewState = ViewState.Input,
                    originalNickname = "Conta Principal",
                    showRemoveBtn = true
                ),
                nicknameTextState = TextFieldState("Conta Principal"),
                onClose = {},
                onNext = {},
                onRemove = {},
                onInputNicknameValidation = {}
            )
        }
    }
}
