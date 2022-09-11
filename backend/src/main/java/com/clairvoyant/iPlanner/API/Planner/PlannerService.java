package com.clairvoyant.iPlanner.API.Planner;

import com.clairvoyant.iPlanner.Exceptions.RequestValidationException;
import com.clairvoyant.iPlanner.Shared.MainMongoDao;
import com.clairvoyant.iPlanner.Utility.Literal;
import com.clairvoyant.iPlanner.Utility.MongoDBConnectionInfo;
import com.clairvoyant.iPlanner.Utility.Utility;
import org.bson.Document;
import org.springframework.data.mongodb.core.query.BasicQuery;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class PlannerService {

    private static PlannerService instance;

    /**
     * Singleton class Lazy initialization with Double check locking
     *
     * @return PlannerService
     */
    public static PlannerService getInstance() {
        if (instance == null) {
            synchronized (PlannerService.class) {
                instance = new PlannerService();
            }
        }
        return instance;
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
            Document listing_doc = MainMongoDao.getInstance().getMongoTemplate().findOne(new BasicQuery(filter_doc), Document.class, MongoDBConnectionInfo.listing_col);
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
            if (MainMongoDao.getInstance().upsertDocument(listing_doc, MongoDBConnectionInfo.listing_col)) {
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
        return MainMongoDao.getInstance().getAllDocuments(MongoDBConnectionInfo.listing_col);
    }

    public Map<String, Object> saveInterviewer(Map<String, Object> interviewer_map) {
        Map<String, Object> return_map = new HashMap<>(Literal.SIX);
        try {
            Document interviewer_document = new Document();
            /**
             * set the fields
             */
            interviewer_document.put(Literal._id, interviewer_map.get(Literal.id));
            interviewer_document.put(Literal.employee_no, interviewer_map.get(Literal.employee_no));
            interviewer_document.put(Literal.name, interviewer_map.get(Literal.name));
            interviewer_document.put(Literal.email, interviewer_map.get(Literal.email));
            interviewer_document.put(Literal.phone, interviewer_map.get(Literal.phone));
            interviewer_document.put(Literal.experience, interviewer_map.get(Literal.experience));
            interviewer_document.put(Literal.isInterviewer, interviewer_map.get(Literal.isInterviewer));
            interviewer_document.put(Literal.job_title, interviewer_map.get(Literal.job_title));
            interviewer_document.put(Literal.department, interviewer_map.get(Literal.department));
            interviewer_document.put(Literal.business_unit, interviewer_map.get(Literal.business_unit));
            interviewer_document.put(Literal.location, interviewer_map.get(Literal.location));
            interviewer_document.put(Literal.skills, interviewer_map.get(Literal.skills));
            /**
             * while create/update employee set deleted/archived status to false
             */
            interviewer_document.put(Literal.archived, Literal.FALSE);
            if (MainMongoDao.getInstance().upsertDocument(interviewer_document, MongoDBConnectionInfo.interviewer_col)) {
                return_map.put(Literal.STATUS, Literal.SUCCESS);
                return_map.put(Literal.MESSAGE, Literal.DATA_UPDATED);
                return return_map;
            } else {
                return_map.put(Literal.STATUS, Literal.SUCCESS);
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

    public void validateSaveInterviewer(Map<String, Object> req_map) throws RequestValidationException {

        /**
         * check null id, means newly add the interviewer / create employee, else update
         */
        if (Utility.isEmptyString(req_map.get(Literal.id))) {
            req_map.put(Literal.id, Utility.UUID());
        }
        /**
         * check null employee_no
         */
        if (Utility.isEmptyString(req_map.get(Literal.employee_no))) {
            throw new RequestValidationException(Literal.EMP_NO_NULL);
        }
        /**
         * check null name
         */
        if (Utility.isEmptyString(req_map.get(Literal.name))) {
            throw new RequestValidationException(Literal.NAME_NULL);
        }
        /**
         * Check null email
         */
        if (Utility.isEmptyString(req_map.get(Literal.email))) {
            throw new RequestValidationException(Literal.EMAIL_NULL);
        }
        /**
         * check valid email
         */
        if (!Utility.chkEmailRegex(req_map.get(Literal.email).toString())) {
            throw new RequestValidationException(Literal.EMAIL_INVALID);
        }
        /**
         * not checking for null phone number, ie - allowing null phone no
         * but check valid phone, if present
         */
        if (!Utility.isEmptyString(req_map.get(Literal.phone)) && !Utility.chkPhoneRegex(req_map.get(Literal.phone).toString())) {
            throw new RequestValidationException(Literal.PHONE_INVALID);
        }
        /**
         * Check null experience
         */
        if (Utility.isEmptyString(req_map.get(Literal.experience))) {
            throw new RequestValidationException(Literal.EXPERIENCE_NULL);
        }
        /**
         * Check valid experience
         */
        if (!Utility.chkValidExperience(req_map.get(Literal.experience).toString())) {
            throw new RequestValidationException(Literal.EXPERIENCE_INVALID);
        }
        /**
         * Check null isInterviewer flag
         */
        if (Utility.isEmptyString(req_map.get(Literal.isInterviewer))) {
            throw new RequestValidationException(Literal.IS_INTERVIEWER_NULL);
        }
        /**
         * Check valid isInterviewer flag
         */
        try {
            // parse string into boolean
            boolean isInterviewer = Boolean.parseBoolean(req_map.get(Literal.isInterviewer).toString());
            // insert boolean into the key
            req_map.put(Literal.isInterviewer, isInterviewer);
        } catch (Exception e) {
            throw new RequestValidationException(Literal.IS_INTERVIEWER_INVALID);
        }
        /**
         * fetch the existing listings from mongodb
         */
        List<Document> all_listing = PlannerService.getInstance().getListing();
        if (all_listing == null || all_listing.isEmpty()) {
            throw new RequestValidationException(Literal.LISTING_EMPTY);
        }
        /**
         * check for job_title listing
         */
        if (!Utility.isEmptyString(req_map.get(Literal.job_title))) {

            Optional<Document> existing_job_title_document = all_listing.stream()
                    .filter(list -> list.get(Literal.type).toString().equalsIgnoreCase(Literal.JOB_TITLE))
                    .findFirst();
            /**
             * If job title listing is not found in mongo
             */
            if (!existing_job_title_document.isPresent()) {
                throw new RequestValidationException(Literal.JOB_TITLE_LIST_EMPTY);
            } else {
                List<String> existing_job_titles = (List<String>) existing_job_title_document.get().get(Literal.items);
                if (!existing_job_titles.contains(req_map.get(Literal.job_title).toString())) {
                    throw new RequestValidationException(Literal.INVALID_JOB_TITLE);
                }
            }
        }
        /**
         * check for department listing
         */
        if (!Utility.isEmptyString(req_map.get(Literal.department))) {

            Optional<Document> existing_department_document = all_listing.stream()
                    .filter(list -> list.get(Literal.type).toString().equalsIgnoreCase(Literal.DEPARTMENT))
                    .findFirst();
            /**
             * If department listing is not found in mongo
             */
            if (!existing_department_document.isPresent()) {
                throw new RequestValidationException(Literal.DEPARTMENT_LIST_EMPTY);
            } else {
                List<String> existing_departments = (List<String>) existing_department_document.get().get(Literal.items);
                if (!existing_departments.contains(req_map.get(Literal.department).toString())) {
                    throw new RequestValidationException(Literal.INVALID_DEPARTMENT);
                }
            }
        }
        /**
         * check for business_unit listing
         */
        if (!Utility.isEmptyString(req_map.get(Literal.business_unit))) {

            Optional<Document> existing_business_unit_document = all_listing.stream()
                    .filter(list -> list.get(Literal.type).toString().equalsIgnoreCase(Literal.BUSINESS_UNIT))
                    .findFirst();
            /**
             * If business_unit listing is not found in mongo
             */
            if (!existing_business_unit_document.isPresent()) {
                throw new RequestValidationException(Literal.BUSINESS_UNIT_LIST_EMPTY);
            } else {
                List<String> existing_business_units = (List<String>) existing_business_unit_document.get().get(Literal.items);
                if (!existing_business_units.contains(req_map.get(Literal.business_unit).toString())) {
                    throw new RequestValidationException(Literal.INVALID_BUSINESS_UNIT);
                }
            }
        }
        /**
         * check for location listing
         */
        if (!Utility.isEmptyString(req_map.get(Literal.location))) {

            Optional<Document> existing_location_document = all_listing.stream()
                    .filter(list -> list.get(Literal.type).toString().equalsIgnoreCase(Literal.LOCATION))
                    .findFirst();
            /**
             * If location listing is not found in mongo
             */
            if (!existing_location_document.isPresent()) {
                throw new RequestValidationException(Literal.LOCATION_LIST_EMPTY);
            } else {
                List<String> existing_locations = (List<String>) existing_location_document.get().get(Literal.items);
                if (!existing_locations.contains(req_map.get(Literal.location).toString())) {
                    throw new RequestValidationException(Literal.INVALID_LOCATION);
                }
            }
        }
        /**
         * check for skills listing
         */
        if (!Utility.isEmptyString(req_map.get(Literal.skills))) {

            Optional<Document> existing_skills_document = all_listing.stream()
                    .filter(list -> list.get(Literal.type).toString().equalsIgnoreCase(Literal.SKILLS))
                    .findFirst();
            /**
             * If skills listing is not found in mongo
             */
            if (!existing_skills_document.isPresent()) {
                throw new RequestValidationException(Literal.SKILLS_LIST_EMPTY);
            } else {
                List<String> existing_skills = (List<String>) existing_skills_document.get().get(Literal.items);
                /**
                 * try to parse the employee_skills from the request
                 */
                try {
                    List<String> employee_skills = (List<String>) req_map.get(Literal.skills);
                    /**
                     * trim and distinct the list
                     */
                    employee_skills = employee_skills
                            .stream()
                            .map(String::trim)
                            .collect(Collectors.toList())
                            .stream()
                            .distinct()
                            .collect(Collectors.toList());
                    /**
                     * for each skill verify if its present in existing skills listing
                     */
                    for (String skill : employee_skills) {
                        if (!existing_skills.contains(skill)) {
                            throw new RequestValidationException(Literal.INVALID_SKILL + skill);
                        }
                    }
                } catch (ClassCastException e) {
                    throw new RequestValidationException(Literal.SKILLS_LIST_PARSING_FAIL);
                }
            }
        }
    }

    public void validateSaveListing(Map<String, Object> req_map) throws RequestValidationException {
        /**
         * check null type
         */
        if (Utility.isEmptyString(req_map.get(Literal.type))) {
            throw new RequestValidationException(Literal.TYPE_NULL);
        }
        /**
         * check invalid type
         */
        if (!Utility.chkListingType(req_map.get(Literal.type).toString())) {
            throw new RequestValidationException(Literal.TYPE_INVALID);
        }
        /**
         * check null Action
         */
        if (Utility.isEmptyString(req_map.get(Literal.action))) {
            throw new RequestValidationException(Literal.ACTION_NULL);
        }
        /**
         * check valid Action
         */
        if (!Utility.chkValidAction(req_map.get(Literal.action).toString())) {
            throw new RequestValidationException(Literal.ACTION_INVALID);
        }
        /**
         * Check null items
         */
        if (req_map.get(Literal.items) == null) {
            throw new RequestValidationException(Literal.ITEMS_NULL);
        }

    }

    public boolean populateDummyListing() {
        /**
         * For each listing_type call saveListing
         */
        PlannerService.getInstance().saveListing(Literal.JOB_TITLE, Literal.ADD, Utility.JOB_TITLE_LIST);
        PlannerService.getInstance().saveListing(Literal.DEPARTMENT, Literal.ADD, Utility.DEPARTMENT_LIST);
        PlannerService.getInstance().saveListing(Literal.BUSINESS_UNIT, Literal.ADD, Utility.BUSINESS_UNIT_LIST);
        PlannerService.getInstance().saveListing(Literal.SKILLS, Literal.ADD, Utility.SKILLS_LIST);
        PlannerService.getInstance().saveListing(Literal.LOCATION, Literal.ADD, Utility.LOCATION_LIST);
        return Literal.TRUE;
    }

    public List<Map<String, Object>>  getInterviewers(String id) {

        Document filter_doc = new Document().append(Literal.archived, Literal.FALSE);
        Document search_doc = new Document().append(Literal.archived, Literal.ZERO);

        if(!id.equalsIgnoreCase(Literal.ALL)) {
            /**
             * append the id to search
             */
            filter_doc.append(Literal._id, id);
        }
        return MainMongoDao.getInstance().getDocuments(new BasicQuery(filter_doc, search_doc),MongoDBConnectionInfo.interviewer_col);
    }

    public boolean deleteInterviewer(String id) {
        Document filter_doc = new Document().append(Literal._id, id);
        return  MainMongoDao.getInstance()
                .deleteDocuments(new BasicQuery(filter_doc), MongoDBConnectionInfo.interviewer_col);
    }
}
