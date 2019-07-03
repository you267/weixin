package org.code.wx.Manager;

import org.code.wx.Util.RobotUtil;
import org.code.wx.service.WxService;
import org.junit.Test;

public class TemplateMessageManager {
	/**
	 * ������ҵ
	 */
	@Test
	public void set() {
		String at = WxService.getAccessTokens();
		String url = "https://api.weixin.qq.com/cgi-bin/template/api_set_industry?access_token="+at;
		String data = "{\r\n" + 
				"    \"industry_id1\":\"1\",\r\n" + 
				"    \"industry_id2\":\"4\"\r\n" + 
				"}";
		String result = RobotUtil.post(url, data);
		System.out.println(result);
	}
	/**
	 * ��ȡ��ҵ
	 */
	@Test
	public void get() {
		String at = WxService.getAccessTokens();
		String url = "https://api.weixin.qq.com/cgi-bin/template/get_industry?access_token="+at;
		String result = RobotUtil.get(url);
		System.out.println(result);
	}
	
	/**
	 * ����ģ����Ϣ
	 */
	@Test
	public void sendTemplateMessage() {
		String at = WxService.getAccessTokens();
		String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="+at; 
		String data = "{\r\n" + 
				"           \"touser\":\"osxWF5_2VEAz1Pc4tJGQZPvsyFa4\",\r\n" + 
				"           \"template_id\":\"ThEOZDHFsNTHqtzis2RnoCu-NwfrOyYQzet5VqyeOlY\",\r\n" + 
				"          \r\n" + 
				"          \r\n" + 
				"           \"data\":{\r\n" + 
				"                   \"first\": {\r\n" + 
				"                       \"value\":\"�����µķ�����Ϣ����\",\r\n" + 
				"                       \"color\":\"#173177\"\r\n" + 
				"                   },\r\n" + 
				"                   \"company\":{\r\n" + 
				"                       \"value\":\"�ε�����\",\r\n" + 
				"                       \"color\":\"#173177\"\r\n" + 
				"                   },\r\n" + 
				"                   \"time\": {\r\n" + 
				"                       \"value\":\"2019��6��29��\",\r\n" + 
				"                       \"color\":\"#173177\"\r\n" + 
				"                   },\r\n" + 
				"                   \"result\": {\r\n" + 
				"                       \"value\":\"����ͨ������\",\r\n" + 
				"                       \"color\":\"#173177\"\r\n" + 
				"                   },\r\n" + 
				"                   \"remark\":{\r\n" + 
				"                       \"value\":\"��ͱ���˾����רԱ��ϵ��\",\r\n" + 
				"                       \"color\":\"#173177\"\r\n" + 
				"                   }\r\n" + 
				"           }\r\n" + 
				"       }";
		String result = RobotUtil.post(url, data);
		System.out.println(result);
		
	}

}
