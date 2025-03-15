package com.ecommerce.sb_ecom.Security.Jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Component // Indique que cette classe est un composant Spring et peut être gérée par le conteneur Spring
public class JwtUtils {
    // Logger pour enregistrer des messages de log
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    // Injection de la valeur de la clé secrète JWT à partir des propriétés de l'application
    @Value("${spring.app.jwtSecret}")
    private String jwtSecret;

    // Injection de la valeur de la durée de validité du JWT (en millisecondes) à partir des propriétés de l'application
    @Value("${spring.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    // Méthode pour extraire le JWT de l'en-tête "Authorization" de la requête HTTP
    public String getJwtFromHeader(HttpServletRequest request) {
        // Récupère la valeur de l'en-tête "Authorization"
        String bearerToken = request.getHeader("Authorization");
        // Log le contenu de l'en-tête pour le débogage
        logger.debug("Authorization Header: {}", bearerToken);
        // Vérifie si l'en-tête contient un token et commence par "Bearer "
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            // Retire le préfixe "Bearer " pour obtenir le token JWT
            return bearerToken.substring(7);
        }
        // Retourne null si le token n'est pas trouvé ou mal formaté
        return null;
    }

    // Méthode pour générer un JWT à partir du nom d'utilisateur (username)
    public String generateTokenFromUsername(UserDetails userDetails) {
        // Récupère le nom d'utilisateur à partir des détails de l'utilisateur
        String username = userDetails.getUsername();
        // Construit le JWT avec les informations suivantes :
        return Jwts.builder()
                .subject(username) // Le sujet du token (ici, le nom d'utilisateur)
                .issuedAt(new Date()) // La date de création du token
                .expiration(new Date((new Date()).getTime() + jwtExpirationMs)) // La date d'expiration du token
                .signWith(key()) // Signe le token avec la clé secrète
                .compact(); // Génère le token sous forme de chaîne compacte
    }

    // Méthode pour extraire le nom d'utilisateur (username) à partir d'un JWT
    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) key()) // Vérifie le token avec la clé secrète
                .build().parseSignedClaims(token) // Parse le token et récupère les claims (informations)
                .getPayload().getSubject(); // Récupère le sujet (username) du token
    }

    // Méthode pour générer la clé secrète utilisée pour signer et vérifier les JWT
    private Key key() {
        // Décode la clé secrète (en Base64) et la convertit en une clé HMAC-SHA
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    // Méthode pour valider un JWT
    public boolean validateJwtToken(String authToken) {
        try {
            System.out.println("Validate");
            // Tente de parser et de vérifier le token avec la clé secrète
            Jwts.parser().verifyWith((SecretKey) key()).build().parseSignedClaims(authToken);
            // Si le token est valide, retourne true
            return true;
        } catch (MalformedJwtException e) {
            // Log une erreur si le token est malformé
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            // Log une erreur si le token est expiré
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            // Log une erreur si le token n'est pas supporté
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            // Log une erreur si le token est vide ou null
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        // Si une exception est levée, retourne false
        return false;
    }
}