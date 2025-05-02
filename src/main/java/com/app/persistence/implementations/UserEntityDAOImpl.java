package com.app.persistence.implementations;

import com.app.entities.UserEntity;
import com.app.persistence.IUserEntityDAO;
import com.app.repositories.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

public class UserEntityDAOImpl implements IUserEntityDAO {

    @Autowired
    private UserEntityRepository userEntityRepository;

    @Override
    public List<UserEntity> findAll() {
        return (List<UserEntity>) this.userEntityRepository.findAll();
    }

    @Override
    public Optional<UserEntity> findById(Long id) {
        return this.userEntityRepository.findById(id);
    }

    @Override
    public UserEntity save(UserEntity user) {

        return this.userEntityRepository.save(user);
    }

    @Override
    public void deleteById(Long id) {

        this.userEntityRepository.deleteById(id);
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        return this.userEntityRepository.findByUsername(username);
    }
}
