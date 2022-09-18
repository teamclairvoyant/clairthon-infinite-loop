package com.clairvoyant.iPlanner.Slack;

import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import com.slack.api.methods.response.conversations.ConversationsListResponse;
import com.slack.api.model.Conversation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class SlackHelper {

    private static final Logger logger = LoggerFactory.getLogger(SlackHelper.class);

    public static final String BOT_TOKEN = "xoxb-1597172380741-4097046480819-JimGISsAq9FndJ0Es7wohAL9";
    public static final String APP_TOKEN = "xapp-1-A043JR2B0AC-4121068359728-0e85bc9defce66654578e1bcfac721112e7cd504f8cda2d40350c0e92ee28140";

    /**
     * Find conversation/channel ID using the conversations.list method
     */
    public static String getChannelId(String channelName) {
        // you can also get this instance via ctx.client() in a Bolt app
        MethodsClient client = Slack.getInstance().methods();
        try {
            // Call the conversations.list method using the built-in WebClient
            ConversationsListResponse result = client.conversationsList(r -> r
                    .token(BOT_TOKEN)
            );
            for (Conversation channel : result.getChannels()) {
                if (channel.getName().equals(channelName)) {
                    String conversationId = channel.getId();
                    logger.info("Found conversation ID: {}", conversationId);
                    return  conversationId;
                }
            }
        } catch (IOException | SlackApiException e) {
            logger.error("error: {}", e.getMessage(), e);
        }
        return null;
    }

    /**
     * Post a message to a channel your app is in using ID and message text
     */
    public static ChatPostMessageResponse publishMessage(String channelName, String text) {
        String channelId = getChannelId(channelName);
        // you can get this instance via ctx.client() in a Bolt app
        MethodsClient client = Slack.getInstance().methods();
        try {
            // Call the chat.postMessage method using the built-in WebClient
            ChatPostMessageResponse result = client.chatPostMessage(r -> r
                            // The token you used to initialize your app
                            .token(BOT_TOKEN)
                            .channel(channelId)
                            .text(text)
                    // You could also use a blocks[] array to send richer content
            );
            logger.info("result {}", result);
            return result;
        } catch (IOException | SlackApiException e) {
            logger.error("error: {}", e.getMessage(), e);
        }
        return null;
    }


}
