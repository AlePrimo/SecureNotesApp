package com.app.controllers;


import com.app.controllers.dtos.authdto.AuthCreateUserRequest;
import com.app.controllers.dtos.authdto.AuthLoginRequest;
import com.app.controllers.dtos.authdto.AuthResponse;
import com.app.entities.UserEntity;
import com.app.services.AuthService;
import com.app.services.UserDetailServiceImpl;
import com.app.services.entitiyservices.implementations.UserEntityService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    @Autowired
    private UserDetailServiceImpl userDetailService;
    @Autowired
    private AuthService authService;
    @Autowired
    private UserEntityService userEntityService;


    @PostMapping("/sign-up")

    public ResponseEntity<AuthResponse> register(@RequestBody @Valid AuthCreateUserRequest authCreateUser) throws AccessDeniedException {
        return new ResponseEntity<>(this.userDetailService.createUser(authCreateUser), HttpStatus.CREATED);

    }

    @PostMapping("/log-in")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthLoginRequest request) {
        return ResponseEntity.ok(this.authService.loginUser(request));
    }

    @PostMapping("/create-admin")
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




    @DeleteMapping("/deleteUserById/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DEVELOPER')")
    public ResponseEntity<?> deleteUserById(@PathVariable Long id) {
        Optional<UserEntity> userOptional = userEntityService.findById(id);

        if (userOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        userEntityService.deleteById(id);
        return ResponseEntity.ok("Usuario y sus notas eliminados correctamente");
    }


    @GetMapping("/findAllUsers")
    @PreAuthorize("hasAnyRole('ADMIN', 'DEVELOPER')")
    public ResponseEntity<?> findAllUsers() {
        List<UserEntity> users = this.userEntityService.findAll();

        List<Map<String, Object>> result = users.stream().map(user -> {
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("id", user.getId());
            userInfo.put("username", user.getUsername());
            userInfo.put("roles", user.getRoles().stream()
                    .map(role -> role.getRoleEnum().name())
                    .collect(Collectors.toList()));
            return userInfo;
        }).toList();

        return ResponseEntity.ok(result);
    }


    @GetMapping("/findUserById/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DEVELOPER')")
    public ResponseEntity<?> findUserById(@PathVariable Long id) {
        Optional<UserEntity> optionalUser = this.userEntityService.findById(id);

        if (optionalUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        UserEntity user = optionalUser.get();
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("id", user.getId());
        userInfo.put("username", user.getUsername());
        userInfo.put("roles", user.getRoles().stream()
                .map(role -> role.getRoleEnum().name())
                .collect(Collectors.toList()));

        return ResponseEntity.ok(userInfo);
    }


}
