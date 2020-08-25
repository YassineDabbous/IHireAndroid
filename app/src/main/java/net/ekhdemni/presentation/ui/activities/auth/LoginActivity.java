package net.ekhdemni.presentation.ui.activities.auth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.onesignal.OneSignal;

import java.util.List;

import net.ekhdemni.R;
import net.ekhdemni.model.models.responses.AuthResponse;
import tn.core.model.responses.BaseResponse;
import net.ekhdemni.presentation.ui.activities.MainActivity;
import net.ekhdemni.presentation.base.MyActivity;
import net.ekhdemni.model.models.Broadcast;
import net.ekhdemni.presentation.mchUI.vms.VMAuth;
import net.ekhdemni.utils.ProgressUtils;

import androidx.lifecycle.ViewModelProviders;

public class LoginActivity extends MyActivity {

    VMAuth mViewModel;
    private EditText inputEmail, inputPassword;
    private ProgressDialog pd;
    private Button btnSignup, btnLogin, btnReset;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set the view now
        setContentView(R.layout.activity_login);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        btnSignup = (Button) findViewById(R.id.btn_signup);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnReset = (Button) findViewById(R.id.btn_reset_password);

        pd = ProgressUtils.getProgressDialog(this);
        pd.setMessage(getString(R.string.authentication));

        mViewModel = ViewModelProviders.of(this).get(VMAuth.class);
        mViewModel.getLiveData().observe(this, this::onReceive);
        mViewModel.broadcasts.observe(this, this::onReceiveBroadcasts);
        mViewModel.loadStatus.observe(this, this::onStatusChanged);
        mViewModel.callErrors.observe(this, this::onError);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputEmail.getText().toString();
                final String password = inputPassword.getText().toString();

                if (!validate()) {
                    return;
                }




                mViewModel.login(email,password);


            }
        });
    }

    void onReceiveBroadcasts(List<Broadcast> brs){
        if (brs.size()>0){
            broadcasts.removeAll(broadcasts);
            broadcasts.addAll(brs);
        }
        for (Broadcast b: broadcasts) {
            MyActivity.log( "Follow Broadcast: "+b.getBroadkey());
            OneSignal.sendTag(b.getBroadkey(), b.getBroadvalue());
        }
        Integer id = YDUserManager.auth().getId();
        OneSignal.sendTag("user", "user"+id);
        if(pd!=null) pd.dismiss();
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public boolean validate() {
        boolean valid = true;

        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            inputEmail.setError("enter a valid email address");
            valid = false;
        } else {
            inputEmail.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            inputPassword.setError(getString(R.string.minimum_password));
            valid = false;
        } else {
            inputPassword.setError(null);
        }

        return valid;
    }


    public void onReceive(BaseResponse<AuthResponse> response){
        YDUserManager.save(response.getData());
        mViewModel.broadcasts();
    }


    void onStatusChanged(Boolean b){
        if(btnLogin!=null) btnLogin.setEnabled(!b);
        if (b){
            if(pd!=null) pd.show();
        }else {
            if(pd!=null) pd.dismiss();
        }
    }
    void onError(List<String> errors){
        //Toast.makeText(SignupActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
        for (String s: errors)
            Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }

}