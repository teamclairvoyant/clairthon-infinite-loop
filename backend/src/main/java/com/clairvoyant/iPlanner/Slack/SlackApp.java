package com.clairvoyant.iPlanner.Slack;

import com.slack.api.bolt.App;
import com.slack.api.bolt.AppConfig;
import com.slack.api.methods.response.views.ViewsPublishResponse;
import com.slack.api.model.event.AppHomeOpenedEvent;
import com.slack.api.model.view.View;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.slack.api.model.block.Blocks.*;
import static com.slack.api.model.block.composition.BlockCompositions.markdownText;
import static com.slack.api.model.block.composition.BlockCompositions.plainText;
import static com.slack.api.model.block.element.BlockElements.asElements;
import static com.slack.api.model.block.element.BlockElements.button;
import static com.slack.api.model.view.Views.view;

@Configuration
public class SlackApp {
    @Bean
    public App initSlackApp() {

        AppConfig appConfig = new AppConfig();
        appConfig.setSigningSecret("e7e55498e8fa71bfa20fb398f7e81a3c");
        appConfig.setSingleTeamBotToken("xoxb-1597172380741-4097046480819-0veZK0aW4MYvD7WcYPwwSXhh");

        App app = new App(appConfig);

        app.command("/hello", (req, ctx) -> {
            return ctx.ack("OK, let's do it!");
        });

        // display home page
        app.event(AppHomeOpenedEvent.class, (payload, ctx) -> {
            View appHomeView = view(view -> view
                    .type("home")
                    .blocks(asBlocks(
                            section(section -> section.text(markdownText(mt -> mt.text("Welcome to Interview Planner4 :tada:")))),
                            divider(),
                            section(section -> section.text(markdownText(mt -> mt.text("You will get notified on any interviews scheduled by HR as per your given time slot.")))),
                            section(section -> section.text(markdownText(mt -> mt.text("In some cases you have to respond to a YES or NO for an interview")))),
                            actions(actions -> actions
                                    .elements(asElements(
                                            button(b -> b.text(plainText(pt -> pt.text("Yes :+1:"))).value("yes").actionId("yes_button")),
                                            button(b -> b.text(plainText(pt -> pt.text("No :-1:"))).value("no").actionId("no_button"))
                                    ))
                            )
                    ))
            );

            ViewsPublishResponse res = ctx.client().viewsPublish(r -> r
                    .userId(payload.getEvent().getUser())
                    .view(appHomeView)
            );

            return ctx.ack();
        });
        return app;
    }
}
