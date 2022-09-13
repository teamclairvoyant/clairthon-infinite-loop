package com.clairvoyant.iPlanner.API.Calendar;

import com.clairvoyant.iPlanner.API.APIEndpoints;
import com.clairvoyant.iPlanner.Exceptions.RequestValidationException;
import com.clairvoyant.iPlanner.Exceptions.TokenValidationException;
import com.clairvoyant.iPlanner.Services.TokenService;
import com.clairvoyant.iPlanner.Utility.Literal;
import com.google.api.client.util.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping(APIEndpoints.FREEBUSY)
public class FreeBusyController {

    @Autowired
    HttpServletRequest request;

    @GetMapping("/test")
    public String testFreeBusy() {
        return "SUCCESS - FreeBusyController";
    }

    @GetMapping("/initialize")
    public Map<String, Object> initFreeBusyCredentials() {
        return FreeBusyService.getInstance().initService();
    }

    /**
     * {
     *     "email" : "abhinav.gogoi@clairvoyantsoft.com",
     *     "start_time": "2022-09-13T12:00:00+05:30",
     *     "end_time": "2022-09-13T15:59:00+05:30"
     * }
     * @param req_map
     * @return
     */
    @PostMapping("/get")
    public String getFreeBusy(@RequestBody Map<String, Object> req_map) {
        try {
            /**
             * Check token
             */
            TokenService.getInstance().validateToken(request);
            /**
             * validate the request data
             */
            String email;
            DateTime start;
            DateTime end;
            try {
                email = req_map.get(Literal.email).toString();
                start = new DateTime(req_map.get(Literal.start_time).toString());
                end = new DateTime(req_map.get(Literal.end_time).toString());
            } catch (Exception e) {
                throw new RequestValidationException(e.getMessage());
            }
            return FreeBusyService.getInstance().getFreeBusy(email, start, end).toString();
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    /**
     * {
     * "emails" : ["abhinav.gogoi@clairvoyantsoft.com", "kedar.shivshette@clairvoyantsoft.com"],
     * "start_time": "2022-12-18T00:00:00",
     * "end_time": "2022-12-18T23:59:00"
     * }
     *
     * @param req_map
     * @return
     */
    @PostMapping("/filterBusyOnes")
    public Map<String, Object> filterBusyOnes(@RequestBody Map<String, Object> req_map) {
        Map<String, Object> return_map = new HashMap<>(Literal.SIX);
        try {
            /**
             * Check token
             */
            TokenService.getInstance().validateToken(request);
            /**
             * validate the request data
             */
            List<String> emails_list;
            DateTime start_time;
            DateTime end_time;
            try{
                emails_list = (List<String>) req_map.get(Literal.emails);
                start_time = new DateTime(req_map.get(Literal.start_time).toString());
                end_time = new DateTime(req_map.get(Literal.end_time).toString());
            } catch (Exception e) {
                throw new RequestValidationException(e.getMessage());
            }
            return_map.put(Literal.STATUS, Literal.SUCCESS);
            return_map.put(Literal.DATA, FreeBusyService.getInstance().filterBusyEmails(emails_list, start_time, end_time));
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

    /**
     * {
     * "email" : "abhinav.gogoi@clairvoyantsoft.com",
     * "start_time": "2022-12-18T00:00:00",
     * "end_time": "2022-12-18T23:59:00"
     * }
     *
     * @param req_map
     * @return
     */
    @PostMapping("/events")
    public Map<String, Object> getEvents(@RequestBody Map<String, Object> req_map) {
        Map<String, Object> return_map = new HashMap<>(Literal.SIX);
        try {
            /**
             * Check token
             */
            TokenService.getInstance().validateToken(request);
            /**
             * validate the request data
             */
            String email;
            DateTime start_time;
            DateTime end_time;
            try{
                email =  req_map.get(Literal.email).toString();
                start_time = new DateTime(req_map.get(Literal.start_time).toString());
                end_time = new DateTime(req_map.get(Literal.end_time).toString());
            } catch (Exception e) {
                throw new RequestValidationException(e.getMessage());
            }
            return_map.put(Literal.STATUS, Literal.SUCCESS);
            return_map.put(Literal.DATA, FreeBusyService.getInstance().getEvents(email, start_time, end_time));
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