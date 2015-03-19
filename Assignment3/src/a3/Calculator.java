package a3;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;

/*
 * Assignment 3
 * Jeffrey Kirman (260493368)
 * 
 */
/* ACADEMIC INTEGRITY STATEMENT
 * 
 * By submitting this file, we state that all group members associated
 * with the assignment understand the meaning and consequences of cheating, 
 * plagiarism and other academic offenses under the Code of Student Conduct 
 * and Disciplinary Procedures (see www.mcgill.ca/students/srr for more information).
 * 
 * By submitting this assignment, we state that the members of the group
 * associated with this assignment claim exclusive credit as the authors of the
 * content of the file (except for the solution skeleton provided).
 * 
 * In particular, this means that no part of the solution originates from:
 * - anyone not in the assignment group
 * - Internet resources of any kind.
 * 
 * This assignment is subject to inspection by plagiarism detection software.
 * 
 * Evidence of plagiarism will be forwarded to the Faculty of Science's disciplinary
 * 

/**
 * Main class for the calculator: creates the GUI for the calculator 
 * and responds to presses of the "Enter" key in the text field 
 * and clicking on the button. You do not need to understand or modify 
 * the GUI code to complete this assignment. See instructions below the line
 * BEGIN ASSIGNMENT CODE
 * 
 * @author Martin P. Robillard 26 February 2015
 *
 */
@SuppressWarnings("serial")
public class Calculator extends JFrame implements ActionListener
{
	private static final Color LIGHT_RED = new Color(214,163,182);
	
	private final JTextField aText = new JTextField(40);
	
