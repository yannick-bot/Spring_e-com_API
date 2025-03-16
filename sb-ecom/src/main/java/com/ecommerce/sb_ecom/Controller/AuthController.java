package com.ecommerce.sb_ecom.Controller;

import com.ecommerce.sb_ecom.Model.AppRole;
import com.ecommerce.sb_ecom.Model.Role;
import com.ecommerce.sb_ecom.Model.User;
import com.ecommerce.sb_ecom.Repositories.RoleRepository;
import com.ecommerce.sb_ecom.Repositories.UserRepository;
import com.ecommerce.sb_ecom.Security.AuthModels.MessageResponse;
import com.ecommerce.sb_ecom.Security.AuthModels.SignupRequest;
import com.ecommerce.sb_ecom.Security.Jwt.JwtUtils;
import com.ecommerce.sb_ecom.Security.AuthModels.LoginRequest;
import com.ecommerce.sb_ecom.Security.AuthModels.LoginResponse;
import jakarta.validation.Valid;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder encoder;

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

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    case "seller":
                        Role modRole = roleRepository.findByRoleName(AppRole.ROLE_SELLER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
}
