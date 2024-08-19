package com.example.demo.mapper;

import com.example.demo.dto.card.CardDTO;
import com.example.demo.entity.Card;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CardMapper extends BaseMapper{
    @Mapping(source = "id", target = "cardId")
    @Mapping(source = "initialAmount", target = "balance")
    @Mapping(source = "user.id", target = "userid")
    CardDTO toDTO(Card card1);
}
