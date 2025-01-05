# URL Shortener Project

## Overview

This project is a scalable and efficient URL shortener application. It consists of a React-based frontend, AWS Lambda functions for backend logic, and DynamoDB for data storage. The project is structured into three main components:

- **UrlShortnerReact**: The React frontend for user interaction
- **UrlShortnerLambda**: The backend implemented using AWS Lambda functions
- **Documentation**: Contains system design and performance testing documentation

## Project Structure

```
url-shortener/
├── UrlShortnerReact/
├── UrlShortnerLambda/
└── Documentation/
```

## UrlShortnerReact

This folder contains the React code for the frontend. To run it locally:

1. **Set up a Dummy Base URL**: Configure a dummy base URL to match your API Gateway's base URL.

2. **Install Dependencies**:
   ```bash
   npm install
   ```

3. **Start the Application**:
   ```bash
   npm start
   ```

The application will be available at `http://localhost:3000`.

*Note: The React app uses react-router-dom for routing and axios for API calls. The Project utilizes React version 19.0.0*

## UrlShortnerLambda

This folder contains the backend logic implemented as AWS Lambda functions written in **JAVA 17**. These functions handle URL shortening, redirection, and analytics.

### Local Development

These functions cannot be directly run locally but can be tested in an IDE like IntelliJ IDEA or VS Code using the AWS Toolkit plugin.

**Prerequisites:**
AWS Toolkit Plugin ([AWS Toolkit for IntelliJ](https://aws.amazon.com/intellij/) or [AWS Toolkit for VS Code](https://aws.amazon.com/visualstudiocode/))
- Docker is required for local testing of AWS Lambda functions

### Testing Steps:

1. Install the AWS Toolkit plugin for your IDE
2. Open the project in your IDE
3. Configure the plugin to detect your Lambda code
4. Provide appropriate input to test each Lambda function

## Documentation

The Documentation folder contains comprehensive system design and performance testing documentation, detailing the architecture, technology choices, and scalability strategies of the project.

## System Requirements

- **Node.js**: For running the React frontend locally
- **Docker**: For local testing of AWS Lambda functions
- **AWS Toolkit Plugin**: For IDE integration to test Lambda functions locally

## Deployment

- The frontend is deployed using AWS Amplify, making it publicly accessible
- The backend is deployed using AWS Lambda and API Gateway, connected to a DynamoDB database