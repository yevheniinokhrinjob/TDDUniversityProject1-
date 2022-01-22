package pl.dmcs.exercises.Calculator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

class ONPTest {

	private ONP onp = new ONP();

	@Test
	void testGetResults() throws Exception {
		testGetTokens("1+2.5", Arrays.asList("1", "+", "2.5"));
		testGetTokens("1-2.5", Arrays.asList("1", "-", "2.5"));
		testGetTokens("(5+2)^5", Arrays.asList("(", "5", "+", "2", ")", "^", "5"));
		testGetTokens("-2*(-5-2)", Arrays.asList("-2", "*", "(", "-5", "-", "2", ")"));
		testGetTokens("1.2+((2+1))", Arrays.asList("1.2", "+", "(", "(", "2", "+", "1", ")", ")"));
		testGetTokens(".2 -.2 *0.3 +0.1", Arrays.asList("2", "-", "2", "*", "0.3", "+", "0.1"));
		testGetTokens("(2.0 - 2) 13.010 3^2", Arrays.asList("(", "2.0", "-", "2", ")", "13.010", "3", "^", "2"));
		testGetTokens("2 * 75%", Arrays.asList("2", "*", "75%"));
		testGetTokens("50%", Arrays.asList("50%"));
		testGetTokens("(1 - 15) * 13 + 5%", Arrays.asList("(", "1", "-", "15", ")", "*", "13", "+", "5%"));
	}

	@Test
	void testBasicGetONP() throws Exception {
		testGetONP(Arrays.asList("1", "+", "2.5"), Arrays.asList("1", "2.5", "+"));
		testGetONP(Arrays.asList("(", "2", "+", "3", ")", "*", "5"), Arrays.asList("2", "3", "+", "5", "*"));
		testGetONP(Arrays.asList("(", "(", "2", "+", "7", ")", "/", "3", "+", "(", "14", "-", "3", ")", "*", "4", ")",
				"/", "2"), Arrays.asList("2", "7", "+", "3", "/", "14", "3", "-", "4", "*", "+", "2", "/"));
	}

	@Test
	void testBasicEvaluateONP() throws Exception {
		testEvaluateONP(Arrays.asList("1", "2.5", "+"), 3.5);
		testEvaluateONP(Arrays.asList("2", "3", "+", "5", "*"), 25);
		testEvaluateONP(Arrays.asList("2", "7", "+", "3", "/", "14", "3", "-", "4", "*", "+", "2", "/"), 23.5);
	}

	private void testGetTokens(String s, List<String> expected) throws Exception {
		assertIterableEquals(expected, onp.getTokens(s));
	}

	private void testGetONP(List<String> s, List<String> expected) throws Exception {
		assertIterableEquals(expected, onp.getONP(s));
	}

	private void testEvaluateONP(List<String> s, double expected) throws Exception {
		assertEquals(expected, onp.evaluateONP(s));
	}
}
