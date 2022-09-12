package com.clairvoyant.iPlanner.Utility;

/**
 * This is a Utility class for MongoDb Connection constants
 * @author Abhinov Gogoi
 */
public class MongoDBConnectionInfo {
    /**
     * Mongodb local URI
     */
    public static final String LOCAL_URI = "mongodb://localhost:27017";
    /**
     * Mongodb remote URI
     */
    public static final String REMOTE_URI = "mongodb+srv://groot:root@meow.nqrji.mongodb.net/meow";
    /**
     * Switch to true if mongodb is used on localhost
     */
    public static final boolean isLocalMongo = false;
    /**
     * MongoDb database name
     */
    public static final String mongoDb_Database = "iPlanner";
    /**
     * Mongodb collection for test connection
     */
    public static final String test_col = "test_col";
    /**
     * Mongodb collection for test connection
     */
    public static final String listing_col = "listing_col";
    /**
     * Mongodb collection for interviewers
     */
    public static final String interviewer_col = "interviewer_col";
}
