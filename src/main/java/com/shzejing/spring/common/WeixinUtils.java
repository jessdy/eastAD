package com.shzejing.spring.common;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.servlet.http.HttpServletRequest;

public class WeixinUtils {

	public static final String UNIFIED_ORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
	public static final String SIGN_TYPE = "MD5";

	public static String createSign(String characterEncoding,
			SortedMap<Object, Object> parameters, String key) {
		StringBuffer sb = new StringBuffer();
		Set es = parameters.entrySet();
		Iterator it = es.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String k = (String) entry.getKey();
			Object v = entry.getValue();
			if (null != v && !"".equals(v) && !"sign".equals(k)
					&& !"key".equals(k)) {
				sb.append(k + "=" + v + "&");
			}
		}
		sb.append("key=" + key);
		String sign = MD5Util.MD5Encode(sb.toString(), characterEncoding)
				.toUpperCase();
		System.out.println(sb.toString());
		return sign;
	}

	public static String createSha1(SortedMap<String, Object> parameters)
			throws NoSuchAlgorithmException, UnsupportedEncodingException {
		StringBuilder sb = new StringBuilder();
		Set<Entry<String, Object>> es = parameters.entrySet();
		int i = 0;
		for (Entry<String, Object> entry : es) {
			String k = (String) entry.getKey();
			Object v = entry.getValue();
			if (null != v && !"".equals(v) && !"sign".equals(k)
					&& !"key".equals(k)) {
				sb.append((i == 0 ? "" : "&") + k + "=" + v);
				i++;
			}
		}
		System.out.println(sb.toString());
		MessageDigest alga = MessageDigest.getInstance("SHA-1");
		byte[] b = alga.digest(sb.toString().getBytes("UTF8"));
		return MD5Util.byteArrayToHexString(b);
	}
	
	public static String sign(String ticket, String noncestr, String timestamp, String url) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		SortedMap<String, Object> parameters = new TreeMap<String, Object>();
		parameters.put("jsapi_ticket", ticket);
		parameters.put("noncestr", noncestr);
		parameters.put("timestamp", timestamp);
		parameters.put("url", url);
		String signature = WeixinUtils.createSha1(parameters);
		return signature;
	}
	
	public static void main(String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		SortedMap<String, Object> parameters = new TreeMap<String, Object>();
//		parameters.put("jsapi_ticket", "kgt8ON7yVITDhtdwci0qeTqUSx-g5dabZmOoK4Vc0Fo6BFLGMyMuNaNNz4yrjLIvaKvkPeMwCx72SIv5SFbgaA");
//		parameters.put("noncestr", "ePh4KqH2d7nvsA5p");
//		parameters.put("timestamp", "1441194735577");
//		parameters.put("url", "http://fxj.bsntsh.com/xiehong/paysuccess?openid=oCmZzv5FZuenVbtrmiEGDQ39kDZQ");
//		parameters.put("jsapi_ticket", "kgt8ON7yVITDhtdwci0qeTqUSx-g5dabZmOoK4Vc0FrjDCGtPUTS8wQsjzrk3f3pemXZYcX1mvaoY9OrLD1_Zw");
//		parameters.put("noncestr", "zhNkyX0HeJEBURjj");
//		parameters.put("timestamp", "1444646811");
//		parameters.put("url", "http://fxj.bsntsh.com/xiehong/paysuccess.html?openid=oCmZzv5FZuenVbtrmiEGDQ39kDZQ");
//		String signature = WeixinUtils.createSha1(parameters);
		System.out.println(sign("kgt8ON7yVITDhtdwci0qeTqUSx-g5dabZmOoK4Vc0FrSlfV7Srh5jwCyBEDedsCBfIb2ONTkzt8SMCR3lr87ZQ", "UMPiRurxjQJL2Mfb", "1444647139", "http://fxj.bsntsh.com/xiehong/paysuccess.html?openid=oCmZzv5FZuenVbtrmiEGDQ39kDZQ"));
//		System.out.println(signature);
	}

	public static String getRequestXml(SortedMap<Object, Object> parameters) {
		StringBuffer sb = new StringBuffer();
		sb.append("<xml>");
		Set es = parameters.entrySet();
		Iterator it = es.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String k = (String) entry.getKey() + "";
			String v = entry.getValue() + "";
			if ("attach".equalsIgnoreCase(k) || "body".equalsIgnoreCase(k)
					|| "sign".equalsIgnoreCase(k)) {
				sb.append("<" + k + ">" + "<![CDATA[" + v + "]]></" + k + ">");
			} else {
				sb.append("<" + k + ">" + v + "</" + k + ">");
			}
		}
		sb.append("</xml>");
		return sb.toString();
	}

	public static String httpsRequest(String requestUrl, String requestMethod,
			String outputStr) {
		try {
			// 创建SSLContext对象，并使用我们指定的信任管理器初始化
			TrustManager[] tm = { new MyX509TrustManager() };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());
			// 从上述SSLContext对象中得到SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();
			URL url = new URL(requestUrl);
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
			conn.setSSLSocketFactory(ssf);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			// 设置请求方式（GET/POST）
			conn.setRequestMethod(requestMethod);
			conn.setRequestProperty("content-type",
					"application/x-www-form-urlencoded");
			// 当outputStr不为null时向输出流写数据
			if (null != outputStr) {
				OutputStream outputStream = conn.getOutputStream();
				// 注意编码格式
				outputStream.write(outputStr.getBytes("UTF-8"));
				outputStream.close();
			}
			// 从输入流读取返回内容
			InputStream inputStream = conn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(
					inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(
					inputStreamReader);
			String str = null;
			StringBuffer buffer = new StringBuffer();
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			// 释放资源
			bufferedReader.close();
			inputStreamReader.close();
			inputStream.close();
			inputStream = null;
			conn.disconnect();
			return buffer.toString();
		} catch (ConnectException ce) {
			System.out.println("连接超时：{}" + ce);
		} catch (Exception e) {
			System.out.println("https请求异常：{}" + e);
		}
		return null;
	}

	public static String CreateNoncestr(int length) {
		String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		String res = "";
		for (int i = 0; i < length; i++) {
			Random rd = new Random();
			res += chars.indexOf(rd.nextInt(chars.length() - 1));
		}
		return res;
	}

	public static String CreateNoncestr() {
		String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		String res = "";
		for (int i = 0; i < 16; i++) {
			Random rd = new Random();
			res += chars.charAt(rd.nextInt(chars.length() - 1));
		}
		return res;
	}

	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("http_client_ip");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		// 如果是多级代理，那么取第一个ip为客户ip
		if (ip != null && ip.indexOf(",") != -1) {
			ip = ip.substring(ip.lastIndexOf(",") + 1, ip.length()).trim();
		}
		return ip;
	}

	public static String setXML(String return_code, String return_msg) {
		return "<xml><return_code><![CDATA[" + return_code
				+ "]]></return_code><return_msg><![CDATA[" + return_msg
				+ "]]></return_msg></xml>";
	}
}
