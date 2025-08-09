package com.learnexa.learnexaapi.entity;

import com.learnexa.learnexaapi.entity.enums.BadgeType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "BADGE")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Goals {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "goals_sequence")
    @SequenceGenerator(name = "goals_sequence", sequenceName = "goals_sequence", allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    @Column(name = "COMLETED_LESSON_GOALS")
    private Integer completedLessonsGoal;

    @Column(name = "COMPLETE_LESSONS")
    private Integer completedLessons;

    @Column(name = "START_DATE")
    private LocalDate startDate;

    @Column(name = "FINISH_DATE")
    private LocalDate finishDate;

    @Column(name = "COMPLETED")
    private Boolean completed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_INFO_ID")
    private UserInfo userInfo;

}

