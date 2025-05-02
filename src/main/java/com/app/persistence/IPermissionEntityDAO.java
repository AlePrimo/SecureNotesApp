package com.app.persistence;

import com.app.entities.PermissionEntity;


import java.util.List;
import java.util.Optional;

public interface IPermissionEntityDAO {

    List<PermissionEntity> findAll();
    Optional<PermissionEntity> findById(Long id);
    void save(PermissionEntity permission);
    void deleteById(Long id);


}
