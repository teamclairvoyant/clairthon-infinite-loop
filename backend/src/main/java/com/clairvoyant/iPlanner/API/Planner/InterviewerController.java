package com.clairvoyant.iPlanner.API.Planner;

import com.clairvoyant.iPlanner.API.APIEndpoints;
import com.clairvoyant.iPlanner.Services.TokenService;
import com.clairvoyant.iPlanner.Utility.Literal;
import com.clairvoyant.iPlanner.Utility.Utility;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(APIEndpoints.INTERVIEWER)
public class InterviewerController {

    @Autowired
    HttpServletRequest request;

    @GetMapping()
    public String testInterviewer() {
        return Literal.SUCCESS + InterviewerController.class.getName();
    }

    /**
     *
     * @param req_map
     * {
     *     "id": "001",
     *     "employee_no": "P0102",
     *     "name": "Abhinov Gogoi",
     *     "email": "abhinav.gogoi@clairvoyantsoft.com",
     *     "phone": "2378263837",
     *     "experience": 2,
     *     "isInterviewer": true,
     *     "job_title": "Software Engineer",
     *     "department": "Enterprise Engineering",
     *     "business_unit": "Enterprise and Data Services",
     *     "location": "Pune",
     *     "skills": [
     *         "Angular",
     *         "Java"
     *     ]
     * }
     * @return
     */
    @PostMapping()
    public Map<String, Object> saveInterviewer(@RequestBody Map<String, Object> req_map) {
        Map<String, Object> return_map = new HashMap<>(Literal.SIX);
        try {
            /**
             * Check token
             */
            if (!TokenService.getInstance().validateToken(request)) {
                return_map.put(Literal.STATUS, Literal.ERROR);
                return_map.put(Literal.MESSAGE, Literal.TOKEN_INVALID);
                return return_map;
            }
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
                return_map.put(Literal.STATUS, Literal.ERROR);
                return_map.put(Literal.MESSAGE, Literal.EMP_NO_NULL);
                return_map.put(Literal.REQUEST_DATA, req_map);
                return return_map;
            }
            /**
             * check null name
             */
            if (Utility.isEmptyString(req_map.get(Literal.name))) {
                return_map.put(Literal.STATUS, Literal.ERROR);
                return_map.put(Literal.MESSAGE, Literal.NAME_NULL);
                return_map.put(Literal.REQUEST_DATA, req_map);
                return return_map;
            }
            /**
             * Check null email
             */
            if (Utility.isEmptyString(req_map.get(Literal.email))) {
                return_map.put(Literal.STATUS, Literal.ERROR);
                return_map.put(Literal.MESSAGE, Literal.EMAIL_NULL);
                return_map.put(Literal.REQUEST_DATA, req_map);
                return return_map;
            }
            /**
             * check valid email
             */
            if (!Utility.chkEmailRegex(req_map.get(Literal.email).toString())) {
                return_map.put(Literal.STATUS, Literal.ERROR);
                return_map.put(Literal.MESSAGE, Literal.EMAIL_INVALID);
                return_map.put(Literal.REQUEST_DATA, req_map);
                return return_map;
            }
            /**
             * not checking for null phone number, ie - allowing null phone no
             * but check valid phone, if present
             */
            if (!Utility.isEmptyString(req_map.get(Literal.phone)) && !Utility.chkPhoneRegex(req_map.get(Literal.phone).toString())) {
                return_map.put(Literal.STATUS, Literal.ERROR);
                return_map.put(Literal.MESSAGE, Literal.PHONE_INVALID);
                return_map.put(Literal.REQUEST_DATA, req_map);
                return return_map;
            }
            /**
             * Check null experience
             */
            if (Utility.isEmptyString(req_map.get(Literal.experience))) {
                return_map.put(Literal.STATUS, Literal.ERROR);
                return_map.put(Literal.MESSAGE, Literal.EXPERIENCE_NULL);
                return_map.put(Literal.REQUEST_DATA, req_map);
                return return_map;
            }
            /**
             * Check valid experience
             */
            if (!Utility.chkValidExperience(req_map.get(Literal.experience).toString())) {
                return_map.put(Literal.STATUS, Literal.ERROR);
                return_map.put(Literal.MESSAGE, Literal.EXPERIENCE_INVALID);
                return_map.put(Literal.REQUEST_DATA, req_map);
                return return_map;
            }
            /**
             * Check null isInterviewer flag
             */
            if (Utility.isEmptyString(req_map.get(Literal.isInterviewer))) {
                return_map.put(Literal.STATUS, Literal.ERROR);
                return_map.put(Literal.MESSAGE, Literal.IS_INTERVIEWER_NULL);
                return_map.put(Literal.REQUEST_DATA, req_map);
                return return_map;
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
                return_map.put(Literal.STATUS, Literal.ERROR);
                return_map.put(Literal.MESSAGE, Literal.IS_INTERVIEWER_INVALID);
                return_map.put(Literal.REQUEST_DATA, req_map);
                return return_map;
            }
            /**
             * fetch the existing listings from mongodb
             */
            List<Document> all_listing = PlannerService.getInstance().getListing();
            if(all_listing == null || all_listing.isEmpty()) {
                return_map.put(Literal.STATUS, Literal.ERROR);
                return_map.put(Literal.MESSAGE, Literal.LISTING_EMPTY);
                return_map.put(Literal.REQUEST_DATA, req_map);
                return return_map;
            }
            /**
             * check for job_title listing
             */
            if(!Utility.isEmptyString(req_map.get(Literal.job_title))) {

                Optional<Document> existing_job_title_document = all_listing.stream()
                        .filter(list -> list.get(Literal.type).toString().equalsIgnoreCase(Literal.JOB_TITLE))
                        .findFirst();
                /**
                 * If job title listing is not found in mongo
                 */
                if(!existing_job_title_document.isPresent()) {
                    return_map.put(Literal.STATUS, Literal.ERROR);
                    return_map.put(Literal.MESSAGE, Literal.JOB_TITLE_LIST_EMPTY);
                    return_map.put(Literal.REQUEST_DATA, req_map);
                    return return_map;
                } else {
                    List<String> existing_job_titles = (List<String>) existing_job_title_document.get().get(Literal.items);
                    if(!existing_job_titles.contains(req_map.get(Literal.job_title).toString())) {
                        return_map.put(Literal.STATUS, Literal.ERROR);
                        return_map.put(Literal.MESSAGE, Literal.INVALID_JOB_TITLE);
                        return_map.put(Literal.REQUEST_DATA, req_map);
                        return return_map;
                    }
                }
            }
            /**
             * check for department listing
             */
            if(!Utility.isEmptyString(req_map.get(Literal.department))) {

                Optional<Document> existing_department_document = all_listing.stream()
                        .filter(list -> list.get(Literal.type).toString().equalsIgnoreCase(Literal.DEPARTMENT))
                        .findFirst();
                /**
                 * If department listing is not found in mongo
                 */
                if(!existing_department_document.isPresent()) {
                    return_map.put(Literal.STATUS, Literal.ERROR);
                    return_map.put(Literal.MESSAGE, Literal.DEPARTMENT_LIST_EMPTY);
                    return_map.put(Literal.REQUEST_DATA, req_map);
                    return return_map;
                } else {
                    List<String> existing_departments = (List<String>) existing_department_document.get().get(Literal.items);
                    if(!existing_departments.contains(req_map.get(Literal.department).toString())) {
                        return_map.put(Literal.STATUS, Literal.ERROR);
                        return_map.put(Literal.MESSAGE, Literal.INVALID_DEPARTMENT);
                        return_map.put(Literal.REQUEST_DATA, req_map);
                        return return_map;
                    }
                }
            }
            /**
             * check for business_unit listing
             */
            if(!Utility.isEmptyString(req_map.get(Literal.business_unit))) {

                Optional<Document> existing_business_unit_document = all_listing.stream()
                        .filter(list -> list.get(Literal.type).toString().equalsIgnoreCase(Literal.BUSINESS_UNIT))
                        .findFirst();
                /**
                 * If business_unit listing is not found in mongo
                 */
                if(!existing_business_unit_document.isPresent()) {
                    return_map.put(Literal.STATUS, Literal.ERROR);
                    return_map.put(Literal.MESSAGE, Literal.BUSINESS_UNIT_LIST_EMPTY);
                    return_map.put(Literal.REQUEST_DATA, req_map);
                    return return_map;
                } else {
                    List<String> existing_business_units = (List<String>) existing_business_unit_document.get().get(Literal.items);
                    if(!existing_business_units.contains(req_map.get(Literal.business_unit).toString())) {
                        return_map.put(Literal.STATUS, Literal.ERROR);
                        return_map.put(Literal.MESSAGE, Literal.INVALID_BUSINESS_UNIT);
                        return_map.put(Literal.REQUEST_DATA, req_map);
                        return return_map;
                    }
                }
            }
            /**
             * check for location listing
             */
            if(!Utility.isEmptyString(req_map.get(Literal.location))) {

                Optional<Document> existing_location_document = all_listing.stream()
                        .filter(list -> list.get(Literal.type).toString().equalsIgnoreCase(Literal.LOCATION))
                        .findFirst();
                /**
                 * If location listing is not found in mongo
                 */
                if(!existing_location_document.isPresent()) {
                    return_map.put(Literal.STATUS, Literal.ERROR);
                    return_map.put(Literal.MESSAGE, Literal.LOCATION_LIST_EMPTY);
                    return_map.put(Literal.REQUEST_DATA, req_map);
                    return return_map;
                } else {
                    List<String> existing_locations = (List<String>) existing_location_document.get().get(Literal.items);
                    if(!existing_locations.contains(req_map.get(Literal.location).toString())) {
                        return_map.put(Literal.STATUS, Literal.ERROR);
                        return_map.put(Literal.MESSAGE, Literal.INVALID_LOCATION);
                        return_map.put(Literal.REQUEST_DATA, req_map);
                        return return_map;
                    }
                }
            }
            /**
             * check for skills listing
             */
            if(!Utility.isEmptyString(req_map.get(Literal.skills))) {

                Optional<Document> existing_skills_document = all_listing.stream()
                        .filter(list -> list.get(Literal.type).toString().equalsIgnoreCase(Literal.SKILLS))
                        .findFirst();
                /**
                 * If skills listing is not found in mongo
                 */
                if(!existing_skills_document.isPresent()) {
                    return_map.put(Literal.STATUS, Literal.ERROR);
                    return_map.put(Literal.MESSAGE, Literal.SKILLS_LIST_EMPTY);
                    return_map.put(Literal.REQUEST_DATA, req_map);
                    return return_map;
                } else {
                    List<String> existing_skills = (List<String>) existing_skills_document.get().get(Literal.items);
                    /**
                     * try to parse the employee_skills from the request
                     */
                    try{
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
                            if(!existing_skills.contains(skill)) {
                                return_map.put(Literal.STATUS, Literal.ERROR);
                                return_map.put(Literal.MESSAGE, Literal.INVALID_SKILL+skill);
                                return_map.put(Literal.REQUEST_DATA, req_map);
                                return return_map;
                            }
                        }
                    } catch (ClassCastException e) {
                        return_map.put(Literal.STATUS, Literal.ERROR);
                        return_map.put(Literal.MESSAGE, Literal.SKILLS_LIST_PARSING_FAIL);
                        return_map.put(Literal.REQUEST_DATA, req_map);
                        return return_map;
                    }
                }
            }


            return PlannerService.getInstance()
                    .saveInterviewer(req_map);

        } catch (Exception e) {
            return_map.put(Literal.STATUS, Literal.ERROR);
            return_map.put(Literal.MESSAGE, Literal.SOMETHING_WENT_WRONG);
            return_map.put(Literal.EXCEPTION, e.getLocalizedMessage());
            return return_map;
        }
    }
}
