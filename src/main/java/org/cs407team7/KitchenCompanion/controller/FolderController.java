package org.cs407team7.KitchenCompanion.controller;

import org.cs407team7.KitchenCompanion.entity.Folder;
import org.cs407team7.KitchenCompanion.entity.Recipe;
import org.cs407team7.KitchenCompanion.entity.User;
import org.cs407team7.KitchenCompanion.repository.FolderRepository;
import org.cs407team7.KitchenCompanion.repository.RecipeRepository;
import org.cs407team7.KitchenCompanion.responseobject.ErrorResponse;
import org.cs407team7.KitchenCompanion.responseobject.GenericResponse;
import org.cs407team7.KitchenCompanion.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/folder")
public class FolderController {

    // Lazy, move me to a service not a controller
    FolderRepository folderRepository;

    RecipeRepository recipeRepository;

    public UserService userService;


    @Autowired
    public FolderController(FolderRepository folderRepository, UserService userService, RecipeRepository recipeRepository) {
        this.folderRepository = folderRepository;
        this.userService = userService;
        this.recipeRepository = recipeRepository;
    }

    @PostMapping(value = "/new")
    public ResponseEntity<?> newFolder(
            @RequestBody Map<String, String> payload
    ) {
        User user = userService.getAuthUser();
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    new ErrorResponse(401, "You must be logged in to create a new folder."));
        }
        try {
            if (!payload.containsKey("title")) {
                throw new IllegalArgumentException("Must contain title");
            }
            // Use the getAuthUser() in the future.
            Long createdBy = user.getId();
            Folder folder = new Folder();
            folder.setCreatedBy(createdBy);
            folder.setTitle(payload.get("title"));

            folderRepository.save(folder);
            return ResponseEntity.status(HttpStatus.CREATED).body(new GenericResponse(folder));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(
                    new ErrorResponse(400, e.getMessage()));
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ErrorResponse(500, "Internal Server Error"));

        }
    }

    @PostMapping(path = "/save")
    public ResponseEntity<Object> saveRecipe(
            @RequestBody Map<String, String> payload
    ) {
        User user = userService.getAuthUser();
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    new ErrorResponse(401, "You must be logged in to see saved recipes."));
        }
        try {
            if (!payload.containsKey("folder") || !payload.containsKey("recipe")) {
                throw new IllegalArgumentException("Must contain a folder and recipe id");
            }
            Folder folder = folderRepository.findById(Long.parseLong(payload.get("folder")))
                    .orElseThrow(() -> new NoSuchElementException("Could not find a folder with that name"));
            Recipe recipe = recipeRepository.findById(Long.parseLong(payload.get("recipe")))
                    .orElseThrow(() -> new NoSuchElementException("Could not find a recipe with that id"));
            if (folder == null) {
                throw new NoSuchElementException("Could not find that folder");
            }
            if (folder.getCreatedBy() != user.getId()) {
                throw new IllegalArgumentException("You cannot modify other user's folders");
            }
            if (!folder.getRecipies().contains(recipe.getId())) {
                folder.getRecipies().add(recipe.getId());
            }
            folderRepository.save(folder);
            return ResponseEntity.status(HttpStatus.OK).body(new GenericResponse(folder));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(400).body(
                    new ErrorResponse(400, e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(
                    new ErrorResponse(400, "Could not find a recipe with that name"));
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ErrorResponse(500, "Internal Server Error"));

        }
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
