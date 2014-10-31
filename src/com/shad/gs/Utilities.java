package com.shad.gs;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.shad.networking.NetworkManager;

public class Utilities {
	
	private static Utilities instance = null;
	
	
	//Empty initializer
	private Utilities() {
		
	}
	
	public static Utilities getInstance() {
		if(null == instance) {
			instance = new Utilities();
		}
		return instance;
	}
	
	
	/**
	 * Creates and returns a JSON object for GS HTTP requests
	 * 
	 * @param methodName Name of the method to be posted.
	 * @param params Additional info to be added.
	 * @param sessionId the Id of the session. Can be null for startSession call.
	 * @return the JSON object
	 */
	public static String makeGSJSON(String methodName, HashMap<String, String> params, String sessionId)  {
		
		//If there is no sessionId (startSession), make it an empty string
		String session = (null == sessionId) ? "" : ",\"sessionID\":\""+sessionId+"\"";
		
		String json = "{\"method\":\""+methodName+"\",\"header\":{\"wsKey\":\""+NetworkManager.gsKey+"\""+session+"},\"parameters\":[";
		
		if(null == params) {
			return json + "]}";
		}
		
		boolean removeEndComma = false; //In case params is empty
		
		for(String key : params.keySet()) {
			json += "\"" + key + "\":\"" + params.get(key) +"\"," ;
			removeEndComma = true;
		}
		
		if(removeEndComma) {
			json = json.substring(0, json.length() - 1);
		}
		
		return json + "]}";
	}
	
	/**
	 * Generates a HmacMD5 encoded string built from the payload and secret.
	 *  
	 * @param payload String The data to encode in string form.
	 * @param secret String The secret to use for encoding.
	 * @return String The HmacMD5 encoded data.
	 */
	public static String encodeWithHmacMD5(String payload, String secret) {
		
		String sEncodedString = null;
		
		try {
			SecretKeySpec key = new SecretKeySpec((secret).getBytes("UTF-8"), "HmacMD5");
			Mac mac = Mac.getInstance("HmacMD5");
			mac.init(key);
			
			byte[] bytes = mac.doFinal(payload.getBytes("UTF-8"));
			
			StringBuffer hash = new StringBuffer();
			
			for (int index = 0; index < bytes.length; index++) {
				String hex = Integer.toHexString(0xFF & bytes[index]);
				if(1 == hex.length()) {
					hash.append('0');
				}
				hash.append(hex);
			}
			
			sEncodedString = hash.toString();
		} 
		catch (UnsupportedEncodingException e) {}
		catch(InvalidKeyException e){}
		catch (NoSuchAlgorithmException e) {}
		
		return sEncodedString;
	}
	
	/**
	 * Generates MD5 encoded string from the passed-in data(String).
	 *  
	 * @param payload String the data to encode in string form.
	 * @return String the MD5 encoded data.
	 */
	public static String encodeWithMD5(String payload) {
		String encodedString = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] hash = md.digest(payload.getBytes());
			
			StringBuilder sb = new StringBuilder(2*hash.length);
			for(byte b : hash) {
				sb.append(String.format("%02x", b&0xFF));
			}
			encodedString = sb.toString();
		}
		catch (NoSuchAlgorithmException ex) {}
		
		return encodedString;
	}
	
	
}
