package com.app.services.entitiyservices.implementations;

import com.app.entities.PermissionEntity;
import com.app.persistence.implementations.PermissionEntityDAO;
import com.app.services.entitiyservices.IPermissionEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PermissionEntityService implements IPermissionEntityService {

    @Autowired
    private PermissionEntityDAO permissionEntityDAO;

    @Override
    public List<PermissionEntity> findAll() {
        return this.permissionEntityDAO.findAll();
    }

    @Override
    public Optional<PermissionEntity> findById(Long id) {
        return this.permissionEntityDAO.findById(id);
    }

    @Override
    public PermissionEntity save(PermissionEntity permission) {
         return this.permissionEntityDAO.save(permission);
    }

    @Override
    public void deleteById(Long id) {
this.permissionEntityDAO.deleteById(id);
    }

    @Override
    public Optional<PermissionEntity> findByName(String name) {
        return this.permissionEntityDAO.findByName(name);
    }
}
