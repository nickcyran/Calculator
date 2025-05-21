# Calculator App
![Status](https://img.shields.io/badge/status-completed-brightgreen)

This Calculator App is a Java-based application built using JavaFX libraries. It is designed to interpret mathematical expressions input by the user, respecting the order of operations to deliver accurate results. The application features a user interface reminiscent of a classic calculator and includes several quality-of-life features.

## Features

* **Familiar Interface**: A front-end design that mimics traditional calculators.
* **Mode Switching**: Ability to switch calculations between degrees and radians.
* **Mathematical Functions**: Supports a variety of functions including:
    * Basic arithmetic operations (+, -, \*, /, %)
    * Exponents (^)
    * Square Root (sqrt)
    * Natural Logarithm (ln)
    * Trigonometric functions (sin, cos, tan)
* **Flexible Input**:
    * **Parentheses-less functions**: For example, `sqrt25` is treated as `sqrt(25)`.
    * **Parentheses-less multiplication**: For example, `2PI` is interpreted as `2 * PI`.
    * **Parenthetical multiplication**: For example, `2(3)` is calculated as `2 * 3`.
* **Continuous Calculation**: Previous answers can be used as a starting point for new calculations (e.g., `1 + 1 = 2`, then `2 * 4 = 8`).
* **Error Handling**: Displays "ERROR" for invalid operations, such as division by zero.

## How It Works

The calculator processes input through the following stages:

1.  **Lexer**: Tokenizes the input string into a sequence of numbers, operators, functions, and parentheses.
2.  **Parser**: Takes the list of tokens and constructs an expression tree (Abstract Syntax Tree - AST) respecting mathematical precedence and associativity.
3.  **Interpreter**: Evaluates the expression tree to produce the final numerical result.

## Technologies Used

* Java
* JavaFX

## Setup and Usage

*(You can add instructions here on how to compile and run the project, if applicable. For example:)*

1.  Ensure you have Java and JavaFX installed and configured.
2.  Compile the Java files.
3.  Run the `Calculator.java` main class.
