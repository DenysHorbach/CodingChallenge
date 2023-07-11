package com.dhorbach.codingchallenge.services;

import com.dhorbach.codingchallenge.exceptions.NotFoundException;
import com.dhorbach.codingchallenge.models.Repository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Collections;

import static com.dhorbach.codingchallenge.util.Constants.GITHUB_BRANCHES;
import static com.dhorbach.codingchallenge.util.Constants.GITHUB_REPOSITORIES;
import static com.dhorbach.codingchallenge.util.Constants.REPOSITORIES;
import static com.dhorbach.codingchallenge.util.Constants.REPO_1;
import static com.dhorbach.codingchallenge.util.Constants.REPO_2;
import static com.dhorbach.codingchallenge.util.Constants.USERNAME;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RepositoryServiceTest {

    @Mock
    private GitHubService gitHubService;
    @InjectMocks
    private RepositoryService repositoryService;

    @Test
    void getRepositories_ValidUsername_ReturnsRepositories() {
        // Arrange

        when(gitHubService.getGithubRepositories(USERNAME)).thenReturn(Flux.fromIterable(GITHUB_REPOSITORIES));
        when(gitHubService.getGitHubBranches(USERNAME, REPO_1)).thenReturn(Flux.fromIterable(GITHUB_BRANCHES));
        when(gitHubService.getGitHubBranches(USERNAME, REPO_2)).thenReturn(Flux.fromIterable(GITHUB_BRANCHES));

        // Act
        Flux<Repository> result = repositoryService.getRepositories(USERNAME);

        // Verify
        StepVerifier.create(result)
            .expectNextSequence(REPOSITORIES)
            .verifyComplete();

        verify(gitHubService, times(1)).getGithubRepositories(USERNAME);
        verify(gitHubService, times(2)).getGitHubBranches(eq(USERNAME), anyString());
    }

    @Test
    void getRepositories_ValidUsername_NoRepositories_ReturnsEmpty() {
        // Arrange
        when(gitHubService.getGithubRepositories(USERNAME)).thenReturn(Flux.fromIterable(Collections.emptyList()));

        // Act
        final Flux<Repository> result = repositoryService.getRepositories(USERNAME);

        // Verify
        StepVerifier.create(result)
            .expectNextCount(0)
            .verifyComplete();

        verify(gitHubService, times(1)).getGithubRepositories(USERNAME);
        verify(gitHubService, never()).getGitHubBranches(anyString(), anyString());
    }

    @Test
    void getRepositories_ValidUsername_ErrorFetchingBranchesReturnsRepositoriesWithoutBranches() {
        // Arrange
        when(gitHubService.getGithubRepositories(USERNAME)).thenReturn(Flux.fromIterable(GITHUB_REPOSITORIES));
        when(gitHubService.getGitHubBranches(eq(USERNAME), anyString()))
            .thenReturn(Flux.error(new NotFoundException("Not found")));

        // Act
        final Flux<Repository> result = repositoryService.getRepositories(USERNAME);

        // Verify
        StepVerifier.create(result)
            .expectNextCount(2)
            .verifyComplete();

        verify(gitHubService, times(1)).getGithubRepositories(USERNAME);
        verify(gitHubService, times(2)).getGitHubBranches(eq(USERNAME), anyString());
    }
}

