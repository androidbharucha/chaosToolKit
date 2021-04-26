package org.common.chaos.enums;

public enum ChaosAssaultType {

	LATENCY_ASSAULT("LATENCY_ASSAULT"), EXCEPTION_ASSAULT("LATENCY_ASSAULT"), HTTP_ASSAULT("HTTP_ASSAULT") , MUTANT_TEST_ASSAULT("MUTANT_TEST_ASSAULT");

	private String chaosAssaultType;

	private ChaosAssaultType(String chaosAssaultType) {
		this.chaosAssaultType = chaosAssaultType;
	}
}
