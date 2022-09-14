package com.clairvoyant.iPlanner.API.Planner;

import com.clairvoyant.iPlanner.API.APIEndpoints;
import com.clairvoyant.iPlanner.Exceptions.RequestValidationException;
import com.clairvoyant.iPlanner.Exceptions.TokenValidationException;
import com.clairvoyant.iPlanner.Services.TokenService;
import com.clairvoyant.iPlanner.Utility.Literal;
import com.clairvoyant.iPlanner.Utility.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping(APIEndpoints.INTERVIEWER)
public class InterviewerController {

    @Autowired
    HttpServletRequest request;

    @GetMapping("/test")
    public String testInterviewer() {
        return Literal.SUCCESS + InterviewerController.class.getName();
    }

    /**
     * @param req_map <br>
     *               REQUEST METHOD - POST <br>
     * {
     *     "id": "eba6ee4d-874f-4289-aaad-d47bf796290b",
     *     "employee_no": "P0103",
     *     "name": "Abhinov Gogoi",
     *     "email": "abhin@gmail.com",
     *     "phone": "+91 8136073065",
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
     * 				  <br>
     *                <table border=1px>
     *                <tr>
     *                <th>Key</th>
     *                <th>Sample Data</th>
     *                <th>Data Type</th>
     *                <th>Constraint</th>
     *                <th>Description</th>
     *                </tr>
     *                <tr>
     *                <td>id</td>
     *                <td>eba6ee4d-874f-4289-aaad-d47bf796290b</td>
     *                <td>String</td>
     *                <td>Mandatory if Create <br> Optional if Update </td>
     *                <td>id of the interviewer</td>
     *                </tr>
     *                <tr>
     *                <td>employee_no</td>
     *                <td>P0103</td>
     *                <td>String</td>
     *                <td>Mandatory</td>
     *                <td>Emp No as per KEKA</td>
     *                </tr>
     *                <tr>
     *                <td>name</td>
     *                <td>Abhinov Gogoi</td>
     *                <td>String</td>
     *                <td>Mandatory</td>
     *                <td>Employee name</td>
     *                </tr>
     *                <tr>
     *                <td>email</td>
     *                <td>abhinav@clairvoyantsoft.com</td>
     *                <td>String</td>
     *                <td>Mandatory</td>
     *                <td>Employee company email</td>
     *                </tr>
     *                <tr>
     *                <td>phone</td>
     *                <td>+91 7489286478</td>
     *                <td>String</td>
     *                <td>Optional</td>
     *                <td>Employee contact no</td>
     *                </tr>
     *                <tr>
     *                <td>experience</td>
     *                <td>2</td>
     *                <td>Integer</td>
     *                <td>Mandatory</td>
     *                <td>Employee experience in years</td>
     *                </tr>
     *                <tr>
     *                <td>isInterviewer</td>
     *                <td>true</td>
     *                <td>String</td>
     *                <td>Mandatory</td>
     *                <td>Whether this Employee is eligible to take interviews</td>
     *                </tr>
     *                <tr>
     *                <td>job_title</td>
     *                <td>Software Engineer II</td>
     *                <td>String</td>
     *                <td>Optional</td>
     *                <td>Employee Job title as per listing</td>
     *                </tr>
     *                <tr>
     *                <td>department</td>
     *                <td>Enterprise Engineering</td>
     *                <td>String</td>
     *                <td>Optional</td>
     *                <td>Employee department as per listing</td>
     *                </tr>
     *                <tr>
     *                <td>business_unit</td>
     *                <td>Enterprise and Data Services</td>
     *                <td>String</td>
     *                <td>Optional</td>
     *                <td>Employee business unit as per listing</td>
     *                </tr>
     *                <tr>
     *                <td>location</td>
     *                <td>Pune</td>
     *                <td>String</td>
     *                <td>Optional</td>
     *                <td>Employee location as per listing</td>
     *                </tr>
     *                <tr>
     *                <td>skills</td>
     *                <td>["Angular","Java"]</td>
     *                <td>String</td>
     *                <td>Optional</td>
     *                <td>Employee skills as per listing</td>
     *                </tr>
     *                </table>
     * {@code @apiNote} headers <br>
     *                <table border=1px>
     *                <tr>
     *                <th>Header Key</th>
     *                <th>Sample Data</th>
     *                <th>Data Type</th>
     *                <th>Constraint</th>
     *                <th>Description</th>
     *                </tr>
     *                <tr>
     *                <td>TOKEN</td>
     *                <td>G6G57HfD49HPl</td>
     *                <td>String</td>
     *                <td>Mandatory</td>
     *                <td>user token</td>
     *                </tr>
     *                <tr>
     *                <td>LOGIN_ID</td>
     *                <td>admin</td>
     *                <td>String</td>
     *                <td>Mandatory</td>
     *                <td>login id / username</td>
     *                </tr>
     *                </table>
     *                <br>
     * @see <b>Functionality: </b> This API is used to save interviewer (create/update) <br>
     * @return <b>SUCCESS MESSAGE:</b> <br>
     *         {"MESSAGE":"Data updated successfully","STATUS":"Success"} <br>
     *         <b>ERROR MESSAGE:</b> <br>
     *         {"EXCEPTION":"Invalid Token","MESSAGE":"TOKEN missing in request header","STATUS":"Error"} <br>
     *         {"EXCEPTION":"Invalid Token","MESSAGE":"Invalid TOKEN for given LOGIN_ID :: admin","STATUS":"Error"} <br>
     */
    @PostMapping()
    public Map<String, Object> saveInterviewer(@RequestBody Map<String, Object> req_map) {
        Map<String, Object> return_map = new HashMap<>(Literal.SIX);
        try {
            /**
             * Check token
             */
            TokenService.getInstance().validateToken(request);

            /**
             * validate the request data
             */
            PlannerService.getInstance().validateSaveInterviewer(req_map);
            /**
             * save the valid data
             */
            return PlannerService.getInstance()
                    .saveInterviewer(req_map);

        } catch (TokenValidationException e) {
            return_map.put(Literal.STATUS, Literal.ERROR);
            return_map.put(Literal.MESSAGE, e.getMessage());
            return_map.put(Literal.EXCEPTION, Literal.TOKEN_INVALID);
            return return_map;
        } catch (RequestValidationException e) {
            return_map.put(Literal.STATUS, Literal.ERROR);
            return_map.put(Literal.MESSAGE, e.getMessage());
            return_map.put(Literal.EXCEPTION, Literal.REQUEST_VALIDATION_FAILED);
            return return_map;
        } catch (Exception e) {
            return_map.put(Literal.STATUS, Literal.ERROR);
            return_map.put(Literal.MESSAGE, e.getMessage());
            return_map.put(Literal.EXCEPTION, Literal.SOMETHING_WENT_WRONG);
            return return_map;
        }
    }


