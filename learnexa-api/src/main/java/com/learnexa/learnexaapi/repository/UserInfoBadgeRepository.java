package com.learnexa.learnexaapi.repository;

import com.learnexa.learnexaapi.entity.UserInfo;
import com.learnexa.learnexaapi.entity.UserInfoBadge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInfoBadgeRepository extends JpaRepository<UserInfoBadge, Long> {
}
