package org.common.chaos.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.common.chaos.enums.ChaosAssaultType;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public  @interface EnableChaos {
	
	public String chaosEndpointname() ;
	
	public ChaosAssaultType[] supportedChaosTypes() ;

}
