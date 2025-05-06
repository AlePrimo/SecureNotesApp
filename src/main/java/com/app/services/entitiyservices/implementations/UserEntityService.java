package com.app.services.entitiyservices.implementations;

import com.app.entities.UserEntity;
import com.app.persistence.implementations.UserEntityDAOImpl;
import com.app.services.entitiyservices.IUserEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class UserEntityService implements IUserEntityService {

    @Autowired
    private UserEntityDAOImpl userEntityDAO;

    @Override
    public List<UserEntity> findAll() {
        return this.userEntityDAO.findAll();
    }

    @Override
    public Optional<UserEntity> findById(Long id) {
        return this.userEntityDAO.findById(id);
    }

    @Override
    public UserEntity save(UserEntity user) {
        return this.userEntityDAO.save(user);
    }

    @Override
    public void deleteById(Long id) {
        this.userEntityDAO.deleteById(id);
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        return this.userEntityDAO.findByUsername(username);
    }
}
