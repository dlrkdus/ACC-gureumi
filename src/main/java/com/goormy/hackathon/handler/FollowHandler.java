package com.goormy.hackathon.handler;

import org.springframework.cloud.function.adapter.aws.FunctionInvoker;

public class FollowHandler extends FunctionInvoker {
    private static String FollowHandler = "followFunction";

    public FollowHandler() {
        super(FollowHandler);
    }
}
