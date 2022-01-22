package pl.dmcs.exercises.Calculator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class ONP {

	private final String REG_EX_OP = "[1-9][0-9]*%|[+\\-*/\\(\\)\\^]|[0-9]+(\\.[0-9]+)?|[1-9][0-9]*";
	private final String REG_EX_NUM = "[1-9][0-9]*%|\\-?[0-9]+(\\.[0-9]+)?|\\-?[1-9][0-9]*|[+\\-*/\\(\\)\\^]";
	private final String REG_EX_NEXT_SYMB = "[^+\\-*/\\(\\)\\^\\s\\d\\.%]";

	List<String> getTokens(String input) throws Exception {
		boolean isNumEx = true;
		Pattern pattern;
		String line = input;
		List<String> tokens = new ArrayList<>();

		while (input.length() > 0) {
			if (isNumEx) {
				pattern = Pattern.compile(REG_EX_NUM);
			} else {
				pattern = Pattern.compile(REG_EX_OP);
			}
			isNumEx = false;
			Matcher matcher = pattern.matcher(input);
			matcher.find();
			String symb = matcher.group();
			if (symb.equals("(")) {

				isNumEx = true;
			}
			tokens.add(symb);

			input = input.substring(matcher.end());

			detectInvalidSymbols(input, line);

		}
		return tokens;
	}

	private void detectInvalidSymbols(String input, String line) throws Exception {
		Pattern pattern;
		Matcher matcher;

		if (input.length() > 0) {
			pattern = Pattern.compile(REG_EX_NEXT_SYMB);
			matcher = pattern.matcher(line);
			if (matcher.find()) {
				pattern = Pattern.compile(REG_EX_OP);

				Matcher matcher1 = pattern.matcher(line.substring(0, matcher.start()));
				int ss = 0;
				while (matcher1.find()) {

					ss = matcher1.start();
				}
				throw new Exception("Invalid symbol detected: " + line.substring(ss, matcher.end()));
			}
		}
	}

	List<String> getONP(List<String> s) throws Exception {
		Stack<String> stack = new Stack<>();
		Queue<String> queue = new LinkedList<>();
		Map<String, Integer> precedence = getOperatorsPrecedence();

		boolean wasOperator = false;
		String lastOperator = "";
		int numberOfOpenBrackets = 0;
		for (String token : s) {
			if ("(".equals(token)) {
				stack.push(token);
				numberOfOpenBrackets++;
				continue;
			} else if (")".equals(token)) {
				numberOfOpenBrackets--;
					while (!stack.isEmpty() && !"(".equals(stack.peek())) {
						queue.add(stack.pop());
					}
					if (!stack.isEmpty()) {
						stack.pop();
					}
				continue;
			} else if (precedence.containsKey(token)) {
				// an operator
				if (wasOperator == true) {
					throw new Exception("Missing number between " + lastOperator + " and " + token + " operators");
				} else {
					wasOperator = true;
					lastOperator = token;
				}

				while (!stack.empty() && precedence.get(token) <= precedence.get(stack.peek())) {
					queue.add(stack.pop());
				}
				stack.push(token);
				continue;
			} else {
				// Number
				queue.add(token);
				wasOperator = false;
				continue;
			}

		}

		checkNumberOfBraces(numberOfOpenBrackets);

		while (!stack.isEmpty()) {
			queue.add(stack.pop());
		}

		return new ArrayList<>(queue);
	}

	private Map<String, Integer> getOperatorsPrecedence() {
		Map<String, Integer> precedence = new HashMap<>();
		precedence.put("^", 3);
		precedence.put("/", 2);
		precedence.put("*", 2);
		precedence.put("+", 1);
		precedence.put("-", 1);
		precedence.put("(", 0);
		return precedence;
	}

	private void checkNumberOfBraces(int numberOfOpenBrackets) throws Exception {
		if (numberOfOpenBrackets != 0) {
			if (numberOfOpenBrackets > 0) {
				throw new Exception("An extra left parenthesis detected");
			} else {
				throw new Exception("An extra right parenthesis detected");
			}
		}
	}

	double evaluateONP(List<String> toknes) throws Exception {
		Stack<Double> stack = new Stack<>();
		double secondValue;

		for (String token : toknes) {
			if (token.equals("+")) {
				stack.push(stack.pop() + stack.pop());
			} else if (token.equals("-")) {
				secondValue = stack.pop();
				stack.push(stack.pop() - secondValue);
			} else if (token.equals("*")) {
				stack.push(stack.pop() * stack.pop());
			} else if (token.equals("/")) {
				secondValue = stack.pop();
				if (secondValue == 0) {
					throw new Exception("Division by zero");
				}
				stack.push(stack.pop() / secondValue);
			} else if (token.equals("^")) {
				secondValue = stack.pop();
				stack.push(Math.pow(stack.pop(), secondValue));
			} else {
				if (token.substring(token.length() - 1).equals("%")) {
					token = token.substring(0, token.length() - 1);
					stack.push(Double.parseDouble(token) * 0.01);
				} else {
					stack.push(Double.parseDouble(token));
				}
			}
		}

		return stack.peek();
	}

}
