package org.code.wx.message_bean;

/**
 * ������Ĺ���ʱ��
 * @author zhangxiaoyi
 *
 */
public class AccessToken {
	private String accessToken;
	private long exporeTime;
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public long getExporeTime() {
		return exporeTime;
	}
	public void setExporeTime(long exporeTime) {
		this.exporeTime = exporeTime;
	}
	public AccessToken(String accessToken, String exporeIn) {
		super();
		this.accessToken = accessToken;
		this.exporeTime = System.currentTimeMillis()+Integer.parseInt(exporeIn)*1000;
	}
	/**
	 * �ж�token�Ƿ����
	 * @return
	 */
	public boolean isExpired() {
		return System.currentTimeMillis()>this.exporeTime;
	}
}
