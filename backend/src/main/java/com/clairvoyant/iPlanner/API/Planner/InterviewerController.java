package com.clairvoyant.iPlanner.API.Planner;

import com.clairvoyant.iPlanner.API.APIEndpoints;
import com.clairvoyant.iPlanner.Services.TokenService;
import com.clairvoyant.iPlanner.Exceptions.RequestValidationException;
import com.clairvoyant.iPlanner.Exceptions.TokenValidationException;
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
     * @param req_map {
     *                "id": "001",
     *                "employee_no": "P0102",
     *                "name": "Abhinov Gogoi",
     *                "email": "abhinav.gogoi@clairvoyantsoft.com",
     *                "phone": "2378263837",
     *                "experience": 2,
     *                "isInterviewer": true,
     *                "job_title": "Software Engineer",
     *                "department": "Enterprise Engineering",
     *                "business_unit": "Enterprise and Data Services",
     *                "location": "Pune",
     *                "skills": [
     *                "Angular",
     *                "Java"
     *                ]
     *                }
     * @return
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
     * @param id
     * @return
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
     * @param id
     * @return
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
}
