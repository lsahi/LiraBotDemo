package com.example.demo.plugin;

import net.lz1998.cq.event.message.CQGroupMessageEvent;
import net.lz1998.cq.robot.CQPlugin;
import net.lz1998.cq.robot.CoolQ;
import org.springframework.stereotype.Component;

@Component
public class WhatToEatPlugin extends CQPlugin {

    String msg;

    public String replyWhatToEat (String s){

        Double random = Math.random();

    }

    @Override
    public int onGroupMessage(CoolQ cq, CQGroupMessageEvent event) {
        long groupId=event.getGroupId();
        Long memberId=event.getUserId();
        msg=event.getMessage();
        String response;

        if (msg.startsWith("!吃啥")|| msg.startsWith("！吃啥")|| msg.startsWith("!whatToEat")||
                msg.startsWith("！whatToEat")|| msg.startsWith("!whattoeat")|| msg.startsWith("！whattoeat")){
            String reply=reply(msg);
            // cq.sendGroupMsg(groupId,reply,false);
            cq.sendGroupMsg(groupId,reply,false);
        }

        return super.onGroupMessage(cq, event);
    }
}
