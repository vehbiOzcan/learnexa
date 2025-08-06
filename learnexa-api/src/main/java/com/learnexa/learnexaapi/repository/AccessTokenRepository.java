package com.learnexa.learnexaapi.repository;

import com.learnexa.learnexaapi.entity.AccessToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import java.util.Optional;

@Repository
public interface AccessTokenRepository extends JpaRepository<AccessToken, Long> {

    Optional<AccessToken> findByAccessToken(String token);

    @Modifying
    @Transactional
    @Query("UPDATE AccessToken at SET at.isActive = false WHERE at.user.id = :userId AND at.isActive = true")
    void updateAccessToken(Long userId);


}
