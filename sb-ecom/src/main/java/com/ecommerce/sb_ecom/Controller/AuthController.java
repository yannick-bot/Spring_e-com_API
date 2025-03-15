package com.ecommerce.sb_ecom.Controller;

import com.ecommerce.sb_ecom.Security.Jwt.JwtUtils;
import com.ecommerce.sb_ecom.Security.AuthModels.LoginRequest;
import com.ecommerce.sb_ecom.Security.AuthModels.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {

        // Déclaration d'un objet Authentication pour stocker les informations d'authentification.
        Authentication authentication;

        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );
        } catch (AuthenticationException exception) {
            Map<String, Object> map = new HashMap<>();
            map.put("message", "Bad credentials"); // Message d'erreur
            map.put("status", false); // Statut d'échec
            return new ResponseEntity<Object>(map, HttpStatus.NOT_FOUND);
        }

        // Si l'authentification réussit, on définit l'objet Authentication dans le contexte de sécurité.
        // Cela permet à Spring Security de savoir que l'utilisateur est authentifié.
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // On récupère les détails de l'utilisateur authentifié à partir de l'objet Authentication.
        // UserDetails est une interface Spring Security qui contient les informations de l'utilisateur.
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // On génère un token JWT (JSON Web Token) à partir du nom d'utilisateur de l'utilisateur.
        // Ce token sera utilisé pour authentifier les requêtes futures de l'utilisateur.
        String jwtToken = jwtUtils.generateTokenFromUsername(userDetails);

        // On récupère les rôles (ou autorités) de l'utilisateur et on les transforme en une liste de chaînes.
        // Les rôles sont généralement utilisés pour gérer les permissions dans l'application.
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority) // On extrait le nom de chaque rôle
                .collect(Collectors.toList()); // On les collecte dans une liste

        // On crée un objet LoginResponse qui contient :
        // - Le nom d'utilisateur
        // - Les rôles de l'utilisateur
        // - Le token JWT généré
        LoginResponse response = new LoginResponse(userDetails.getUsername(), roles, jwtToken);

        // On renvoie une réponse HTTP 200 (OK) avec l'objet LoginResponse dans le corps de la réponse.
        return ResponseEntity.ok(response);
    }
}
