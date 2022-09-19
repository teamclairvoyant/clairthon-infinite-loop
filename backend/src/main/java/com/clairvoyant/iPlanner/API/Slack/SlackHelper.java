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
import com.slack.api.model.block.LayoutBlock;
import com.slack.api.status.v2.StatusApiException;
import com.slack.api.status.v2.model.CurrentStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class SlackHelper {

    private static final Logger logger = LoggerFactory.getLogger(SlackHelper.class);

    // todo :: token should not expire allow Auto rotation of BOT_TOKEN
    //  opt in for 'Advanced token security via token rotation'
    public static final String BOT_TOKEN = "xoxb-1597172380741-4097046480819-wQmYVOQMcyRLFvMvduayI9jM";
    public static final String APP_TOKEN = "xapp-1-A043JR2B0AC-4121068359728-0e85bc9defce66654578e1bcfac721112e7cd504f8cda2d40350c0e92ee28140";

    /**
     * Find conversation/channel ID for the channel name using the conversations.list method
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
     * Post a message to a channel  using channel name and the message
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
     * Post a block message (with rich-text) to a channel  using channel name
     * eg block message - "[{\"type\": \"divider\"}]"
     */
    public static ChatPostMessageResponse publishBlockMessageToChannel(String channelName, List<LayoutBlock> message) {
        String channelId = getChannelId(channelName);
        // you can get this instance via ctx.client() in a Bolt app
        MethodsClient client = Slack.getInstance().methods();
        try {
            // Call the chat.postMessage method using the built-in WebClient
            ChatPostMessageResponse result = client.chatPostMessage(r -> r
                            // The token you used to initialize your app
                            .token(BOT_TOKEN)
                            .channel(channelId)
                            .blocks(message)
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
     * Post a message to a person/user using their email
     */
    public static ChatPostMessageResponse publishMessageToUser(String email, String message) {
        User user = getSlackUserId(email);
        MethodsClient client = Slack.getInstance().methods();
        try {
            ChatPostMessageResponse result = client.chatPostMessage(r -> r
                    .token(BOT_TOKEN)
                    .channel(user.getId())
                    .text(message)
            );
            logger.info("result {}", result);
            return result;
        } catch (IOException | SlackApiException e) {
            logger.error("error: {}", e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Post a block message to a person/user using their email
     */
    public static ChatPostMessageResponse publishBlockMessageToUser(String email, List<LayoutBlock> message) {
        User user = getSlackUserId(email);
        MethodsClient client = Slack.getInstance().methods();
        try {
            ChatPostMessageResponse result = client.chatPostMessage(r -> r
                    .token(BOT_TOKEN)
                    .channel(user.getId())
                    .blocks(message)
            );
            logger.info("result {}", result);
            return result;
        } catch (IOException | SlackApiException e) {
            logger.error("error: {}", e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Get all slack users in the workspace
     */
    public static List<User> getSlackUsers() {
        MethodsClient client = Slack.getInstance().methods();
        try {
            UsersListResponse users = client.usersList(r -> r.token(BOT_TOKEN));
            if (!Utility.isEmptyString(users.getError())) {
                throw new RuntimeException(users.getError());
            }
            users.getMembers().forEach(user -> System.out.println(user.toString()));
            return users.getMembers();
        } catch (IOException | SlackApiException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Get slack user id for an email
     */
    public static User getSlackUserId(String email) {
        List<User> slackUsers = getSlackUsers();
        Optional<User> user = slackUsers.stream().filter(usr -> email.equalsIgnoreCase(usr.getProfile().getEmail())).findFirst();
        return user.orElse(null);
    }

    /**
     * The Slack Status API describes the health of the Slack product.
     * When there’s an incident, outage, or maintenance,
     * the Slack Status API reflects all the information
     */
    public static CurrentStatus getCurrentStatus() throws IOException, StatusApiException {
        Slack slack = Slack.getInstance();
        return slack.status().current();
    }


}
