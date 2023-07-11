package com.dhorbach.codingchallenge.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GitHubRepository {
    private String name;
    private boolean fork;
    private String ownerLogin;
    @JsonProperty("owner")
    private void setOwnerLogin(final Map<String, String> owner) {
        ownerLogin = owner.get("login");
    }
}
