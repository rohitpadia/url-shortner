package org.example.utils;


import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.google.gson.Gson;

import java.util.Map;

public class DynamoDBUtils {
    // Private constructor to prevent instantiation
    private DynamoDBUtils() {}

    private static final AmazonDynamoDB CLIENT = AmazonDynamoDBClientBuilder.standard().build();

    private static final DynamoDBMapper DYNAMO_DB_MAPPER = new DynamoDBMapper(CLIENT);

    private static final Gson GSON = new Gson();

    private static final int HASH_LENGTH = 8;

    private static final int MAX_URL_LENGTH = 2048;

    private static final Map<String, String> HEADERS = Map.of("Content-Type", "application/json",
            "Access-Control-Allow-Origin", "*",
            "Access-Control-Allow-Methods", "OPTIONS,POST,GET",
            "Access-Control-Allow-Headers", "Content-Type,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token");

    // Getters for the static fields
    public static AmazonDynamoDB getClient() {
        return CLIENT;
    }

    public static DynamoDBMapper getDynamoDbMapper() {
        return DYNAMO_DB_MAPPER;
    }

    public static Gson getGson() {
        return GSON;
    }

    public static Map<String, String> getHeaders() {
        return HEADERS;
    }

    public static int getHashLength() {
        return HASH_LENGTH;
    }

    public static int getMaxUrlLength() {
        return MAX_URL_LENGTH;
    }
}

