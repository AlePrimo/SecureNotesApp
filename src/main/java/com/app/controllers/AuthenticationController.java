package com.app.controllers;


import com.app.controllers.dtos.authdto.AuthCreateUserRequest;
import com.app.controllers.dtos.authdto.AuthLoginRequest;
import com.app.controllers.dtos.authdto.AuthResponse;
import com.app.services.AuthService;
import com.app.services.UserDetailServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    @Autowired
    private UserDetailServiceImpl userDetailService;
    @Autowired
    private AuthService authService;


    @PostMapping("/sign-up")

    public ResponseEntity<AuthResponse> register(@RequestBody @Valid AuthCreateUserRequest authCreateUser) throws AccessDeniedException {
        return new ResponseEntity<>(this.userDetailService.createUser(authCreateUser), HttpStatus.CREATED);

    }

    @PostMapping("/log-in")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthLoginRequest request) {
        return ResponseEntity.ok(this.authService.loginUser(request));
    }

    @PostMapping
    @PreAuthorize("hasRole('DEVELOPER')")
    public ResponseEntity<AuthResponse> createAdmin(@RequestBody @Valid AuthCreateUserRequest authCreateUserRequest) throws AccessDeniedException {

        List<String> roles = authCreateUserRequest.roleRequest().roleListName();

        if (!roles.contains("ADMIN") || roles.size() > 1) {
            return ResponseEntity.badRequest().body(
                    new AuthResponse(null, "Solo se puede asignar el rol ADMIN en este endpoint", null, false)
            );
        }

        return new ResponseEntity<>(this.userDetailService.createUser(authCreateUserRequest), HttpStatus.CREATED);

    }


    @PostMapping("/create-developer")
    @PreAuthorize("hasRole('DEVELOPER')")
    public ResponseEntity<AuthResponse> createDeveloper(@RequestBody @Valid AuthCreateUserRequest request) throws AccessDeniedException {

        List<String> roles = request.roleRequest().roleListName();

        if (!roles.contains("DEVELOPER") || roles.size() > 1) {
            return ResponseEntity.badRequest().body(
                    new AuthResponse(null, "Solo se puede asignar el rol DEVELOPER en este endpoint", null, false)
            );
        }

        return new ResponseEntity<>(userDetailService.createUser(request), HttpStatus.CREATED);
    }





}
