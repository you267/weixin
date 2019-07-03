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
	
	//΢�Ź��ں�
	public static final String APPID = "wx6a5f858c619744b9";
	public static final String APPSECRET = "f719923a81cc728fb80bb349d16eeb50";
	
	//�ٶ�AI
	public static final String APP_ID = "16672284";
    public static final String API_KEY = "6NeUKz3b3RAzn0jxDoRKEXQG";
    public static final String SECRET_KEY = "DqY4UoTmaRc7saQbjGB7KMWFCyjwTFHo";

	//���ڴ洢token
	private static AccessToken at;
	
	private static void getToken(){
		String url = GET_TOKEN_URL.replace("APPID", APPID).replace("APPSECRET", APPSECRET);
		String tokenStr = RobotUtil.get(url);
		System.out.println(tokenStr);
		JSONObject jsonObject = JSONObject.fromObject(tokenStr);
		String token = jsonObject.getString("access_token");
		String expireIn = jsonObject.getString("expires_in");
		//������token���󣬲�������
		at = new AccessToken(token, expireIn);
	}
	
	/**
	 * ����ȡtoken�ķ���
	 * @return
	 */
	public static String getAccessTokens() {
		//���token�����ڻ����ѹ���
		if(at==null||at.isExpired()) {
			getToken();
		}
		
		return at.getAccessToken();
		
		
	}
	
	/**
	 * ��֤ǩ��
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
	 * sha1����
	 * @param str
	 * @return
	 */
	private static String sha1(String src) {
		try {
			//��ȡ���ܵĶ���
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
	 * ����xml���ݰ�
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
	 * ���ڴ������е��¼�����Ϣ�Ļظ�
	 * @param requestMap
	 * @return���ص���xml�����ݰ�
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
	 * ����ͼƬʶ��
	 * @param requestMap
	 * @return
	 */
	
	private static BaseMessage deaiImage(Map<String, String> requestMap) {
		  // ��ʼ��һ��AipOcr
        AipOcr client = new AipOcr(APP_ID, API_KEY, SECRET_KEY);

        // ��ѡ�������������Ӳ���
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);


        // ���ýӿ�
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
	 * �����¼�����
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
		articles.add(new Article("�Ź�������,�����ؼ�!!!","�����ؼ�","http://zhangyi.free.idcfengye.com/weixin/images/tg2.jpg","http://zhangyi.free.idcfengye.com/weixin/index.html"));
		articles.add(new Article("��ѡ��-����ϸѡ�������Զ.","����ϸѡ�������Զ.","http://zhangyi.free.idcfengye.com/weixin/images/you.jpg","http://zhangyi.free.idcfengye.com/weixin/index.html"));
		articles.add(new Article("��ֵ��-һ�ָ�������ֵ����!","һ�ָ�������ֵ����!","http://zhangyi.free.idcfengye.com/weixin/images/cz.jpg","http://zhangyi.free.idcfengye.com/weixin/index.html"));
		articles.add(new Article("��ʡ��-Ʒ�ʶ�һ�����Ź���ʡǮ!","Ʒ�ʶ�һ�����Ź���ʡǮ!","http://zhangyi.free.idcfengye.com/weixin/images/sc.jpg","http://zhangyi.free.idcfengye.com/weixin/index.html"));
		NewsMessage nm = new NewsMessage(requestMap, articles);
		
		
		
		return nm;
		
		
		
		
	}

	/**
	 * ����view���͵İ�ť�Ĳ˵�
	 * @param requestMap
	 * @return
	 */
	private static BaseMessage dealView(Map<String, String> requestMap) {
		
		return null;
	}
	
	/**
	 * ����Click�˵�
	 * @param requestMap
	 * @return
	 */
	private static BaseMessage dealClick(Map<String, String> requestMap) {
		String key = requestMap.get("EventKey");
		switch (key) {
		case "1":
			//����˵�һ��һ���˵�
			return new TextMessage(requestMap, "����������ظ��Ź�");
		
		case "32":
			//����˵�����һ���˵��ĵĶ����Ӳ˵�
			
			break;
		
		default:
			break;
		}
		return null;
	}

	/**
	 * ����Ϣ������Ϊxml���ݰ�
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
		if(msg.equals("�Ź�")){
			List<Article> articles = new ArrayList<>();
			articles.add(new Article("�Ź�������!!!","�����ؼ�","http://is4.mzstatic.com/image/pf/us/r30/Purple7/v4/50/76/a3/5076a32b-6685-8cdf-e8d4-0c4724cdb8bb/mzl.epxqpkav.png","http://zhangyi.free.idcfengye.com/weixin/index.html"));
			
			NewsMessage nm = new NewsMessage(requestMap, articles);
			
			return nm;
			
		}
		
		if(msg.equals("��¼")){
			String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE#wechat_redirect";
			url = url.replace("APPID", APPID).replace("REDIRECT_URI","http://zhangyi.free.idcfengye.com/weixin/GetUserInfo").replace("SCOPE", "snsapi_userinfo");
			TextMessage tm = new TextMessage(requestMap, "���<a href = \""+url+"\">����</a>��¼");
			return tm;
		}
		
		
		
		String resp = chat(msg);
		
		TextMessage tm = new TextMessage(requestMap, resp);
		
		
		return tm;
	}
	private static String chat(String msg) {
		String result =null;
        String url ="http://op.juhe.cn/robot/index";//����ӿڵ�ַ
        Map params = new HashMap();//�������
        params.put("key",APPKEY);//�����뵽�ı��ӿ�ר�õ�APPKEY
        params.put("info",msg);//Ҫ���͸������˵����ݣ���Ҫ����30���ַ�
        params.put("dtype","");//���ص����ݵĸ�ʽ��json��xml��Ĭ��Ϊjson
        params.put("loc","");//�ص㣬�籱���йش�
        params.put("lon","");//���ȣ�����116.234632��С�������6λ������ҪдΪ116234632
        params.put("lat","");//γ�ȣ���γ40.234632��С�������6λ������ҪдΪ40234632
        params.put("userid","");//1~32λ����userid������Լ���ÿһ���û������������ĵĹ���
        try {
            result =RobotUtil.net(url, params, "GET");
            System.out.println(result);
            //����json
            JSONObject jsonObject = JSONObject.fromObject(result);
            //ȡ��error_code
            int code = jsonObject.getInt("error_code");
            if(code!=0) {
            		return null;
            }
            //ȡ�����ص���Ϣ������
            String resp = jsonObject.getJSONObject("result").getString("text");
            return resp;
        } catch (Exception e) {
            e.printStackTrace();
        }
		return null;
	}
	
	/**
	 * �ϴ���ʱ�ز�
	 * @param path �ϴ����ļ�·��
	 * @param type �ϴ����ļ�����
	 * @return
	 */
	public static String upload(String path,String type) {
		File file  = new File(path);
		
		String url = "https://api.weixin.qq.com/cgi-bin/media/upload?access_token=ACCESS_TOKEN&type=TYPE";
		url = url.replace("ACCESS_TOKEN",getAccessTokens()).replace("TYPE",type);
		
		try {
			URL urlObj = new URL(url);
			//ǿת�ɰ�ȫ����
			HttpsURLConnection conn = (HttpsURLConnection) urlObj.openConnection();
			//�������ӵ���Ϣ
			conn.setDoInput(true);
			conn.setDoOutput(true);
			//����ʹ�û���
			conn.setUseCaches(false);
			
			//��������ͷ��Ϣ
			conn.setRequestProperty("Connection","keep-Alive");
			conn.setRequestProperty("Charset", "utf8");
			//���ݱ߽�
			String boundary = "-----"+System.currentTimeMillis();
			conn.setRequestProperty("Content-Type", "multipart/form-data;boundary="+boundary);
			InputStream is = new FileInputStream(file);
			OutputStream out = conn.getOutputStream();
			
			//׼������
			//1.ͷ����Ϣ
			StringBuilder sb = new StringBuilder();
			sb.append("--");
			sb.append(boundary);
			sb.append("\r\n");
			sb.append("Content-Disposition:form-data;name=\"media\";filename=\""+file.getName()+"\"\r\n");
			sb.append("Content-Type:application/octet-stream\r\n\r\n");
			out.write(sb.toString().getBytes());
			System.out.println(sb.toString());
			
			//2.�ļ�����
			byte[] b = new byte[1024];
			int len;
			while((len = is.read(b))!=-1) {
				out.write(b,0,len);
			}
			
			//3.β����Ϣ
			String foot = "\r\n--"+boundary+"--\r\n";
			out.write(foot.getBytes());
			out.flush();
			out.close();
			
			//��ȡ����
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
	 * ��ȡ�������Ķ�ά���ticket
	 * @return
	 */
	public static String getQrCodeTicket() {
		String at = getAccessTokens();
		String url = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token="+at;
		//������ʱ�ַ�����ά��
		String data = "{\"expire_seconds\": 600, \"action_name\": \"QR_STR_SCENE\", \"action_info\": {\"scene\": {\"scene_str\": \"zhangyi\"}}}";
		
		String result = RobotUtil.post(url, data);
		String ticket = JSONObject.fromObject(result).getString("ticket");
		
		
		return ticket;
	}
	
	/**
	 * ��ȡ�û��Ļ�����Ϣ
	 * @return
	 */
	public static String getUserInfo(String openid) {
		String url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
		url = url.replace("ACCESS_TOKEN",getAccessTokens()).replace("OPENID", openid);
		return RobotUtil.get(url);
		
		
		
	}
}
