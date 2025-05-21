package application;

enum functionType {SQRT, LN, SIN, COS, TAN}

public class FunctionNode extends Node{
	private functionType fnType;
	private Node fnInner;
	
	FunctionNode(functionType function, Node fnIn){
		fnType = function;
		fnInner = fnIn;
	}
	
	public functionType getFunction() {
		return fnType;
	}
	
	public Node getFunctionInnards() {
		return fnInner;
	}

	@Override
	public String toString() {
		return fnType.toString() + "(" + fnInner + ")";
	}
}
