package com.example.demo.repository;

import com.example.demo.entity.AuthUser;
import com.example.demo.entity.IdempotencyKey;
import com.example.demo.repository.base.BaseRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IdempotencyKeyRepository extends JpaRepository<IdempotencyKey, String> , BaseRepository {

    Optional<IdempotencyKey> findByKey(String key);
}
