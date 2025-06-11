package com.example.LMS.service;

import com.example.LMS.dto.AuthorDTO;
import com.example.LMS.exception.DuplicateResourceException;
import com.example.LMS.exception.ResourceNotFoundException;
import com.example.LMS.model.Author;
import com.example.LMS.repository.AuthorRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public AuthorDTO createAuthor(AuthorDTO authorDTO) {
        if (authorRepository.existsByName(authorDTO.getName())) {
            throw new DuplicateResourceException("Author with name '" + authorDTO.getName() + "' already exists");
        }

        Author author = Author.builder()
                .name(authorDTO.getName())
                .biography(authorDTO.getBiography())
                .birthDate(authorDTO.getBirthDate())
                .nationality(authorDTO.getNationality())
                .build();

        Author savedAuthor = authorRepository.save(author);
        log.info("Created new author: {}", savedAuthor.getName());

        return convertToDTO(savedAuthor);
    }

    public List<AuthorDTO> getAllAuthors() {
        return authorRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public AuthorDTO getAuthorById(UUID id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + id));
        return convertToDTO(author);
    }

    public AuthorDTO updateAuthor(UUID id, AuthorDTO authorDTO) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + id));

        // Check if name is being changed and if new name already exists
        if (!author.getName().equals(authorDTO.getName()) &&
                authorRepository.existsByName(authorDTO.getName())) {
            throw new DuplicateResourceException("Author with name '" + authorDTO.getName() + "' already exists");
        }

        author.setName(authorDTO.getName());
        author.setBiography(authorDTO.getBiography());
        author.setBirthDate(authorDTO.getBirthDate());
        author.setNationality(authorDTO.getNationality());

        Author updatedAuthor = authorRepository.save(author);
        log.info("Updated author: {}", updatedAuthor.getName());

        return convertToDTO(updatedAuthor);
    }

    public void deleteAuthor(UUID id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + id));

        authorRepository.delete(author);
        log.info("Deleted author: {}", author.getName());
    }

    private AuthorDTO convertToDTO(Author author) {
        return AuthorDTO.builder()
                .id(author.getId())
                .name(author.getName())
                .biography(author.getBiography())
                .birthDate(author.getBirthDate())
                .nationality(author.getNationality())
                .totalBooks(author.getTotalBooks())
                .createdAt(author.getCreatedAt())
                .updatedAt(author.getUpdatedAt())
                .build();
    }
}

