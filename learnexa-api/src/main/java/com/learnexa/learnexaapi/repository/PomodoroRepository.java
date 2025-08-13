package com.learnexa.learnexaapi.repository;

import com.learnexa.learnexaapi.entity.Pomodoro;
import com.learnexa.learnexaapi.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PomodoroRepository extends JpaRepository<Pomodoro, Long> {
    List<Pomodoro> findByUserInfo(UserInfo userInfo);
}
