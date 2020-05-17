package com.example.demo.plugin;


import net.lz1998.cq.event.message.CQGroupMessageEvent;
import net.lz1998.cq.robot.CQPlugin;
import net.lz1998.cq.robot.CoolQ;
import org.springframework.stereotype.Component;

@Component
public class BlameLiraResopnsePlugin extends CQPlugin {

    String msg;

    @Override
    public int onGroupMessage(CoolQ cq, CQGroupMessageEvent event) {

        long groupId=event.getGroupId();
        String inMsg=event.getMessage();

        boolean hello=inMsg.matches("Lira你(.*)|lira你(.*)");


//        这里切割替换字符串
//        Lira你好坏 -> 你才坏
//        这里好像还挺麻烦的，先mark
//
        String sub="";
        sub=inMsg.replaceAll("Lira你|lira你","");

        //先做一个switch-case吧
        if (hello){
            switch (sub){
                case "好":
                    cq.sendGroupMsg(groupId,"你坏",false);
                    break;
                case "吗":
                    cq.sendGroupMsg(groupId,"?",false);
                    break;
                case "好色哦":
                    cq.sendGroupMsg(groupId,"那色图呢",false);
                    break;
                default:
                    cq.sendGroupMsg(groupId,"你才"+sub, false);
            }
            //hello这个插件走完了就不管别的了
            return MESSAGE_BLOCK;
        }

        return super.onGroupMessage(cq, event);
    }
}
