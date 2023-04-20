package ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
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
import util.hideSoftKeyboard

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
                    Divider(color = MaterialTheme.extensionColors.translateColor, thickness = 2.dp)
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
    Box(
        modifier = Modifier
            .weight(0.6f)
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        hideSoftKeyboard()
                    }
                )
            }
            .padding(top = 20.dp),
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Text(
                text = viewModel.displayOutput,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.8f)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(20.dp))
                    .background(MaterialTheme.extensionColors.outputBackgroundColor)
                    .padding(start = 20.dp, top = 20.dp),
                fontSize = 20.sp,
                fontWeight = FontWeight.W500,
                color = MaterialTheme.extensionColors.outputTextColor
            )
            if (viewModel.displayOutput.isNotEmpty() && viewModel.isTranslatSuccess) {
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
//                        IconButton(
//                            onClick = {
//                                viewModel.clearOutputDisplay()
//                            },
//                            modifier = Modifier
//                                .fillMaxHeight()
//                        ) {
//                            Icon(
//                                painter = painterResource("ic_undo.xml"),
//                                contentDescription = "undo",
//                                tint = MaterialTheme.extensionColors.iconColor
//                            )
//                        }
//                        Spacer(modifier = Modifier.width(2.dp))
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
                                    viewModel.requestCopyToClipboard()
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
                    .clip(RoundedCornerShape(20.dp)),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = MaterialTheme.extensionColors.inputBackgroundColor,
                    focusedIndicatorColor = Color.White,
                    unfocusedIndicatorColor = Color.White,
                    textColor = MaterialTheme.extensionColors.inputTextColor
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
                            if (viewModel.displayInput.isEmpty()) {
                                coroutineScope.launch {
                                    scaffoldState.snackbarHostState.showSnackbar("输入内容不能为空哦～")
                                }
                            } else {
                                viewModel.clearOutputDisplay()
                                viewModel.translate()
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
                    hideSoftKeyboard()
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

        ) {
            Text(
                text = viewModel.displaySourceLanguage,
                fontSize = 18.sp,
                fontWeight = FontWeight.W900,
                modifier = Modifier.alpha(textAlpha.value),
                color = Color.Black
            )
            Spacer(modifier = Modifier.width(2.dp))
            Icon(
                painter = painterResource("ic_down_arrow.xml"),
                contentDescription = "down_arrow",
                Modifier.size(15.dp),
                tint = MaterialTheme.extensionColors.toggleLangIconColor
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
                    hideSoftKeyboard()
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
        ) {
            Text(
                text = viewModel.displayTargetLanguage,
                fontSize = 18.sp,
                fontWeight = FontWeight.W900,
                modifier = Modifier.alpha(textAlpha.value),
                color = Color.Black
            )
            Spacer(modifier = Modifier.width(2.dp))
            Icon(
                painter = painterResource("ic_down_arrow.xml"),
                contentDescription = "down_arrow",
                Modifier.size(15.dp),
                tint = MaterialTheme.extensionColors.toggleLangIconColor
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
            SearchLanguageField(viewModel)
            Text(
                text = "全部${viewModel.displayLanguageList.size}种语言",
                color = MaterialTheme.extensionColors.selectLangTextColor,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)
            )
            LazyColumn(
                Modifier.fillMaxWidth(),
            ) {
                for (language in viewModel.displayLanguageList) {
                    if (viewModel.currentSelectMode == SelectMode.TARGET && language == "自动检测") {
                        continue
                    }
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
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .clickable {
                viewModel.selectLanguage(lang)
                scope.launch {
                    hideSoftKeyboard()
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
fun SearchLanguageField(viewModel: MainViewModel) {
    var displayLanguageInput by remember { mutableStateOf("") }
    TextField(
        value = displayLanguageInput,
        onValueChange = { value ->
            displayLanguageInput = value
            viewModel.displayLanguageList = if (value.isEmpty()) {
                AllAvailableLanguages.keys.toList()
            } else {
                AllAvailableLanguages.keys.filter {
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
                        viewModel.displayLanguageList = AllAvailableLanguages.keys.toList()
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
            backgroundColor = MaterialTheme.extensionColors.searchLanguageColor,
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
