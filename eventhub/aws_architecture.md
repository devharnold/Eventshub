# Refactoring an Events Booking Platform to use AWS Lambda

## Overview

The current backend follows a traditional layered monolithic architecture:

```
Client
   │
Spring Boot API
├── Controllers
├── Services
├── DAO Layer
└── PostgreSQL
```

This architecture is suitable for small to medium workloads. However, when ticket sales begin for popular events, thousands of users may attempt to book simultaneously. Instead of scaling the entire application, only the services experiencing high demand should scale independently.

The recommended approach is to adopt a **hybrid architecture**, where the core application remains a Spring Boot monolith while selected components are moved to AWS Lambda as serverless functions.

---

# Current Project Structure

```
Spring Boot
│
├── Authentication
├── Users
├── Organizations
├── Events
├── Bookings
├── Payments
├── Tickets
└── PostgreSQL
```

---

# Components That Should Remain in Spring Boot

The following modules should remain within the primary application because they are stateful, frequently accessed, and do not benefit significantly from serverless execution.

* User Authentication
* User Management
* Organization Management
* Event Management
* Ticket Management
* JWT Security
* Administrative APIs

These services continuously interact with the database and are better suited to a traditional backend.

```
                Spring Boot API
        ┌─────────────────────────┐
        │ Authentication          │
        │ Users                   │
        │ Organizations           │
        │ Events                  │
        │ Tickets                 │
        └──────────┬──────────────┘
                   │
             PostgreSQL / Amazon RDS
```

---

# Components That Should Become Serverless

The following services experience unpredictable traffic spikes and benefit greatly from automatic scaling.

## 1. Booking Service

### Current Flow

```
Client

↓

BookingController

↓

BookingService

↓

BookingDAO

↓

Database
```

Every booking request is processed by the Spring Boot server. During large events, the server may become overwhelmed.

### Proposed Flow

```
Client

↓

API Gateway

↓

AWS Lambda (Booking)

↓

Amazon SQS

↓

Booking Processor Lambda

↓

PostgreSQL / Amazon RDS
```

### Advantages

* Automatically scales to thousands of concurrent booking requests.
* Booking requests are queued instead of being lost.
* Reduces pressure on the Spring Boot application.
* Improves fault tolerance during traffic spikes.

---

## 2. Payment Service (M-Pesa)

Payment processing is an excellent candidate for serverless because each payment request is independent.

### Current Flow

```
Client

↓

PaymentController

↓

PaymentService

↓

M-Pesa API

↓

Database
```

### Proposed Flow

```
Client

↓

API Gateway

↓

Payment Lambda

↓

Safaricom M-Pesa API

↓

Payment Callback

↓

Callback Lambda

↓

Database
```

### Responsibilities of Payment Lambda

* Generate M-Pesa access token.
* Initiate STK Push.
* Store transaction details.
* Return payment status.

### Responsibilities of Callback Lambda

* Receive M-Pesa callback.
* Verify transaction.
* Update payment status.
* Trigger booking confirmation.

### Benefits

* Independent scaling.
* Faster response times.
* No idle infrastructure costs.
* Better resilience during high payment volumes.

---

## 3. Ticket Generation

Ticket generation typically occurs after a successful payment and is an event-driven task.

### Flow

```
Payment Successful

↓

EventBridge

↓

Ticket Lambda

↓

Generate QR Code

↓

Generate Ticket Number

↓

Save Ticket

↓

Send Email
```

### Responsibilities

* Generate unique ticket number.
* Generate QR code.
* Save ticket information.
* Notify the customer.

---

## 4. Email Notifications

Email delivery should be handled asynchronously.

```
Booking Completed

↓

Amazon SNS

↓

Notification Lambda

↓

Amazon SES

↓

Customer
```

### Responsibilities

* Send booking confirmation emails.
* Send ticket emails.
* Send payment confirmations.
* Send cancellation notifications.

---

## 5. Analytics

Analytics should not interfere with booking performance.

```
Booking Event

↓

Amazon EventBridge

↓

Analytics Lambda

↓

CloudWatch

↓

Amazon S3

↓

Amazon Athena
```

Example metrics include:

* Total bookings
* Revenue
* Popular events
* Peak booking hours
* Payment success rate

---

# Recommended Project Structure

```
eventhub/

├── spring-api/
│
│   ├── controller/
│   ├── service/
│   ├── dao/
│   ├── security/
│   ├── model/
│   └── utils/
│
├── lambda-booking/
│   ├── BookingHandler.java
│   ├── BookingService.java
│   └── BookingRepository.java
│
├── lambda-payment/
│   ├── MpesaHandler.java
│   ├── MpesaService.java
│   └── CallbackHandler.java
│
├── lambda-ticket/
│   ├── TicketHandler.java
│   ├── QRGenerator.java
│   └── TicketGenerator.java
│
├── lambda-notification/
│   ├── EmailHandler.java
│   └── NotificationService.java
│
├── shared/
│   ├── DTOs/
│   ├── Models/
│   ├── Utilities/
│   └── Constants/
│
└── infrastructure/
    ├── SAM/
    ├── CloudFormation/
    └── Terraform/
```

---

# High-Level Architecture

```
                    Users
                      │
                      ▼
               Amazon API Gateway
                      │
        ┌─────────────┴─────────────┐
        │                           │
        ▼                           ▼
 Spring Boot API             AWS Lambda
        │                     (Booking)
        │                           │
        │                     Amazon SQS
        │                           │
        │                     Booking Lambda
        │                           │
        ├───────────────┐           │
        ▼               ▼           ▼
Authentication      Events      PostgreSQL
Organizations       Tickets      (Amazon RDS)

                      │
                      ▼
               Payment Lambda
                      │
                Safaricom M-Pesa
                      │
                Callback Lambda
                      │
                 Ticket Lambda
                      │
               Notification Lambda
                      │
                 Amazon SES
```

