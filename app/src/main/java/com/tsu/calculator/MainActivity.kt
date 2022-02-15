package com.tsu.calculator

import android.os.Bundle
import android.util.Log
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tsu.calculator.ui.theme.*
import java.math.RoundingMode

class MainActivity : ComponentActivity() {

    private var previousNumber: Double = 0.0
    private var operation: Operation = Operation.EQUAL

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalculatorTheme {
                // A surface container using the 'background' color from the theme
                MainSurface()
            }
        }
    }

    private fun btnClicked(btn: Buttons, number: MutableState<String>) {
        when (btn) {
            Buttons.ZERO -> addSymbol("0", number)
            Buttons.ONE -> addSymbol("1", number)
            Buttons.TWO -> addSymbol("2", number)
            Buttons.THREE -> addSymbol("3", number)
            Buttons.FOUR -> addSymbol("4", number)
            Buttons.FIVE -> addSymbol("5", number)
            Buttons.SIX -> addSymbol("6", number)
            Buttons.SEVEN -> addSymbol("7", number)
            Buttons.EIGHT -> addSymbol("8", number)
            Buttons.NINE -> addSymbol("9", number)

            Buttons.COMMA -> comma(number)
            Buttons.EQUAL -> equal(number)
            Buttons.PLUS -> plus(number)
            Buttons.MINUS -> minus(number)
            Buttons.MULTIPLY -> multiply(number)
            Buttons.DIVIDE -> divide(number)
            Buttons.AC -> clear(number)
            Buttons.PLUSMINUS -> plusminus(number)
            Buttons.PERCENT -> percent(number)
        }
    }

    private fun addSymbol(digit: String, number: MutableState<String>) {
        if (number.value == "Infinity") {
            number.value = ""
        }
        number.value += digit
    }

    private fun equal(number: MutableState<String>) {
        if (number.value == "") return
        when (operation) {
            Operation.PLUS -> number.value = (previousNumber + number.value.toDouble()).toString()
            Operation.MINUS -> number.value = (previousNumber - number.value.toDouble()).toString()
            Operation.DIVIDE -> number.value = (previousNumber / number.value.toDouble()).toString()
            Operation.MULTIPLY -> number.value = (previousNumber * number.value.toDouble()).toString()
        }
        val num = number.value.toDouble()
        if (number.value == "NaN" || num == Double.POSITIVE_INFINITY || num == Double.NEGATIVE_INFINITY) {
            return
        }
        outputControl(number)

        Log.d("Double", number.value.toDouble().toString())
    }

    private fun plus(number: MutableState<String>) {
        if (number.value == "NaN" || number.value == "") return
        previousNumber = number.value.toDouble()
        operation = Operation.PLUS
        number.value = ""
    }

    private fun minus(number: MutableState<String>) {
        if (number.value == "NaN" || number.value == "") return
        previousNumber = number.value.toDouble()
        operation = Operation.MINUS
        number.value = ""
    }

    private fun divide(number: MutableState<String>) {
        if (number.value == "NaN" || number.value == "") return
        previousNumber = number.value.toDouble()
        operation = Operation.DIVIDE
        number.value = ""
    }

    private fun multiply(number: MutableState<String>) {
        if (number.value == "NaN" || number.value == "") return
        previousNumber = number.value.toDouble()
        operation = Operation.MULTIPLY
        number.value = ""
    }

    private fun clear(number: MutableState<String>) {
        number.value = ""
        previousNumber = 0.0
        operation = Operation.EQUAL
    }

    private fun comma(number: MutableState<String>) {
        val num = number.value.toDouble()
        if (num % 1.0 == 0.0) {
            number.value += "."
        }
    }

    private fun percent(number: MutableState<String>) {
        if (number.value == "NaN" || number.value == "") return
        previousNumber = number.value.toDouble()
        operation = Operation.PERCENT
        number.value = (previousNumber / 100).toString()
    }

    private fun plusminus(number: MutableState<String>) {
        if (number.value == "NaN" || number.value == "") return

        number.value = (number.value.toDouble() * (-1.0)).toString()
        outputControl(number)
    }

    private fun outputControl(number: MutableState<String>) {
        val num = number.value.toDouble()
        if (num % 1.0 == 0.0) {
            number.value = num.toInt().toString()
        } else {
            val integerLength = num.toInt().toString().length
            val roundedUp = num.toBigDecimal().setScale(9 - integerLength, RoundingMode.UP).toDouble()
            number.value = roundedUp.toString()
        }
    }

    @Preview()
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
                Row {
                    WhiteButton(num, Buttons.AC)
                    Spacer(modifier = Modifier.width(15.dp))
                    WhiteButton(num,Buttons.PLUSMINUS)
                    Spacer(modifier = Modifier.width(15.dp))
                    WhiteButton(num,Buttons.PERCENT)
                    Spacer(modifier = Modifier.width(15.dp))
                    BlueButton(num,Buttons.DIVIDE)
                }
                Row {
                    WhiteButton(num,Buttons.SEVEN)
                    Spacer(modifier = Modifier.width(15.dp))
                    WhiteButton(num,Buttons.EIGHT)
                    Spacer(modifier = Modifier.width(15.dp))
                    WhiteButton(num,Buttons.NINE)
                    Spacer(modifier = Modifier.width(15.dp))
                    BlueButton(num,Buttons.MULTIPLY)
                }
                Row {
                    WhiteButton(num,Buttons.FOUR)
                    Spacer(modifier = Modifier.width(15.dp))
                    WhiteButton(num,Buttons.FIVE)
                    Spacer(modifier = Modifier.width(15.dp))
                    WhiteButton(num,Buttons.SIX)
                    Spacer(modifier = Modifier.width(15.dp))
                    BlueButton(num,Buttons.MINUS)
                }
                Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                    WhiteButton(num,Buttons.ONE)
                    Spacer(modifier = Modifier.width(15.dp))
                    WhiteButton(num,Buttons.TWO)
                    Spacer(modifier = Modifier.width(15.dp))
                    WhiteButton(num,Buttons.THREE)
                    Spacer(modifier = Modifier.width(15.dp))
                    BlueButton(num,Buttons.PLUS)
                }
                Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                    WideButton(num)
                    Spacer(modifier = Modifier.width(15.dp))
                    WhiteButton(num,Buttons.COMMA)
                    Spacer(modifier = Modifier.width(15.dp))
                    BlueButton(num, Buttons.EQUAL)
                }
            }
        }
    }

    @Composable
    fun Title() {
        Text(
            text = "Calculator",
            fontSize = 28.sp,
            color = ClassicNum,
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
                text = "8888888888",
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
        val context = LocalContext.current
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
                btnClicked(btn, num)
                //Toast.makeText(context, "Clicked on Button", Toast.LENGTH_SHORT).show()
            }
        ) {
            Text(btn.symbol, fontSize = 29.sp, fontFamily = FontFamily(Font(R.font.monsserat_bold)))
        }
    }

    fun Modifier.drawColoredShadow(
        color: Color,
        alpha: Float = 0.2f,
        borderRadius: Dp = 0.dp,
        blurRadius: Dp = 20.dp,
        offsetY: Dp = 0.dp,
        offsetX: Dp = 0.dp
    ) = this.drawBehind {
        val transparentColor =
            android.graphics.Color.toArgb(color.copy(alpha = 0.0f).value.toLong())
        val shadowColor = android.graphics.Color.toArgb(color.copy(alpha = alpha).value.toLong())
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
