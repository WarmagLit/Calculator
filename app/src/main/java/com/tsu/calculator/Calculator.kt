package com.tsu.calculator

import androidx.compose.runtime.MutableState
import java.math.RoundingMode

class Calculator {
    private var previousNumber: Double = 0.0
    private var operation: MathOperation = MathOperation.EQUAL

    fun btnClicked(btn: Buttons, number: MutableState<String>) {
        when (btn) {
            Buttons.COMMA -> comma(number)
            Buttons.EQUAL -> equal(number)
            Buttons.PLUS -> mathOperation(number, MathOperation.PLUS)
            Buttons.MINUS -> mathOperation(number, MathOperation.MINUS)
            Buttons.MULTIPLY -> mathOperation(number, MathOperation.MULTIPLY)
            Buttons.DIVIDE -> mathOperation(number, MathOperation.DIVIDE)
            Buttons.AC -> clear(number)
            Buttons.PLUSMINUS -> plusminus(number)
            Buttons.PERCENT -> percent(number)
            else -> {
                if (btn >= Buttons.ZERO && btn <= Buttons.NINE) {
                    addSymbol(btn.symbol, number)
                }
            }
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
            MathOperation.PLUS -> number.value = (previousNumber + number.value.toDouble()).toString()
            MathOperation.MINUS -> number.value = (previousNumber - number.value.toDouble()).toString()
            MathOperation.DIVIDE -> number.value = (previousNumber / number.value.toDouble()).toString()
            MathOperation.MULTIPLY -> number.value = (previousNumber * number.value.toDouble()).toString()
            MathOperation.EQUAL -> return
        }
        val num = number.value.toDouble()
        if (number.value == "NaN" || num == Double.POSITIVE_INFINITY || num == Double.NEGATIVE_INFINITY) {
            return
        }
        outputControl(number)

    }

    private fun mathOperation(number: MutableState<String>, op: MathOperation) {
        if (number.value == "NaN" || number.value == "") return
        previousNumber = number.value.toDouble()
        operation = op
        number.value = ""
    }

    private fun clear(number: MutableState<String>) {
        number.value = ""
        previousNumber = 0.0
        operation = MathOperation.EQUAL
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
}