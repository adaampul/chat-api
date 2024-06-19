## Project Goal
The goal of the project was to create a simple Chat API for a support service.

## Expected Workflow
1. Customer creates an inquiry.
2. Support agent reads the message and assigns a support case to it.
3. Support agent responds to the customer and provides support.
4. Customer can provide additional information by sending follow-up messages.

### Documentation
API documentation is available at `/swagger-ui/index.html`.

### Running the App
1.
 ```shell
 ./gradlew bootRun
 ```
2.
 ```shell
 ./gradlew build
 java -jar build/libs/chat-api-0.0.1-SNAPSHOT.jar
 ```
3.
 ```shell
 docker compose up
 ```

## API Validation
1. Create a message from "Alex Johnson" with the content: "My laptop does not work".
    ```shell
    curl \
       --header "Content-Type: application/json" \
       --request POST \
       --data @- \
       http://localhost:8080/messages <<EOF
       {
         "userId": "456",
         "username": "Alex Johnson",
         "content": "My laptop does not work"
       }
    EOF
    ```
2. Create a client case, with the client name "Alex Johnson", and having the previously created message in its messages list.
    ```shell
    curl \
       --header "Content-Type: application/json" \
       --request POST \
       --data @- \
       http://localhost:8080/support-cases <<EOF
       {
         "clientReference": "Alex Johnson",
         "messages": [{
            "id": 1
         }]
       }
    EOF
    ```

3. Create a message from "Emily Clark", with the following content: "I'm Emily, and I will assist you. What is your laptop brand and model?". This message will be linked to the previously created client case.
    ```shell
    curl \
       --header "Content-Type: application/json" \
       --request POST \
       --data @- \
       http://localhost:8080/messages <<EOF
       {
         "userId": "654",
         "username": "Emily Clark",
         "supportCaseId": 1,
         "content": "I'm Emily, and I will assist you. What is your laptop brand and model?"
       }
    EOF
    ```

4. Modify the client case by adding the client reference "LB-45CD7". This will validate the client case modification feature.
    ```shell
    curl \
       --header "Content-Type: application/json" \
       --request PATCH \
       --data @- \
       http://localhost:8080/support-cases/1 <<EOF
       {
         "clientReference": "LB-45CD7"
       }
    EOF
    ```

5. Fetch all client cases. The result will only contain one client case, the one we created before.
    ```shell
    curl http://localhost:8080/support-cases
    ```

## Performance Tests using JMETER
### Scenarios
This test plan is designed to stress-test the ChatAPI, identify potential bottlenecks, and ensure it can handle concurrent user actions efficiently.

1. **Create New Support Case**
   - Simulates users creating new support cases and associated messages.
   - **Threads:** 20
   - **Loops:** 15

2. **Create New Message**
   - Simulates users adding new messages to existing support cases.
   - **Threads:** 55
   - **Loops:** 15

3. **Fetch All Cases**
   - Simulates users retrieving all existing support cases.
   - **Threads:** 20
   - **Loops:** 15

4. **Update Existing Case**
   - Simulates users updating details of existing support cases.
   - **Threads:** 5
   - **Loops:** 15

To run the performance test:
1. Run the app with `performance-test` profile
    ```shell
    java -Dspring.profiles.active=performance-test -jar build/libs/chat-api-0.0.1-SNAPSHOT.jar
    ```
2. Run the test plan with `JMeter` CLI mode
    ```shell
    jmeter -n -t perf_test_conf.jmx -l jmeter-run.log -e -o jmeter
    ```
3. Stop the application.

The results, including the response time percentiles is visible at html file in folder jmeter

## Architecture Description

### Architecture Description
The system architecture consists of the following components:
1. **Spring Boot Application** - Responsible for business logic and API handling.
2. **Database** - Stores information about clients, messages, and support cases.
3. **Swagger UI** - Interface for API documentation and testing.
4. **Docker** - Tool for containerizing the application, enabling easy deployment and scaling.
5. **JMeter** - Tool for performance testing the application.

### Deployment Infrastructure
The application can be deployed in a containerized environment using Docker Compose, which allows easy management of dependencies and configuration. Deployment in the cloud is also possible using services such as AWS, Azure, or Google Cloud, providing scalability and high availability.

# JMeter Performance Test Plan for ChatAPI

This JMeter test plan evaluates the performance of the ChatAPI by simulating various user interactions. It includes four main test scenarios, each designed to mimic real-world usage patterns.

### Key Components

- **HTTP Requests**: Used to send various types of requests (POST, GET, PATCH) to the ChatAPI endpoints.
- **Counters**: Generate unique values for user IDs and case IDs to simulate different users and cases.
- **Header Managers**: Ensure all requests include the `Content-Type: application/json` header.
- **Post Processors**: Extract data from API responses for use in subsequent requests.
