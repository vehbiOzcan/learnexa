package com.learnexa.learnexaapi.dto;

import com.learnexa.learnexaapi.entity.Badge;
import com.learnexa.learnexaapi.entity.Goals;
import com.learnexa.learnexaapi.entity.Pomodoro;
import com.learnexa.learnexaapi.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DtoUserInfo {
    private Long id;

    private Integer score;

    private Integer rank;

    private Integer star;

    private Integer series;

    private User user;

    private List<Pomodoro> pomodoros;

    private List<Goals> goals;

    private List<Badge> badges;
}
