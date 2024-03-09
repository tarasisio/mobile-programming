package com.hillal.calc


import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import java.lang.Double.parseDouble
import java.lang.Integer.parseInt
import kotlin.math.sqrt


class MainActivity : AppCompatActivity() {

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        val postfix = InfixToPostfix()
        val screen = findViewById<TextView>(R.id.screen)
        val buttons = listOf<Button>(
            findViewById(R.id.Zero),
            findViewById(R.id.point),
            findViewById(R.id.one),
            findViewById(R.id.two),
            findViewById(R.id.three),
            findViewById(R.id.four),
//            findViewById(R.id.squareRoot),
//            findViewById(R.id.negate),
//            findViewById(R.id.delete),
//            findViewById(R.id.sine),
//            findViewById(R.id.cosine),
            findViewById(R.id.five),
            findViewById(R.id.six),
            findViewById(R.id.seven),
            findViewById(R.id.eigth),
            findViewById(R.id.nine),
            findViewById(R.id.leftBracket),
            findViewById(R.id.rigthBracket),
            findViewById(R.id.divide),
            findViewById(R.id.multiply),
            findViewById(R.id.minus),
        )

        val Reset = findViewById<Button>(R.id.ac)

        Reset.setOnClickListener {
            screen.text = ""
            Reset.text = "AC"
        }

        findViewById<Button>(R.id.percentage).setOnClickListener {
            val temp = screen.text.toString()
            if (temp == "") {
                screen.text = "percentage error!"
            } else {
                val c = parseDouble(temp)
                val v = c / 100
                screen.text = v.toString()
            }

        }

        val negate = findViewById<Button>(R.id.negate)
        if (negate != null) {
            negate.setOnClickListener {
                val current = screen.text.toString()
                val newcurrent = parseInt(current) * -1
                screen.text = newcurrent.toString()
            }
        }


        val addition = findViewById<Button>(R.id.plus)
        addition.setOnClickListener {
            val plusText = addition.text
            val currentText = screen.text
            screen.text = currentText.toString() + plusText

        }

        val Sine = findViewById<Button>(R.id.sine)
        if (Sine != null) {
            Sine.setOnClickListener {

                try {
                    val sineConversion = screen.text.toString()
                    screen.text = Math.sin(Math.toRadians(parseDouble(sineConversion))).toString()

                } catch (e: NumberFormatException) {
                    Log.e("MainActivity", "Error parsing number : $e{e.message")

                    screen.text = "Error"
                }

            }
        }


        findViewById<Button>(R.id.cosine)?.setOnClickListener {
            try {
                val cosineConversion = screen.text.toString()

                screen.text = Math.cos(Math.toRadians(parseDouble(cosineConversion))).toString()
            } catch (e: NumberFormatException) {
                Log.e("MainActivity", "Error parsing number : $e{e.message")

                screen.text = "Error"
            }
        }


        findViewById<Button>(R.id.squareRoot)?.setOnClickListener {
            val squareRoot = screen.text.toString()

            screen.text = sqrt(parseDouble(squareRoot)).toString()
        }


        val erase = findViewById<Button>(R.id.delete)


        erase?.setOnClickListener {
            val screenContent = screen.text.toString()

            if (screenContent.isNotEmpty()) {
                // Get the substring excluding the last character
                val newScreenContent = screenContent.substring(0, screenContent.length - 1)
                screen.text = newScreenContent
            }
        }


        findViewById<Button>(R.id.equals).setOnClickListener {

            val currentText: String = screen.text.toString()
//            currentText = InfixToPostfix.infixToPostfix(currentText)
//            Log.i("MainActivity",currentText)


            try {
                // Specify the delimiters for split
                val delimiters = arrayOf('+', '-', 'x', '/')

                // Find the operator by checking the current text
                val operator = delimiters.find { currentText.contains(it) }

                if (operator != null) {
                    // Split the current text by the identified operator and get individual numbers
                    val numbers = currentText.split(operator).map { it.toInt() }.toIntArray()

                    when (operator) {
                        '+' -> {
                            if (numbers.size == 2) {
                                // Perform addition using the add function if there are just two numbers

                                val result: Int = addTwo(numbers[0], numbers[1])
                                screen.text = result.toString()
                            } else {
                                // Perform addition for multiple numbers using addMany
                                val results: Int = addMany(*numbers)
                                screen.text = results.toString()
                            }
                        }

                        '-' -> {
                            if (numbers.size == 2) {
                                // Perform subtraction using the subtract function if there are just two numbers
                                val result: Int = subtract(numbers[0], numbers[1])
                                screen.text = result.toString()

                            } else {
                                // Handle the case where there are not enough numbers for subtraction
                                val result: Int = subtraction(*numbers)
                                screen.text = result.toString()
                            }
                        }

                        '/' -> {
//                            if (numbers.size ==2){
                            val results: Double =
                                divide(numbers[0].toDouble(), numbers[1].toDouble())
                            screen.text = results.toString()
//                            }else{
//                                screen.text="Oops! sorry"
//                            }
                        }

                        'x' -> {
//                            if (numbers.size ==2){
                            val result: Int = multiply(numbers[0], numbers[1])
                            screen.text = result.toString()
//                            }
                        }


                    }
                } else {
                    // Handle the case where no operator is found
                    Log.e("MainActivity", "No operator found in the input")
                }

            } catch (e: NumberFormatException) {
                // Handle the case where parsing fails
                Log.e("MainActivity", "Error parsing numbers: ${e.message}")
            } catch (e: IndexOutOfBoundsException) {
                // Handle the case where there are not enough numbers
                Log.e("MainActivity", "Not enough numbers for operation: ${e.message}")
            }
        }


        val buttonClickListener = { button: Button ->
            val buttonText = button.text
            val currentText = screen.text
            screen.text = currentText.toString() + buttonText


        }

        buttons.forEach { button ->
            button.setOnClickListener {
                Reset.text = "C"
                buttonClickListener.invoke(button)

            }
        }


    }


}


fun  addTwo (x: Int, y: Int): Int {
    return (x+y)
}

fun addMany(vararg numbers : Int): Int {
    var sum : Int = 0
        for (number in numbers) {
            sum += number
        }
    return sum
}

fun subtract(parameterOne: Int,parameterTwo: Int):Int{
    return parameterOne - parameterTwo
}

fun subtraction(vararg numbers : Int):Int{

    var result : Int=numbers[0]
    for (index in 1 until numbers.size){
        result -= numbers[index]
    }
    return  result
}

fun divide(parameterOne: Double,parameterTwo: Double):Double{
    val result:Double  =(parameterOne/parameterTwo)
    return  result

}

fun multiply(parameterOne: Int,parameterTwo: Int):Int{
    return parameterOne*parameterTwo
}



