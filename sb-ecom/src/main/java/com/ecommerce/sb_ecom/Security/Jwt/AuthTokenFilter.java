package com.ecommerce.sb_ecom.Security.Jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component // Indique que cette classe est un composant Spring et peut être gérée par le conteneur Spring
public class AuthTokenFilter extends OncePerRequestFilter { // Étend OncePerRequestFilter pour s'assurer que le filtre est exécuté une fois par requête

    @Autowired // Injection de dépendance pour JwtUtils
    private JwtUtils jwtUtils;

    @Autowired // Injection de dépendance pour UserDetailsService
    private UserDetailsService userDetailsService;

    // Logger pour enregistrer des messages de log
    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // Log l'URI de la requête pour le débogage
        logger.debug("AuthTokenFilter called for URI: {}", request.getRequestURI());
        try {
            // Tente d'extraire et de valider le JWT de la requête
            String jwt = parseJwt(request);
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                // Si le JWT est valide, extrait le nom d'utilisateur (username) du JWT
                String username = jwtUtils.getUserNameFromJwtToken(jwt);

                // Charge les détails de l'utilisateur à partir du nom d'utilisateur
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // Crée un objet d'authentification Spring Security avec les détails de l'utilisateur
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails,
                                null, // Les credentials ne sont pas nécessaires ici
                                userDetails.getAuthorities()); // Les rôles de l'utilisateur
                // Log les rôles de l'utilisateur pour le débogage
                logger.debug("Roles from JWT: {}", userDetails.getAuthorities());

                // Ajoute les détails de la requête (comme l'adresse IP) à l'objet d'authentification
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Définit l'authentification dans le contexte de sécurité Spring
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            // Log une erreur si l'authentification ne peut pas être définie
            logger.error("Cannot set user authentication: {}", e);
        }

        // Passe la requête et la réponse au filtre suivant dans la chaîne
        filterChain.doFilter(request, response);
    }

    // Méthode pour extraire le JWT de la requête
    private String parseJwt(HttpServletRequest request) {
        // Utilise JwtUtils pour extraire le JWT de l'en-tête "Authorization"
        String jwt = jwtUtils.getJwtFromHeader(request);
        // Log le JWT extrait pour le débogage
        logger.debug("AuthTokenFilter.java: {}", jwt);
        return jwt;
    }
}