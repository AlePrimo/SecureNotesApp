package com.app.repositories;

import com.app.entities.PermissionEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PermissionRepository extends CrudRepository<PermissionEntity, Long> {
    Optional<PermissionEntity> findByName(String name);
}
