package org.common.chaos.model;

import java.util.Map;

import org.common.chaos.enums.ChaosAssaultType;
import org.common.chaos.enums.ChaosParameterKeysConstant;

public class ChaosAssault {
	
	private String experimentId;
	
	private ChaosAssaultType assaultType;
	
	private Map<ChaosParameterKeysConstant,Object> assaultParameters;
	
	public String getExperimentId() {
		return experimentId;
	}

	public ChaosAssaultType getAssaultType() {
		return assaultType;
	}

	public Map<ChaosParameterKeysConstant, Object> getAssaultParameters() {
		return assaultParameters;
	}
	
	public ChaosAssault() {
		super();
	}

	public ChaosAssault(String experimentId, ChaosAssaultType assaultType, Map<ChaosParameterKeysConstant, Object> assaultParameters) {
		super();
		this.experimentId = experimentId;
		this.assaultType = assaultType;
		this.assaultParameters = assaultParameters;
	}

	@Override
	public String toString() {
		return "ChaosAssault [experimentId=" + experimentId + ", assaultType=" + assaultType + ", assaultParameters="
				+ assaultParameters + "]";
	}

	public void setExperimentId(String experimentId) {
		this.experimentId = experimentId;
	}

	public void setAssaultType(ChaosAssaultType assaultType) {
		this.assaultType = assaultType;
	}

	public void setAssaultParameters(Map<ChaosParameterKeysConstant, Object> assaultParameters) {
		this.assaultParameters = assaultParameters;
	}
	
	
	
}
