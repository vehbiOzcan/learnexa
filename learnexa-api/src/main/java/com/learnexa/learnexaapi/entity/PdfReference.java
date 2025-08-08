package com.learnexa.learnexaapi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "PDF_REFERENCE")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PdfReference {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pdf_reference_sequence")
    @SequenceGenerator(name = "pdf_reference_sequence", sequenceName = "pdf_reference_sequence", allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "PATH")
    private String path;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LESSON_TOPIC_ID")
    private LessonTopic lessonTopic;


}
