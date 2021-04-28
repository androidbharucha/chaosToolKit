package org.common.chaos.assault.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.common.chaos.datastore.ActiveChaosDataStore;
import org.common.chaos.datastore.StaticChaosDataStore;
import org.common.chaos.enums.ChaosAssaultType;
import org.common.chaos.enums.ChaosParameterKeysConstant;
import org.common.chaos.model.ChaosAssault;
import org.common.chaos.model.HTTPChaos;
import org.common.chaos.model.MutantTest;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CreateChaosServiceImpl implements ICreateChaosService {

	private static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

	@Override
	public void createHTTPChaos(HTTPChaos httpChaos) {

		Map<ChaosParameterKeysConstant, Object> assaultParameters = new HashMap<>();
		assaultParameters.put(ChaosParameterKeysConstant.CHAOS_THROWBLE_EXCEPTION,
				new ResponseStatusException(httpChaos.getHttpStatus(), httpChaos.getErrorResponseMessage()));
		ChaosAssault httpChaosAssault = new ChaosAssault(httpChaos.getExperimentID(), ChaosAssaultType.HTTP_ASSAULT,

				assaultParameters);
		httpChaos.getChaosEndpoints().forEach(chaosEndpoint -> {

			if (StaticChaosDataStore.isHTTPAssaultAllowedOnEndPoint(chaosEndpoint))
				ActiveChaosDataStore.addActiveChaos(chaosEndpoint, httpChaosAssault);
		});

		clearChaosAsynchroneously(httpChaos.getExperimentID() , httpChaos.getChaosEndpoints(),ChaosAssaultType.HTTP_ASSAULT.name());

	}

	public void clearChaosAsynchroneously(String experimentId, List<String> endPoints, String chaosAssaultType) {
		scheduler.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				endPoints.forEach((endpoint) -> {
					clearChaos(experimentId, endpoint, chaosAssaultType);
				});

			}
		}, new Long(120), TimeUnit.DAYS.toSeconds(1), TimeUnit.SECONDS);
	}

	@Override
	public void createMutantChaos(MutantTest mutantTest) {

		Map<ChaosParameterKeysConstant, Object> assaultParameters = new HashMap<>();
		assaultParameters.put(ChaosParameterKeysConstant.CHAOS_THROWBLE_EXCEPTION,
				mutantTest.getChaosEndpoints2Mutants());
		ChaosAssault mutantTestAssault = new ChaosAssault(mutantTest.getExperimentID(),
				ChaosAssaultType.MUTANT_TEST_ASSAULT, assaultParameters);

		mutantTest.getChaosEndpoints2Mutants().forEach((endPoint, mutantsOnEndpoint) -> {
			if (StaticChaosDataStore.isMutantAssaultAllowedOnEndPoint(endPoint))
				ActiveChaosDataStore.addActiveChaos(endPoint, mutantTestAssault);
		});


		clearChaosAsynchroneously(mutantTest.getExperimentID(), new ArrayList<String>(mutantTest.getChaosEndpoints2Mutants().keySet()),ChaosAssaultType.MUTANT_TEST_ASSAULT.name());
		

	}

	@Override
	public void clearChaos(String experimentId, String endPointName, String chaosAssaultType) {

		System.out.println("Chaos experiment '" + experimentId + "' ,endpoint '" + endPointName + "' , assault type '"
				+ chaosAssaultType + "' will be removed ");
		ActiveChaosDataStore.removeChaos(experimentId, endPointName, chaosAssaultType);

	}

}