    /**
     * {@code @apiNote}
     *                REQUEST METHOD - GET <br>
     * 				  <br>
     *                <table border=1px>
     *                <tr>
     *                <th>Key</th>
     *                <th>Sample Data</th>
     *                <th>Data Type</th>
     *                <th>Constraint</th>
     *                <th>Description</th>
     *                </tr>
     *                <tr>
     *                <td>id</td>
     *                <td>eba6ee4d-874f-4289-aaad-d47bf796290b or ALL</td>
     *                <td>String</td>
     *                <td>Optional</td>
     *                <td>id of the interviewer or ALL</td>
     *                </tr>
     *
     *                <table border=1px>
     *                <tr>
     *                <th>Header Key</th>
     *                <th>Sample Data</th>
     *                <th>Data Type</th>
     *                <th>Constraint</th>
     *                <th>Description</th>
     *                </tr>
     *                <tr>
     *                <td>TOKEN</td>
     *                <td>G6G57HfD49HPl</td>
     *                <td>String</td>
     *                <td>Mandatory</td>
     *                <td>user token</td>
     *                </tr>
     *                <tr>
     *                <td>LOGIN_ID</td>
     *                <td>admin</td>
     *                <td>String</td>
     *                <td>Mandatory</td>
     *                <td>login id / username</td>
     *                </tr>
     *                </table>
     *                <br>
     * @see <b>Functionality: </b> This API is used to get ALL or ONE interviewer/s <br>
     * @return <b>SUCCESS MESSAGE:</b> <br>
     *          {"STATUS":"Success","DATA":[{"_id":"eba6ee4d-874f-4289-aaad-d47bf796290b",
     *          "business_unit":"Enterprise and Data Services","department":"Enterprise Engineering",
     *          "email":"abhin@gmail.com","employee_no":"P0103","experience":2,"isInterviewer":true,
     *          "job_title":"Software Engineer","location":"Pune","name":"Abhinov Gogoi",
     *          "phone":"+91 8136073065","skills":["Angular","Java"],
     *          "updatedDate":"2022-09-11T15:25:08.069+0000","updatedDateMs":1662909908069},
     *          {"_id":"6ad08b90-606b-4740-b803-5f164c18ce86","business_unit":"Enterprise and Data Services",
     *          "department":"Enterprise Engineering","email":"mike@gmail.com","employee_no":"P0100",
     *          "experience":2,"isInterviewer":true,"job_title":"Software Engineer","location":"Pune",
     *          "name":"Mike","phone":"+91 1234567890","skills":["ReactJS","Python"],"updatedDate":
     *          "2022-09-11T17:26:22.125+0000","updatedDateMs":1662917182125}]}
     *         <b>ERROR MESSAGE:</b> <br>
     *         {"EXCEPTION":"Invalid Token","MESSAGE":"TOKEN missing in request header","STATUS":"Error"} <br>
     *         {"EXCEPTION":"Invalid Token","MESSAGE":"Invalid TOKEN for given LOGIN_ID :: admin","STATUS":"Error"} <br>
     */
    @GetMapping()
    public Map<String, Object> getInterviewers(@RequestParam(value = "id", required = false) String id) {
        Map<String, Object> return_map = new HashMap<>(Literal.SIX);
        try {
            if (Utility.isEmptyString(id)) {
                id = Literal.ALL;
            }
            /**
             * Check token
             */
            TokenService.getInstance().validateToken(request);
            /**
             * save the valid data
             */
            return_map.put(Literal.STATUS, Literal.SUCCESS);
            return_map.put(Literal.DATA, PlannerService.getInstance()
                    .getInterviewers(id));
            return return_map;
        } catch (TokenValidationException e) {
            return_map.put(Literal.STATUS, Literal.ERROR);
            return_map.put(Literal.MESSAGE, e.getMessage());
            return_map.put(Literal.EXCEPTION, Literal.TOKEN_INVALID);
            return return_map;
        } catch (Exception e) {
            return_map.put(Literal.STATUS, Literal.ERROR);
            return_map.put(Literal.MESSAGE, e.getMessage());
            return_map.put(Literal.EXCEPTION, Literal.SOMETHING_WENT_WRONG);
            return return_map;
        }
    }

