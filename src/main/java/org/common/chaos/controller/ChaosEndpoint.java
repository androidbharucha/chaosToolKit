package org.common.chaos.controller;

import java.util.List;
import java.util.Map;

import org.common.chaos.annotation.EnableChaos;
import org.common.chaos.annotation.Mutant;
import org.common.chaos.assault.service.ICreateChaosService;
import org.common.chaos.datastore.StaticChaosDataStore;
import org.common.chaos.enums.ChaosAssaultType;
import org.common.chaos.model.HTTPChaos;
import org.common.chaos.model.MutantTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RestController
@RequestMapping(value = "/chaos")
public class ChaosEndpoint {

	@Autowired
	ICreateChaosService createChaosServiceImpl;
	
	
	public static final String SMOKE_TEST_200_MSG = "There is no Chaos assault active at present ! To smoke test if Chaos config is active , enable any chaos assault on chaosendpoint 'SmokeTestForChaosConfig'.";
	
	public static final String SMOKE_TEST_MUTANT_1_MSG = "Era of mutant has arrived !";
	
	public static final String SMOKE_TEST_MUTANT_2_MSG = "Era of X Man  has arrived !";
	
	public static final String SMOKE_TEST_ENDPOINT_NAME = "SmokeTestForChaosConfig";

	@RequestMapping(value = "/chaosPoints/smoketest/{message}", method = RequestMethod.GET)
	@EnableChaos(chaosEndpointname = SMOKE_TEST_ENDPOINT_NAME, supportedChaosTypes = { ChaosAssaultType.HTTP_ASSAULT,
			ChaosAssaultType.EXCEPTION_ASSAULT, ChaosAssaultType.LATENCY_ASSAULT,
			ChaosAssaultType.MUTANT_TEST_ASSAULT })
	public String smokeTestForChaos(@PathVariable String message) {
		return SMOKE_TEST_200_MSG;
	}

	
	@Mutant(mutantName = "mutant1" , chaosEndpointname = SMOKE_TEST_ENDPOINT_NAME)
	public String smokeTestForChaosMutant1(String message) {
		return SMOKE_TEST_MUTANT_1_MSG;
	}
	
	@Mutant(mutantName = "mutant2" , chaosEndpointname = SMOKE_TEST_ENDPOINT_NAME)
	public String smokeTestForChaosMutant2(String message) {
		return SMOKE_TEST_MUTANT_2_MSG;
	}
	

	@RequestMapping(value = "/chaosPoints", method = RequestMethod.GET)
	@EnableChaos(chaosEndpointname = "getChaosPoints", supportedChaosTypes = ChaosAssaultType.HTTP_ASSAULT)
	public Map<String, List<ChaosAssaultType>> getChaosPoints(String message) {
		return StaticChaosDataStore.enabledChaosByEndpoint;
	}

	@RequestMapping(value = "/assault/http", method = RequestMethod.POST)
	public HTTPChaos createHTTPAssault(@RequestBody HTTPChaos httpChaos) {

		createChaosServiceImpl.createHTTPChaos(httpChaos);
		return httpChaos;
	}
	
	@RequestMapping(value = "/assault/mutant", method = RequestMethod.POST)
	public MutantTest createMutantAssault(@RequestBody MutantTest mutantTest) {

		createChaosServiceImpl.createMutantChaos(mutantTest);
		return mutantTest;
	}
	
//	@RequestMapping(value = "/assault/mutant", method = RequestMethod.GET)
//	public MutantTest createMutantAssault() {
//
//		Map<String, List<String>> chaosEndpointToMutant = new HashMap<>();
//		chaosEndpointToMutant.put("SmokeTestForChaosConfig",
//				Arrays.asList("smokeTestForChaosMutant", "smokeTestForChaosMutant2"));
//
//		MutantTest mutantTest = new MutantTest("experiement_mutant_1" , chaosEndpointToMutant , " SmokeTestForChaosConfig" , Arrays.asList("smokeTestForChaosMutant", "smokeTestForChaosMutant2"));
//
//		return mutantTest;
//	}
	
	
	
	
}
