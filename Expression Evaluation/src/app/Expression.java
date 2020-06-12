package app;

// need bc array lists
import java.io.*;

import java.util.*;
import java.util.regex.*;

import structures.Stack;

import java.util.StringTokenizer;

public class Expression {

	public static String delims = " \t*+-/()[]";
			
    /**
     * Populates the vars list with simple variables, and arrays lists with arrays
     * in the expression. For every variable (simple or array), a SINGLE instance is created 
     * and stored, even if it appears more than once in the expression.
     * At this time, values for all variables and all array items are set to
     * zero - they will be loaded from a file in the loadVariableValues method.
     * 
     * @param expr The expression
     * @param vars The variables array list - already created by the caller
     * @param arrays The arrays array list - already created by the caller
     */
    public static void 
    makeVariableLists(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
    	/** COMPLETE THIS METHOD **/
    	/** DO NOT create new vars and arrays - they are already created before being sent in
    	 ** to this method - you just need to fill them in.
    	 **/
    	
    	// RECITATION NOTES - String Tokenizer and use deliminators 
    	// tokenize "["
    	// Tonkenize may help here
    	
    	String specialDelims = "*+-/()]";
    	expr = expr.replaceAll("\\s+", "");	
    	
    	StringTokenizer str = new StringTokenizer(expr, specialDelims);
    	Stack<String> hold = new Stack<String>();
    	
    	boolean cont = true;
    	
    	String temp = "";
    	
    	ArrayList<Variable> varsCopy = new ArrayList<Variable>();
    	ArrayList<Array> arraysCopy = new ArrayList<Array>();
    	
    	// (1) temp == whitespace -> continue
		// (2) temp == digit -> continue
		// (3) temp.contains("[")
			// split at "[" -> convert to an array
			// loop array BUT skip last element (array.length-1) -> add to arrayList arrays
			// last element - access with index (array.length-1)
				// (1) whitespace -> ctu
				// (2) digit -> ctu
				// (3) variable -> arrayList vars
		// (4) else (means var name) 
			// add to vars ArrayList
    	
    	while (str.hasMoreTokens()) {
    		temp = str.nextToken();	
    		
    		if (temp.equals(" ")) {		// checking whitespace
    			continue;
    		} 
    		char c = temp.charAt(0); 
    		if (Character.isDigit(c)) {
    			continue;
    		}
    		if (temp.contains("[")) {
    			String[] arr = temp.split("\\[", 0);		// array name array
    			
    			
    			for (int i = 0; i < arr.length-1;i++) {		// where we add into arrays arrayLists
    				Array a = new Array(arr[i]);
    				arraysCopy.add(a);
    			}
    			
    			// check last element
    			String lastEl = arr[arr.length-1];
    			char ch = lastEl.charAt(0);
    			if (lastEl.equals(" ")) {		// whitespace
    				continue;	
    			} else if (Character.isDigit(ch)) { 	// a digit
    				continue;	
    			} else {		// a variable
    				Variable v = new Variable(lastEl); 
    				varsCopy.add(v);
    			}
    		} else {
    			Variable v = new Variable(temp);
    			varsCopy.add(v);
    		}
    		// System.out.println(str.nextToken());		// prints all names to test
    	}
    	
    	// Deleting duplicates
    	for (Array element : arraysCopy) {
    		if (!arrays.contains(element)) { 
                arrays.add(element); 
            } 
    	}
    	
    	for (Variable element : varsCopy) {
    		if (!vars.contains(element)) { 
                vars.add(element); 
            } 
    	}
    	
    	// System.out.println("The vars arrayLists is " + vars + ", and the arrays arrayLists is " + arrays);
    }
    
    /**
     * Loads values for variables and arrays in the expression
     * 
     * @param sc Scanner for values input
     * @throws IOException If there is a problem with the input 
     * @param vars The variables array list, previously populated by makeVariableLists
     * @param arrays The arrays array list - previously populated by makeVariableLists
     */
    
    // DO NOT TOUCH - already done
    public static void 
    loadVariableValues(Scanner sc, ArrayList<Variable> vars, ArrayList<Array> arrays) 
    throws IOException {
        while (sc.hasNextLine()) {
            StringTokenizer st = new StringTokenizer(sc.nextLine().trim());
            int numTokens = st.countTokens();
            String tok = st.nextToken();
            Variable var = new Variable(tok);
            Array arr = new Array(tok);
            int vari = vars.indexOf(var);
            int arri = arrays.indexOf(arr);
            if (vari == -1 && arri == -1) {
            	continue;
            }
            int num = Integer.parseInt(st.nextToken());
            if (numTokens == 2) { // scalar symbol
                vars.get(vari).value = num;
            } else { // array symbol
            	arr = arrays.get(arri);
            	arr.values = new int[num];
                while (st.hasMoreTokens()) {
                    tok = st.nextToken();
                    StringTokenizer stt = new StringTokenizer(tok," (,)");
                    int index = Integer.parseInt(stt.nextToken());
                    int val = Integer.parseInt(stt.nextToken());
                    arr.values[index] = val;              
                }
            }
        }
    }
    
