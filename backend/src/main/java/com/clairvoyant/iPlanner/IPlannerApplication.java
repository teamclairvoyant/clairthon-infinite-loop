package com.clairvoyant.iPlanner;

import com.clairvoyant.iPlanner.API.Slack.SlackHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

import java.sql.Timestamp;

@SpringBootApplication()
@ServletComponentScan
public class IPlannerApplication {

	private static Logger logger = LoggerFactory.getLogger(IPlannerApplication.class);

	public static void main(String[] args) {
		logger.info("Starting IPlanner");
		SpringApplication.run(IPlannerApplication.class, args);
//		sendSlackTestMessage();
//		getSlackUsers();
	}

	private static void getSlackUsers() {
		SlackHelper.getSlackUsers();
	}

	public static void sendSlackTestMessage() {
		logger.info("sendSlackTestMessage :::::::::::::::::::::::::::");
		String channelId = SlackHelper.getChannelId("big-data");
		SlackHelper.publishMessageToChannel(channelId, "Hello from slack bot "+new Timestamp(System.currentTimeMillis()));
	}

}
