package org.example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import org.example.entity.ShortenedUrlEntity;
import org.example.records.ErrorResponse;
import org.example.records.UrlAnalyticsResponse;
import org.example.utils.DynamoDBUtils;

import java.util.Map;
import java.util.Objects;
public class UrlAnalyticsHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        LambdaLogger logger = context.getLogger();

        // Extract shortUrl from path parameters
        String shortUrl = extractShortUrl(input, logger);
        logger.log("INFO: Extracted shortUrl request: " + shortUrl);
        if (Objects.isNull(shortUrl)) {
            return buildResponse(400, new ErrorResponse("Short URL not provided"));
        }

        try {
        logger.log("INFO: Loading from DynamoDB ...");
        var entity = DynamoDBUtils.getDynamoDbMapper().load(ShortenedUrlEntity.class, shortUrl);

        if (Objects.isNull(entity)) {
            logger.log("ERROR: Short URL not found: " + shortUrl);
            return buildResponse(404, new ErrorResponse("URL not found"));
        }

        logger.log("INFO: Short URL info found ");
        return buildResponse(200, new UrlAnalyticsResponse(entity.getClickCount(),
                entity.getLastAccessedAt(), entity.getCreatedAt(),entity.getLongUrl()));

        } catch (Exception e) {
            logger.log("ERROR: Failed to process redirect - " + e.getMessage());
            return buildResponse(500, new ErrorResponse("Unexpected Error Occurred"));
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

    private APIGatewayProxyResponseEvent buildResponse(int statusCode, Object body) {
        return new APIGatewayProxyResponseEvent()
                .withStatusCode(statusCode)
                .withHeaders(DynamoDBUtils.getHeaders())
                .withBody(DynamoDBUtils.getGson().toJson(body));
    }
}

