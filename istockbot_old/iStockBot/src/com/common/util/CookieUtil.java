package com.common.util;

import java.util.*;
import javax.servlet.http.*;

/**
 * <pre>
 * com.skmns.utils
 *   - CookieUtil.java
 * </pre>
 * @author		Hyup, Ko
 * @version		1.0
 * @since		2011. 7. 26.
 */
public class CookieUtil  {
	
	public CookieUtil() throws Exception {
	}
	
	/**
	 * ����� ��Ű�� ��� �´�.
	 * ��ȣȭ �Ǿ�ٸ� �� �κп��� ��ȣȭ�� �ؾ� �Ѵ�.
	 * 
	 * @param request 	Request ���尴ü.
	 * @param name		��Ű �̸�.
	 * @param charset	�ɸ��ͼ�.
	 * @return String	��Ű ��.
	 * @throws Exception 
	 * @see
	 */
	
	public static String getCookieValue(HttpServletRequest request, String name, String charset) throws Exception {
		return getCookieValue(request, name, charset, true);
	}
	public static String getCookieValue(HttpServletRequest request, String name, String charset, boolean decrypt) throws Exception {
		String ret = "";
		try {
			Cookie[] cookies = request.getCookies();
			if (cookies != null && cookies.length > 0) {
				for (int i=0;i<cookies.length;i++) {
					if (name.equals(cookies[i].getName())){
						ret = cookies[i].getValue();
					}
				}
				if (!"".equals(ret) && decrypt) {
					ret = deCrypt(java.net.URLDecoder.decode(ret,charset),charset);
				}
			}
		} 
        catch (Exception ex) {
            throw new Exception("CookieUtil.getCookieValue() " + ex.getMessage());
        }
		return ret;
	}
	
	/**
	 * ��Ű�� ���´�.
	 * ��ȣȭ �Ϸ��� �� �κп��� ��ȣȭ �Ѵ�.
	 * 
	 * @param request		Request ���尴ü.
	 * @param response		Response ���尴ü.
	 * @param name			���� ��Ű �̸�.
	 * @param data			���� ��Ű ��.
	 * @param domain		���� ��Ű ������.
	 * @param charset		�ɸ��� ��.
	 * @return void
	 * @throws Exception 
	 * @see
	 */
	public static void setCookie(HttpServletRequest request, HttpServletResponse response, String name, String data, String domain, String charset) throws Exception {
		try {
			// ��Ű�� �ɱ�
			Cookie cookie = new Cookie(name,java.net.URLEncoder.encode(setCrypt(data,charset),charset));
//			System.out.println("[CookieUtil  -  setCookie]  :  " + data);
			if (data == null || "".equals(data)) {
				cookie.setMaxAge(0);
			}
			cookie.setPath("/");
			cookie.setMaxAge(-1);
			response.addCookie(cookie);
		} 
	    catch (Exception ex) {
	        throw new Exception("CookieUtil.setCookie() " + ex.getMessage());
	    }
	}
	
	/**
	 * ��Ű �ð��� �����ؼ� ���´�.
	 * 
	 * @param request		request ���� ��ü.
	 * @param response		response ���� ��ü.
	 * @param name			���� ��Ű �̸�.
	 * @param data			���� ��Ű ��.
	 * @param domain		���� ��Ű ������.
	 * @param charset		�ɸ��� ��.
	 * @param dayofTime		��Ű ���� �ð�.
	 * @return void
	 * @throws Exception
	 * @see
	 */
	public static void setCookieWithTime(HttpServletRequest request, HttpServletResponse response, String name, String data, String domain, String charset, int dayofTime) throws Exception {
		try {
			// ��Ű�� �ɱ�
			Cookie cookie = new Cookie(name,java.net.URLEncoder.encode(setCrypt(data,charset),charset));
//			System.out.println("[CookieUtil  -  setCookieWithTime]  :  " + data);
			if (data == null || "".equals(data)) {
				cookie.setMaxAge(0);
			}
			cookie.setPath("/");
			cookie.setMaxAge(60*60*24*dayofTime);
			response.addCookie(cookie);
		} 
	    catch (Exception ex) {
	        throw new Exception("CookieUtil.setCookie() " + ex.getMessage());
	    }
	}
	
