package com.group9.nfc.nfctag;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import connection.client.Client;

public class SignInActivity extends Activity {
    private SignInActivity mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_login);
        if (Client.getClient().isLogined()) {
            if (Client.getClient().getUsertype() == Client.USERTYPE_RETAILER) {
                Intent intent = new Intent(this, MainActivity.class);
                Toast.makeText(this, Client.getClient().getUsername()+" sign in succeed", Toast.LENGTH_LONG).show();
                mContext.startActivity(intent);
                mContext.finish();
            } else {
                Intent intent = new Intent(this, MainActivity2.class);
                Toast.makeText(this, "User :" + Client.getClient().getUsername() + " log in succeed", Toast.LENGTH_LONG).show();
                mContext.startActivity(intent);
                mContext.finish();
            }
        }
        TextView textView = (TextView)findViewById(R.id.register_text);
        String register = textView.getText().toString();
        SpannableString spannableString = new SpannableString(register);
        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent intent = new Intent(SignInActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        },5,textView.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(spannableString);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
//        final Button button = (Button) findViewById(R.id.register_button);
//        button.setOnClickListener(new Button.OnClickListener(){
//
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(mContext, RegisterActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        });
    }

    public void signIn(View view) {

        EditText accountView = (EditText) findViewById(R.id.username_edit);
        EditText passwordView = (EditText) findViewById(R.id.password_edit);
        final String username = accountView.getText().toString();
        final String password = passwordView.getText().toString();
        Client.Response response = new Client.AsnyRequest() {
            public Client.Response getResponse() {
                return Client.getClient().validate(username, password);
            }
        }.post();
        if (response.getResult().equals("success")) {
            if (Client.getClient().getUsertype() == Client.USERTYPE_RETAILER) {
                Intent intent = new Intent(this, MainActivity.class);
                Toast.makeText(this, "Admin sign in succeed", Toast.LENGTH_LONG).show();
                intent.putExtra("account", "admin");
                mContext.startActivity(intent);
                mContext.finish();
            } else {
                Intent intent = new Intent(this, MainActivity2.class);
                Toast.makeText(this, "User :" + username + " log in succeed", Toast.LENGTH_LONG).show();
                mContext.startActivity(intent);
                mContext.finish();
            }
        } else {
//            Toast.makeText(this, "input again", Toast.LENGTH_LONG).show();
            Toast.makeText(this, response.getErrorMsg(), Toast.LENGTH_LONG).show();
        }
    }
}
