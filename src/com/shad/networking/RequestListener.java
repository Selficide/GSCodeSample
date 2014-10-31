package com.shad.networking;

/**
 * Defines callback methods for NetworkRequest objects
 * @author Shad
 *
 */
public interface RequestListener {
	
	public void onRequestSuccess(NetworkResponse response);
	
	public void onRequestError(Error error);

}
