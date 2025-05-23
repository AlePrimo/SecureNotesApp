package com.app.persistence;

import com.app.entities.UserEntity;

import java.util.List;
import java.util.Optional;

public interface IUserEntityDAO {

    List<UserEntity> findAll();

    Optional<UserEntity> findById(Long id);

    UserEntity save(UserEntity user);

    void deleteById(Long id);

    Optional<UserEntity> findByUsername(String username);

}
