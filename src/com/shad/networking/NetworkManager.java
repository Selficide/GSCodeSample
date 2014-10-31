package com.shad.networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.codehaus.jackson.map.ObjectMapper;


public class NetworkManager {
	
	public static String gsUrl = "https://api.grooveshark.com";
	public static String gsAPI = "/ws3.php";
	public static String gsSecret = "NOT_A_REAL_SECRET";
	public static String gsKey = "NOT_A_REAL_KEY";
	private static NetworkManager instance = null;
	
	private ArrayList<NetworkRequest> requestQueue = null;
	private Thread requestThread = null;
	
	//private initializer
	private NetworkManager() {
		
	}
	
	public static NetworkManager getInstance() {
		if(null == instance) {
			instance = new NetworkManager();
		}
		return instance;
	}
	
	
	public void addRequest(NetworkRequest request) {
		if(null == requestQueue) {
			requestQueue = new ArrayList<NetworkRequest>();
		}
		requestQueue.add(request);
		if(requestThread == null) {
			requestThread = new Thread(requestRunnable);
			requestThread.start();
		}
	}
	
	
	/**
	 * A Runnable containing logic for the requestThread to call. Each NetworkRequest is processed, and it's response (or error) is
	 * passed back to the request's listener.  
	 */
	private Runnable requestRunnable = new Runnable(){
	    @Override
	    public void run() {
	    	
	    		while(requestQueue.size() != 0) {
	    			
	    			//Pull the top request
		    		NetworkRequest request = requestQueue.remove(0);
		    		
	    			try {
	    				
						URL url = new URL(request.url);
						URLConnection connection = url.openConnection();
						
						//tell the connection object to allow input/output
						connection.setDoInput(true);
						connection.setDoOutput(true);
						
						connection.connect();
						
						//send the jsonPayload up to the server
						OutputStream os = connection.getOutputStream();
						PrintWriter pw = new PrintWriter(new OutputStreamWriter(os));
						pw.write(request.jsonPayload);
						pw.close();
						
						//read in the response
						InputStream is = connection.getInputStream();
						BufferedReader reader = new BufferedReader(new InputStreamReader(is));
						String line = null;
						StringBuffer sb = new StringBuffer();
						ObjectMapper mapper = new ObjectMapper();
						while((line = reader.readLine()) != null) {
							sb.append(line);
						}
						is.close();
						
						//Depending on the type of request, map the response into one of the predefined types
						switch(request.requestType) {
							case SESSION_REQUEST: {
								GSSessionResponse response = mapper.readValue(sb.toString(), GSSessionResponse.class);
								response.responseType = ResponseType.SESSION_RESPONSE;
								
								if(null == response.errors) {
									request.requestListener.onRequestSuccess(response);
								} else {
									
									//TODO: Better error handling
									request.requestListener.onRequestError(null);
								}
							}
							break;
							case AUTH_REQUEST: {
								
							}
								break;
							default:
								break;
						}
					
	    			} 
	    			catch (MalformedURLException e) {
	    				request.requestListener.onRequestError(null);
	    			} 
	    			catch (IOException e) {
	    				request.requestListener.onRequestError(null);
	    			}

	    		}
	    		
	    		//requestQueue empty
				requestThread = null;	
	    }
	};
	

}

