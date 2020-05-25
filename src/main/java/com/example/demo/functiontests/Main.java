package com.example.demo.functiontests;

import com.example.demo.DemoApplication;
import org.springframework.boot.SpringApplication;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    static class TestClass {
        public boolean gojudge (String message){
            return message.startsWith("Lira");
        }

        // 具体情况：
        // 今天晚上 [吃] 不 [吃] 饭 = 回答：[吃]饭/不[吃]饭
        // 今天晚上 [要] 不 [要] 吃饭 = 回答：[要]吃饭/不[要]吃饭
        // 今天晚上 [吃饭] 不 [吃饭] = 回答：[吃饭]/不[吃饭]

        // 细节处理
        // 重复词有“！”视为恶意代码，不作回应（没人会用"学!code不学!code"聊天吧）

        public String reply (String msg){

            Pattern splitpattern=Pattern.compile("[不|没]");
            String[] str=splitpattern.split(msg);
            String parta=str[0];
            String partb=str[1];

            System.out.println(partb);

//        if (needreply){
//
//        }
            return msg;

        }

    }



    public static void main(String[] args) {

        TestClass a=new TestClass();
        a.reply("打不打游戏");

    }

}
