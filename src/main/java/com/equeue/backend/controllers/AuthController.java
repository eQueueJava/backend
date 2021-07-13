package com.equeue.backend.controllers;

import com.equeue.backend.configs.jwt.JwtUtils;
import com.equeue.backend.models.Role;
import com.equeue.backend.models.RoleEntity;
import com.equeue.backend.models.UserEntity;
import com.equeue.backend.pojo.JwtResponse;
import com.equeue.backend.pojo.LoginRequest;
import com.equeue.backend.pojo.MessageResponse;
import com.equeue.backend.pojo.SignupRequest;
import com.equeue.backend.repository.RoleRepository;
import com.equeue.backend.repository.UserRepository;
import com.equeue.backend.service.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> authUser(@RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }

    @PostMapping("/registration")
    public ResponseEntity<?> registerUser(@RequestBody @Valid SignupRequest signupRequest, BindingResult result) {
        if (!result.hasErrors()) {
            if (userRepository.existsByUsername(signupRequest.getUsername())) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: Username is exist"));
            }

            UserEntity user = new UserEntity(signupRequest.getUsername(),
                    signupRequest.getEmail(),
                    passwordEncoder.encode(signupRequest.getPassword()));

            Set<String> reqRoles = signupRequest.getRoles();
            Set<RoleEntity> roles = new HashSet<>();

            if (reqRoles == null) {
                RoleEntity userRole = roleRepository
                        .findByName(Role.CLIENT)
                        .orElseThrow(() -> new RuntimeException("Error, Role CLIENT is not found"));
                roles.add(userRole);
            } else {
                reqRoles.forEach(r -> {
                    switch (r) {
                        case "admin":
                            RoleEntity adminRole = roleRepository
                                    .findByName(Role.ADMIN)
                                    .orElseThrow(() -> new RuntimeException("Error, Role ADMIN is not found"));
                            roles.add(adminRole);

                            break;
                        case "costumer":
                            RoleEntity modRole = roleRepository
                                    .findByName(Role.COSTUMER)
                                    .orElseThrow(() -> new RuntimeException("Error, Role costumer is not found"));
                            roles.add(modRole);

                            break;

                        default:
                            RoleEntity userRole = roleRepository
                                    .findByName(Role.CLIENT)
                                    .orElseThrow(() -> new RuntimeException("Error, Role CLIENT is not found"));
                            roles.add(userRole);
                    }
                });
            }
            user.setRoles(roles);
            userRepository.save(user);
            return ResponseEntity.ok(new MessageResponse("User CREATED"));
        } else {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is incorrect"));
        }
    }
}
