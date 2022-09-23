package com.clairvoyant.iPlanner.API.Slack;

import com.clairvoyant.iPlanner.Shared.MainMongoDao;
import com.clairvoyant.iPlanner.Utility.MongoDBConnectionInfo;
import com.slack.api.bolt.App;
import com.slack.api.bolt.AppConfig;
import com.slack.api.methods.response.views.ViewsPublishResponse;
import com.slack.api.model.event.AppHomeOpenedEvent;
import com.slack.api.model.view.View;
import org.bson.Document;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.slack.api.model.block.Blocks.*;
import static com.slack.api.model.block.composition.BlockCompositions.markdownText;
import static com.slack.api.model.block.composition.BlockCompositions.plainText;
import static com.slack.api.model.block.element.BlockElements.asElements;
import static com.slack.api.model.block.element.BlockElements.button;
import static com.slack.api.model.view.Views.view;

@Configuration
public class SlackApp {

    @Bean
    public App initSlackApp() throws Exception {

        App app = new App(AppConfig.builder().singleTeamBotToken(SlackHelper.BOT_TOKEN).build());

        app.command("/hello", (req, ctx) -> {
            return ctx.ack("OK, let's do it!");
        });

        app.command("/test", (req, ctx) -> {
            if (MainMongoDao.getInstance().testMongoConnection()) {
                return ctx.ack("Success. MongoDB is UP");
            } else {
                return ctx.ack("Failed to connect to MongoDB");
            }
        });

        app.command("/interviews", (req, ctx) -> {
            List<Document> allDocuments = MainMongoDao.getInstance().getAllDocuments(MongoDBConnectionInfo.events_col);
            Stream<String> objectStream = allDocuments.stream().map(Document::toJson);
            return ctx.ack("Here are your scheduled interviews :: \n" + objectStream.collect(Collectors.toList()));
        });

        // display home page
        app.event(AppHomeOpenedEvent.class, (payload, ctx) -> {
            View appHomeView = view(view -> view
                    .type("home")
                    .blocks(asBlocks(
                            section(section -> section.text(markdownText(mt -> mt.text("Welcome to Interview Planner :tada:")))),
                            section(section -> section.text(markdownText(mt -> mt.text("You will get notified on any interviews scheduled by HR as per your given time slot.")))),
                            divider(),
                            section(section -> section.text(markdownText(mt -> mt.text("SYSTEM INFORMATION")))),
                            section(section -> section.text(markdownText(mt -> mt.text("Current Time :: " + new Timestamp(System.currentTimeMillis()))))),
                            section(section -> section.text(markdownText(mt -> mt.text("Available Memory :: " + Runtime.getRuntime().freeMemory())))),
                            section(section -> section.text(markdownText(mt -> mt.text("Total Memory :: " + Runtime.getRuntime().totalMemory())))),
                            divider(),
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


        // todo if this websocket server starts then http APIs are not working
//        SocketModeApp socketModeApp = new SocketModeApp(SlackHelper.APP_TOKEN, app);
//        socketModeApp.start();


        return app;
    }


}
