package com.elrain.whattocook.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.elrain.whattocook.R;
import com.elrain.whattocook.activity.helper.DialogGetter;
import com.elrain.whattocook.activity.helper.ProgressBarDialogBuilder;
import com.elrain.whattocook.message.CommonMessage;
import com.elrain.whattocook.util.Crypting;
import com.elrain.whattocook.util.NetworkUtil;
import com.elrain.whattocook.util.OnLongPress;
import com.elrain.whattocook.util.Preferences;
import com.elrain.whattocook.webutil.rest.api.ApiWorker;
import com.elrain.whattocook.webutil.rest.api.Errors;
import com.elrain.whattocook.webutil.rest.body.UserBody;
import com.elrain.whattocook.webutil.rest.response.UserInfoResponse;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;

import de.greenrobot.event.EventBus;

/**
 * Created by elrain on 19.06.15.
 */
public class LoginActivity extends Activity implements View.OnClickListener {

    private ProgressDialog mProgressDialog;
    private EditText mEtName;
    private EditText mEtPassword;
    private Preferences mPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        EventBus.getDefault().register(this);
        mPreferences = Preferences.getInstance(this);
        mProgressDialog = new ProgressBarDialogBuilder(this, getString(R.string.dialog_message_logining)).build();
        mEtName = (EditText) findViewById(R.id.etName);
        initEtPassword();
        try {
            mEtName.setText(mPreferences.getUserName());
            mEtPassword.requestFocus();
        } catch (IllegalArgumentException e) {
            mEtName.requestFocus();
        }
        Button btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);
        Button btnLoginNoname = (Button) findViewById(R.id.btnNoName);
        btnLoginNoname.setOnClickListener(this);
        Button btnRegisterNew = (Button) findViewById(R.id.btnRegisterName);
        btnRegisterNew.setOnClickListener(this);
    }

    private void initEtPassword() {
        mEtPassword = (EditText) findViewById(R.id.etPassword);
        mEtPassword.setLongClickable(false);
        mEtPassword.setOnTouchListener(new OnLongPress(this, mEtPassword).getListener());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void onEventMainThread(CommonMessage message) {
        mProgressDialog.dismiss();
        if (message.mMessageEvent == CommonMessage.MessageEvent.LOGIN_ACCEPTED) {
            UserInfoResponse userInfoResponse = (UserInfoResponse) message.messageObject;
            mPreferences.setUserName(mEtName.getText().toString());
            mPreferences.setUserType(userInfoResponse.getIdUserType());
            mPreferences.setUserId(userInfoResponse.getIdUser());
            LoginActivity.this.finish();
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        } else if (message.mMessageEvent == CommonMessage.MessageEvent.API_ERROR) {
            switch ((Errors) message.messageObject) {
                case HTTP_401:
                    DialogGetter.incorrectCredentials(LoginActivity.this).show();
                    break;
                case FAILED_TO_CONNECT_TO:
                case EHOSTUNREACH:
                case ETIMEDOUT:
                case HTTP_404:
                case HTTP_500:
                case HTTP_503:
                    DialogGetter.noServerConnection(LoginActivity.this).show();
                    break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin:
                String password = mEtPassword.getText().toString();
                String name = mEtName.getText().toString();
                if (checkOnNotEmpty(name, password))
                    login(name, password);
                break;
            case R.id.btnNoName:
                mPreferences.setUserType(3);
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                LoginActivity.this.finish();
                break;
            case R.id.btnRegisterName:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;
        }
    }

    private void login(final String name, final String password) {
        if (NetworkUtil.isNetworkOnline(this)) {
            mProgressDialog.show();
            ApiWorker apiWorker = ApiWorker.getInstance(LoginActivity.this);
            UserBody body = null;
            try {
                body = new UserBody(name, Crypting.encrypt(password));
            } catch (GeneralSecurityException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            apiWorker.login(body);
        } else DialogGetter.noInternetDialog(this, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                login(name, password);
            }
        }).show();
    }

    private boolean checkOnNotEmpty(String name, String password) {
        if ("".equals(name)) {
            mEtName.setError(getString(R.string.text_error_empty));
            return false;
        }
        if ("".equals(password)) {
            mEtPassword.setError(getString(R.string.text_error_empty));
            return false;
        }
        return true;
    }


}
