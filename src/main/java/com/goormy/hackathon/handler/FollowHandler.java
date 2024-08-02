package com.goormy.hackathon.handler;

import org.springframework.cloud.function.adapter.aws.FunctionInvoker;

public class FollowHandler extends FunctionInvoker {
    private static final String FOLLOW_FUNCTION = "followFunction";

    public FollowHandler() {
        super(FOLLOW_FUNCTION);
    }
}
