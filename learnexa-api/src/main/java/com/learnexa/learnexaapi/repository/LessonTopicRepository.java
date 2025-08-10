package com.learnexa.learnexaapi.repository;

import com.learnexa.learnexaapi.entity.LessonTopic;
import com.learnexa.learnexaapi.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LessonTopicRepository extends JpaRepository<LessonTopic, Long> {
}
