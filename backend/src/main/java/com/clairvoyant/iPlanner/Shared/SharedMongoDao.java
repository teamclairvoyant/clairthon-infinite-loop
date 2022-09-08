package com.clairvoyant.iPlanner.Shared;

import com.clairvoyant.iPlanner.Utility.Literal;
import com.clairvoyant.iPlanner.Utility.MongoDBConnectionInfo;
import com.clairvoyant.iPlanner.Utility.MongoDbConnection;
import com.clairvoyant.iPlanner.Utility.Utility;
import com.mongodb.DuplicateKeyException;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class SharedMongoDao {
    /**
     * mongo template for mongo connection
     */
    private MongoTemplate mongoTemplate;

    /**
     * Call constructor
     */
    public SharedMongoDao() {
        /**
         * Get mongo template connection
         */
        mongoTemplate = MongoDbConnection.getInstance().mongoTemplate();
    }

    /**
     * Class instance
     */
    private static SharedMongoDao instance;

    /**
     * @return instance of this class
     */
    public static SharedMongoDao getInstance() {
        synchronized (SharedMongoDao.class) {
            if (instance == null) {
                instance = new SharedMongoDao();
            }
            return instance;
        }
    }

    /**
     * This method is used to check mongo is up or down.
     *
     * @return
     */
    public boolean testMongoConnection() {
        try {
            mongoTemplate.dropCollection(MongoDBConnectionInfo.test_col);
            /**
             * try to create a collection in this db
             */
            mongoTemplate.createCollection(MongoDBConnectionInfo.test_col);
            /**
             * return true if collection exists
             */
            if (mongoTemplate.collectionExists(MongoDBConnectionInfo.test_col)) {
                /**
                 * delete the collection
                 */
                mongoTemplate.dropCollection(MongoDBConnectionInfo.test_col);
                /**
                 * mongo is up, return true
                 */
                return Literal.TRUE;
            } else {
                return Literal.FALSE;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Literal.FALSE;
        }
    }

    /**
     * Generic method to insert a document into mongo collection.
     *
     * @param document
     * @param collection
     * @return
     */
    public boolean insertDocument(final Document document, final String collection) {
        try {
            document.append(Literal.createdDate, Utility.now());
            document.append(Literal.createdDateMs, Utility.currentTimeMillis());
            document.append(Literal.updatedDate, Utility.now());
            document.append(Literal.updatedDateMs, Utility.currentTimeMillis());

            mongoTemplate.insert(document, collection);
            return Literal.TRUE;
        } catch (DuplicateKeyException de) {
            return Literal.FALSE;
        } catch (Exception e) {
            e.printStackTrace();
            return Literal.FALSE;
        }
    }

    /**
     * Generic method to find a documents in a mongo collection with query.
     *
     * @param collection MongoDb Collection name for this search
     * @return
     */
    public List<Map<String, Object>> getDocuments(final Query query, final String collection) {
        try {
            /**
             * Get the data
             */
            return new ArrayList<Map<String, Object>>(mongoTemplate.find(query, Document.class, collection));
        } catch (Exception e) {
            e.printStackTrace();
            return Literal.EMPTY_ARRAYLIST;
        }
    }

    /**
     * Generic method to find a documents in a mongo collection with filter.
     *
     * @param filter_doc Filter options
     * @param collection MongoDb Collection name for this search
     * @return
     */
    public List<Map<String, Object>> getDocuments(final Document filter_doc, final String collection) {
        return getDocuments(filter_doc, null, collection);
    }

    /**
     * Generic method to find a documents in a mongo collection with filter and projection.
     *
     * @param filter_doc Filter options
     * @param search_doc Search/Projection options
     * @param collection MongoDb Collection name for this search
     * @return
     */
    public List<Map<String, Object>> getDocuments(final Document filter_doc, final Document search_doc, final String collection) {
        try {
            Query query;
            if (search_doc == null || search_doc.isEmpty()) {
                query = new BasicQuery(filter_doc);
            } else {
                query = new BasicQuery(filter_doc, search_doc);
            }
            /**
             * Get the data
             */
            return new ArrayList<Map<String, Object>>(mongoTemplate.find(query, Document.class, collection));
        } catch (Exception e) {
            e.printStackTrace();
            return Literal.EMPTY_ARRAYLIST;
        }
    }

    /**
     * Generic method to find a documents in a mongo collection with Pagination.
     * By default, sort by creation time in descending
     *
     * @param filter_doc Filter options
     * @param search_doc Search/Projection options
     * @param collection MongoDb Collection name for this search
     * @param count      page start count /page index
     * @param r_count    the size of the page to be returned.
     */
    public List<Map<String, Object>> getDocuments(final Document filter_doc, final Document search_doc, final String collection, int count, int r_count, String order_key) {
        try {
            Query query;
            if (search_doc == null || search_doc.isEmpty()) {
                query = new BasicQuery(filter_doc);
            } else {
                query = new BasicQuery(filter_doc, search_doc);
            }
            /**
             * add pagination query into main query
             */
            query.with(PageRequest.of(count, r_count, Sort.by(Sort.Direction.DESC, order_key)));
            /**
             * Get the data
             */
            return new ArrayList<Map<String, Object>>(mongoTemplate.find(query, Document.class, collection));
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<Map<String, Object>>();
        }
    }

    /**
     * Generic method to find a documents in a mongo collection with Pagination.
     * By default, sort by creation time in descending
     *
     * @param filter_doc Filter options
     * @param search_doc Search/Projection options
     * @param collection MongoDb Collection name for this search
     */
    public List<Map<String, Object>> getDocuments(final Document filter_doc, final Document search_doc, final String collection, final String order_key, final String order) {
        try {
            Query query;
            if (search_doc == null || search_doc.isEmpty()) {
                query = new BasicQuery(filter_doc);
            } else {
                query = new BasicQuery(filter_doc, search_doc);
            }
            /**
             *
             */
            if (order.equalsIgnoreCase(Literal.ASC)) {
                /**
                 * add pagination query into main query
                 */
                query.with(Sort.by(Sort.Direction.ASC, order_key));
            } else {
                /**
                 * add pagination query into main query
                 */
                query.with(Sort.by(Sort.Direction.DESC, order_key));
            }

            /**
             * Get the data
             */
            return new ArrayList<Map<String, Object>>(mongoTemplate.find(query, Document.class, collection));
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<Map<String, Object>>();
        }
    }

    /**
     * Generic method to update a document into mongo collection with query.
     * Update will NOT insert new document, if document is not found
     *
     * @param query
     * @param update
     * @param collection
     * @return
     */
    public boolean updateDocument(final Query query, final Update update, final String collection) {
        try {
            update.set(Literal.updatedDate, Utility.now());
            update.set(Literal.updatedDateMs, Utility.currentTimeMillis());
            /**
             * update data in MONGODB
             */
            UpdateResult upd = mongoTemplate.updateFirst(query, update, collection);
            return upd.wasAcknowledged();
        } catch (Exception e) {
            e.printStackTrace();
            return Literal.FALSE;
        }
    }

    /**
     * Generic method to update a document into mongo collection with filter.
     * Update will NOT insert new document, if document is not found
     *
     * @param update
     * @param collection
     * @return
     */
    public boolean updateDocument(final Document filter_doc, final Update update, final String collection) {
        try {
            update.set(Literal.updatedDate, Utility.now());
            update.set(Literal.updatedDateMs, Utility.currentTimeMillis());

            Query query = new BasicQuery(filter_doc);
            /**
             * update data in MONGODB
             */
            UpdateResult upd = mongoTemplate.updateFirst(query, update, collection);
            return upd.wasAcknowledged();
        } catch (Exception e) {
            e.printStackTrace();
            return Literal.FALSE;
        }
    }

    /**
     * Generic method to upsert a document into mongo collection.
     * Upsert will insert new document if not found (update+insert)
     *
     * @param query
     * @param update
     * @param collection
     * @return
     */
    public boolean upsertDocument(final Query query, final Update update, final String collection) {
        try {
            update.set(Literal.updatedDate, Utility.now());
            update.set(Literal.updatedDateMs, Utility.currentTimeMillis());
            /**
             * upsert data in MONGODB
             */
            UpdateResult upd = mongoTemplate.upsert(query, update, collection);
            return upd.wasAcknowledged();
        } catch (Exception e) {
            e.printStackTrace();
            return Literal.FALSE;
        }
    }

    /**
     * Generic method to upsert a document into mongo collection.
     * Upsert will insert new document if not found (update+insert)
     *
     * document should contain _id to upsert
     *
     * @param collection
     * @return
     */
    public boolean upsertDocument(Document document, final String collection) {
        try {
            document.append(Literal.updatedDate, Utility.now());
            document.append(Literal.updatedDateMs, Utility.currentTimeMillis());

            Document filter_doc = new Document().append(Literal._id, document.get(Literal._id));
            Query query = new BasicQuery(filter_doc);

            Update update = new Update();
            document.forEach(update::set);
            /**
             * upsert data in MONGODB
             */
            return upsertDocument(query, update, collection);
        } catch (Exception e) {
            e.printStackTrace();
            return Literal.FALSE;
        }
    }


    /**
     * Generic method to find a documents in a mongo collection with query.
     *
     * @param collection MongoDb Collection name for this search
     * @return
     */
    public Map<String, Object> getDocument(final Query query, final String collection) {
        try {
            /**
             * Get the data
             */
            return mongoTemplate.findOne(query, Document.class, collection);
        } catch (Exception e) {
            e.printStackTrace();
            return Literal.EMPTY_HASHMAP;
        }
    }

    /**
     * Generic method to find a documents in a mongo collection with filter and search doc.
     *
     * @param filter_doc Filter options
     * @param collection MongoDb Collection name for this search
     * @return
     */
    public Map<String, Object> getDocument(final Document filter_doc, final Document srch_doc, final String collection) {
        try {
            Query query;
            if (srch_doc != null) {
                query = new BasicQuery(filter_doc, srch_doc);
            } else {
                query = new BasicQuery(filter_doc);
            }
            /**
             * Get the data
             */
            return mongoTemplate.findOne(query, Document.class, collection);
        } catch (Exception e) {
            e.printStackTrace();
            return Literal.EMPTY_HASHMAP;
        }
    }

    /**
     * Generic method to delete documents in a mongo collection with query.
     *
     * @param collection MongoDb Collection name for this search
     * @return
     */
    public boolean deleteDocuments(final Query query, final String collection) {
        try {
            /**
             * Get the data
             */
            return mongoTemplate.remove(query, collection).wasAcknowledged();
        } catch (Exception e) {
            e.printStackTrace();
            return Literal.FALSE;
        }
    }


    /**
     * Get all documents for a collection
     * @param collection_name
     * @return
     */
    public List<Document> getAllDocuments(String collection_name) {
        return mongoTemplate.findAll(Document.class, collection_name);
    }

    public MongoTemplate getMongoTemplate() {
        return mongoTemplate;
    }
}
