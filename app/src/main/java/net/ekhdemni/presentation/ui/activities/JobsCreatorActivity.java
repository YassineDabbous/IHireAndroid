package net.ekhdemni.presentation.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import net.ekhdemni.R;
import net.ekhdemni.presentation.base.MyActivity;
import net.ekhdemni.model.models.requests.Searcher;
import tn.core.util.Const;
import net.ekhdemni.utils.ImageHelper;
import tn.core.util.Utilities;
import net.ekhdemni.model.oldNet.Action;
import net.ekhdemni.model.oldNet.Ekhdemni;
import android.widget.TextView;

public class JobsCreatorActivity extends MyActivity implements View.OnClickListener {
    //ArrayAdapter<String> degreesAdapter;
    List<EditText> requiredSkills = new ArrayList<>();
    LinearLayout skillsLayout;
    EditText title, description, minSalary, maxSalary, experience;
    Spinner specialitySpinner, categorySpinner, countrySpinner, workSystemSpinner, degreesSpinner;
    CheckBox permis;
    RadioGroup gender;
    Button submit, addSkill;
    TextView toolbarTitle;
    ArrayList<String> worktypesList=new ArrayList<>(), worktypesListKeys =new ArrayList<>();
    ArrayList<String> degreesList=new ArrayList<>(), degreesListKeys =new ArrayList<>();
    ArrayList<String> categoriesList =new ArrayList<>(), categoriesListKeys =new ArrayList<>();
    ArrayList<String> countriesList =new ArrayList<>(), countriesListKeys =new ArrayList<>();
    public static boolean isSearch = false;
    public static int searchFor = 0;

    @Override
    public void clean() {
        super.clean();
        skillsLayout = null;
        title = description = minSalary = maxSalary = experience = null;
        specialitySpinner = categorySpinner = countrySpinner = workSystemSpinner = degreesSpinner=null;
        permis = null;
        gender = null;
        submit = addSkill = null;
        toolbarTitle = null;
        worktypesList = degreesList = categoriesList = countriesList = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobs_creator);
        setupToolbar();
        skillsLayout = findViewById(R.id.requiredSkills);
        worktypesList.add(getText(R.string.choose_work_type).toString());
        degreesList.add(getText(R.string.choose_degree).toString());
        categoriesList.add(getText(R.string.choose_category).toString());
        countriesList.add(getText(R.string.choose_country).toString());

        title = findViewById(R.id.title);
        description = findViewById(R.id.description);
        minSalary = findViewById(R.id.minSalary);
        maxSalary = findViewById(R.id.maxSalary);
        experience = findViewById(R.id.experience);
        categorySpinner = findViewById(R.id.category);
        specialitySpinner = findViewById(R.id.speciality);
        countrySpinner = findViewById(R.id.country);
        workSystemSpinner = findViewById(R.id.work_system);
        degreesSpinner = findViewById(R.id.degree);
        permis = findViewById(R.id.permis);
        gender = findViewById(R.id.gender);
        submit = findViewById(R.id.submit);
        addSkill = findViewById(R.id.addSkills);
        imageView = findViewById(R.id.picture);
        imageView.setOnClickListener(this);


        EditText s1 = findViewById(R.id.s1);
        EditText s2 = findViewById(R.id.s2);
        requiredSkills.add(s1);
        requiredSkills.add(s2);