    /**
     *
     * {@code @apiNote}  
     *                REQUEST METHOD - DELETE <br>
     * 				  <br>
     *                <table border=1px>
     *                <tr>
     *                <th>Key</th>
     *                <th>Sample Data</th>
     *                <th>Data Type</th>
     *                <th>Constraint</th>
     *                <th>Description</th>
     *                </tr>
     *                <tr>
     *                <td>id</td>
     *                <td>eba6ee4d-874f-4289-aaad-d47bf796290b</td>
     *                <td>String</td>
     *                <td>Mandatory</td>
     *                <td>id of the interviewer to delete</td>
     *                </tr>
     *
     *                <table border=1px>
     *                <tr>
     *                <th>Header Key</th>
     *                <th>Sample Data</th>
     *                <th>Data Type</th>
     *                <th>Constraint</th>
     *                <th>Description</th>
     *                </tr>
     *                <tr>
     *                <td>TOKEN</td>
     *                <td>G6G57HfD49HPl</td>
     *                <td>String</td>
     *                <td>Mandatory</td>
     *                <td>user token</td>
     *                </tr>
     *                <tr>
     *                <td>LOGIN_ID</td>
     *                <td>admin</td>
     *                <td>String</td>
     *                <td>Mandatory</td>
     *                <td>login id / username</td>
     *                </tr>
     *                </table>
     *                <br>
     * @see <b>Functionality: </b> This API is used to delete ONE interviewer by id <br>
     * @return <b>SUCCESS MESSAGE:</b> <br>
     *         {"MESSAGE":"Data updated successfully","STATUS":"Success"}
     *         <b>ERROR MESSAGE:</b> <br>
     *         {"EXCEPTION":"Invalid Token","MESSAGE":"TOKEN missing in request header","STATUS":"Error"} <br>
     *         {"EXCEPTION":"Invalid Token","MESSAGE":"Invalid TOKEN for given LOGIN_ID :: admin","STATUS":"Error"} <br>
     */
    @DeleteMapping()
    public Map<String, Object> deleteInterviewer(@RequestParam(value = "id", required = true) String id) {
        Map<String, Object> return_map = new HashMap<>(Literal.SIX);
        try {
            /**
             * Check token
             */
            TokenService.getInstance().validateToken(request);
            /**
             * delete the interviewer
             */
            if (PlannerService.getInstance().deleteInterviewer(id)) {
                return_map.put(Literal.STATUS, Literal.SUCCESS);
                return_map.put(Literal.MESSAGE, Literal.DATA_UPDATED);
                return return_map;
            } else {
                return_map.put(Literal.STATUS, Literal.ERROR);
                return_map.put(Literal.MESSAGE, Literal.DATA_NOT_UPDATED);
                return return_map;
            }
        } catch (TokenValidationException e) {
            return_map.put(Literal.STATUS, Literal.ERROR);
            return_map.put(Literal.MESSAGE, e.getMessage());
            return_map.put(Literal.EXCEPTION, Literal.TOKEN_INVALID);
            return return_map;
        } catch (Exception e) {
            return_map.put(Literal.STATUS, Literal.ERROR);
            return_map.put(Literal.MESSAGE, e.getMessage());
            return_map.put(Literal.EXCEPTION, Literal.SOMETHING_WENT_WRONG);
            return return_map;
        }
    }

