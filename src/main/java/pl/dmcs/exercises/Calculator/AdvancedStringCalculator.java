package pl.dmcs.exercises.Calculator;

class AdvancedStringCalculator {

	private ONP onp;

	public AdvancedStringCalculator() {
		this.onp = new ONP();
	}

	double compute(String s) throws Exception {

		return onp.evaluateONP(onp.getONP(onp.getTokens(s)));
	}

}
