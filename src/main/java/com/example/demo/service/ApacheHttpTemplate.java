package com.example.demo.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.pojo.GetFoodResult;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.web.bind.annotation.RequestBody;


public class ApacheHttpTemplate {

    private final String USER_AGENT = "Mozilla/5.0";
    private static String tokenString = "";
    private static String AUTH_TOKEN_EXPIRED = "AUTH_TOKEN_EXPIRED";
    private static CloseableHttpClient httpClient = null;

    /**
     * 以get方式调用第三方接口
     * @param url
     * @return
     */
    public static String doGet(String url){
        //创建HttpClient对象
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet get = new HttpGet(url);

        try {
            //api_gateway_auth_token自定义header头，用于token验证使用
            //get.addHeader("api_gateway_auth_token", tokenString);
            //get.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.81 Safari/537.36");
            HttpResponse response = httpClient.execute(get);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                //返回json格式
                String res = EntityUtils.toString(response.getEntity());

                return res;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 以post方式调用第三方接口
     * @param url
     * @param json
     * @return
     */
    public static String doPost(String url, JSONObject json){

        try {
            if (httpClient == null){
                httpClient = HttpClientBuilder.create().build();
            }

            HttpPost post = new HttpPost(url);

            if (tokenString != null && !tokenString.equals("")){
                tokenString = getToken();
            }

            //api_gateway_auth_token自定义header头，用于token验证使用
            post.addHeader("api_gateway_auth_token", tokenString);
            post.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.81 Safari/537.36");

            StringEntity s = new StringEntity(json.toString());
            s.setContentEncoding("UTF-8");
            //发送json数据需要设置contentType
            s.setContentType("application/x-www-form-urlencoded");
            //设置请求参数
            post.setEntity(s);
            HttpResponse response = httpClient.execute(post);

            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                //返回json格式
                String res = EntityUtils.toString(response.getEntity());
                return res;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (httpClient != null){
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 获取第三方接口的token
     */
    public static String getToken(){

        String token = "";

        JSONObject object = new JSONObject();
        object.put("appid", "appid");
        object.put("secretkey", "secretkey");

        try {
            if (httpClient == null){
                httpClient = HttpClientBuilder.create().build();
            }
            HttpPost post = new HttpPost("http://localhost/login");

            post.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.81 Safari/537.36");

            StringEntity s = new StringEntity(object.toString());
            s.setContentEncoding("UTF-8");
            //发送json数据需要设置contentType
            s.setContentType("application/x-www-form-urlencoded");
            //设置请求参数
            post.setEntity(s);
            HttpResponse response = httpClient.execute(post);

            //这里可以把返回的结果按照自定义的返回数据结果，把string转换成自定义类
            //ResultTokenBO result = JSONObject.parseObject(response, ResultTokenBO.class);

            //把response转为jsonObject
//            JSONObject result = JSONObject.parseObject(response);
//            if (result.containsKey("token")){
//                token = result.getString("token");
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return token;
    }

    public static void main (String[] args){
        ApacheHttpTemplate tmp = new ApacheHttpTemplate();
        try{
            String res = tmp.doGet("https://liyuankun.cn/api/v1/recipe/");
            Map<String,Object> firstTryResponseBody = new HashMap<String,Object>(JSONObject.parseObject(res));

            String uriStartWith = new String("https://liyuankun.cn/api/v1/recipe/");
            Integer page;//这里其实是count，/api/v1/recipe/?limit=20&offset=20
            int pos=(int)Math.round(20*(Math.random())); //目前第三方api的每页大小是20

            //向第三方请求两次，第一次是拿基础信息，第二次才是实际请求
            page = (Integer) firstTryResponseBody.get("count");

            long offset=Math.round(page*(Math.random()));
            String uri = uriStartWith + "?limit=20&offset=" + offset;
            String finalTry = ApacheHttpTemplate.doGet(uri);
            //这回是要真正返回吃啥的了
            Map<String,Object> finalTryResponseBody = new HashMap<>(JSONObject.parseObject(finalTry));
            JSONArray results = (JSONArray) finalTryResponseBody.get("results");
            System.out.println("test\n"
                    + finalTryResponseBody
                    + "\nresults"
                    + results.get(pos) + "\n"
                    + results.get(pos).getClass()
                    + "\ntestaaaaaa"
                    + "\n" + JSONObject.parseObject(finalTry).getClass()
            );
            JSONObject singleResult = (JSONObject) results.get(pos);
            System.out.println("\n" + singleResult.get("title"));
        }catch (Exception e){
            System.out.println(e.toString());
        }
    }

}
