package com.clairvoyant.iPlanner.API.Slack;


import com.clairvoyant.iPlanner.API.APIEndpoints;
import com.clairvoyant.iPlanner.Slack.SlackHelper;
import com.clairvoyant.iPlanner.Utility.Literal;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(APIEndpoints.SLACK)
public class SlackController {

    @GetMapping("/sendMessage")
    public Map<String, Object> getListing(
            @RequestParam(value = "message", required = true) String message,
            @RequestParam(value = "channel", required = true) String channel) {
        Map<String, Object> return_map = new HashMap<>(Literal.SIX);
        try {
            /**
             * send the message
             */
            ChatPostMessageResponse chatPostMessageResponse = SlackHelper.publishMessage(channel, message);

            if (chatPostMessageResponse.isOk()) {
                return_map.put(Literal.STATUS, Literal.SUCCESS);
                return_map.put(Literal.MESSAGE, "Message sent");
                return_map.put(Literal.DATA, chatPostMessageResponse);
                return return_map;
            } else {
                return_map.put(Literal.STATUS, Literal.ERROR);
                return_map.put(Literal.MESSAGE, chatPostMessageResponse.getError());
                return_map.put(Literal.EXCEPTION, Literal.SOMETHING_WENT_WRONG);
                return return_map;
            }

        } catch (Exception e) {
            return_map.put(Literal.STATUS, Literal.ERROR);
            return_map.put(Literal.MESSAGE, e.getMessage());
            return_map.put(Literal.EXCEPTION, Literal.SOMETHING_WENT_WRONG);
            return return_map;
        }
    }
}
