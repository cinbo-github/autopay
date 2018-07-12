package com.autopay;
import javax.mail.*;
import java.io.IOException;
import java.util.Properties;
import java.security.Security;
/**
 * @author cinbo
 * @time 2018-7-9
 * */

public class MailBoxWorker {
        /**
         * 获得邮件文本内容
         * @param part 邮件体
         * @param content 存储邮件文本内容的字符串
         * @throws MessagingException
         * @throws IOException
         */
        public static void getMailTextContent(Part part, StringBuffer content) throws MessagingException, IOException {
            //如果是文本类型的附件，通过getContent方法可以取到文本内容，但这不是我们需要的结果，所以在这里要做判断
            boolean isContainTextAttach = part.getContentType().indexOf("name") > 0;
            if (part.isMimeType("text/*") && !isContainTextAttach) {
                content.append(part.getContent().toString());
            } else if (part.isMimeType("message/rfc822")) {
                getMailTextContent((Part)part.getContent(),content);
            } else if (part.isMimeType("multipart/*")) {
                Multipart multipart = (Multipart) part.getContent();
                int partCount = multipart.getCount();
                for (int i = 0; i < partCount; i++) {
                    BodyPart bodyPart = multipart.getBodyPart(i);
                    getMailTextContent(bodyPart,content);
                }
            }
        }

        /**
         * 为了减少干扰，在开始接收验证短信前，删除邮箱中其他的邮件
         * */

        public static int EmptyMailBox()
        throws Exception
        {
            Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
            final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";

            // 创建一个有具体连接信息的Properties对象
            Properties prop = new Properties();
            prop.setProperty("mail.debug", "false");
            prop.setProperty("mail.store.protocol", "pop3");
            prop.setProperty("mail.pop3.host", MAIL_SERVER_HOST);
            prop.setProperty("mail.pop3.socketFactory.class", SSL_FACTORY);
            prop.setProperty("mail.pop3.port", "995");
            prop.setProperty("mail.pop3.socketFactory.port", "995");
            prop.setProperty("mail.pop3.auth", "true");
            // 1、创建session
            Session session = Session.getInstance(prop);
            // 2、通过session得到Store对象
            Store store = session.getStore();
            // 3、连上邮件服务器
            store.connect(MAIL_SERVER_HOST, MAIL_USER, MAIL_PASSWORD);
            // 4、获得邮箱内的邮件夹
            Folder folder = store.getFolder("inbox");
            folder.open(Folder.READ_WRITE);
            // 获得邮件夹Folder内的所有邮件Message对象
            Message[] messages = folder.getMessages();
            int mailnumber = messages.length;
            for (int i = 0; i < mailnumber; i++) {
                messages[i].setFlag(Flags.Flag.DELETED,true);
            }

            // 5、关闭
            folder.close(true);
            store.close();
            //System.out.println("删除邮件数:"+messages.length);
            return mailnumber;
        }

        /**
         * 获取 短信valid code
         * */
        public static String getvalidcode()
        {
            //判断短信是 银联短信的值 是
            String condition1 = "中国银联";
            String condition2 = "不要告诉任何人";
            try {
                Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
                final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";

                // 创建一个有具体连接信息的Properties对象
                Properties prop = new Properties();
                prop.setProperty("mail.debug", "false");
                prop.setProperty("mail.store.protocol", "pop3");
                prop.setProperty("mail.pop3.host", MAIL_SERVER_HOST);
                prop.setProperty("mail.pop3.socketFactory.class", SSL_FACTORY);
                prop.setProperty("mail.pop3.port", "995");
                prop.setProperty("mail.pop3.socketFactory.port", "995");
                prop.setProperty("mail.pop3.auth", "true");
                // 1、创建session
                Session session = Session.getInstance(prop);
                // 2、通过session得到Store对象
                Store store = session.getStore();
                // 3、连上邮件服务器
                store.connect(MAIL_SERVER_HOST, MAIL_USER, MAIL_PASSWORD);
                // 4、获得邮箱内的邮件夹
                Folder folder = store.getFolder("inbox");
                folder.open(Folder.READ_WRITE);
                // 获得邮件夹Folder内的所有邮件Message对象
                Message[] messages = folder.getMessages();
                int mailnumber = messages.length;
                if (mailnumber == 0) {
                    folder.close(true);
                    store.close();
                    return "";
                }
                int mailindex=-1;
                for (int i = 0; i < messages.length; i++) {

                    String subject = messages[i].getSubject();
                    if(subject.indexOf("valid code") == -1)
                    {
                        messages[i].setFlag(Flags.Flag.DELETED,true);

                    }
                    else
                    {
                        mailindex = i;
                        break;
                    }
                }
                if(mailindex == -1 ) {
                    folder.close(true);
                    store.close();
                    return "";
                }


                StringBuffer content = new StringBuffer(30);
                getMailTextContent(messages[mailindex], content);
                String validcode = "";
                if(content.indexOf(condition1) != -1 && content.indexOf(condition2) != -1)
                {
                    int index = content.indexOf("验证码");
                    validcode = content.substring(index+3,index+9);
                }

                messages[mailindex].setFlag(Flags.Flag.DELETED,true);
                folder.close(true);
                store.close();
                return validcode;

            }catch (Exception e)
            {
                e.printStackTrace();
                return "";
            }
        }

