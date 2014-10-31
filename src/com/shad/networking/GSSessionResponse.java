package com.shad.networking;

import java.util.ArrayList;


public class GSSessionResponse extends NetworkResponse {

	private static final long serialVersionUID = -3592644075429467377L;
	
	public Header header;
	public Result result;
	public ArrayList<Error> errors;
	
}
