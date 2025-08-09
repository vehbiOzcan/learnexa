package com.learnexa.learnexaapi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "LESSON_TOPIC")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LessonTopic {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "lesson_topic_seq")
    @SequenceGenerator(name = "lesson_topic_seq", sequenceName = "lesson_topic_seq", allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "CONTENT")
    private String content;

    @Column(name = "PRIORITY")
    private Integer priority;

    @Column(name = "LT_ORDER")
    private Integer lessonTopicOrder;

    @Column(name = "COMPLETED")
    private Boolean comleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LESSON_ID")
    private Lesson lesson;

    @OneToMany(mappedBy = "lessonTopic", fetch = FetchType.LAZY)
    private List<PdfReference> pdfReferences;
}
