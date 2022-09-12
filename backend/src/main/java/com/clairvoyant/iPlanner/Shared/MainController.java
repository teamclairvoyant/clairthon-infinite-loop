package com.clairvoyant.iPlanner.Shared;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@Controller
public class MainController {

    @GetMapping("/testMongo")
    @ResponseBody
    public String testMongo() {
        if (MainMongoDao.getInstance().testMongoConnection()) {
            return "Mongo is UP " + new Date(System.currentTimeMillis());
        } else {
            return "Mongo DOWN !! " + new Date(System.currentTimeMillis());
        }
    }

//    @GetMapping("/")
//    public ModelAndView index() {
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.setViewName("coming-soon");
//        return modelAndView;
//    }
}
