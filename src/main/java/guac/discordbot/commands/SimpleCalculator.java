package guac.discordbot.commands;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.user.User;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;

//litterally my cs 112 project lmao
public class SimpleCalculator implements CommandExecutor {
	public static String delims = " \t*+-/()";
	
	@Command(aliases = {"&calc"}, description = "Simple calculator", 
			usage = "&calc (3*(4+5))/2")
	
	public void calculatorCommand(String[] args, User user, TextChannel channel) {
		if(user.isBot()) {return;}
		if(args.length == 0) {
			channel.sendMessage("Nothing to calculate :pensive:");
			return;
		}
		String calc = String.join("", args);
		try {
			channel.sendMessage(Float.toString(evaluate(calc)));
		} catch (java.util.NoSuchElementException e) {
			channel.sendMessage("Please type your expression in correctly :slight_smile:");
		}
		return;
	}
	
	private static float evaluateRec(StringTokenizer st) {
    	float ans = 0;
        
    	
        Stack<Float> operands = new Stack<Float>();
        Stack<String> operators = new Stack<String>();
        
        Stack<Float> revOperands = new Stack<Float>();
        Stack<String> revOperators = new Stack<String>();
         
        String op = "";
        float op1 = 0;
        float op2 = 0;
        float val = 0;
        String str = "";
        
        while(st.hasMoreTokens()) {
            str = st.nextToken();
            //begin recursion on a '('
            if(str.charAt(0) == '(') { 
            	float num = evaluateRec(st);
            	operands.push(num);
            }
            
            //end recursion on a ')'
            if(str.charAt(0) == ')') {
            	while(!operators.isEmpty()) {
            		String value = operators.pop();
                    revOperators.push(value);
            	}
            	while(!operands.isEmpty()) {
                    val = operands.pop();
                    revOperands.push(val);
            	}
            	while(!revOperators.isEmpty()) {
                   op = revOperators.pop();
                   op1 = revOperands.pop();
                   op2 = revOperands.pop();
                   ans = eval(op,op1,op2);
                   revOperands.push(ans);    
              }
              if(revOperators.isEmpty() && !revOperands.isEmpty()) {
                   ans = revOperands.pop();
                   return ans;         
              } 
               
            }
            //push number 
            if(str.matches("[0-9]+")) {
         	   operands.push(Float.valueOf(str));
            }
            //push addition/subtraction
            if(str.charAt(0) == '+' || str.charAt(0) == '-') {
            	operators.push(str);
            }
            //multiply/divide on the spot
            if(str.charAt(0) == '*' || str.charAt(0) == '/') { 
            	op = str;
            	op1 = operands.pop();
            	str = st.nextToken();
            	
                if(str.matches("[0-9]+")) {
                    op2 = Float.valueOf(str);
                }
                if(str.charAt(0) == '(') {
                    op2 = evaluateRec(st);
                }
                ans = eval(op,op1,op2);
                operands.push(ans);              
            }
        }
        
      //reverses the stack to add and subtract in correct order        
       while(!operators.isEmpty()) {  
            String value = operators.pop();
            revOperators.push(value);
       }
       while(!operands.isEmpty()) {           
            val = operands.pop();
            revOperands.push(val);
        }
      while(!revOperators.isEmpty()) {
           op = revOperators.pop();
           op1 = revOperands.pop();
           op2 = revOperands.pop();
           ans = eval(op,op1,op2);
           revOperands.push(ans);
      } 
      if(revOperators.isEmpty() && !revOperands.isEmpty()) {
           ans = revOperands.pop();
           return ans;         
      }
    	
    return ans;	
    }
	private static float eval(String op, float op1, float op2) {
    	switch(op) {
    		case "+": return op1 + op2;
    		case "-": return op1 - op2;
    		case "*": return op1 * op2;
    		case "/": return op1 / op2;
    	}
    	return 0;
    }
	
	public static float evaluate(String expr) {
    	expr = expr.replaceAll(" ", "");
        StringTokenizer st = new StringTokenizer(expr, delims, true);   	
        return evaluateRec(st);
    }

}
 class Stack<T> {

	/**
	 * Items in the stack.
	 */
	private ArrayList<T> items;

	/**
	 * Initializes stack to empty.
	 */
	public Stack() {
		items = new ArrayList<T>();
	}

	/**
	 * Pushes a new item on top of stack.
	 * 
	 * @param item Item to push.
	 */
	public void push(T item) {
		items.add(item);
	}

	/**
	 * Pops item at top of stack and returns it.
	 * 
	 * @return Popped item.
	 * @throws NoSuchElementException If stack is empty.
	 */
	public T pop() 
	throws NoSuchElementException {
		if (items.isEmpty()) {
			//return null;
			throw new NoSuchElementException("can't pop from an empty stack");
		}
		return items.remove(items.size()-1);
	}

	/**
	 * Returns item on top of stack, without popping it.
	 * 
	 * @return Item at top of stack.
	 * @throws NoSuchElementException If stack is empty.
	 */
	public T peek() 
	throws NoSuchElementException {
		if (items.size() == 0) {
			//return null;
			throw new NoSuchElementException("can't peek on an empty stack");
		}
		return items.get(items.size()-1);
	}

	/**
	 * Tells if stack is empty.
	 * 
	 * @return True if stack is empty, false if not.
	 */
	public boolean isEmpty() {
		return items.isEmpty();
	}

	/**
	 * Returns number of items in stack.
	 * 
	 * @return Number of items in stack.
	 */
	public int size() {
		return items.size();
	}

	/**
	 * Empties the stack.
	 */
	public void clear() {
		items.clear();
	}
}