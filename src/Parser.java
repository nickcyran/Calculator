package application;
import java.util.LinkedList;
import java.util.Optional;

public class Parser {
	public class TokenHandler {
		private LinkedList<Token> tokenList;

		TokenHandler(LinkedList<Token> tokens) {
			tokenList = tokens;
		}
		
		/** Look at the next token in the list and return it **/
		public Optional<Token> peek(int j) {
			if (j < tokenList.size()) {
				return Optional.of(tokenList.get(j));
			} else {
				throw new IndexOutOfBoundsException("Incomplete Expression. No tokens Left to parse. Empty at index: " + j);
			}
		}
		
		/** See if the current Token type matches the input, remove and return **/
		public Optional<Token> matchAndRemove(Tokens t) {
			Tokens currentType = peek(0).get().getType(); 										//Get type of first token in list
			return (currentType == t) ? Optional.of(tokenList.remove()) : Optional.empty(); 	//Found -> remove from list -> return optional token
		} 																						//Not found -> dont remove -> return empty
		
		public boolean moreTokens() {
			return !tokenList.isEmpty();
		}
	}
	
	private TokenHandler tokenManager;

	Parser(LinkedList<Token> tokens) {
		
		tokenManager = new TokenHandler(tokens);
	}

	public Node parse() {
		Optional<Node> node = parseExpression();
		
		// If there was some sort of user error it will return as null; nothing further will happen as Calculator will stop the progression if an error occured
		if(node.isPresent()) {
			return node.get();
		}
		
		Calculator.error();
		return null;
	}

	public Optional<Node> parseExpression() {
		return expression();
	}

	/** parses basic expressions using left side and parenthesis grouping rules **/
	private Optional<Node> expression() {
		Optional<Node> left = term();

		while (tokenManager.moreTokens()) {
			operations operator = operations.ADD;
			if (!currentTokenPresent(Tokens.ADD)) {
				operator = operations.SUBTRACT;
				if (!currentTokenPresent(Tokens.SUBTRACT)) {
					return left;
				}
			}
			Optional<Node> right = term();
	
			if (right.isEmpty()) {
				Calculator.error();
				System.err.println("ERROR: The '" + operator + "' operator cannot be used this way");
				return Optional.empty();
			}
			
			// e.g) +3 =; NOTE: negative numbers work just fine
			if (left.isEmpty()) {
				Calculator.error();
				System.err.println("ERROR: The '" + operator + "' operator cannot be used this way");
				return Optional.empty();
			}
			
			left = Optional.of(new OperationNode(left.get(), operator, right));
		}
		return left;
	}
	
	private boolean multiplicationOutliers(){
		Tokens token =  tokenManager.peek(0).get().getType();
		
		switch(token) {
		case FUNCTION, PI, E, NUMBER:
			return true;
		default:
			return false;
		}
	}
	
	/** parses terms using left side and parenthesis grouping rules **/
	private Optional<Node> term() {
		Optional<Node> left = parseExponent();

		while (tokenManager.moreTokens()) {
			operations operator = operations.MULTIPLY;
			
			// 8(2) is valid multiplication
			if (currentTokenPresent(Tokens.L_PARENTHESIS)) {
				Optional<Node> right = parseExpression();
				
				// Check and consume the right parenthesis
				if (currentTokenPresent(Tokens.R_PARENTHESIS)) {
					left = Optional.of(new OperationNode(left.get(), operator, right));
				} else {
					Calculator.error();
					System.err.println("ERROR: Missing parenthesis");
				}
			// Case where number is next to function e.g) 16sqrt25	
			} else if (multiplicationOutliers()) { 
				left = Optional.of(new OperationNode(left.get(), operator, parseExpression()));
			}  else {
				if (!currentTokenPresent(Tokens.MULTIPLY)) {
					operator = operations.DIVIDE;
					
					if (!currentTokenPresent(Tokens.DIVIDE)) {
						operator = operations.MODULO;
						
						if (!currentTokenPresent(Tokens.MODULO)) {
							return left;
						}
					}
				}

				Optional<Node> right = parseExponent();
				
				// e.g) *; a floating operation
				if (right.isEmpty()) {
					Calculator.error();
					System.err.println("ERROR: The '" + operator + "' operator cannot be used this way");
					return Optional.empty();
				}
				
				// e.g) *3; an operation with no left side
				if (left.isEmpty()) {
					Calculator.error();
					System.err.println("ERROR: The '" + operator + "' operator cannot be used this way");
					return Optional.empty();
				}
				
				left = Optional.of(new OperationNode(left.get(), operator, right));
			}
		}
		return left;
	}

