package com.app.services.entitiyservices;

import com.app.entities.RoleEntity;

import java.util.List;
import java.util.Optional;

public interface IRoleEntityService {

    List<RoleEntity> findAll();
    Optional<RoleEntity> findById(Long id);
    void save(RoleEntity role);
    void deleteById(Long id);
    List<RoleEntity> findRoleEntityByRoleEnumIn (List<String> roleNames);



}
