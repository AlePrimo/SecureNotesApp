package com.app.controllers;


import com.app.controllers.dtos.NoteDTO;
import com.app.entities.Note;
import com.app.services.entitiyservices.implementations.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/notes")
public class NoteController {

    @Autowired
    private NoteService noteService;


    @GetMapping("/findById/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id){
        Optional<Note> noteOptional = this.noteService.findById(id);

        if(noteOptional.isPresent()){
         Note note = noteOptional.get();
            NoteDTO noteDTO = NoteDTO.builder()
                    .id(id)
                    .title(note.getTitle())
                    .description(note.getDescription())
                    .user(note.getUser())
                    .build();
            return ResponseEntity.ok(noteDTO);

        }
       return ResponseEntity.notFound().build();

    }
//
//   FALTAN TODOS LOS ENDPOINTS DE NOTE , LOS DE ROLE??? , LOS DE PERMISSION???  HAY QUE VER COMO COMPAGINAR LAS AUTORIZACIONES CON ESTOS ENDPOINTS
//            SUBIR A GITHUB Y PASARLE A CHAT




}
