package com.clairvoyant.iPlanner.API.Planner;

import java.util.Map;

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

    public Map<String, Object> saveListing(Map<String, Object> req_map) {
        return null;
    }
}
