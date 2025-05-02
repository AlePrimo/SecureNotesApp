package com.app.persistence.implementations;

import com.app.entities.Note;
import com.app.persistence.INoteDAO;
import com.app.repositories.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

public class NoteDAOImpl implements INoteDAO {

    @Autowired
    private NoteRepository noteRepository;

    @Override
    public List<Note> findAll() {
        return (List<Note>) this.noteRepository.findAll();
    }

    @Override
    public Optional<Note> findById(Long id) {
        return this.noteRepository.findById(id);
    }

    @Override
    public void save(Note note) {
        this.noteRepository.save(note);
    }

    @Override
    public void deleteById(Long id) {
         this.noteRepository.deleteById(id);
    }
}
