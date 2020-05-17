package com.example.demo.plugin;

import net.lz1998.cq.event.message.CQGroupMessageEvent;
import net.lz1998.cq.robot.CQPlugin;
import net.lz1998.cq.robot.CoolQ;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class AskLiraIfPlugin extends CQPlugin {


    // 具体情况：
    // 今天晚上 [吃] 不 [吃] 饭 = 回答：[吃]饭/不[吃]饭
    // 今天晚上 [要] 不 [要] 吃饭 = 回答：[要]吃饭/不[要]吃饭
    // 今天晚上 [吃饭] 不 [吃饭] = 回答：[吃饭]/不[吃饭]

    // 细节处理 TODO
    // 重复词有“！”视为恶意代码，不作回应（没人会用"学!code不学!code"聊天吧）

    String msg="";
    String lastMsg="";
    Integer repeatCount=0;
    String response="";
    String lastResponse="";
    String tempresponse="";

    public String judgeIfDo(String msg,String lastResponse,Integer repeatCount){
        //有人问过就回回过的内容，没人问过就随机一个
        double a=0.5;
        double flag=Math.random();
        String temp;
        if (flag<a){
            temp = "不"+msg;
        }else {
            temp =msg;
        }
        if (repeatCount>1){
            temp=lastResponse;
        }
        lastResponse=temp;
        return lastResponse;
    }

    public String reply (String msg){
//        testdata:

//        String line="吃没吃";
//        boolean needreply = gojudge(msg);
//        Pattern p= Pattern.compile("[不|没]");
//        Matcher m=p.matcher(line);
//        String[] str=p.split(line);
//        Boolean dostr = msg.matches("");
//        System.out.println("split");
//        System.out.println(str[0]+str[1]);

        //这里逻辑没想好，先置空
        //Lira aaaaa不bb -> bb/不bb
        String sub="";
        sub=msg.replaceAll("Lira|lira","");

        //目前有在工作的逻辑
        Pattern splitpattern=Pattern.compile("[不|没]");
        String[] str=splitpattern.split(msg);
        String parta=str[0];
        String partb=str[1];

        return judgeIfDo(partb,lastResponse,repeatCount);

    }

    //假如连续问了几次同样的问题之后要有情绪表现
    //现在不接数据库，暂且和复读机的逻辑一致
    public void renderCurrentResponse (String msg){

        if (msg.equals(lastMsg)){
            repeatCount++;
            switch (repeatCount){
                case 1:
                    break;
                case 2:
                    tempresponse=lastResponse+"!";
                case 3:
                    tempresponse=lastResponse+"!!";
                default:
                    tempresponse="你是复读机吗???\n是是是是是是是";
            }
        }else{
            repeatCount=0;
            tempresponse="";
            lastResponse="";
            lastMsg=msg;
        }
    }



    @Override
    public int onGroupMessage(CoolQ cq, CQGroupMessageEvent event) {
        long groupId=event.getGroupId();
        Long memberId=event.getUserId();
        msg=event.getMessage();
        String response;

        if (msg.startsWith("Lira")){
            String reply=reply(msg);
            renderCurrentResponse(reply);
            cq.sendGroupMsg(groupId,tempresponse,false);
        }

        return super.onGroupMessage(cq, event);
    }
}
