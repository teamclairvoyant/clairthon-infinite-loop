package com.clairvoyant.iPlanner.API.Planner;

import com.clairvoyant.iPlanner.API.APIEndpoints;
import com.clairvoyant.iPlanner.Utility.Literal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(APIEndpoints.INTERVIEWER)
public class InterviewerController {

    @Autowired
    HttpServletRequest request;

    @GetMapping()
    public String testInterviewer() {
        return "SUCCESS - InterviewerController";
    }

    @PostMapping("/save")
    // todo
    public Map<String, Object> saveInterviewer(@RequestBody Map<String, Object> req_map) {
        Map<String, Object> return_map = new HashMap<>(Literal.SIX);
        return return_map;

    }


}
