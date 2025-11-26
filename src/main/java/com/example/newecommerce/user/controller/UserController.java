package com.example.newecommerce.user.controller;

import com.example.newecommerce.user.application.UserService;
import com.example.newecommerce.user.dto.PointHistoryResponse;
import com.example.newecommerce.user.dto.PointResponse;
import com.example.newecommerce.user.dto.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "유저", description = "사용자 관련 기능")
@RestController
@RequestMapping(name = "/point")
public class UserController {

    public final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    //포인트 충전
    @Operation(summary = "유저/포인트 충전", description = "유저의 포인트를 충전합니다")
    @PatchMapping("/{id}/charge")
    public PointResponse charge(@PathVariable("id") Long userId, @RequestBody int point) {


        return userService.chargePoint(userId ,point);

    };



    //포인트 조회
    @Operation(summary = "유저/포인트 조회", description = "유저의 포인트를 조회합니다")
    @GetMapping("/{id}/inquiry")
    public UserResponse pointInquiry(@PathVariable("id") Long userId) {


        return userService.PointInquiry(userId);
    };


    //포인트 내역 조회
    @Operation(summary = "유저/포인트 내역 조회", description = "포인트 내역을 조회합니다")
    @GetMapping("/{id}/histories")
    public List<PointHistoryResponse> historiesInquiry(@PathVariable("id") Long userId) {


        return userService.PointHistoryInquiry(userId);
    };





}