	/**
	 * ��ȣȭ ó�� �Լ�.
	 * 
	 * @param value		��ȣȭ �� ��.
	 * @param charset	�ɸ��� ��.
	 * @return String	��ȣȭ �� ��� ��.
	 * @throws Exception 
	 * @see
	 */
	public static String setCrypt(String value, String charset) throws Exception {
		try {
			return CryptoUtil.encrypt(java.net.URLEncoder.encode(CryptoUtil.encrypt(value),charset));
		} 
	    catch (Exception ex) {
	        throw new Exception("CookieUtil.setCrypt() " + ex.getMessage());
	    }
	}

	/**
	 * ��ȣȭ ó�� �Լ�.
	 * 
	 * @param value		��ȣȭ �� ��.
	 * @param charset	�ɸ��� ��.
	 * @return String	��ȣȭ �� ��� ��.
	 * @throws Exception 
	 * @see
	 */
	public static String deCrypt(String value, String charset) throws Exception  {
		try {
			return CryptoUtil.decrypt(java.net.URLDecoder.decode(CryptoUtil.decrypt(value),charset));
			//return value;
		} catch (Exception ex) {
			System.out.println("CookieUtil.deCrypt()  :  ");
			ex.printStackTrace();
//	        throw new Exception("CookieUtil.deCrypt() " + ex.getMessage());
	    }
		return null;
	}

	/**
	 * ������� ������ String �迭�� Return.
	 * a=b, c=d -> a:b##c:d		field=##, gubun=:
	 * 
	 * @param cookieValue	������ ��Ű ��.
	 * @param field			������ 1.
	 * @param gubun			������ 2.
	 * @return String[][]
	 * @see
	 */
	public String [][] getParams(String cookieValue, String field, String gubun) {
		String [][] ret = null;
        if (cookieValue != null && cookieValue.length() > 0) {
        	String [] st = cookieValue.split(field);
        	ret = new String[st.length][2];
        	for (int i=0;i<st.length;i++) {
        		String [] tm = st[i].split(gubun);
        		ret[i][0] = tm[0];
        		ret[i][1] = tm[1];
        	}
        }
		return ret;
	}

	/**
	 * ������� ������ HashTable�� Return.
	 * 
	 * @param cookieValue		������ ��Ű ��.
	 * @param field				������ 1.
	 * @param gubun				������ 2.
	 * @return Hashtable
	 * @see
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Hashtable getParamsToHash(String cookieValue, String field, String gubun) {
        Hashtable cookieHash = null;
        if (cookieValue != null && cookieValue.length() > 0) {
        	cookieHash		= new Hashtable();
        	StringTokenizer st = new StringTokenizer(cookieValue,field);
        	int stLength = st.countTokens();
        	for (int i=0;i<stLength;i++) {
        		String [] tm = st.nextToken().split(gubun);
        		if (tm.length > 1) {
        			cookieHash.put(tm[0], tm[1]);
        		}
        	}
        }
		return cookieHash;
	}

	/**
	 * ��Ű ����.
	 * 
	 * @param request		Request ���尴ü.
	 * @param response		Response ���� ��ü.
	 * @param name			��Ű �̸�.
	 * @return void			
	 * @throws Exception 
	 * @see
	 */
	public static void removeCookie(HttpServletRequest request, HttpServletResponse response, String name) throws Exception {
		try {
			// ��Ű�� �ɱ�
			Cookie[] cookies = request.getCookies();
		    if (cookies != null && cookies.length > 0) {
		        for (int i = 0 ; i < cookies.length ; i++) {
		            if (cookies[i].getName().equals(name)) {
		                Cookie cookie = new Cookie("name", "");
		                cookie.setMaxAge(0);
		                response.addCookie(cookie);
		                break;
		            }
		        }
		   }			
		} 
	    catch (Exception ex) {
	        throw new Exception("CookieUtil.removeCookie() " + ex.getMessage());
	    }
	}

}
