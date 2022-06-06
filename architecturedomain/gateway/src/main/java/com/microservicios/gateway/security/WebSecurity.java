package com.microservicios.gateway.security;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

   @Autowired
   private Environment env;

   @Override
   protected void configure(HttpSecurity http) throws Exception {
      http.csrf().disable();
      http.headers().frameOptions().disable();
      http.authorizeRequests()
         .antMatchers(HttpMethod.POST, "/api/microservicio-usuario/login").permitAll()
         .and()
         .cors().configurationSource(corsConfigurationSource())
         .and()
         .authorizeRequests().anyRequest().authenticated()
         .and()
         .addFilter(new AuthenticationFilter(super.authenticationManager(), env));
      
      http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

   }

   @Bean
   public CorsConfigurationSource corsConfigurationSource() {
      CorsConfiguration corsConfig = new CorsConfiguration();
      corsConfig.setAllowedOrigins(Arrays.asList("*"));
      corsConfig.setAllowedMethods(Arrays.asList("*"));
      corsConfig.setAllowedHeaders(Arrays.asList("*"));
      corsConfig.setExposedHeaders(Arrays.asList("*"));
      corsConfig.setAllowCredentials(true);

      UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
      source.registerCorsConfiguration("/**", corsConfig);

      return source;
   }
   
}