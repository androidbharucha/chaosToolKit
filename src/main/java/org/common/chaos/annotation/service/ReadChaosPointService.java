package org.common.chaos.annotation.service;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.common.chaos.annotation.EnableChaos;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.springframework.stereotype.Service;

@Service
public class ReadChaosPointService implements IReadChaosPointService {

	public static Map<String, Method> CHAOS_POINT_2_METHODS_MAP = new HashMap();

	public static String PACKAGE_NAME_TO_BE_SCANNED = "org.common";

	static {
		Reflections reflections = new Reflections(
				new ConfigurationBuilder().setUrls(ClasspathHelper.forPackage(PACKAGE_NAME_TO_BE_SCANNED))
						.setScanners(new MethodAnnotationsScanner()));
		reflections.getMethodsAnnotatedWith(EnableChaos.class).forEach(method -> {
			if (CHAOS_POINT_2_METHODS_MAP.containsKey(method.getAnnotation(EnableChaos.class).chaosEndpointname())) {
				System.out.println("More than one method is configured with same point :"
						+ method.getAnnotation(EnableChaos.class).chaosEndpointname());
			}
			CHAOS_POINT_2_METHODS_MAP.put(method.getAnnotation(EnableChaos.class).chaosEndpointname(), method);
		});

	}

	public Set<String> getConfiguredChaosEndPoints() {
		System.out.println(CHAOS_POINT_2_METHODS_MAP.values());
		return CHAOS_POINT_2_METHODS_MAP.keySet();

	}

	

}
