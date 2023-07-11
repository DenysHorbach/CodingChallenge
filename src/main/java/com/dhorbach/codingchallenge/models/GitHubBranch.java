package com.dhorbach.codingchallenge.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GitHubBranch {
    private String name;
    private String lastCommitSha;
    @JsonProperty("commit")
    private void setLastCommitSha(final Map<String, String> owner) {
        lastCommitSha = owner.get("sha");
    }
}
