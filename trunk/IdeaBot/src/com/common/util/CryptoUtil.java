package com.common.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

import sun.misc.BASE64Decoder;

/**
 * <pre>
 * com.skmns.utils
 *   - CryptoUtil.java
 * </pre>
 * @author		Hyup, Ko
 * @version		1.0
 * @since		2011. 7. 26.
 */
@SuppressWarnings("restriction")
public class CryptoUtil{
    /**
     * ���Ͼ�ȣȭ�� ���̴� ���� ũ�� ����
     */
    public static final int kBufferSize = 8192;
	public static java.security.Key key = null;
	public static final String defaultkeyfileurl = "defaultkey.key";

	
	/**
     * ���Ű ��޼ҵ�
     * @return  void
     * @exception java.io.IOException,java.security.NoSuchAlgorithmException
     */
 
	public static java.io.File makekey() throws java.io.IOException,java.security.NoSuchAlgorithmException{
		return makekey(defaultkeyfileurl);
	}
	public static java.io.File makekey(String filename) throws java.io.IOException,java.security.NoSuchAlgorithmException{
		java.io.File tempfile = new java.io.File(".",filename);
		javax.crypto.KeyGenerator generator = javax.crypto.KeyGenerator.getInstance("DES");
		generator.init(new java.security.SecureRandom());
		java.security.Key key = generator.generateKey();
		java.io.ObjectOutputStream out = new java.io.ObjectOutputStream(new java.io.FileOutputStream(tempfile));
		out.writeObject(key);
		out.close();
		return tempfile;
	}

    /**
     * ������ ���Ű�� ������ ���� �޼���
     * @return  Key ���Ű Ŭ����
     * @exception Exception
     */
	private static java.security.Key getKey() throws Exception{
		if (key != null){
		   return key;
		} else {
		   return getKey(defaultkeyfileurl);
		}
	}
	private static java.security.Key getKey(String fileurl) throws Exception{
		if(key == null){
			java.io.File file = new java.io.File(fileurl);
			if(!file.exists()){
				file = makekey();
			}
			if(file.exists()){
				java.io.ObjectInputStream in = new java.io.ObjectInputStream(new java.io.FileInputStream(fileurl));
				key = (java.security.Key)in.readObject();
				in.close();
			} else {
				throw new Exception("��ȣŰ��ü�� ���� �� ����ϴ�.");
			}
		}
		return key;
	}
 
    /**
     * ���ڿ� ��Ī ��ȣȭ
     * @param   ID  ���Ű ��ȣȭ�� ����ϴ� ���ڿ�
     * @return  String  ��ȣȭ�� ID
     * @exception Exception
     */
	public static String encrypt(String ID) throws Exception{
	   if ( ID == null || ID.length() == 0 ) return "";
	   javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance("DES/ECB/PKCS5Padding");
	   cipher.init(javax.crypto.Cipher.ENCRYPT_MODE,getKey());
	   String amalgam = ID;
	  
	   byte[] inputBytes1 = amalgam.getBytes("UTF8");
	   byte[] outputBytes1 = cipher.doFinal(inputBytes1);
	   sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();        
	   String outputStr1 = encoder.encode(outputBytes1);
	   return outputStr1;
	}
 
    /**
     * ���ڿ� ��Ī ��ȣȭ
     * @param   codedID  ���Ű ��ȣȭ�� ����ϴ� ���ڿ�
     * @return  String  ��ȣȭ�� ID
     * @exception Exception
     */
	public static String decrypt(String codedID) throws Exception{
	   if ( codedID == null || codedID.length() == 0 ) return "";
	   javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance("DES/ECB/PKCS5Padding");
	   cipher.init(javax.crypto.Cipher.DECRYPT_MODE, getKey());
	   sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
	  
	   byte[] inputBytes1  = decoder.decodeBuffer(codedID);
	   byte[] outputBytes2 = cipher.doFinal(inputBytes1);
	  
	   String strResult = new String(outputBytes2,"UTF8");
	   return strResult;
	}
 
    /**
     * ���� ��Ī ��ȣȭ
     * @param   infile ��ȣȭ�� ����ϴ� ���ϸ�
     * @param   outfile ��ȣȭ�� ���ϸ�
     * @exception Exception
     */
    public static void encryptFile(String infile, String outfile) throws Exception{
    	javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance("DES/ECB/PKCS5Padding");
		cipher.init(javax.crypto.Cipher.ENCRYPT_MODE,getKey());
		
		java.io.FileInputStream in = new java.io.FileInputStream(infile);
		java.io.FileOutputStream fileOut = new java.io.FileOutputStream(outfile);
		
		javax.crypto.CipherOutputStream out = new javax.crypto.CipherOutputStream(fileOut, cipher);
		byte[] buffer = new byte[kBufferSize];
		int length;
		while((length = in.read(buffer)) != -1)
			out.write(buffer,0,length);
		in.close();
		out.close();
	}

    /**
     * ���� ��Ī ��ȣȭ
     * @param   infile ��ȣȭ�� ����ϴ� ���ϸ�
     * @param   outfile ��ȣȭ�� ���ϸ�
     * @exception Exception
     */
    public static void decryptFile(String infile, String outfile) throws Exception{
            javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance("DES/ECB/PKCS5Padding");
            cipher.init(javax.crypto.Cipher.DECRYPT_MODE,getKey());
 
            java.io.FileInputStream in = new java.io.FileInputStream(infile);
            java.io.FileOutputStream fileOut = new java.io.FileOutputStream(outfile);
 
            javax.crypto.CipherOutputStream out = new javax.crypto.CipherOutputStream(fileOut, cipher);
            byte[] buffer = new byte[kBufferSize];
            int length;
            while((length = in.read(buffer)) != -1)
                    out.write(buffer,0,length);
            in.close();
            out.close();
    }
    
    /**
     * ���� 1Byte �� �߶� HEX(2Byte) �� ��ȯ
     * @param   str �� ���ڿ�
     * @param   ret ��ȯ�� ���ڿ�
     * @exception Exception
     */
	public static String EncordeOnePass(String str) {
		String ret = "";
		if (str.length() > 0) {
			// byte array -> hex string
			ret = new java.math.BigInteger(str.getBytes()).toString(16);
		}
		return ret;
	}

    /**
     * ���� 2Byte �� �߶� ASCII(1Byte) �� ��ȯ
     * @param   str �� ���ڿ�
     * @param   ret ��ȯ�� ���ڿ�
     * @exception Exception
     */
    public static String DecordeOnePass(String str) {
    	String ret = "";
        try {
    		if (str.length() > 0) {    	
    	    	// hex string -> byte array
    	    	byte[] bytes = new java.math.BigInteger(str, 16).toByteArray();
    	    	ret = new String(bytes, "EUC-KR");
    		}
        } catch (UnsupportedEncodingException e) {

        } 
		return ret;		
	}    
    
    /**
     * BASE64 ���ڵ�
     */
   public static String decodeBase64(String strDecode) {
		byte[] buf = null;
		BASE64Decoder base64Decoder = new BASE64Decoder();
		ByteArrayInputStream bin = new ByteArrayInputStream(strDecode.getBytes());
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
     
	    try {
	      base64Decoder.decodeBuffer(bin, bout);
	    } catch (Exception e) {
	      System.out.println("CryptoUtil.decodeBase64() Exception");
	      e.printStackTrace();
	    }
     
		buf = bout.toByteArray();
		return new String(buf);
   }
}

