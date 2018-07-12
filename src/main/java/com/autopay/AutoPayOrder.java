package com.autopay;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
/**
 *  @author cinbo
 *  @time 2018-7-9
 *
 */

public class AutoPayOrder {
    public static int doPayByUionPay(String payurl)
    {

        System.out.println("清空邮箱");
        try {
            MailBoxWorker.EmptyMailBox();
        }catch (Exception e)
        {
            e.printStackTrace();
            return 20;
        }
        //后面是selenium chromedriver的实际程序路径
        System.setProperty("webdriver.chrome.driver","/Users/cinbo/Downloads/chromedriver");
        //新建一个WebDriver 的对象，但是new 的是ChromeDriver的驱动
        WebDriver driver =new ChromeDriver();
        try {
            driver.get(payurl);
        }catch (Exception e)
        {
            e.printStackTrace();
            //driver.quit();
            return 1;
        }

        //支付第一个页面，选择支付方式，选择银联支付方式
        System.out.println("进入支付第一个页面,选择支付方式");
        try {
            ((ChromeDriver) driver).findElementByXPath("//input[@name=\"selectbank\" and @onclick=\"javascript:selectbank(252,126,253)\"]").click();
            ((ChromeDriver) driver).findElementByName("submit1").click();
            Thread.sleep(3000);
        } catch(Exception e){
            e.printStackTrace();
            //driver.quit();
            return 2;
        }

        System.out.println("进入网银支付第一个页面");
        try{
            ((ChromeDriver) driver).findElementByName("cardNumber").click();
            ((ChromeDriver) driver).findElementByName("cardNumber").sendKeys("6214830213890626");
            Thread.sleep(1000);
            ((ChromeDriver) driver).findElementById("btnNext").click();
            Thread.sleep(1000);
        }catch(Exception e)
        {
            e.printStackTrace();
            //driver.quit();
            return 3;
        }

        System.out.println("进入网银支付第二个界面");
        try{
            if(((ChromeDriver) driver).findElementById("cellPhoneNumber").isDisplayed())
            {
                ((ChromeDriver) driver).findElementById("cellPhoneNumber").sendKeys("15618897569");
            }
            else
            {
                //System.out.println("已经记住留在银行的电话号码了");
            }
            System.out.println("点击发送验证码");
            ((ChromeDriver) driver).findElementById("btnGetCode").click();
            Thread.sleep(1000);

        }catch (Exception e)
        {
            e.printStackTrace();
            //driver.quit();
            return 4;
        }

        System.out.println("开始接收验证码");
        int limittimes =5;
        int trytimes=0;
        String validcode = "";
        while(trytimes < limittimes ) {
            trytimes++;
            try{
                Thread.sleep(15000);
            }catch (Exception e){}
            validcode = MailBoxWorker.getvalidcode();
            if(validcode.equals("")==true)
            {
                continue;
            }
            else{
                break;
            }

        }

        if(validcode.equals("") == true)
        {
            System.out.println("获取验证码超时");
            //driver.quit();
            return 5;
        }

        System.out.println("提交验证码");
        try{
            ((ChromeDriver) driver).findElementById("smsCode").click();
            ((ChromeDriver) driver).findElementById("smsCode").sendKeys(validcode);
            ((ChromeDriver) driver).findElementById("btnCardPay").click();

        }catch(Exception e)
        {
            e.printStackTrace();
            //driver.quit();
            return 6;
        }

        int timeout=10;
        int timecount=0;

        while(!driver.getCurrentUrl().contains("Payresult.Action")){
            try{
                Thread.sleep(1000);
            }catch (Exception e){}
            timecount += 1;

            if(timecount > timeout) {break;}
        }

        System.out.println("判断此次结果");
        String bodysrc = driver.getPageSource();

        int flag =0;
        if(bodysrc.contains("验证码"))
        {
            System.out.println("验证码有问题");
            return 7;
        }else if(bodysrc.contains("订单未支付成功"))
        {
            System.out.println("订单未支付成功");
            return 8;
        }
        else if(bodysrc.contains("已成功支付"))
        {
            System.out.println("成功支付");
            return 0;
        }else
        {
            System.out.println("未知状态");
            System.out.println("页面信息如下"+bodysrc);
            return 9;
        }

    }
    public static void test(){
        //chromedriver服务地址
        System.setProperty("webdriver.chrome.driver","/Users/cinbo/Downloads/chromedriver");
        //新建一个WebDriver 的对象，但是new 的是FirefoxDriver的驱动
        WebDriver driver =new ChromeDriver();
        //打开指定的网站
        driver.get("http://www.baidu.com");
        //找到kw元素的id，然后输入hello
        driver.findElement(By.id("kw")).sendKeys(new  String[] {"hello"});
        //点击按扭
        driver.findElement(By.id("su")).click();
        try {
            /**
             * WebDriver自带了一个智能等待的方法。
             dr.manage().timeouts().implicitlyWait(arg0, arg1）；
             Arg0：等待的时间长度，int 类型 ；
             Arg1：等待时间的单位 TimeUnit.SECONDS 一般用秒作为单位。
             */
            driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        /**
         * dr.quit()和dr.close()都可以退出浏览器,简单的说一下两者的区别：第一个close，
         * 如果打开了多个页面是关不干净的，它只关闭当前的一个页面。第二个quit，
         * 是退出了所有Webdriver所有的窗口，退的非常干净，所以推荐使用quit最为一个case退出的方法。
         */
        try
        {
            Thread.sleep(30000);
        } catch(Exception e)
        {
            e.printStackTrace();
        }
        driver.quit();//退出浏览器
    }

    public static void main(String[] args) throws Exception{


        BufferedReader br=new BufferedReader(new FileReader("/tmp/payurl.txt"));
        String payurl = br.readLine();
        int rtncode = doPayByUionPay(payurl);
        System.out.println("支付返回："+rtncode);
    }
}

