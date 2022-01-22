package pl.dmcs.exercises.Calculator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class AdvancedStringCalculatorTest {

	private AdvancedStringCalculator advancedStringCalculator = new AdvancedStringCalculator();

	@Test
	void testBasicOperations() throws Exception {

		testCompute("1+2.5", 3.5);
		testCompute("5+2", 7);
		testCompute("1.2+2", 3.2);
		testCompute("1.2+2+1", 4.2);
		testCompute("2-2", 0);
		testCompute("2-1", 1);
		testCompute("2*3.5", 7);
		testCompute("44*20", 880);
		testCompute("10/4", 2.5);
		testCompute("200/1", 200);
		testCompute("2^4", 16);
		testCompute("3^3", 27);
	}

	@Test
	void testWhitespaces() throws Exception {
		testCompute("2.25     *    3", 6.75);
		testCompute("5  +2", 7);
		testCompute("2     -        2 +         3    *    2", 6);
	}

	@Test
	void testComplexOperations() throws Exception {
		testCompute("1.5 + 2 * 3^2", 19.5);
		testCompute("1.5+3*2^2", 13.5);
		testCompute("5+2*2/4+6-7", 5);
		testCompute("2^3*6-2   -2 * 3", 40);
	}

	@Test
	void testBrackets() throws Exception {
		testCompute("(1.5 + 2) * 3^2", 31.5);
		testCompute("((2.4 - 0.4) * 3)^4", 1296);
		testCompute("((2 * 2))^(1+1)", 16);
	}

	@Test
	void testInvalidArguments() throws Exception {
		testComputeException("1 -* 3", "Missing number between - and * operators");
		testComputeException("1 ++- 3", "Missing number between + and + operators");
		testComputeException("(1 * 3)-2)", "An extra right parenthesis detected");
		testComputeException("(1 * 3))-2)", "An extra right parenthesis detected");
		testComputeException("((1 + 1)", "An extra left parenthesis detected");
		testComputeException("(((((((1", "An extra left parenthesis detected");
		testComputeException("2a * 3", "Invalid symbol detected: 2a");
		testComputeException("2 * 3 +a", "Invalid symbol detected: +a");
		testComputeException(" 1 + frh89fj099", "Invalid symbol detected: + f");
	}

	@Test
	void testDivisionByZero() throws Exception {
		testComputeException("1/0", "Division by zero");
		testComputeException("1 /	(3-3)", "Division by zero");
		testComputeException("((1 * 3 + 4 )-2^(3/(4-2*2)))", "Division by zero");
	}

	@Test
	void testPercentNumbers() throws Exception {
		testCompute("50%", 0.5);
		testCompute("2 * 75%", 1.5);
		testCompute("1.5+50%*2^2", 3.5);
		testCompute("3 * 4 * 5 * 25%", 15);
		testCompute("10 / 50%", 20);
		testCompute("(30-10) / 25%", 80);
	}

	private void testCompute(String s, double expected) throws Exception {
		assertEquals(expected, advancedStringCalculator.compute(s));
	}

	private void testComputeException(String s, String message) {
		Exception exception = assertThrows(Exception.class, () -> advancedStringCalculator.compute(s));
		assertEquals(message, exception.getMessage());
	}

}
