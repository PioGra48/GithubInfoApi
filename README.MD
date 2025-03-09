# GitHubInfoAPI
GitHubInfoApi is an API compiling information of users' repositories.
The information includes repository name, user login, repository branches and SHA of the last commit.

## Running requirements
The API requires at least Java 17 and SpringBoot.

## Endpoints
### GET /api/user/{username}
#### Response format
The endpoint responds with the compiled info of user `{username}`'s repositories as JSON list.
```json
{
  "0": {
    "login": "owner_login",
    "repo_name": "repository_name",
    "branches": {
    "0": {
      "name": "branch_name",
      "commit": {
        "sha": "commit_sha"
        }
      }
    }
  }
}
```