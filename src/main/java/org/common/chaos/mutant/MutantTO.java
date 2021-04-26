package org.common.chaos.mutant;

public class MutantTO {
	
	private String mutantName;
	
	private String endpointName;
	
	private String methodSignature;

	public String getMutantName() {
		return mutantName;
	}

	public void setMutantName(String mutantName) {
		this.mutantName = mutantName;
	}

	public String getEndpointName() {
		return endpointName;
	}

	public void setEndpointName(String endpointName) {
		this.endpointName = endpointName;
	}

	public String getMethodSignature() {
		return methodSignature;
	}

	public void setMethodSignature(String methodSignature) {
		this.methodSignature = methodSignature;
	}

	public MutantTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MutantTO(String mutantName, String endpointName, String methodSignature) {
		super();
		this.mutantName = mutantName;
		this.endpointName = endpointName;
		this.methodSignature = methodSignature;
	}
	
	
	
	
	

}
