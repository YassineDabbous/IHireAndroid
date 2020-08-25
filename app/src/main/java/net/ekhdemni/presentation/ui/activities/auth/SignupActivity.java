package net.ekhdemni.presentation.ui.activities.auth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.onesignal.OneSignal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.ekhdemni.R;
import net.ekhdemni.model.models.responses.AuthResponse;
import tn.core.model.responses.BaseResponse;
import net.ekhdemni.presentation.ui.activities.MainActivity;
import net.ekhdemni.presentation.base.MyActivity;
import net.ekhdemni.presentation.mchUI.vms.VMAuth;
import net.ekhdemni.utils.AlertUtils;
import net.ekhdemni.utils.ProgressUtils;
import net.ekhdemni.model.oldNet.Action;
import net.ekhdemni.model.oldNet.Ekhdemni;

import androidx.lifecycle.ViewModelProviders;

public class SignupActivity extends AppCompatActivity {

    private EditText inputEmail, inputPassword, inputName;
    private Button btnSignIn, btnSignUp, btnResetPassword;
    CheckBox accountTypeView;
    Spinner countrySpinner;
    Spinner categoriesSpinner, specialitySpinner;
    ArrayList<String> countriesList =new ArrayList<>(), countriesListKeys =new ArrayList<>();
    ArrayList<String> categoriesList =new ArrayList<>(), categoriesListKeys =new ArrayList<>();
    ArrayList<String> specialitiesList =new ArrayList<>(), specialitiesListKeys =new ArrayList<>();
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        btnSignIn = (Button) findViewById(R.id.sign_in_button);
        btnSignUp = (Button) findViewById(R.id.sign_up_button);
        categoriesSpinner = findViewById(R.id.category);
        specialitySpinner = findViewById(R.id.speciality);
        inputName = (EditText) findViewById(R.id.name);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        accountTypeView = findViewById(R.id.accounttype);
        btnResetPassword = (Button) findViewById(R.id.btn_reset_password);
        countrySpinner = findViewById(R.id.country);

        fillLists();

        setListener();

        pd = ProgressUtils.getProgressDialog(this);
        pd.setMessage(getString(R.string.authentication));
        //pd.setCancelable(false);

