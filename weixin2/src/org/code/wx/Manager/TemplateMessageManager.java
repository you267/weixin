package org.code.wx.Manager;

import org.code.wx.Util.RobotUtil;
import org.code.wx.service.WxService;
import org.junit.Test;

public class TemplateMessageManager {
	/**
	 * 设置行业
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
	 * 获取行业
	 */
	@Test
	public void get() {
		String at = WxService.getAccessTokens();
		String url = "https://api.weixin.qq.com/cgi-bin/template/get_industry?access_token="+at;
		String result = RobotUtil.get(url);
		System.out.println(result);
	}
	
	/**
	 * 发送模板消息
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
				"                       \"value\":\"您有新的反馈信息啦！\",\r\n" + 
				"                       \"color\":\"#173177\"\r\n" + 
				"                   },\r\n" + 
				"                   \"company\":{\r\n" + 
				"                       \"value\":\"课得在线\",\r\n" + 
				"                       \"color\":\"#173177\"\r\n" + 
				"                   },\r\n" + 
				"                   \"time\": {\r\n" + 
				"                       \"value\":\"2019年6月29日\",\r\n" + 
				"                       \"color\":\"#173177\"\r\n" + 
				"                   },\r\n" + 
				"                   \"result\": {\r\n" + 
				"                       \"value\":\"面试通过！！\",\r\n" + 
				"                       \"color\":\"#173177\"\r\n" + 
				"                   },\r\n" + 
				"                   \"remark\":{\r\n" + 
				"                       \"value\":\"请和本公司人事专员联系！\",\r\n" + 
				"                       \"color\":\"#173177\"\r\n" + 
				"                   }\r\n" + 
				"           }\r\n" + 
				"       }";
		String result = RobotUtil.post(url, data);
		System.out.println(result);
		
	}

}
