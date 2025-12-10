package es.dsw.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.dsw.jwtservices.JwtService;
import es.dsw.models.business.Login;
import es.dsw.models.responses.ApiResponse;

/****************************************************************
 * CONTROLADORA QUE RESUELVE LA COMPLEJIDAD DE LA AUTENTICACIÓN *
 ****************************************************************/
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String username, @RequestParam String password) {

    	try {
	        //1º Autentica las credenciales.
    		Authentication auth = authenticationManager.authenticate(
	            new UsernamePasswordAuthenticationToken(username, password)
	        );
	
	        //2º Obtiene un objeto userDetailsService desde el CustomUserDetailsService configurado.
	        UserDetails userDetails = (UserDetails) auth.getPrincipal();
	        
	        //3º Genera el token JWT desde el JwtService configurado (por un tiempo máximo).
	        String token = jwtService.generateToken(userDetails.getUsername());
	
	        //4º Extraer roles (se necesitan en el proyecto del front-end para explotar spring security)
	        List<String> roles = userDetails.getAuthorities()
	                                        .stream()
	                                        .map(a -> a.getAuthority())
	                                        .toList();
	
	        //5º Se devuelve una respuesta con los datos del usuario. En este punto podría
	        //   requerirse consultar otros datos del usuario (email, cargo, etc) y ampliar
	        //   la clase Login.
	        Login response = new Login(token, userDetails.getUsername(), roles);
	
	        //6º Se responde con código 200 http y el objeto ApiResponse que embebe el objeto response
	        //   con los datos del usuario.
	        return ResponseEntity.ok(ApiResponse.ok(response));
	        
      } catch(Exception ex) {
    	  //Si ha ocurrido un error (autenticación, credenciales u otro) se responde también con un código 200 http
    	  //pero la bandera de error del objeto ApiResponse a true. Podría interesar ampliar la clase ApiResponse
    	  //e incluir el código del error (si se necesita en el front-end aplicar lógica sobre el motivo del error).
          return ResponseEntity
                  //.badRequest() //Otra alternativa es responder con un código 400 - solicitud incorrecta o no puede ser procesada.
        		  .ok()
                  .body(ApiResponse.error("Credenciales inválidas. +info: "+ex.getMessage()));
      }
    }

}