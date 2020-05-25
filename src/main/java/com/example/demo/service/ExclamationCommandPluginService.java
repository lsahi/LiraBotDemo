package com.example.demo.service;


import java.lang.reflect.Array;

/**
 * 这个service是用来处理以感叹号开头的命令
 */
public class ExclamationCommandPluginService {

    public String roll (String msg){
        msg=msg.substring(1, msg.length());
        String simpleRollTemp=msg.trim();
        if (simpleRollTemp.equals("roll")){
            Long roll=Math.round(6*(Math.random()));
            return (roll.toString());
        }
        String[] rollString=msg.split (" ",2);
        Long rollResult=Math.round(100*(Math.random()));
        return ("你今天"+rollString[1]+"的概率是"+rollResult.toString());
    }

    public String switcher (String msg){

        if (msg.startsWith("!roll")|msg.startsWith("！roll")){
            return (roll(msg));
        }else {
            return "ex";
        }
    }

}
