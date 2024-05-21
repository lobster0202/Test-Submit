package com.ohgiraffers.jpapractice2.pocketpal.service;

import com.ohgiraffers.jpapractice2.pocketpal.dto.GradeDTO;
import com.ohgiraffers.jpapractice2.pocketpal.dto.MonsterDTO;
import com.ohgiraffers.jpapractice2.pocketpal.entity.Grade;
import com.ohgiraffers.jpapractice2.pocketpal.entity.Monster;
import com.ohgiraffers.jpapractice2.pocketpal.repository.GradeRepository;
import com.ohgiraffers.jpapractice2.pocketpal.repository.MonsterRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PocketPalService {
    private final MonsterRepository monsterRepository;

    private final ModelMapper modelMapper;

    private final GradeRepository gradeRepository;

    /* 1. findByNo */
    public MonsterDTO findPalByPalNo(int palNo) {

        Monster foundMonster = monsterRepository.findById(palNo).orElseThrow(IllegalArgumentException::new);

        return modelMapper.map(foundMonster, MonsterDTO.class);

    }

    /* 2. findAll : Sort */
    public List<MonsterDTO> findMonsterList() {

        List<Monster> monsterList = monsterRepository.findAll(Sort.by("palNo").descending());

        return monsterList.stream()
                .map(monster -> modelMapper.map(monster, MonsterDTO.class))
                .toList();

    }

    /* 3. finAll : Pageable */
    public Page<MonsterDTO> findMonsterList(Pageable pageable) {
        pageable = PageRequest.of(
                pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1,
                pageable.getPageSize(),
                Sort.by("palNo").descending()
        );

        Page<Monster> monsterList = monsterRepository.findAll(pageable);

        return monsterList.map(monster -> modelMapper.map(monster, MonsterDTO.class));

    }

    /* 4. Query Method */
    public List<MonsterDTO> findPalNo(Integer palNo) {

        List<Monster> monsterList = monsterRepository.findByPalNoGreaterThan(
                palNo,
                Sort.by("palNo").descending()
        );

        return monsterList.stream()
                .map(monster -> modelMapper.map(monster, MonsterDTO.class))
                .toList();
    }

    /* JPQL or Native Query */
    public List<GradeDTO> findAllGrade() {

        List<Grade> gradeList = gradeRepository.findAllGrade();

        System.out.println("@@@@@@@@@@@@");
        return gradeList.stream()
                .map(grade -> modelMapper.map(grade, GradeDTO.class))
                .toList();
    }

    /* 6. save(insert)*/
    @Transactional
    public void registMonster(MonsterDTO monsterDTO) {

        monsterRepository.save(modelMapper.map(monsterDTO, Monster.class));

    }

    /* 7. modify(update)*/
    @Transactional
    public void modifyMonster(MonsterDTO monsterDTO) {

        Monster foundMonster = monsterRepository.findById(monsterDTO.getPalNo())
                .orElseThrow(IllegalArgumentException::new);

        foundMonster.modifyMonsterName(monsterDTO.getPalName());

    }

    /* 8. deleteById */
    @Transactional
    public void deleteMonster(Integer palNo) {
        monsterRepository.deleteById(palNo);
    }
}
