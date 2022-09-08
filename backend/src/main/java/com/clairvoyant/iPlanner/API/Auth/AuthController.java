package com.clairvoyant.iPlanner.API.Auth;

import com.clairvoyant.iPlanner.API.APIEndpoints;
import com.clairvoyant.iPlanner.Utility.Literal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(APIEndpoints.AUTH)
public class AuthController {

    @Autowired
    HttpServletRequest request;

    @GetMapping()
    public String testAuth() {
        return "SUCCESS - AuthController";
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, Object> req_map) {
        Map<String, Object> return_map = new HashMap<>(Literal.SIX);
        try {

            /**
             * Check for the null login_id
             */
            if (StringUtils.isEmpty(req_map.get(Literal.login_id))) {
                return_map.put(Literal.STATUS, Literal.ERROR);
                return_map.put(Literal.MESSAGE, Literal.LOGIN_ID_NULL);
                return_map.put(Literal.REQUEST_DATA, req_map);
                return return_map;
            }
            /**
             * Check for null password
             */
            if (StringUtils.isEmpty(req_map.get(Literal.password))) {
                return_map.put(Literal.STATUS, Literal.ERROR);
                return_map.put(Literal.MESSAGE, Literal.NULL_PASSWORD);
                return_map.put(Literal.REQUEST_DATA, req_map);
                return return_map;
            }
            /**
             * check Hardcoded login id and password
             * todo :: change hardcoded creds to fetch from mongo
             */
            if(req_map.get(Literal.login_id).toString().equals("admin")
                    && req_map.get(Literal.password).toString().equals("admin")) {
                return_map.put(Literal.STATUS, Literal.SUCCESS);
                return_map.put(Literal.LOGIN_ID, req_map.get(Literal.login_id));
                return_map.put(Literal.TOKEN, Literal.iPlanner);
                return return_map;
            } else {
                return_map.put(Literal.STATUS, Literal.ERROR);
                return_map.put(Literal.MESSAGE, Literal.INCORRECT_CREDENTIALS);
                return_map.put(Literal.REQUEST_DATA, req_map);
                return return_map;
            }
        } catch (Exception e) {
            return_map.put(Literal.STATUS, Literal.ERROR);
            return_map.put(Literal.MESSAGE, Literal.SOMETHING_WENT_WRONG);
            return_map.put(Literal.EXCEPTION, e.getStackTrace());
            return_map.put(Literal.REQUEST_DATA, req_map);
            return return_map;
        }
    }
}
