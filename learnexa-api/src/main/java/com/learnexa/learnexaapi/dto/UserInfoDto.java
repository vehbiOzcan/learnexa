package com.learnexa.learnexaapi.dto;

import com.learnexa.learnexaapi.entity.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDto {
    private Long id;

    private Integer score;

    private Integer rank;

    private Integer star;

    private Integer series;

    //private User user;

    private List<Pomodoro> pomodoros;

    private List<Goals> goals;

    private List<Badge> badges;

    private List<Lesson> lessons;
}
