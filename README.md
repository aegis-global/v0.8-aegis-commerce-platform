# v0.8-aegis-commerce-platform
# ![Production-ready App using Spring](example-logo.png)

> ### Micronaut Framework codebase containing real world examples (CRUD, auth, advanced patterns, etc) that adheres to the [RealWorld](https://github.com/gothinkster/realworld) spec and API

[![CI](https://github.com/aegis-global/aegis-commerce-platform/workflows/CI/badge.svg)](https://github.com/aegis-global/aegis-commerce-platform/actions)
[![Codecov](https://img.shields.io/codecov/c/gh/aegis-global/aegis-commerce-platform?logo=codecov)](https://codecov.io/gh/aegis-global/aegis-commerce-platform)

This codebase was created as a fully fledged fullstack application built with
**[Micronaut](https://micronaut.io/)** including CRUD operations, authentication, routing, pagination, and more.

We've gone to great lengths to adhere to the Micronaut community styleguides & best practices.

For more information on how this works with other frontends/backends, head over to the
[RealWorld](https://github.com/gothinkster/realworld) repo.

## Live Demo

Check out the live application on [**Render**](https://render.com/):

| Resource   | URL                                                         |
|------------|-------------------------------------------------------------|
| api        | https://aegis-commerce-platform.onrender.com/api        |
| swagger-ui | https://aegis-commerce-platform.onrender.com/swagger-ui |

💡 The application is deployed on a free tier, so it may take a few seconds to start.

## How it works

### App

- [Micronaut Framework](https://docs.micronaut.io/latest/guide/)
- [Micronaut Data JPA](https://micronaut-projects.github.io/micronaut-data/latest/guide/#hibernate) with Hibernate
- [Micronaut Liquibase](https://micronaut-projects.github.io/micronaut-liquibase/latest/guide/)
  for the database changes management
- [Micronaut Declarative HTTP Clients](https://docs.micronaut.io/latest/guide/#clientAnnotation)
  for integration tests
- [Micronaut Security JWT](https://micronaut-projects.github.io/micronaut-security/latest/guide/#jwt)
  for authentication and authorization
- [Micronaut Management](https://docs.micronaut.io/latest/guide/#management) built-in endpoints
- [Micronaut OpenAPI/Swagger](https://micronaut-projects.github.io/micronaut-openapi/latest/guide/)
- H2 in memory database
- GraalVM support

### CI

- [Github Actions](https://github.com/aegis-global/aegis-commerce-platform/actions)
- Building jar
- Building GraalVM native image for Linux, macOS, and Windows
- Building Docker image with the native executable and pushing it to
  [GitHub Container Registry](https://github.com/aegis-global/aegis-commerce-platform/pkgs/container/aegis-commerce-platform)
- Execution of
  [Realworld Postman collection](https://github.com/gothinkster/realworld/blob/master/api/Conduit.postman_collection.json)
  with newman for all the builds
- Code coverage with [Codecov](https://codecov.io/gh/aegis-global/aegis-commerce-platform)
- Publishing [GitHub release](https://github.com/aegis-global/aegis-commerce-platform/releases) with the artifacts
- Triggering deployment on [Render](https://aegis-commerce-platform.onrender.com/swagger-ui)

## Getting started

Note that Java 17 or above is required to build the project with Gradle.

### JVM

Run the application with Gradle:

    ./gradlew run

Or download the latest artifact from
the [releases](https://github.com/aegis-global/aegis-commerce-platform/releases/latest) page,
and run:

    java -jar aegis-commerce-platform-*.jar

### Native Image

Run the application with Gradle:

    ./gradlew nativeRun

Or download the latest artifact for your platform from
the [releases](https://github.com/aegis-global/aegis-commerce-platform/releases/latest) page,
unpack, and run the executable:

    ./aegis-commerce-platform

### Docker

Run the application with Docker:

    docker run -p 8080:8080 ghcr.io/aegis-global/aegis-commerce-platform:latest

## Try it out with a RealWorld frontend

The entry point address of the backend API is at http://localhost:8080/api

## Run test

The repository contains a lot of test cases to cover both api test and repository test.

    ./gradlew test
# v0.8-aegis-commerce-platform
