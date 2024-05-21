package com.ohgiraffers.jpapractice2.pocketpal.controller;

import com.ohgiraffers.jpapractice2.common.Pagenation;
import com.ohgiraffers.jpapractice2.common.PagingButton;
import com.ohgiraffers.jpapractice2.pocketpal.dto.GradeDTO;
import com.ohgiraffers.jpapractice2.pocketpal.dto.MonsterDTO;
import com.ohgiraffers.jpapractice2.pocketpal.service.PocketPalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/monster")
@RequiredArgsConstructor
public class PocketPalController {

    private final PocketPalService pocketPalService;

    @GetMapping("/{palNo}")
    public String findPalByPalNo(@PathVariable int palNo, Model model) {
        MonsterDTO resultMonster = pocketPalService.findPalByPalNo(palNo);

        model.addAttribute("monster", resultMonster);

        return "monster/detail";
    }

    @GetMapping("/list")
    public String findMonsterList(Model model, @PageableDefault Pageable pageable) {

        Page<MonsterDTO> monsterList = pocketPalService.findMonsterList(pageable);

        PagingButton paging = Pagenation.getPagingButtonInfo(monsterList);

        model.addAttribute("monsterList", monsterList);
        model.addAttribute("paging", paging);

        return "monster/list";
    }

    @GetMapping("querymethod")
    public void querymethodPage() {

    }

    @GetMapping("/search")
    public String findByPalNo(@RequestParam Integer palNo, Model model) {

        List<MonsterDTO> monsterList = pocketPalService.findPalNo(palNo);

        model.addAttribute("monsterList", monsterList);

        return "monster/searchResult";
    }

    @GetMapping("/regist")
    public void registPage() {

    }

    @GetMapping("/grade")
    @ResponseBody
    public List<GradeDTO> findGradeList() {
        return pocketPalService.findAllGrade();
    }

    @PostMapping("/regist")
    public String registMenu(@ModelAttribute MonsterDTO monsterDTO) {

        pocketPalService.registMonster(monsterDTO);

        return "redirect:/monster/list";

    }

    @GetMapping("/modify")
    public void modifyPage() {

    }

    @PostMapping("/modify")
    public String modifyMenu(@ModelAttribute MonsterDTO monsterDTO, Model model) {

        pocketPalService.modifyMonster(monsterDTO);

        return "redirect:/monster/" + monsterDTO.getPalNo();
    }

    @GetMapping("/delete")
    public void deletePage() {

    }

    @PostMapping("/delete")
    public String deleteMonster(@RequestParam Integer palNo) {

        pocketPalService.deleteMonster(palNo);

        return "redirect:/monster/list";
    }
}
