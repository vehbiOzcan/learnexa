package com.learnexa.learnexaapi.repository;

import com.learnexa.learnexaapi.entity.Pomodoro;
import com.learnexa.learnexaapi.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PomodoroRepository extends JpaRepository<Pomodoro, Long> {
}
