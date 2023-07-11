package com.dhorbach.codingchallenge.services;

import com.dhorbach.codingchallenge.exceptions.NotFoundException;
import com.dhorbach.codingchallenge.models.GitHubBranch;
import com.dhorbach.codingchallenge.models.GitHubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service for fetching data from GitHub APIs.
 */
@Service
@RequiredArgsConstructor
public class GitHubService {

    private final WebClient webClient;

    /**
     * Retrieves GitHub repositories for a given username.
     *
     * @param username the username
     * @return a flux of GitHub repositories
     * @throws NotFoundException if the user with the given username is not found
     */
    public Flux<GitHubRepository> getGithubRepositories(final String username) {
        return webClient.get()
            .uri("/users/{username}/repos", username)
            .retrieve()
            .onStatus(
                HttpStatus.NOT_FOUND::equals,
                clientResponse -> Mono.error(
                    new NotFoundException(String.format("User with username %s not found!", username))
                )
            )
            .bodyToFlux(GitHubRepository.class);
    }

    /**
     * Retrieves GitHub branches for a given username and repository name.
     *
     * @param username        the username
     * @param repositoryName  the repository name
     * @return a flux of GitHub branches
     * @throws NotFoundException if the user or repository with the given username and repository name is not found
     */
    public Flux<GitHubBranch> getGitHubBranches(final String username, final String repositoryName) {
        return webClient.get()
            .uri("/repos/{username}/{repositoryName}/branches", username, repositoryName)
            .retrieve()
            .onStatus(
                HttpStatus.NOT_FOUND::equals,
                clientResponse -> Mono.error(
                    new NotFoundException(
                        String.format("Branch for username %s and repository name %s not found!", username, repositoryName)
                    )
                )
            )
            .bodyToFlux(GitHubBranch.class);
    }
}