	public Calculator()
	{
		setTitle("COMP 250 Calculator");
		setLayout(new GridLayout(2, 1));
		setResizable(false);
		add(aText);
		JButton clear = new JButton("Clear");
		clear.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				aText.setText("");		
				aText.requestFocusInWindow();
			}
		});
		add(clear);
		
		aText.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 16));
		aText.addActionListener(this);

		aText.getDocument().addDocumentListener(new DocumentListener()
		{
			@Override
			public void removeUpdate(DocumentEvent e)
			{
				aText.getHighlighter().removeAllHighlights();	
			}
			
			@Override
			public void insertUpdate(DocumentEvent e)
			{
				aText.getHighlighter().removeAllHighlights();
			}
			
			@Override
			public void changedUpdate(DocumentEvent e)
			{
				aText.getHighlighter().removeAllHighlights();
			}
		});
		
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}
	
	/**
	 * Run this main method to start the calculator
	 * @param args Not used.
	 */
	public static void main(String[] args)
	{
		new Calculator();
	}
	
	/* 
	 * Responds to events by the user. Do not modify this method.
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e)
	{
		if( aText.getText().contains("="))
		{
			aText.setText("");		
		}
		else
		{
			Queue<Token> expression = processExpression(aText.getText());
			if( expression != null )
			{
				String result = evaluateExpression(expression);
				if( result != null )
				{
					aText.setText(aText.getText() + " = " + result);
				}
			}
		}
	}
	
	/**
	 * Highlights a section of the text field with a color indicating an error.
	 * Any change to the text field erase the highlights.
	 * Call this method in your own code to highlight part of the equation to 
	 * indicate an error.
	 * @param pBegin The index of the first character to highlight.
	 * @param pEnd The index of the first character not to highlight.
	 */
	public void flagError( int pBegin, int pEnd )
	{
		assert pEnd > pBegin;
		try
		{
			aText.getHighlighter().addHighlight(pBegin, pEnd, new DefaultHighlighter.DefaultHighlightPainter(LIGHT_RED));
		}
		catch(BadLocationException e)
		{
			
		}
	}
	
	/******************** BEGIN ASSIGNMENT CODE ********************/
	
	/**
	 * Tokenizes pExpression (see Tokenizer, below), and 
	 * returns a Queue of Tokens that describe the original 
	 * mathematical expression in reverse Polish notation (RPN).
	 * Flags errors and returns null if the expression
	 * a) contains any symbol except spaces, digits, round parentheses, or operators (+,-,*,/)
	 * b) contains unbalanced parentheses
	 * c) contains invalid combinations of parentheses and numbers, e.g., 2(3)(4)
	 * 
	 * @param pExpression A string.
	 * @return The tokenized expression transformed in RPN
	 */
	private Queue<Token> processExpression(String pExpression)
	{
		Queue<Token> input_queue;
		Stack<Token> operator_stack = new Stack<>();
		Queue<Token> output_queue = new LinkedList<>();
		Tokenizer a = new Tokenizer();
		Token token;
		
		try {
			a.tokenize(pExpression);
		} catch (InvalidExpressionException e) {
			flagError(e.getPosition(), e.getPosition()+1);
			return null;
		}
		
		input_queue = a.getStreamOfTokens();
		
		while (!input_queue.isEmpty()) {
			
			token = input_queue.poll();
			
			// Open parenthesis is pushed into the stack
			if (token.getStringValue().equals("(")) {
				operator_stack.push(token);
			}
			
			// Integers are added to the Queue
			else if (token instanceof IntegerToken) {
				output_queue.add(token);
			}
			
			// Operators are pushed into the stack if it is empty
			// 				 put into the output queue if the operator in the stack has higher precedence
			//				 swapped with the current operator in the stack if it has a equal or lower precedence
			else if (token instanceof OperatorToken) {
				while(true) {
					if (operator_stack.isEmpty()) {
						operator_stack.push(token);
						break;
					}
					else if (operator_stack.peek() instanceof ParenthesisToken) {
						operator_stack.push(token);
						break;
					}
					else if (((OperatorToken)operator_stack.peek()).compareTo((OperatorToken)token) == -1) {
						operator_stack.push(token);
						break;
					}
					else {
						output_queue.add(operator_stack.pop());
					}
				}
			}
			
			// Closed parenthesis triggers the following:
			// Returns null if there is a parenthesis mismatch
			// Pops all operators in the stack into the output queue until an open parenthesis is detected
			else if (token.getStringValue().equals(")")) {
				if (operator_stack.isEmpty()) {
					flagError(token.getStart(), token.getEnd() + 1);
					return null;
				}
				else {
					while (!operator_stack.peek().getStringValue().equals("(")) {
						if (operator_stack.isEmpty()) {
							flagError(token.getStart(), token.getEnd() + 1);
							return null;
						}
						else {
							output_queue.add(operator_stack.pop());
						}
					}
					operator_stack.pop();
				}
			}
		}
		
		
		while (!operator_stack.isEmpty()) {
			if (operator_stack.peek() instanceof ParenthesisToken) {
				flagError(operator_stack.peek().getStart(), operator_stack.peek().getEnd() + 1);
				return null;
			}
			output_queue.add(operator_stack.pop());
		}
		return output_queue;
	}
	
	private void printQueue(Queue<Token> a) {
		Queue<Token> b = new LinkedList<>();
		b.addAll(a);
		Token c;
		
		System.out.println(b.size());
		
		while (!b.isEmpty()) {
			c = b.poll();
			System.out.print("[" + c.getStringValue() + "] ");
		}
		
		System.out.println();
	}
	
	/**
	 * Assumes pExpression is a Queue of tokens produced as the output of processExpression.
	 * Evaluates the answer to the expression. The division operator performs a floating-point 
	 * division. 
	 * Flags errors and returns null if the expression is an invalid RPN expression e.g., 1+-
	 * @param pExpression The expression in RPN
	 * @return A string representation of the answer)
	 */
	private String evaluateExpression(Queue<Token> pExpression)
	{
		Stack<Double> temp_stack = new Stack<>();
		Token token;
		char token_char;
		double a;
		double b;
		Double output = null;
		
		while(!pExpression.isEmpty()) {
			
			token = pExpression.poll();
			token_char = token.getStringValue().charAt(0);
			
			if (token instanceof OperatorToken) {
				
				// Pop the first integer
				if (temp_stack.isEmpty()) {
						flagError(token.getStart(), token.getEnd() + 1);
						return null;
					}
				a = (double)temp_stack.pop();
				
				// Pop the second integer
				if (temp_stack.isEmpty()) {
						flagError(token.getStart(), token.getEnd() + 1);
						return null;
					}
				b = (double)temp_stack.pop();
				
				temp_stack.push(operate(b, a, token_char));
			}
			else {
				temp_stack.push((double)((IntegerToken)token).getTokenValue());
			}
			
		}
		
		// Hint return String.format("%s", <YOUR ANSWER>);
		output = temp_stack.pop();
		return String.format("%s", String.valueOf(output)); // TODO
	}
	
	/**
	 * Performs an operation on two doubles based on an operator char
	 * @param a A double.
	 * @param b A double.
	 * @param operator Either +, -, *, or /
	 * @return a+b, a-b, a*b, or a/b depending on the operator provided.
	 */
	private double operate(double a, double b, char operator) {
		
		switch(operator) {
		case '+':
			return a+b;
		case '-':
			return a-b;
		case '*':
			return a*b;
		case '/':
			return a/b;
		default:
			return a/b;
		}
		
	}
}

