package com.example.demo.repository;

import com.example.demo.entity.AuthUser;
import com.example.demo.repository.base.BaseRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthUserRepository extends JpaRepository<AuthUser, Long> , BaseRepository {

    Optional<AuthUser> findByUsername(String username);

    Optional<AuthUser> findByUsernameAndIsActive(String username, Boolean isActive);
}
