package com.example.LMS.dto;

import com.example.LMS.model.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BorrowTransactionDTO {

    private UUID id;
    private UUID bookId;
    private String bookTitle;
    private String bookIsbn;
    private Long borrowerId;
    private String borrowerName;
    private String borrowerEmail;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private TransactionStatus status;
    private BigDecimal fineAmount;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Helper methods
    public boolean isOverdue() {
        return status == TransactionStatus.ACTIVE &&
                dueDate != null &&
                dueDate.isBefore(LocalDate.now());
    }

    public long getDaysOverdue() {
        if (!isOverdue()) {
            return 0;
        }
        return ChronoUnit.DAYS.between(dueDate, LocalDate.now());
    }
}
