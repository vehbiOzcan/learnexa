package com.learnexa.learnexaapi.entity;

import com.learnexa.learnexaapi.entity.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import jakarta.persistence.*;

import java.util.List;


@Entity
@Table(name = "USER_INFO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_info_seq")
    @SequenceGenerator(name = "user_info_seq", sequenceName = "user_info_seq", allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    @Column(name = "SCORE")
    private Integer score;

    @Column(name = "RANK")
    private Integer rank;

    @Column(name = "STAR")
    private Integer star;

    @Column(name = "SERIES")
    private Integer series;

    @OneToOne
    @JoinColumn(name = "USER_ID", referencedColumnName = "ID")
    private User user;

    @OneToMany(mappedBy = "userInfo", fetch = FetchType.LAZY)
    private List<Pomodoro> pomodoros;

    @OneToMany(mappedBy = "userInfo", fetch = FetchType.LAZY)
    private List<Goals> goals;

    @OneToMany(mappedBy = "userInfo", fetch = FetchType.LAZY)
    private List<Badge> badges;

}
