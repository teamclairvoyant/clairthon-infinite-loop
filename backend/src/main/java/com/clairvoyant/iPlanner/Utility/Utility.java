package com.clairvoyant.iPlanner.Utility;

import java.util.*;

public class Utility {

    public static final Set<String> LISTING_TYPE = new HashSet<>(Arrays.asList("JOB_TITLE", "DEPARTMENT", "BUSINESS_UNIT", "SKILLS"));
    public static final Set<String> JOB_TITLE = new HashSet<>(Arrays.asList("Software Engineer", "Software Engineer II", "QA Engineer", "HR"));
    public static final Set<String> DEPARTMENT = new HashSet<>(Arrays.asList("Enterprise Engineering", "Business Development"));
    public static final Set<String> BUSINESS_UNIT = new HashSet<>(Arrays.asList("Enterprise and Data Services", "Sales"));
    public static final Set<String> SKILLS = new HashSet<>(Arrays.asList("Java", "Angular", "ReactJS", "Big Data", "Python"));
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

    private static long currentTimeMillis() {
        return System.currentTimeMillis();
    }

    public static String UUID() {
        return UUID.randomUUID().toString();
    }
}
