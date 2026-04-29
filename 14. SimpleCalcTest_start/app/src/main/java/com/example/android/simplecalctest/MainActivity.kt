/*
 * Copyright 2018, Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.simplecalctest

import android.app.Activity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.core.view.WindowCompat
import com.example.android.simplecalctest.Calculator.Operator

/**
 * Source:
 * https://developer.android.com/codelabs/android-training-unit-tests#0
 * https://github.com/google-developer-training/android-fundamentals-apps-v2/tree/master/SimpleCalcTest
 */
class MainActivity : Activity() {
    private lateinit var mCalculator: Calculator
    private lateinit var mOperandOneEditText: EditText
    private lateinit var mOperandTwoEditText: EditText
    private lateinit var mResultTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.enableEdgeToEdge(window)
        setContentView(R.layout.activity_main)

        // Initialize the calculator class and all the views.
        mCalculator = Calculator()
        mOperandOneEditText = findViewById(R.id.operand_one_edit_text)
        mOperandTwoEditText = findViewById(R.id.operand_two_edit_text)
        mResultTextView = findViewById(R.id.operation_result_text_view)
    }

    /**
     * OnClick method called when the add Button is pressed.
     */
    fun onAdd(view: View?) {
        compute(Operator.ADD)
    }

    /**
     * OnClick method called when the subtract Button is pressed.
     */
    fun onSub(view: View?) {
        compute(Operator.SUB)
    }

    /**
     * OnClick method called when the divide Button is pressed.
     */
    fun onDiv(view: View?) {
        compute(Operator.DIV)
    }

    /**
     * OnClick method called when the multiply Button is pressed.
     */
    fun onMul(view: View?) {
        compute(Operator.MUL)
    }

    /**
     * @return the operand value entered in an EditText as double.
     */
    internal fun getOperand(operandEditText: EditText): Double {
        val operandText = getOperandText(operandEditText)
        // A NumberFormatException is thrown by toDouble()
        // if the string is not a valid representation of a Double.
        return operandText.toDouble()
    }

    /**
     * @return the operand text which was entered in an EditText.
     */
    private fun getOperandText(operandEditText: EditText): String {
        return operandEditText.text.toString()
    }

    private fun compute(operator: Operator) {
        val operandOne: Double
        val operandTwo: Double
        try {
            operandOne = getOperand(mOperandOneEditText)
            operandTwo = getOperand(mOperandTwoEditText)
        } catch (nfe: NumberFormatException) {
            Log.e(TAG, "NumberFormatException", nfe)
            mResultTextView.text = getString(R.string.computationError)
            return
        }

        mResultTextView.text =
            when (operator) {
                Operator.ADD -> mCalculator.add(operandOne, operandTwo).toString()
                Operator.SUB -> mCalculator.sub(operandOne, operandTwo).toString()
                Operator.DIV -> mCalculator.div(operandOne, operandTwo).toString()
                Operator.MUL -> mCalculator.mul(operandOne, operandTwo).toString()
            }
    }

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }
}
