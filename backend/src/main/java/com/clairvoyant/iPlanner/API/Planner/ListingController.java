package com.clairvoyant.iPlanner.API.Planner;

import com.clairvoyant.iPlanner.API.APIEndpoints;
import com.clairvoyant.iPlanner.Services.TokenService;
import com.clairvoyant.iPlanner.Exceptions.RequestValidationException;
import com.clairvoyant.iPlanner.Exceptions.TokenValidationException;
import com.clairvoyant.iPlanner.Utility.Literal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping(APIEndpoints.LISTING)
public class ListingController {

    @Autowired
    HttpServletRequest request;

    /**
     * @param req_map <br>
     *               REQUEST METHOD - POST <br>
     *               {
     *                "type" : "DEPARTMENT",
     *                "action" : "ADD",
     *                "items" : ["Software Engineer", "QA Engineer", "HR Manager"]
     *                } <br>
     *                OR <br>
     *               {
     *                "type" : "DEPARTMENT",
     *                "action" : "DELETE",
     *                "items" : ["Software Engineer"]
     *                } <br>
     *
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
     *                <td>type</td>
     *                <td>DEPARTMENT</td>
     *                <td>String</td>
     *                <td>Mandatory</td>
     *                <td>should be valid type - JOB_TITLE, DEPARTMENT, BUSINESS_UNIT, SKILLS, LOCATION</td>
     *                </tr>
     *                <tr>
     *                <td>action</td>
     *                <td>ADD</td>
     *                <td>String</td>
     *                <td>Mandatory</td>
     *                <td>ADD or DELETE</td>
     *                </tr>
     *                <tr>
     *                <td>items</td>
     *                <td>["Software Engineer", "QA Engineer", "HR Manager"]</td>
     *                <td>List of Strings</td>
     *                <td>Mandatory</td>
     *                <td>Items to ADD or DELETE to/from the listing</td>
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
     * @see <b>Functionality: </b> This API is used to save listing (create/update) <br>
     * @return <b>SUCCESS MESSAGE:</b> <br>
     *         {"MESSAGE":"Data updated successfully","STATUS":"Success"} <br>
     *         <b>ERROR MESSAGE:</b> <br>
     *         {"EXCEPTION":"Request validation failed","MESSAGE":"Invalid listing type from the list (JOB_TITLE, DEPARTMENT, BUSINESS_UNIT, SKILLS)","STATUS":"Error"} <br>
     *         {"EXCEPTION":"Request validation failed","MESSAGE":"Kindly provide a valid action - ADD or DELETE","STATUS":"Error"} <br>
     *         {"EXCEPTION":"Invalid Token","MESSAGE":"TOKEN missing in request header","STATUS":"Error"} <br>
     *         {"EXCEPTION":"Invalid Token","MESSAGE":"Invalid TOKEN for given LOGIN_ID :: admin","STATUS":"Error"} <br>
     */
    @PostMapping()
    public Map<String, Object> saveListing(@RequestBody Map<String, Object> req_map) {
        Map<String, Object> return_map = new HashMap<>(Literal.SIX);
        try {
            /**
             * Check token
             */
            TokenService.getInstance().validateToken(request);
            /**
             * Validate Request
             */
            PlannerService.getInstance().validateSaveListing(req_map);
            /**
             * save the valid data
             */
            return PlannerService.getInstance()
                    .saveListing(
                            req_map.get(Literal.type).toString(),
                            req_map.get(Literal.action).toString(),
                            (List<String>) req_map.get(Literal.items));
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
     * {@code @apiNote} headers <br>
     *               REQUEST METHOD - GET <br>
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
     * @see <b>Functionality: </b> This API is used to get ALL listing <br>
     * @return <b>SUCCESS MESSAGE:</b> <br>
     * {
     *     "STATUS": "Success",
     *     "DATA": [
     *         {
     *             "_id": "c98f3410-97d1-4fe1-a289-f15f262ae102",
     *             "items": [
     *                 "Software Engineer",
     *                 "Software Engineer II",
     *                 "QA Engineer",
     *                 "HR",
     *                 "HR Manager"
     *             ],
     *             "type": "JOB_TITLE",
     *             "updatedDate": "2022-09-11T15:43:55.619+0000",
     *             "updatedDateMs": 1662911035619
     *         },
     *         {
     *             "_id": "8e8fa6c4-6222-4c03-ace7-53b358b963fe",
     *             "items": [
     *                 "Enterprise Engineering",
     *                 "Business Development"
     *             ],
     *             "type": "DEPARTMENT",
     *             "updatedDate": "2022-09-11T15:43:55.867+0000",
     *             "updatedDateMs": 1662911035867
     *         },
     *         {
     *             "_id": "32e8c8cd-b8f1-48db-8d27-3b48cdf0ceb8",
     *             "items": [
     *                 "Enterprise and Data Services",
     *                 "Sales"
     *             ],
     *             "type": "BUSINESS_UNIT",
     *             "updatedDate": "2022-09-11T15:43:56.071+0000",
     *             "updatedDateMs": 1662911036071
     *         },
     *         {
     *             "_id": "b7c7e5d1-9c41-4582-a0f8-1ec3ea8d3edb",
     *             "items": [
     *                 "Java",
     *                 "Angular",
     *                 "ReactJS",
     *                 "Big Data",
     *                 "Python"
     *             ],
     *             "type": "SKILLS",
     *             "updatedDate": "2022-09-11T15:43:56.265+0000",
     *             "updatedDateMs": 1662911036265
     *         },
     *         {
     *             "_id": "865d23b3-d048-40d4-a638-d763fefbb0fb",
     *             "items": [
     *                 "Pune",
     *                 "Hyderabad",
     *                 "US",
     *                 "Canada"
     *             ],
     *             "type": "LOCATION",
     *             "updatedDate": "2022-09-11T15:43:56.495+0000",
     *             "updatedDateMs": 1662911036495
     *         }
     *     ]
     * } <br>
     *         <b>ERROR MESSAGE:</b> <br>
     *         {"EXCEPTION":"Invalid Token","MESSAGE":"TOKEN missing in request header","STATUS":"Error"} <br>
     *         {"EXCEPTION":"Invalid Token","MESSAGE":"Invalid TOKEN for given LOGIN_ID :: admin","STATUS":"Error"} <br>
     */
    @GetMapping()
    public Map<String, Object> getListing() {
        Map<String, Object> return_map = new HashMap<>(Literal.SIX);
        try {
            /**
             * Check token
             */
            TokenService.getInstance().validateToken(request);
            /**
             * get the data
             */
            return_map.put(Literal.STATUS, Literal.SUCCESS);
            return_map.put(Literal.DATA, PlannerService.getInstance().getListing());
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
     * {@code @apiNote} headers <br>
     *               REQUEST METHOD - POST <br>
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
     * This API is used to populate dummy data for Listing - JOB_TITLE, DEPARTMENT, BUSINESS_UNIT, SKILLS, LOCATION <br>
     * @return <b>SUCCESS MESSAGE:</b> <br>
     * {
     *     "MESSAGE": "Data updated successfully",
     *     "STATUS": "Success",
     *     "DATA": [
     *         {
     *             "_id": "c98f3410-97d1-4fe1-a289-f15f262ae102",
     *             "items": [
     *                 "Software Engineer",
     *                 "Software Engineer II",
     *                 "QA Engineer",
     *                 "HR",
     *                 "HR Manager"
     *             ],
     *             "type": "JOB_TITLE",
     *             "updatedDate": "2022-09-11T15:43:55.619+0000",
     *             "updatedDateMs": 1662911035619
     *         },
     *         {
     *             "_id": "8e8fa6c4-6222-4c03-ace7-53b358b963fe",
     *             "items": [
     *                 "Enterprise Engineering",
     *                 "Business Development"
     *             ],
     *             "type": "DEPARTMENT",
     *             "updatedDate": "2022-09-11T15:43:55.867+0000",
     *             "updatedDateMs": 1662911035867
     *         },
     *         {
     *             "_id": "32e8c8cd-b8f1-48db-8d27-3b48cdf0ceb8",
     *             "items": [
     *                 "Enterprise and Data Services",
     *                 "Sales"
     *             ],
     *             "type": "BUSINESS_UNIT",
     *             "updatedDate": "2022-09-11T15:43:56.071+0000",
     *             "updatedDateMs": 1662911036071
     *         },
     *         {
     *             "_id": "b7c7e5d1-9c41-4582-a0f8-1ec3ea8d3edb",
     *             "items": [
     *                 "Java",
     *                 "Angular",
     *                 "ReactJS",
     *                 "Big Data",
     *                 "Python"
     *             ],
     *             "type": "SKILLS",
     *             "updatedDate": "2022-09-11T15:43:56.265+0000",
     *             "updatedDateMs": 1662911036265
     *         },
     *         {
     *             "_id": "865d23b3-d048-40d4-a638-d763fefbb0fb",
     *             "items": [
     *                 "Pune",
     *                 "Hyderabad",
     *                 "US",
     *                 "Canada"
     *             ],
     *             "type": "LOCATION",
     *             "updatedDate": "2022-09-11T15:43:56.495+0000",
     *             "updatedDateMs": 1662911036495
     *         }
     *     ]
     * } <br>
     *         <b>ERROR MESSAGE:</b> <br>
     *         {"EXCEPTION":"Invalid Token","MESSAGE":"TOKEN missing in request header","STATUS":"Error"} <br>
     *         {"EXCEPTION":"Invalid Token","MESSAGE":"Invalid TOKEN for given LOGIN_ID :: admin","STATUS":"Error"} <br>
     */
    @GetMapping("/initialize")
    public Map<String, Object> populateDummyListing() {
        Map<String, Object> return_map = new HashMap<>(Literal.SIX);
        try {
            /**
             * Check token
             */
            TokenService.getInstance().validateToken(request);

            if (PlannerService.getInstance().populateDummyListing()) {
                return_map.put(Literal.STATUS, Literal.SUCCESS);
                return_map.put(Literal.MESSAGE, Literal.DATA_UPDATED);
                return_map.put(Literal.DATA, PlannerService.getInstance().getListing());
                return return_map;
            } else {
                return_map.put(Literal.STATUS, Literal.ERROR);
                return_map.put(Literal.MESSAGE, Literal.DATA_NOT_UPDATED);
                return_map.put(Literal.DATA, PlannerService.getInstance().getListing());
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
}
