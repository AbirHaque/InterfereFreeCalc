import java.io.*;
import java.util.*;
import java.math.*;
public class EquationReader
{
    private static ArrayList<String> functionList = new ArrayList<String>();
    private static ArrayList<String> leftAssociative = new ArrayList<String>();
    private static Map<String, Integer> precedence = new HashMap<String, Integer>();
    private static Map<String, String> constants = new HashMap<String, String>();
    private static Map<String, Integer> multiParamFunctions = new HashMap<String, Integer>();
    public static void readyFunctionList()
    {
        functionList.add("sqrt");
        functionList.add("abs");
        functionList.add("log");
        functionList.add("ln");
        functionList.add("sin");
        functionList.add("cos");
        functionList.add("tan");
        functionList.add("csc");
        functionList.add("sec");
        functionList.add("cot");
        functionList.add("++");
        functionList.add("--");
    }
    public static void readyLeftAssociative()
    {
        leftAssociative.add("%");
        leftAssociative.add("*");
        leftAssociative.add("/");
        leftAssociative.add("+");
        leftAssociative.add("-");
    }
    public static void readyPrecedence()
    {
        precedence.put("^", new Integer(3));
        precedence.put("%", new Integer(2));
        precedence.put("*", new Integer(2));
        precedence.put("/", new Integer(2));
        precedence.put("+", new Integer(1));
        precedence.put("-", new Integer(1));
    }
    public static void readyConstants()
    {
        constants.put("pi", String.valueOf(Math.PI));
        constants.put("e", String.valueOf(Math.E));
    }
    public static void readyMultiParamFunctions()
    {
        multiParamFunctions.put("logbase", new Integer(2));
    }
    public static String evalOperator(String value1, String value2, String operator) throws Exception
    {
        String finalValue = "";
        Double input1 = 0.0;
        Double input2 = 0.0;
        if (value1.contains("-"))
        {
            input1 = -1.0*Double.valueOf(value1.substring(1,value1.length()));
        }
        else
        {
            input1 = Double.valueOf(value1);
        }
        if (value2.contains("-"))
        {
            input2 = -1.0*Double.valueOf(value2.substring(1,value2.length()));
        }
        else
        {
            input2 = Double.valueOf(value2);
        }
        switch(operator)
        {
            case "^":
                finalValue = String.valueOf(Math.pow(input1,input2));
                break;
            case "%":
                finalValue = String.valueOf(input1%input2);
                break;
            case "*":
                finalValue = String.valueOf(input1*input2);
                break;
            case "/":
                finalValue = String.valueOf(input1/input2);
                break;
            case "+":
                finalValue = String.valueOf(input1+input2);
                break;
            case "-":
                finalValue = String.valueOf(input1-input2);
                break;
        }
        return finalValue;
    }
    public static String evalFunction(String value, String function) throws Exception
    {
        String finalValue = "";
        Double input = 0.0;
        if (value.contains("-"))
        {
            input = -1.0*Double.valueOf(value.substring(1,value.length()));
        }
        else
        {
            input = Double.valueOf(value);
        }
        switch(function)
        {
            case "sqrt":
                finalValue = String.valueOf(Math.sqrt(input));
                break;
            case "abs":
                finalValue = String.valueOf(Math.abs(input));
                break;
            case "log":
                finalValue = String.valueOf(Math.log10(input));
                break;
            case "ln":
                finalValue = String.valueOf(Math.log(input));
                break;
            case "sin":
                finalValue = String.valueOf(Math.sin(input));
                break;
            case "cos":
                finalValue = String.valueOf(Math.cos(input));
                break;
            case "tan":
                finalValue = String.valueOf(Math.tan(input));
                break;
            case "csc":
                finalValue = String.valueOf(Double.valueOf(1/Math.sin(input)));
                break;
            case "sec":
                finalValue = String.valueOf(Double.valueOf(1/Math.cos(input)));
                break;
            case "cot":
                finalValue = String.valueOf(Double.valueOf(1/Math.tan(input)));
                break;
            case "++":
                finalValue = String.valueOf(Double.valueOf(input+1.0));
                break;
            case "--":
                finalValue = String.valueOf(Double.valueOf(input-1.0));
                break;
        }
        return finalValue;
    }
    public static String evalMultiParamFunction(String[] values, String function) throws Exception
    {
        String finalValue = "";
        Double[] inputs = new Double[values.length];
        for (int i = values.length-1; i >= 0; i--)
        {
            if (values[i].contains("-"))
            {
                inputs[i] = -1.0*Double.valueOf(values[i].substring(1,values[i].length()));
            }
            else
            {
                inputs[i] = Double.valueOf(values[i]);
            }
        }
        switch(function)
        {
            case "logbase":
                finalValue = String.valueOf(Math.log(inputs[0])/Math.log(inputs[1]));
                break;
        }
        return finalValue;
    }
    public static boolean isNumeric(final String str) throws Exception
    {
            if (str == null || str.length() == 0) 
        {
            return false;
            }
            for (char c : str.toCharArray()) 
        {
                    if (!Character.isDigit(c)) 
            {
                        return false;
                    }
            }
            return true;
    }
    public static String evalPostFix(ArrayList<String> originalOutput) throws Exception
    {
        ArrayList<String> output = new ArrayList<String>();
        for(int i = 0; i < originalOutput.size(); i++)
        {
            //Replace variable tokens with their respective values
            if (constants.containsKey(originalOutput.get(i)))
            {
                output.add(constants.get(originalOutput.get(i)));
            }
            else
            {
                output.add(originalOutput.get(i));
            }
        }
        ArrayList<String> stack = new ArrayList<String>();
        for(int i = 0; i < output.size(); i++)
        {
            if (isNumeric(output.get(i))||((output.get(i).contains("-")||output.get(i).contains("."))&&output.get(i).length()>1))
            {
                stack.add(output.get(i));
            }
            if (functionList.contains(output.get(i))&&stack.size()>0)
            {
                String input = stack.get(stack.size()-1);
                String function = output.get(i);
                stack.remove(stack.size()-1);
                stack.add(evalFunction(input, function));
            }
            if (multiParamFunctions.containsKey(output.get(i)))
            {
                int requiredInputs = multiParamFunctions.get(output.get(i)).intValue();
                if (stack.size()>=requiredInputs)
                {
                    String[] inputs = new String[requiredInputs];
                    for (int k = 0; k < inputs.length; k++)
                    {
                        inputs[k] = stack.get(stack.size()-1);
                        stack.remove(stack.size()-1);
                    }
                    String function = output.get(i);
                    stack.add(evalMultiParamFunction(inputs, function));
                }
            }
            if (precedence.containsKey(output.get(i))&&stack.size()>1)
            {
                //probably an operator
                String input1 = stack.get(stack.size()-2);
                String input2 = stack.get(stack.size()-1);
                String operator = output.get(i);
                stack.remove(stack.size()-2);
                stack.remove(stack.size()-1);
                stack.add(evalOperator(input1, input2, operator));
            }
        }
        return stack.get(0);
    }
    public static ArrayList<String> evalShuntingYard(String equation) throws Exception
    {   
        //0.5 * cos ( 2 * pi * 440 * x / 44100 )
        //0.5 * sin ( 2 * pi * 440 * x / 44100 )
        equation = equation.replaceAll("\\("," \\( ");
        equation = equation.replaceAll("\\)"," \\) ");
        equation = equation.replaceAll(","," ");
        for (String operator : precedence.keySet())
        {
            equation = equation.replaceAll("\\"+operator," \\"+operator+" ");
        }
        
        
        ArrayList<String> tokens = new ArrayList<String>(Arrays.asList(equation.split(" ")));
        ArrayList<String> stack = new ArrayList<String>();
        ArrayList<String> output = new ArrayList<String>();
        for(int i = 0; i < tokens.size(); i++)
        {
            switch(tokens.get(i))
            {
                case ",":
                    while(!stack.get(stack.size()-1).equals("(")&&!stack.get(stack.size()-1).equals(","))
                    {
                        output.add(stack.get(stack.size()-1));
                        stack.remove(stack.size()-1);
                    }
                    if(stack.get(stack.size()-1).equals("(")||stack.get(stack.size()-1).equals(","))
                    {
                        stack.remove(stack.size()-1);
                    }
                    if(stack.size() > 0 && multiParamFunctions.containsKey(stack.get(stack.size()-1)))
                    {
                        output.add(stack.get(stack.size()-1));
                        stack.remove(stack.size()-1);
                    }
                    //else
                    //{
                            stack.add(tokens.get(i));
                    //}
                    break;
                case "(":
                        stack.add(tokens.get(i));
                    break;
                case ")":
                    while(!stack.get(stack.size()-1).equals("(")&&stack.get(stack.size()-1).equals(","))
                    {
                        output.add(stack.get(stack.size()-1));
                        stack.remove(stack.size()-1);
                    }
                    if(stack.get(stack.size()-1).equals("(")||stack.get(stack.size()-1).equals(","))
                    {
                        stack.remove(stack.size()-1);
                    }
                    if(stack.size() > 0 && functionList.contains(stack.get(stack.size()-1)))
                    {
                        output.add(stack.get(stack.size()-1));
                        stack.remove(stack.size()-1);
                    }
                    if(stack.size() > 0 && multiParamFunctions.containsKey(stack.get(stack.size()-1)))
                    {
                        output.add(stack.get(stack.size()-1));
                        stack.remove(stack.size()-1);
                    }
                    break;
                default:
                    if (functionList.contains(tokens.get(i)))
                    {
                        stack.add(tokens.get(i));
                    }else{
                    if (multiParamFunctions.containsKey(tokens.get(i)))
                    {
                        stack.add(tokens.get(i));
                    }
                    else
                    {
                        if (precedence.containsKey(tokens.get(i)))
                        {
                            while (
                                ( stack.size() > 0 && precedence.containsKey(stack.get(stack.size()-1))&& !stack.get(stack.size()-1).equals(",") && !stack.get(stack.size()-1).equals("(") && !stack.get(stack.size()-1).equals(")") )
                                && ( 
                                    ( precedence.get(stack.get(stack.size()-1)).compareTo(precedence.get(tokens.get(i))) > 0 )
                                        ||( precedence.get(stack.get(stack.size()-1)).compareTo(precedence.get(tokens.get(i)))==0 && leftAssociative.contains(tokens.get(i)) )
                                    )
                            )
                            {
                                        output.add(stack.get(stack.size()-1));
                                stack.remove(stack.size()-1);
                            }
                                stack.add(tokens.get(i));
                        }
                        else
                        {
                            output.add(tokens.get(i));
                        }
                    }}
            }
        }
        while(stack.size()>0)
        {
            output.add(stack.get(stack.size()-1));
            stack.remove(stack.size()-1);
        }
        return output;
    }
}