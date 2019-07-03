package org.code.wx.Util;

import java.util.HashMap;

import org.code.wx.service.WxService;
import org.json.JSONObject;
import org.junit.Test;

import com.baidu.aip.ocr.AipOcr;



public class TestWx {
	//设置APPID/AK/SK
    public static final String APP_ID = "16672284";
    public static final String API_KEY = "6NeUKz3b3RAzn0jxDoRKEXQG";
    public static final String SECRET_KEY = "DqY4UoTmaRc7saQbjGB7KMWFCyjwTFHo";
    
   
    @Test
    public void testGetUserInfo() {
    	String user = "osxWF5_2VEAz1Pc4tJGQZPvsyFa4";
    	System.out.println(WxService.getUserInfo(user));
    	
    }
    
    
    @Test
    public void testQrCode() {
    	
    	String ticket = WxService.getQrCodeTicket();
    	System.out.println(ticket);
    }
    
    
    @Test
    public void test() {
    	System.out.println(WxService.getAccessTokens());
    	
    }
	
    @Test
	public void testUpload() {
		String file = "C:\\Users\\zhangxiaoyi\\Desktop\\img\\a4.jpg";
		String result = WxService.upload(file, "image");
		System.out.println(result);
	}
    
    
	@Test
	public void testPic() {
		  // 初始化一个AipOcr
        AipOcr client = new AipOcr(APP_ID, API_KEY, SECRET_KEY);

        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);

        // 可选：设置代理服务器地址, http和socket二选一，或者均不设置
//        client.setHttpProxy("proxy_host", proxy_port);  // 设置http代理
//        client.setSocketProxy("proxy_host", proxy_port);  // 设置socket代理

        // 可选：设置log4j日志输出格式，若不设置，则使用默认配置
        // 也可以直接通过jvm启动参数设置此环境变量
//        System.setProperty("aip.log4j.conf", "path/to/your/log4j.properties");

        // 调用接口
        String path = "C:\\Users\\zhangxiaoyi\\Desktop\\5035.png";
        org.json.JSONObject res = client.basicGeneral(path, new HashMap<String, String>());
        System.out.println(res.toString(2));

	}
	
	
}
