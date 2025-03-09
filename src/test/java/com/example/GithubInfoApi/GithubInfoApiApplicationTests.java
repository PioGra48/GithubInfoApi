package com.example.GithubInfoApi;

import java.util.List;

import com.example.GithubInfoApi.services.GithubInfoService;
import io.smallrye.mutiny.helpers.test.AssertSubscriber;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.example.GithubInfoApi.controllers.GithubInfoRestController;
import com.example.GithubInfoApi.records.Branch;
import com.example.GithubInfoApi.records.Commit;
import com.example.GithubInfoApi.records.CompiledRepoInfo;

import io.smallrye.mutiny.Multi;

@WebFluxTest(controllers = GithubInfoRestController.class)
@ContextConfiguration(classes = {GithubInfoService.class, GithubInfoRestController.class})
class GithubInfoRestControllerTest {

    @Autowired
    private WebTestClient client;

    @Test
    public void testGetCompiledInfoEndpoint() {

        // given
        List<CompiledRepoInfo> compiledRepoInfoList = List.of(
                new CompiledRepoInfo(
                        "PioGra48",
                        "git-project",
                        List.of(
                                new Branch(
                                        "master",
                                        new Commit(
                                                "ec81f7e6cc6d1e5854c3dd8ef3b939f08f3eea45"
                                        ))
                        )
                ),
                new CompiledRepoInfo(
                        "PioGra48",
                        "GithubInfoApi",
                        List.of(
                                new Branch(
                                        "main",
                                        new Commit(
                                                "d092c9d12bff720fe2719f2a4340b96a83041cd8"
                                        ))
                        )
                ),
                new CompiledRepoInfo(
                        "PioGra48",
                        "public-PioGra48",
                        List.of()
                ));

        Multi<CompiledRepoInfo> testInfoMulti = Multi.createFrom().iterable(compiledRepoInfoList);

        AssertSubscriber<CompiledRepoInfo> assertSubscriber =
                testInfoMulti.subscribe().withSubscriber(AssertSubscriber.create(3));

        // when
        client
            .get().uri("/api/user/PioGra48")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectBody(CompiledRepoInfo[].class)
            .value(compiledRepoInfo -> {
                assertSubscriber.assertItems(compiledRepoInfo);
            });


    }

}
