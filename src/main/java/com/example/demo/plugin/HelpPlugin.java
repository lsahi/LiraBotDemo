package com.example.demo.plugin;

import net.lz1998.cq.event.message.CQGroupMessageEvent;
import net.lz1998.cq.robot.CQPlugin;
import net.lz1998.cq.robot.CoolQ;
import org.springframework.stereotype.Component;

@Component
public class HelpPlugin extends CQPlugin {

    String msg;

    String replyhelp =
            "!help                      - 帮助文档\n" +
            "Lira/lira + 【询问的事】     - 问问Lira要不要\n";

    @Override
    public int onGroupMessage(CoolQ cq, CQGroupMessageEvent event) {

        Long groupId=event.getGroupId();
        msg=event.getMessage();


        if (msg.equals("!help")||msg.equals("！help")){
            cq.sendGroupMsg(groupId,replyhelp,false);
        }

        return super.onGroupMessage(cq, event);
    }
}
