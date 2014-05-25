package com.adil.classroominteractionpart2;




import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class AudioDoubt extends Activity implements OnClickListener {

	AudioSession as;
	int queue=1;
	Socket s;
	BufferedReader br;
	PrintWriter pw;
	final Context context = this;
	TextView queuePos, status;
	Button cancel;
	boolean permissionGranted=false;
    volatile int k=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_audio_doubt_waiting);
		init();
		receiver();
		cancel.setOnClickListener(this);
	}

	private void receiver() {
		// TODO Auto-generated method stub
		Thread th= new Thread(){
			
			public void run(){
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						Random rn = new Random();
						int j=rn.nextInt(10000);
						
						 int i = j % 2;
						
						if(i==0)
						{
							
							status.setText("You are currently in the waiting queue.");
							queuePos.setText("Current Queue Position: "+queue);
							 
							final Handler handler = new Handler();
					        handler.postDelayed(new Runnable() {
					          public void run() {
					        	  status.setText("You are in the active list.");
									queuePos.setText("Wait for your turn.");

					          }
					        }, 2000);
							 
							
					        final Handler handler1 = new Handler();
					        handler1.postDelayed(new Runnable() {
					          public void run() {
					        	  status.setText("Its your turn !!!");
									queuePos.setText("Start Speaking !!!");

					          }
					        }, 2000);
							
							
						
							//status.setText("Its your tarn !!!");
							//queuePos.setText("Start Speaking !!!");
							
						}//end if
						else {
							
							status.setText("You are in the active list.");
							queuePos.setText("Wait for your turn.");
							
							
							 final Handler handler1 = new Handler();
						        handler1.postDelayed(new Runnable() {
						          public void run() {
						        	  status.setText("Its your turn !!!");
										queuePos.setText("Start Speaking !!!");

						          }
						        }, 2000);
							
							
						}//end else
						
					}
				});
					
					
				
				
				
			}//end run
			
		};//End thread
		
		th.start();
		
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
