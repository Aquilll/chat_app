package com.adil.classroominteractionpart2;

import static android.media.MediaRecorder.AudioSource.MIC;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.regex.Pattern;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;




public class AudioSession {
	private boolean isRecording=false;
	private Button      recordButton;
	public  EditText    ipAddressField;
	public  EditText    portField;
	public  AudioRecord recorder;
	public  ProgressBar progressBar;
	private int port          =50005;
	private int sampleRate    =44100;
	private int channelConfig =AudioFormat.CHANNEL_IN_MONO;
	private int encodingFormat=AudioFormat.ENCODING_PCM_16BIT;
	int minBufSize=AudioRecord.getMinBufferSize(sampleRate,channelConfig,encodingFormat)+4096;
	int bufferSize=0;
	public String ipAddress="10.105.14.252";
	private static final String IPV4_NUM       ="([01]?\\d\\d?|2[0-4]\\d|25[0-5])";
	private static final String IP_DOT         ="\\.";
	private static final String IPV4_PATTERN   ="^"+IPV4_NUM+IP_DOT+IPV4_NUM+IP_DOT+IPV4_NUM+IP_DOT+IPV4_NUM+"$";
	public static final  String PERMISSION_TEXT="You may start talking";

  
  public
	void stopStreaming(){
	// queue_pos.setEnabled(false);
		//queue_pos.setText("");   
		recorder.stop();
		recorder.release();
		recorder=null;
		progressBar.setIndeterminate(false);
		//queue_pos.setText("Start speaking");
		//queue_pos.setEnabled(true);
	}
	
	
	public void startStreaming(){
		//queue_pos.setEnabled(false);
		//queue_pos.setText("");
		new Thread(new Runnable(){
			@Override
			public
			void run(){
				try{
					DatagramSocket socket=new DatagramSocket();
					byte[] buffer=new byte[minBufSize];
					DatagramPacket packet;
					final InetAddress destination=InetAddress.getByName(ipAddress);
					recorder=new AudioRecord(MIC,sampleRate,channelConfig,encodingFormat,minBufSize*10);
					recorder.startRecording();
					new Thread(new Runnable(){
						@Override
						public
						void run(){
			//				queue_pos.setText("Stop");
				//			queue_pos.setEnabled(true);
							progressBar.setIndeterminate(true);
						}
					});
					while(isRecording){
						bufferSize=recorder.read(buffer,0,buffer.length);
						packet=new DatagramPacket(buffer,buffer.length,destination,port);
						socket.send(packet);
					}
				}
				catch(UnknownHostException e){
					//Log.e("VS","UnknownHostException");
				}
				catch(SocketException e){
					e.printStackTrace();
				//	Log.e("VS","IOException");
				}
				catch(IOException e){
					e.printStackTrace();
					//Log.e("VS","IOException");
				}
			}
		}).start();
	}//End start streaming

	
	 	public void onRequestPress(){
		final byte[] request = ("Raise Hand").getBytes();
		new Thread(new Runnable(){
			@Override
			public
			void run(){
				try{
				//	port=getPort();
					final InetAddress destination=InetAddress.getByName(ipAddress);
					new DatagramSocket().send(new DatagramPacket(request,request.length,destination,port));
					while(waitingForPermission()) ;
				}
				catch(SocketException e){
					e.printStackTrace();
				}
				catch(UnknownHostException e){
					e.printStackTrace();
				}
				catch(IOException e){
					e.printStackTrace();
				}
			}
		}).start();
	}//End onrequestpress

	private
	boolean waitingForPermission() throws IOException{
		byte[] receiveData=new byte[8192];
		DatagramSocket socket=new DatagramSocket(port);
		DatagramPacket receivePacket=new DatagramPacket(receiveData,receiveData.length);
		socket.receive(receivePacket);
		final String requestText=new String(receivePacket.getData());
	//	Log.d("requestText",requestText);
		if(requestText.contains(PERMISSION_TEXT)){
			isRecording=true;
			startStreaming();
			return false;
		}
		return true;
	}//End waitingforpermission

	public void onWithdrawPress(){
		final byte[] request=("Withdraw").getBytes();
		new Thread(new Runnable(){
			@Override
			public
			void run(){
				try{
					//port=getPort();
					final InetAddress destination=InetAddress.getByName(ipAddress);
					new DatagramSocket().send(new DatagramPacket(request,request.length,destination,port));
					isRecording=false;
					stopStreaming();
				}
				catch(SocketException e){
					e.printStackTrace();
				}
				catch(UnknownHostException e){
					e.printStackTrace();
				}
				catch(IOException e){
					e.printStackTrace();
				}
			}
		}).start();
	}//End onWithdrawpress
	
  



}//End audio session