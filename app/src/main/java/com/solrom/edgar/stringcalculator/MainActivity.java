package com.solrom.edgar.stringcalculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    private Button zero,one,two,three,four,five,six,seven,eight,nine;
    private Button delete,clear,divide,multiply,minus,plus,equal,dot;
    private String operation = "";
    private String result = "";
    private TextView operationText;
    private TextView resultText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        // creates variables
        // создает переменные
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        operationText = (TextView) findViewById(R.id.operationText);
        resultText = (TextView) findViewById(R.id.resultText);

        // кнопки
        zero = (Button) findViewById(R.id.zero);
        one = (Button) findViewById(R.id.one);
        two = (Button) findViewById(R.id.two);
        three = (Button) findViewById(R.id.three);
        four = (Button) findViewById(R.id.four);
        five = (Button) findViewById(R.id.five);
        six = (Button) findViewById(R.id.six);
        seven = (Button) findViewById(R.id.seven);
        eight = (Button) findViewById(R.id.eight);
        nine = (Button) findViewById(R.id.nine);
        delete = (Button) findViewById(R.id.delete);
        clear = (Button) findViewById(R.id.clear);
        divide = (Button) findViewById(R.id.division);
        multiply = (Button) findViewById(R.id.multiplication);
        minus = (Button) findViewById(R.id.minus);
        plus = (Button) findViewById(R.id.plus);
        equal = (Button) findViewById(R.id.equal);
        dot = (Button) findViewById(R.id.dot);
    }

    // activates onclick functions of buttons
    // активирует onClick функции кнопок
    public void addOpeningBracket(View view){
        operation += "("; operationText.setText(operation);
    }
    public void addClosingBracket(View view){
        operation += ")"; operationText.setText(operation);
    }
    // delete button
    // кнопка Delete
    public void eraseString(View view){
        if (operation.length() != 0) {
            operation = operation.substring(0, operation.length() - 1);
        }
        operationText.setText(operation);
    }
    // clear button, clears all the operation
    // clear кнопка, очищает все операции
    public void clearString(View view){
        operation = "";
        operationText.setText(operation);
        result = "";
        resultText.setText(result);
    }

    // on click functions of the numbers
    // on Click функции чисел
    public void addSeven(View view){
        operation += "7";
        operationText.setText(operation);
    }
    public void addEight(View view){
        operation += "8";
        operationText.setText(operation);
    }
    public void addNine(View view){
        operation += "9";
        operationText.setText(operation);
    }
    public void addDivision(View view){
        operation += "/";
        operationText.setText(operation);
    }
    public void addFour(View view){
        operation += "4";
        operationText.setText(operation);
    }
    public void addFive(View view){
        operation += "5";
        operationText.setText(operation);
    }
    public void addSix(View view){
        operation += "6";
        operationText.setText(operation);
    }
    public void addMultiplication(View view){
        operation += "*";
        operationText.setText(operation);
    }
    public void addOne(View view){
        operation += "1";
        operationText.setText(operation);
    }
    public void addTwo(View view){
        operation += "2";
        operationText.setText(operation);
    }
    public void addThree(View view){
        operation += "3";
        operationText.setText(operation);
    }
    public void addMinus(View view){
        operation += "-";
        operationText.setText(operation);
    }
    public void addDot(View view){
        operation += ".";
        operationText.setText(operation);
    }
    public void addZero(View view){
        operation += "0";
        operationText.setText(operation);
    }

    // asks for results
    // запрашивает результаты
    public void calculateResult(View view){
        if (operation.length() != 0) {
            Calculator cal = new Calculator(operation);
            result = cal.getResult();
            resultText.setText(result);
        }
    }

    // enters a "+" sign
    // доблавяет симбол "+"
    public void addPlus(View view){
        operation += "+";
        operationText.setText(operation);
    }
}
