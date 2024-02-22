package org.cs407team7.KitchenCompanion.controller;

import org.cs407team7.KitchenCompanion.entity.Folder;
import org.cs407team7.KitchenCompanion.entity.Recipe;
import org.cs407team7.KitchenCompanion.repository.FolderRepository;
import org.cs407team7.KitchenCompanion.responseobject.ErrorResponse;
import org.cs407team7.KitchenCompanion.responseobject.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/folders")
public class FolderController {

    // Lazy, move me to a service not a controller
    FolderRepository folderRepository;

    @Autowired
    public FolderController(FolderRepository folderRepository) {
        this.folderRepository = folderRepository;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getFolder(
            @PathVariable Long id
    ) {
        try {
            Folder folder = folderRepository.findById(id).orElse(null);
            if (folder == null) {
                throw new RuntimeException("Could not find that folder");
            }
            // check folder privacy

            return ResponseEntity.ok(new GenericResponse(200, folder));

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(304, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(500, "Internal Server Error"));
        }
    }
}
