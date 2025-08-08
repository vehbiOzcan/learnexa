package com.learnexa.learnexaapi.entity;

import com.learnexa.learnexaapi.entity.enums.BadgeType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "BADGE")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Badge {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "badge_sequence")
    @SequenceGenerator(name = "badge_sequence", sequenceName = "badge_sequence", allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    private String badge;

    private BadgeType badgeType;

}