    /**
     * {@code @apiNote}
     *                REQUEST METHOD - POST <br>
     * {
     *     "start_time": "2022-12-18T12:00:00+05:30",
     *     "end_time": "2022-12-18T23:59:00+05:30",
     *     "experience": 2,
     *     "job_title": "Software Engineer",
     *     "department": "Enterprise Engineering",
     *     "business_unit": "Enterprise and Data Services",
     *     "location": "Pune",
     *     "skills" : ["React", "Java"]
     * }
     * 				  <br>
     *                <table border=1px>
     *                <tr>
     *                <th>Key</th>
     *                <th>Sample Data</th>
     *                <th>Data Type</th>
     *                <th>Constraint</th>
     *                <th>Description</th>
     *                </tr>
     *                <tr>
     *                <td>start_time</td>
     *                <td>2022-12-18T12:00:00+05:30</td>
     *                <td>String</td>
     *                <td>Optional</td>
     *                <td>filter those interviewers who are free at this time range</td>
     *                </tr>
     *                <tr>
     *                <td>end_time</td>
     *                <td>2022-12-18T16:00:00+05:30</td>
     *                <td>String</td>
     *                <td>Optional</td>
     *                <td>filter those interviewers who are free at this time range</td>
     *                </tr>
     *                <tr>
     *                <td>experience</td>
     *                <td>2</td>
     *                <td>Integer</td>
     *                <td>Optional</td>
     *                <td>filter by experience of the interviewers</td>
     *                </tr>
     *                <tr>
     *                <td>job_title</td>
     *                <td>Software Engineer</td>
     *                <td>String</td>
     *                <td>Optional</td>
     *                <td>filter by job_title of the interviewers</td>
     *                </tr>
     *                <tr>
     *                <td>department</td>
     *                <td>Enterprise Engineering</td>
     *                <td>String</td>
     *                <td>Optional</td>
     *                <td>filter by department of the interviewers</td>
     *                </tr>
     *                <tr>
     *                <td>business_unit</td>
     *                <td>Enterprise and Data Services</td>
     *                <td>String</td>
     *                <td>Optional</td>
     *                <td>filter by business_unit of the interviewers</td>
     *                </tr>
     *                <tr>
     *                <td>location</td>
     *                <td>Pune</td>
     *                <td>String</td>
     *                <td>Optional</td>
     *                <td>filter by location of the interviewers</td>
     *                </tr>
     *                <tr>
     *                <td>skills</td>
     *                <td>["Java", "React"]</td>
     *                <td>List of Strings</td>
     *                <td>Optional</td>
     *                <td>filter by skills of the interviewers</td>
     *                </tr>
     *
     *                <table border=1px>
     *                <tr>
     *                <th>Header Key</th>
     *                <th>Sample Data</th>
     *                <th>Data Type</th>
     *                <th>Constraint</th>
     *                <th>Description</th>
     *                </tr>
     *                <tr>
     *                <td>TOKEN</td>
     *                <td>G6G57HfD49HPl</td>
     *                <td>String</td>
     *                <td>Mandatory</td>
     *                <td>user token</td>
     *                </tr>
     *                <tr>
     *                <td>LOGIN_ID</td>
     *                <td>admin</td>
     *                <td>String</td>
     *                <td>Mandatory</td>
     *                <td>login id / username</td>
     *                </tr>
     *                </table>
     *                <br>
     * @see <b>Functionality: </b>
     * This API is used to search interviewer based on filters. <br>
     * Note that each filter searches based on AND OPERATOR, whereas individual skills will be matched by OR OPERATOR<br>
     * @note All fields are filtered on AND operator and individual skills on OR/(IN) operator
     * @return <b>SUCCESS MESSAGE:</b> <br>
     * {
     *     "STATUS": "Success",
     *     "DATA": [
     *         {
     *             "_id": "59cd855a-c691-4aa7-8ddf-6d57ee08f86a",
     *             "business_unit": "Enterprise and Data Services",
     *             "department": "Enterprise Engineering",
     *             "email": "abhinav.gogoi@clairvoyantsoft.com",
     *             "employee_no": "P010444",
     *             "experience": 3,
     *             "isInterviewer": true,
     *             "job_title": "Software Engineer",
     *             "location": "Pune",
     *             "name": "Abhinov Gogoi 222",
     *             "phone": "+91 1234567890",
     *             "skills": [
     *                 "Java"
     *             ],
     *             "updatedDate": "2022-09-11T18:14:38.757+0000",
     *             "updatedDateMs": 1662920078757
     *         },
     *         {
     *             "_id": "59cd855a-c691-4aa7-8ddf-6d57assdfs",
     *             "business_unit": "Enterprise and Data Services",
     *             "department": "Enterprise Engineering",
     *             "email": "abhinav.in@clairvoyantsoft.com",
     *             "employee_no": "P010444",
     *             "experience": 3,
     *             "isInterviewer": true,
     *             "job_title": "Software Engineer",
     *             "location": "Pune",
     *             "name": "Abhinov Gogoi 222",
     *             "phone": "+91 1234567890",
     *             "skills": [
     *                 "Java"
     *             ],
     *             "updatedDate": "2022-09-11T18:14:38.757+0000",
     *             "updatedDateMs": 1662920078757
     *         }
     *     ]
     * } <br>
     *         <b>ERROR MESSAGE:</b> <br>
     *         {"EXCEPTION":"Invalid Token","MESSAGE":"TOKEN missing in request header","STATUS":"Error"} <br>
     *         {"EXCEPTION":"Invalid Token","MESSAGE":"Invalid TOKEN for given LOGIN_ID :: admin","STATUS":"Error"} <br>
     */
    @PostMapping("/search")
    public Map<String, Object> searchInterviewers(@RequestBody Map<String, Object> req_map) {
        Map<String, Object> return_map = new HashMap<>(Literal.SIX);
        try {
            /**
             * TODO ::
             * One issue with search is that this removes any interviewer who has an event scheduled at the start time and end time
             * This is good for approach 1. i.e Filtering out the busy interviewers from the eligible interviewers list
             *
             * BUT this would CONTRADICT the 2nd approach where, we also want each interviewer to mention their free time on google calendar
             * by creating an event with a specific name like 'FREE TIME'. Because, while scanning events in 1st approach ,
             * these events would be picked up too and the interviewer would be considered BUSY at that time.
             * 2 approaches are Self contradictory.
             *
             * Solution is :
             * DON'T Let interviewers slot their FREE TIME on google calendar. This involves manual work.
             * But Let the HR Admin, view the calendars of each suitable interviewer
             * and decide upon to select the final interviewer
             *
            /**
             * Check token
             */
            TokenService.getInstance().validateToken(request);
            /**
             * Validate request params
             */
            PlannerService.getInstance().validateInterviewerSearchRequest(req_map);
            /**
             * get the results
             */
            return_map.put(Literal.STATUS, Literal.SUCCESS);
            return_map.put(Literal.DATA, PlannerService.getInstance().searchInterviewers(req_map));
            return return_map;
        } catch (TokenValidationException e) {
            return_map.put(Literal.STATUS, Literal.ERROR);
            return_map.put(Literal.MESSAGE, e.getMessage());
            return_map.put(Literal.EXCEPTION, Literal.TOKEN_INVALID);
            return return_map;
        } catch (RequestValidationException e) {
            return_map.put(Literal.STATUS, Literal.ERROR);
            return_map.put(Literal.MESSAGE, e.getMessage());
            return_map.put(Literal.EXCEPTION, Literal.REQUEST_VALIDATION_FAILED);
            return return_map;
        } catch (Exception e) {
            return_map.put(Literal.STATUS, Literal.ERROR);
            return_map.put(Literal.MESSAGE, e.getMessage());
            return_map.put(Literal.EXCEPTION, Literal.SOMETHING_WENT_WRONG);
            return return_map;
        }
    }
}
