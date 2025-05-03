package com.app.persistence.implementations;

import com.app.entities.RoleEntity;
import com.app.persistence.IRoleEntityDAO;
import com.app.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;


@Component
public class RoleEntityDAO implements IRoleEntityDAO {

    @Autowired
    private RoleRepository roleRepository;
    @Override
    public List<RoleEntity> findAll() {
        return this.roleRepository.findAll();
    }

    @Override
    public Optional<RoleEntity> findById(Long id) {
        return this.roleRepository.findById(id);
    }

    @Override
    public void save(RoleEntity role) {
      this.roleRepository.save(role);
    }

    @Override
    public void deleteById(Long id) {
        this.roleRepository.deleteById(id);

    }

    @Override
    public List<RoleEntity> findRoleEntityByRoleEnumIn(List<String> roleNames) {
        return this.roleRepository.findRoleEntityByRoleEnumIn(roleNames);
    }
}
