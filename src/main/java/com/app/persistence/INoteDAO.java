package com.app.persistence;

import com.app.entities.Note;

import java.util.List;
import java.util.Optional;

public interface INoteDAO {

    List<Note> findAll();
    Optional<Note> findById(Long id);
    void save(Note note);
    void deleteById(Long id);

}
