package com.clairvoyant.iPlanner.API.Planner;

import com.clairvoyant.iPlanner.Shared.SharedMongoDao;
import com.clairvoyant.iPlanner.Utility.Literal;
import com.clairvoyant.iPlanner.Utility.MongoDBConnectionInfo;
import com.clairvoyant.iPlanner.Utility.Utility;
import org.bson.Document;
import org.springframework.data.mongodb.core.query.BasicQuery;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PlannerService {

    private static PlannerService instance;

    /**
     * Singleton class Lazy initialization with Double check locking
     *
     * @return FreeBusyService
     */
    public static PlannerService getInstance() {
        synchronized (PlannerService.class) {
            if (instance == null) {
                instance = new PlannerService();
            }
            return instance;
        }
    }

    /**
     * private constructor for singleton class
     */
    private PlannerService() {
    }

    public Map<String, Object> saveListing(String type, String action, List<String> list_items) {
        Map<String, Object> return_map = new HashMap<>(Literal.SIX);
        try {
            /**
             * trim and distinct the list
             */
            list_items = list_items
                    .stream()
                    .map(String::trim)
                    .collect(Collectors.toList())
                    .stream()
                    .distinct()
                    .collect(Collectors.toList());
            /**
             * Get the existing doc from mongo
             */
            Document filter_doc = new Document().append(Literal.type, type);
            Document listing_doc = SharedMongoDao.getInstance().getMongoTemplate().findOne(new BasicQuery(filter_doc), Document.class, MongoDBConnectionInfo.listing_col);
            if (listing_doc == null || listing_doc.isEmpty()) {
                // first time
                listing_doc = new Document();
                listing_doc.append(Literal._id, Utility.UUID());
                listing_doc.append(Literal.type, type);
            }
            /**
             * if action is ADD
             */
            if (action.equalsIgnoreCase(Literal.ADD)) {
                if (listing_doc.get(Literal.items) == null) {
                    listing_doc.append(Literal.items, list_items);
                } else {
                    List<String> existing_list = (List<String>) listing_doc.get(Literal.items);
                    existing_list.addAll(list_items);
                    List<String> final_list = existing_list
                            .stream()
                            .map(String::trim)
                            .collect(Collectors.toList())
                            .stream()
                            .distinct()
                            .collect(Collectors.toList());
                    listing_doc.append(Literal.items, final_list);
                }
            }
            /**
             * if action is DELETE
             */
            if (action.equalsIgnoreCase(Literal.DELETE)) {
                if (listing_doc.get(Literal.items) == null) {
                    return_map.put(Literal.STATUS, Literal.ERROR);
                    return_map.put(Literal.MESSAGE, Literal.EMPTY_LIST);
                    return return_map;
                } else {
                    List<String> existing_list = (List<String>) listing_doc.get(Literal.items);
                    existing_list.removeAll(list_items);
                    List<String> final_list = existing_list
                            .stream()
                            .map(String::trim)
                            .collect(Collectors.toList())
                            .stream()
                            .distinct()
                            .collect(Collectors.toList());
                    listing_doc.append(Literal.items, final_list);
                }
            }
            if (SharedMongoDao.getInstance().upsertDocument(listing_doc, MongoDBConnectionInfo.listing_col)) {
                return_map.put(Literal.STATUS, Literal.SUCCESS);
                return_map.put(Literal.MESSAGE, Literal.DATA_UPDATED);
                return return_map;
            } else {
                return_map.put(Literal.STATUS, Literal.ERROR);
                return_map.put(Literal.MESSAGE, Literal.DATA_NOT_UPDATED);
                return return_map;
            }
        } catch (Exception e) {
            return_map.put(Literal.STATUS, Literal.ERROR);
            return_map.put(Literal.MESSAGE, Literal.SOMETHING_WENT_WRONG);
            return_map.put(Literal.EXCEPTION, e.getStackTrace());
            return return_map;
        }
    }

    public List<Document> getListing() {
        return SharedMongoDao.getInstance().getAllDocuments(MongoDBConnectionInfo.listing_col);
    }
}
