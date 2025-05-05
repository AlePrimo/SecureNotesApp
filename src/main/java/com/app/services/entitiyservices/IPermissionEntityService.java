package com.app.services.entitiyservices;

import com.app.entities.PermissionEntity;

import java.util.List;
import java.util.Optional;

public interface IPermissionEntityService {
    List<PermissionEntity> findAll();
    Optional<PermissionEntity> findById(Long id);
    PermissionEntity save(PermissionEntity permission);
    void deleteById(Long id);
    Optional<PermissionEntity> findByName(String name);
}
