package application;

import java.text.DecimalFormat;
import java.util.function.BinaryOperator;
import java.util.function.DoubleUnaryOperator;

public class Interpreter {
	private DecimalFormat decimals;

	Interpreter() {
		decimals = new DecimalFormat("#.###########");
	}

	public double interpret(Node node) {
		String roundedNumber = decimals.format(getNumber(node));
		return Double.parseDouble(roundedNumber);
	}

	// If number return number, if operationNode return the solved node
	private double getNumber(Node node) {
		
		if (node instanceof FunctionNode fnNode) {
			return interpretFunction(fnNode);
		} else if (node instanceof OperationNode opNode) {
			return mathOperations(opNode);
		} else if (node instanceof NumberNode numNode) {
			return numNode.getNumber();
		}

		// only 3 types of nodes so this should not be accessible by normal circustances
		throw new IllegalArgumentException("getNumber");	
	}

	private double processTrigFunction(FunctionNode node, DoubleUnaryOperator mathFunction) {
			double EPSILON = 1e-15;	
		  	double num = getNumber(node.getFunctionInnards());
		  
		  	double result = mathFunction.applyAsDouble(Calculator.inRadians() ? num : Math.toRadians(num));
		  	
		  	return Math.abs(result) < EPSILON ? 0.0 : result;	// Rounds to expected value -> ex. sin(180deg) = 0
	}
	
	private double interpretFunction(FunctionNode node) {
		return switch (node.getFunction()) {
			case SQRT -> Math.sqrt(getNumber(node.getFunctionInnards()));
			case LN -> Math.log(getNumber(node.getFunctionInnards()));
			case SIN -> processTrigFunction(node, Math::sin);
		    case COS -> processTrigFunction(node, Math::cos);
		    case TAN -> processTrigFunction(node, Math::tan);
		    // Not accessible normally, input is preset
		    default -> throw new IllegalArgumentException("Unsupported function: " + node.getFunction());
		};
	}

	private double mathOperations(OperationNode node) {
		BinaryOperator<Double> mathOp;
		boolean division = false;

		switch (node.getOperator()) {
			case ADD -> mathOp = (a, b) -> a + b;
			case SUBTRACT -> mathOp = (a, b) -> a - b;
			case MULTIPLY -> mathOp = (a, b) -> a * b;
			case DIVIDE -> {
				division = true;
				mathOp = ((a, b) -> a / b);}
			case MODULO -> mathOp = (a, b) -> a % b;
			case EXPONENT -> mathOp = Math::pow;
			default -> {
				return interpretNegative(node);
			}
		}

		double left = getNumber(node.getLeft()); // Do the left
		double right = 0;

		if (node.getRight().isPresent()) { // If theres a right do it
			right = getNumber(node.getRight().get());
		}
		
		// Division by 0
		if(division && right == 0) {
			Calculator.error();
			System.err.println("ERROR: Division by 0");
			return 0;
		}

		// Apply the operation with the parsed values
		return mathOp.apply(left, right);
	}

	private double interpretNegative(OperationNode node) {
		if (node.getOperator() == operations.NEGATIVE) {
			return -getNumber(node.getLeft());
		} else {
			// Not accessible under normal circumstances, this would be if noth
			throw new IllegalArgumentException("There is nothing left this can be interpreted as");
		}

	}

}
