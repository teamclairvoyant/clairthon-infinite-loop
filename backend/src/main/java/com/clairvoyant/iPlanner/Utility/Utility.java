package com.clairvoyant.iPlanner.Utility;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Utility {

    public static final Set<String> LISTING_TYPE = new HashSet<>(Arrays.asList("JOB_TITLE", "DEPARTMENT", "BUSINESS_UNIT", "SKILLS"));

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
}
