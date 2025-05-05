package com.app.services.entitiyservices.implementations;

import com.app.entities.RoleEntity;
import com.app.entities.RoleEnum;
import com.app.persistence.implementations.RoleEntityDAO;
import com.app.services.entitiyservices.IRoleEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleEntityService implements IRoleEntityService {

    @Autowired
    private RoleEntityDAO roleEntityDAO;


    @Override
    public List<RoleEntity> findAll() {
        return this.roleEntityDAO.findAll();
    }

    @Override
    public Optional<RoleEntity> findById(Long id) {
        return this.roleEntityDAO.findById(id);
    }

    @Override
    public RoleEntity save(RoleEntity role) {
return this.roleEntityDAO.save(role);
    }

    @Override
    public void deleteById(Long id) {
this.roleEntityDAO.deleteById(id);
    }

    @Override
    public List<RoleEntity> findRoleEntityByRoleEnumIn(List<String> roleNames) {
        return this.roleEntityDAO.findRoleEntityByRoleEnumIn(roleNames);
    }

    @Override
    public Optional<RoleEntity> findByRoleEnum(RoleEnum roleEnum) {
        return Optional.empty();
    }


}
