package com.clairvoyant.iPlanner.Utility;

import com.slack.api.bolt.App;
import com.slack.api.bolt.jetty.SlackAppServer;
import com.slack.api.model.event.AppHomeOpenedEvent;

import static com.slack.api.model.block.Blocks.*;
import static com.slack.api.model.block.composition.BlockCompositions.markdownText;
import static com.slack.api.model.block.composition.BlockCompositions.plainText;
import static com.slack.api.model.block.element.BlockElements.asElements;
import static com.slack.api.model.block.element.BlockElements.button;
import static com.slack.api.model.view.Views.view;

/**
 * Link - https://api.slack.com/start/building/bolt-java#listening
 * set these env variables in IntelliJ configuration window
 * SLACK_BOT_TOKEN=xoxb-xxxxx
 * SLACK_SIGNING_SECRET=xxxxx
 *
 */
public class SlackBotDriver {
    public static void main(String[] args) throws Exception {
        App app = new App();


        // All the room in the world for your code
        app.event(AppHomeOpenedEvent.class, (payload, ctx) -> {
            var appHomeView = view(view -> view
                    .type("home")
                    .blocks(asBlocks(
                            section(section -> section.text(markdownText(mt -> mt.text("*Welcome to your Abhinov's _App's Home_* :tada:")))),
                            divider(),
                            section(section -> section.text(markdownText(mt -> mt.text("This button won't do much for now but you can set up a listener for it using the `actions()` method and passing its unique `action_id`. See an example on <https://slack.dev/java-slack-sdk/guides/interactive-components|slack.dev/java-slack-sdk>.")))),
                            actions(actions -> actions
                                    .elements(asElements(
                                            button(b -> b.text(plainText(pt -> pt.text("Click me!"))).value("button1").actionId("button_1"))
                                    ))
                            )
                    ))
            );

            var res = ctx.client().viewsPublish(r -> r
                    .userId(payload.getEvent().getUser())
                    .view(appHomeView)
            );

            return ctx.ack();
        });

        SlackAppServer server = new SlackAppServer(app);
        server.start();


    }
}
