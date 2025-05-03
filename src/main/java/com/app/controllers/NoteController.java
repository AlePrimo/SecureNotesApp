package com.app.controllers;


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
    public ResponseEntity<?> findById(@PathVariable Long id){
        Optional<Note> noteOptional = this.noteService.findById(id);

        if(noteOptional.isPresent()){
         Note note = noteOptional.get();
            NoteDTO noteDTO = NoteDTO.builder()
                    .id(id)
                    .title(note.getTitle())
                    .description(note.getDescription())
                    .userId(note.getUser().getId())
                    .build();
            return ResponseEntity.ok(noteDTO);

        }
       return ResponseEntity.notFound().build();

    }


    @GetMapping("/findAll")
    public ResponseEntity<?> findAll(){

        List<NoteDTO> noteDTOList = this.noteService.findAll().stream()
            .map(note -> NoteDTO.builder().id(note.getId())
                    .title(note.getTitle())
                    .description(note.getDescription())
                    .userId(note.getUser().getId()).build()).toList();

        return ResponseEntity.ok(noteDTOList);

}



    @PostMapping("/saveNote")
    public ResponseEntity<?> saveNote(@RequestBody @Valid NoteDTO noteDTO) throws URISyntaxException {

        Optional<UserEntity> optionalUser = this.userEntityService.findById(noteDTO.getUserId());

        if (optionalUser.isEmpty()) {
            return ResponseEntity.badRequest().body("Usuario no encontrado");
        }


        this.noteService.save(Note.builder()
                .title(noteDTO.getTitle())
                .description(noteDTO.getDescription())
                .user(optionalUser.get()).build());

       return ResponseEntity.created(new URI("/api/notes/saveNote")).build();


}



    @PutMapping("/updateNote/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> updateNote(@PathVariable Long id, @RequestBody NoteDTO noteDTO, Authentication authentication){

        Optional<Note> noteOptional = this.noteService.findById(id);
        if (noteOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Note note = noteOptional.get();
        Optional<UserEntity> optionalUser = this.userEntityService.findById(noteDTO.getUserId());

        if (optionalUser.isEmpty()) {
            return ResponseEntity.badRequest().body("Usuario no encontrado");
        }

        String username = authentication.getName();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        if (!note.getUser().getUsername().equals(username) && !isAdmin) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No tienes permisos para editar esta nota");
        }



           note.setTitle(noteDTO.getTitle());
           note.setDescription(noteDTO.getDescription());
           note.setUser(optionalUser.get());
           this.noteService.save(note);

           return ResponseEntity.ok("Nota Actualizada");






}


    @DeleteMapping("/deleteById/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> deleteById(@PathVariable Long id ,Authentication authentication) {

        Optional<Note> noteOptional = this.noteService.findById(id);

        if (noteOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Note note = noteOptional.get();

        String authUsername = authentication.getName();

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        if (!note.getUser().getUsername().equals(authUsername) && !isAdmin) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No tienes permiso para eliminar esta nota");
        }


        this.noteService.deleteById(id);
        return ResponseEntity.ok("Nota eliminada con éxito");

    }








//
//   HAY QUE VER COMO COMPAGINAR LAS AUTORIZACIONES CON ESTOS ENDPOINTS
//            SUBIR A GITHUB Y PASARLE A CHAT




}
