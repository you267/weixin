package org.code.wx.message_bean;

import java.util.Map;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("xml")
public class ImgMessage extends BaseMessage {
	private String mediaId;
	
	
	
	
	public String getMediaId() {
		return mediaId;
	}




	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}




	public ImgMessage(Map<String, String> requestMap,String mediaId ) {
		super(requestMap);
		this.setMsgType("image");
		this.mediaId = mediaId;
	}

}
