package com.clairvoyant.iPlanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication()
@ServletComponentScan
public class IPlannerApplication {

    private static Logger logger = LoggerFactory.getLogger(IPlannerApplication.class);

    public static void main(String[] args) {
        logger.info("Starting IPlanner");
        SpringApplication.run(IPlannerApplication.class, args);
    }



}
