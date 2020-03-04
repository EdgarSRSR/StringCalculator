package com.solrom.edgar.stringcalculator;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;


public class Calculator {

    private String operation;
    // карта приоритета оператора / Operator precedence map
    private Map<String, Integer> precedence;
    public Queue<String> suffixTemp;

    public Calculator(String operation) {
        this.operation = operation;
        initPrecedence();
    }

    public String getSuffix(){
        return String.valueOf(suffixTemp);
    }

    private void initPrecedence() {
        // эта хэш-карта содержит значения приоритета. " / " и "*" имеют вышний приоритет
        // this hash map contains the precedence values with "/" and "*" being the one with priority
        this.precedence = new HashMap<>();
        this.precedence.put("#", 0);
        this.precedence.put("+", 1);
        this.precedence.put("-", 1);
        this.precedence.put("*", 2);
        this.precedence.put("/", 2);
    }

    // gets the operator precedence
    // возвращает приоритет оператора
    public int getPrecedence(String operator) {

        // looks for brackets
        // возвращает скобки
        if (operator.matches("[()]")) {
            return -1;
        } else {
            return precedence.get(operator);
        }
    }

    //finding out the precedence
    //выяснение приоритета
    private boolean hasPrecedence(String a, String b) {
        return getPrecedence(a) <= getPrecedence(b);
    }

    //получить верхний элемент стека
    //get the top element of the stack
    private <T> T getTop(Stack<T> stack) {
        if (stack == null) {
            return null;
        } else {
            return stack.get(stack.size() - 1);
        }
    }


    //check input errors
    //проверьте, нет ли ошибок во входных данных
    private boolean checkSyntax(){
        int openingBracket = 0;
        int closingBracket = 0;
        String current = "";
        String currentLeft = "";
        String currentRight = "";
        // it decides how to deal with operations that begin in "(", "-", "+"
        //он решает, как бороться с операциями, которые начинаются c "(", "-", "+"
        int length0 = operation.length();
        for (int i= 0; i<length0; i++) {
            // gets the character at a certain position
            //получает символ в определенной позиции
            current = String.valueOf(operation.charAt(i));
            // if its not at the beginning, it finds the character on the left
            // если его нет в начале, находит символ слева
            if (i != 0){
                currentLeft = String.valueOf(operation.charAt(i-1));
            }
            // if "+" or "-" follow a "(" or are located in the beginning of the string
            // если "+" или "-" последуют "(" или находяться в начало сторки
            if (current.matches("[\\+\\-]") & (currentLeft.matches("\\(") | i == 0)) {
                if (i == 0) {
                    operation = "0" + operation;
                }
                else {
                    operation = operation.substring(0, i) + "0" + operation.substring(i);
                }
            }
        }
        int length1 = operation.length();
        for (int i = 0; i < length1; i++) {
            current = String.valueOf(operation.charAt(i));
            if (i != 0){
                // if its not at the beginning, it finds the character on the left
                // если его нет в начале, находит символ слева
                currentLeft = String.valueOf(operation.charAt(i-1));
            }
            // gets the character on the right
            // получает символ справа
            if (i != length1-1){

                currentRight = String.valueOf(operation.charAt(i+1));
            }
            // if it matches a "("
            // если "("
            if (current.matches("[\\(]")) {
                //return false if opening bracket contains numbers, decimal points, closing bracket on the left, or operators on the right or is at the end
                // возвращает false, если открывающая скобка содержит числа, десятичные точки, закрывающая скобка слева, операторы справа или находится в конце
                if (currentLeft.matches("[\\d\\.\\)]") | currentRight.matches("[\\+\\-\\*/]") | i == length1 - 1){
                    return false;
                }
                // increment openingBracket
                // увеличает openingBracket
                openingBracket++;
                // if it matches a "("
                // если "("
            } else if (current.matches("[\\)]")) {
                // return false if followed by closing bracket, operands, numbers or decimal or if it is at the beginning
                // возвращает false, если за ним следует закрывающая скобка, операнды, числа или десятичные дроби, или если находится в начале
                if (currentLeft.matches("[\\+\\-\\*/]") | currentRight.matches("[\\d\\.\\(]") | i == 0) {
                    return false;
                }
                //increment closingBracket
                // увеличает closingBracket
                closingBracket++;
                //return false if there is a decimal point at the end
                // возвращает false если десятичнaя дроба находится в конце
            } else if (current.matches("[\\.]") & i == length1-1) {
                return false;
            } else if (current.matches("[\\+\\-\\*/]")) {
                // return false if there are operators followed by operators, or operators at the end
                // возвращает false, если есть операторы, за которыми следуют операторы, или операторы в конце
                if (currentLeft.matches("[\\+\\-\\*/]") | currentRight.matches("[\\+\\-\\*/]") | i == length1 - 1 | length1 == 1){
                    return false;
                }
            }
        }
        // the number of opening and closing brackets should be the same or it returns false
        // количество открывающих и закрывающих скобок должно быть одинаковым или возвращает false
        return openingBracket == closingBracket;
    }

