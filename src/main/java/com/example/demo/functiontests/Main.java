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

            String line="吃没吃";
            Pattern p= Pattern.compile("[不|没]");
            //Pattern p= Pattern.compile("(.+)[is]\\1");
            Matcher m=p.matcher(line);
            String[] str=p.split(line);
            Boolean dostr = msg.matches("");
            System.out.println("split");
            System.out.println(str[0]+str[1]);
            boolean needreply = gojudge(msg);


//        if (needreply){
//
//        }
            return msg;

        }

    }



    public static void main(String[] args) {

        TestClass a=new TestClass();
        a.reply("a");

    }

}
