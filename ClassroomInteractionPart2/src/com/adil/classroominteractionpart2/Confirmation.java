package com.adil.classroominteractionpart2;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


public class Confirmation extends Dialog implements OnClickListener {

	public Confirmation(Activity parent) {
		super(parent);
		// TODO Auto-generated constructor stub
	}

	String textMsg, option, textSubject, macAddress, name;
	TextView doubtText, doubtSubject;
	Button yes, no;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_confirmation);
		init();
		doubtSubject.setText("Subject:- " + textSubject);
		doubtText.setText("Doubt:-\n" + textMsg);
		yes.setOnClickListener(this);
		no.setOnClickListener(this);
	}

	public void receiveDoubt(String subject, String doubt) {
		textMsg = doubt;
		textSubject = subject;
		
	}

	public void init() {
		doubtText = (TextView) findViewById(R.id.text_doubt);
		doubtSubject = (TextView) findViewById(R.id.text_subject);
		yes = (Button) findViewById(R.id.yes_btn);
		no = (Button) findViewById(R.id.no_btn);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.yes_btn:
			option = "yes";
			break;
		case R.id.no_btn:
			option = "no";
			break;
		}
		Confirmation.this.dismiss();
	}

	
}
