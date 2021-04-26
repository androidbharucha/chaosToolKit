package org.common.chaos.aspects;
import java.util.List;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.common.chaos.datastore.ActiveChaosDataStore;
import org.common.chaos.enums.ChaosAssaultType;
import org.common.chaos.enums.ChaosParameterKeysConstant;
import org.common.chaos.model.ChaosAssault;
import org.common.chaos.mutant.MutantExecutor;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
@Aspect
@Component
public class EnableChaosAspectInterceptor implements ApplicationContextAware {
	
	    @Around("@annotation(org.common.chaos.annotation.EnableChaos)")//applying pointcut on before advice  
	    public Object defaultChaosAdvice(ProceedingJoinPoint pjp) throws Throwable//it is advice (before advice)  
	    {  
	    	
	    	
	    	
	    	MethodSignature methodSignature = (MethodSignature)pjp.getSignature();
	        System.out.println("additional concern : " + methodSignature.getMethod().toString());  
	        if(ActiveChaosDataStore.isThereActiveChaosOnMethod(methodSignature.getMethod().toString())) {
	        	
	        	
	        	// Should I apply chaos randomly  IRandomChekChaos and sample 
	        	
	        	List<ChaosAssault> assaultsOnMethod = ActiveChaosDataStore.getActiveChaosAssaultsOnMethod(methodSignature.getMethod().toString());
	        	System.out.println(">>>>>>>>>>>>>>>>>> following are active"+assaultsOnMethod);
	        	if(assaultsOnMethod.size() == 1) {
	        		ChaosAssault assault =   assaultsOnMethod.get(0);
	        		if(ChaosAssaultType.EXCEPTION_ASSAULT.equals(assault.getAssaultType()) || 
	        				ChaosAssaultType.HTTP_ASSAULT.equals(assault.getAssaultType())) {
	        				
	        				throw (Throwable) assault.getAssaultParameters().get(ChaosParameterKeysConstant.CHAOS_THROWBLE_EXCEPTION);
	        				
	        		}else if(ChaosAssaultType.MUTANT_TEST_ASSAULT.equals(assault.getAssaultType())){
	        				return new MutantExecutor().executeMutantForMethod(pjp);
	        		}else {
	        			//TODO Latency 
	        		}
	        	}else {
	        		// TODO : When multiple assaults are configured 
	        	}
	        	
	        	
	        	
	        }
	        
	        // Memoery and system shutdown use from libary
	        
	        // Behavirol Chaos : MUTANT and LATENCY 
	        
	        // EXPTION CHAOS : Exption and http 
	        
	        // system failure : memory and breakdown 
	        return pjp.proceed();
	        
	    }

		@Override
		public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
			System.out.println("aspect for chaos initiaized ");
		}  

}
