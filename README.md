# TED

This project has been created as an assessment for iO Digital. Below are steps to run the project.

## Client description
The imagined client for this project is a startup that wants to launch their MVP yesterday. Getting it live is the priority.
To accommodate this, the project has been setup without a Postgres database. Directly interacting with the CSV file, abusing it as an MVP database.
This is not necessarily a good practice, but it was a fun approach and allows for more to talk about during the interview.

## Plugins

These are the Ktor plugins I used for this project.

| Name                                                                   | Description                                                                                                                                                                                                                     |
| ------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [Routing](https://start.ktor.io/p/routing)                             | Provides a structured routing DSL. This allows me to have an entry to the application functionality, by API route and method.                                                                                                   |
| [Authentication](https://start.ktor.io/p/auth)                         | Provides extension point for handling the Authorization header. This is added to connect with the API from the frontend using an API key as authentication                                                                      |
| [Compression](https://start.ktor.io/p/compression)                     | Compresses responses using encoding algorithms like GZIP. Probably not needed for this project, but a good setup to allow the project to grow to a size where it is usefull, without having to change it further down the line. |
| [Content Negotiation](https://start.ktor.io/p/content-negotiation)     | Provides automatic content conversion according to Content-Type and Accept headers. Allows to send and accept JSON, the standard protocol for communication.                                                                    |
| [kotlinx.serialization](https://start.ktor.io/p/kotlinx-serialization) | Handles JSON serialization using kotlinx.serialization library. Allows very easy serialization with Data classes                                                                                                                |
| [OpenAPI](https://start.ktor.io/p/openapi)                             | Serves OpenAPI documentation. A nice interactive documentation page, available at `/openapi`                                                                                                                                    |


## Building & Running

To build or run the project, use one of the following tasks:

| Task                  | Description                                                          |
|-----------------------|---------------------------------------------------------------------- |
| `./gradlew test`      | Run the tests                                                        |
| `./gradlew run`       | Run the server                                                       |
| `./gradlew runDocker` | Run using the local docker image                                     |

If the server starts successfully, you'll see the following output:

```
2024-12-04 14:32:45.584 [main] INFO  Application - Application started in 0.303 seconds.
2024-12-04 14:32:45.682 [main] INFO  Application - Responding at http://0.0.0.0:8080
```

## Open API
Navigate to `/openapi` after running the server to see and interact with the API.

# Decisions

## Extensions file

I've added an extensions file in the root. This is a place where I generally like to place extensions methods that feel
like an addition to Kotlin's idiomatic standard library. 

Where the function body can be ugly but allows for neat function composition.

## Contracts + Context parameters

The domain package contains a contracts folder. It contains the interfaces that can be implemented in multiple ways. 

A repository interface is added to replace the CSV repository for a postgres repository in the future.

Context parameters provide an easy to use and clean way to pass around dependencies. The protocol is currently under review 
and will probably be changed in the future. 

For most applications I'm still in favor of using them and upgrading the protocol
when it is finished. 

## Exceptions

In the early version of this assessment, I had some exceptions for Illegal arguments. 

I got to remove them by coercing the input to fit.
In general I'm a proponent of Arrow's Either instead of using Exceptions. 

Then catching all exceptions that happen outside of self-written code.

## Shutdown hook

My idea was to register a shutdown hook, keep the changes in talks in memory and write them to a file in a graceful shutdown with the hook.
The hook never triggered in my tests, 

so instead I directly append to the file when a new talk is added. The traces of the shutdown hook still
remain in the code to discuss during the interview.