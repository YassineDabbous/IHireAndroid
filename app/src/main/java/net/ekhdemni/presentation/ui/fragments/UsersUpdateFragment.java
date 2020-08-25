package net.ekhdemni.presentation.ui.fragments;


import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.onesignal.OneSignal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import net.ekhdemni.R;
import net.ekhdemni.presentation.mchUI.adapters.ExperiencesAdapter;
import net.ekhdemni.presentation.base.MyActivity;
import tn.core.presentation.base.MyFragment;
import tn.core.presentation.listeners.OnClickItemListener;
import net.ekhdemni.model.models.Experience;
import net.ekhdemni.model.models.user.User;
import net.ekhdemni.model.models.user.UserLanguage;
import net.ekhdemni.model.oldNet.Action;
import net.ekhdemni.model.oldNet.Ekhdemni;
import android.widget.TextView;
import net.ekhdemni.presentation.mchUI.vms.VMUser;
import tn.core.util.Const;

import okhttp3.Call;

/**
 * A simple {@link Fragment} subclass.
 */
public class UsersUpdateFragment extends MyFragment<VMUser> {
    User user;
    Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener date;
    ImageButton addLang;
    Button saveGeneral, saveContact, saveLang, saveEducation, saveSkills, saveMore, saveDesired, addSkills;
    RadioGroup gender;
    CheckBox permis, military;
    EditText name, description, birthday, address, salary, email, phone, website, university, school, graduation, experience, newSkill;
    Spinner speciality, categoriesSpinner, countriesSpinner, workTypeSpinner, degreesSpinner;
    Spinner lang, langLevel;
    LinearLayout skillsLayout;
    LinearLayout languagesLayout;
    Context context;


    public static UsersUpdateFragment newInstance(int id) {
        UsersUpdateFragment f = new UsersUpdateFragment();
        Bundle bundle= new Bundle();
        bundle.putInt(Const.ID, id);
        f.setArguments(bundle);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(VMUser.class);
        mViewModel.callErrors.observe(this, this::onError);
        mViewModel.loadStatus.observe(this, this::onStatusChanged);
        mViewModel.getLiveData().observe(this, this::onDataReceived);
        mViewModel.experiences.observe(this, this::onDataReceived);
        if(user==null){
            int id = getArgs().getInt(Const.ID);
            mViewModel.init(id);
        }
    }

    //@Override
    public void onDataReceived(User data) {
        //super.onDataReceived(data);
        user = data;
        bind();
        mViewModel.experiences(user.getId());
    }
    public void onDataReceived(List<Experience> lista) {
        Log.wtf("ekhdemni.net","Alerts count:"+lista.size());
        if(lista.size() == 0){
            if (getView()!=null) getView().findViewById(R.id.experiences).setVisibility(View.GONE);
        }else {
            recyclerView.setAdapter(new ExperiencesAdapter((OnClickItemListener<Experience>) item -> {
                ((MyActivity) getActivity()).setFragment(UserExperienceFragment.getInstance(item));
            }, lista));
        }
    }

    @Override
    public void clean() {
        super.clean();
        myCalendar = null;
        date = null;
        gender = null;
        skillsLayout = null;
        languagesLayout = null;
        permis = military = null;
        speciality = categoriesSpinner = countriesSpinner = workTypeSpinner = degreesSpinner = null;
        name = description = birthday = address = salary = email = phone = website = university = school = graduation = experience = newSkill = null;
        saveGeneral = saveContact = saveLang = saveEducation = saveSkills = saveMore = saveDesired = addSkills = null;
    }

