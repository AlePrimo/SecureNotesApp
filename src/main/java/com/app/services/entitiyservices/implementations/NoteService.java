package com.app.services.entitiyservices.implementations;

import com.app.entities.Note;
import com.app.persistence.implementations.NoteDAOImpl;
import com.app.services.entitiyservices.INoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class NoteService implements INoteService {

    @Autowired
    private NoteDAOImpl noteDAO;

    @Override
    public List<Note> findAll() {
        return this.noteDAO.findAll();
    }

    @Override
    public Optional<Note> findById(Long id) {
        return this.noteDAO.findById(id);
    }

    @Override
    public void save(Note note) {
       this.noteDAO.save(note);
    }

    @Override
    public void deleteById(Long id) {
this.noteDAO.deleteById(id);
    }

    @Override
    public List<Note> findByUserUsername(String username) {
        return this.noteDAO.findByUserUsername(username);
    }
}
