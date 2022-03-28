package com.controller;

import org.springframework.stereotype.Component;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;
@Component
public class Client {

    public void email(String name,String sj) throws Exception {
        Properties props = new Properties();
        //设置邮件地址
        props.put("mail.smtp.host", "smtp.126.com");
        //开启认证
        props.put("mail.smtp.auth", "true");
        Session session = Session.getDefaultInstance(props, null);
        Transport transport = session.getTransport();
        //用户名
        String user = "yangrui1262022@126.com";//发送邮件的用户名
        //授权码
        String password = "DZQNKFIARQFWYPRR";//发送邮件的授权码
        transport.connect(user, password);
        //创建邮件消息
        MimeMessage msg = new MimeMessage(session);
        msg.setSentDate(new Date());
        //邮件发送人
        InternetAddress fromAddress = new InternetAddress(user, "邮件服务");
        msg.setFrom(fromAddress);
        //邮件接收人
        String to = name;//接收邮件的用户名
        InternetAddress[] toAddress = new InternetAddress[]{new InternetAddress(to)};
        msg.setRecipients(Message.RecipientType.TO, toAddress);
        //邮件主题
        msg.setSubject("测试邮件发送", "UTF-8");
        //邮件内容和格式 验证码位置是发送文件的内容
        msg.setContent("欢迎注册爱旅行用户"+name+"，您的激活码是:"+sj, "text/html;charset=UTF-8");
        msg.saveChanges();
        //发送
        transport.sendMessage(msg, msg.getAllRecipients());
    }

}
