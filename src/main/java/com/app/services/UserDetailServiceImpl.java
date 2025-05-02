package com.app.services;


import com.app.controllers.dtos.authdto.AuthCreateUserRequest;
import com.app.controllers.dtos.authdto.AuthLoginRequest;
import com.app.controllers.dtos.authdto.AuthResponse;
import com.app.entities.RoleEntity;
import com.app.entities.UserEntity;
import com.app.services.entitiyservices.implementations.RoleEntityService;
import com.app.services.entitiyservices.implementations.UserEntityService;
import com.app.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserEntityService userEntityService;

    @Autowired
    private RoleEntityService roleEntityService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserEntity userEntity = this.userEntityService.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("El usuario " +username+ " no existe en la base de datos"));


        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        userEntity.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority("ROLE_".concat(role.getRoleEnum().name()))));

        userEntity.getRoles().stream()
                .flatMap(role -> role.getPermissions().stream())
                .forEach(permission -> authorities.add(new SimpleGrantedAuthority(permission.getName())));

        return new User(userEntity.getUsername()
                , userEntity.getPassword()
                , userEntity.isEnabled()
                ,userEntity.isAccountNotExpired()
                ,userEntity.isCredentialsNotExpired()
                ,userEntity.isAccountNotLocked(), authorities);


    }

    public AuthResponse loginUser(AuthLoginRequest authLoginRequest){

        String username = authLoginRequest.username();
        String password = authLoginRequest.password();

        Authentication authentication = this.authenticate(username, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);


        String accessToken = jwtUtils.createToken(authentication);

        AuthResponse authResponse = new AuthResponse(username,"Login Succesfully",accessToken,true);

        return authResponse;



    }


    public Authentication authenticate(String username, String password){

        UserDetails userDetails= this.loadUserByUsername(username);

        if(userDetails == null){
            throw  new BadCredentialsException("Invalid username or password");
        }

        if(!passwordEncoder.matches(password, userDetails.getPassword())){
            throw  new BadCredentialsException("Invalid  password");
        }


       return new UsernamePasswordAuthenticationToken(username, userDetails.getPassword(),userDetails.getAuthorities());



    }


    public AuthResponse createUser(AuthCreateUserRequest authCreateUserRequest){

        String username = authCreateUserRequest.username();
        String password = authCreateUserRequest.password();

        List<String> roleRequest = authCreateUserRequest.roleRequest().roleListName();

        Set<RoleEntity> roleEntitySet = this.roleEntityService.findRoleEntityByRoleEnumIn(roleRequest).stream().collect(Collectors.toSet());


        if(roleEntitySet.isEmpty()){

            throw new IllegalArgumentException("The specified roles does not exist");
        }


        UserEntity userEntity = UserEntity.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .roles(roleEntitySet)
                .accountNotLocked(true)
                .credentialsNotExpired(true)
                .isEnabled(true)
                .accountNotExpired(true)
                .build();



        UserEntity userCreated = this.userEntityService.save(userEntity);


        ArrayList<SimpleGrantedAuthority> authorityList = new ArrayList<>();

        userCreated.getRoles().forEach(role -> authorityList.add(new SimpleGrantedAuthority("ROLE_".concat(role.getRoleEnum().name()))));

        userCreated.getRoles()
                .stream() //PRIMER STREAM PARA RECORRER LOS ROLES
                .flatMap(role -> role.getPermissions().stream())//SEGUNDO STREAM RECORRE LOS PERMISOS DE CADA ROLE
                .forEach(permission -> authorityList.add(new SimpleGrantedAuthority(permission.getName()))); // AGREGAMOS CADA PERMISO A LA LISTA



        Authentication authentication = new UsernamePasswordAuthenticationToken(userCreated.getUsername(), userCreated.getPassword(), authorityList);

        String accessToken = jwtUtils.createToken(authentication);

        AuthResponse authResponse = new AuthResponse(userCreated.getUsername(),"User created succesfully", accessToken, true);


        return authResponse;



    }









}
