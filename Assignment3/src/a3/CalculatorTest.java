package a3;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.Queue;

import org.junit.Test;

public class CalculatorTest {

	@Test
	public void testTokenize() {
		
		final String TEST_EXPRESSION = "4 + 33 * (3-   2    1)/7";
		Queue<Token> TOKEN_LIST = new LinkedList<>();
		Queue<Token> test_token_list = new LinkedList<>();
		Token expected;
		Token test;
		int counter = 0;
		
		// Construct correct queue
		TOKEN_LIST.add(new IntegerToken(0,0,4));
		TOKEN_LIST.add(new OperatorToken(2,2,'+'));
		TOKEN_LIST.add(new IntegerToken(4,5,33));
		TOKEN_LIST.add(new OperatorToken(7,7,'*'));
		TOKEN_LIST.add(new ParenthesisToken(9,9,'('));
		TOKEN_LIST.add(new IntegerToken(10,10,3));
		TOKEN_LIST.add(new OperatorToken(11,11,'-'));
		TOKEN_LIST.add(new IntegerToken(15,20,21));
		TOKEN_LIST.add(new ParenthesisToken(21,21,')'));
		TOKEN_LIST.add(new OperatorToken(22,22,'/'));
		TOKEN_LIST.add(new IntegerToken(23,23,7));
		
		Tokenizer a = new Tokenizer();
		try {
			a.tokenize(TEST_EXPRESSION);
		} catch (InvalidExpressionException e) {
			fail("Tokenizer threw an invalid expression exception.");
			e.printStackTrace();
		}
		test_token_list = a.getStreamOfTokens();
		
		assertEquals("Size of queue not correct.", TOKEN_LIST.size(), test_token_list.size());
		
		while(!test_token_list.isEmpty()) {
			expected = TOKEN_LIST.poll();
			test = test_token_list.poll();
			assertEquals("Token at position " + counter + " not same start position.", expected.getStart(), test.getStart());
			assertEquals("Token at position " + counter + " not same end position.", expected.getEnd(), test.getEnd());
			assertEquals("Token at position " + counter + " not same value.", expected.getStringValue(), test.getStringValue());
			counter++;
		}
		
		
	}
	
}
