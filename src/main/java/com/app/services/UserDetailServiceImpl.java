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

        String username = authCreateUserRequest.username(); //OBTENEMOS EL USERNAME
        String password = authCreateUserRequest.password(); //OBTENEMOS EL PASSWORD

        List<String> roleRequest = authCreateUserRequest.roleRequest().roleListName(); //OBTENEMOS LA LISTA DE ROLES QUE SUPUESTAMENTE VA A TENER EL USUARIO

        Set<RoleEntity> roleEntitySet = this.roleEntityService.findRoleEntityByRoleEnumIn(roleRequest).stream().collect(Collectors.toSet()); //COMPROBAMOS QUE LA LISTA
        // DE ROLES QUE SUPUESTAMENTE VA A TENER EL USUARIO CORRESPONDAN A LOS ROLES QUE ESTAN ESPECIFICADOS EN BASE DE DATOS

        if(roleEntitySet.isEmpty()){ // VERIFICAMOS QUE SI LOS ROLES CON QUE SE PRETENDE REGISTRAR EL USUARIO NO EXISTEN ,EL SET DE ROLES ESTARA VACIO
            // SI ES ASI EL SISTEMA DARA UNA EXCEPCION
            throw new IllegalArgumentException("The specified roles does not exist");
        }

        // UNA VEZ VERIFICADOS LOS ROLES  , SE CREA EL USUARIO CON TODOS SUS ATRIBUTOS
        UserEntity userEntity = UserEntity.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .roles(roleEntitySet)
                .accountNotLocked(true)
                .credentialsNotExpired(true)
                .isEnabled(true)
                .accountNotExpired(true)
                .build();


        // UNA VEZ CREADO EL USUARIO SE GUARDA EN BASE DE DATOS Y SE LO ASIGNA A UN OBJETO USERCREATED
        UserEntity userCreated = this.userEntityService.save(userEntity);

        //CREAMOS UNA LISTA PARA AGREGAR LOS ROLES DEL CLIENTE  DEL TIPO SIMPLEGRANTEDAUTHORITY PARA QUE SPRING LO RECONCOZCA Y LO MANEJE
        ArrayList<SimpleGrantedAuthority> authorityList = new ArrayList<>();

        //LUEGO ACCEDEMOS AL SET DE ROLES QUE TIENE EL USUARIO CREADO Y AL RECORRERLO VAMOS CREANDO UN OBJETO SIMPLEGRANTED Y LOS VAMOS AGREGANDO A LA LISTA ANTERIOR
        userCreated.getRoles().forEach(role -> authorityList.add(new SimpleGrantedAuthority("ROLE_".concat(role.getRoleEnum().name())))); //SIEMPRE AGREGAR LOS ROLES COMO "ROLE_"
        //MAS EL NOMBRE CONCATENADO


        //LUEDO DE ESTO DEBEMOS ACCEDER A LOS PERMISOS QUE TIENE CADA ROLE ,PARA ESO VAMOS A NECESITAR USAR DOS STREAMS , UNO QUE RECORRA LA LISTA DE ROLES Y OTRO QUE RECORRA
        //LA LISTA DE PERMISOS QUE TIENE CADA ROLE, Y QUE VAYA AGREGANDOLOS A LA LISTA AUTHORITYLIST

        userCreated.getRoles()
                .stream() //PRIMER STREAM PARA RECORRER LOS ROLES
                .flatMap(role -> role.getPermissions().stream())//SEGUNDO STREAM RECORRE LOS PERMISOS DE CADA ROLE
                .forEach(permission -> authorityList.add(new SimpleGrantedAuthority(permission.getName()))); // AGREGAMOS CADA PERMISO A LA LISTA




        //LUEGO DEBEMOS AUTENTICAR EL USUARIO CON SU USERNAME, PASSWORD Y LA LISTA DE AUTHORITIES QUE POSEE

        Authentication authentication = new UsernamePasswordAuthenticationToken(userCreated.getUsername(), userCreated.getPassword(), authorityList);


        //CREAR EL TOKEN CON LA AUTENTICACION

        String accessToken = jwtUtils.createToken(authentication);

        //CREAR EL OBJETO AUTHRESPONSE CON SUS ATRIBUTOS

        AuthResponse authResponse = new AuthResponse(userCreated.getUsername(),"User created succesfully", accessToken, true);


        return authResponse;



    }









}
