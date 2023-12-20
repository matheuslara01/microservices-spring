# Microservices Spring Cloud Ecosystem
This repository contains a microservices architecture implemented with the Spring ecosystem

The block diagram below illustrates the components of a modern e-commerce simulation, designed to run in a cloud environment. The system allows users to buy products online, with a focus on high availability, scalability and cloud deployment using Docker, Kubernetes and the entire Spring Cloud ecosystem.


![BLOCK DIAGRAM](https://github.com/matheuslara01/microservices-spring/blob/main/diagram.png)


## Microservices

**1. API Gateway:**
   - Acts as the single entry point to the system, receiving customer requests and directing them to the appropriate services.
   - It works like a reverse proxy, using rules to route requests based on URLs, headers, and other factors.
   - Provides security, authentication and authorization to the system.

**2. Product Service:**
   - Manages information about products available in the system.
   - Implemented with NoSQL (MongoDB) for horizontal scalability.

**3. Order Service:**
   - Process users purchase orders, checking stock availability, calculating final values and managing order flow.
   - Implemented with transaction processing systems (TPs) to ensure data consistency and integrity of financial transactions.
   - Integrates with the inventory service for stock validation.

**4. Inventory Service:**
   - Maintains control of the stock of available products, recording entries, exits and updating levels in real time.
   - Integrates with the order service to validate availability and reserve stock during order processing.
    
**5. Notification Service:**
   - Sends notifications to users about system-related events such as new products, order confirmation, delivery status, etc.
   - Uses messaging systems like Apache Kafka for asynchronous and scalable communication.

## Communication between components

Communication between system components occurs in two ways: 

**1. Synchronous Communication:**
     
  - Synchronous communication occurs when components wait for the other party to respond before continuing.
  - Synchronous communication is generally used for operations that require an immediate response, such as authentication checks or stock availability queries.

**3. Asynchronous Communication:**
     
  - Asynchronous communication occurs when components do not need to wait for the other party to respond before continuing.
  - Asynchronous communication is generally used for operations that can run in the background, such as order processing.

## Infrastructure

The system is designed as a microservices architeture, where each component is independent and can be deployed, scaled and updated individually.

**Technologies used:**

**1. Docker:**

  - Each microservices is packaged as a Docker image, making it easy to deploy and replicate across different environments.

**2. Kafka:**

  - Is a distributed messaging system that is used for asynchronous communication.
  - Is a scalable and reliable technology that can be used to communicate components across different services.

**3. Resilience4j:**

  - Is a library that provides features for fault tolerance and high availability.
  - Resilience4j can be used to protect system components from failures and ensure that the system remais available even in the event of failures.

**4. Discovery Server:**

  - Is a service that helps system components find each other.
  - Discovery server can be used to simplify system configuration and facilitate the deployment of new components.  

<!--  3. Kubernetes:
     - A container orchestration system with Kubernetes manages the lifecycle of Docker microservices, automating deployment, scaling, and load balancing.. -->
     
## Monitoring, Observability and Tracking 

**1. Prometheus:**

  - Is a monitoring system that collects data on the performance of each component of the system.
  - Prometheus can be used to identify system performance issues and take steps to improve performance.
       
**3. Grafana:**
     
  - Is a data visualization tool that is used to visualize data collected by Prometheus.
  - Grafana can be used to create graphs and dashboards that helps understand system performance.

**5. Zipkin:**
     
  - Is a request tracking system that helps identify system performance issues.
  - Zipkin can be used to trace a request path through the system and identify bottlenecks or connection issues.

## Requirements

 - ![Java-17](https://img.shields.io/badge/Java-17-blue)
 - ![Docker](https://img.shields.io/badge/Docker-blue)
 - ![Maven](https://img.shields.io/badge/Maven-blue)

## Getting Started

1. Clone this repository on your local machine:
   ```sh
   git clone 'https://github.com/matheuslara01/microservices-spring.git'
   ```

2. Navigate to the project directory:
   ```sh
   cd microservices-spring
   ```
3. Run the following command to build the project images:
   ```sh
   mvn clean install -DskipTests
   ```
   
4. Run the following command to run the images in docker and run the project:
   ```sh
   docker-compose up -d
   ```

5. To check running containers:
   ```sh
   docker ps
   ```
