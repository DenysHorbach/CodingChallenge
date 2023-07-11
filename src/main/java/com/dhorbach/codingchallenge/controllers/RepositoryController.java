package com.dhorbach.codingchallenge.controllers;

import api.RepositoryControllerApi;
import com.dhorbach.codingchallenge.models.Repository;
import com.dhorbach.codingchallenge.services.RepositoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import models.RepositoryDTO;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@Slf4j
public class RepositoryController implements RepositoryControllerApi {
    private final RepositoryService repositoryService;
    private final ModelMapper modelMapper;

    @Override
    public Mono<ResponseEntity<Flux<RepositoryDTO>>> getRepositoriesListByUsername(String username, ServerWebExchange exchange) {
        final Flux<Repository> repositories = repositoryService.getRepositories(username);
        return Mono.just(ResponseEntity.ok(repositories.map(repository -> modelMapper.map(repository, RepositoryDTO.class))));
    }

    @GetMapping("/health")
    public Mono<ResponseEntity<Void>> health() {
        log.info("HEALTH CHECK TRIGGERED");
        return Mono.just(ResponseEntity.ok().build());
    }
}
