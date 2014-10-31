package com.shad.networking;

import java.io.Serializable;

/**
 * Response
 *  Abstract base class for HTTP responses.
 * @author Shad
 *
 */
public abstract class NetworkResponse implements Serializable {

	/**
	 * serializable id used for caching. Do not change.
	 */
	private static final long serialVersionUID = 4248317575729225079L;
	
	//Status of response, (ex. 200 for Success)
	public int status;

	public ResponseType responseType;
	
	//Defining inner classes
	public static class Header {
		public String hostname;
	}
	
	public static class Result {
		public boolean success;
		public String sessionID;
		
	}
	
	public static class Error {
		public int code;
		public String message;
	}
}
