package com.elrain.whattocook.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.elrain.whattocook.R;
import com.elrain.whattocook.activity.helper.DialogGetter;
import com.elrain.whattocook.message.CommonMessage;
import com.elrain.whattocook.webutil.rest.api.ApiWorker;
import com.elrain.whattocook.webutil.rest.body.UserBody;

import de.greenrobot.event.EventBus;

/**
 * Created by elrain on 23.06.15.
 */
public class RegisterActivity extends Activity implements View.OnClickListener {

    private static final int MIN_NAME_LENGTH = 4;
    private static final int MIN_PASSWORD_LENGTH = 6;
    private static final int MAX_NAME_LENGTH = 20;
    private static final int MAX_PASSWORD_LENGTH = 45;
    private EditText etName;
    private EditText etPassword;
    private EditText etPasswordConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_name);
        EventBus.getDefault().register(this);
        Button btnRegisterName = (Button) findViewById(R.id.btnRegisterName);
        btnRegisterName.setOnClickListener(this);
        etName = (EditText) findViewById(R.id.etName);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etPasswordConfirm = (EditText) findViewById(R.id.etPasswordConfirm);
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
                String name = etName.getText().toString();
                String password = etPassword.getText().toString();
                String passwordConfirm = etPasswordConfirm.getText().toString();

                if (allInfoCorrect(name, password, passwordConfirm)) {
                    ApiWorker.getInstance(RegisterActivity.this).registerUser(new UserBody(name, password));
                }
                break;
        }
    }

    private boolean allInfoCorrect(String name, String password, String passwordConfirm) {
        if ("".equals(name)) {
            etName.setError(getString(R.string.text_error_empty));
            return false;
        } else if (name.length() < 4) {
            etName.setError(String.format(getString(R.string.text_error_length_low), MIN_NAME_LENGTH));
            return false;
        } else if (name.length() > 20) {
            etName.setError(String.format(getString(R.string.text_error_length_high), MAX_NAME_LENGTH));
            return false;
        }

        if ("".equals(password)) {
            etPassword.setError(getString(R.string.text_error_empty));
            return false;
        } else if (password.length() < 6) {
            etPassword.setError(String.format(getString(R.string.text_error_length_low), MIN_PASSWORD_LENGTH));
            return false;
        } else if (password.length() > 45) {
            etPassword.setError(String.format(getString(R.string.text_error_length_high), MAX_PASSWORD_LENGTH));
            return false;
        }

        if ("".equals(passwordConfirm)) {
            etPasswordConfirm.setError(getString(R.string.text_error_empty));
            return false;
        } else if (!password.equals(passwordConfirm)) {
            etPasswordConfirm.setError(getString(R.string.text_error_not_same));
            return false;
        }
        return true;
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
