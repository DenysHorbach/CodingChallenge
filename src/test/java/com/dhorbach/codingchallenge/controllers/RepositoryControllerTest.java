package com.dhorbach.codingchallenge.controllers;

import com.dhorbach.codingchallenge.exceptions.NotFoundException;
import com.dhorbach.codingchallenge.models.Repository;
import com.dhorbach.codingchallenge.services.RepositoryService;
import models.FailureResponse;
import models.RepositoryDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.Collectors;

import static com.dhorbach.codingchallenge.util.Constants.REPOSITORIES;
import static com.dhorbach.codingchallenge.util.Constants.USERNAME;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@WebFluxTest
class RepositoryControllerTest {

    @Autowired
    private WebTestClient client;

    @MockBean
    private RepositoryService repositoryService;

    @SpyBean
    private ModelMapper modelMapper;

    private static final String API_URL = "/api/v1/repositories/{username}";

    @Test
    void getRepositoriesListByUsername_ValidUsername_ReturnsRepositories() {
        // Arrange
        final Flux<Repository> repositories = Flux.fromIterable(REPOSITORIES);
        final List<RepositoryDTO> expectedResponse =
            REPOSITORIES.stream()
                .map(repository -> modelMapper.map(repository, RepositoryDTO.class))
                .collect(Collectors.toList());

        when(repositoryService.getRepositories(USERNAME)).thenReturn(repositories);

        // Act & Verify
        client.get()
            .uri(API_URL, USERNAME)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBodyList(RepositoryDTO.class)
            .isEqualTo(expectedResponse);
    }

    @Test
    void getRepositoriesListByUsername_InvalidUsername_ReturnsNotFound() {
        // Arrange
        final String errorMessage = "User with username testuser not found!";
        when(repositoryService.getRepositories(USERNAME)).thenThrow(new NotFoundException(errorMessage));
        final FailureResponse failureResponse = new FailureResponse().status(404).message(errorMessage);
        
        // Act & Verify
        client.get()
            .uri(API_URL, USERNAME)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isNotFound()
            .expectBody(FailureResponse.class)
            .isEqualTo(failureResponse);
    }

    @Test
    void getRepositoriesListByUsername_InvalidAcceptHeader_ReturnsNotAcceptable() {
        //Arrange
        final int statusCode = 406;
        final FailureResponse failureResponse =
            new FailureResponse().status(statusCode)
                .message("Could not find acceptable representation");

        // Act & Verify
        client.get()
            .uri(API_URL, USERNAME)
            .accept(MediaType.APPLICATION_XML)
            .exchange()
            .expectStatus().isEqualTo(HttpStatusCode.valueOf(statusCode))
            .expectBody(FailureResponse.class)
            .isEqualTo(failureResponse);
    }
}