        public static void displayallemail() throws Exception
        {
            Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
            final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";

            // 创建一个有具体连接信息的Properties对象
            Properties prop = new Properties();
            prop.setProperty("mail.debug", "false");
            prop.setProperty("mail.store.protocol", "pop3");
            prop.setProperty("mail.pop3.host", MAIL_SERVER_HOST);
            prop.setProperty("mail.pop3.socketFactory.class", SSL_FACTORY);
            prop.setProperty("mail.pop3.port", "995");
            prop.setProperty("mail.pop3.socketFactory.port", "995");
            prop.setProperty("mail.pop3.auth", "true");
            // 1、创建session
            Session session = Session.getInstance(prop);
            // 2、通过session得到Store对象
            Store store = session.getStore();
            // 3、连上邮件服务器
            store.connect(MAIL_SERVER_HOST, MAIL_USER, MAIL_PASSWORD);
            // 4、获得邮箱内的邮件夹
            Folder folder = store.getFolder("inbox");
            folder.open(Folder.READ_WRITE);
            // 获得邮件夹Folder内的所有邮件Message对象
            Message[] messages = folder.getMessages();
            System.out.println("收件箱邮件数:"+messages.length);
            for (int i = 0; i < messages.length; i++) {
                String subject = messages[i].getSubject();
                String from = (messages[i].getFrom()[0]).toString();

                System.out.println("第 " + (i + 1) + "封邮件的主题：" + subject);
                System.out.println("第 " + (i + 1) + "封邮件的发件人地址：" + from);
                //System.out.println(messages[i].getContent().toString());
                StringBuffer content = new StringBuffer(30);
                getMailTextContent(messages[i], content);
                System.out.println("邮件正文：" + (content.length() > 1000 ? content.substring(0,1000) + "..." : content));
                //messages[i].setFlag(Flags.Flag.DELETED,true);
            }

            // 5、关闭
            folder.close(true);
            store.close();

        }

        /**
         * 邮箱用户民
         * */
        final static String MAIL_USER = "3764579@qq.com";
        /**
         * 邮箱密码
         * */
        final static String MAIL_PASSWORD = "upnkafsoroukbigh";
        /**
         *邮箱服务器
         * */
        public final static String MAIL_SERVER_HOST = "pop.qq.com";
        /**
         * 文本内容类型
         * */
        public final static String TYPE_HTML = "text/html;charset=UTF-8";
        /**
         * 发件人
         * */
        public final static String MAIL_FROM = "[email protected]";
        /**
         * 收件人
         * */
        public final static String MAIL_TO = "[email protected]";
        /**
         * 抄送人
         * */
        public final static String MAIL_CC = "[email protected]";
        /**
         * 密送人
         * */
        public final static String MAIL_BCC = "[email protected]";
        public static void main(String[] args) throws Exception {

            //displayallemail();
            System.out.println("branch2 测试代码");
            System.out.println("测试代码");
            System.out.println("验证码:"+getvalidcode());

        }
}


