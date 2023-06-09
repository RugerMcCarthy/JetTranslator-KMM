package ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.substring
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.bupt.jetdeepl.data.AllAvailableLanguages
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import model.MainViewModel
import model.SelectMode
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import ui.theme.extensionColors
import util.acquirePlatformContext
import util.copyToClipboard
import util.hideSoftKeyboard

@OptIn(ExperimentalResourceApi::class, ExperimentalMaterialApi::class)
@Composable
fun SettingsDrawer(viewModel: MainViewModel) {
    var passwordVisibility by remember { mutableStateOf(false) }
    val inputInteractionSource = remember { MutableInteractionSource() }
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(20.dp),
    ) {
        Text(
            text = "设置面板",
            fontSize = 25.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(top = 20.dp),
            color = MaterialTheme.extensionColors.inputTextColor
        )
        Spacer(modifier = Modifier.height(30.dp))
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(30.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "访问密码",
                    fontSize = 20.sp,
                    color = MaterialTheme.extensionColors.inputTextColor,
                )
                Row(
                    modifier = Modifier.fillMaxHeight(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            passwordVisibility = !passwordVisibility
                        },
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            painter = painterResource(
                                if (passwordVisibility) "ic_visible.xml" else "ic_none_visible.xml"
                            ),
                            contentDescription = "cancel",
                            tint = MaterialTheme.extensionColors.iconColor
                        )
                    }
                    Spacer(modifier = Modifier.width(5.dp))
                    BasicTextField(
                        value = viewModel.accessCode,
                        onValueChange = {
                            viewModel.accessCode = it.let {
                                if (it.length <= 5) {
                                    return@let it
                                }
                                it.substring(0, 5)
                            }
                        },
                        cursorBrush = SolidColor(MaterialTheme.extensionColors.inputTextColor),
                        modifier = Modifier
                            .width(70.dp)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(8.dp))
                            .border(
                                1.dp,
                                MaterialTheme.extensionColors.inputTextColor,
                                RoundedCornerShape(8.dp)
                            ),
                        textStyle = TextStyle(
                            color = MaterialTheme.extensionColors.inputTextColor,
                            textAlign = TextAlign.Center
                        ),
                        decorationBox = @Composable { innerTextField ->
                            TextFieldDefaults.TextFieldDecorationBox(
                                value = viewModel.accessCode,
                                visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                                innerTextField = innerTextField,
                                singleLine = true,
                                enabled = true,
                                colors = TextFieldDefaults.textFieldColors(
                                    backgroundColor = Color.Transparent,
                                    textColor = MaterialTheme.extensionColors.inputTextColor,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent
                                ),
                                interactionSource = inputInteractionSource,
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 3.dp)
                            )
                        },
                        visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation()
                    )
                }
            }
        }
    }
}

