package com.goormy.hackathon.handler;

import org.springframework.cloud.function.adapter.aws.FunctionInvoker;

public class ScheduledHandler extends FunctionInvoker {
    private static String ScheduledHandler = "scheduledFunction";

    public ScheduledHandler() {
        super(ScheduledHandler);
    }
}
