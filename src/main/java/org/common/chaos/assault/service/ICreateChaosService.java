package org.common.chaos.assault.service;

import org.common.chaos.model.HTTPChaos;
import org.common.chaos.model.MutantTest;

public interface ICreateChaosService {

	public void createHTTPChaos(HTTPChaos httpChaos);
	
	public void clearChaos(String experimentId , String endPointName, String chaosAssaultType) ;
	
	public void createMutantChaos(MutantTest mutantTest); 
}
