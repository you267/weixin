package org.code.wx.message_bean;

import java.util.Map;

import com.thoughtworks.xstream.annotations.XStreamAlias;


public class BaseMessage {
	@XStreamAlias("ToUserName")
	private String toUserName;
	@XStreamAlias("FromUserName")
	private String formUserName;
	@XStreamAlias("CreateTime")
	private String createTime;
	@XStreamAlias("MsgType")
	private String msgType;
	
	public BaseMessage(Map<String,String> requestMap) {
		this.toUserName = requestMap.get("FromUserName");
		this.formUserName = requestMap.get("ToUserName");
		this.createTime = System.currentTimeMillis()/1000+"";
	}
	
	public String getToUserName() {
		return toUserName;
	}
	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}
	public String getFormUserName() {
		return formUserName;
	}
	public void setFormUserName(String formUserName) {
		this.formUserName = formUserName;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getMsgType() {
		return msgType;
	}
	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	
	
	
	
	
	
	
}
