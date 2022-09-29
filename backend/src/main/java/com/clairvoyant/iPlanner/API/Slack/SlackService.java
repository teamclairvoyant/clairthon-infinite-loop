package com.clairvoyant.iPlanner.API.Slack;

import com.clairvoyant.iPlanner.Utility.Utility;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import com.slack.api.model.block.*;
import com.slack.api.model.block.composition.ConfirmationDialogObject;
import com.slack.api.model.block.composition.MarkdownTextObject;
import com.slack.api.model.block.composition.PlainTextObject;
import com.slack.api.model.block.element.ButtonElement;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class SlackService {

    private static SlackService instance;

    /**
     * Singleton class Lazy initialization with Double check locking
     *
     * @return SlackService
     */
    public static SlackService getInstance() {
        if (instance == null) {
            synchronized (SlackService.class) {
                instance = new SlackService();
            }
        }
        return instance;
    }

    /**
     * private constructor for singleton class
     */
    private SlackService() {
    }


    public ChatPostMessageResponse interviewNotification(
            String email,
            String name,
            String description,
            String time,
            String meetLink,
            String calendarEventLink) {

        List<LayoutBlock> message = new ArrayList<>();
        message.add(HeaderBlock
                .builder()
                .text(PlainTextObject
                        .builder()
                        .text("*Hi " + name + ":wave: You have been assigned an Interview!*")
                        .build())
                .build());
        message.add(SectionBlock
                .builder()
                .fields(Arrays.asList(
                        MarkdownTextObject
                                .builder()
                                .text(":hourglass: *TIME:* " + time)
                                .build(),
                        MarkdownTextObject
                                .builder()
                                .text(":computer: *GOOGLE MEET:* " + meetLink)
                                .build(),
                        MarkdownTextObject
                                .builder()
                                .text(":memo: *DESCRIPTION:* " + description)
                                .build(),
                        MarkdownTextObject
                                .builder()
                                .text(":spiral_calendar_pad: *CALENDAR:* " + calendarEventLink)
                                .build()
                ))
                .build());
        message.add(DividerBlock
                .builder()
                .build());
        return SlackHelper.publishBlockMessageToUser(email, message);
    }

    public ChatPostMessageResponse interviewConfirmation(
            String email) {

        String description = "The interviewee is a React master developer of 5 years experience. " +
                "Do you accept this interview ?";
        String name = "Abhinov";

        List<LayoutBlock> message = new ArrayList<>();
        message.add(HeaderBlock
                .builder()
                .text(PlainTextObject
                        .builder()
                        .text("*Hi " + name + ":wave: Have Time for an Interview?*")
                        .build())
                .build());
        message.add(SectionBlock
                .builder()
                .fields(Arrays.asList(
                        MarkdownTextObject
                                .builder()
                                .text(":hourglass: *TIME:* " + new Timestamp(System.currentTimeMillis()))
                                .build(),
                        MarkdownTextObject
                                .builder()
                                .text(":memo: *DESCRIPTION:* " + description)
                                .build()
                ))
                .build());
        message.add(DividerBlock
                .builder()
                .build());

        message.add(ActionsBlock
                .builder()
                .elements(Arrays.asList(
                        ButtonElement
                                .builder()
                                .text(PlainTextObject
                                        .builder()
                                        .text(":thumbsup_all: Yes")
                                        .build())
                                .style("primary")
                                .actionId(Utility.UUID()) // todo hold the ID for later used to identify response
                                .build(),
                        ButtonElement
                                .builder()
                                .text(PlainTextObject
                                        .builder()
                                        .text(":thumbsdown: No")
                                        .build())
                                .style("danger")
                                .actionId(Utility.UUID()) // todo hold the ID for later used to identify response
                                .confirm(ConfirmationDialogObject
                                        .builder()
                                        .text(PlainTextObject
                                                .builder()
                                                .text("I conform I *DO NOT* want to take this Interview")
                                                .emoji(true)
                                                .build())
                                        .build())
                                .build()
                ))
                .build());
        return SlackHelper.publishBlockMessageToUser(email, message);
    }
}
