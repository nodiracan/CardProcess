package com.example.demo.service.base;

import com.example.demo.dto.card.CardDTO;
import com.example.demo.mapper.BaseMapper;
import com.example.demo.repository.base.BaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
public class AbstractService<R extends BaseRepository, M extends BaseMapper> {
    protected final R repository;
    protected final M mapper;


}
