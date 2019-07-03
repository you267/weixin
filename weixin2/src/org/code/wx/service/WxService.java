package org.code.wx.service;

import java.awt.font.TextMeasurer;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpServletRequest;

import org.code.wx.Util.RobotUtil;
import org.code.wx.message_bean.AccessToken;
import org.code.wx.message_bean.Article;
import org.code.wx.message_bean.BaseMessage;
import org.code.wx.message_bean.ImgMessage;
import org.code.wx.message_bean.MusicMessage;
import org.code.wx.message_bean.NewsMessage;
import org.code.wx.message_bean.TextMessage;
import org.code.wx.message_bean.VideoMessage;
import org.code.wx.message_bean.VoiceMessage;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.baidu.aip.ocr.AipOcr;
import com.sun.corba.se.impl.protocol.giopmsgheaders.Message;

import com.sun.prism.Image;
import com.thoughtworks.xstream.XStream;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;



public class WxService {
	private static final String TOKEN = "zhangyi";
	private static final String APPKEY="1fec136dbd19f44743803f89bd55ca62";
	private static final String GET_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
	
	//微信公众号
	public static final String APPID = "wx6a5f858c619744b9";
	public static final String APPSECRET = "f719923a81cc728fb80bb349d16eeb50";
	
	//百度AI
	public static final String APP_ID = "16672284";
    public static final String API_KEY = "6NeUKz3b3RAzn0jxDoRKEXQG";
    public static final String SECRET_KEY = "DqY4UoTmaRc7saQbjGB7KMWFCyjwTFHo";

	//用于存储token
	private static AccessToken at;
	
	private static void getToken(){
		String url = GET_TOKEN_URL.replace("APPID", APPID).replace("APPSECRET", APPSECRET);
		String tokenStr = RobotUtil.get(url);
		System.out.println(tokenStr);
		JSONObject jsonObject = JSONObject.fromObject(tokenStr);
		String token = jsonObject.getString("access_token");
		String expireIn = jsonObject.getString("expires_in");
		//创建的token对象，并存起来
		at = new AccessToken(token, expireIn);
	}
	
	/**
	 * 外界获取token的方法
	 * @return
	 */
	public static String getAccessTokens() {
		//如果token不存在或者已过期
		if(at==null||at.isExpired()) {
			getToken();
		}
		
		return at.getAccessToken();
		
		
	}
	
