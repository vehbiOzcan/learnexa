package com.learnexa.learnexaapi.repository;

import com.learnexa.learnexaapi.entity.Badge;
import com.learnexa.learnexaapi.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BadgeRepository extends JpaRepository<Badge, Long> {
}
