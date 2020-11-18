package org.georchestra.pluievolution.core.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class CoreAspect {

	private static final Logger LOGGER = LoggerFactory.getLogger(CoreAspect.class);

	@Pointcut("execution(* org.georchestra.pluievolution.core.dao.**.impl.*.*(..))")
	public void businessMethods() {
		// Do nothing - c'est une déclaration point cut
	}

	@Around("businessMethods()")
	public Object profile(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		long start = System.currentTimeMillis();
		Object output = proceedingJoinPoint.proceed();
		long elapsedTime = System.currentTimeMillis() - start;
		LOGGER.info(elapsedTime + " - " + proceedingJoinPoint.getSignature().getDeclaringTypeName() + " "
				+ proceedingJoinPoint.getSignature().getName());
		return output;
	}

}
