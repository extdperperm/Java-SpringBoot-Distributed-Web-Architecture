package es.dsw.controllers;

import java.util.ArrayList;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.dsw.models.business.Alumno;
import es.dsw.models.business.Modulo;
import es.dsw.models.responses.ApiResponse;

@RestController
@RequestMapping("/api")
public class ServicesController {

    @GetMapping("/test")
    public Map<String, String> test() {
        return Map.of("mensaje", "Estás autenticado con JWT");
    }
    
    @PostMapping("/getData")
    public ResponseEntity<?> getData(@ModelAttribute Alumno objAlumno) {
    
    	ArrayList<Modulo> listaModulos = new ArrayList<Modulo>();
    	listaModulos.add(new Modulo("DSW","Desarrollo de Aplicaciones en Entorno Servidor"));
    	listaModulos.add(new Modulo("DPL","Despliegue de Aplicaciones Web"));
    	
    	return ResponseEntity.ok(ApiResponse.ok(listaModulos));
    
    }
}