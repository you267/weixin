package org.code.wx.message_bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("item")
public class Article {
	@XStreamAlias("Title")
	private String title;
	@XStreamAlias("Description")
	private String description;
	
	private String PicUrl;
	@XStreamAlias("Url")
	private String url;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getPicUrl() {
		return PicUrl;
	}
	public void setPicUrl(String picUrl) {
		PicUrl = picUrl;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Article(String title, String description, String picUrl, String url) {
		super();
		this.title = title;
		this.description = description;
		PicUrl = picUrl;
		this.url = url;
	}
	
	
	
	
}
