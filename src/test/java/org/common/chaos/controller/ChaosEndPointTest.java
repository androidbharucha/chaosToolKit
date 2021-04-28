package org.common.chaos.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.common.chaos.assault.service.ICreateChaosService;
import org.common.chaos.controller.ChaosEndpoint;
import org.common.chaos.datastore.ActiveChaosDataStore;
import org.common.chaos.enums.ChaosAssaultType;
import org.common.chaos.model.HTTPChaos;
import org.common.chaos.model.MutantTest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ChaosEndPointTest {

	@LocalServerPort
	private int port;

	private static final String CONTEXT_PATH_GET_SMOKETEST = "/chaos/chaosPoints/smoketest/mutanttest";

	private static final String CONTEXT_PATH_POST_MUTANTTEST = "/chaos/assault/mutant";

	private static final String CONTEXT_PATH_POST_HTTPASSAULT = "/chaos/assault/http";

	private static final String CONTEXT_PATH_GET_STATIC_CHAOS_DATA = "/chaos/chaosPoints";

	private HttpHeaders getHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		return headers;
	}

	@Autowired
	private TestRestTemplate restTemplate;

	
	@Autowired
	private ICreateChaosService createChaosService;
	
	
	@Test
	public void testMutantTestchaos() throws Exception {
		
		String MUTANT_EXPERIMENT_ID = "experiement_mutant_1";
		assertThat(this.restTemplate.getForObject("http://localhost:" + port + CONTEXT_PATH_GET_SMOKETEST,
				String.class)).contains(
						ChaosEndpoint.SMOKE_TEST_200_MSG);
		assertEquals(ChaosEndpoint.SMOKE_TEST_MUTANT_1_MSG, new ChaosEndpoint().smokeTestForChaosMutant1(""));
		assertEquals(ChaosEndpoint.SMOKE_TEST_MUTANT_2_MSG, new ChaosEndpoint().smokeTestForChaosMutant2(""));
		

		Map<String, List<String>> chaosEndpointToMutant = new HashMap<>();
		chaosEndpointToMutant.put(ChaosEndpoint.SMOKE_TEST_ENDPOINT_NAME,
				Arrays.asList("smokeTestForChaosMutant", "smokeTestForChaosMutant2"));

		MutantTest mutantTest = new MutantTest(MUTANT_EXPERIMENT_ID, chaosEndpointToMutant, ChaosEndpoint.SMOKE_TEST_ENDPOINT_NAME,
				Arrays.asList("smokeTestForChaosMutant", "smokeTestForChaosMutant2"));
		
		System.out.println(mutantTest);

		HttpEntity<String> request = new HttpEntity<String>(new ObjectMapper().writeValueAsString(mutantTest),
				getHeaders());

		ResponseEntity<MutantTest> mutantTestResponseEntity = this.restTemplate
				.postForEntity("http://localhost:" + port + CONTEXT_PATH_POST_MUTANTTEST, request, MutantTest.class);
		System.out.println(mutantTestResponseEntity.getBody());
		assertThat(MUTANT_EXPERIMENT_ID.equals(mutantTestResponseEntity.getBody().getExperimentID()));

		String smokeTestForMutantresponse = this.restTemplate
				.getForObject("http://localhost:" + port + CONTEXT_PATH_GET_SMOKETEST, String.class);

		assertThat(ChaosEndpoint.SMOKE_TEST_MUTANT_1_MSG.equals(smokeTestForMutantresponse)
				|| ChaosEndpoint.SMOKE_TEST_MUTANT_2_MSG.equals(smokeTestForMutantresponse));

		createChaosService.clearChaos(MUTANT_EXPERIMENT_ID, ChaosEndpoint.SMOKE_TEST_ENDPOINT_NAME,
				ChaosAssaultType.MUTANT_TEST_ASSAULT.name());
		
//		ActiveChaosDataStore.removeChaos(MUTANT_EXPERIMENT_ID, ChaosEndpoint.SMOKE_TEST_ENDPOINT_NAME,
//				ChaosAssaultType.MUTANT_TEST_ASSAULT.name());
		
		assertThat(this.restTemplate.getForObject("http://localhost:" + port + CONTEXT_PATH_GET_SMOKETEST,
				String.class)).contains(
						ChaosEndpoint.SMOKE_TEST_200_MSG);


	}

	@Test
	public void testHTTPChaos() throws Exception {
		
		String HTTP_CHAOS_EXPERIMENT_ID = "http_chaos_unit_test";
		String HTTP_CHAOS_ERR_MSG = "HTTP 500 error through Chaos";
		
		
		assertThat(this.restTemplate.getForObject("http://localhost:" + port + CONTEXT_PATH_GET_SMOKETEST,
				String.class)).contains(
						ChaosEndpoint.SMOKE_TEST_200_MSG);
		HTTPChaos httpChaos = new HTTPChaos(HTTP_CHAOS_EXPERIMENT_ID, HttpStatus.INTERNAL_SERVER_ERROR,
				"HTTP 500 error through Chaos", Arrays.asList(ChaosEndpoint.SMOKE_TEST_ENDPOINT_NAME));
		
		System.out.println(httpChaos);
		

		HttpEntity<String> request = new HttpEntity<String>(new ObjectMapper().writeValueAsString(httpChaos),
				getHeaders());

		ResponseEntity<HTTPChaos> httpChaosResults = this.restTemplate
				.postForEntity("http://localhost:" + port + CONTEXT_PATH_POST_HTTPASSAULT, request, HTTPChaos.class);

		assertEquals(HTTP_CHAOS_EXPERIMENT_ID, httpChaosResults.getBody().getExperimentID());

		ResponseEntity<String> testHTTPResponsePostHTTPAssault = this.restTemplate
				.getForEntity("http://localhost:" + port + CONTEXT_PATH_GET_SMOKETEST, String.class);

		JSONObject json = new JSONObject(testHTTPResponsePostHTTPAssault.getBody());

		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, testHTTPResponsePostHTTPAssault.getStatusCode());
		assertEquals(HTTP_CHAOS_ERR_MSG, json.get("message"));
		System.out.println(testHTTPResponsePostHTTPAssault.getBody().toString());

		ActiveChaosDataStore.removeChaos(HTTP_CHAOS_EXPERIMENT_ID, ChaosEndpoint.SMOKE_TEST_ENDPOINT_NAME,
				ChaosAssaultType.HTTP_ASSAULT.name());

	}

	@Test
	public void testStaticChaosData() throws Exception {

		ResponseEntity<String> testHTTPResponsePostHTTPAssault = this.restTemplate
				.getForEntity("http://localhost:" + port + CONTEXT_PATH_GET_STATIC_CHAOS_DATA, String.class);

		JSONObject json = new JSONObject(testHTTPResponsePostHTTPAssault.getBody());

		JSONArray assaultsOnGetChaosEndpoitn = (JSONArray) json.get("getChaosPoints");
		
		JSONArray assaultsOnSmokeTest = (JSONArray) json.get(ChaosEndpoint.SMOKE_TEST_ENDPOINT_NAME);

		assertArrayEquals(new String[] { "HTTP_ASSAULT" }, converJSONArrayToStringArray(assaultsOnGetChaosEndpoitn));
		assertArrayEquals(
				new String[] { "HTTP_ASSAULT", "EXCEPTION_ASSAULT", "LATENCY_ASSAULT", "MUTANT_TEST_ASSAULT" },
				converJSONArrayToStringArray(assaultsOnSmokeTest));

	}
	
	

	private String[] converJSONArrayToStringArray(JSONArray arr) throws JSONException {

		String[] returnValue = new String[arr.length()];
		
		for (int i = 0; i < arr.length(); i++) {
			returnValue[i] = arr.getString(i);
		}

		return returnValue;
	}

}