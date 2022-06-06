package com.microservicios.gateway.security;

import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import io.jsonwebtoken.Jwts;

public class AuthenticationFilter extends BasicAuthenticationFilter {

   private Logger log = LoggerFactory.getLogger(AuthenticationFilter.class);

   private Environment env;

   public AuthenticationFilter(AuthenticationManager authenticationManager, Environment env) {
      super(authenticationManager);
      this.env = env;
   }

   @Override
   protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
         throws IOException, ServletException {

      String authorizationHeader = req.getHeader(env.getProperty("authorization.token.header.name"));

      if (authorizationHeader == null 
            || !authorizationHeader.startsWith(env.getProperty("authorization.token.header.prefix"))) {
         chain.doFilter(req, res);
         return;
      }

      String token = authorizationHeader.replace(env.getProperty("authorization.token.header.prefix"), "");
      
      log.info("toke: {}", token);
      
      String login = Jwts.parser()
                        .setSigningKey(env.getProperty("token.secret"))
                        .parseClaimsJws(token)
                        .getBody()
                        .getSubject();

      UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(login, null, new ArrayList<>());
      SecurityContextHolder.getContext().setAuthentication(authentication);

      chain.doFilter(req, res);
   }
   
}