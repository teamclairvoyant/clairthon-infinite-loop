package com.clairvoyant.iPlanner.Utility;

import java.util.*;

public class Utility {

    public static final List<String> LISTING_TYPE = new ArrayList<>(Arrays.asList("JOB_TITLE", "DEPARTMENT", "BUSINESS_UNIT", "SKILLS", "LOCATION"));
    public static final List<String> JOB_TITLE = new ArrayList<>(Arrays.asList("Software Engineer", "Software Engineer II", "QA Engineer", "HR"));
    public static final List<String> DEPARTMENT = new ArrayList<>(Arrays.asList("Enterprise Engineering", "Business Development"));
    public static final List<String> BUSINESS_UNIT = new ArrayList<>(Arrays.asList("Enterprise and Data Services", "Sales"));
    public static final List<String> SKILLS = new ArrayList<>(Arrays.asList("Java", "Angular", "ReactJS", "Big Data", "Python"));
    public static final List<String> LOCATION = new ArrayList<>(Arrays.asList("Pune", "Hyderabad", "US", "Canada"));
    /**
     * check null object or empty string
     *
     * @param obj_data
     * @return
     */
    public static boolean isEmptyString(Object obj_data) {
        if (obj_data == null || obj_data.toString().trim().equals(Literal.EMPTY_STRING)) {
            return Literal.TRUE;
        }
        return Literal.FALSE;
    }

    public static boolean chkListingType(String type) {
        return LISTING_TYPE.contains(type);
    }

    public static boolean chkValidAction(String action) {
        return action.equalsIgnoreCase(Literal.ADD) || action.equalsIgnoreCase(Literal.DELETE);
    }

    public static Date now() {
        return new Date(Utility.currentTimeMillis());
    }

    /**
     * Will be useful if we want to shift application time
     * @return
     */
    public static long currentTimeMillis() {
        return System.currentTimeMillis();
    }

    public static String UUID() {
        return UUID.randomUUID().toString();
    }
}
