package org.one.gene.util.mail;
import java.util.Date;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;


/**
 * ������ʼ�����
 */

public class SimpleMailSender {
	/**
	 * ��html��ʽ�����ʼ�
	 * 
	 * @param mailInfo
	 *            ���͵��ʼ�����Ϣ
	 */
	public boolean sendTextMail(MailSenderInfo mailInfo) {
		// �ж��Ƿ���Ҫ�����֤
		MyAuthenticator authenticator = null;
		Properties pro = mailInfo.getProperties();
		if (mailInfo.isValidate()) {
			// �����Ҫ�����֤���򴴽�һ��������֤��
			authenticator = new MyAuthenticator(mailInfo.getUserName(),mailInfo.getPassword());		
		}
		// ����ʼ��Ự���Ժ�������֤������һ�������ʼ���session
		Session sendMailSession = Session.getInstance(pro, authenticator);//����ע�ⲻҪ��Ĭ��session
		try {
			// ���session����һ���ʼ���Ϣ
			Message mailMessage = new MimeMessage(sendMailSession);
			// �����ʼ������ߵ�ַ
			Address from = new InternetAddress(mailInfo.getFromAddress());
			// �����ʼ���Ϣ�ķ�����
			mailMessage.setFrom(from);
			// �����ʼ��Ľ����ߵ�ַ�������õ��ʼ���Ϣ��
			Address to = new InternetAddress(mailInfo.getToAddress());
			mailMessage.setRecipient(Message.RecipientType.TO, to);
			// �����ʼ���Ϣ������
			mailMessage.setSubject(mailInfo.getSubject());
			// �����ʼ���Ϣ���͵�ʱ��
			mailMessage.setSentDate(new Date());
			MimeBodyPart mbpHtml = new MimeBodyPart();// ������һ��messagebody����,�����ʼ�����
			// ����HTML��������
			mbpHtml.setContent(mailInfo.getContent(), "text/html; charset=gbk");
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(mbpHtml);// �������
			//�������ʼ���������ʼ�����ڶ���messagebody���󣬱����ʼ�����
			if(mailInfo.getAttachFilePath()!=null&&mailInfo.getAttachFilePath().length>0){
				MimeBodyPart mbpAttachment = new MimeBodyPart(); // �����ڶ���messagebody���󣬱����ʼ�����
				DataSource source = new FileDataSource((mailInfo.getAttachFilePath()[0]).getFilePath()
						+ (mailInfo.getAttachFilePath()[0]).getFileName());
				mbpAttachment.setDataHandler(new DataHandler(source));			
				mbpAttachment.setFileName(MimeUtility.encodeText((mailInfo.getAttachFilePath()[0]).getFileName()));//�����ʼ��������������������
				multipart.addBodyPart(mbpAttachment);// ��Ӹ���
			}
			mailMessage.setContent(multipart);
			mailMessage.saveChanges();
			// �����ʼ�
			Transport.send(mailMessage);
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}

}
