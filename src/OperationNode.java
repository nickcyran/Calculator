package application;
import java.util.Optional;

enum operations {ADD, SUBTRACT, MULTIPLY, DIVIDE, MODULO, NEGATIVE, EXPONENT}

public class OperationNode extends Node{
	private operations operator;
	private Node left;
	private Optional<Node> right;
	
	OperationNode(Node left, operations op) {
	    this(left, op, Optional.empty());
	}

	OperationNode(Node left, operations op, Optional<Node> right) {
	    this.left = left;
	    operator = op;
	    this.right = right;
	}
	
	public Node getLeft() {
		return left;
	}
	
	public Optional<Node> getRight() {
		return right;
	}
	
	public operations getOperator() {
		return operator;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("(");
		if (right.isEmpty()) {
			sb.append(operator.toString()).append(" " + left);
		} else {
			sb.append(left).append(" " + operator.toString() + " ");
			sb.append(right.get());
		}

		sb.append(")");

		return sb.toString();
	}

}
