package com.goormy.hackathon.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.ScheduledEvent;
import com.goormy.hackathon.lambda.FollowFunction;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ScheduledHandler implements RequestHandler<ScheduledEvent, String> {

    private static final ApplicationContext context = new AnnotationConfigApplicationContext(FollowFunction.class);
    private final FollowFunction followFunction;

    public ScheduledHandler() {
        followFunction = context.getBean(FollowFunction.class);
    }

    @Override
    public String handleRequest(ScheduledEvent event, Context context) {
        followFunction.migrateData();
        return "Data migration completed";
    }
}
