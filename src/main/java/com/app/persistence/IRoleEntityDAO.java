package com.app.persistence;

import com.app.entities.RoleEntity;
import com.app.entities.RoleEnum;


import java.util.List;
import java.util.Optional;

public interface IRoleEntityDAO {

    List<RoleEntity> findAll();
    Optional<RoleEntity> findById(Long id);
    RoleEntity save(RoleEntity role);
    void deleteById(Long id);
    List<RoleEntity> findRoleEntityByRoleEnumIn (List<String> roleNames);
    Optional<RoleEntity> findByRoleEnum(RoleEnum roleEnum);

}
