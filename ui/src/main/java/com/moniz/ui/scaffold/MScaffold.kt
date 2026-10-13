package com.moniz.ui.scaffold

import android.os.Build

@Composable
fun MScaffold(
    modifier: Modifier = Modifier,
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    backgroundColor: Color = BulletTheme.colors.surfaceBG,
    overlayView: @Composable BoxScope.() -> Unit = {},
    showLoading: LoadingUiState? = null,
    toolbar: ToolbarUiState.Simple,
    mainMessageState: MainMessageUiState? = null,
    contentScrollState: ScrollState = rememberScrollState(),
    bottomContent: @Composable (ColumnScope.() -> Unit)? = null,
    useContentHorizontalPadding: Boolean = false,
    content: @Composable ColumnScope.(ScrollState) -> Unit
) {
    val titleHeight = rememberSaveable { mutableIntStateOf(0) }
    val titleScrollOffsetLimit = rememberTitleScrollOffsetLimit(titleHeight.intValue)

    val contentScrollOffset = rememberScrollOffset(contentScrollState)

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val blurMode = when {
        showCoachmark != null -> BlurMode.FullScreenBlur
        showLoading != null -> BlurMode.ContentBlur
        else -> BlurMode.None
    }

    LaunchedEffect(showLoading) {
        if (showLoading != null) keyboardController?.hide()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            modifier = modifier
                .semantics { testTagsAsResourceId = true }
                .conditional(
                    predicate = blurMode == BlurMode.FullScreenBlur && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S,
                    ifTrue = { blur(BulletTheme.dimensions.size3) }
                ),
            contentWindowInsets = WindowInsets.statusBars,
            scaffoldState = scaffoldState,
            backgroundColor = backgroundColor,
            topBar = {
                Toolbar(
                    state = toolbar,
                    isCollapsed = mainMessageState != null && (contentScrollOffset.intValue >= titleScrollOffsetLimit.intValue),
                    collapsedTitle = mainMessageState?.title ?: "",
                    canScrollBackward = contentScrollState.canScrollBackward
                )
            }
        ) {
            Box(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectTapGestures(onTap = { focusManager.clearFocus() })
                    },
                contentAlignment = Alignment.TopCenter
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .imePadding()
                        .conditional(
                            predicate = blurMode == BlurMode.ContentBlur && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S,
                            ifTrue = { blur(BulletTheme.dimensions.size3) }
                        )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .conditional(
                                predicate = useContentHorizontalPadding,
                                ifTrue = { padding(horizontal = BulletTheme.dimensions.size4) }
                            )
                            .verticalScroll(contentScrollState),
                        horizontalAlignment = Alignment.CenterHorizontally //FIXME: TO BE REMOVED
                    ) {
                        mainMessageState?.let { state ->
                            MainMessage(
                                state = state,
                                useHorizontalPadding = !useContentHorizontalPadding,
                                onHeightChanged = { height -> titleHeight.intValue = height }
                            )
                        }
                        content(contentScrollState)
                        if (bottomContent == null) {
                            BottomSpacer()
                        }
                    }
                    if (bottomContent != null) {
                        if (contentScrollState.canScrollForward) {
                            HorizontalDivider(
                                modifier = Modifier.fillMaxWidth(),
                                color = BulletTheme.colors.outlineLow,
                                thickness = 1.dp
                            )
                        }
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .conditional(
                                    predicate = useContentHorizontalPadding,
                                    ifTrue = { padding(horizontal = BulletTheme.dimensions.size4) }
                                ),
                            horizontalAlignment = Alignment.CenterHorizontally //FIXME: TO BE REMOVED
                        ) {
                            bottomContent()
                        }
                        BottomSpacer()
                    }
                }
                overlayView()
                showLoading?.let { Loading(inverted = it.isInverted, lottieAnimation = it.lottieAnimation) }
            }
        }
        showCoachmark?.let { B8Coachmark(state = it) }
    }
}
