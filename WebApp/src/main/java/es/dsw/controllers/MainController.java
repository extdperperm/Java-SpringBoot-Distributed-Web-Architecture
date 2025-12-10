package es.dsw.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.function.client.WebClient;

import es.dsw.models.business.Alumno;
import es.dsw.models.business.Login;
import es.dsw.models.business.Modulo;
import es.dsw.models.responses.ApiResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class MainController {

	private String apiUrl = "http://localhost:8081/api/getData";
	
    @Autowired
    private WebClient webClient;
    
	@GetMapping(value= {"/", "/index"})
	public String index() {
        
		return "index";
	}
	
	@GetMapping("/testApi")
    public String obtenerModulos(HttpSession session, Model model) {

        Login user = (Login) session.getAttribute("DATA_USER");
        
        if (user == null) {
            return "redirect:/loggin?expired";
        }

        String token = user.getToken();

        //Se simulan datos a enviar
        Alumno objAlumno = new Alumno("11111111A");
        
        ApiResponse<List<Modulo>> response =
                webClient.post()
                        .uri(apiUrl)
                        .header("Authorization", "Bearer " + token)
                        .bodyValue(objAlumno)
                        .retrieve()
                        .bodyToMono(new ParameterizedTypeReference<ApiResponse<List<Modulo>>>() {})
                        .block(); // ← .block() para modo síncrono
        
        if (response == null || response.isError()) {
            model.addAttribute("error", "No se pudo obtener los módulos.");
            return "modulos";
        }

        List<Modulo> lista = response.getData();
        model.addAttribute("listaModulos", lista);

        return "TestApi";
    }
	
}
