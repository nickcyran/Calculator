package application;

enum Tokens {NUMBER, ADD, SUBTRACT, MULTIPLY, DIVIDE, EXPONENT, MODULO, L_PARENTHESIS, R_PARENTHESIS,
	  E, PI, FUNCTION}
//SQRT, LN, SIN, COS, TAN <-- functions 

public class Token {
	private Tokens tokenType;
	private String tokenValue;
	
	Token(Tokens type){
		tokenType = type;
	}
	
	Token(Tokens type, String value){
		this(type);
		tokenValue = value;
	}
	
	public Tokens getType() {
		return tokenType;
	}
	
	public String getValue() {
		return tokenValue;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(tokenType.toString());
		
		return tokenValue != null ? sb.append(" - " + tokenValue).toString() : sb.toString();
	}
}
