package com.example.GithubInfoApi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.GithubInfoApi.exceptions.UserNotFoundException;
import com.example.GithubInfoApi.records.CompiledInfo;
import com.example.GithubInfoApi.records.ErrorMessage;
import com.example.GithubInfoApi.services.GithubInfoService;

import io.smallrye.mutiny.Multi;

@RestController
@RequestMapping("/api/user")
public class GithubInfoRestController {

    @Autowired
    GithubInfoService githubInfoService = new GithubInfoService();

    @GetMapping("/{username}")
    public ResponseEntity<Multi<CompiledInfo>> getUserReposInfo(@PathVariable("username") String username) {
        return new ResponseEntity<>(githubInfoService.getCompiledInfo(username), HttpStatus.OK);
    }

    @ExceptionHandler({ UserNotFoundException.class })
    public ResponseEntity<ErrorMessage> handleUserNotFoundException() {
        return new ResponseEntity<>(new ErrorMessage("404 Not Found", "User not found."), HttpStatus.NOT_FOUND);
    }

}
