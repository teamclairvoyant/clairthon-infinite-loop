package com.clairvoyant.iPlanner.API.FreeBusy;

import com.clairvoyant.iPlanner.API.APIEndpoints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(APIEndpoints.FREEBUSY)
public class FreeBusyController {

    @Autowired
    FreeBusyService freeBusyService;

    public FreeBusyController(FreeBusyService freeBusyService) {
        this.freeBusyService = FreeBusyService.getInstance();
    }

    @GetMapping("/test")
    public String testFreeBusy() {
        return "SUCCESS - FreeBusyController";
    }

    @GetMapping("/initialize")
    public Map<String, Object> initFreeBusyCredentials() {
        return freeBusyService.initService();
    }

    @GetMapping("/get")
    public String getFreeBusy(@RequestParam(value = "email", required = false) String email) {
        try {
            if(email == null) {
                email = "abhinav.gogoi@clairvoyantsoft.com";
            }
            return freeBusyService.getFreeBusy(email);
        } catch (Exception e) {
            return  e.getLocalizedMessage();
        }
    }
}