	/**
	 * 验证签名
	 * @param timestamp
	 * @param nonce
	 * @param signature
	 * @return
	 */
	public static boolean check(String timestamp,String nonce,String signature) {
		String[] strs = new String[] {TOKEN,timestamp,nonce};
		Arrays.sort(strs);
		String str = strs[0]+strs[1]+strs[2];
		String mysig = sha1(str);
		System.out.println(mysig);
		System.out.println(signature);
		
		return mysig.equals(signature);
		
		
		
		
		
	}
	/**
	 * sha1锟斤拷锟斤拷
	 * @param str
	 * @return
	 */
	private static String sha1(String src) {
		try {
			//锟斤拷取锟斤拷锟杰的讹拷锟斤拷
			MessageDigest md = MessageDigest.getInstance("sha1");
			byte[] digest = md.digest(src.getBytes());
			char[] chars= {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
			StringBuilder sb = new StringBuilder();
			
			for(byte b:digest) {
				sb.append(chars[(b>>4)&15]);
				sb.append(chars[b&15]);
			}
//			System.out.println(sb.toString());
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			
			e.printStackTrace();
		}
		
		
		return null;
	}
	
	/**
	 * 锟斤拷锟斤拷xml锟斤拷锟捷帮拷
	 * @param is
	 * @return
	 */
	public static Map<String, String> parseRequest(InputStream is) {
		Map<String, String> map = new HashMap<>();
		SAXReader reader = new SAXReader();
		try {
			org.dom4j.Document document = reader.read(is);
			Element root = document.getRootElement();
			List<Element> elements = root.elements();
			for (Element e : elements) {
				map.put(e.getName(), e.getStringValue());
			}
			
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		return map;
	}
	
	/**
	 * 锟斤拷锟节达拷锟斤拷锟斤拷锟叫碉拷锟铰硷拷锟斤拷锟斤拷息锟侥回革拷
	 * @param requestMap
	 * @return锟斤拷锟截碉拷锟斤拷xml锟斤拷锟斤拷锟捷帮拷
	 */
	public static String getRespose(Map<String, String> requestMap) {
		BaseMessage msg = null;
		String msgType = requestMap.get("MsgType");
		switch (msgType) {
		case "text":
			msg = dealTextMessage(requestMap);
			break;
		case "image":
			msg = deaiImage(requestMap);
			break;
		case "voice":
	
			break;
		case "video":
	
			break;
		case "shortvideo":
	
			break;
		case "location":
	
			break;
		case "link":
			
			break;
		case "event":
			msg = dealEvent(requestMap);
			break;

		default:
			break;
		}
		
		System.out.println(msg);
		if(msg!=null) {
			return beanToXml(msg);
		}else {
			return null;
		}
	}
	
	/**
	 * 进行图片识别
	 * @param requestMap
	 * @return
	 */
	
	private static BaseMessage deaiImage(Map<String, String> requestMap) {
		  // 初始化一个AipOcr
        AipOcr client = new AipOcr(APP_ID, API_KEY, SECRET_KEY);

        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);


        // 调用接口
        String path = requestMap.get("PicUrl");
        
//        org.json.JSONObject res = client.basicGeneral(path, new HashMap<String, String>());
        org.json.JSONObject res =  client.generalUrl(path, new HashMap<String, String>());

        String json = res.toString();
        
        JSONObject jsonObject = JSONObject.fromObject(json);
        
        JSONArray jsonArray = jsonObject.getJSONArray("words_result");
        Iterator<JSONObject> it = jsonArray.iterator();
        StringBuilder sb = new StringBuilder(); 
        while(it.hasNext()) {
        	JSONObject next = it.next();
        	sb.append(next.getString("words"));
        }
//        System.out.println(res.toString(2));
	
		
		return new TextMessage(requestMap, sb.toString());
	}

	/**
	 * 处理事件推送
	 * @param requestMap
	 * @return
	 */
	private static BaseMessage dealEvent(Map<String, String> requestMap) {
		
		String event = requestMap.get("Event");
		switch (event) {
		case "CLICK":
			return dealClick(requestMap);
			
		case "VIEW":
			return dealView(requestMap);
		
		case "subscribe":
			return dealSubscribe(requestMap);
		

		default:
			break;
		}
		
		return null;
	}
	
	
	
	private static BaseMessage dealSubscribe(Map<String, String> requestMap) {
		
		List<Article> articles = new ArrayList<>();
		articles.add(new Article("团购节来啦,天天特价!!!","天天特价","http://zhangyi.free.idcfengye.com/weixin/images/tg2.jpg","http://zhangyi.free.idcfengye.com/weixin/index.html"));
		articles.add(new Article("优选е-精挑细选，真诚永远.","精挑细选，真诚永远.","http://zhangyi.free.idcfengye.com/weixin/images/you.jpg","http://zhangyi.free.idcfengye.com/weixin/index.html"));
		articles.add(new Article("超值о-一分付出，超值享受!","一分付出，超值享受!","http://zhangyi.free.idcfengye.com/weixin/images/cz.jpg","http://zhangyi.free.idcfengye.com/weixin/index.html"));
		articles.add(new Article("超省м-品质都一样，团购更省钱!","品质都一样，团购更省钱!","http://zhangyi.free.idcfengye.com/weixin/images/sc.jpg","http://zhangyi.free.idcfengye.com/weixin/index.html"));
		NewsMessage nm = new NewsMessage(requestMap, articles);
		
		
		
		return nm;
		
		
		
		
	}

	/**
	 * 处理view类型的按钮的菜单
	 * @param requestMap
	 * @return
	 */
	private static BaseMessage dealView(Map<String, String> requestMap) {
		
		return null;
	}
	
	/**
	 * 处理Click菜单
	 * @param requestMap
	 * @return
	 */
	private static BaseMessage dealClick(Map<String, String> requestMap) {
		String key = requestMap.get("EventKey");
		switch (key) {
		case "1":
			//点击了第一个一级菜单
			return new TextMessage(requestMap, "具体内容请回复团购");
		
		case "32":
			//点击了第三个一级菜单的的二个子菜单
			
			break;
		
		default:
			break;
		}
		return null;
	}

	/**
	 * 将消息对象处理为xml数据包
	 */
	private static String beanToXml(BaseMessage msg){
		XStream stream = new XStream();
		stream.processAnnotations(TextMessage.class);
		stream.processAnnotations(ImgMessage.class);
		stream.processAnnotations(MusicMessage.class);
		stream.processAnnotations(NewsMessage.class);
		stream.processAnnotations(VideoMessage.class);
		stream.processAnnotations(VoiceMessage.class);
		String xml = stream.toXML(msg);
		System.out.println(xml);
		return xml;
		
	}
	
	/**
	 * 
	 * @param requestMap
	 * @return
	 */
	private static BaseMessage dealTextMessage(Map<String, String> requestMap) {
		String msg = requestMap.get("Content");
		if(msg.equals("团购")){
			List<Article> articles = new ArrayList<>();
			articles.add(new Article("团购节来啦!!!","天天特价","http://is4.mzstatic.com/image/pf/us/r30/Purple7/v4/50/76/a3/5076a32b-6685-8cdf-e8d4-0c4724cdb8bb/mzl.epxqpkav.png","http://zhangyi.free.idcfengye.com/weixin/index.html"));
			
			NewsMessage nm = new NewsMessage(requestMap, articles);
			
			return nm;
			
		}
		
		if(msg.equals("登录")){
			String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE#wechat_redirect";
			url = url.replace("APPID", APPID).replace("REDIRECT_URI","http://zhangyi.free.idcfengye.com/weixin/GetUserInfo").replace("SCOPE", "snsapi_userinfo");
			TextMessage tm = new TextMessage(requestMap, "点击<a href = \""+url+"\">这里</a>登录");
			return tm;
		}
		
		
		
		String resp = chat(msg);
		
		TextMessage tm = new TextMessage(requestMap, resp);
		
		
		return tm;
	}
	private static String chat(String msg) {
		String result =null;
        String url ="http://op.juhe.cn/robot/index";//请求接口地址
        Map params = new HashMap();//请求参数
        params.put("key",APPKEY);//您申请到的本接口专用的APPKEY
        params.put("info",msg);//要发送给机器人的内容，不要超过30个字符
        params.put("dtype","");//返回的数据的格式，json或xml，默认为json
        params.put("loc","");//地点，如北京中关村
        params.put("lon","");//经度，东经116.234632（小数点后保留6位），需要写为116234632
        params.put("lat","");//纬度，北纬40.234632（小数点后保留6位），需要写为40234632
        params.put("userid","");//1~32位，此userid针对您自己的每一个用户，用于上下文的关联
        try {
            result =RobotUtil.net(url, params, "GET");
            System.out.println(result);
            //解析json
            JSONObject jsonObject = JSONObject.fromObject(result);
            //取出error_code
            int code = jsonObject.getInt("error_code");
            if(code!=0) {
            		return null;
            }
            //取出返回的消息的内容
            String resp = jsonObject.getJSONObject("result").getString("text");
            return resp;
        } catch (Exception e) {
            e.printStackTrace();
        }
		return null;
	}
	
	/**
	 * 上传临时素材
	 * @param path 上传的文件路径
	 * @param type 上传的文件类型
	 * @return
	 */
	public static String upload(String path,String type) {
		File file  = new File(path);
		
		String url = "https://api.weixin.qq.com/cgi-bin/media/upload?access_token=ACCESS_TOKEN&type=TYPE";
		url = url.replace("ACCESS_TOKEN",getAccessTokens()).replace("TYPE",type);
		
		try {
			URL urlObj = new URL(url);
			//强转成安全链接
			HttpsURLConnection conn = (HttpsURLConnection) urlObj.openConnection();
			//设置链接的信息
			conn.setDoInput(true);
			conn.setDoOutput(true);
			//不能使用缓存
			conn.setUseCaches(false);
			
			//设置请求头信息
			conn.setRequestProperty("Connection","keep-Alive");
			conn.setRequestProperty("Charset", "utf8");
			//数据边界
			String boundary = "-----"+System.currentTimeMillis();
			conn.setRequestProperty("Content-Type", "multipart/form-data;boundary="+boundary);
			InputStream is = new FileInputStream(file);
			OutputStream out = conn.getOutputStream();
			
			//准备数据
			//1.头部信息
			StringBuilder sb = new StringBuilder();
			sb.append("--");
			sb.append(boundary);
			sb.append("\r\n");
			sb.append("Content-Disposition:form-data;name=\"media\";filename=\""+file.getName()+"\"\r\n");
			sb.append("Content-Type:application/octet-stream\r\n\r\n");
			out.write(sb.toString().getBytes());
			System.out.println(sb.toString());
			
			//2.文件内容
			byte[] b = new byte[1024];
			int len;
			while((len = is.read(b))!=-1) {
				out.write(b,0,len);
			}
			
			//3.尾部信息
			String foot = "\r\n--"+boundary+"--\r\n";
			out.write(foot.getBytes());
			out.flush();
			out.close();
			
			//读取数据
			InputStream is2 = conn.getInputStream();
			StringBuilder resp = new StringBuilder(); 
			
			while((len=is2.read(b))!=-1) {
				resp.append(new String(b,0,len));
			}
			
			return resp.toString();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return null;
	}
	
	/**
	 * 获取带参数的二维码的ticket
	 * @return
	 */
	public static String getQrCodeTicket() {
		String at = getAccessTokens();
		String url = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token="+at;
		//生成临时字符串二维码
		String data = "{\"expire_seconds\": 600, \"action_name\": \"QR_STR_SCENE\", \"action_info\": {\"scene\": {\"scene_str\": \"zhangyi\"}}}";
		
		String result = RobotUtil.post(url, data);
		String ticket = JSONObject.fromObject(result).getString("ticket");
		
		
		return ticket;
	}
	
	/**
	 * 获取用户的基本信息
	 * @return
	 */
	public static String getUserInfo(String openid) {
		String url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
		url = url.replace("ACCESS_TOKEN",getAccessTokens()).replace("OPENID", openid);
		return RobotUtil.get(url);
		
		
		
	}
}