    // Converts to postfix expression using Shunting Yard algorithm
    // Преобразует в постфиксное выражение с помощью алгоритма Shunting Yard algorithm
    public Queue<String> toSuffix() {
        // creates values queue
        // создает очередь операндов
        Queue<String> operands = new LinkedList<>();
        if (!checkSyntax()) {
            // adds '#' to indicate a format error.
            // добавляет '#', чтобы указать на ошибку формата.
            operands.add("#");
            return operands;
        }
        // creates stack for operators
        // создает стек для операторов
        Stack<String> operatorStack = new Stack<>();
        operatorStack.push("#");

        String current = "";
        String operator = "";
        String number = "";
        int start = 0;
        int end = 0;
        for (int i = 0; i < operation.length(); i++) {
            // gets symbol
            // получает символ
            current = String.valueOf(operation.charAt(i));
            // If it is a number, end ++ is marked
            // Если это число, то end ++ увеличивается
            if (current.matches("[\\d\\.]")) {
                // If the number is the last character, put it directly on queue
                // Если число является последним символом, поместите его в очередь
                if (i == operation.length() - 1) {
                    current = String.valueOf(operation.substring(start, end+1));
                    // put a zero to decimal numbers before the point
                    // перед точкой ставим нуль в десятичные числа
                    operands.add("0" + current);
                } else {
                    end++;
                }
            } else {
                // If it is a character
                // Если это символ
                // If it is an opening parenthesis, push it to the stack
                // Если это открывающая скобка, поместите ее в стек
                if (current.equals("(")) {
                    operatorStack.push(current);
                } else {
                    // If it is a closing parenthesis and other operators, the previous number is first queued
                    // Если это закрывающая скобка и другие операторы, то предыдущее число сначала ставится в очередь
                    number = operation.substring(start, end);
                    if (!number.isEmpty()) {
                        // put a zero on the left of the number
                        // поставьте ноль слева от числа
                        operands.add("0" + number);
                    }
                    // If it is a right parenthesis, perform a pop operation and put the popped elements
                    // into the queue until the left parenthesis is popped out of the stack,
                    // and the left parenthesis is directly popped off the stack.
                    // Если это закрывающая скобка, выполнять операции Pop и поставить выскочил элементов в очереди,
                    // пока левая скобка выталкивается из стека, и левой круглой скобкой непосредственно извлеченный из стека.
                    if (current.equals(")")) {
                        while (!getTop(operatorStack).equals("(")) {
                            operands.add(operatorStack.pop());
                        }
                        operatorStack.pop();
                    } else {
                        // If it is another operator, pop all the top elements of the operator
                        // whose priority is greater than or equal to the operator, and then push
                        // the operator onto the stack
                        // Если это другой оператор, выделите все верхние элементы оператора,
                        // приоритет которых больше или равен оператору, а затем переместите оператор в стек
                        operator = current;
                        while (hasPrecedence(operator, getTop(operatorStack))) {
                            operands.add(operatorStack.pop());
                        }
                        operatorStack.push(operator);
                    }
                }
                // Increment the start and end pointers by one
                // Увеличьте start и end указатели на единицу
                start = i + 1;
                end = i + 1;
            }
        }
        // adds the operators to the queue of operands
        // добавляет операторы в очередь операндов
        for (int i = operatorStack.size() - 1; i > 0; i--) {
            operands.add(operatorStack.pop());
        }
        // returns the postfix expression
        // возвращает постфиксное выражение
        return operands;
    }

    // Calculate the result of an arithmetic expression
    // Вычислить результат арифметического выражения
    public String getResult() {
        // creates a queue for all the elements
        //  создает очередь для всех элементов
        Queue<String> suffixQueue = toSuffix();
        suffixTemp = new LinkedList<>(suffixQueue);
        // checks if there is an error
        // проверяет, нет ли ошибки
        if (suffixQueue.peek().equals("#")){
            return "mistake while typing operation";
        }
        // creates a stack
        // создает стек
        Stack<String> suffixStack = new Stack<String>();
        String current = "";
        BigDecimal frontOperand;
        BigDecimal backOperand;

        double resultValue = 0;
        // goes checking the elements of the queue
        // проверяет элементы очереди
        for (int i = suffixQueue.size(); i > 0; i--) {
            // gets element at top of the queue
            // возвращает элемент в начало очереди
            current = suffixQueue.poll();
            // If it is a number, push it on the stack
            // Если это число, нажмите его на стеке
            if (current.matches("^\\d+(\\.\\d+)*$")) {
                suffixStack.push(current);
            } else {
                // if its an operator it fetches the operands from the stack into two variables
                // если это оператор он извлекает операнды из стека в две переменные
                backOperand = BigDecimal.valueOf(Double.valueOf(suffixStack.pop()));
                frontOperand = BigDecimal.valueOf(Double.valueOf(suffixStack.pop()));
                if (current.equals("+")) {
                    // sum
                    // сумма
                    resultValue = frontOperand.add(backOperand).doubleValue();
                } else if (current.equals("-")) {
                    // substraction
                    // вычитание
                    resultValue = frontOperand.subtract(backOperand).doubleValue();
                } else if (current.equals("*")) {
                    // multiplication
                    // умножение
                    resultValue = frontOperand.multiply(backOperand).doubleValue();
                } else if (current.equals("/")) {
                    // division, but first checks that there is no division by zero
                    // деление, но сначала проверяет, что нет деления на ноль
                    if (backOperand.doubleValue() == 0) return "Division by zero is forbidden";
                    resultValue = frontOperand.divide(backOperand,10, BigDecimal.ROUND_HALF_UP).doubleValue();
                }
                // pushes result value to the stack
                // помещает результирующее значение в стек
                suffixStack.push(String.valueOf(resultValue));
            }
        }
        // at the end, the final results will be the only element in the stack
        // в конце концов конечные результаты будут единственным элементом в стеке
        return suffixStack.get(0);
    }

}