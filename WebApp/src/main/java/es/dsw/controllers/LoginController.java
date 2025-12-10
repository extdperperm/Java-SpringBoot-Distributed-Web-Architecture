package es.dsw.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.reactive.function.client.WebClient;

import es.dsw.models.business.Login;
import es.dsw.models.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.stream.Collectors;

@Controller
public class LoginController {

	@Autowired
	private WebClient webClient;
	
	//Url de la ApiRest (back-end). Sería ideal parametrizar la url (http://localhost:8081) en un fichero de propiedades. 
    private String authApiUrl = "http://localhost:8081/auth/login";

    //Formulario de autenticación
    @GetMapping("/loggin")
    public String loginForm(HttpSession session) {
    	
        Login user = (Login) session.getAttribute("DATA_USER");
        
        if (user != null) {
            return "redirect:/index";
        }
        
        return "loggin";
    }

    //Lo que antes hacía spring security en una arquitectura monolitica, ahora se implementa para
    //obtener la autenticación desde la Api. Una vez autenticado se carga los datos del usuario (incluido roles)
    //en el módulo de seguridad. De esta manera se explotará toda la potencia de spring security, secure thymeleafe, sesiones, etc.
    @PostMapping("/logginprocess")
    public String loginProcess(String username, String password, HttpServletRequest request) {


        try {
        	//Se prepara la petición al método de autenticación de la Api.
            MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
            form.add("username", username);
            form.add("password", password);

            //Se realiza la petición a la Api
            ApiResponse<Login> apiResponse =
                    webClient.post()
                            .uri(authApiUrl)
                            .bodyValue(form)
                            .retrieve()
                            .bodyToMono(new ParameterizedTypeReference<ApiResponse<Login>>() {})
                            .block();
             
            //Comprobaciones de control.
            if (apiResponse == null) {
            	return "redirect:/loggin?error";
            }
            
            if (apiResponse.isError() || apiResponse.getData() == null) {
                return "redirect:/loggin?loginFailure";
            }

            //Se obtiene los datos del usuario.
            Login response = apiResponse.getData(); // Obtenemos el LoginResponse real

            //Se extraen los roles
            var authorities = response.getRoles().stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            //Se obtiene un objeto UsernamePasswordAuthenticationToken de spring security con los datos del usuario.
            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(response.getUsername(), null, authorities);

            //Se carga la información del usuario con sus roles en el contexto de spring security.
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(auth);
            SecurityContextHolder.setContext(context);

            //ZONA DE INICIALIZACIÓN DE LA SESIÓN DE USUARIO.
            HttpSession session = request.getSession(true);
            
            //Importante: Para que persista el contexto de seguridad se debe alojar en una variable de sesión. De lo contrario,
            //cuando finalice la petición, el módulo de seguridad seguirá considerando no autenticado al usuario.
            session.setAttribute("SPRING_SECURITY_CONTEXT", context);
            
            //Se guarda en sesión los datos del usuario (opcional). Es una buena zona para que inicialices en sesión los datos que estimes oportuno.
            session.setAttribute("DATA_USER", response);
            
            //Redirecciona a lo que se considera el home de la aplicación.
            return "redirect:/index";

        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/loggin?error";
        }
    }
    
    
}