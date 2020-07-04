package com.example.demo.service;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;

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

    public String replyWhatToEat (){

        String uriStartWith = new String("https://liyuankun.cn/api/v1/recipe/");
        Integer page;//这里其实是count，/api/v1/recipe/?limit=20&offset=20
        int pos=(int)Math.round(20*(Math.random())); //目前第三方api的每页大小是20

        //向第三方请求两次，第一次是拿基础信息，第二次才是实际请求
        String firstTry = ApacheHttpTemplate.doGet(uriStartWith);
        Map<String,Object> firstTryResponseBody = new HashMap<String,Object>(JSONObject.parseObject(firstTry));
        page = (Integer)firstTryResponseBody.get("count");

        long offset=Math.round(page*(Math.random()));
        String uri = uriStartWith + "?limit=20&offset=" + offset;
        String finalTry = ApacheHttpTemplate.doGet(uri);
        //这回是要真正返回吃啥的了
        Map<String,Object> finalTryResponseBody = new HashMap<>(JSONObject.parseObject(finalTry));
        JSONArray results = (JSONArray) finalTryResponseBody.get("results");

        JSONObject singleFood = (JSONObject) results.get(pos);
        String title = singleFood.get("title").toString();
        String recipeId = singleFood.get("recipeId").toString();


        return title + " " + recipeId;
    }

    public String replyHowToCook (String s) {
        //先不写了
        String[] eatstring = s.split (" ",2);
        String recipeNo = eatstring[1];
        String uri = new String("https://liyuankun.cn/api/v1/recipe/"+recipeNo);

        //temp resp
        return "春日リラ现已和\"北京百度网讯科技有限公司\"达成了战略合作关系\n百度一下，你就知道!\n戳 >> https://www.baidu.com/";
    }

    public String switcher (String msg){

        if (msg.startsWith("!roll")|msg.startsWith("！roll")){
            // roll点功能
            return (roll(msg));
        } else if (msg.startsWith("!version")|msg.startsWith("！version")){
            //看心情随便写个version，哈哈
            return ("v0.0.0");
        } else if (msg.startsWith("!吃啥")|| msg.startsWith("！吃啥")|| msg.startsWith("!whatToEat")||
                msg.startsWith("！whatToEat")|| msg.startsWith("!whattoeat")|| msg.startsWith("！whattoeat")){
            //吃啥
            return (replyWhatToEat());
        } else if (msg.startsWith("!咋做")|| msg.startsWith("！咋做")|| msg.startsWith("!howToCook")||
                msg.startsWith("！howToCook")|| msg.startsWith("!howtocook")|| msg.startsWith("！howtocook")){
            //咋做 这个和上面的一样，接了个菜谱api
            return (replyHowToCook(msg));
        } else {
            return "命令不存在，输入!help哦";
        }
    }

}
