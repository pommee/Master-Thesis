# Exam-Project

## _Hugo Sigurdson | Mohammadreza Kazemi_

Performance comparison in a chat-application targeting REST-API & gRPC using Monolith & Microservice architecture.

> In order for the group to get an accurate result
> on the performance difference and resource usage
> depending on what type of communication and architecture
> is used, custom tests had to be done.

## Tech

Different technologies and solutions have been utilized and will be explained in the table below.

| Tech                 | Info                              | Link                                                 |
|----------------------|-----------------------------------|------------------------------------------------------|
| Spring Boot          | Used for creating REST-API        | https://spring.io/projects/spring-boot               |
| Protoc & Protoc Java | Compiler to generate gRPC files   | https://github.com/protocolbuffers/protobuf/releases |

## Project Structure

### Client

**src/main/java** contains all files needed, though servers need to be started seperated.<br>
A menu for starting benchmarking and setting custom parameters are all present.

### RESTAPI
