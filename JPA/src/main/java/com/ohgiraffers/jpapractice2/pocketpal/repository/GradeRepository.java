package com.ohgiraffers.jpapractice2.pocketpal.repository;

import com.ohgiraffers.jpapractice2.pocketpal.entity.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GradeRepository  extends JpaRepository<Grade, Integer> {

    @Query("SELECT g FROM Grade g ORDER BY g.palGrade")
    List<Grade> findAllGrade();



}
