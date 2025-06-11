package com.example.LMS.controller;

import com.example.LMS.dto.ApiResponse;
import com.example.LMS.dto.AuthorDTO;
import com.example.LMS.exception.DuplicateResourceException;
import com.example.LMS.exception.ResourceNotFoundException;
import com.example.LMS.service.AuthorService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/authors")
@Validated
@Slf4j
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AuthorDTO>> createAuthor(@Valid @RequestBody AuthorDTO authorDTO) {
        try {
            if (log.isInfoEnabled()) {
                log.info("Creating new author: {}", authorDTO.getName());
            }
            AuthorDTO createdAuthor = authorService.createAuthor(authorDTO);

            ApiResponse<AuthorDTO> response = ApiResponse.<AuthorDTO>builder()
                    .success(true)
                    .message("Author created successfully")
                    .data(createdAuthor)
                    .timestamp(LocalDateTime.now())
                    .build();

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (DuplicateResourceException e) {
            log.error("Duplicate author creation attempted: {}", e.getMessage());

            ApiResponse<AuthorDTO> errorResponse = ApiResponse.<AuthorDTO>builder()
                    .success(false)
                    .message("Author with this name already exists")
                    .timestamp(LocalDateTime.now())
                    .build();

            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);

        } catch (Exception e) {
            log.error("Error creating author: {}", e.getMessage(), e);

            ApiResponse<AuthorDTO> errorResponse = ApiResponse.<AuthorDTO>builder()
                    .success(false)
                    .message("Failed to create author")
                    .timestamp(LocalDateTime.now())
                    .build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AuthorDTO>>> getAllAuthors() {
        try {
            List<AuthorDTO> authors = authorService.getAllAuthors();

            ApiResponse<List<AuthorDTO>> response = ApiResponse.<List<AuthorDTO>>builder()
                    .success(true)
                    .message("Authors retrieved successfully")
                    .data(authors)
                    .timestamp(LocalDateTime.now())
                    .build();

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error retrieving authors: {}", e.getMessage(), e);

            ApiResponse<List<AuthorDTO>> errorResponse = ApiResponse.<List<AuthorDTO>>builder()
                    .success(false)
                    .message("Failed to retrieve authors")
                    .timestamp(LocalDateTime.now())
                    .build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AuthorDTO>> getAuthorById(@PathVariable UUID id) {
        try {
            AuthorDTO author = authorService.getAuthorById(id);

            ApiResponse<AuthorDTO> response = ApiResponse.<AuthorDTO>builder()
                    .success(true)
                    .message("Author retrieved successfully")
                    .data(author)
                    .timestamp(LocalDateTime.now())
                    .build();

            return ResponseEntity.ok(response);

        } catch (ResourceNotFoundException e) {
            log.error("Author not found: {}", e.getMessage());

            ApiResponse<AuthorDTO> errorResponse = ApiResponse.<AuthorDTO>builder()
                    .success(false)
                    .message("Author not found")
                    .timestamp(LocalDateTime.now())
                    .build();

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);

        } catch (Exception e) {
            log.error("Error retrieving author: {}", e.getMessage(), e);

            ApiResponse<AuthorDTO> errorResponse = ApiResponse.<AuthorDTO>builder()
                    .success(false)
                    .message("Failed to retrieve author")
                    .timestamp(LocalDateTime.now())
                    .build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AuthorDTO>> updateAuthor(@PathVariable UUID id,
                                                               @Valid @RequestBody AuthorDTO authorDTO) {
        try {
            log.info("Updating author with id: {}", id);
            AuthorDTO updatedAuthor = authorService.updateAuthor(id, authorDTO);

            ApiResponse<AuthorDTO> response = ApiResponse.<AuthorDTO>builder()
                    .success(true)
                    .message("Author updated successfully")
                    .data(updatedAuthor)
                    .timestamp(LocalDateTime.now())
                    .build();

            return ResponseEntity.ok(response);

        } catch (ResourceNotFoundException e) {
            log.error("Author not found for update: {}", e.getMessage());

            ApiResponse<AuthorDTO> errorResponse = ApiResponse.<AuthorDTO>builder()
                    .success(false)
                    .message("Author not found")
                    .timestamp(LocalDateTime.now())
                    .build();

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);

        } catch (DuplicateResourceException e) {
            log.error("Duplicate author name during update: {}", e.getMessage());

            ApiResponse<AuthorDTO> errorResponse = ApiResponse.<AuthorDTO>builder()
                    .success(false)
                    .message("Author with this name already exists")
                    .timestamp(LocalDateTime.now())
                    .build();

            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);

        } catch (Exception e) {
            log.error("Error updating author: {}", e.getMessage(), e);

            ApiResponse<AuthorDTO> errorResponse = ApiResponse.<AuthorDTO>builder()
                    .success(false)
                    .message("Failed to update author")
                    .timestamp(LocalDateTime.now())
                    .build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteAuthor(@PathVariable UUID id) {
        try {
            log.info("Deleting author with id: {}", id);
            authorService.deleteAuthor(id);

            ApiResponse<Void> response = ApiResponse.<Void>builder()
                    .success(true)
                    .message("Author deleted successfully")
                    .timestamp(LocalDateTime.now())
                    .build();

            return ResponseEntity.ok(response);

        } catch (ResourceNotFoundException e) {
            log.error("Author not found for deletion: {}", e.getMessage());

            ApiResponse<Void> errorResponse = ApiResponse.<Void>builder()
                    .success(false)
                    .message("Author not found")
                    .timestamp(LocalDateTime.now())
                    .build();

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);

        } catch (Exception e) {
            log.error("Error deleting author: {}", e.getMessage(), e);

            ApiResponse<Void> errorResponse = ApiResponse.<Void>builder()
                    .success(false)
                    .message("Failed to delete author")
                    .timestamp(LocalDateTime.now())
                    .build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}