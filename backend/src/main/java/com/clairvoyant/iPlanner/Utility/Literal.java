package com.clairvoyant.iPlanner.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * GLOBAL Constants
 */
public class Literal {
    public static final int SIX = 6;
    public static final String iPlanner = "iPlanner";
    public static final String STATUS = "STATUS";
    public static final String ERROR = "Error";
    public static final String MESSAGE = "MESSAGE";
    public static final String TOKEN_INVALID = "Invalid Token";
    public static final String login_id = "login_id";
    public static final String LOGIN_ID_NULL = "Kindly provide login id";
    public static final String REQUEST_DATA = "REQUEST_DATA";
    public static final String password = "password";
    public static final String NULL_PASSWORD = "Kindly provide password";
    public static final String SUCCESS = "Success";
    public static final String LOGIN_ID = "LOGIN_ID";
    public static final String TOKEN = "TOKEN";
    public static final String SOMETHING_WENT_WRONG = "Something went wrong";
    public static final String EXCEPTION = "EXCEPTION";
    public static final String INCORRECT_CREDENTIALS = "Incorrect login id or password";
    public static final String AUTH_LINK = "AUTH_LINK";
    public static final boolean FALSE = false;
    public static final boolean TRUE = true;
    public static final String ADMIN = "ADMIN";
    public static final String admin = "admin";
    public static final String EMPTY_STRING = "";
    public static final String type = "type";
    public static final String TYPE_NULL = "Kindly provide a listing type - JOB_TITLE, DEPARTMENT, BUSINESS_UNIT, SKILLS";
    public static final String TYPE_INVALID = "Invalid listing type from the list (JOB_TITLE, DEPARTMENT, BUSINESS_UNIT, SKILLS)";
    public static final String action = "action";
    public static final String ACTION_NULL = "Kindly provide action";
    public static final String ADD = "ADD";
    public static final String DELETE = "DELETE";
    public static final String items = "items";
    public static final String ACTION_INVALID = "Kindly provide a valid action - ADD or DELETE";
    public static final String ITEMS_NULL = "Kindly provide items";
    public static final List<Map<String, Object>> EMPTY_ARRAYLIST = new ArrayList<>();
    public static final Map<String, Object> EMPTY_HASHMAP = new HashMap<>();
    public static final String ASC = "ASC";
    public static final String createdDate = "createdDate";
    public static final String createdDateMs = "createdDateMs";
    public static final String DATA_UPDATED = "Data updated successfully";
    public static final String DATA_NOT_UPDATED = "Data not updated";
    public static final String _id = "_id";
    public static final String EMPTY_LIST = "The listing is already empty";
    public static final String DATA = "DATA";
    public static final String updatedDate = "updatedDate";
    public static final String updatedDateMs = "updatedDateMs";
    public static final String id = "id";
    public static final String employee_no = "employee_no";
    public static final String EMP_NO_NULL = "Kindly provide employee_no";
    public static final String name = "name";
    public static final String NAME_NULL = "Kindly provide name";
    public static final String email = "email";
    public static final String EMAIL_NULL = "Kindly provide valid email";
    public static final String EmailRegEx = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
    public static final String PhoneRegEx = "^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$";
    public static final String EMAIL_INVALID = "Kindly provide a valid email";
    public static final String phone = "phone";
    public static final String PHONE_INVALID = "Kindly provide valid phone number";
    public static final String experience = "experience";
    public static final String EXPERIENCE_NULL = "Kindly provide experience";
    public static final String EXPERIENCE_INVALID = "Kindly provide valid experience";
    public static final String IS_INTERVIEWER_NULL = "Kindly provide isInterviewer flag";
    public static final String isInterviewer = "isInterviewer";
    public static final String IS_INTERVIEWER_INVALID = "Kindly provide a valid isInterviewer - either true or false";
    public static final String LISTING_EMPTY = "No Listing document present, please add a list, or populate dummy listing";

    public static final String job_title = "job_title";
    public static final String JOB_TITLE = "JOB_TITLE";
    public static final String JOB_TITLE_LIST_EMPTY = "There are no job titles present in listing, please add to listing first";
    public static final String INVALID_JOB_TITLE = "Kindly provide a valid job title from the existing job title list";

    public static final String department = "department";
    public static final String DEPARTMENT = "DEPARTMENT";
    public static final String DEPARTMENT_LIST_EMPTY = "There are no departments present in listing, please add to listing first";
    public static final String INVALID_DEPARTMENT = "Kindly provide a valid department from the existing department list";

    public static final String business_unit = "business_unit";
    public static final String BUSINESS_UNIT = "BUSINESS_UNIT";
    public static final String BUSINESS_UNIT_LIST_EMPTY = "There are no business units present in listing, please add to listing first";
    public static final String INVALID_BUSINESS_UNIT = "Kindly provide a valid business unit from the existing business unit list";

    public static final String location = "location";
    public static final String LOCATION = "LOCATION";
    public static final String LOCATION_LIST_EMPTY = "There are no locations present in listing, please add to listing first";
    public static final String INVALID_LOCATION = "Kindly provide a valid location from the existing location list";

    public static final String SKILLS_LIST_PARSING_FAIL = "Given skills list could not be parsed. Please provide a valid list of Strings";
    public static final String skills = "skills";
    public static final String INVALID_SKILL = "Kindly provide a valid skill from the existing skills list. Invalid skill :: ";
    public static final String SKILLS = "SKILLS";
    public static final String SKILLS_LIST_EMPTY = "There are no skills present in listing, please add to listing first";
    public static final String archived = "archived";
}
