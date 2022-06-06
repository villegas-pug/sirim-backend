package com.microservicio.usuario.security;

import com.microservicio.usuario.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

   @Autowired
   private UsuarioService usuarioService;

   @Autowired
   private BCryptPasswordEncoder bCryptPasswordEncoder;

   @Autowired
   private Environment env;

   @Override
   protected void configure(HttpSecurity http) throws Exception {
      http.csrf().disable();
      http.authorizeRequests().antMatchers("/**").permitAll()
         .and()
         .addFilter(this.getAuthenticationFilter());
      http.headers().frameOptions().disable();
   }

   @Override
   protected void configure(AuthenticationManagerBuilder auth) throws Exception {
      auth.userDetailsService(usuarioService).passwordEncoder(bCryptPasswordEncoder);
   }
   
   private AuthenticationFilter getAuthenticationFilter() throws Exception{
      AuthenticationFilter authFilter = new AuthenticationFilter(env, usuarioService);
      authFilter.setAuthenticationManager(super.authenticationManager());
      return authFilter;
   }
}