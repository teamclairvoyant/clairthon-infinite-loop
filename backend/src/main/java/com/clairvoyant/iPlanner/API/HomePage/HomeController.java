package com.clairvoyant.iPlanner.API.HomePage;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

//    @GetMapping("/")
//    public String homePage() {
//        System.out.println("homePage API hit");
//        return "coming-soon";
//    }

    @GetMapping("/")
    public ModelAndView index () {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("coming-soon");
        return modelAndView;
    }
}
