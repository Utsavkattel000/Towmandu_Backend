package com.tow.mandu.repository;

import com.tow.mandu.config.firebase.FCMToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface FCMTokenRepository extends JpaRepository<FCMToken, Integer> {

    Optional<List<FCMToken>> findByUserId(Long userId);

    FCMToken findByFcmToken(String token);



    @Query("SELECT f FROM FCMToken f WHERE f.userId IN :userIds")
    List<FCMToken> findByUserIds(@Param("userIds") List<Long> userIds);

    @Transactional
    @Modifying
    @Query("DELETE FROM FCMToken f WHERE f.userId = :userId")
    void deleteByUserId(@Param("userId") Long userId);



}

