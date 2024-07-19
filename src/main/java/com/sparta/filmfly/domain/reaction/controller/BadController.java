package com.sparta.filmfly.domain.reaction.controller;

import com.sparta.filmfly.domain.reaction.service.BadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class BadController {

    private final BadService badService;
}