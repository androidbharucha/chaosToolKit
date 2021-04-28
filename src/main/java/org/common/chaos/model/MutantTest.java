package org.common.chaos.model;

import java.util.List;
import java.util.Map;

public class MutantTest {
	private String experimentID;

	private Map<String, List<String>> chaosEndpoints2Mutants;
	
	private String endpointname;
	
	private List<String> mutantsName;

	public MutantTest() {
		super();
	}


	
	public MutantTest(String experimentID, Map<String, List<String>> chaosEndpoints2Mutants, String endpointname,
			List<String> mutantsName) {
		super();
		this.experimentID = experimentID;
		this.chaosEndpoints2Mutants = chaosEndpoints2Mutants;
		this.endpointname = endpointname;
		this.mutantsName = mutantsName;
	}

	public String getExperimentID() {
		return experimentID;
	}

	public void setExperimentID(String experimentID) {
		this.experimentID = experimentID;
	}

	public Map<String, List<String>> getChaosEndpoints2Mutants() {
		return chaosEndpoints2Mutants;
	}

	public void setChaosEndpoints2Mutants(Map<String, List<String>> chaosEndpoints2Mutants) {
		this.chaosEndpoints2Mutants = chaosEndpoints2Mutants;
	}

	public String getEndpointname() {
		return endpointname;
	}

	public void setEndpointname(String endpointname) {
		this.endpointname = endpointname;
	}

	public List<String> getMutantsName() {
		return mutantsName;
	}

	public void setMutantsName(List<String> mutantsName) {
		this.mutantsName = mutantsName;
	}
	
	
	

}
