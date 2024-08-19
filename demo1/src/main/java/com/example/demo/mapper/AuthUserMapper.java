package com.example.demo.mapper;

import com.example.demo.dto.authUser.AuthUserDTO;
import com.example.demo.entity.AuthUser;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AuthUserMapper extends BaseMapper{
    AuthUser fromAuthUserDTO(AuthUserDTO dto, @MappingTarget AuthUser authUser);
}