	/** parses Exponents using right side rules **/
	private Optional<Node> parseExponent() {
		Optional<Node> left = parseBottomLevel();

		while (tokenManager.moreTokens() && currentTokenPresent(Tokens.EXPONENT)) {
			Optional<Node> right = parseExponent();
			
			// e.g) ^
			if (right.isEmpty()) {
				Calculator.error();
				System.err.println("ERROR: The 'EXPONENT' operator cannot be used this way");
				return Optional.empty();
			}
			
			// e.g) ^4
			if (left.isEmpty()) {
				Calculator.error();
				System.err.println("ERROR: The 'EXPONENT' operator cannot be used this way");
				return Optional.empty();
			}
			
			left = Optional.of(new OperationNode(left.get(), operations.EXPONENT, right));
		}
		return left;
	}

	/** Parses the lowest level, creating operationNodes representing the tree **/
	private Optional<Node> parseBottomLevel() {
		Optional<Token> current;
		
		if(!tokenManager.moreTokens()) {
			return Optional.empty();
		}
		
		current = tokenManager.matchAndRemove(Tokens.NUMBER);
		if (current.isPresent()) {
			return Optional.of(new NumberNode(Double.parseDouble(getValueFromOptional(current))));
		}

		current = tokenManager.matchAndRemove(Tokens.E);
		if (current.isPresent()) {
			return Optional.of(new NumberNode(Math.E));
		}

		current = tokenManager.matchAndRemove(Tokens.PI);
		if (current.isPresent()) {
			return Optional.of(new NumberNode(Math.PI));
		}

		// Parenthesis have their own operations and must be parsed as a whole
		if (currentTokenPresent(Tokens.L_PARENTHESIS)) {

			Optional<Node> operation = parseExpression();

			if (currentTokenPresent(Tokens.R_PARENTHESIS))
				return operation;
		}

		if (currentTokenPresent(Tokens.SUBTRACT)) {
			Optional<Node> operation = parseBottomLevel();
			
			if(operation.isEmpty()) {
				Calculator.error();
				System.err.println("Error: The 'MINUS' operator cannot be used in this way");
				return Optional.empty();
			}
			
			return Optional.of(new OperationNode(operation.get(), operations.NEGATIVE));
		}

		Optional<Node> function = parseFunction();
		if (function.isPresent()) {
			return function;
		}
		
		return Optional.empty();
	}

	/**
	 * Checks if the functions are one that are programed in (i.e. sin, cos, tan,
	 * ln, sqrt) throw an error if it is some misc. function NOTE: Last level; if it
	 * gets here it is definitely a functionNode
	 **/
	public Optional<Node> parseFunction() {
		Optional<Token> current = tokenManager.matchAndRemove(Tokens.FUNCTION);
		if (current.isPresent()) {
			functionType fnType;
			Optional<Node> insideFunction;
			
			// Check if the function exists as an enum
			try {
				fnType = functionType.valueOf(current.get().getValue().toUpperCase());
			} catch (IllegalArgumentException e) {
				// Can't reach here through user input
				throw new IllegalArgumentException("Function does not exist");
			}

			// If the next token is a parenthesis, then parse fully again
			if (currentTokenPresent(Tokens.L_PARENTHESIS)) {
				insideFunction = parseExpression();

				// Check and consume the right parenthesis
				if (currentTokenPresent(Tokens.R_PARENTHESIS)) {
					return Optional.of(new FunctionNode(fnType, insideFunction.get()));
				} else {
					Calculator.error();
					System.err.println("ERROR: Missing parenthesis");
					return Optional.empty();
				}
			} else {
				// If not a parenthesis, only one number can be in the function
				insideFunction = parseBottomLevel();
				
				if(insideFunction.isEmpty()) {
					Calculator.error();
					System.err.println("ERROR: No value inside function");
					return Optional.empty();
				}
				
				return Optional.of(new FunctionNode(fnType, insideFunction.get()));
			}
		} else {
			return Optional.empty();
		}
	}

	private boolean currentTokenPresent(Tokens t) {
		if (!tokenManager.moreTokens()) {
			Calculator.error();
			return false;
		}

		return tokenManager.matchAndRemove(t).isPresent();
	}

	private String getValueFromOptional(Optional<Token> t) {
		return t.get().getValue();
	}
}
