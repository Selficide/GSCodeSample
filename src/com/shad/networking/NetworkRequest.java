package com.shad.networking;


/**
 * Request 
 *  Abstract base class for HTTP requests
 *  
 * @author Shad
 *
 */
public abstract class NetworkRequest {
	
	protected String url;
	protected String jsonPayload;
	protected RequestType requestType;
	protected RequestListener requestListener;
	
}
