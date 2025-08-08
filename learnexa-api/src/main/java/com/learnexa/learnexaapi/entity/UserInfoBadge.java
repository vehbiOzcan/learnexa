package com.learnexa.learnexaapi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "USER_INFO_BADGE")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoBadge {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userinfo_badge_sequence")
    @SequenceGenerator(name = "userinfo_badge_sequence", sequenceName = "userinfo_badge_sequence", allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_INFO_ID")
    private UserInfo userInfoId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BADGE_ID")
    private Badge badgeId;

}
