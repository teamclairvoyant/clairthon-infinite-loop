package com.clairvoyant.iPlanner.Utility;

import org.springframework.web.multipart.MultipartFile;

import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Utility {

    public static final List<String> LISTING_TYPE = new ArrayList<>(Arrays.asList("JOB_TITLE", "DEPARTMENT", "BUSINESS_UNIT", "SKILLS", "LOCATION", "PROJECT"));
    public static final List<String> JOB_TITLE_LIST = new ArrayList<>(Arrays.asList("Software Engineer", "Software Engineer II", "QA Engineer", "HR"));
    public static final List<String> DEPARTMENT_LIST = new ArrayList<>(Arrays.asList("Enterprise Engineering", "Business Development"));
    public static final List<String> BUSINESS_UNIT_LIST = new ArrayList<>(Arrays.asList("Enterprise and Data Services", "Sales"));
    public static final List<String> SKILLS_LIST = new ArrayList<>(Arrays.asList("Java", "Angular", "ReactJS", "Big Data", "Python"));
    public static final List<String> LOCATION_LIST = new ArrayList<>(Arrays.asList("Pune", "Hyderabad", "US", "Canada"));
    public static final List<String> PROJECT_LIST = new ArrayList<>(Arrays.asList("Kogni", "CLA"));

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

    /**
     * @return true if match Email RegEx else False
     */
    public static boolean chkEmailRegex(String data) {
        return data.matches(Literal.EmailRegEx);
    }

    /**
     * @return true if match Email RegEx else False
     */
    public static boolean chkPhoneRegex(String data) {
        return data.matches(Literal.PhoneRegEx);
    }

    public static Date now() {
        return new Date(Utility.currentTimeMillis());
    }

    /**
     * Will be useful if we want to shift application time
     *
     * @return
     */
    public static long currentTimeMillis() {
        return System.currentTimeMillis();
    }

    public static String UUID() {
        return UUID.randomUUID().toString();
    }

    /**
     * @return true if experience is between 0 and 100 integer range
     */
    public static boolean chkValidExperience(String str) {
        try {
            int experience = Integer.parseInt(str);
            return experience > -1 && experience < 100;
        } catch (NumberFormatException e) {
            return Literal.FALSE;
        }
    }

    public static boolean saveCredentialsFile(MultipartFile file) {
        try {
            Path filepath = Paths.get("src/main/resources", "OAuth.json");
            try (OutputStream os = Files.newOutputStream(filepath)) {
                os.write(file.getBytes());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