        addSkill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayout.LayoutParams mRparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, (int) getResources().getDimension(R.dimen.edittext_height));
                EditText newSkill = new EditText(getApplicationContext());
                newSkill.setHint(getString(R.string.add_skill));
                newSkill.setLayoutParams(mRparams);
                newSkill.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_edit_text));
                skillsLayout.addView(newSkill);
                requiredSkills.add(newSkill);
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                t = title.getText().toString();
                d = description.getText().toString();
                mns = minSalary.getText().toString();
                mxs = maxSalary.getText().toString();
                e = experience.getText().toString();
                p = permis.isChecked() ? "1" : "0";
                g = "0";
                switch (gender.getCheckedRadioButtonId()){
                    case R.id.male: g = "1"; break;
                    case R.id.female: g = "2"; break;
                    default: g = "0";
                }
                JSONArray skills = new JSONArray();
                for (EditText EDT: requiredSkills) {
                    String s = EDT.getText().toString();
                    if (!s.isEmpty()) skills.put(s);
                }
                rs = skills.toString();
                if(isSearch) search();
                else store();
            }
        });
        setListeners();
        getSpinnerList(Ekhdemni.degrees, degreesList, degreesListKeys, degreesSpinner, getText(R.string.choose_degree).toString());
        getSpinnerList(Ekhdemni.worktypes, worktypesList, worktypesListKeys, workSystemSpinner, getText(R.string.choose_work_type).toString());
        getSpinnerList(Ekhdemni.categories, categoriesList, categoriesListKeys, categorySpinner, getText(R.string.choose_category).toString());
        getSpinnerList(Ekhdemni.countries, countriesList, countriesListKeys, countrySpinner, getText(R.string.choose_country).toString());
        if(isSearch){
            imageView.setVisibility(View.GONE);
            title.setVisibility(View.GONE);
            description.setVisibility(View.GONE);
            minSalary = findViewById(R.id.minSalary);
            maxSalary.setVisibility(View.GONE);
            skillsLayout.setVisibility(View.GONE);
            addSkill.setVisibility(View.GONE);
        }
    }


    void setListeners(){
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position>0){
                    cat = categoriesListKeys.get(position-1)+"";
                    getSpinnerList(Ekhdemni.categories+"/"+categoriesListKeys.get(position-1)+"/children", specialitiesList, specialitiesListKeys, specialitySpinner, getText(R.string.choose_speciality).toString());
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                cat="0";
            }
        });
        specialitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position>0){
                    sp = specialitiesListKeys.get(position-1)+"";
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
                    c = countriesListKeys.get(position-1)+"";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                c="0";
            }
        });
        degreesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position>0){
                    dg = degreesListKeys.get(position-1)+"";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                dg="0";
            }
        });
        workSystemSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position>0){
                    ws = worktypesListKeys.get(position-1)+"";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                ws="0";
            }
        });
    }


    String t="",d="", mns="0",mxs="0",e="0",p="",sp="0",cat="0",c="0",ws="", dg ="0",g="0",rs="";
    public void store(){
        Log.wtf("ekhdemni.net","store: "+t);
        Action action = new Action(getApplicationContext()) {
            public void doFunction(String s) throws JSONException {
                Log.wtf("ekhdemni.net","job result:"+s);
                JSONObject json = new JSONObject(s);
                int code = json.getInt("code");
                if(code==200){
                    Intent intent = new Intent(getApplicationContext(), JobsActivity.class);
                    intent.putExtra(Const.CATEGORY,cat);
                    startActivity(intent);
                    finish();
                }
            }
        };
        action.method = action.POST;
        action.url = Ekhdemni.jobs;
        action.params.put("title", t);
        action.params.put("description", d);
        action.params.put("min_salary",  mns);
        action.params.put("max_salary",  mxs);
        action.params.put("experience",  e);
        action.params.put("permis",  p);
        Object o = cat.equals("0") ? null : action.params.put("category_id",  cat);
        if(!sp.equals("0")) action.params.put("speciality",  sp);
        if(!c.equals("0")) action.params.put("country_id",  c);
        o = dg.equals("0") ? null : action.params.put("degree_id",  dg);
        o = ws.equals("0") ? null : action.params.put("work_type_id",  ws);
        o = g.equals("0") ? null : action.params.put("gender",  g);
        action.params.put("skills",  rs);
        if(bmp != null)
            action.params.put("data", ImageHelper.toBase64(bmp));
        action.run();
    }

    ImageView imageView;
    Bitmap bmp;
    public void search(){
        Log.wtf("ekhdemni.net","search: "+cat);
        String searchUrl = Ekhdemni.searchUsers;
        searchUrl += "?";
        Object o = cat.equals("0") ? null : (searchUrl+="&category_id="+ cat);
        o = c.equals("0") ? null : (searchUrl+="&country_id="+  c);
        o = dg.equals("0") ? null : (searchUrl+="&degree_id="+ dg);
        o = ws.equals("0") ? null : (searchUrl+="&work_type_id="+ ws);
        o = g.equals("0") ? null : (searchUrl+="&gender="+ g);
        o = mns.equals("0") ? null : (searchUrl+="&min_salary="+ mns);
        o = e.equals("0") ? null : (searchUrl+="&experience="+ e);
        o = p.equals("0") ? null : (searchUrl+="&permis="+ p);
        Searcher searcher = new Searcher(cat, c, dg, ws, g, mns, e, p);
        Intent intent;
        if(searchFor==1){
            intent = new Intent(getApplicationContext(), UsersActivity.class);
        }else{
            intent = new Intent(getApplicationContext(), JobsActivity.class);
        }
        intent.putExtra(Const.SEARCH, searcher);
        startActivity(intent);
    }





    ArrayList<String> specialitiesList =new ArrayList<>();
    ArrayList<String> specialitiesListKeys =new ArrayList<>();
    public void getSpinnerList(String url, final List<String> list, final List<String> keys, final Spinner spinner, final String chooseText){
        Action action = new Action(getApplicationContext()) {
            public void doFunction(String s) throws JSONException {
                JSONObject jsonObj = new JSONObject(s);
                JSONArray jArray = jsonObj.getJSONArray("data");
                list.removeAll(list);
                list.add(chooseText);
                for(int i=0;i<jArray.length();i++){
                    JSONObject jsonObject=jArray.getJSONObject(i);
                    list.add(jsonObject.getString("name"));
                    if(keys!=null){
                        keys.add(jsonObject.getString("id"));
                    }
                }
                if(spinner!=null && getApplicationContext()!=null)
                    spinner.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, list));
            }
        };
        action.url = url;
        action.enableCache = true;
        action.run();
    }




    public void onClick(View v) {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto , 1);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case 1:
                if (resultCode == Activity.RESULT_OK) {
                    Uri selectedImage = data.getData();
                    bmp = BitmapFactory.decodeFile(Utilities.getPath(selectedImage, getApplicationContext()));
                    imageView.setImageBitmap(bmp);
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    // User cancelled the image capture
                } else {
                    // Image capture failed, advise user
                }
                break;
        }
    }






    @Override
    public void setupToolbar() {
        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(null);
        toolbarTitle = findViewById(R.id.toolbar_title);

        if (isSearch)  toolbarTitle.setText(getString(R.string.search));
        else           toolbarTitle.setText(getText(R.string.create_job));
        ImageView right = findViewById(R.id.toolbar_right);
        ImageView right2 = findViewById(R.id.toolbar_right_2);
        ImageView left = findViewById(R.id.toolbar_left);
        right.setVisibility(View.INVISIBLE);
        right2.setVisibility(View.INVISIBLE);
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

}
