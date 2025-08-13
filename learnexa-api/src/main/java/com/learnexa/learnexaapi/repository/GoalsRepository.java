package com.learnexa.learnexaapi.repository;

import com.learnexa.learnexaapi.entity.Goals;
import com.learnexa.learnexaapi.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GoalsRepository extends JpaRepository<Goals, Long> {
    List<Goals> findByUserInfo(UserInfo userInfo);
}
