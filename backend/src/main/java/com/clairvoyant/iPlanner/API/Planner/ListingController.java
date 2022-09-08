package com.clairvoyant.iPlanner.API.Planner;

import com.clairvoyant.iPlanner.API.APIEndpoints;
import com.clairvoyant.iPlanner.Services.TokenService;
import com.clairvoyant.iPlanner.Utility.Literal;
import com.clairvoyant.iPlanner.Utility.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(APIEndpoints.LISTING)
public class ListingController {

    @Autowired
    HttpServletRequest request;

    /**
     * This API is used to update listing
     *
     * @param req_map {
     *                "type" : "DEPARTMENT",
     *                "action" : "ADD",
     *                "items" : ["Software Engineer", "QA Engineer", "HR Manager"]
     *                }
     * @return
     */
    @PostMapping()
    public Map<String, Object> saveListing(@RequestBody Map<String, Object> req_map) {
        Map<String, Object> return_map = new HashMap<>(Literal.SIX);
        try {
            /**
             * Check token
             */
            if (!TokenService.getInstance().validateToken(request)) {
                return_map.put(Literal.STATUS, Literal.ERROR);
                return_map.put(Literal.MESSAGE, Literal.TOKEN_INVALID);
                return_map.put(Literal.REQUEST_DATA, req_map);
                return return_map;
            }
            /**
             * check null type
             */
            if (Utility.isEmptyString(req_map.get(Literal.type))) {
                return_map.put(Literal.STATUS, Literal.ERROR);
                return_map.put(Literal.MESSAGE, Literal.TYPE_NULL);
                return_map.put(Literal.REQUEST_DATA, req_map);
                return return_map;
            }
            /**
             * check invalid type
             */
            if (!Utility.chkListingType(req_map.get(Literal.type).toString())) {
                return_map.put(Literal.STATUS, Literal.ERROR);
                return_map.put(Literal.MESSAGE, Literal.TYPE_INVALID);
                return_map.put(Literal.REQUEST_DATA, req_map);
                return return_map;
            }
            /**
             * check null Action
             */
            if (Utility.isEmptyString(req_map.get(Literal.action))) {
                return_map.put(Literal.STATUS, Literal.ERROR);
                return_map.put(Literal.MESSAGE, Literal.ACTION_NULL);
                return_map.put(Literal.REQUEST_DATA, req_map);
                return return_map;
            }
            /**
             * check valid Action
             */
            if (Utility.chkValidAction(req_map.get(Literal.action).toString())) {
                return_map.put(Literal.STATUS, Literal.ERROR);
                return_map.put(Literal.MESSAGE, Literal.ACTION_INVALID);
                return_map.put(Literal.REQUEST_DATA, req_map);
                return return_map;
            }
            /**
             * Check null items
             */
            if (req_map.get(Literal.items) == null) {
                return_map.put(Literal.STATUS, Literal.ERROR);
                return_map.put(Literal.MESSAGE, Literal.ITEMS_NULL);
                return_map.put(Literal.REQUEST_DATA, req_map);
                return return_map;
            }

            return PlannerService.getInstance().saveListing(req_map);


        } catch (Exception e) {
            return_map.put(Literal.STATUS, Literal.ERROR);
            return_map.put(Literal.MESSAGE, Literal.SOMETHING_WENT_WRONG);
            return_map.put(Literal.EXCEPTION, e.getStackTrace());
            return_map.put(Literal.REQUEST_DATA, req_map);
            return return_map;
        }

    }

    @GetMapping()
    // todo
    public Map<String, Object> getListing(@RequestBody Map<String, Object> req_map) {
        Map<String, Object> return_map = new HashMap<>(Literal.SIX);
        return return_map;

    }
}
