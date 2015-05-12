package com.ilicit.ewerdima.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.ilicit.ewerdima.R;


public class ProgressDialogButton extends Dialog {

	Context context;
	String title, message;
	TextView mTitle, mMessage;

	Button mOk;

	public ProgressDialogButton(Context context, String title, String message) {
		super(context);
		this.context = context;
		this.title = title;
		this.message = message;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.progressbutton);
		init();
	}
	
	private void init() {


		mTitle = (TextView) this.findViewById(R.id.dialog_header);
		mTitle.setText(title);

		mMessage = (TextView) this.findViewById(R.id.dialog_message);
		mMessage.setText(message);


		mOk = (Button) this.findViewById(R.id.buttonCloseDialog);
		mOk.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ProgressDialogButton.this.dismiss();
			}
		});
		

	}

	/*
	 * @param listener You have to call the show method before you can customize
	 * the OK button click listener
	 */
	public void setOkClickedAction(String text,View.OnClickListener listener) {
		mOk.setText(text); 
		mOk.setOnClickListener(listener);
	}
	
	public void setOkClickedAction(View.OnClickListener listener) {
		mOk.setOnClickListener(listener);
	}
	
	public interface Test extends Runnable {
		
	}
}
