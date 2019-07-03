package org.code.wx.Util;

import java.util.HashMap;
import java.util.Map;

import org.code.wx.message_bean.AbstractButton;
import org.code.wx.message_bean.Button;
import org.code.wx.message_bean.ClickButton;
import org.code.wx.message_bean.PhotoOrAlbum;
import org.code.wx.message_bean.SubButton;
import org.code.wx.message_bean.TextMessage;
import org.code.wx.message_bean.ViewButton;
import org.code.wx.service.WxService;
import org.junit.Test;

import com.thoughtworks.xstream.XStream;

import net.sf.json.JSONObject;

public class test {
	
	@Test
	public void testButton() {
		Button btn = new Button();
		btn.getButton().add(new ClickButton("团购商城", "1"));
		btn.getButton().add(new ViewButton("百度","http://www.baidu.com"));
		SubButton sb = new SubButton("实用工具");
		sb.getSub_button().add(new PhotoOrAlbum("传图提取文字", "31"));
		sb.getSub_button().add(new ClickButton("点击", "32"));
		sb.getSub_button().add(new ViewButton("网易新闻", "http://news.163.com"));
		btn.getButton().add(sb);
		JSONObject jsonObject = JSONObject.fromObject(btn);
		System.out.println(jsonObject.toString());
	}
	
	@Test
	public void testToken() {
		System.out.println(WxService.getAccessTokens());
		System.out.println(WxService.getAccessTokens());
	}
	
	
	
	
	/*
	 * @Test public void testMsg() { Map<String,String> requestMap =new HashMap<>();
	 * requestMap.put("ToUserName","to"); requestMap.put("FromUserName","from");
	 * requestMap.put("MsgType", "type"); TextMessage tm = new
	 * TextMessage(requestMap, "不好"); System.out.println(tm); XStream stream = new
	 * XStream(); stream.processAnnotations(TextMessage.class); String xml =
	 * stream.toXML(tm); System.out.println(xml); }
	 */
	
}
