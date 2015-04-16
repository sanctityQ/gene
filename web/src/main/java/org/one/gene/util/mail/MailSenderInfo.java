package org.one.gene.util.mail;
import java.util.Properties;   

public class MailSenderInfo {
    private String mailServerHost="";    
    private String mailServerPort = "25";    
    private String fromAddress="";    
    private String toAddress="";    
    private String userName="";    
    private String password="";    
    private boolean validate = false;    
    private String subject="";    
    private String content="";    
    private EmailAttachment[] attachFilePath;      

    public Properties getProperties(){    
      Properties p = new Properties();    
      p.put("mail.smtp.host", this.mailServerHost);
      p.put("mail.smtp.port", this.mailServerPort);
      p.put("mail.smtp.auth", validate ? "true" : "false");
      return p;    
    }    
    public String getMailServerHost() {
      return mailServerHost;    
    }    
    public void setMailServerHost(String mailServerHost) {
      this.mailServerHost = mailServerHost;    
    }   
    public String getMailServerPort() {
      return mailServerPort;    
    }   
    public void setMailServerPort(String mailServerPort) {
      this.mailServerPort = mailServerPort;    
    }   
    public boolean isValidate() {
      return validate;    
    }   
    public void setValidate(boolean validate) {
      this.validate = validate;    
    }   
    public EmailAttachment[] getAttachFilePath() {
      return attachFilePath;    
    }   
    public void setAttachFilePath(EmailAttachment[] filePath) {    
      this.attachFilePath = filePath;    
    }   
    public String getFromAddress() {    
      return fromAddress;    
    }    
    public void setFromAddress(String fromAddress) {    
      this.fromAddress = fromAddress;    
    }   
    public String getPassword() {    
      return password;    
    }   
    public void setPassword(String password) {    
      this.password = password;    
    }   
    public String getToAddress() {    
      return toAddress;    
    }    
    public void setToAddress(String toAddress) {    
      this.toAddress = toAddress;    
    }    
    public String getUserName() {    
      return userName;    
    }   
    public void setUserName(String userName) {    
      this.userName = userName;    
    }   
    public String getSubject() {    
      return subject;    
    }   
    public void setSubject(String subject) {    
      this.subject = subject;    
    }   
    public String getContent() {    
      return content;    
    }   
    public void setContent(String textContent) {    
      this.content = textContent;    
    }    
}   


