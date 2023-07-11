package com.dhorbach.codingchallenge.services;

import com.dhorbach.codingchallenge.models.GitHubBranch;
import com.dhorbach.codingchallenge.models.GitHubRepository;
import com.dhorbach.codingchallenge.models.Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

/**
 * Service for retrieving repositories.
 */
@Service
@RequiredArgsConstructor
public class RepositoryService {

    private final GitHubService gitHubService;

    /**
     * Retrieves repositories and its corresponding branches for a given username.
     *
     * @param username the username
     * @return a flux of repositories
     */
    public Flux<Repository> getRepositories(final String username) {
        return gitHubService.getGithubRepositories(username)
            .filter(repository -> !repository.isFork())
            .flatMap(gitHubRepository ->
                gitHubService.getGitHubBranches(username, gitHubRepository.getName())
                    .collectList()
                    .onErrorResume(throwable -> Mono.just(Collections.emptyList()))
                    .map(branches -> buildRepository(gitHubRepository, branches)));
    }

    /**
     * Builds a Repository object using the given {@link GitHubRepository} and list of {@link GitHubBranch}.
     *
     * @param gitHubRepository the {@link GitHubRepository} object
     * @param gitHubBranches   the list of {@link GitHubBranch} objects
     * @return a Repository object
     */
    private Repository buildRepository(
        final GitHubRepository gitHubRepository,
        final List<GitHubBranch> gitHubBranches
    ) {
        return Repository.builder()
            .name(gitHubRepository.getName())
            .ownerLogin(gitHubRepository.getOwnerLogin())
            .branches(gitHubBranches)
            .build();
    }
}
