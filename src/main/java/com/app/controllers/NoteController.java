package com.app.controllers;


import com.app.controllers.dtos.CreateNoteDTO;
import com.app.controllers.dtos.NoteDTO;
import com.app.entities.Note;
import com.app.entities.UserEntity;
import com.app.services.entitiyservices.implementations.NoteService;
import com.app.services.entitiyservices.implementations.UserEntityService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/notes")
public class NoteController {

    @Autowired
    private NoteService noteService;

    @Autowired
    private UserEntityService userEntityService;


    @GetMapping("/findById/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> findById(@PathVariable Long id, Authentication authentication) {
        Optional<Note> noteOptional = this.noteService.findById(id);

        if (noteOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Note note = noteOptional.get();

        String username = authentication.getName();
        boolean isAdmin = isAdmin(authentication);
        boolean isDeveloper = isDeveloper(authentication);

        if (!note.getUser().getUsername().equals(username) && !isAdmin && !isDeveloper) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No tienes permisos para ver esta nota");
        }

        NoteDTO noteDTO = NoteDTO.builder()
                .id(note.getId())
                .title(note.getTitle())
                .description(note.getDescription())
                .userId(note.getUser().getId())
                .build();
        return ResponseEntity.ok(noteDTO);


    }


    @GetMapping("/findAll")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> findAll(Authentication authentication) {


        String username = authentication.getName();
        boolean isAdmin = isAdmin(authentication);
        boolean isDeveloper = isDeveloper(authentication);


        List<Note> notes;

        if (isAdmin || isDeveloper) {
            notes = this.noteService.findAll();
        } else {
            notes = this.noteService.findByUserUsername(username);
        }


        List<NoteDTO> noteDTOList = notes.stream()
                .map(note -> NoteDTO.builder().id(note.getId())
                        .title(note.getTitle())
                        .description(note.getDescription())
                        .userId(note.getUser().getId()).build()).toList();

        return ResponseEntity.ok(noteDTOList);

    }


    @PostMapping("/saveNote")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> saveNote(@RequestBody @Valid CreateNoteDTO createNoteDTO, Authentication authentication) throws URISyntaxException {

        String username = authentication.getName();

        Optional<UserEntity> optionalUser = this.userEntityService.findByUsername(username);

        System.out.println("Username extraído del token: " + username);

        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no válido");
        }


        this.noteService.save(Note.builder()
                .title(createNoteDTO.getTitle())
                .description(createNoteDTO.getDescription())
                .user(optionalUser.get())
                .build());

        return ResponseEntity.created(new URI("/api/notes/saveNote")).build();


    }






    @PutMapping("/updateNote/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> updateNote(@PathVariable Long id, @RequestBody NoteDTO noteDTO, Authentication authentication) {

        Optional<Note> noteOptional = this.noteService.findById(id);
        if (noteOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Note note = noteOptional.get();
        String username = authentication.getName();
        boolean isAdmin = isAdmin(authentication);
        boolean isDeveloper = isDeveloper(authentication);

        if (!note.getUser().getUsername().equals(username) && !isAdmin && !isDeveloper) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No tienes permisos para editar esta nota");
        }


        note.setTitle(noteDTO.getTitle());
        note.setDescription(noteDTO.getDescription());
        this.noteService.save(note);

        return ResponseEntity.ok("Nota Actualizada");


    }


    @DeleteMapping("/deleteById/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> deleteById(@PathVariable Long id, Authentication authentication) {


        Optional<Note> noteOptional = this.noteService.findById(id);

        if (noteOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        if (!isAdmin(authentication) && !isDeveloper(authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No tienes permiso para eliminar notas");
        }

        this.noteService.deleteById(id);
        return ResponseEntity.ok("Nota eliminada con éxito");


    }



    private boolean isAdmin(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
    }


    private boolean isDeveloper(Authentication auth) {
        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_DEVELOPER"));
    }






}






