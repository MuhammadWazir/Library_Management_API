package com.example.LMS.controller;

import com.example.LMS.dto.ApiResponse;
import com.example.LMS.dto.BorrowerDTO;
import com.example.LMS.service.BorrowerService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/borrowers")
@Validated
@Slf4j
public class BorrowerController {

    private final BorrowerService borrowerService;

    public BorrowerController(BorrowerService borrowerService) {
        this.borrowerService = borrowerService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<BorrowerDTO>> createBorrower(@Valid @RequestBody BorrowerDTO borrowerDTO) {
        try {
            log.info("Creating new borrower with email: {}", borrowerDTO.getEmail());
            BorrowerDTO createdBorrower = borrowerService.createBorrower(borrowerDTO);

            ApiResponse<BorrowerDTO> response = ApiResponse.<BorrowerDTO>builder()
                    .success(true)
                    .message("Borrower created successfully")
                    .data(createdBorrower)
                    .timestamp(LocalDateTime.now())
                    .build();

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            log.error("Error creating borrower: {}", e.getMessage(), e);
            ApiResponse<BorrowerDTO> errorResponse = ApiResponse.<BorrowerDTO>builder()
                    .success(false)
                    .message("Failed to create borrower")
                    .timestamp(LocalDateTime.now())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
