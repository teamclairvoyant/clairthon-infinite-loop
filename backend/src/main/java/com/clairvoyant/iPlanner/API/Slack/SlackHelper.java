package com.clairvoyant.iPlanner.API.Slack;

import com.clairvoyant.iPlanner.Utility.Utility;
import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import com.slack.api.methods.response.conversations.ConversationsListResponse;
import com.slack.api.methods.response.users.UsersListResponse;
import com.slack.api.model.Conversation;
import com.slack.api.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class SlackHelper {

    private static final Logger logger = LoggerFactory.getLogger(SlackHelper.class);

    public static final String BOT_TOKEN = "xoxb-1597172380741-4097046480819-tJeliT9lRnBB108NeXshgAoh";
    public static final String APP_TOKEN = "xapp-1-A043JR2B0AC-4121068359728-0e85bc9defce66654578e1bcfac721112e7cd504f8cda2d40350c0e92ee28140";

    /**
     * Find conversation/channel ID using the conversations.list method
     */
    public static String getChannelId(String channelName) throws RuntimeException {
        // you can also get this instance via ctx.client() in a Bolt app
        MethodsClient client = Slack.getInstance().methods();
        try {
            // Call the conversations.list method using the built-in WebClient
            ConversationsListResponse result = client.conversationsList(r -> r
                    .token(BOT_TOKEN)
            );

            if (!Utility.isEmptyString(result.getError())) {
                throw new RuntimeException(result.getError());
            }

            for (Conversation channel : result.getChannels()) {
                if (channel.getName().equals(channelName)) {
                    String conversationId = channel.getId();
                    logger.info("Found conversation ID: {}", conversationId);
                    return conversationId;
                }
            }
        } catch (IOException | SlackApiException e) {
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }

    /**
     * Post a message to a channel your app is in using ID and message
     */
    public static ChatPostMessageResponse publishMessageToChannel(String channelName, String message) {
        String channelId = getChannelId(channelName);
        // you can get this instance via ctx.client() in a Bolt app
        MethodsClient client = Slack.getInstance().methods();
        try {
            // Call the chat.postMessage method using the built-in WebClient
            ChatPostMessageResponse result = client.chatPostMessage(r -> r
                            // The token you used to initialize your app
                            .token(BOT_TOKEN)
                            .channel(channelId)
                            .text(message)
                    // You could also use a blocks[] array to send richer content
            );
            logger.info("result {}", result);
            return result;
        } catch (IOException | SlackApiException e) {
            logger.error("error: {}", e.getMessage(), e);
        }
        return null;
    }

    /**
     * Post a message to a person/user
     */
    public static ChatPostMessageResponse publishMessageToUser(String email, String message) {
        // todo

        User user = getSlackUserId(email);

        // you can get this instance via ctx.client() in a Bolt app
        MethodsClient client = Slack.getInstance().methods();
        try {
            // Call the chat.postMessage method using the built-in WebClient
            ChatPostMessageResponse result = client.chatPostMessage(r -> r
                            // The token you used to initialize your app
                            .token(BOT_TOKEN)
                            .channel(user.getId())
                            .text(message)
                    // You could also use a blocks[] array to send richer content
            );
            logger.info("result {}", result);
            return result;
        } catch (IOException | SlackApiException e) {
            logger.error("error: {}", e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }

    public static List<User> getSlackUsers() {
        MethodsClient client = Slack.getInstance().methods();
        try {
            // Call the conversations.list method using the built-in WebClient
            UsersListResponse users = client.usersList(r -> r
                    .token(BOT_TOKEN)
            );

            if (!Utility.isEmptyString(users.getError())) {
                throw new RuntimeException(users.getError());
            }

            users.getMembers().forEach(user -> System.out.println(user.toString()));

            return users.getMembers();

        } catch (IOException | SlackApiException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static User getSlackUserId(String email) {
        List<User> slackUsers = getSlackUsers();
        Optional<User> user = slackUsers.stream().filter(usr -> email.equalsIgnoreCase(usr.getProfile().getEmail())).findFirst();
        return user.orElse(null);
    }


}