/**
 * Use this class as the root class of a hierarchy of token classes
 * that can represent the following types of tokens:
 * a) Integers (e.g., "123" "4", or "345") Negative numbers are not allowed as inputs
 * b) Parentheses '(' or ')'
 * c) Operators '+', '-', '/', '*' Hint: consider using the Comparable interface to support
 * comparing operators for precedence
 */
class Token
{
	private int aStart;
	private int aEnd;
	
	/**
	 * @param pStart The index of the first character of this token
	 * in the original expression.
	 * @param pEnd The index of the last character of this token in
	 * the original expression
	 */
	public Token( int pStart, int pEnd )
	{
		aStart = pStart;
		aEnd = pEnd;
	}
	
	public int getStart()
	{
		return aStart;
	}
	
	public int getEnd()
	{
		return aEnd;
	}
	
	public String toString()
	{
		return "{" + aStart + "," + aEnd + "}";
	}
	
	/**
	 * Gets the String representation of the value of the token
	 * @return String representation of the value of the token
	 */
	public String getStringValue() {
		return null;
	}

}

/**
 * Class for all tokens that are integers.
 * @author Jeffrey
 *
 */
class IntegerToken extends Token {

	private int token_value;
	
	public IntegerToken(int pStart, int pEnd, int token_value) {
		super(pStart, pEnd);
		this.token_value = token_value;
	}
	
	/**
	 * Returns the integer value of the token.
	 * @return The int value of the token.
	 */
	public int getTokenValue() {
		return token_value;
	}
	
	@Override
	public String getStringValue() {
		return Integer.toString(token_value);
	}
	
}

class ParenthesisToken extends Token {

	char token_value;
	
	public ParenthesisToken(int pStart, int pEnd, char token_value) {
		super(pStart, pEnd);
		this.token_value = token_value;

	}
	
	/**
	 * Returns the integer value of the token.
	 * @return The int value of the token.
	 */
	public char getTokenValue() {
		return token_value;
	}
	
	@Override
	public String getStringValue() {
		return Character.toString(token_value);
	}
	
}

class OperatorToken extends Token implements Comparable<OperatorToken> {

	char token_value;
	boolean precedence;
	
	public OperatorToken(int pStart, int pEnd, char token_value) {
		super(pStart, pEnd);
		this.token_value = token_value;
		
		// Parses the the input character into the enumeration value
		if (token_value == '+' || token_value == '-') {
			precedence = false;
		}
		else {
			precedence = true;
		}
		
	}
	
