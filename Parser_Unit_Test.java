package application;
import org.junit.Assert;
import org.junit.Test;


public class Parser_Unit_Test {
	Node node;

	public void setUp(String s) {
		var list = (new Lexer(s)).lex();
		Parser p = new Parser(list);
		node = p.parse();
	}

	@Test
	public void testNegative() {
		setUp("1 + -2");
		Assert.assertEquals(node.toString(), "(1.0 ADD (NEGATIVE 2.0))");	//Correct: (1 + (-2))
	}
	
	@Test
	public void testAdd() {
		setUp("1 + 2 + 3");
		Assert.assertEquals(node.toString(), "((1.0 ADD 2.0) ADD 3.0)");	//Correct: ((1 + 2) + 3)
	}
	
	@Test
	public void testSubtract() {
		setUp("1 - 2 - 3");
		Assert.assertEquals(node.toString(), "((1.0 SUBTRACT 2.0) SUBTRACT 3.0)");	//Correct: ((1 - 2) - 3)
	}
	
	@Test
	public void testExponents() {
		setUp("3^2");
		Assert.assertEquals(node.toString(), "(3.0 EXPONENT 2.0)");	//Correct: (3 ^ 2)
		
		setUp("3^2^4");
		Assert.assertEquals(node.toString(), "(3.0 EXPONENT (2.0 EXPONENT 4.0))");	//Correct: (3 ^ (2^4))
	}
	
	@Test
	public void testFunctions() {
		setUp("sin(2)");
		Assert.assertEquals(node.toString(), "SIN(2.0)");	//Correct: sin(2)
		
		setUp("sin(2 + 1)");
		Assert.assertEquals(node.toString(), "SIN((2.0 ADD 1.0))");	//Correct: sin((2 + 1))
		
		setUp("sin(sqrt(16))");
		Assert.assertEquals(node.toString(), "SIN(SQRT(16.0))");	//Correct: sin(sqrt(16))	
	}
	
	@Test
	public void testMultiplicationParentheses() {
		setUp("2(2+3)");
		Assert.assertEquals(node.toString(), "(2.0 MULTIPLY (2.0 ADD 3.0))");	//Correct: (2 * (2 + 3))
		
		setUp("sin(3)(18) + 1");
		
		Assert.assertEquals(node.toString(), "((SIN(3.0) MULTIPLY 18.0) ADD 1.0)");	//Correct: ((sin(3.0) * 18.0) + 1)
	}
	
	
	@Test
	public void testMultiOperations() {
		setUp("2*(2 + 3)/ 3^(1+1)");
		Assert.assertEquals(node.toString(), "((2.0 MULTIPLY (2.0 ADD 3.0)) DIVIDE (3.0 EXPONENT (1.0 ADD 1.0)))");	//Correct: ((2 * (2 + 3)) / (3 ^ (1 + 1)))
		
		setUp("2*(2+3)/12");
		Assert.assertEquals(node.toString(), "((2.0 MULTIPLY (2.0 ADD 3.0)) DIVIDE 12.0)");	//Correct: ((2*(2+3))/12)
	}
}
