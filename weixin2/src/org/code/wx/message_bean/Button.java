package org.code.wx.message_bean;

import java.util.ArrayList;
import java.util.List;


/**
 * ����button
 * @author zhangxiaoyi
 *
 */
public class Button {
	private List<AbstractButton> button = new ArrayList<>();

	public List<AbstractButton> getButton() {
		return button;
	}

	public void setButton(List<AbstractButton> button) {
		this.button = button;
	}
	
	
	
}
