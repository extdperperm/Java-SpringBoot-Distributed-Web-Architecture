package es.dsw.jwtservices;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    //Se genera una key aleatoria. Se podría sustituir por una key estática.
	private final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
	
	//Genera el toke. Se configura el tiempo de vida del token en el servidor. Cuando se cumple el tiempo
	//se requiere volverse autenticar al usuario.
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(SECRET_KEY)
                .compact();
    }

    //Método que permite devovler el nombre de usuario asociado al token.
    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    //Método que indica si el token es válido (ej: si sigue vigente en memoria de aplicación).
    public boolean isTokenValid(String token) {
        try {
            getUsernameFromToken(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}