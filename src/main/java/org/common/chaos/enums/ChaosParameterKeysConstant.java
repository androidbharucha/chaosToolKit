package org.common.chaos.enums;

public enum ChaosParameterKeysConstant {

	CHAOS_THROWBLE_EXCEPTION("CHAOS_THROWBLE_EXCEPTION") , MUTANT_TESTS("MUTANT_TESTS");

	private String chaosParameterKey;

	private ChaosParameterKeysConstant(String chaosParameterKey) {
		this.chaosParameterKey = chaosParameterKey;
	}
}
