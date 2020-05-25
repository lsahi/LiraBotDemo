package com.example.demo.plugin;

import com.example.demo.service.ExclamationCommandPluginService;
import net.lz1998.cq.event.message.CQGroupMessageEvent;
import net.lz1998.cq.robot.CQPlugin;
import net.lz1998.cq.robot.CoolQ;
import org.springframework.stereotype.Component;

@Component
public class CommandPlugin extends CQPlugin {

    @Override
    public int onGroupMessage(CoolQ cq, CQGroupMessageEvent event) {

        long groupId = event.getGroupId();
        String msg = event.getMessage();
        String resp="";
        ExclamationCommandPluginService exclamation=new ExclamationCommandPluginService();
        if (msg.startsWith("!")|msg.startsWith("！")){
            resp=exclamation.switcher(msg);
            cq.sendGroupMsg(groupId,resp,false);
        }


        return super.onGroupMessage(cq, event);
    }

}
