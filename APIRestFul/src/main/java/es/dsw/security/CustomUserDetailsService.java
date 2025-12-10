package es.dsw.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

/************************************************************************************
 *                  CONFIGURACIÓN DEL SERVICIO PROVEEDOR DE USUARIOS                *
 * **********************************************************************************
 * Implementa la lógica que define los usuarios. En loadUserByUsername es donde     *
 * se debe acceder a la base de datos para obtener los datos de usuario y devolver  *
 * si existe un objeto UserDetails. También podría cachearse los usuarios en un     *
 * evento de inicio de aplicación y en este punto acceder a la cache de usuarios.   *
 *                                                                                  *
 * loadUserByUsername es un método que invoca automaticamente por spring security al*
 * autenticar y también JwtAuthFilter al verificar cada petición por el token JWT.  *                                                      *  
 * **********************************************************************************/
@Service
public class CustomUserDetailsService implements UserDetailsService {

	
    @Override
    public UserDetails loadUserByUsername(String username) {
    	
    	//Aquí se debe preguntar contra la BBDD o cache si existe el usuario (por su username, asumiendo que es clave única)
    	if (username.equals("daniel")) {
    		//Extraido los datos del usuario de la base de datos se crea el objeto UserDetails y se devuelve.
	        return User.withUsername("daniel")
	        		//{noop} indica que la password no está cifrada. Si se indica por ejemplo {bcrypt}, automáticamente
	        		//spring security lo tiene en cuenta para aplicar cifrado en la comparación por bcrypt. Si se aplica
	        		//este modelo, cuando se extrae la clave de la base de datos hay que añadirle dicha cadena.
	                .password("{noop}1234")
	                .roles("USER")
	                .build();
    	} else {
    		//Si no existe será imposible la autenticación y devuelve null.
	        return null;
    	}
        
    
        
    }
}