	/**
	 * Returns the integer value of the token.
	 * @return The int value of the token.
	 */
	public char getTokenValue() {
		return token_value;
	}
	
	public String getStringValue() {
		return Character.toString(token_value);
	}

	/**
	 * Compares two OperatorTokens.
	 * @param op_tok The OperatorToken which should be compared with the current OpteratorToken
	 * @return If the OperatorToken of the object is of:
	 * 	lower priority then -1 is returned,
	 * 	equal priority then 0 is returned,
	 * 	higher priority the 1 is returned.
	 */
	@Override
	public int compareTo(OperatorToken op_tok) {
		
		final int LOWER_PRIORITY = -1;
		final int EQUAL_PRIORITY = 0;
		final int HIGHER_PRIORITY = 1;
		
		
		if (this.precedence && !op_tok.precedence) {
			return HIGHER_PRIORITY;
		}
		else if (!this.precedence && op_tok.precedence) {
			return LOWER_PRIORITY;
		}
		else {
			return EQUAL_PRIORITY;
		}
		
	}
	
}

/**
 * Partial implementation of a tokenizer that can convert any valid string
 * into a stream of tokens, or detect invalid strings. Do not change the signature
 * of the public methods, but you can add private helper methods. The signature of the
 * private methods is there to help you out with basic ideas for a design (it is strongly 
 * recommended to use them). Hint: consider making your Tokenizer an Iterable<Token>
 */
class Tokenizer
{
	
	private final static char[] PARENTHESES = {'(', ')'};
	private final static char[] OPERATORS = {'+', '-', '*', '/'};
	private ArrayList<Token> stream_of_tokens;
	private Queue<Character> current_digit_stream = new LinkedList<>();
	private int number_start = 0;
	private int space_count = 0;
	private int number_length = 0;
	
	/**
	 * Converts pExpression into a sequence of Tokens that are retained in
	 * a data structure in this class that can be made available to other objects.
	 * Every call to tokenize should clear the structure and start fresh.
	 * White spaces are tolerated but should be ignored (not converted into tokens).
	 * The presence of any illegal character should raise an exception.
	 * 
	 * @param pExpression An expression to tokenize. Can contain invalid characters.
	 * @throws InvalidExpressionException If any invalid character is detected or if parentheses
	 * are misused as operators.
	 */
	public void tokenize(String pExpression) throws InvalidExpressionException {
		
		stream_of_tokens = new ArrayList<>();
		
		for (int i = 0; i < pExpression.length(); i++) {
			consume(pExpression.charAt(i), i);
		}
		
		if (!current_digit_stream.isEmpty()) {
			stream_of_tokens.add(new IntegerToken(number_start, number_start + number_length, flushDigitStream()));
		}
		
		validate();
		
	}
	
	/**
	 * Checks if a character is a parenthesis.
	 * @param c char to check.
	 * @return True if the char is a parenthesis.
	 */
	public static boolean isParenthesis(char c) {
		
		for (int i = 0; i < PARENTHESES.length; i++) {
			if (c == PARENTHESES[i]) {
				return true;
			}
		}
		return false;
		
	}
	
	/**
	 * Checks if a character is an operator.
	 * @param c char to check.
	 * @return True if the char is an operator.
	 */
	public static boolean isOperator(char c) {
		
		for (int i = 0; i < OPERATORS.length; i++) {
			if (c == OPERATORS[i]) {
				return true;
			}
		}
		return false;
		
	}
	
