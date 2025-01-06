package org.example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.JsonSyntaxException;
import org.example.entity.ShortenedUrlEntity;
import org.example.records.ErrorResponse;
import org.example.records.ShortUrlRequest;
import org.example.records.ShortUrlResponse;
import org.example.utils.DynamoDBUtils;
import org.example.utils.HashGenerator;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.Instant;
import java.util.Objects;

public class ShortenUrlHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        LambdaLogger logger = context.getLogger();
        // Validate input
        if (Objects.isNull(input.getBody())) {
            logger.log("ERROR: Empty request body");
            return buildResponse(400, new ErrorResponse("Request body cannot be empty"));
        }

        // Parse request
        ShortUrlRequest request;
        try {
            request = DynamoDBUtils.getGson().fromJson(input.getBody(), ShortUrlRequest.class);
            logger.log("INFO: Received request: " + request);
        } catch (JsonSyntaxException e) {
            logger.log("ERROR: Invalid JSON format: " + e.getMessage());
            return buildResponse(400, new ErrorResponse("Invalid request format"));
        }

        // Validate URL
        String longUrl = request.longUrl();
        if (Objects.isNull(longUrl) || longUrl.trim().isEmpty()) {
            logger.log("ERROR: Empty URL provided");
            return buildResponse(400, new ErrorResponse("URL cannot be empty"));
        }

        if (longUrl.length() > DynamoDBUtils.getMaxUrlLength()) {
            logger.log("ERROR: URL exceeds maximum length");
            return buildResponse(400, new ErrorResponse("URL exceeds maximum length of " + DynamoDBUtils.getMaxUrlLength()));
        }

        // Validate URL format
        if (!isValidURL(longUrl)) {
            logger.log("ERROR: Invalid URL format: " + longUrl);
            return buildResponse(400, new ErrorResponse("Invalid URL format"));
        }
        logger.log("INFO: Valid URL validation successful, generating short URL ...");
        // Generate short URL
        String shortUrl = HashGenerator.createHash(DynamoDBUtils.getHashLength());

        // Save to DynamoDB
        try {
            saveUrlMapping(shortUrl, longUrl, logger);
        } catch (Exception e) {
            return buildResponse(500, new ErrorResponse("Failed to create short URL"));
        }

        logger.log("SUCCESS: URL shortened successfully: " + shortUrl);
        return buildResponse(201, new ShortUrlResponse(shortUrl));
    }

    private void saveUrlMapping(String shortUrl, String longUrl, LambdaLogger logger) {
        logger.log("INFO: Saving to DynamoDB ...");
        try {
            DynamoDBUtils.getDynamoDbMapper().save(createEntity(shortUrl, longUrl));
        } catch (Exception e) {
            logger.log("ERROR: Failed to save URL mapping: " + e.getMessage());
            throw e;
        }
    }

    private ShortenedUrlEntity createEntity(String shortUrl, String longUrl) {
        return new ShortenedUrlEntity(shortUrl, longUrl, 0L, Instant.now().toString(), Instant.now().toString());
    }

    private APIGatewayProxyResponseEvent buildResponse(int statusCode, Object body) {
        return new APIGatewayProxyResponseEvent()
                .withStatusCode(statusCode)
                .withHeaders(DynamoDBUtils.getHeaders())
                .withBody(DynamoDBUtils.getGson().toJson(body));
    }

    private boolean isValidURL(String urlString) {
        try {
            URL url = new URL(urlString);
            url.toURI();
            String protocol = url.getProtocol().toLowerCase();
            return protocol.equals("http") || protocol.equals("https");
        } catch (URISyntaxException | MalformedURLException e) {
            return false;
        }
    }
}
