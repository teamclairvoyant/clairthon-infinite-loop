package com.clairvoyant.iPlanner.API.Planner;

import com.clairvoyant.iPlanner.API.APIEndpoints;
import com.clairvoyant.iPlanner.Services.TokenService;
import com.clairvoyant.iPlanner.Shared.RequestValidationException;
import com.clairvoyant.iPlanner.Shared.TokenValidationException;
import com.clairvoyant.iPlanner.Utility.Literal;
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
            TokenService.getInstance().validateToken(request);

            /**
             * validate the request data
             */
            PlannerService.getInstance().validateInterviewer(req_map);

            /**
             * save the valid data
             */
            return PlannerService.getInstance()
                    .saveInterviewer(req_map);

        } catch (TokenValidationException e) {
            return_map.put(Literal.STATUS, Literal.ERROR);
            return_map.put(Literal.MESSAGE, Literal.TOKEN_INVALID);
            return_map.put(Literal.EXCEPTION, e.getLocalizedMessage());
            return return_map;
        } catch (RequestValidationException e) {
            return_map.put(Literal.STATUS, Literal.ERROR);
            return_map.put(Literal.MESSAGE, Literal.REQUEST_VALIDATION_FAILED);
            return_map.put(Literal.EXCEPTION, e.getLocalizedMessage());
            return return_map;
        } catch (Exception e) {
            return_map.put(Literal.STATUS, Literal.ERROR);
            return_map.put(Literal.MESSAGE, Literal.SOMETHING_WENT_WRONG);
            return_map.put(Literal.EXCEPTION, e.getLocalizedMessage());
            return return_map;
        }
    }
}
