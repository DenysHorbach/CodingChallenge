package com.dhorbach.codingchallenge.models;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class Repository {
    String name;
    List<GitHubBranch> branches;
    String ownerLogin;
}
