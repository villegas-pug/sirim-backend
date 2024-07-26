package com.commons.utils.aop;

import com.commons.utils.constants.MessageType;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Before;
public class CommonAop {

   /*
    * @Pointcut("execution( * com.microservicios.*.controllers.*.*(..))") public
    * void servicePointCut() { }
    */

   @Before("execution( * com.microservicios.*.controllers.*.*(..))")
   protected void before(JoinPoint point) {
      String[] rootPackage = point.getTarget().getClass().getName().split("\\.");
      String microservicio = rootPackage[2].toString();/* » com.microservicios.test.controllers.TestController */
      System.setProperty("microservicio", microservicio);
      System.setProperty("method", point.getSignature().getName());
      System.setProperty("levelLog", MessageType.ERROR);/* » Default level-log... */
   }
}