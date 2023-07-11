package com.dhorbach.codingchallenge.util;

import com.dhorbach.codingchallenge.models.GitHubBranch;
import com.dhorbach.codingchallenge.models.GitHubRepository;
import com.dhorbach.codingchallenge.models.Repository;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class Constants {

    public static final String USERNAME = "testuser";
    public static final String REPO_1 = "repo1";
    public static final String REPO_2 = "repo2";
    public static final String REPO_3 = "repo3";
    public static final String BRANCH_1 = "branch1";
    public static final String BRANCH_2 = "branch2";
    public static final String COMMIT = "commit sha";
    public static final List<GitHubRepository> GITHUB_REPOSITORIES = List.of(
        new GitHubRepository(REPO_1, false, USERNAME),
        new GitHubRepository(REPO_2, false, USERNAME),
        new GitHubRepository(REPO_3, true, USERNAME)
    );

    public static final List<GitHubBranch> GITHUB_BRANCHES = List.of(
        new GitHubBranch(BRANCH_1, COMMIT),
        new GitHubBranch(BRANCH_2, COMMIT)
    );
    public static final List<Repository> REPOSITORIES = List.of(
        Repository.builder().name(REPO_1).ownerLogin(USERNAME).branches(GITHUB_BRANCHES).build(),
        Repository.builder().name(REPO_2).ownerLogin(USERNAME).branches(GITHUB_BRANCHES).build());
}
