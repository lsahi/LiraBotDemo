package com.example.demo.plugin;


import net.lz1998.cq.event.message.CQGroupMessageEvent;
import net.lz1998.cq.robot.CQPlugin;
import net.lz1998.cq.robot.CoolQ;
import org.springframework.stereotype.Component;

@Component
public class RepeatorPlugin extends CQPlugin {

    String lastMsg;
    Integer repeatCount=0;

    public boolean needRepeat(){
        double a=0.8;
        double flag=Math.random();
        return flag < a;
    }

    @Override
    public int onGroupMessage(CoolQ cq, CQGroupMessageEvent event) {
        String msg=event.getMessage();
        Long groupId=event.getGroupId();

        if (msg.equals(lastMsg)){
            repeatCount++;
            if(repeatCount>2){
                if (needRepeat()){
                    cq.sendGroupMsg(groupId,msg,false);
                    repeatCount=0;
                }
            }
        }else{
            repeatCount=0;
            lastMsg=msg;
        }
        return super.onGroupMessage(cq, event);
    }
}
