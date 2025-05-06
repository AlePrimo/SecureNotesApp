package com.app.services.entitiyservices;

import com.app.entities.RoleEntity;
import com.app.entities.RoleEnum;

import java.util.List;
import java.util.Optional;

public interface IRoleEntityService {

    List<RoleEntity> findAll();

    Optional<RoleEntity> findById(Long id);

    RoleEntity save(RoleEntity role);

    void deleteById(Long id);

    List<RoleEntity> findRoleEntityByRoleEnumIn(List<String> roleNames);

    Optional<RoleEntity> findByRoleEnum(RoleEnum roleEnum);


}
