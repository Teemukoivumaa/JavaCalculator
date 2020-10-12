package calculator;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Controller {
    public TextField input;
    public String text;
    public Label answer;
    public Button equalsButton;

    public void addMark(ActionEvent event) {
        String temp = event.toString();
        String mark = temp.substring(42, temp.length() - 24);
        text = input.getText();

        switch (mark) {
            case "zero":    input.setText(text + "0");      break;
            case "one":     input.setText(text + "1");      break;
            case "two":     input.setText(text + "2");      break;
            case "three":   input.setText(text + "3");      break;
            case "four":    input.setText(text + "4");      break;
            case "five":    input.setText(text + "5");      break;
            case "six":     input.setText(text + "6");      break;
            case "seven":   input.setText(text + "7");      break;
            case "eight":   input.setText(text + "8");      break;
            case "nine":    input.setText(text + "9");      break;
            case "plus":    input.setText(text + "+");      break;
            case "minus":   input.setText(text + "-");      break;
            case "mult":    input.setText(text + "*");      break;
            case "div":     input.setText(text + "/");      break;
            case "dot":     input.setText(text + ".");      break;
            case "clear":   input.clear();                  break;
            default:
                System.out.println("Invalid input.");
                break;
        }
    }

    public void erase(ActionEvent actionEvent) {
        text = input.getText();
        if (text.length() > 0) {
            input.setText(text.substring(0, text.length() - 1));
        }
    }

    public void equals(ActionEvent actionEvent) {
        text = input.getText();

        if (text.length() > 0) {
            List<String> calculation = new ArrayList<>();
            List<String> inputCalculation = Arrays.asList(input.getText().trim().split(""));
            Boolean isCalculationOk = false;
            String prevValue = "", mark = "";
            int numbers = 0;

            if (inputCalculation.size() > 2) {
                isCalculationOk = true;
                for (int i = 0; i < inputCalculation.size(); i++) { // get calculation to format [-32, +, 30]
                    boolean isNumber = inputCalculation.get(i).matches("[-+]?\\d*\\.?\\d+"); // check if contains numbers
                    String newValue = inputCalculation.get(i);

                    if (isNumber) {
                        numbers++; // count numbers
                        mark = mark + newValue;
                        prevValue = newValue;
                    } else {
                        if (newValue.equals(".") && !prevValue.equals(".")) {
                            mark = mark + newValue;
                            prevValue = newValue;
                        } else if (newValue.equals("-") && mark.equals("")) { // if mark is empty next value is negative
                            mark = newValue;
                            prevValue = newValue;
                        } else {
                            if (prevValue.equals("-") || prevValue.equals("+") || prevValue.equals("/") || prevValue.equals("*")) {
                                i = inputCalculation.size();
                                isCalculationOk = false;
                            } else {
                                calculation.add(mark); // add numbers
                                mark = newValue;
                                calculation.add(mark); // add +,-,*,/
                                mark = "";
                            }
                        }
                    }
                }
            }

            if (numbers > 1) {
                if (isCalculationOk) {
                    calculation.add(mark); // need to add last one here for some reason
                    Calculation(calculation);
                } else {
                    System.out.println("Invalid syntax. Check calculation");
                    answer.setText("Invalid syntax. Check calculation");
                }
            } else {
                System.out.println("Invalid syntax. Need two numbers");
                answer.setText("Invalid syntax. Need two numbers");
            }
        } else {
            System.out.println("No input.");
            answer.setText("No input.");
        }
    }

    public void Calculation(List<String> calculation) {
        boolean calculating = true;
        while (calculating) {
            System.out.println("Calculation: " + calculation);
            int operatorLocation = 0;
            double result = 0;

            if (calculation.size() > 1) { // if calculation contains only 1 value it means it's the result
                List<String> operators = new ArrayList<>();
                List<String> operatorLocations = new ArrayList<>();
                for (int i=0; i < calculation.size(); i++) { // get operators and their locations on calculation
                    boolean isNumber = calculation.get(i).matches("[-+]?\\d*\\.?\\d+");
                    if (!isNumber) {
                        operators.add(calculation.get(i));
                        operatorLocations.add(String.valueOf(i));
                    }
                }

                String wantedOperator;
                String wantedSecondOperator;
                Locations locations = new Locations();

                if (operators.contains("*") || operators.contains("/")) {
                    wantedOperator = "*"; wantedSecondOperator = "/";
                    for (int i=0; i < operators.size(); i++) {
                        String operator = operators.get(i);
                        operatorLocation = Integer.parseInt(operatorLocations.get(i)); // get operator location
                        if (operator.equals(wantedOperator)) { // multiplication operation
                            locations.GetLocations(calculation, operatorLocation); // get value locations

                            result = BasicCalculation(locations.prevValue, locations.nextValue, operator); // calculate
                            calculation = CreateNewCalculation(calculation, locations.prevValueLocation, operatorLocation, locations.nextValueLocation, result); // make new calculation

                            i = operators.size(); // exits for loop
                        } else if (operator.equals(wantedSecondOperator)) { // division operation
                            locations.GetLocations(calculation, operatorLocation); // get value locations

                            result = BasicCalculation(locations.prevValue, locations.nextValue, operator); // calculate
                            calculation = CreateNewCalculation(calculation, locations.prevValueLocation, operatorLocation, locations.nextValueLocation, result); // make new calculation

                            i = operators.size(); // exits for loop
                        }
                    }
                } else if (operators.contains("+") || operators.contains("-")) {
                    wantedOperator = "+"; wantedSecondOperator = "-";
                    for (int i=0; i < operators.size(); i++) {
                        String operator = operators.get(i);
                        operatorLocation = Integer.parseInt(operatorLocations.get(i)); // get operator location
                        if (operator.equals(wantedOperator)) { // plus operation
                            locations.GetLocations(calculation, operatorLocation); // get value locations

                            result = BasicCalculation(locations.prevValue, locations.nextValue, operator); // calculate
                            calculation = CreateNewCalculation(calculation, locations.prevValueLocation, operatorLocation, locations.nextValueLocation, result); // make new calculation

                            i = operators.size(); // exits for loop
                        } else if (operator.equals(wantedSecondOperator)) { // minus operation
                            locations.GetLocations(calculation, operatorLocation); // get value locations

                            result = BasicCalculation(locations.prevValue, locations.nextValue, operator); // calculate
                            calculation = CreateNewCalculation(calculation, locations.prevValueLocation, operatorLocation, locations.nextValueLocation, result); // make new calculation

                            i = operators.size(); // exits for loop
                        }
                    }
                } else {
                    System.out.println("Unknown operator.");
                    answer.setText("Could not find operator");
                    calculating = false;
                }
            } else {
                answer.setText(calculation.get(0));
                calculating = false;
            }
        }
    }

    public double BasicCalculation(double first, double second, String operator) {
        System.out.print("Calculating: " + first + operator + second);
        double resultValue = 0;
        switch (operator) { // Calculate with different operators
            case "+": resultValue = first + second; input.clear(); break;
            case "-": resultValue = first - second; input.clear(); break;
            case "*": resultValue = first * second; input.clear(); break;
            case "/": resultValue = first / second; input.clear(); break;
        }
        System.out.println(" = " + resultValue);
        return resultValue;
    }

    public List<String> CreateNewCalculation(List<String> calculation, int prevValueLocation, int operatorLocation, int nextValueLocation, double result) {
        // After calculation we want to make a new calculation
        // Example [2, *, 3, +, 6, *, 5] and first calculation is 2*3 then new calculation is
        // [6, +, 6, *, 5]
        List<String> newCalculation = new ArrayList<>();
        for (int i=0; i < calculation.size(); i++) {
            if (i == prevValueLocation) { // Add calculation result
                newCalculation.add(String.valueOf(result));
            } else if (i != operatorLocation && i != nextValueLocation) { // If we aren't on previous- or nextValue location add number/operator to newCalculation
                newCalculation.add(calculation.get(i));
            }
        }

        System.out.println("New calculation: " + newCalculation);
        System.out.println();
        return newCalculation;
    }
}

class Locations {
    public static double prevValue, nextValue;
    public static int prevValueLocation, nextValueLocation;

    static void GetLocations(List<String> calculation, int operatorLocation) {
        prevValueLocation = operatorLocation - 1;
        nextValueLocation = operatorLocation + 1;

        prevValue = Double.parseDouble(calculation.get(prevValueLocation));
        nextValue = Double.parseDouble(calculation.get(nextValueLocation));
    }
}