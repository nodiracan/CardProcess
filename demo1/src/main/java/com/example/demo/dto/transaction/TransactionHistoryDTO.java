package com.example.demo.dto.transaction;

import com.example.demo.enums.Currency;

import java.util.List;

public record TransactionHistoryDTO(int page, int size, int totalPages, long totalItems, List<TransactionDTO> content) {
}
