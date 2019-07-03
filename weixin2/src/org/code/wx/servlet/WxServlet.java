package org.code.wx.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.connector.Request;
import org.code.wx.service.WxService;

/**
 * Servlet implementation class WxServlet
 */
@WebServlet("/wx")
public class WxServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public WxServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/**
		 * 
			����	����
			signature	΢�ż���ǩ����signature����˿�������д��token�����������е�timestamp������nonce������
			timestamp	ʱ���
			nonce	�����
			echostr	����ַ���
		 */
		
		
		String signature = request.getParameter("signature");
		String timestamp = request.getParameter("timestamp");
		String nonce = request.getParameter("nonce");
		String echostr = request.getParameter("echostr");
		
		//У��ǩ��
		if(WxService.check(timestamp,nonce,signature)) {
			System.out.println("����ɹ�");
			PrintWriter out = response.getWriter();
			//ԭ�����ز���
			out.print(echostr);
			out.flush();
			out.close();
		}else {
			System.out.println("����ʧ��");
			
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/*
		 * ServletInputStream is = request.getInputStream(); byte[] b = new byte[1024];
		 * int len; StringBuilder sb = new StringBuilder(); while((len=is.read(b))!=-1)
		 * { sb.append(new String(b,0,len)); } System.out.println(sb.toString());
		 * System.out.println("==========dopost");
		 */
		//������Ϣ���¼�����
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		Map<String,String> requestMap = WxService.parseRequest(request.getInputStream());
		System.out.println(requestMap);
		
		//׼���ظ������ݰ�
		/*
		 * String respXml = "<xml>\r\n" +
		 * "<ToUserName><![CDATA["+requestMap.get("FromUserName")+"]]></ToUserName>\r\n"
		 * + "<FromUserName><![CDATA["+requestMap.get("ToUserName")+
		 * "]]></FromUserName>\r\n" +
		 * "<CreateTime>"+System.currentTimeMillis()/1000+"</CreateTime>\r\n" +
		 * "<MsgType><![CDATA[text]]></MsgType>\r\n" +
		 * "<Content><![CDATA[why?]]></Content>\r\n" + "</xml>";
		 */
//		System.out.println(respXml);
		String respXml = WxService.getRespose(requestMap);
		PrintWriter out = response.getWriter();
		
		out.print(respXml);
		out.flush();
		out.close();
		
	}

}
