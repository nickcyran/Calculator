package application;

import org.junit.Assert;
import org.junit.Test;


public class Interpreter_Unit_Test {
	private double num;
	
	public void setUp(String s) {
		var list = (new Lexer(s)).lex();
		Node node = (new Parser(list)).parse();
		Interpreter interpreter = new Interpreter();
		num = interpreter.interpret(node);
	}
	
	@Test
	public void testExpressions() {
		setUp("1 + 1");
		Assert.assertTrue(num == 2);
		
		setUp("2 * 4");
		Assert.assertTrue(num == 8);
		
		setUp("15 % 2");
		Assert.assertTrue(num == 1);
		
		setUp("15 - 2");
		Assert.assertTrue(num == 13);
		
		setUp("14 / 8");
		Assert.assertTrue(num == 1.75);
		
		setUp("3^3");
		Assert.assertTrue(num == 27);
		
		setUp("1 + (2*3)");
		Assert.assertTrue(num == 7);
		
		setUp("(4*3+2)/2");
		Assert.assertTrue(num == 7);
		
		setUp("(2.5 + .5)^2^2");
		Assert.assertTrue(num == 81);
	}
	
	@Test
	public void testTrigFunctions() {
		setUp("sin(180)");
		Assert.assertTrue(num == 0);
		
		setUp("sin(90)");
		Assert.assertTrue(num == 1);
		
		Calculator.changeMode(); 		//set to radians
		
		setUp("tan(5+5)");
		Assert.assertTrue(num == 0.64836082746);
		
		setUp("cos(2*0)");
		Assert.assertTrue(num == 1);
		
		setUp("cos(12)");
		Assert.assertTrue(num == 0.84385395873);
	}
	
	@Test
	public void testSQRT() {
		setUp("SQRT(2+2)");
		Assert.assertTrue(num == 2);
		
		setUp("SQRT(4*(2+2))");
		Assert.assertTrue(num == 4);
		
		setUp("SQRT(3)");
		Assert.assertTrue(num == 1.73205080757);
		
		setUp("SQRT(1)");
		Assert.assertTrue(num == 1);
	}
	
	@Test
	public void testLn() {
		setUp("ln(10)");
		Assert.assertTrue(num == 2.30258509299);
		
		setUp("ln(ln(2)) + 3");
		Assert.assertTrue(num == 2.63348707942);
		
		setUp("ln(3^(2+1))");
		Assert.assertTrue(num == 3.295836866);
	}
	
	@Test
	public void testNegatives() {
		setUp("-2 * -2");
		Assert.assertTrue(num == 4);
		
		setUp("-2 * 3");
		Assert.assertTrue(num == -6);
		
		setUp("4*2^-2");
		Assert.assertTrue(num == 1);
	}
	
	@Test
	public void testMisc() {
		setUp("pi - 3");
		Assert.assertTrue(num == 0.14159265359);
		
		setUp("e");
		Assert.assertTrue(num == 2.71828182846);
		
		setUp("e * pi");
		Assert.assertTrue(num == 8.53973422267);
	}
}	