    /**
     * Evaluates the expression.
     * 
     * @param vars The variables array list, with values for all variables in the expression
     * @param arrays The arrays array list, with values for all array items
     * @return Result of evaluation
     */
    public static float 
    evaluate(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
		
    	/** COMPLETE THIS METHOD **/
    	expr = expr.replaceAll("\\s+", "");		// TAKE OUT SPACES
    	
    	// Creating expr into character array (holds each letter)
    	// How to create char array - "char[] ch = new char[str.length()];"
    	char[] block = new char[expr.length()];
    	for (int i = 0; i < expr.length(); i++) { 
            block[i] = expr.charAt(i); 
        } 
    	
    	Stack<Float> digits = new Stack<Float>();
    	Stack<Character> operations = new Stack<Character>();
    	Stack<String> arrStk = new Stack<String>();
    	
    	int i = 0;
    	while (i < block.length) {
    		// check number
    		if (block[i] >= '0' && block[i] <= '9') {
    			String parsedNum = "";
    			int temp = i;
    			int len = block.length;
    			
    			while (temp < len && block[temp] >= '0' && block[temp] <= '9') {
    				parsedNum += block[temp];
    				temp++;
    			} i = temp - 1;
    			digits.push((float)Integer.parseInt(parsedNum));
    		} else if ((block[i] >= 'a' && block[i] <= 'z') || (block[i] >= 'A' && block[i] <= 'Z')) {
    			String varName = "";
    			int temp = i;
    			while (temp < block.length && ((block[temp] >= 'a' && block[temp] <= 'z') || (block[temp] >= 'A' && block[temp] <= 'Z'))) {
    				varName += block[temp];
    				temp++;
    			} i = temp - 1;
    			
    			// last index in array it has to be a variable
    			if (i == block.length - 1) {
    				for (int k = 0; k < vars.size(); k++) {
        				if (varName.equals(vars.get(k).name)) {
        					digits.push((float)vars.get(k).value);
        					break;
        				}
        			}
    			} else if (i <= block.length - 1 && block[i + 1] != '[') {
    				for (int k = 0; k < vars.size(); k++) {
        				if (varName.equals(vars.get(k).name)) {
        					digits.push((float)vars.get(k).value);
        					break;
        				}
        			}
    			} else if (i <= block.length - 1 && block[i + 1] == '[') {
    				for (int k = 0; k < arrays.size(); k++) {
    					if (varName.equals(arrays.get(k).name)) {
    						arrStk.push(arrays.get(k).name);
    					}
    				}
    			}
    		} else if (block[i] == '[') {
    			operations.push(block[i]);
    		} else if (block[i] == ']') {
    			while (operations.peek() != '[') {
    				digits.push(calc(operations.pop(), digits.pop(), digits.pop()));
    			}
    			float temp = digits.pop();
				for (int k = 0; k < arrays.size(); k++) {
					if (arrStk.peek().equals(arrays.get(k).name)) {
						digits.push((float)arrays.get(k).values[(int) temp]);
						arrStk.pop();
						break;
					}
				} operations.pop();
    		} else if (block[i] == '(') {		// testing left parenthesis
    			operations.push(block[i]);
    		} else if (block[i] == ')') {		// testing right parenthesis
    			while (operations.peek() != '(') {
    				digits.push(calc(operations.pop(), digits.pop(), digits.pop()));
    			} operations.pop();
    		} else if (block[i] == '+' || block[i] == '-' || block[i] == '*' || block[i] == '/') {
    			while (!operations.isEmpty() && pemdas(block[i], operations.peek())) {
    				digits.push(calc(operations.pop(), digits.pop(), digits.pop()));
    			} operations.push(block[i]);
    		}
    		
    		i++;
    	}
    	
    	while (!operations.isEmpty()) {
    		digits.push(calc(operations.pop(), digits.pop(), digits.pop()));
    	}
    	
    	float ans = digits.pop();
    	return ans;
    	
    }
    
    private static float calc(char op, float x, float y) {
    	switch(op) {
    	case '+':
    		return x + y;
    	case '-': 
    		return y - x;
    	case '*':
    		return x * y;
    	case '/':
    		return y / x;
    	} return 0;
    }

    private static boolean pemdas(char currentOp, char stackOp) {
    	if (stackOp == '(' || stackOp == ')') return false;
    	if (stackOp == '[' || stackOp == ']') return false;
    	if ((currentOp == '*' || currentOp == '/') && (stackOp == '+' || stackOp == '-')) { 
    		return false;
    	} else {
    		return true;
    	}
    }
    
    
}









