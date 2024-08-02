package com.goormy.hackathon.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.ScheduledEvent;
import com.goormy.hackathon.lambda.FollowFunction;
import org.springframework.cloud.function.adapter.aws.FunctionInvoker;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ScheduledHandler extends FunctionInvoker {
    private static final String MIGRATE = "migrationFunction";

    public ScheduledHandler() {
        super(MIGRATE);
    }

}
