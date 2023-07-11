package com.dhorbach.codingchallenge.services;

import com.dhorbach.codingchallenge.exceptions.NotFoundException;
import com.dhorbach.codingchallenge.models.GitHubBranch;
import com.dhorbach.codingchallenge.models.GitHubRepository;
import com.dhorbach.codingchallenge.util.FileUtil;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.util.List;

import static com.dhorbach.codingchallenge.util.Constants.BRANCH_1;
import static com.dhorbach.codingchallenge.util.Constants.BRANCH_2;
import static com.dhorbach.codingchallenge.util.Constants.REPO_1;
import static com.dhorbach.codingchallenge.util.Constants.REPO_2;
import static com.dhorbach.codingchallenge.util.Constants.USERNAME;

class GitHubServiceTest {

    private MockWebServer mockWebServer;
    private GitHubService gitHubService;

    @BeforeEach
    void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        final WebClient webClient = WebClient.builder()
            .baseUrl(mockWebServer.url("/").toString())
            .build();

        gitHubService = new GitHubService(webClient);
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void getGithubRepositories_ValidUsername_ReturnsRepositories() throws IOException {
        // Arrange
       final List<GitHubRepository> expectedRepositories = List.of(
            new GitHubRepository(REPO_1, false, USERNAME),
            new GitHubRepository(REPO_2, true, USERNAME)
        );
        final String bodyJson = FileUtil.readFromFileToString("/github-repositories.json");

        mockWebServer.enqueue(new MockResponse()
            .setResponseCode(HttpStatus.OK.value())
            .setHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
            .setBody(bodyJson));

        // Act
        final Flux<GitHubRepository> result = gitHubService.getGithubRepositories(USERNAME);

        // Verify
        StepVerifier.create(result)
            .expectNextSequence(expectedRepositories)
            .verifyComplete();
    }

    @Test
    void getGithubRepositories_UserNotFound_ThrowsNotFoundException() {
        // Arrange
        mockWebServer.enqueue(new MockResponse()
            .setResponseCode(HttpStatus.NOT_FOUND.value()));

        // Act & Verify
        StepVerifier.create(gitHubService.getGithubRepositories(USERNAME))
            .expectError(NotFoundException.class)
            .verify();
    }

    @Test
    void getGitHubBranches_ValidUsernameAndRepository_ReturnsBranches() throws IOException {
        // Arrange
        final String commit = "commit sha";
        final List<GitHubBranch> expectedBranches = List.of(
            new GitHubBranch(BRANCH_1, commit),
            new GitHubBranch(BRANCH_2, commit)
        );

        final String bodyJson = FileUtil.readFromFileToString("/github-branches.json");

        mockWebServer.enqueue(new MockResponse()
            .setResponseCode(HttpStatus.OK.value())
            .setHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
            .setBody(bodyJson));

        // Act
        final Flux<GitHubBranch> result = gitHubService.getGitHubBranches(USERNAME, REPO_1);

        // Verify
        StepVerifier.create(result)
            .expectNextSequence(expectedBranches)
            .verifyComplete();
    }

    @Test
    void getGitHubBranches_UserOrRepositoryNotFound_ThrowsNotFoundException() {
        // Arrange
        mockWebServer.enqueue(new MockResponse()
            .setResponseCode(HttpStatus.NOT_FOUND.value()));

        // Act & Verify
        StepVerifier.create(gitHubService.getGitHubBranches(USERNAME, REPO_1))
            .expectError(NotFoundException.class)
            .verify();
    }
}
