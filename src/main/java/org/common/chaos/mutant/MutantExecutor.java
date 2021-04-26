package org.common.chaos.mutant;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.common.chaos.annotation.EnableChaos;
import org.common.chaos.controller.ChaosEndpoint;
import org.common.chaos.datastore.StaticChaosDataStore;
import org.common.chaos.enums.ChaosAssaultType;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

public class MutantExecutor {

	public Object executeMutantForMethod(JoinPoint jp) {
		Object returnValue = null;
		// Annotation annotatois = methodSignature.getMethod().getAnnotations();
		Object[] parameters = jp.getArgs();
		Method method = ((MethodSignature) jp.getSignature()).getMethod();

		List<Class> parametersList = new ArrayList<Class>();

		for (Object obj : parameters) {
			System.out.println(obj.getClass());
			parametersList.add(obj.getClass());
		}

		try {

			;
			List<MutantTO> mutantsOnMethod = StaticChaosDataStore.mutantsByEndpoint
					.get(StaticChaosDataStore.methodSignature2endpointName.get(method.toString()));
			
			Map<String,Object> methodComponents = parstMethodSignature(chooseRandomMutantForExecution(mutantsOnMethod));
			
			Class mutantClass = (Class) methodComponents.get("CLASS_TYPE");
			String methodName = (String) methodComponents.get("METHOD_NAME");
			
			
			
			Method mutant = mutantClass.getMethod(methodName,
					parametersList.toArray(new Class[parametersList.size()]));
			returnValue = mutant.invoke(jp.getTarget(), parameters);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
				| SecurityException  | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return returnValue;
	}

	public List<String> parseMethodSignature(String methodSignature) {
		return null;
	}

	public String chooseRandomMutantForExecution(List<MutantTO> mutantsOnMethod) {

		return mutantsOnMethod.get(new Random().nextInt(mutantsOnMethod.size())).getMethodSignature();
	}

	public static Map<String,Object> parstMethodSignature(String methodSignature) throws ClassNotFoundException {
		//String methodSignature = "public java.lang.String org.common.chaos.controller.ChaosEndpoint.smokeTestForChaosMutant1(java.lang.Object)";

		Map<String,Object> methodComponents = new HashMap();
		
		List<String> methodSignatureComponents = Arrays.asList(methodSignature.split(" "));

		String accessModifier = Arrays.asList("public", "private", "protected", "default")
				.contains(methodSignatureComponents.get(0)) ? methodSignatureComponents.get(0) : "default";

		String returnType = methodSignatureComponents.get(1);

		String parameters = methodSignatureComponents.get(2).substring(
				methodSignatureComponents.get(2).indexOf('(') + 1, methodSignatureComponents.get(2).indexOf(')'));

		String fullyQualifiedMethodName = methodSignatureComponents.get(2).substring(0,
				methodSignatureComponents.get(2).indexOf('('));

		String[] fullyQualifiedMethodNameSplit = fullyQualifiedMethodName.split("\\.");

		String methodName = fullyQualifiedMethodNameSplit[(fullyQualifiedMethodNameSplit.length - 1)];

		String fullyQualifiedClassNAme = fullyQualifiedMethodName.replaceAll("\\." + methodName, "") ;

		System.out.println("access modifier :" + accessModifier);
		System.out.println("return type :" + returnType);
		System.out.println("parameters name :" + parameters);
		System.out.println("Fully qualified  method name :" + fullyQualifiedMethodName);
		System.out.println("plain method name :" + methodName);
		System.out.println("fully Qualified Class name :" + fullyQualifiedClassNAme);

		
		methodComponents.put("METHOD_NAME", methodName);
		methodComponents.put("CLASS_TYPE", Class.forName(fullyQualifiedClassNAme));
		
		return methodComponents;
	}

}
