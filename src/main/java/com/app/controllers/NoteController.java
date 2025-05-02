package com.app.controllers;


import com.app.controllers.dtos.NoteDTO;
import com.app.entities.Note;
import com.app.services.entitiyservices.implementations.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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


    @GetMapping("/findAll")
    public ResponseEntity<?> findAll(){

        List<NoteDTO> noteDTOList = this.noteService.findAll().stream()
            .map(note -> NoteDTO.builder().id(note.getId())
                    .title(note.getTitle())
                    .description(note.getDescription())
                    .user(note.getUser()).build()).toList();

        return ResponseEntity.ok(noteDTOList);

}



    @PostMapping("/saveNote")
    public ResponseEntity<?> saveNote(@RequestBody NoteDTO noteDTO) throws URISyntaxException {

if(noteDTO.getTitle().isBlank()){
    return ResponseEntity.badRequest().build();
}

this.noteService.save(Note.builder()
        .id(noteDTO.getId())
        .title(noteDTO.getTitle())
        .description(noteDTO.getDescription())
        .user(noteDTO.getUser()).build());

return ResponseEntity.created(new URI("/api/notes/saveNote")).build();


}



    @PutMapping("/updateNote/{id}")
    public ResponseEntity<?> updateNote(@PathVariable Long id, @RequestBody NoteDTO noteDTO){

        Optional<Note> noteOptional = this.noteService.findById(id);

        if(noteOptional.isPresent()){
           Note note = noteOptional.get();
           note.setTitle(noteDTO.getTitle());
           note.setDescription(noteDTO.getDescription());
           note.setUser(noteDTO.getUser());
           this.noteService.save(note);
           return ResponseEntity.ok("Nota Actualizada");
        }

        return ResponseEntity.notFound().build();



}


    @DeleteMapping("/deleteById/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        Optional<Note> noteOptional = this.noteService.findById(id);

     if(id != null && noteOptional.isPresent()){
          this.noteService.deleteById(id);
          return ResponseEntity.ok("Nota Eliminada");}

        return ResponseEntity.notFound().build();
    }










//
//   FALTAN TODOS LOS ENDPOINTS DE NOTE , LOS DE ROLE??? , LOS DE PERMISSION???  HAY QUE VER COMO COMPAGINAR LAS AUTORIZACIONES CON ESTOS ENDPOINTS
//            SUBIR A GITHUB Y PASARLE A CHAT




}
