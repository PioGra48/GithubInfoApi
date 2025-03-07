package com.example.GithubInfoApi.records;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Repo(String name, String branches_url) { }
