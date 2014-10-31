package com.shad.gs;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

import com.shad.networking.GSSessionRequest;
import com.shad.networking.GSSessionResponse;
import com.shad.networking.NetworkManager;
import com.shad.networking.NetworkResponse;
import com.shad.networking.RequestListener;

public class MainActivity extends Activity implements RequestListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        GSSessionRequest request = new GSSessionRequest(this);
        NetworkManager.getInstance().addRequest(request);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    
	@Override
	public void onRequestSuccess(NetworkResponse response) {

		switch(response.responseType) {
		case AUTH_RESPONSE:
			break;
		case SESSION_RESPONSE:{
			GSSessionResponse gsResp = (GSSessionResponse)response;
			updateResultText("SessionID = "+gsResp.result.sessionID);
		}
			break;
		default:
			break;
		
		}
	}


	@Override
	public void onRequestError(Error error) {
		updateResultText("There was an error");
	}
	
	
//Helper methods
	
	private void updateResultText(final String text) {
		runOnUiThread(new Runnable(){

			@Override
			public void run() {
				((TextView) findViewById(R.id.result_text_view)).setText(text);
			}});
	}
    
}
