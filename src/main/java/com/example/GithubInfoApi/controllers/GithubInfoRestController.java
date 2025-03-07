package com.example.GithubInfoApi.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.GithubInfoApi.exceptions.UserNotFoundException;
import com.example.GithubInfoApi.records.Branch;
import com.example.GithubInfoApi.records.CompiledInfo;
import com.example.GithubInfoApi.records.ErrorMessage;
import com.example.GithubInfoApi.records.Repo;
import com.example.GithubInfoApi.records.User;
import com.example.GithubInfoApi.services.GithubInfoService;

import io.smallrye.mutiny.Multi;

@RestController
@RequestMapping("/api/user")
public class GithubInfoRestController {

    @Autowired
    GithubInfoService githubInfoService = new GithubInfoService();

    @GetMapping("/{username}")
    public ResponseEntity<Multi<CompiledInfo>> getUserReposInfo(@PathVariable("username") String username) {
        try {
            List<CompiledInfo> compiledInfos = new ArrayList<>();

            User user = githubInfoService.getUserInfo(username);

            List<Repo> repos = githubInfoService.getUserRepos(user);

            for (Repo repo : repos) {
                List<Branch> branches = githubInfoService.getRepoBranches(repo);
                compiledInfos.add(new CompiledInfo(user.login(), repo.name(), branches));
            }

            return new ResponseEntity<>(Multi.createFrom().iterable(compiledInfos), HttpStatus.OK);
        } catch (Exception e) {
            throw new UserNotFoundException();
        }
    }

    @ExceptionHandler({ UserNotFoundException.class })
    public ResponseEntity<ErrorMessage> handleUserNotFoundException() {
        return new ResponseEntity<>(new ErrorMessage("404 Not Found", "User not found."), HttpStatus.NOT_FOUND);
    }

}
