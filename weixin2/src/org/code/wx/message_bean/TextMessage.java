package org.code.wx.message_bean;

import java.util.Map;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("xml")
public class TextMessage extends BaseMessage{
	@XStreamAlias("Content")
	private String content;

	public TextMessage(Map<String,String> requestMap,String content){
		
		super(requestMap);
		//�O���ı���Ϣ������Ϊtext
		this.setMsgType("text");
		this.content = content;
	}
	
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}


	@Override
	public String toString() {
		return "TextMessage [content=" + content + ", getToUserName()=" + getToUserName() + ", getFormUserName()="
				+ getFormUserName() + ", getCreateTime()=" + getCreateTime() + "]";
	}


	
	
	
	
	
	
	
}
