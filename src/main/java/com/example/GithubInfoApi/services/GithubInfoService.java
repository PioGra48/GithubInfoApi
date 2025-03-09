package com.example.GithubInfoApi.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.example.GithubInfoApi.exceptions.UserNotFoundException;
import com.example.GithubInfoApi.records.Branch;
import com.example.GithubInfoApi.records.CompiledRepoInfo;
import com.example.GithubInfoApi.records.Repo;
import com.example.GithubInfoApi.records.User;

import io.smallrye.mutiny.Multi;

@Service
public class GithubInfoService {
    private String apiUrl = "https://api.github.com/users/";
    private RestTemplate restTemplate = new RestTemplate();

    public User getUserInfo(String username) {
        try {
            return restTemplate.getForObject(apiUrl + username, User.class);
        } catch (RestClientException e) {
            throw e;
        }
    }

    public List<Repo> getUserRepos(User user) {
        ResponseEntity<List<Repo>> reposResponse = 
            restTemplate.exchange(user.repos_url(), HttpMethod.GET, null, new ParameterizedTypeReference<List<Repo>>() {});
        return reposResponse.getBody();
    }

    public List<Branch> getRepoBranches(Repo repo) {
        // Cut off {/branches} path variable from the url
        String branchesUrl = repo.branches_url().split("\\{")[0];
        ResponseEntity<List<Branch>> branchesResponse = 
            restTemplate.exchange(branchesUrl, HttpMethod.GET, null, new ParameterizedTypeReference<List<Branch>>() {});
        return branchesResponse.getBody();
    }

    public Multi<CompiledRepoInfo> getCompiledInfo(String username) {
        try {
            List<CompiledRepoInfo> compiledReposInfo = new ArrayList<>();

            User user = this.getUserInfo(username);

            List<Repo> repos = this.getUserRepos(user);

            for (Repo repo : repos) {
                List<Branch> branches = this.getRepoBranches(repo);
                compiledReposInfo.add(new CompiledRepoInfo(user.login(), repo.name(), branches));
            }

            return Multi.createFrom().iterable(compiledReposInfo);
        } catch (RestClientException e) {
            throw new UserNotFoundException();
        }
    } 

}
