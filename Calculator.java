package application;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/* nicyran@gmail.com
 * 
 * This project was made using javafx, utilizing parsing techniques to interpretate 
 * input resulting in a solved equation. This calculator follows the order of operations,
 * guaranteeing the most accurate outcome, all the while offering several quality of life features. 
 *  
 * Features: - Reminiscent front end design
 * 			 - Switching modes from degrees to radians
 * 			 - Several functions
 * 			 - Parentheses-less functions (e.g sqrt25 = 5)
 * 			 - Parentheses-less multiplication (e.g 2PI = 6.28318)
 * 			 - Parenthetical multiplication (e.g 2(3) = 6);
 * 			 - Previous answers built upon (e.g 1 + 1 = 2 -> 2 [* 4] = 8)
 * */

public class Calculator extends Application {
	private static boolean radians = false;
	private static boolean errorOccured = false;
	private boolean newlyReturned;
	
	private static TextField display;
	private Label mode;
	
	private Interpreter interpreter;
	
	public Calculator(){
		interpreter = new Interpreter();
		newlyReturned = false;
		
		createDisplay();
	}
	
    public static void main(String[] args) {
        launch(args);
    }
	
    @Override
    public void start(Stage primaryStage) {
        try {
            BorderPane root = new BorderPane();
            root.setStyle("-fx-background-color: #0d0f17;"); // Set the background color to red
            Scene scene = new Scene(root, 350, 600);

            GridPane gridPane = new GridPane();
            gridPane.setPadding(new Insets(10));
            
            gridPane.setHgap(5);
            gridPane.setVgap(5);

            setUpDisplay(gridPane);
            createLabels(gridPane);
           
            setUpNumButtons(gridPane);
            setUpRightSideButtons(gridPane);
            setUpOtherButtons(gridPane);
            
            root.setCenter(gridPane);

            primaryStage.setScene(scene);
            primaryStage.setTitle("Calculator");
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void setUpDisplay(GridPane pane) {
        // Configure the display TextField
        display.setPrefColumnCount(16);
        display.setEditable(false);
        display.setPrefHeight(60);
        display.setAlignment(Pos.CENTER_RIGHT);
        
        display.setStyle("-fx-background-color: #b6dbb9;"
                       + "-fx-border-color: black;"
                       + "-fx-font-family: 'OCR A Extended';"
                       + "-fx-font-size: 20;");

        // Create a Rectangle as a background for the display
        Rectangle backgroundRectangle = new Rectangle(330, 150);
        backgroundRectangle.setFill(Color.LIGHTGREY);
        pane.add(backgroundRectangle, 0, 0, 7, 8);

        // Set margin for the display
        GridPane.setMargin(display, new Insets(20, 10, 10, 10));
        pane.add(display, 0, 0, 7, 8);
    }
    
    private void createLabels(GridPane pane) {
        // Create and configure the label for the application name
        Label nameLabel = new Label("New York Instruments");
        GridPane.setMargin(nameLabel, new Insets(35, 0, 10, 10));
        
        nameLabel.setStyle("-fx-text-fill: black;"
        				 + "-fx-font-size: 12;"
        				 + "-fx-font-family: 'Roboto', sans-serif;");
       
        pane.add(nameLabel, 0, 0, 7, 1);

        // Create and configure the label for the instrument type
        Label modelLabel = new Label("NC-13ZIIY");
        GridPane.setMargin(modelLabel, new Insets(24, 6, 10, 10));
        GridPane.setHalignment(modelLabel, HPos.RIGHT);
        
        modelLabel.setStyle("-fx-font-weight: bold;"
        				  + "-fx-text-fill: #283240;"
        				  + "-fx-font-size: 20;"
        				  + "-fx-font-family: 'Courier New';");
        
        pane.add(modelLabel, 0, 0, 6, 2);
        

        // Create and configure the label for the mode (radians or degrees)
        mode = new Label(radians ? "rad" : "deg");
        GridPane.setMargin(mode, new Insets(-18, 10, 10, 20));
        
        mode.setStyle("-fx-text-fill: black;"
        			+ "-fx-font-size: 10;"
        			+ "-fx-font-family: 'OCR A Extended';");
        
        pane.add(mode, 0, 1);
    }
    
	private void setUpNumButtons(GridPane gridPane) {
		String[][] buttonLabels = { { "7", "8", "9" }, 
									{ "4", "5", "6" }, 
									{ "1", "2", "3" }, 
									{ "0", ".", "mode" }};

		for (int i = 0; i < buttonLabels.length; i++) {
			for (int j = 0; j < buttonLabels[i].length; j++) {
				Button button = new Button(buttonLabels[i][j]);
				button.setMinSize(60, 50);
				button.setStyle("-fx-background-radius: 20;");
				gridPane.add(button, j + 1, i + 18);
				button.setOnAction(e -> {
					if ("mode".equals(button.getText())) {
						changeMode();
						mode.setText(radians ? "rad" : "deg");
					}
					else {
						handleButtonClick(button.getText());
					}
				});
			}
		}
	}
		
	private void setUpOtherButtons(GridPane gridPane) {
		String[][] buttons = { {"π", "sin", "cos", "tan"},
						       {"^", "sqrt", "(", ")"},
						       {"ln"},
						       {"e"},
						       {"c"}};

		for (int i = 0; i < buttons.length; i++) {
			for (int j = 0; j < buttons[i].length; j++) {
				// Create rectangles behind the buttons to avoid blending in with the background
				Rectangle r = new Rectangle();
				r.setWidth(60);
				r.setHeight(40);
				r.setFill(Color.WHITE);
				
				r.setArcWidth(40.0); 
			    r.setArcHeight(40.0); 
			    
			    // Create and configure each button
			    Button button = new Button(buttons[i][j]);
				
				button.setMinSize(60, 40);
				button.setStyle("-fx-background-radius: 20;"
							  + "-fx-background-color: #06070a;"
							  + "-fx-text-fill: white;");
				
				gridPane.add(r, j , i + 16);
				gridPane.add(button, j , i + 16);
				
				// Setup the action handler
				button.setOnAction(e -> {
					if("c".equals(button.getText())) {
						display.clear();
					}
					else {
						handleButtonClick(button.getText());
					}
				});
			}
		}
	}

	private void setUpRightSideButtons(GridPane gridPane) {
	    String[] buttons = {"%", "/", "*", "-", "+", "="};

	    for (int i = 0; i < buttons.length; i++) {
	        Button button = new Button(buttons[i]);
	        button.setMinSize(60, 50);
	        button.setStyle("-fx-background-radius: 20;"
	        			  + "-fx-background-color: #152b4d;"
	        		      + "-fx-text-fill: white;"
	        		      + "-fx-font-weight: bold;");
	        
	        gridPane.add(button, 5, i + 16);

	        button.setOnAction(e -> {
	            if ("=".equals(button.getText())) {
	            	handleEquals();
	            }
	            else {
	            	handleButtonClick(button.getText());
	            }
	        });
	    }
	}

	private void handleButtonClick(String value) {
		if (errorOccured) {
			display.clear();
			changeErrorMode();
		}
		
		// Will keep previous answer if building upon it with operators
		if (newlyReturned) {
			newlyReturned = false;
			switch(value) {
				case "%","/","*","-","+","^","(":
					break;
				default:
					display.clear();
			}
		}
		String currentText = display.getText();
		display.setText(currentText + value);
	}
    
	private void handleEquals() {
		// Interpret the string expression and return the solved double
		Lexer lexer = new Lexer(display.getText());
		var listOfTokens = lexer.lex();
		
		if(errorOccured) {
			return;
		}
		
		Parser parser = new Parser(listOfTokens);
		var node = parser.parse();
		
		if(errorOccured) {
			return;
		}
		
		double number = interpreter.interpret(node);
		
		if(errorOccured) {
			return;
		}
		
		display.setText(truncateInteger(number));
		newlyReturned = true;
	}
	
	// If the value can be expressed as an integer then chop of the end .0
	private String truncateInteger(double num) {
		String asString = Double.toString(num);
		int len = asString.length();
		
		if(asString.substring(len - 2, len).equals(".0")) {
			asString = asString.substring(0, len - 2);
		}
		
		return(asString);
	}
	
	public static void error() {
		display.setText("ERROR");
		errorOccured = true;
	}
	
    public static boolean inRadians() {
		return radians;
	}
    
	public static void changeMode() {
		radians = !radians;
	} 
	
	private static void createDisplay() {
		display = new TextField();
	}
    
    private static void changeErrorMode() {
    	errorOccured = !errorOccured;
    }
}