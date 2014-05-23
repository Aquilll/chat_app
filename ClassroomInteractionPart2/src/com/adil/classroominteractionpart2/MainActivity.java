package com.adil.classroominteractionpart2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;

//import com.adil.classroominteractionpart2.ClientThread;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

	Socket s;
	DataInputStream dis;
	DataOutputStream dos;
	final Context context = this;
	Confirmation c;
	String option, textMsg, textSubject, macAddress;
	Intent confirm;
	ImageButton raiseHandAudio;
	EditText doubtText, doubtSubject;
	Button sendDoubtText;
	private boolean connected = false;
	static Socket socket = null;
	static Socket socket1 = null;
	DataOutputStream dataOutputStream = null;
	DataInputStream dataInputStream = null;
	static BufferedReader fromServer = null;
	PrintWriter out = null;
	TextView server_message;
	int wait=0;
	boolean top_five=false;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
		raiseHandAudio.setOnClickListener(this);
		sendDoubtText.setOnClickListener(this);
	}

	public void init() {
		raiseHandAudio = (ImageButton) findViewById(R.id.audio_doubt_button);
		doubtText = (EditText) findViewById(R.id.text_doubt_msg);
		sendDoubtText = (Button) findViewById(R.id.send_text_doubt);
		doubtSubject = (EditText) findViewById(R.id.doubt_subject);
		server_message = (TextView) findViewById(R.id.myTextView);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.send_text_doubt:
			textMsg = doubtText.getText().toString();
			textSubject = doubtSubject.getText().toString();
			if (textMsg.isEmpty() || textSubject.isEmpty()) {
				Toast.makeText(MainActivity.this, "All fields are mandatory",
						Toast.LENGTH_LONG).show();
			} else {
				createDialog();
				
				}
			break;
			
			
			
		case R.id.audio_doubt_button:
			/*Thread chk_conn = new Thread() {

				public void run() {          
			
					
					try {
						Log.e("trying","Trying to connect" );
					
						socket1 = new Socket("10.105.14.252", 8899);
						Log.e("connection","socket connected" );
					
						
						if(socket1.isConnected()){
							//confirm=new Intent(MainActivity.this, AudioDoubt.class);
							//startActivity(confirm);
							createDialogAudio();
						}//end if
						else {
							
							Toast.makeText(getBaseContext(), "No connection", Toast.LENGTH_LONG).show();
						}//end else
					} catch (UnknownHostException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
					
					
						
					
					
					
				}//end run
			};//end thread
			chk_conn.start();
			
			*/
			//createDialogAudio();
			
			
			
			confirm=new Intent(MainActivity.this,  AudioDoubt.class);
			startActivity(confirm);
			
			
			
			break;
		}
}

	private void createDialog() {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setIcon(R.drawable.ic_launcher);
		builder.setTitle("Submit this message?");
		builder.setMessage("Subject:- " + textSubject + "\n   Doubt:- \n"
				+ textMsg);

		builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.dismiss();
			}
		});
		builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				Log.e("yes", "yes");
				sendTextRequest();
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
	}//end createDialog
	
	
	
	private void createDialogAudio() {
		// TODO Auto-generated method stub
		
		Random rn = new Random();
		int i = rn.nextInt() % 2;
		
		
		if(i==1){
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setIcon(R.drawable.ic_launcher);
			builder.setTitle("Active Status");
			builder.setMessage("You are in the active list\n\n Wait for your turn");
			
			builder.setPositiveButton("Disconnect", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					Log.e("Disconnect", "Dialog Disconnected");
					
				}
			});
			builder.setNegativeButton("back", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.dismiss();
				}
			});
			AlertDialog alert = builder.create();
			alert.show();
		}//end if
		else{
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setIcon(R.drawable.ic_launcher);
			builder.setTitle("Waiting Status");
			
			builder.setMessage("You have been added\n in the waiting list\n\n Current position :");
			
			builder.setPositiveButton("Disconnect", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					Log.e("Disconnect", "Dialog Disconnected");
					
				}
			});
			builder.setNegativeButton("back", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.dismiss();
				}
			});
			AlertDialog alert = builder.create();
			alert.show();
			
			
		}//end else
		

		
		
		
	}//end createDialogAudio

	

	private void sendTextRequest() {
		// TODO Auto-generated method stub

		Toast.makeText(getBaseContext(), "hey", Toast.LENGTH_LONG).show();
		// public class ClientThread implements Runnable {
		Thread client_thread = new Thread() {

			public void run() {
				// TODO Auto-generated method stub

				try {

					// InetAddress serverAddr =
					// InetAddress.getByName(serverIpAddress);
					Log.e("ClientActivity", "C: Connecting...");
					Socket socket = new Socket("10.3.160.129", 8899);
					connected = true;
					while (connected) {
						try {

							Log.e("ClientActivity", "C: Sending command.");
							out = new PrintWriter(new BufferedWriter(
									new OutputStreamWriter(
											socket.getOutputStream())), true); // write
																				// to
																				// server

							out.println(doubtText.getText().toString());
                         
							out.println(getMacAddress());

							out.flush();

							fromServer = new BufferedReader(
									new InputStreamReader(
											socket.getInputStream())); // read
																		// from
																		// server

							final String msgserver = fromServer.readLine();

							runOnUiThread(new Runnable() {

								@Override
								public void run() {
									// textIn.setText(msgserver);
									server_message.setText("msg: " + msgserver);
									System.out.println("message from server "
											+ msgserver);
								}
							});

							connected = false;
							Log.e("ClientActivity", "C: Sent.");
						} catch (Exception e) {
							Log.e("ClientActivity", "S: Error", e);
						}
					}

					Log.e("ClientActivity", "C: Closed.");
				} catch (Exception e) {
					Log.e("ClientActivity", "C: Error", e);
					connected = false;

				} finally {
					if (socket != null) {
						try {
							// close socket connection after using it otherwise
							// next time when u reconnect on the same port
							// it will throw error if you dont close it

							socket.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

				}

			}// End run

		};// end therad
			// @Override

		client_thread.start();

		// }//end ClientThread

	}

	private String getMacAddress() {
		// TODO Auto-generated method stub
		WifiManager wifiManager = (WifiManager) this
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wInfo = wifiManager.getConnectionInfo();
		return wInfo.getMacAddress().toString();
	}// End getMacAddress

}
