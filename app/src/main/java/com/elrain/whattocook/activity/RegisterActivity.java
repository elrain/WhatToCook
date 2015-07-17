package com.elrain.whattocook.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.elrain.whattocook.R;
import com.elrain.whattocook.activity.helper.DialogGetter;
import com.elrain.whattocook.message.CommonMessage;
import com.elrain.whattocook.util.Crypting;
import com.elrain.whattocook.util.OnLongPress;
import com.elrain.whattocook.webutil.rest.api.ApiWorker;
import com.elrain.whattocook.webutil.rest.body.UserBody;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;

import de.greenrobot.event.EventBus;

/**
 * Created by elrain on 23.06.15.
 */
public class RegisterActivity extends Activity implements View.OnClickListener {

    private static final int MIN_NAME_LENGTH = 4;
    private static final int MIN_PASSWORD_LENGTH = 6;
    private static final int MAX_NAME_LENGTH = 20;
    private static final int MAX_PASSWORD_LENGTH = 45;
    private EditText mEtName;
    private EditText mEtPassword;
    private EditText mEtPasswordConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_name);
        EventBus.getDefault().register(this);
        Button btnRegisterName = (Button) findViewById(R.id.btnRegisterName);
        btnRegisterName.setOnClickListener(this);
        mEtName = (EditText) findViewById(R.id.etName);
        mEtName.addTextChangedListener(new LoginWatcher());
        initEtPasswords();
    }

    private void initEtPasswords() {
        mEtPassword = (EditText) findViewById(R.id.etPassword);
        mEtPassword.setLongClickable(false);
        mEtPassword.setOnTouchListener(new OnLongPress(this, mEtPassword).getListener());

        mEtPasswordConfirm = (EditText) findViewById(R.id.etPasswordConfirm);
        mEtPasswordConfirm.setLongClickable(false);
        mEtPasswordConfirm.setOnTouchListener(new OnLongPress(this, mEtPasswordConfirm).getListener());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnRegisterName:
            default:
                String name = mEtName.getText().toString();
                String password = mEtPassword.getText().toString();
                String passwordConfirm = mEtPasswordConfirm.getText().toString();

                if (allInfoCorrect(name, password, passwordConfirm)) {
                    try {
                        ApiWorker.getInstance(RegisterActivity.this).registerUser(new UserBody(name,
                                Crypting.encrypt(password)));
                    } catch (GeneralSecurityException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    private class LoginWatcher implements TextWatcher{
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    }

    private boolean allInfoCorrect(String name, String password, String passwordConfirm) {
        if (isNameIncorrect(name)) return false;
        if (isPasswordIncorrect(password)) return false;
        if (isConfirmationFail(password, passwordConfirm)) return false;
        return true;
    }

    private boolean isNameIncorrect(String name) {
        if ("".equals(name)) {
            mEtName.setError(getString(R.string.text_error_empty));
            return true;
        } else if (name.length() < 4) {
            mEtName.setError(String.format(getString(R.string.text_error_length_low), MIN_NAME_LENGTH));
            return true;
        } else if (name.length() > 20) {
            mEtName.setError(String.format(getString(R.string.text_error_length_high), MAX_NAME_LENGTH));
            return true;
        }
        return false;
    }

    private boolean isConfirmationFail(String password, String passwordConfirm) {
        if ("".equals(passwordConfirm)) {
            mEtPasswordConfirm.setError(getString(R.string.text_error_empty));
            return true;
        } else if (!password.equals(passwordConfirm)) {
            mEtPasswordConfirm.setError(getString(R.string.text_error_not_same));
            return true;
        }
        return false;
    }

    private boolean isPasswordIncorrect(String password) {
        if ("".equals(password)) {
            mEtPassword.setError(getString(R.string.text_error_empty));
            return true;
        } else if (password.length() < 6) {
            mEtPassword.setError(String.format(getString(R.string.text_error_length_low), MIN_PASSWORD_LENGTH));
            return true;
        } else if (password.length() > 45) {
            mEtPassword.setError(String.format(getString(R.string.text_error_length_high), MAX_PASSWORD_LENGTH));
            return true;
        } else {
            boolean isOneUpper = false;
            for (int index = 0; index < password.length(); ++index) {
                if (password.charAt(index) == password.toUpperCase().charAt(index)) {
                    isOneUpper = true;
                    break;
                }
            }
            if(!isOneUpper){
                mEtPassword.setError(getString(R.string.text_error_one_upper_symbol));
                return true;}
        }
        return false;
    }

    public void onEventMainThread(CommonMessage message) {
        switch (message.mMessageEvent) {
            case USER_REGISTERED:
                DialogGetter.userRegistered(this, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        RegisterActivity.this.finish();
                    }
                }).show();
                break;
            case ERROR_USER_EXISTS:
                DialogGetter.userAlreadyExists(this).show();
            default:
                break;
        }
    }
}
