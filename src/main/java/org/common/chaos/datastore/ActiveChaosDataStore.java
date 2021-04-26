package org.common.chaos.datastore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.common.chaos.model.ChaosAssault;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * This class provides information about active chaosAssaults, experiments ids
 * and their associated endpoints.
 * 
 * @author ANSAP_ADMIN
 *
 */
public class ActiveChaosDataStore {

	private static Map<String, List<ChaosAssault>> activeChaosByEndpoint = new HashMap<>();

	public static Map<String, List<ChaosAssault>> activeExperiments = new HashMap<>();

	public static void addActiveChaos(String endpointSignature, ChaosAssault assault) {
		 
		if (activeChaosByEndpoint.containsKey(endpointSignature)) {
			List<ChaosAssault> assaultsOnEndpoint = activeChaosByEndpoint.get(endpointSignature);
			assaultsOnEndpoint.add(assault);
			activeChaosByEndpoint.put(endpointSignature, assaultsOnEndpoint);

		} else {
			List<ChaosAssault> assaultsOnEndpoint = new ArrayList();
			assaultsOnEndpoint.add(assault);
			activeChaosByEndpoint.put(endpointSignature, assaultsOnEndpoint);
		}
	}	
	public static void removeChaos(String experimentId, String endPointName, String chaosAssaultType) {

		for (Map.Entry<String, List<ChaosAssault>> endpointswithchaos : activeChaosByEndpoint.entrySet()) { // TODO
																											// Check if
																											// iterator
																											// is
																											// required
																											// here
			Iterator<ChaosAssault> chaosAssaultIterator = endpointswithchaos.getValue().listIterator();
			while (chaosAssaultIterator.hasNext()) {
				ChaosAssault chaosAssault = chaosAssaultIterator.next();
				if (chaosAssault.getExperimentId().equalsIgnoreCase(experimentId)
						&& chaosAssault.getAssaultType().name().equalsIgnoreCase(chaosAssaultType)
						&& endpointswithchaos.getKey().equalsIgnoreCase(endPointName)) {
					chaosAssaultIterator.remove();
				}
			}
		}

	}

	public static boolean isThereActiveChaosOnMethod(String methodSignature) {

		String endPointNameForMethod = StaticChaosDataStore.methodSignature2endpointName.get(methodSignature);

		return (activeChaosByEndpoint.containsKey(endPointNameForMethod)
				&& activeChaosByEndpoint.get(endPointNameForMethod) != null
				&& activeChaosByEndpoint.get(endPointNameForMethod).size() > 0);

	}
	
	public static List<ChaosAssault> getActiveChaosAssaultsOnMethod(String methodSignature) {

		String endPointNameForMethod = StaticChaosDataStore.methodSignature2endpointName.get(methodSignature);

		return  activeChaosByEndpoint.get(endPointNameForMethod);

	}
	

}
