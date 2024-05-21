package com.ohgiraffers.jpapractice2.pocketpal.repository;

import com.ohgiraffers.jpapractice2.pocketpal.entity.Monster;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MonsterRepository extends JpaRepository<Monster, Integer> {

    /* 3. 파라미터로 전달 받은 번호를 초과하는 팰 목록을 전달 받은 정렬 기준으로 조회 */
    List<Monster> findByPalNoGreaterThan(Integer palNo, Sort sort);
}
