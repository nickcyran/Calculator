package application;
import java.util.LinkedList;

public class Lexer {
	
	private String expression;
	
	Lexer(String expression){
		this.expression = expression;
	}
	
	/** If the user is using keyboard input spaces are commonly used and must be 'eaten'**/
	private void discardWhiteSpace() {
		while(!expression.isEmpty()) {
			char currentChar = expression.charAt(0);
			
			if(currentChar == ' ' || currentChar == '\t' || currentChar == '\r' || currentChar == '\n') {
				expression = expression.substring(1);
			}
			else {
				break;
			}
		}
	}
	
	/** Go through the entered expression tokenizing each element into a list**/
	public LinkedList<Token> lex(){
		LinkedList<Token> tokens = new LinkedList<>();
		discardWhiteSpace();
		
		//While there are characters left in the expression loop through
		while(!expression.isEmpty()) {
			char currentChar = expression.charAt(0);
			
			switch (currentChar) {
				//Single Char Operations: single character functions (i.e. e) or operation
				case 'π' -> tokens.add(new Token(Tokens.PI));
				case 'e' -> tokens.add(new Token(Tokens.E));
				case '+' -> tokens.add(new Token(Tokens.ADD));
				case '-' -> tokens.add(new Token(Tokens.SUBTRACT));
				case '*' -> tokens.add(new Token(Tokens.MULTIPLY));
				case '/' -> tokens.add(new Token(Tokens.DIVIDE));
				case '^' -> tokens.add(new Token(Tokens.EXPONENT));
				case '%' -> tokens.add(new Token(Tokens.MODULO));
				case '(' -> tokens.add(new Token(Tokens.L_PARENTHESIS));
				case ')' -> tokens.add(new Token(Tokens.R_PARENTHESIS));
				default -> {
					//If number -> loop through only allowing other numbers or decimal points
					if (currentChar == '.' || Character.isDigit(currentChar)) {
						tokens.add(lexNumber());
						discardWhiteSpace();
						continue;				//Prevents removing the first char in the expression twice
					}
					//If letter -> has to be a function (deal with if it exists in parser)
					else if (Character.isLetter(currentChar)) {
						tokens.add(lexFunction());
						discardWhiteSpace();
						continue;
					}
					
					//Any other character is unrecognized -> wont reach considering no keyboard input
					else {
						throw new IllegalArgumentException("The character '" + currentChar + "' is unrecognized");
					}
				}
			}
			expression = expression.substring(1);
			discardWhiteSpace();
		}
		return tokens;
	}
	
	private Token lexFunction() {
		StringBuilder function = new StringBuilder();

		while (!expression.isEmpty()) {
			try {			
				functionType.valueOf(function.toString().toUpperCase());	//check and see if it exists as a functionType
				return new Token(Tokens.FUNCTION, function.toString());
			} catch (IllegalArgumentException e) {
				/* Because functions are the last stop it is acceptable to look and see if the partial string exists
				 * think of the scenario sin(cos(8)) -> sincos8 will also be accepted.
				 * if it is not a token then ignore the exception, wrong function names will be dealt in Interpreter.
				 */
			}
			
			char currentChar = expression.charAt(0);

			if (Character.isLetter(currentChar)) {
				function.append(currentChar);
			} else {
				break;
			}

			expression = expression.substring(1);
			
			// PI is the only value tokenized here that is not a function
			if (function.length() == 2 && function.toString().equalsIgnoreCase("pi")) { 
				return new Token(Tokens.PI);
			}
		}
		return new Token(Tokens.FUNCTION, function.toString());
	}
	
	/** If number or decimal found loop until the number is finished and create a token**/
	private Token lexNumber() {
		boolean containsDecimal = false;					// Only allows one decimal place within a number
		StringBuilder number = new StringBuilder();
		
		while(!expression.isEmpty()) {						// Loop through expression until it is empty or is no longer a number
			char currentChar = expression.charAt(0);	
			
			if(Character.isDigit(currentChar)) {		
				number.append(currentChar);
			}
			else if(currentChar == '.') {					
				number.append(currentChar);
				
				if(containsDecimal) {
					Calculator.error();
					System.err.println("ERROR: Too many decimals in this number");
				}
				containsDecimal = true;
			}
			else {
				break;										// The number in the expression has been lexed completely
			}
			expression = expression.substring(1);			// Truncate the expression so only unlexed data is left
		}
		return new Token(Tokens.NUMBER, number.toString());
	}
	
}
