package org.example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import org.example.entity.ShortenedUrlEntity;
import org.example.records.ErrorResponse;
import org.example.utils.DynamoDBUtils;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RedirectUrlHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        LambdaLogger logger = context.getLogger();

        // Extract shortUrl from path parameters
        String shortUrl = extractShortUrl(input, logger);
        logger.log("INFO: Extracted shortUrl request: " + shortUrl);
        if (Objects.isNull(shortUrl)) {
            return buildErrorResponse(400, "Short URL not provided");
        }

        try {
            // Load URL from DynamoDB
            var entity = DynamoDBUtils.getDynamoDbMapper().load(ShortenedUrlEntity.class, shortUrl);
            if (Objects.isNull(entity)) {
                logger.log("ERROR: Short URL not found: " + shortUrl);
                return buildErrorResponse(404, "URL not found");
            }

            // Update access statistics asynchronously
            updateAccessStats(entity, logger);

            // Return redirect response
            return buildRedirectResponse(entity.getLongUrl());

        } catch (Exception e) {
            logger.log("ERROR: Failed to process redirect - " + e.getMessage());
            return buildErrorResponse(500, "Unexpected Error Occurred");
        }
    }

    private String extractShortUrl(APIGatewayProxyRequestEvent input, LambdaLogger logger) {
        Map<String, String> pathParameters = input.getPathParameters();
        if (Objects.isNull(pathParameters) || !pathParameters.containsKey("shortUrl")) {
            logger.log("ERROR: No short URL provided in path parameters");
            return null;
        }
        return pathParameters.get("shortUrl");
    }

    private void updateAccessStats(ShortenedUrlEntity entity, LambdaLogger logger) {
        try {
            entity.setClickCount(entity.getClickCount() + 1);
            entity.setLastAccessedAt(Instant.now().toString());
            DynamoDBUtils.getDynamoDbMapper().save(entity);
        } catch (Exception e) {
            logger.log("Error: Failed to update access stats - " + e.getMessage());
            throw e;
        }
    }

    private APIGatewayProxyResponseEvent buildRedirectResponse(String longUrl) {
        var headers = new HashMap<>(Map.of(
                "Location", longUrl,
                "Cache-Control", "no-cache, no-store, must-revalidate"));
        headers.putAll(DynamoDBUtils.getHeaders());
        return new APIGatewayProxyResponseEvent()
                .withStatusCode(301)
                .withHeaders(headers);
    }

    private APIGatewayProxyResponseEvent buildErrorResponse(int statusCode, String message) {
        return new APIGatewayProxyResponseEvent()
                .withStatusCode(statusCode)
                .withHeaders(DynamoDBUtils.getHeaders())
                .withBody(DynamoDBUtils.getGson().toJson(new ErrorResponse(message)));
    }
}

