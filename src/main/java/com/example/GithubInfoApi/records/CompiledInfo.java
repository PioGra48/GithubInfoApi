package com.example.GithubInfoApi.records;

import java.util.List;

public record CompiledInfo(
    String login,
    String repo_name,
    List<Branch> branches
) { }