---

# AWS Services Used

| Service                 | Purpose                                                                         |
| ----------------------- | ------------------------------------------------------------------------------- |
| Amazon API Gateway      | Entry point for REST APIs                                                       |
| AWS Lambda              | Serverless compute for bookings, payments, ticket generation, and notifications |
| Amazon SQS              | Queue booking requests during traffic spikes                                    |
| Amazon EventBridge      | Trigger event-driven workflows                                                  |
| Amazon RDS (PostgreSQL) | Persistent relational database                                                  |
| Amazon SES              | Send transactional emails                                                       |
| Amazon CloudWatch       | Logging and monitoring                                                          |
| AWS IAM                 | Secure permissions between services                                             |
| AWS SAM or Terraform    | Infrastructure as Code                                                          |

---

# Benefits of the Hybrid Architecture

* Automatically scales during ticket sale peaks.
* Reduces operational costs by running serverless functions only when needed.
* Isolates booking and payment workloads from the core application.
* Improves fault tolerance through asynchronous processing.
* Simplifies maintenance by separating independent services.
* Demonstrates modern cloud-native architecture suitable for production workloads.

---

# Conclusion

A hybrid architecture combining Spring Boot and AWS Lambda provides the best balance between simplicity, scalability, and cost efficiency. The Spring Boot application continues to manage authentication, user accounts, organizations, events, and ticket data, while AWS Lambda handles high-demand, event-driven workloads such as booking processing, M-Pesa payments, ticket generation, notifications, and analytics.

This design ensures that critical operations can scale independently during peak demand without requiring the entire backend to scale, making the platform more resilient, efficient, and cloud-native.


## DOCUMENTATION REFERENCES

The following AWS documentation provides the official guidance for implementing the serverless architecture used in this project.

## Core Serverless Services

### AWS Lambda

Learn how to build, deploy, invoke, and scale Java-based Lambda functions.

* https://docs.aws.amazon.com/lambda/latest/dg/welcome.html
* https://docs.aws.amazon.com/lambda/latest/dg/java-handler.html
* https://docs.aws.amazon.com/lambda/latest/dg/best-practices.html

---

### Amazon API Gateway

Used to expose REST endpoints that invoke Lambda functions.

* https://docs.aws.amazon.com/apigateway/latest/developerguide/welcome.html
* https://docs.aws.amazon.com/apigateway/latest/developerguide/http-api.html

---

### Amazon SQS

Queue booking requests to absorb traffic spikes and process them asynchronously.

* https://docs.aws.amazon.com/AWSSimpleQueueService/latest/SQSDeveloperGuide/welcome.html
* https://docs.aws.amazon.com/AWSSimpleQueueService/latest/SQSDeveloperGuide/sqs-best-practices.html

---

### Amazon EventBridge

Trigger downstream services such as ticket generation and analytics.

* https://docs.aws.amazon.com/eventbridge/latest/userguide/eb-what-is.html

---

### Amazon RDS for PostgreSQL

Persistent relational database for users, events, bookings, and payments.

* https://docs.aws.amazon.com/AmazonRDS/latest/UserGuide/Welcome.html
* https://docs.aws.amazon.com/AmazonRDS/latest/UserGuide/CHAP_PostgreSQL.html

---

### AWS IAM

Manage permissions for Lambda, API Gateway, RDS, CloudWatch, and other AWS services.

* https://docs.aws.amazon.com/IAM/latest/UserGuide/introduction.html
* https://docs.aws.amazon.com/IAM/latest/UserGuide/best-practices.html

---

### Amazon CloudWatch

Monitor logs, metrics, and application performance.

* https://docs.aws.amazon.com/AmazonCloudWatch/latest/monitoring/WhatIsCloudWatch.html
* https://docs.aws.amazon.com/lambda/latest/dg/monitoring-cloudwatchlogs.html

---

### Amazon SES

Send booking confirmations, payment receipts, and ticket emails.

* https://docs.aws.amazon.com/ses/latest/dg/Welcome.html

---

## Java Development

### AWS SDK for Java 2.x

Official Java SDK for interacting with AWS services.

* https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/home.html

---

## Infrastructure as Code

### AWS SAM (Serverless Application Model)

Deploy Lambda functions, API Gateway, and supporting resources.

* https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/what-is-sam.html

Alternatively, if using Terraform:

* https://developer.hashicorp.com/terraform/tutorials/aws-get-started

---

## Security

### AWS Secrets Manager

Store database credentials, API keys, and M-Pesa secrets securely.

* https://docs.aws.amazon.com/secretsmanager/latest/userguide/intro.html

---

## Architecture Guidance

### AWS Well-Architected Framework

Official AWS best practices for designing secure, reliable, high-performing, and cost-effective cloud applications.

* https://docs.aws.amazon.com/wellarchitected/latest/framework/welcome.html

---

### AWS Serverless Lens

Best practices specifically for serverless applications.

* https://docs.aws.amazon.com/wellarchitected/latest/serverless-applications-lens/welcome.html

---

## Recommended Learning Order

1. AWS IAM
2. AWS Lambda
3. Amazon API Gateway
4. AWS SDK for Java 2.x
5. Amazon SQS
6. Amazon EventBridge
7. Amazon RDS (PostgreSQL)
8. Amazon CloudWatch
9. Amazon SES
10. AWS Secrets Manager
11. AWS SAM
12. AWS Well-Architected Framework
13. AWS Serverless Lens
