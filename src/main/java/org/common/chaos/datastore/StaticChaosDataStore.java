package org.common.chaos.datastore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.common.chaos.annotation.EnableChaos;
import org.common.chaos.annotation.Mutant;
import org.common.chaos.enums.ChaosAssaultType;
import org.common.chaos.mutant.MutantTO;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

/**
 * This inmemory cache/Datastore will provided Chaos information that that
 * programmer has configured using annotations @EnabledEndPoint and @Mutant at
 * compilation time.
 * 
 * @author ANSAP_ADMIN
 *
 */
public class StaticChaosDataStore {

	public static String PACKAGE_NAME_TO_BE_SCANNED = "org.common";

	public static Map<String, List<ChaosAssaultType>> enabledChaosByEndpoint = new HashMap<>();

	public static Map<String, List<MutantTO>> mutantsByEndpoint = new HashMap<>();

	public static Map<String, String> endPointName2MethodSignature = new HashMap<>();
	
	public static Map<String, String> methodSignature2endpointName = new HashMap<>();

	static {

		Reflections reflections = new Reflections(
				new ConfigurationBuilder().setUrls(ClasspathHelper.forPackage(PACKAGE_NAME_TO_BE_SCANNED))
						.setScanners(new MethodAnnotationsScanner()));
		reflections.getMethodsAnnotatedWith(EnableChaos.class).forEach(method -> {
			
			EnableChaos enabledChaos = method.getAnnotation(EnableChaos.class);
			enabledChaosByEndpoint.put(enabledChaos.chaosEndpointname(),
					Arrays.asList(enabledChaos.supportedChaosTypes()));
			endPointName2MethodSignature.put(enabledChaos.chaosEndpointname(), method.toString());
			methodSignature2endpointName.put(method.toString(), enabledChaos.chaosEndpointname());
		});

		reflections.getMethodsAnnotatedWith(Mutant.class).forEach(method -> {
			Mutant mutant = method.getAnnotation(Mutant.class);
			List<MutantTO> mutantsOnEndpoint = mutantsByEndpoint.containsKey(mutant.chaosEndpointname())
					? mutantsByEndpoint.get(mutant.chaosEndpointname())
					: new ArrayList<>();
			mutantsOnEndpoint.add(new MutantTO(mutant.mutantName(), mutant.chaosEndpointname(), method.toString()));
			mutantsByEndpoint.put(mutant.chaosEndpointname(), mutantsOnEndpoint);
		});

	}

	
	public static boolean isMutantAssaultAllowedOnEndPoint(String endpointName) {
		return (enabledChaosByEndpoint.containsKey(endpointName) && enabledChaosByEndpoint.get(endpointName).contains(ChaosAssaultType.MUTANT_TEST_ASSAULT));
	}
	
	public static boolean isHTTPAssaultAllowedOnEndPoint(String endpointName) {
		return (enabledChaosByEndpoint.containsKey(endpointName) && enabledChaosByEndpoint.get(endpointName).contains(ChaosAssaultType.HTTP_ASSAULT));
	}
	
	
	
}
