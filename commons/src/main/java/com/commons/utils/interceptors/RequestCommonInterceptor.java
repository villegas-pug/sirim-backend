package com.commons.utils.interceptors;

import com.commons.utils.constants.LevelLog;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class RequestCommonInterceptor implements HandlerInterceptor {

   @Override
   public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception {
      /* String uri = req.getRequestURL().toString(); */
      /* » Obtiene nombre del microservicio... */
      /* String microservicio = uri[2].split("-")[1].toString(); */

      /* System.setProperty("entity", microservicio.toUpperCase()); */
      /* System.setProperty("microservicio", microservicio); */

      /*-> Default... */
      System.setProperty("levelLog", LevelLog.ERROR);
      return true;
   }

}