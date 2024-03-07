package com.hillal.calc


import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import java.lang.Double.parseDouble




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

        var Reset = findViewById<Button>(R.id.ac)

            Reset.setOnClickListener {
            screen.text=""
                Reset.text="AC"
        }

        findViewById<Button>(R.id.percentage).setOnClickListener{
            var temp = screen.text.toString()
            if (temp==""){
                screen.text ="percentage error!"
            }else{
                var c = parseDouble(temp)
                var v = c/100
                screen.text =v.toString()
            }

        }


        var addition = findViewById<Button>(R.id.plus)

            addition.setOnClickListener {

            var plusText = addition.text
             var currentText = screen.text
            screen.text = currentText.toString()+plusText

        }

//        findViewById<Button>(R.id.multiply)

        findViewById<Button>(R.id.equals).setOnClickListener{

            var currentText : String = screen.text.toString()
//            currentText = InfixToPostfix.infixToPostfix(currentText)
//            Log.i("MainActivity",currentText)



            try {
                // Specify the delimiters for split
                val delimiters = arrayOf('+', '-', 'x', '/')

                // Find the operator by checking the current text
                val operator = delimiters.find { currentText.contains(it) }

                if (operator != null) {
                    // Split the current text by the identified operator and get individual numbers
                    var numbers = currentText.split(operator).map { it.toInt() }.toIntArray()

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
                                var  result :Int = subtraction(*numbers)
                                screen.text = result.toString()
                            }
                        }
                        '/'->{
//                            if (numbers.size ==2){
                                val results : Double = divide(numbers[0].toDouble(),numbers[1].toDouble())
                                screen.text = results.toString()
//                            }else{
//                                screen.text="Oops! sorry"
//                            }
                        }
                        'x'->{
//                            if (numbers.size ==2){
                                var result : Int = multiply(numbers[0],numbers[1])
                                screen.text =result.toString()
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
                Reset.text ="C"
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
    val result:Double  =(parameterOne/parameterTwo).toDouble()
    return  result

}

fun multiply(parameterOne: Int,parameterTwo: Int):Int{
    return parameterOne*parameterTwo
}



