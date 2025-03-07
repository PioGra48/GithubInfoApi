package com.example.GithubInfoApi.services;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.GithubInfoApi.records.Branch;
import com.example.GithubInfoApi.records.Repo;
import com.example.GithubInfoApi.records.User;

@Service
public class GithubInfoService {
    private String apiUrl = "https://api.github.com/users/";
    private RestTemplate restTemplate = new RestTemplate();

    public User getUserInfo(String username) {
        try {
            return restTemplate.getForObject(apiUrl + username, User.class);
        } catch (Exception e) {
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

}