	private void consume(char pChar, int pPosition) throws InvalidExpressionException {
		
		Integer current_integer;
		
		// Test to see if the character is in the list of allowable characters //

		if (pChar == ' ') {
			space_count++;
			return;
		}
		
		// If the character is a parenthesis flush the digit stream into a token and create the parenthesis token
		else if (isParenthesis(pChar)) {
			current_integer = flushDigitStream();
			
			if (current_integer != null) {
				stream_of_tokens.add(new IntegerToken(number_start, number_start + number_length, current_integer));
			}
			
			stream_of_tokens.add(new ParenthesisToken(pPosition, pPosition, pChar));
			return;
		}
		
		// If the character is an operator flush the digit stream into a token and create the operator token
		else if (isOperator(pChar)) {
			current_integer = flushDigitStream();
			
			if (current_integer != null) {
				stream_of_tokens.add(new IntegerToken(number_start, number_start + number_length, current_integer));
			}
			
			stream_of_tokens.add(new OperatorToken(pPosition, pPosition, pChar));
			return;
		}
		
		// If the character is a digit add it to the digit stream
		else if (Character.isDigit(pChar)) {
			
			if (current_digit_stream.isEmpty()) {
				number_start = pPosition;
				number_length = 0;
			}
			else {
				number_length += space_count + 1;
			}
			space_count = 0;
			
			current_digit_stream.add(pChar);
			return;
		}
		
		// Throw an exception if the character is not valid
		else {
			throw new InvalidExpressionException(pPosition);
		}
		
	}
	
	/**
	 * Converts the current digit stream into an Integer.
	 * @return The integer value of the stream of digits, null if there is no stream of digits.
	 */
	private Integer flushDigitStream() {
		
		char current_char;
		
		if (!current_digit_stream.isEmpty()) {
			
			String token_value = "";
			
			while(!current_digit_stream.isEmpty()) {
				current_char = current_digit_stream.poll();
				token_value = token_value.concat(Character.toString(current_char));
			}
			
			return Integer.parseInt(token_value);
		}
		
		return null;
	}
	
	/**
	 * Detects if parentheses are misused, also detects if there are two
	 * adjacent operators.
	 * @throws InvalidExpressionException
	 */
	private void validate() throws InvalidExpressionException
	{
		
		Token left;
		Token right;
		
		if (stream_of_tokens.isEmpty()) {
			return;
		}
		
		for (int i = 0; i < stream_of_tokens.size() - 1; i++) {
			
			left = stream_of_tokens.get(i);
			right = stream_of_tokens.get(i+1);
			
			// Check open parenthesis condition
			if (right.getStringValue().equals("(")) {
				if (!left.getStringValue().equals("(") &&
						!(left instanceof OperatorToken)) {
					throw new InvalidExpressionException(left.getStart());
				}
			}
			
			// Check close parenthesis condition
			if (left.getStringValue().equals(")")) {
				if (!right.getStringValue().equals(")") &&
						!(right instanceof OperatorToken)) {
					throw new InvalidExpressionException(right.getStart());
				}
			}
			
			// Checks if there are two adjacent operators. Misuse of operators can be
			// checked later but this causes issues in the correct operator being highlighted
			// because of the precedence of operators in the RPN
			if ((left instanceof OperatorToken) && (right instanceof OperatorToken)) {
				throw new InvalidExpressionException(left.getStart());
			}
			
		}
			
		// An easy way to detect if parentheses are misused is 
		// to look for any opening parenthesis preceded by a token that
		// is neither an operator nor an opening parenthesis, and for any
		// closing parenthesis that is followed by a token that is
		// neither an operator nor a closing parenthesis. Don't check for
		// unbalanced parentheses here, you can do it in processExpression
		// directly as part of the Shunting Yard algorithm.
		// Call this method as the last statement in tokenize.
	}
	
	public Queue<Token> getStreamOfTokens() {
		
		Queue<Token> output = new LinkedList<>();
		output.addAll(stream_of_tokens);

		return output;
	}

}

/**
 * Thrown by the Tokenizer if an expression is detected to be invalid.
 * You don't need to modify this class.
 */
@SuppressWarnings("serial")
class InvalidExpressionException extends Exception
{
	private int aPosition;
	
	public InvalidExpressionException( int pPosition )
	{
		aPosition = pPosition;
	}
	
	public int getPosition()
	{
		return aPosition;
	}
}