package com.app.persistence.implementations;

import com.app.entities.PermissionEntity;
import com.app.persistence.IPermissionEntityDAO;
import com.app.repositories.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

public class PermissionEntityDAO implements IPermissionEntityDAO {

    @Autowired
    private PermissionRepository permissionRepository;
    @Override
    public List<PermissionEntity> findAll() {
        return (List<PermissionEntity>) this.permissionRepository.findAll();
    }

    @Override
    public Optional<PermissionEntity> findById(Long id) {
        return this.permissionRepository.findById(id);
    }

    @Override
    public void save(PermissionEntity permission) {
this.permissionRepository.save(permission);
    }

    @Override
    public void deleteById(Long id) {
        this.permissionRepository.deleteById(id);

    }
}
