package com.learnexa.learnexaapi.repository;

import com.learnexa.learnexaapi.entity.Lesson;
import com.learnexa.learnexaapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LessonRepository extends JpaRepository<Lesson, Long> {

    List<Lesson> findByUserId(Long userİd);
}
