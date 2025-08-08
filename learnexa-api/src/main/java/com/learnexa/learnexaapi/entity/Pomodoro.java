package com.learnexa.learnexaapi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "POMODORO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pomodoro {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pomodoro_sequence")
    @SequenceGenerator(name = "pomodoro_sequence", sequenceName = "pomodoro_sequence", allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "TIME")
    private Integer time;

    @Column(name = "POMODORO_DATE")
    private LocalDateTime pomodoroDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_INFO_ID")
    private UserInfo userInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LESSON_ID")
    private Lesson lesson;
}
