package com.autopay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class YooxOrderWorkder {
    private HttpClient myhttpclient;
    private String useragent="";
    private CookieStore cookieStore = null;
    private String lastcookie="";


    public  void setCookieStore(HttpResponse httpResponse) {
        System.out.println("----setCookieStore");
        this.cookieStore = new BasicCookieStore();
        // JSESSIONID
        String setCookie = httpResponse.getFirstHeader("Set-Cookie")
                .getValue();
        String JSESSIONID = setCookie.substring("JSESSIONID=".length(),
                setCookie.indexOf(";"));
        System.out.println("JSESSIONID:" + JSESSIONID);
        // 新建一个Cookie
        BasicClientCookie cookie = new BasicClientCookie("JSESSIONID",
                JSESSIONID);
        cookie.setVersion(0);
        cookie.setDomain("127.0.0.1");
        cookie.setPath("/CwlProClient");
        // cookie.setAttribute(ClientCookie.VERSION_ATTR, "0");
        // cookie.setAttribute(ClientCookie.DOMAIN_ATTR, "127.0.0.1");
        // cookie.setAttribute(ClientCookie.PORT_ATTR, "8080");
        // cookie.setAttribute(ClientCookie.PATH_ATTR, "/CwlProWeb");
        cookieStore.addCookie(cookie);

    }

    public void setUseragent(String ua)
    {
        this.useragent = ua;
    }
    public YooxOrderWorkder()
    {
        this.cookieStore = new BasicCookieStore();
        myhttpclient = HttpClients.custom()
                .setDefaultCookieStore(cookieStore).build();

    }
    public String doGet(String geturl)
    {
        HttpGet httpGet = new HttpGet(geturl);
        //httpGet.addHeader("Content-Type","application/json;charset=UTF-8");
        httpGet.addHeader("User-Agent",this.useragent);
        RequestConfig defaultConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build();
        httpGet.setConfig(defaultConfig);
        try{
            HttpResponse httpresp = this.myhttpclient.execute(httpGet);
            /*
            Header[] hds = httpresp.getAllHeaders();
            for(Header hd :hds)
            {
                System.out.println(hd.getName()+":"+hd.getValue());
            }
            System.out.println("statuscode"+httpresp.getStatusLine().getStatusCode());
            System.out.println(httpresp.getEntity().getContentType().getValue());
            if(httpresp.getEntity().isStreaming())
            {
                System.out.println("isStreaming enable");
            }
            */
            String bodysrc = EntityUtils.toString(httpresp.getEntity());
            //System.out.println("返回内容："+ bodysrc);

            return bodysrc;


        }catch(Exception e)
        {
            e.printStackTrace();
            return "";
        }


    }

    public String doWebGet(String geturl)
    {
        HttpGet httpGet = new HttpGet(geturl);
        //httpGet.addHeader("Content-Type","application/json;charset=UTF-8");
        httpGet.addHeader("User-Agent","Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_5) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/11.1.1 Safari/605.1.15");
        RequestConfig defaultConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build();
        httpGet.setConfig(defaultConfig);
        try{
            HttpResponse httpresp = this.myhttpclient.execute(httpGet);
            /*
            Header[] hds = httpresp.getAllHeaders();

            for(Header hd :hds)
            {
                System.out.println(hd.getName()+":"+hd.getValue());
            }
            */

            System.out.println("statuscode"+httpresp.getStatusLine().getStatusCode());
            //System.out.println(httpresp.getEntity().getContentType().getValue());

            String bodysrc = EntityUtils.toString(httpresp.getEntity());
            //System.out.println("返回内容："+ bodysrc);

            return bodysrc;


        }catch(Exception e)
        {
            e.printStackTrace();
            return "";
        }


    }

    public void PrintCookie()
    {
        List<Cookie> cookies = cookieStore.getCookies();
        System.out.println("cookies length"+cookies.size());
        for (int i = 0; i < cookies.size(); i++) {
            System.out.println("Local cookie: " + cookies.get(i));
        }
    }

    public String doPost(String posturl,String jsonstr)
    {
        HttpPost httpPost = new HttpPost(posturl);

        //httpPost.addHeader("Content-Type","application/json;charset=UTF-8");
        httpPost.addHeader("User-Agent",this.useragent);
        RequestConfig defaultConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build();
        httpPost.setConfig(defaultConfig);
        try {
            StringEntity entity = new StringEntity(jsonstr, "utf-8");
            entity.setContentEncoding("UTF-8");
            entity.setContentType("application/json");
            httpPost.setEntity(entity);

            HttpResponse httpresp = this.myhttpclient.execute(httpPost);
            if(httpresp.getStatusLine().getStatusCode() != HttpStatus.SC_OK)
            {

            }
            /*
            Header[] hds = httpresp.getAllHeaders();
            for(Header hd :hds)
            {
                System.out.println(hd.getName()+":"+hd.getValue());
            }
            */




            String rtnstring = EntityUtils.toString(httpresp.getEntity());

            return rtnstring;


        }catch (Exception e)
        {
            e.printStackTrace();
            return "";
        }

    }

    public String doWebPost(String posturl, Map<String,String> map)
    {
        HttpPost httpPost = new HttpPost(posturl);
        HttpClientContext context = HttpClientContext.create();

        httpPost.addHeader("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");
        httpPost.addHeader("User-Agent","Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_5) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/11.1.1 Safari/605.1.15");
        RequestConfig defaultConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build();
        httpPost.setConfig(defaultConfig);

        try {
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            if(map!=null){
                for (Entry<String, String> entry : map.entrySet()) {
                    nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                }
            }
            //设置参数到请求对象中

            httpPost.setEntity(new UrlEncodedFormEntity(nvps));

            System.out.println("请求参数："+nvps.toString());

            HttpResponse httpresp = this.myhttpclient.execute(httpPost,context);
            if(httpresp.getStatusLine().getStatusCode() != HttpStatus.SC_OK)
            {

            }
            String redirectUrl = "";
            List<URI> redirectUrls = context.getRedirectLocations();
            if(redirectUrls!=null){
                redirectUrl = redirectUrls.get(0).toString();
                System.out.println("重定向：" + redirectUrl);
            }

            System.out.println("statuscode :"+httpresp.getStatusLine().getStatusCode());

            /*
            Header[] hds = httpresp.getAllHeaders();
            for(Header hd :hds)
            {
                System.out.println(hd.getName()+":"+hd.getValue());
            }
            */




            String rtnstring = EntityUtils.toString(httpresp.getEntity());

            return rtnstring;


        }catch (Exception e)
        {
            e.printStackTrace();
            return "";
        }

    }
    public static void main(String[] args){
        String rtnstr;
        YooxOrderWorkder yow = new YooxOrderWorkder();
        yow.setUseragent("native-mobile-yoox/2.0 v.6.0.6(build 60603) iPhone8,1 iOS 10.3.3");
        //yow.doGet("http://www.cinbo.cn");

        //用户注册
        Map<String, String> regdata = new HashMap<String, String>();
        regdata.put("gender","D");
        regdata.put("BirthDay","1988-06-27");
        regdata.put("Password","12345678a1");
        regdata.put("Name","我的名字");
        regdata.put("Email","cingx@163.com");
        regdata.put("Surname","");
        String regurl = "https://secure.api.yoox.biz/YooxCore.API/1.0/YOOX_CN/Myoox/Account";
        //rtnstr = yow.doPost(regurl,JSON.toJSONString(regdata));
        //System.out.println(rtnstr);

        //System.out.println("######################################################");

        //用户登录
        Map<String,String> logindata = new HashMap<String, String>();
        logindata.put("Email","cingx@163.com");
        logindata.put("password","12345678a1");
        String jsonstr = JSON.toJSONString(logindata);
        String loginurl = "https://secure.api.yoox.biz/YooxCore.API/1.0/YOOX_CN/Myoox/Login";
        //rtnstr = yow.doPost(loginurl, jsonstr);
        //System.out.println(rtnstr);
        //JSONObject jsonObject = JSONObject.parseObject(rtnstr);
        //System.out.println(jsonObject.toJSONString());
        //JSONObject jsonuserobj = jsonObject.getJSONObject("User");
        //JSONObject jsoncartobj = jsonObject.getJSONObject("CartInfo");


        //访问购物车
        //String geturl = "https://secure.api.yoox.biz/YooxCore.API/1.0/YOOX_CN/Cart?";
        //geturl = geturl + "accessToken="+jsonuserobj.getString("AccessToken") + "&";
        //geturl = geturl + "cartId="+jsoncartobj.getString("CartId") + "&";
        //geturl = geturl + "cartToken="+jsoncartobj.getString("CartToken") + "&";
        //geturl = geturl + "gender="+jsonuserobj.getString("gender") + "&";
        //geturl = geturl + "userId="+String.valueOf(jsonuserobj.getString("idUser"));

        //rtnstr = yow.doGet(geturl);
        //System.out.println(rtnstr);

        //网页登录，
        Map<String, String> weblogindata = new HashMap<String, String>();
        weblogindata.put("Email","cinbo@163.com");
        weblogindata.put("Password","12345678a1");
        weblogindata.put("ReturnUrl","");
        weblogindata.put("LoginPageType","Myoox");



        String webloginurl = "https://store.yoox.cn/cn/myoox/login";
        //String webloginurl = "http://127.0.0.1:8080/test/testpost";
        rtnstr = yow.doWebPost(webloginurl,weblogindata);
        System.out.println(rtnstr);
        //yow.PrintCookie();


        String  orderurl = "https://store.yoox.cn/cn/myoox/Orders/" +
                "OrderDetailsAuth?orderId=0607Y171718407&isRendable=False&childInfo=%5B%5D";

        rtnstr = yow.doWebGet(orderurl);
        System.out.println(rtnstr);





    }
}
