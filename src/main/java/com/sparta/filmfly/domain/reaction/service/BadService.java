package com.sparta.filmfly.domain.reaction.service;

import com.sparta.filmfly.domain.reaction.repository.BadRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BadService {

    private final BadRepository badRepository;
}