@OptIn(
    ExperimentalMaterialApi::class,
    ExperimentalFoundationApi::class,
    ExperimentalAnimationApi::class
)
@Composable
fun TranslateLayout(viewModel: MainViewModel, scaffoldState: ScaffoldState) {
    val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        sheetContent = {
            SelectLanguageSheet(sheetState, viewModel)
        }
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            SelectLanguageBar(sheetState, scaffoldState, viewModel)
            Surface(
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(30.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    InputBlock(viewModel, scaffoldState)
                    Divider(
                        color = MaterialTheme.extensionColors.inputTextColor,
                        thickness = 2.dp,
                        modifier = Modifier.padding(vertical = 10.dp)
                    )
                    OutputBlock(viewModel, scaffoldState)
                }
            }
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@ExperimentalAnimationApi
@Composable
fun ColumnScope.OutputBlock(viewModel: MainViewModel, scaffoldState: ScaffoldState) {
    val coroutineScope = rememberCoroutineScope()
    val platformContext = acquirePlatformContext()
    Box(
        modifier = Modifier
            .weight(0.6f)
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        hideSoftKeyboard(platformContext)
                    }
                )
            },
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Text(
                text = viewModel.displayOutput,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.8f)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.Transparent)
                    .padding(start = 20.dp, top = 20.dp),
                fontSize = 20.sp,
                fontWeight = FontWeight.W500,
                color = MaterialTheme.extensionColors.outputTextColor
            )
            if (viewModel.displayOutput.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.2f)
                        .fillMaxHeight()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(0.5f)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = {
                                viewModel.clearOutputDisplay()
                            }
                        ) {
                            Icon(
                                painter = painterResource("ic_cancel.xml"),
                                contentDescription = "cancel",
                                tint = MaterialTheme.extensionColors.iconColor
                            )
                        }
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(0.5f)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End
                    ) {
                        IconButton(
                            onClick = {
                                if (viewModel.displayOutput.isEmpty()) {
                                    coroutineScope.launch {
                                        scaffoldState.snackbarHostState.showSnackbar("待复制内容为空")
                                    }
                                    return@IconButton
                                } else {
                                    coroutineScope.launch {
                                        scaffoldState.snackbarHostState.showSnackbar("已复制到剪贴板")
                                    }
                                    copyToClipboard(platformContext, viewModel.displayOutput)
                                }
                            },
                            modifier = Modifier
                                .fillMaxHeight()
                        ) {
                            Icon(
                                painter = painterResource("ic_clipboard.xml"),
                                contentDescription = "clipboard",
                                tint = MaterialTheme.extensionColors.iconColor
                            )
                        }
                    }
                }
            } else {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.2f)
                        .fillMaxHeight()
                )
            }
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun ColumnScope.InputBlock(viewModel: MainViewModel, scaffoldState: ScaffoldState) {
    val coroutineScope = rememberCoroutineScope()
    val platformContext = acquirePlatformContext()
    Box(
        modifier = Modifier
            .weight(0.4f)
            .fillMaxSize()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            TextField(
                value = viewModel.displayInput,
                onValueChange = {
                    viewModel.displayInput = it
                },
                textStyle = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.W500
                ),
                placeholder = {
                    Text(
                        text = "随便输入点什么吧~",
                        color = MaterialTheme.extensionColors.inputHintColor,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.W900
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.8f)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(20.dp))
                    .border(1.dp, Color.White, RoundedCornerShape(20.dp)),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                    focusedIndicatorColor = Color.White,
                    unfocusedIndicatorColor = Color.White,
                    textColor = MaterialTheme.extensionColors.inputTextColor,
                    cursorColor = MaterialTheme.extensionColors.inputTextColor
                )
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.3f)
                    .fillMaxHeight()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(0.5f)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {}) {
                        Icon(
                            painter = painterResource("ic_volumn.xml"),
                            contentDescription = "volumn",
                            tint = MaterialTheme.extensionColors.iconColor
                        )
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(0.5f)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            viewModel.clearInputDisplay()
                        },
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(0.3f)
                            .fillMaxWidth()
                    ) {
                        Icon(
                            painter = painterResource("ic_undo.xml"),
                            contentDescription = "undo",
                            tint = MaterialTheme.extensionColors.iconColor
                        )
                    }
                    Spacer(modifier = Modifier.width(5.dp))
                    Button(
                        onClick = {
                            if (viewModel.accessCode.isEmpty()) {
                                coroutineScope.launch {
                                    scaffoldState.snackbarHostState.showSnackbar("访问密码还未设置～")
                                }
                            } else if (viewModel.displayInput.isEmpty()) {
                                coroutineScope.launch {
                                    scaffoldState.snackbarHostState.showSnackbar("输入内容不能为空哦～")
                                }
                            } else {
                                viewModel.clearOutputDisplay()
                                viewModel.translate(platformContext)
                            }
                        },
                        modifier = Modifier
                            .padding(vertical = 10.dp)
                            .fillMaxHeight()
                            .weight(0.7f)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp)),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = MaterialTheme.extensionColors.translateColor,
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            text = "翻译",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.W900
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@ExperimentalMaterialApi
@Composable
fun SelectLanguageBar(
    sheetState: ModalBottomSheetState,
    scaffoldState: ScaffoldState,
    viewModel: MainViewModel
) {
    val rotateAngle = remember { Animatable(0f) }
    val textAlpha = remember { Animatable(1f) }
    val scope = rememberCoroutineScope()
    val platformContext = acquirePlatformContext()
    scope.launch {
        viewModel.flipEventFlow.collect {
            launch {
                rotateAngle.animateTo(if (viewModel.flipToggle) 180f else 0f, tween(1000))
            }
            launch {
                textAlpha.animateTo(0f, tween(500))
                val tempLanguage = viewModel.displaySourceLanguage
                viewModel.displaySourceLanguage = viewModel.displayTargetLanguage
                viewModel.displayTargetLanguage = tempLanguage
                textAlpha.animateTo(1f, tween(500))
            }
        }
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(10.dp)
    ) {
        Button(
            onClick = {
                viewModel.changeSelectMode(SelectMode.SOURCE)
                scope.launch {
                    hideSoftKeyboard(platformContext)
                    sheetState.show()
                }
            },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colors.onBackground
            ),
            modifier = Modifier
                .width(150.dp)
                .fillMaxHeight()
                .clip(RoundedCornerShape(20.dp))
                .border(1.dp, Color.White, RoundedCornerShape(20.dp))
        ) {
            Text(
                text = viewModel.displaySourceLanguage,
                fontSize = 18.sp,
                fontWeight = FontWeight.W900,
                modifier = Modifier.alpha(textAlpha.value),
                color = MaterialTheme.extensionColors.toggleLangTextColor
            )
            Spacer(modifier = Modifier.width(2.dp))
            Icon(
                painter = painterResource("ic_down_arrow.xml"),
                contentDescription = "down_arrow",
                Modifier.size(15.dp),
                tint = MaterialTheme.extensionColors.toggleLangTextColor
            )
        }
        Box(
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .width(40.dp)
                .height(40.dp)
                .clip(RoundedCornerShape(50))
                .background(Color(0xff6bc6a5))
                .clickable {
                    if (viewModel.displaySourceLanguage == "自动检测") {
                        scope.launch {
                            scaffoldState.snackbarHostState.showSnackbar("源语言为自动检测时无法翻转")
                        }
                        return@clickable
                    }
                    viewModel.flipLanguage()
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource("ic_swap.xml"),
                contentDescription = "swap",
                tint = Color.White,
                modifier = Modifier
                    .padding(5.dp)
                    .rotate(rotateAngle.value)
            )
        }
        Button(
            onClick = {
                viewModel.changeSelectMode(SelectMode.TARGET)
                scope.launch {
                    hideSoftKeyboard(platformContext)
                    sheetState.show()
                }
            },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colors.onBackground
            ),
            modifier = Modifier
                .width(150.dp)
                .fillMaxHeight()
                .clip(RoundedCornerShape(20.dp))
                .border(1.dp, Color.White, RoundedCornerShape(20.dp))
        ) {
            Text(
                text = viewModel.displayTargetLanguage,
                fontSize = 18.sp,
                fontWeight = FontWeight.W900,
                modifier = Modifier.alpha(textAlpha.value),
                color = MaterialTheme.extensionColors.toggleLangTextColor
            )
            Spacer(modifier = Modifier.width(2.dp))
            Icon(
                painter = painterResource("ic_down_arrow.xml"),
                contentDescription = "down_arrow",
                Modifier.size(15.dp),
                tint = MaterialTheme.extensionColors.toggleLangTextColor
            )
        }
    }
}

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
fun SelectLanguageSheet(sheetState: ModalBottomSheetState, viewModel: MainViewModel) {
    Box(
        Modifier
            .fillMaxSize()
            .padding(top = 25.dp, start = 15.dp, end = 15.dp)
    ) {
        Column(Modifier.fillMaxWidth()) {
            val availableDisplayLanguageList = AllAvailableLanguages.filter {
                if (viewModel.currentSelectMode == SelectMode.SOURCE) {
                    return@filter it != viewModel.displayTargetLanguage
                } else {
                    return@filter it != viewModel.displaySourceLanguage && it != "自动检测"
                }
            }
            SearchLanguageField(viewModel, availableDisplayLanguageList)
            Text(
                text = "全部${availableDisplayLanguageList.size}种语言",
                color = MaterialTheme.extensionColors.selectLangTextColor,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)
            )
            LazyColumn(
                Modifier.fillMaxWidth(),
            ) {
                for (language in availableDisplayLanguageList) {
                    item {
                        SelectLanguageItem(sheetState, viewModel, language)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@ExperimentalMaterialApi
@Composable
fun SelectLanguageItem(sheetState: ModalBottomSheetState, viewModel: MainViewModel, lang: String) {
    val scope = rememberCoroutineScope()
    val platformContext = acquirePlatformContext()
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .clickable {
                viewModel.selectLanguage(lang)
                scope.launch {
                    hideSoftKeyboard(platformContext)
                    sheetState.hide()
                }
            }
    ) {
        Text(
            text = lang,
            color = if (viewModel.isSelectedLanguage(lang)) MaterialTheme.extensionColors.selectLangTextSpecificColor else MaterialTheme.extensionColors.selectLangTextColor,
            fontSize = 18.sp,
            fontWeight = FontWeight.W500,
            modifier = Modifier
                .weight(1f)
                .wrapContentWidth(Alignment.Start)
        )
        if (viewModel.isSelectedLanguage(lang)) {
            Icon(
                painter = painterResource("ic_selected.xml"),
                contentDescription = "selected",
                tint = MaterialTheme.extensionColors.selectLangTextSpecificColor,
                modifier = Modifier
                    .weight(1f)
                    .wrapContentWidth(Alignment.End)
            )
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun SearchLanguageField(viewModel: MainViewModel, availableDisplayLanguageList: List<String>) {
    var displayLanguageInput by remember { mutableStateOf("") }
    var displayLanguageList by remember {
        mutableStateOf(availableDisplayLanguageList)
    }
    TextField(
        value = displayLanguageInput,
        onValueChange = { value ->
            displayLanguageInput = value
            displayLanguageList = if (value.isEmpty()) {
                availableDisplayLanguageList
            } else {
                availableDisplayLanguageList.filter {
                    it.contains(value)
                }
            }
        },
        leadingIcon = {
            Icon(painter = painterResource("ic_search.xml"), contentDescription = "search")
        },
        trailingIcon = {
            if (viewModel.focusOnSearch) {
                IconButton(
                    onClick = {
                        displayLanguageInput = ""
                        displayLanguageList = availableDisplayLanguageList
                    }
                ) {
                    Icon(
                        painter = painterResource("ic_cancel.xml"),
                        contentDescription = "cancel",
                    )
                }
            }
        },
        placeholder = {
            Text(text = "搜索")
        },
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
        ),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .onFocusChanged {
                viewModel.focusOnSearch = it.isFocused
            }
    )
}
