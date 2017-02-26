package venus.helper.middle;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import venus.dao.OtConfigMapper;
import venus.model.dao.OtConfig;

@Component
public class AlertMiddle {
	@Autowired OtConfigMapper otConfigMapper;
	@Value("${mail.host}")
	String mailHost;
	@Value("${mail.username}")
	String mailUsername;
	@Value("${mail.password}")
	String mailPassword;
	
	JavaMailSenderImpl javaMailSenderImpl;
	public void init(String subject,String text){
		OtConfig otConfig=otConfigMapper.find("ALERT_ADDRESS");
		
		String mails=otConfig.getValue();
		if(mails==null||mails.equals(""))return;
		
		String[] mail=mails.split(",");
		
		javaMailSenderImpl=new JavaMailSenderImpl();
		
		javaMailSenderImpl.setHost(mailHost);
		javaMailSenderImpl.setUsername(mailUsername);
		javaMailSenderImpl.setPassword(mailPassword);
		
		MimeMessage mimeMessage = javaMailSenderImpl.createMimeMessage();
		MimeMessageHelper helper;
		try {
			helper = new MimeMessageHelper(mimeMessage, true, "GBK");
			 //基本信息    
            
	        //发送者地址，必须填写正确的邮件格式，否者会发送失败    
	        helper.setFrom("xmxkkk@163.com");    
	        //邮件主题    
	        helper.setSubject(subject);    
	        //邮件内容，简单的邮件信息只能添加文本信息    
	        helper.setText(text);    
	        //邮件接收者的邮箱地址    
	        helper.setTo(mail[0]);
	        
	        for(int i=1;i<mail.length;i++){
	        	if(mail[i]==null||mail[i].equals(""))continue;
	        	helper.addCc(mail[i]);
	        }
	        
	        
	        javaMailSenderImpl.send(mimeMessage);
	        
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}     
         
	}
}
