package net.ekhdemni.presentation.ui.activities.auth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import net.ekhdemni.R;
import net.ekhdemni.presentation.base.MyActivity;
import tn.core.model.responses.BaseResponse;
import net.ekhdemni.presentation.mchUI.vms.VMAuth;
import net.ekhdemni.utils.ProgressUtils;

import java.util.List;

public class ResetPasswordActivity extends AppCompatActivity {

    private EditText inputEmail;
    private Button btnReset, btnBack;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        inputEmail = (EditText) findViewById(R.id.email);
        btnReset = (Button) findViewById(R.id.btn_reset_password);
        btnBack = (Button) findViewById(R.id.btn_back);

        pd = ProgressUtils.getProgressDialog(this);
        pd.setMessage(getString(R.string.authentication));

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputEmail.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    inputEmail.setError(getString(R.string.validate_email));
                    return;
                }
                mViewModel.recover(email);
            }
        });

        mViewModel = ViewModelProviders.of(this).get(VMAuth.class);
        mViewModel.recover.observe(this, this::onReceive);
        mViewModel.loadStatus.observe(this, this::onStatusChanged);
        mViewModel.callErrors.observe(this, this::onError);
    }
    VMAuth mViewModel;
    void onReceive(BaseResponse<Object> response){
        Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
    void onStatusChanged(Boolean b){
        MyActivity.log( "• • • onStatusChanged "+b);
        btnReset.setEnabled(!b);
        if (b){
            pd.show();
        }else {
            pd.dismiss();
        }
    }
    void onError(List<String> errors){
        //Toast.makeText(SignupActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
        for (String s: errors)
            Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }

}