package com.adil.classroominteractionpart2;


import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class AudioDoubt extends Activity implements OnClickListener {

	AudioSession as;
	int queue=1;
	Socket s;
	BufferedReader br;
    PrintWriter pw;
	final Context context = this;
	TextView queuePos, status;
	Button cancel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_audio_doubt_waiting);
		init();
		receiver();
		cancel.setOnClickListener(this);
	
	
	 Thread th = new Thread(){
	    	
	    	public void run(){
	    		
	    		runOnUiThread(new Runnable() {

	    			public void run() {

	    				Random rn = new Random();
	    				int i = rn.nextInt() % 2;
	    				if(i==1){
	    					status.setText("You have been added to\n the waiting list");
	    					queuePos.setText("Current Position: 3");
	    					
	    				}//end if
	    				else
	    				{
	    					
	    					status.setText("You are in the\n Active list");
	    					queuePos.setText("Wait for your turn");
	    				}//end else
	    				
	    			}
	    		});
	    		
	    		
	    	}//End run
	    	
	    };//End thread
		
		
		th.start();
		
		
	
	
	
	
	}
   
	
	
	
	private void receiver() {
		// TODO Auto-generated method stub
		while(queue>=0)	//crap condition... to be changed
		{
			if(queue>0)
			{
				status.setText("You are currently in the waiting queue.");
				queuePos.setText("Current Queue Position: "+queue);
			}
			else
			{
				status.setText("You are in the active list.");
				queuePos.setText("Wait for your turn.");
			}
			queue--;
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		status.setText("Its your turn !!!");
		queuePos.setText("Start Speaking !!!");
		queuePos.setOnClickListener(this);
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				as.onWithdrawPress();
				cancel.setOnClickListener(this);
			}
		});
	}

	public void init() {
		queuePos=(TextView) findViewById(R.id.queue_pos);
		status=(TextView) findViewById(R.id.state);
		cancel=(Button) findViewById(R.id.cancel_request);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.audio_doubt, menu);
		return true;
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch(arg0.getId())
		{
		case R.id.queue_pos:
			as=new AudioSession();
			as.onRequestPress();
			break;
		case R.id.cancel_request:
			finish();
			break;
		}
	}

	

}
