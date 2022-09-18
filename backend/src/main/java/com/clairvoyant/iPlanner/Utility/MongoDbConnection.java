package com.clairvoyant.iPlanner.Utility;

import com.mongodb.MongoClientURI;
import com.mongodb.*;
import org.springframework.data.mongodb.core.MongoTemplate;

public class MongoDbConnection {
    /**
     * Hold the class instance
     */
    private static MongoDbConnection instance;
    /**
     * Dynamic DB configuration
     */
    public static MongoClient MONGOCLIENT = mongoClient();
    /**
     * Hold the mongo_template instance
     */
    public static MongoTemplate mongo_template;

    /**
     * @return
     */
    public static MongoDbConnection getInstance() {
        /**
         * Check for the Null
         */
        synchronized (MongoDbConnection.class) {
            if (instance == null) {
                instance = new MongoDbConnection();
            }
            return instance;
        }
    }

    /**
     * Get mongo client
     *
     * @return
     */
    public static MongoClient mongoClient() {
        try {
            if (MongoDBConnectionInfo.isLocalMongo) {
                return new MongoClient(new MongoClientURI(MongoDBConnectionInfo.LOCAL_URI));
            } else {
                return new MongoClient(new MongoClientURI(MongoDBConnectionInfo.REMOTE_URI));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Mongotemplate for frtu
     *
     * @return
     */
    public MongoTemplate mongoTemplate() {
        try {
            if (mongo_template == null) {
                mongo_template = new MongoTemplate(mongoClient(), MongoDBConnectionInfo.mongoDb_Database);
            }
            return mongo_template;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Mongotemplate for any db name
     *
     * @return
     */
    public MongoTemplate mongoTemplate(String db_name) {
        try {
            return new MongoTemplate(mongoClient(), db_name);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
