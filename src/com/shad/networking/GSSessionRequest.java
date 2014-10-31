package com.shad.networking;

import com.shad.gs.Utilities;

public class GSSessionRequest extends NetworkRequest {
	
	public GSSessionRequest(RequestListener listener) {
		jsonPayload = Utilities.makeGSJSON("startSession", null, null);
		url = NetworkManager.gsUrl+NetworkManager.gsAPI
				+"?sig="+Utilities.encodeWithHmacMD5(jsonPayload, NetworkManager.gsSecret);
		requestType = RequestType.SESSION_REQUEST;
		requestListener = listener;
	}

}
