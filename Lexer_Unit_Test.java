package application;
import java.util.LinkedList;

import org.junit.Assert;
import org.junit.Test;

public class Lexer_Unit_Test {
	LinkedList<Token> tokenList;
	
	public void setUp(String s) {
		tokenList = (new Lexer(s)).lex();
	}
	
	@Test
	public void testExpression() {
		setUp("(3+2)*9-12");
		
		Assert.assertEquals(tokenList.get(0).getType(), Tokens.L_PARENTHESIS);
		Assert.assertEquals(tokenList.get(1).getValue(), "3");
		Assert.assertEquals(tokenList.get(2).getType(), Tokens.ADD);
		Assert.assertEquals(tokenList.get(3).getValue(), "2");
		Assert.assertEquals(tokenList.get(4).getType(), Tokens.R_PARENTHESIS);
		Assert.assertEquals(tokenList.get(5).getType(), Tokens.MULTIPLY);
		Assert.assertEquals(tokenList.get(6).getValue(), "9");
		Assert.assertEquals(tokenList.get(7).getType(), Tokens.SUBTRACT);
		Assert.assertEquals(tokenList.get(8).getValue(), "12");
	}
	
	@Test
	public void testPi() {
		setUp("pi");
		Assert.assertEquals(tokenList.get(0).getType(), Tokens.PI);
		
		setUp("pi12");
		Assert.assertEquals(tokenList.get(0).getType(), Tokens.PI);
		Assert.assertEquals(tokenList.get(1).getValue(), "12");
	}
	
	@Test
	public void testE() {
		setUp("e");
		Assert.assertEquals(tokenList.get(0).getType(), Tokens.E);
		
		setUp("e12");
		Assert.assertEquals(tokenList.get(0).getType(), Tokens.E);
		Assert.assertEquals(tokenList.get(1).getValue(), "12");
	}
	
	@Test
	public void testWhiteSpace() {
		setUp("(3 % 2) / 9 ^ 12");
		
		Assert.assertEquals(tokenList.get(0).getType(), Tokens.L_PARENTHESIS);
		Assert.assertEquals(tokenList.get(1).getValue(), "3");
		Assert.assertEquals(tokenList.get(2).getType(), Tokens.MODULO);
		Assert.assertEquals(tokenList.get(3).getValue(), "2");
		Assert.assertEquals(tokenList.get(4).getType(), Tokens.R_PARENTHESIS);
		Assert.assertEquals(tokenList.get(5).getType(), Tokens.DIVIDE);
		Assert.assertEquals(tokenList.get(6).getValue(), "9");
		Assert.assertEquals(tokenList.get(7).getType(), Tokens.EXPONENT);
		Assert.assertEquals(tokenList.get(8).getValue(), "12");
	}
	
	@Test
	public void testFunctions() {
		setUp("Sin(3)");
		
		Assert.assertEquals(tokenList.get(0).getType(), Tokens.FUNCTION);
		Assert.assertEquals(tokenList.get(0).getValue(),"Sin");
		
		Assert.assertEquals(tokenList.get(1).getType(), Tokens.L_PARENTHESIS);
		Assert.assertEquals(tokenList.get(2).getValue(), "3");
		Assert.assertEquals(tokenList.get(3).getType(), Tokens.R_PARENTHESIS);
	}
}
