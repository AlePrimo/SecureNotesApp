package com.app.persistence;

import com.app.entities.RoleEntity;


import java.util.List;
import java.util.Optional;

public interface IRoleEntityDAO {

    List<RoleEntity> findAll();
    Optional<RoleEntity> findById(Long id);
    void save(RoleEntity role);
    void deleteById(Long id);
    List<RoleEntity> findRoleEntityByRoleEnumIn (List<String> roleNames);


}
