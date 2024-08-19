package com.example.demo.mapper;

import com.example.demo.dto.card.CardDTO;
import com.example.demo.dto.transaction.TransactionDTO;
import com.example.demo.entity.Card;
import com.example.demo.entity.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TransactionMapper extends BaseMapper{
    TransactionDTO toDto(Transaction transaction);
}
