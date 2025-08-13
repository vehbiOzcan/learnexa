package com.learnexa.learnexaapi.repository;

import com.learnexa.learnexaapi.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {

    /*@Query("""
        SELECT ui FROM UserInfo ui
        LEFT JOIN FETCH ui.pomodoros
        LEFT JOIN FETCH ui.goals
        LEFT JOIN FETCH ui.uiBadges uib
        LEFT JOIN FETCH uib.badge
        LEFT JOIN FETCH ui.user u
        LEFT JOIN FETCH u.lessons
        WHERE u.id = :userId
    """)
    Optional<UserInfo> findUserInfoByUserId(Long userId);*/

    Optional<UserInfo> findUserInfoByUserId(Long userId);

}
