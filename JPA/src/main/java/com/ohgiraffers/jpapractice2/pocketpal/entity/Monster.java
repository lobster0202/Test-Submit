package com.ohgiraffers.jpapractice2.pocketpal.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tbl_pal")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Monster {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int palNo;
    private String palName;
    private String palType;
    private String palGrade;

    public void modifyMonsterName(String palName) {
        this.palName = palName;
    }
}