        mViewModel = ViewModelProviders.of(this).get(VMAuth.class);
        mViewModel.getLiveData().observe(this, this::onReceive);
        mViewModel.loadStatus.observe(this, this::onStatusChanged);
        mViewModel.callErrors.observe(this, this::onError);
    }
    VMAuth mViewModel;

    void fillLists(){
        getSpinnerList("0",Ekhdemni.countries, countriesList, countriesListKeys, countrySpinner, getText(R.string.choose_country).toString());
        getSpinnerList("0",Ekhdemni.categories, categoriesList, categoriesListKeys, categoriesSpinner, getString(R.string.choose_category));
    }

    String cat="0",c="0",sp="0";
    void setListener(){
        categoriesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position>0){
                    String oldCat = categoriesListKeys.get(position)+"";
                    if (!cat.equals(oldCat)){
                        cat = oldCat;
                        specialitiesList.removeAll(specialitiesList);
                        specialitiesListKeys.removeAll(specialitiesListKeys);
                        getSpinnerList(null,Ekhdemni.categories+"/"+categoriesListKeys.get(position)+"/children", specialitiesList, specialitiesListKeys, specialitySpinner, getString(R.string.choose_speciality));
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        specialitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position>0){
                    sp = specialitiesListKeys.get(position)+"";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                sp="0";
            }
        });
        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position>0){
                    c = countriesListKeys.get(position)+"";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                c="0";
            }
        });


        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this, ResetPasswordActivity.class));
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = inputName.getText().toString().trim();
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                int accounttype = 0;
                if(accountTypeView.isChecked()){
                    accounttype = 1;
                }

                if (TextUtils.isEmpty(name)) {
                    inputName.setError(getString(R.string.enter_your_name));
                    return;
                }

                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    inputEmail.setError(getString(R.string.validate_email));
                    return;
                }

                if (password.length() < 6) {
                    inputPassword.setError(getString(R.string.minimum_password));
                    return;
                }
                if (c==null && c.equals("0")){
                    Toast.makeText(SignupActivity.this, getString(R.string.choose_country), Toast.LENGTH_LONG).show();
                    return;
                }
                if (cat==null && cat.equals("0")){
                    Toast.makeText(SignupActivity.this, getString(R.string.choose_category), Toast.LENGTH_LONG).show();
                    return;
                }
                mViewModel.register(name, email, password, accounttype, Integer.parseInt(c), Integer.parseInt(cat), Integer.parseInt(sp));
            }
        });
    }

    void onStatusChanged(Boolean b){
        MyActivity.log( "• • • onStatusChanged "+b);
        btnSignUp.setEnabled(!b);
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

    void setInputErrors(View input, List<String> errors){
        if (input instanceof EditText) ((EditText) input).setError(errors.get(0));
        else if (input instanceof CheckBox) ((CheckBox) input).setError(errors.get(0));
        else if (input instanceof Spinner) {
            TextView errorText = (TextView) ((Spinner) input).getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);//just to highlight that this is an error
            errorText.setText(errors.get(0));
        }
    }
    public void onReceive(BaseResponse<AuthResponse> response){
        MyActivity.log( "• • • stage 1");
        if (response.getValidation()!=null){
            MyActivity.log( " validation respones "+response.getValidation().size()+" => "+response.getValidation().toString());
            MyActivity.log( "• • • stage 2");
            fillLists();
            MyActivity.log( "• • • stage 3");
            for (Map.Entry<String, List<String>> param : response.getValidation().entrySet()) {
                MyActivity.log( "• • • stage 4 "+ param.getKey());
                switch (param.getKey()){
                    case "name": setInputErrors(inputName, param.getValue()); break;
                    case "email": setInputErrors(inputEmail, param.getValue()); break;
                    case "password": setInputErrors(inputPassword, param.getValue()); break;
                    case "accounttype": setInputErrors(accountTypeView, param.getValue()); break;
                    case "country":  if (categoriesList.size()>0) setInputErrors(countrySpinner, param.getValue()); break;
                    case "category": if (categoriesList.size()>0) setInputErrors(categoriesSpinner, param.getValue()); break;
                    case "speciality": if (specialitiesList.size()>0) setInputErrors(specialitySpinner, param.getValue()); break;
                }
            }
            MyActivity.log( "• • • stage 5");
            return;
        }
        MyActivity.log( "• • • stage 6");
        YDUserManager.save(response.getData());

        String id = response.getData().getId()+"";
        OneSignal.sendTag("user", "user"+id);
        MyActivity.log( "Follow Broadcast: "+"user"+id);
        Intent intent = new Intent(SignupActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }



    public void getSpinnerList(final String current, String url, final List<String> list, final List<String> keys, final Spinner spinner, final String chooseText){
        Action action = new Action(this) {
            public void doFunction(String s) throws JSONException {
                if (errorCode==0){
                    JSONObject jo =new JSONObject(s);
                    JSONArray jArray = jo.getJSONArray("data");
                    list.removeAll(list);
                    keys.removeAll(keys);
                    list.add(chooseText);
                    keys.add("0");
                    for(int i=0;i<jArray.length();i++){
                        JSONObject jsonObject=jArray.getJSONObject(i);
                        list.add(jsonObject.getString("name"));
                        if(keys!=null){
                            keys.add(jsonObject.getString("id"));
                        }
                    }
                    fill(current, list, keys, spinner);
                }else {
                    retry(this);
                }
            }
        };
        action.url = url;
        action.enableCache = true;
        if(list.size()==0)
            action.run();
        else
            fill(current, list, keys, spinner);
    }

    void fill(final String current, final List<String> list, final List<String> keys, final Spinner spinner){
        MyActivity.log("fill me with + "+list);
        if(spinner!=null){
            spinner.setAdapter(new ArrayAdapter<String>(this, R.layout.spinner_item, list));
            if (current!=null) spinner.setSelection(keys.indexOf(current), true);
        }else{
            MyActivity.log("spinner is null");
        }
    }


    public void retry(Action action) {
        AlertUtils.Action a = new AlertUtils.Action() {
            @Override
            public void doFunction(Object o) {
                action.run();
            }
        };
        a.message = getApplicationContext().getString(R.string.network_error);
        AlertUtils.alert(getApplicationContext(), a);
    }

    @Override
    protected void onResume() {
        super.onResume();
        pd.dismiss();
    }
}
