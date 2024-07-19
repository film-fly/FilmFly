package com.sparta.filmfly.domain.reaction.service;

import com.sparta.filmfly.domain.reaction.repository.GoodRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoodService {

    private final GoodRepository goodRepository;
}