package com.microservicio.usuario.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.commons.utils.models.entities.Usuario;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservicio.usuario.services.UsuarioService;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

   private Environment env;
   private UsuarioService usuarioService;

   public AuthenticationFilter(Environment env, UsuarioService usuarioService) {
      this.env = env;
      this.usuarioService = usuarioService;
   }

   @Override
   public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
         throws AuthenticationException {
      try {
         Usuario creds = new ObjectMapper().readValue(req.getInputStream(), Usuario.class);
         return super.getAuthenticationManager().authenticate(
                  new UsernamePasswordAuthenticationToken(
                        creds.getLogin(), 
                        creds.getPassword(), 
                        new ArrayList<>()));
      } catch (IOException e) {
         e.printStackTrace();
      }
      return null;
   }

   @Override
   protected void successfulAuthentication(
                     HttpServletRequest req, 
                     HttpServletResponse res, 
                     FilterChain chain,
                     Authentication authResult) throws IOException, ServletException {

      String login = ((User) authResult.getPrincipal()).getUsername();
      String token = Jwts.builder()
                           .setSubject(login)
                           .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(env.getProperty("token.expire"))))
                           .signWith(SignatureAlgorithm.HS512, env.getProperty("token.secret"))
                           .compact();

      Usuario usuarioAuth = this.usuarioService.findByLogin(login).get();

      res.addHeader("token", token);
      res.addHeader("userAuth", usuarioAuth.getLogin());
      res.addHeader("usernameAuth", usuarioAuth.getNombres());

   }
}