    @Override
    public void init() {
        super.init();
        View v = getView();
        context = getContext();
        saveGeneral = v.findViewById(R.id.saveGeneral);
        saveContact = v.findViewById(R.id.saveContact);
        saveLang = v.findViewById(R.id.saveLang);
        saveEducation = v.findViewById(R.id.saveEducation);
        saveSkills = v.findViewById(R.id.saveSkills);
        saveMore = v.findViewById(R.id.saveMore);
        saveDesired = v.findViewById(R.id.saveDesired);
        addSkills = v.findViewById(R.id.addSkills);

        gender = v.findViewById(R.id.gender);

        categoriesSpinner = v.findViewById(R.id.category);
        countriesSpinner = v.findViewById(R.id.country);
        workTypeSpinner = v.findViewById(R.id.work_type);

        lang = v.findViewById(R.id.langSpinner);
        langLevel = v.findViewById(R.id.langLevelSpinner);
        addLang = v.findViewById(R.id.addLang);

        languagesLayout = v.findViewById(R.id.languagesLayout);
        skillsLayout = v.findViewById(R.id.skillsLayout);
        newSkill = v.findViewById(R.id.newSkill);
        salary = v.findViewById(R.id.salary);
        permis = v.findViewById(R.id.permis);
        military = v.findViewById(R.id.military);
        experience = v.findViewById(R.id.experience);
        name = v.findViewById(R.id.name);
        description = v.findViewById(R.id.description);
        birthday = v.findViewById(R.id.birthday);
        address = v.findViewById(R.id.address);

        email = v.findViewById(R.id.email);
        phone = v.findViewById(R.id.phone);
        website = v.findViewById(R.id.website);




        university = v.findViewById(R.id.university);
        speciality = v.findViewById(R.id.speciality);
        school = v.findViewById(R.id.school);
        degreesSpinner = v.findViewById(R.id.level);
        graduation = v.findViewById(R.id.graduation);

        addExperience = v.findViewById(R.id.addExperience);
        addExperience.setOnClickListener(view -> ((MyActivity) getActivity()).setFragment(UserExperienceFragment.getInstance(null)));
        recyclerView = v.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
    Button addExperience;
    RecyclerView recyclerView;



    public UsersUpdateFragment() {
        // Required empty public constructor
    }

    ViewGroup container;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.container = container;
        return inflater.inflate(R.layout.fragment_users_update, container, false);
    }
    private void updateLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        birthday.setText(sdf.format(myCalendar.getTime()));
    }




    public void bind(){
        if(gender!=null){
            if(user.getGender().equals("0")) gender.check(R.id.male); else gender.check(R.id.female);
            name.setText(user.getName());
            description.setText(user.getDescription());
            experience.setText(user.getExperience()+" y");
            birthday.setText(user.getBirthday());
            address.setText(user.getAddress());
            email.setText(user.getEmail());
            phone.setText(user.getPhone());
            website.setText(user.getWebsite()!=null ? user.getWebsite() : "");
            university.setText(user.getUniversity());
            school.setText(user.getSchool());
            graduation.setText(user.getYearGraduation());
            address.setText(user.getAddress());
            salary.setText(user.getMinSalary()+"");
            if(user.getMilitaryService().equals("1")) military.setChecked(true);
            if(user.getPermis().equals("1")) permis.setChecked(true);
            categoriesSpinner.setSelection(categoriesListKeys.indexOf(user.getCategoryId()),true);
            speciality.setSelection(specialitiesListKeys.indexOf(user.getSpeciality()),true);
            countriesSpinner.setSelection(countriesListKeys.indexOf(user.getCountryId()),true);
            workTypeSpinner.setSelection(worktypesListKeys.indexOf(user.getWorkTypeName()),true);

            degreesSpinner.setSelection(degreesListKeys.indexOf(user.getDegree()),true);

            updateLanguageLayout();
            updateSkillsLayout();
            setListeners();

            dg = user.getDegreeId()+"";
            cat = user.getCategoryId()+"";
            c = user.getCountryId()+"";
            g = user.getGender();
            sp = user.getSpecialityId()+"";
            wt = user.getWorkTypeId()+"";


            getSpinnerList(cat, Ekhdemni.categories, categoriesList, categoriesListKeys, categoriesSpinner, getContext().getString(R.string.choose_category));
            getSpinnerList(sp,Ekhdemni.categories+"/"+user.getCategoryId()+"/children", specialitiesList, specialitiesListKeys, speciality, getContext().getString(R.string.choose_speciality));
            getSpinnerList(c, Ekhdemni.countries, countriesList, countriesListKeys, countriesSpinner, getContext().getString(R.string.choose_country));
            getSpinnerList(wt, Ekhdemni.worktypes, worktypesList, worktypesListKeys, workTypeSpinner, getContext().getString(R.string.choose_work_type));
            getSpinnerList(dg ,Ekhdemni.degrees, degreesList, degreesListKeys, degreesSpinner, getContext().getString(R.string.choose_degree));
            getSpinnerList("0", Ekhdemni.languages, languages, languagesKeys, lang, getContext().getString(R.string.choose_language));
            getSpinnerList("0" , Ekhdemni.languagesLevels, languagesLevels, languagesLevelsKeys, langLevel, getContext().getString(R.string.choose_language_level));

        }
        else init();
    }

    void setListeners(){
        addLang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(l!=null && ll!=null && lname!=null && llname!=null){
                    String name = lname;
                    String levelName = llname;
                    String lkey = l;
                    String llkey = ll;
                    MyActivity.log("UserLanguage "+name+" key "+lkey+" for position "+lang.getSelectedItemPosition());
                    MyActivity.log("LanguageLevel "+levelName+" key "+llkey+" for position "+langLevel.getSelectedItemPosition());
                    user.getLanguages().add(new UserLanguage(lkey, llkey,name,levelName));
                    MyActivity.log("☺ new Languages list will be : "+user.getLanguages().toString());
                    l = ll = lname = llname = null;
                    lang.setSelection(0, true);
                    langLevel.setSelection(0, true);
                    updateLanguageLayout();
                }else{
                    Toast.makeText(context, "Select new language first", Toast.LENGTH_SHORT).show();
                }
            }
        });
        addSkills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String skl = newSkill.getText().toString();
                if(skl!=null){
                    user.getSkills().add(skl);
                    updateSkillsLayout();
                    newSkill.setText("");
                }
            }
        });


        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(context, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        lang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position>0){
                    l = languagesKeys.get(position)+"";
                    lname = languages.get(position)+"";
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        langLevel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position>0){
                    ll = languagesLevelsKeys.get(position)+"";
                    llname = languagesLevels.get(position)+"";
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        categoriesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position>0){
                    String oldCat = categoriesListKeys.get(position)+"";
                    if (!cat.equals(oldCat)){
                        cat = oldCat;
                        specialitiesList.removeAll(specialitiesList);
                        specialitiesListKeys.removeAll(specialitiesListKeys);
                        getSpinnerList(null,Ekhdemni.categories+"/"+categoriesListKeys.get(position)+"/children", specialitiesList, specialitiesListKeys, speciality, getContext().getString(R.string.choose_speciality));
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        speciality.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        countriesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        degreesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position>0){
                    dg = degreesListKeys.get(position)+"";
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                dg="0";
            }
        });
        workTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position>0){
                    wt = worktypesListKeys.get(position)+"";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                wt="0";
            }
        });

        saveGeneral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveGeneral();
            }
        });
        saveContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveContact();
            }
        });
        saveLang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveLang();
            }
        });
        saveEducation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveEducation();
            }
        });
        saveSkills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveSkills();
            }
        });
        saveDesired.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveDesired();
            }
        });
        saveMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveMore();
            }
        });
    }




    String t,d, b, ad, e ,cat="0",c="0",g="0"
            , l, ll, lname, llname
            , em, ph, web
            , sp="0", univ, sch, dg="0" ,gy
            , wt="0", sal
            , p, m;
    void saveGeneral(){
        if(saveGeneral!=null) saveGeneral.setEnabled(false);
        t = name.getText().toString();
        d = description.getText().toString();
        ad = address.getText().toString();
        b = birthday.getText().toString();
        switch (gender.getCheckedRadioButtonId()){
            case R.id.male: g = "0"; break;
            case R.id.female: g = "1"; break;
            default: g = "0";
        }
        MyActivity.log("store: "+t);
        Action action = new Action(context) {
            public void doFunction(String s) throws JSONException {
                if(saveGeneral!=null) saveGeneral.setEnabled(true);
                JSONObject json = new JSONObject(s);
                int code = json.getInt("code");
                if(code==200){
                    Toast.makeText(context, "Done", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, "Failure", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                super.onFailure(call, e);
                if(saveGeneral!=null) saveGeneral.setEnabled(true);
            }
        };
        action.method = action.POST;
        action.url = Ekhdemni.users+"/general";
        action.params.put("name", t);
        action.params.put("description", d);
        action.params.put("address",  ad);
        action.params.put("gender",  g);
        action.params.put("birthday",  b);
        Object o = c.equals("0") ? null : action.params.put("country_id",  c);
        action.run();
    }
    void saveContact(){
        if(saveContact!=null) saveContact.setEnabled(false);
        em = email.getText().toString();
        ph = phone.getText().toString();
        web = website.getText().toString();
        Action action = new Action(context) {
            public void doFunction(String s) throws JSONException {
                if(saveContact!=null) saveContact.setEnabled(true);
                JSONObject json = new JSONObject(s);
                int code = json.getInt("code");
                if(code==200){
                    Toast.makeText(context, "Done", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, "Failure", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                super.onFailure(call, e);
                if(saveContact!=null) saveContact.setEnabled(true);
            }
        };
        action.method = action.POST;
        action.url = Ekhdemni.users+"/contact";
        action.params.put("email", em);
        action.params.put("phone", ph);
        action.params.put("website",  web);
        MyActivity.log("update contact: "+action.params);
        action.run();
    }
    void saveLang(){
        if(saveLang!=null) saveLang.setEnabled(false);
        Action action = new Action(context) {
            public void doFunction(String s) throws JSONException {
                if(saveLang!=null) saveLang.setEnabled(true);
                JSONObject json = new JSONObject(s);
                int code = json.getInt("code");
                if(code==200){
                    Toast.makeText(context, "Done", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, "Failure", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                super.onFailure(call, e);
                if(saveLang!=null) saveLang.setEnabled(true);
            }
        };
        action.method = action.POST;
        action.url = Ekhdemni.users+"/lang";

        JSONArray userLangsArray = new JSONArray();
        if (user.getLanguages().size()>0) {
            for (UserLanguage userLanguage : user.getLanguages()) {
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("id",userLanguage.getId());
                    jsonObject.put("level_id",userLanguage.getLevelId());
                    userLangsArray.put(jsonObject);
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }
        }
        MyActivity.log("old Languages: "+user.getLanguages().toString());
        MyActivity.log("new Languages: ", userLangsArray.toString());
        action.params.put("languages", userLangsArray.toString());
        if(action.params.size() > 0)
            action.run();
        else{
            Toast.makeText(context, getContext().getString(R.string.choose_language), Toast.LENGTH_SHORT).show();
        }
    }
    void saveEducation(){
        if(saveEducation!=null) saveEducation.setEnabled(false);
        sch = school.getText().toString();
        univ = university.getText().toString();
        gy = graduation.getText().toString();
        e = experience.getText().toString();
        Action action = new Action(context) {
            public void doFunction(String s) throws JSONException {
                if(saveEducation!=null) saveEducation.setEnabled(true);
                JSONObject json = new JSONObject(s);
                int code = json.getInt("code");
                if(code==200){
                    OneSignal.sendTag("category"+sp,"category"+sp);
                    Toast.makeText(context, "Done", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, "Failure", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                super.onFailure(call, e);
                if(saveEducation!=null) saveEducation.setEnabled(true);
            }
        };
        action.method = action.POST;
        action.url = Ekhdemni.users+"/education";
        if(!cat.equals("0")) action.params.put("category_id",  cat);
        if(!dg.equals("0")) action.params.put("degree",  dg);
        if(!sp.equals("0")) action.params.put("speciality",  sp);
        action.params.put("school", sch);
        action.params.put("university", univ);
        action.params.put("year_graduation",  gy);
        action.params.put("experience",  e);
        MyActivity.log("update Education: "+action.params);
        action.run();
    }
    void saveDesired(){
        if(saveDesired!=null) saveDesired.setEnabled(false);
        sal = salary.getText().toString();
        Action action = new Action(context) {
            public void doFunction(String s) throws JSONException {
                if(saveDesired!=null) saveDesired.setEnabled(true);
                JSONObject json = new JSONObject(s);
                int code = json.getInt("code");
                if(code==200){
                    Toast.makeText(context, "Done", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, "Failure", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                super.onFailure(call, e);
                if(saveDesired!=null) saveDesired.setEnabled(true);
            }
        };
        action.method = action.POST;
        action.url = Ekhdemni.users+"/desired";
        action.params.put("salary", sal);
        if(!wt.equals("0")) action.params.put("worktype", wt);
        MyActivity.log("update Desired: "+action.params);
        action.run();
    }
    void saveMore(){
        if(saveMore!=null) saveMore.setEnabled(false);
        p = permis.isChecked() ? "1" : "0";
        m = military.isChecked() ? "1" : "0";
        Action action = new Action(context) {
            public void doFunction(String s) throws JSONException {
                if(saveMore!=null) saveMore.setEnabled(true);
                JSONObject json = new JSONObject(s);
                int code = json.getInt("code");
                if(code==200){
                    Toast.makeText(context, "Done", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, "Failure", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                super.onFailure(call, e);
                if(saveMore!=null) saveMore.setEnabled(true);
            }
        };
        action.method = action.POST;
        action.url = Ekhdemni.users+"/advantages";
        action.params.put("permis", p);
        action.params.put("military", m);
        MyActivity.log("update More: "+action.params);
        action.run();
    }




    void updateLanguageLayout(){
        MyActivity.log("user.languages size: "+user.getLanguages().size());
        languagesLayout.removeAllViews();
        if (user.getLanguages().size()>0){
            LayoutInflater li = LayoutInflater.from(getActivity());
            for (UserLanguage userLanguage: user.getLanguages()){
                View layout = li.inflate(R.layout.item_user_language, null, false);
                Button btn = layout.findViewById(R.id.deleteMe);
                TextView lang = layout.findViewById(R.id.lang);
                TextView langLevel = layout.findViewById(R.id.langLevel);
                lang.setText(userLanguage.getName());
                langLevel.setText(userLanguage.getLevelName());
                languagesLayout.addView(layout);
                btn.setTag(userLanguage.getId());
                btn.setVisibility(View.VISIBLE);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        deleteLang(view.getTag());
                    }
                });
            }
        }
    }
    void deleteLang(Object tag){
        if(tag!=null){
            MyActivity.log("☺ clicked btn tag : "+tag);
            List<UserLanguage> lgs = new ArrayList<>();
            for (UserLanguage userLanguage: user.getLanguages()){
                if (!userLanguage.getId().equals(tag)) lgs.add(userLanguage);
            }
            MyActivity.log("☺ Languages before deleting : "+user.getLanguages().toString());
            user.getLanguages().removeAll(user.getLanguages());
            user.getLanguages().addAll(lgs);
            MyActivity.log("☺ Languages after deleting : "+user.getLanguages().toString());
            updateLanguageLayout();
        }
    }


    void updateSkillsLayout(){
        MyActivity.log("user.languages size: "+user.getSkills().size());
        skillsLayout.removeAllViews();
        int i=0;
        if (user.getSkills().size()>0){
            LayoutInflater li = LayoutInflater.from(getActivity());
            for (String skill: user.getSkills()){
                View layout = li.inflate(R.layout.item_user_skill, null, false);
                Button btn = layout.findViewById(R.id.deleteMe);
                TextView sk = layout.findViewById(R.id.skill);
                sk.setText(skill);
                skillsLayout.addView(layout);
                btn.setTag(i);
                i++;
                btn.setVisibility(View.VISIBLE);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        deleteSkill(view.getTag());
                    }
                });
            }
        }
    }
    void deleteSkill(Object tag){
        if(tag!=null){
            MyActivity.log("☺ clicked btn tag : "+tag);
            List<String> sks = new ArrayList<>();
            for (String s: user.getSkills()){
                if (!s.equals(user.getSkills().get(Integer.parseInt(tag.toString())))) sks.add(s);
            }
            MyActivity.log("☺ skills before deleting : "+user.getLanguages().toString());
            user.getSkills().removeAll(user.getSkills());
            user.getSkills().addAll(sks);
            MyActivity.log("☺ skills after deleting : "+user.getLanguages().toString());
            updateSkillsLayout();
        }
    }

    void saveSkills(){
        if(saveSkills!=null) saveSkills.setEnabled(false);
        Action action = new Action(context) {
            public void doFunction(String s) throws JSONException {
                if(saveSkills!=null) saveSkills.setEnabled(true);
                JSONObject json = new JSONObject(s);
                int code = json.getInt("code");
                if(code==200){
                    Toast.makeText(context, "Done", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, "Failure", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                super.onFailure(call, e);
                if(saveSkills!=null) saveSkills.setEnabled(true);
            }
        };
        action.method = action.POST;
        action.url = Ekhdemni.users+"/skills";

        JSONArray userSkillsArray = new JSONArray();
        if (user.getSkills().size()>0) {
            for (String skl : user.getSkills()) {
                userSkillsArray.put(skl);
            }
        }
        MyActivity.log("old skills: "+user.getSkills().toString());
        MyActivity.log("new skills: ", userSkillsArray.toString());
        action.params.put("skills", userSkillsArray.toString());
        if(action.params.size() > 0)
            action.run();
        else{
            Toast.makeText(context, getContext().getString(R.string.add_skill), Toast.LENGTH_SHORT).show();
        }
    }



    ArrayList<String> worktypesList=new ArrayList<>(), worktypesListKeys =new ArrayList<>();
    ArrayList<String> degreesList=new ArrayList<>(), degreesListKeys =new ArrayList<>();
    ArrayList<String> categoriesList =new ArrayList<>(), categoriesListKeys =new ArrayList<>();
    ArrayList<String> specialitiesList =new ArrayList<>(), specialitiesListKeys =new ArrayList<>();
    ArrayList<String> countriesList =new ArrayList<>(), countriesListKeys =new ArrayList<>();
    ArrayList<String> languages =new ArrayList<>(), languagesKeys =new ArrayList<>();
    ArrayList<String> languagesLevels =new ArrayList<>(), languagesLevelsKeys =new ArrayList<>();

    public void getSpinnerList(final String current, String url, final List<String> list, final List<String> keys, final Spinner spinner, final String chooseText){
        Action action = new Action(context) {
            public void doFunction(String s) throws JSONException {
                JSONObject jsonObj = new JSONObject(s);
                JSONArray jArray =jsonObj.getJSONArray("data");
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
        if(spinner!=null && context!=null){
            spinner.setAdapter(new ArrayAdapter<String>(context, R.layout.spinner_item, list));
            if (current!=null) spinner.setSelection(keys.indexOf(current), true);
        }
    }

}
