package org.one.gene.domain.service;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

import org.one.gene.util.mail.MailSenderInfo;
import org.one.gene.util.mail.SimpleMailSender;

import com.google.common.collect.Maps;

public class SendEmailService {
   
	private static Map<String, String> emailMap = Maps.newHashMap();
	
    public static String getValue(String key){
        if(emailMap.get(key)==null){
          String valueStr = createValue(key);
          emailMap.put(key,valueStr);
        }
        return emailMap.get(key);
       }
    
    private static String createValue(String key){
    	Properties p = new Properties();
    	String valueStr = "";
    	try {
			p.load(PropotiesService.class.getClassLoader().getResourceAsStream("email.properties"));
			valueStr = p.getProperty(key);
		} catch (IOException e1) {
			 e1.printStackTrace();
		}
    	if(valueStr==null || ("null").equals(valueStr)){valueStr="";}
    	return valueStr;
    }
    
    public void sendEmail(MailSenderInfo mailInfo){

    	System.out.println("$$$$$$$$ 开始发送邮件,信息如下：");
    	System.out.println("$$$$$$$$ 开始时间"+new Date());
    	System.out.println("$$$$$$$$ 收件人地址："+mailInfo.getToAddress());
    	System.out.println("$$$$$$$$ 主题："+mailInfo.getSubject());
    	System.out.println("$$$$$$$$ 内容："+mailInfo.getContent());
    	
    	//这个类主要是设置邮件
		mailInfo.setMailServerHost(getValue("serverhost"));//邮件服务器地址
		mailInfo.setMailServerPort(getValue("serverport"));//邮件服务器端口号
		mailInfo.setValidate(true);
		mailInfo.setUserName(getValue("username"));//用户名
		mailInfo.setPassword(getValue("password"));//邮箱密码
		mailInfo.setFromAddress(getValue("fromaddress"));//发件人地址
        
		if (!"".equals(mailInfo.getMailServerHost())) {
			SimpleMailSender sms = new SimpleMailSender();
			sms.sendTextMail(mailInfo);// 发送文体格式
			
			System.out.println("$$$$$$$$ 邮件服务器信息已配置，可以发送邮件");
		}else{
			System.out.println("$$$$$$$$ 邮件服务器信息未配置，不能发送邮件");
		}
		
		System.out.println("$$$$$$$$ 结束发送邮件,时间"+new Date());
            
    }
    
    public static void main(String[] args) {
    	
		MailSenderInfo mailInfo = new MailSenderInfo();//smtp.sina.com
        mailInfo.setToAddress("huabaoguo@163.com");// 获取地址
        mailInfo.setContent("业务员，您好：<br>&nbsp&nbsp&nbsp&nbsp具体内容");// 内容
        mailInfo.setSubject("测试");
        
        new SendEmailService().sendEmail(mailInfo);

        System.out.println("finish");
	}
	
}
