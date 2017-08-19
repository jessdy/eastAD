package com.shzejing.spring.common;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import javax.imageio.ImageIO;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.io.FilenameUtils;

public class Common {

	public static Date nextNDay(Date day, int n) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(day);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH)
				+ n);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		return new Date(calendar.getTimeInMillis());
	}

	public static JSONObject response(int code, String msg, JSONObject data) {
		JSONObject jo = new JSONObject();
		jo.put("err_code", code);
		jo.put("err_msg", msg);
		jo.put("data", data);
		return jo;
	}

	public static JSONObject response(String successString, JSONObject data) {
		return response(0, successString, data);
	}

	public static JSONObject response(int code, String msg, JSONArray data) {
		JSONObject jo = new JSONObject();
		jo.put("err_code", code);
		jo.put("err_msg", msg);
		jo.put("data", data);
		return jo;
	}

	public static JSONObject response(String successString, JSONArray data) {
		return response(0, successString, data);
	}

	/**
	 * 截取图片
	 * 
	 * @param srcImageFile
	 *            原图片地址
	 * @param x
	 *            截取时的x坐标
	 * @param y
	 *            截取时的y坐标
	 * @param desWidth
	 *            截取的宽度
	 * @param desHeight
	 *            截取的高度
	 */
	public static String imgCut(String src, String path, Float x, Float y,
			Float desWidth, Float desHeight) {
		try {
			src = src.substring(8); // 去掉开头的"/upload/"
			File srcImage = new File(path + src);
			String baseName = FilenameUtils.getBaseName(srcImage.getName());
			String extName = FilenameUtils.getExtension(srcImage.getName());
			Image img;
			ImageFilter cropFilter;
			BufferedImage bi = ImageIO.read(srcImage);
			int srcWidth = bi.getWidth();
			int srcHeight = bi.getHeight();
			if (srcWidth >= desWidth && srcHeight >= desHeight) {
				Image image = bi.getScaledInstance(srcWidth, srcHeight,
						Image.SCALE_DEFAULT);
				cropFilter = new CropImageFilter(x.intValue(), y.intValue(),
						desWidth.intValue(), desHeight.intValue());
				img = Toolkit.getDefaultToolkit().createImage(
						new FilteredImageSource(image.getSource(), cropFilter));
				BufferedImage tag = new BufferedImage(desWidth.intValue(),
						desHeight.intValue(), BufferedImage.TYPE_INT_RGB);
				Graphics g = tag.getGraphics();
				g.drawImage(img, 0, 0, null);
				g.dispose();
				// 输出文件
				String dest = src.replace(baseName, baseName + "s");
				ImageIO.write(tag, extName, new File(path + dest));
				return "/upload/" + dest;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/upload/" + src;
	}

	public static String minuteToTimeStr(long time) {
		long hour = time / 60;
		long min = time % 60;
		return hour + "时" + min + "分";
	}

	public static String httpGet(String url) {
		HttpClient client = new HttpClient();
		GetMethod method = new GetMethod(url);
		try {
			int statusCode = client.executeMethod(method);

			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("Method failed: " + method.getStatusLine());
			}
			
			String responseBody = new String(method.getResponseBody(), "utf8");
			return responseBody;
		} catch (HttpException e) {
			System.err.println("Fatal protocol violation: " + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Fatal transport error: " + e.getMessage());
			e.printStackTrace();
		} finally {
			method.releaseConnection();
		}
		return null;
	}

	@SuppressWarnings("deprecation")
	public static String httpPost(String url, String body) {
		HttpClient client = new HttpClient();
		PostMethod post = new PostMethod(url);
		post.setRequestBody(body);
		try {
			int statusCode = client.executeMethod(post);

			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("Method failed: " + post.getStatusLine());
			}

			String responseBody = new String(post.getResponseBody(), "utf8");
			return responseBody;
		} catch (HttpException e) {
			System.err.println("Fatal protocol violation: " + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Fatal transport error: " + e.getMessage());
			e.printStackTrace();
		} finally {
			post.releaseConnection();
		}
		return null;
	}
}
