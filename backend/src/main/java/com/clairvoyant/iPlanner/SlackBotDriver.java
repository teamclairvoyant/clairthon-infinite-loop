package com.clairvoyant.iPlanner;

/**
 * Link - https://api.slack.com/start/building/bolt-java#listening
 * set these env variables in IntelliJ configuration window
 * SLACK_BOT_TOKEN=xoxb-xxxxx
 * SLACK_SIGNING_SECRET=xxxxx
 *
 */
public class SlackBotDriver {

//    public static void main(String[] args) throws Exception {
//        App app = new App();
//
//
//        // All the room in the world for your code
//
//        // display home page
//        app.event(AppHomeOpenedEvent.class, (payload, ctx) -> {
//            var appHomeView = view(view -> view
//                    .type("home")
//                    .blocks(asBlocks(
//                            section(section -> section.text(markdownText(mt -> mt.text("Welcome to Interview Planner :tada:")))),
//                            divider(),
//                            section(section -> section.text(markdownText(mt -> mt.text("You will get notified on any interviews scheduled by HR as per your given time slot.")))),
//                            section(section -> section.text(markdownText(mt -> mt.text("In some cases you have to respond to a YES or NO for an interview")))),
//                            actions(actions -> actions
//                                    .elements(asElements(
//                                            button(b -> b.text(plainText(pt -> pt.text("Yes :+1:"))).value("yes").actionId("yes_button")),
//                                            button(b -> b.text(plainText(pt -> pt.text("No :-1:"))).value("no").actionId("no_button"))
//                                    ))
//                            )
//                    ))
//            );
//
//            var res = ctx.client().viewsPublish(r -> r
//                    .userId(payload.getEvent().getUser())
//                    .view(appHomeView)
//            );
//
//            return ctx.ack();
//        });
//
//        app.command("/hello", (req, ctx) -> {
//            return ctx.ack(":wave: Hello! I am Abhinov's Interview Planner");
//        });
//
//        SlackAppServer server = new SlackAppServer(app);
//        server.start();
//    }
}
