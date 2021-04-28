package org.common.chaos.model;

import java.util.List;

import org.springframework.http.HttpStatus;

public class HTTPChaos {
	
	private String experimentID;
	
	private HttpStatus httpStatus;
	
	private String errorResponseMessage;
	
	private List<String> chaosEndpoints;

	public String getExperimentID() {
		return experimentID;
	}

	public void setExperimentID(String experimentID) {
		this.experimentID = experimentID;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}

	public String getErrorResponseMessage() {
		return errorResponseMessage;
	}

	public void setErrorResponseMessage(String errorResponseMessage) {
		this.errorResponseMessage = errorResponseMessage;
	}

	public List<String> getChaosEndpoints() {
		return chaosEndpoints;
	}

	public void setChaosEndpoints(List<String> chaosEndpoints) {
		this.chaosEndpoints = chaosEndpoints;
	}

	@Override
	public String toString() {
		return "HTTPChaos [experimentID=" + experimentID + ", httpStatus=" + httpStatus + ", errorResponseMessage="
				+ errorResponseMessage + ", chaosEndpoints=" + chaosEndpoints + "]";
	}

	public HTTPChaos(String experimentID, HttpStatus httpStatus, String errorResponseMessage,
			List<String> chaosEndpoints) {
		super();
		this.experimentID = experimentID;
		this.httpStatus = httpStatus;
		this.errorResponseMessage = errorResponseMessage;
		this.chaosEndpoints = chaosEndpoints;
	}

	public HTTPChaos() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	

}
