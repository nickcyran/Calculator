package application;

public class NumberNode extends Node{
	private double number;
	
	NumberNode(double num){
		number = num;
	}
	
	public double getNumber() {
		return number;
	}
	
	@Override
	public String toString() {
		return Double.toString(number);
	}

}
