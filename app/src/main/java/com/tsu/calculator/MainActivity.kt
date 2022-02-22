package com.tsu.calculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tsu.calculator.ui.theme.*

class MainActivity : ComponentActivity() {

    private var calc = Calculator()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalculatorTheme {
                // A surface container using the 'background' color from the theme
                MainSurface()
            }
        }
    }

    @Composable
    fun MainSurface() {
        val num = remember { mutableStateOf("") }
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                Row {
                    Title()
                }
                Row(horizontalArrangement = Arrangement.Center) {
                    InputPanel(num)
                }
                ButtonRow(
                    num = num,
                    btn1 = Buttons.AC,
                    btn2 = Buttons.PLUSMINUS,
                    btn3 = Buttons.PERCENT,
                    btnBlue = Buttons.DIVIDE
                )
                ButtonRow(
                    num = num,
                    btn1 = Buttons.SEVEN,
                    btn2 = Buttons.EIGHT,
                    btn3 = Buttons.NINE,
                    btnBlue = Buttons.MULTIPLY
                )
                ButtonRow(
                    num = num,
                    btn1 = Buttons.FOUR,
                    btn2 = Buttons.FIVE,
                    btn3 = Buttons.SIX,
                    btnBlue = Buttons.MINUS
                )
                ButtonRow(
                    num = num,
                    btn1 = Buttons.ONE,
                    btn2 = Buttons.TWO,
                    btn3 = Buttons.THREE,
                    btnBlue = Buttons.PLUS
                )
                Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                    WideButton(num)
                    Spacer(modifier = Modifier.width(dimensionResource(R.dimen.btn_spacer)))
                    WhiteButton(num, Buttons.COMMA)
                    Spacer(modifier = Modifier.width(dimensionResource(R.dimen.btn_spacer)))
                    BlueButton(num, Buttons.EQUAL)
                }
            }
        }
    }

    @Composable
    fun ButtonRow(
        num: MutableState<String>,
        btn1: Buttons,
        btn2: Buttons,
        btn3: Buttons,
        btnBlue: Buttons
    ) {
        Row {
            WhiteButton(num, btn1)
            Spacer(modifier = Modifier.width(dimensionResource(R.dimen.btn_spacer)))
            WhiteButton(num, btn2)
            Spacer(modifier = Modifier.width(dimensionResource(R.dimen.btn_spacer)))
            WhiteButton(num, btn3)
            Spacer(modifier = Modifier.width(dimensionResource(R.dimen.btn_spacer)))
            BlueButton(num, btnBlue)
        }
    }

    @Composable
    fun Title() {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp, 0.dp),
            text = stringResource(R.string.calculator),
            fontSize = 28.sp,
            color = ClassicNum,
            textAlign = TextAlign.Left,
            fontFamily = FontFamily(Font(R.font.museo_regular_500))
        )
    }


    @Composable
    fun InputPanel(number: MutableState<String>) {
        Card(border = null) {
            Image(
                painter = painterResource(id = R.drawable.ic_input_background),
                contentDescription = null
            )
            Text(
                modifier = Modifier.padding(20.dp),
                textAlign = TextAlign.Right,
                text = stringResource(R.string.default_background),
                color = TransparentNum,
                fontSize = 40.sp,
                fontFamily = FontFamily(Font(R.font.digitalnumbers_regular))
            )
            Text(
                modifier = Modifier.padding(20.dp),
                text = number.value,
                color = ClassicNum,
                fontSize = 40.sp,
                fontFamily = FontFamily(Font(R.font.digitalnumbers_regular))
            )

        }
    }

    @Composable
    fun WhiteButton(num: MutableState<String>, btn: Buttons = Buttons.MULTIPLY) {
        CalcButton(num, backgroundColor = WhiteIce, contentColor = EveningBlue, btn)
    }

    @Composable
    fun BlueButton(num: MutableState<String>, btn: Buttons = Buttons.MULTIPLY) {
        CalcButton(num, backgroundColor = EveningBlue, contentColor = WhiteIce, btn)
    }

    @Composable
    fun WideButton(num: MutableState<String>, btn: Buttons = Buttons.ZERO) {
        CalcButton(num, backgroundColor = WhiteIce, contentColor = EveningBlue, btn, 175)
    }

    @Composable
    fun CalcButton(
        num: MutableState<String>,
        backgroundColor: Color,
        contentColor: Color,
        btn: Buttons = Buttons.MULTIPLY,
        width: Int = 80
    ) {
        TextButton(
            colors = ButtonDefaults.textButtonColors(
                backgroundColor = backgroundColor,
                contentColor = contentColor,
            ),

            modifier = Modifier
                .width(width.dp)
                .height(80.dp)
                .drawColoredShadow(White, 0.99f, 20.dp, 10.dp, (-5).dp, (-5).dp)
                .drawColoredShadow(ShadowBlue, 0.2f, 20.dp, 10.dp, 5.dp, 5.dp),
            shape = Shapes.large,
            onClick = {
                calc.btnClicked(btn, num)
            }
        ) {
            Text(
                btn.symbol,
                fontSize = 29.sp,
                fontFamily = FontFamily(Font(R.font.monsserat_bold))
            )
        }
    }

    private fun Modifier.drawColoredShadow(
        color: Color,
        alpha: Float = 0.2f,
        borderRadius: Dp = 0.dp,
        blurRadius: Dp = 20.dp,
        offsetY: Dp = 0.dp,
        offsetX: Dp = 0.dp
    ) = this.drawBehind {
        val transparentColor =
            android.graphics.Color.toArgb(color.copy(alpha = 0.0f).value.toLong())
        val shadowColor =
            android.graphics.Color.toArgb(color.copy(alpha = alpha).value.toLong())
        this.drawIntoCanvas {
            val paint = Paint()
            val frameworkPaint = paint.asFrameworkPaint()
            frameworkPaint.color = transparentColor
            frameworkPaint.setShadowLayer(
                blurRadius.toPx(),
                offsetX.toPx(),
                offsetY.toPx(),
                shadowColor
            )
            it.drawRoundRect(
                0f,
                0f,
                this.size.width,
                this.size.height,
                borderRadius.toPx(),
                borderRadius.toPx(),
                paint
            )
        }
    }
}
