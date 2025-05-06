package com.app.services.entitiyservices;

import com.app.entities.Note;

import java.util.List;
import java.util.Optional;

public interface INoteService {

    List<Note> findAll();

    Optional<Note> findById(Long id);

    void save(Note note);

    void deleteById(Long id);

    List<Note> findByUserUsername(String username);
}
