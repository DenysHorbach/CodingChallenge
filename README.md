# Microsevices Coding Challenge

## General Info
This service is a RESTful API that provides information about GitHub repositories. It allows you to retrieve a list of repositories for a given username, along with their branches.

## Technologies Used
The service is built using the following technologies:

- Spring Boot
- WebFlux
- Java 17
- Docker
- AWS Fargate
- AWS API Gateway
- Jenkins

## Local Setup
To run the service locally, follow these steps:

1. Make sure you have Java 17 installed on your machine.
2. Clone the repository from GitHub.
3. Generate the OpenAPI code using the Gradle wrapper: `./gradlew openApiGenerate`.
4. Build the project using the Gradle wrapper: `./gradlew build`.
5. Run the application using the Gradle wrapper: `./gradlew bootRun`.

The service will be available at `http://localhost:8080`.

## AWS Deployment
The service can be deployed on AWS Fargate using the provided CloudFormation template. The template sets up the necessary resources, including the Fargate service and API Gateway.

To deploy the service on AWS, follow these steps:

1. Build the Docker image using the provided Dockerfile.
2. Push the Docker image to an Amazon ECR repository.
3. Deploy the CloudFormation stack using the provided template.

## Jenkins CI/CD
The Coding Challenge service is integrated with Jenkins for continuous integration and deployment. The Jenkins pipeline automates the build, Docker image creation, and deployment processes.

The pipeline is triggered on every commit to the GitHub repository. It builds the application, creates a Docker image, pushes it to ECR, and updates the Fargate service with the new image.

## Links
- Swagger UI: [http://ccnxegdrq1.execute-api.eu-central-1.amazonaws.com/webjars/swagger-ui/index.html](http://ccnxegdrq1.execute-api.eu-central-1.amazonaws.com/webjars/swagger-ui/index.html)
- Jenkins: [http://3.76.8.219:8080/](http://3.76.8.219:8080/)

