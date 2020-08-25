package net.ekhdemni.presentation.ui.fragments;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.DatePickerDialog;
import android.os.Bundle;
import com.google.android.material.textfield.TextInputLayout;

import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;

import net.ekhdemni.R;
import net.ekhdemni.presentation.base.MyActivity;
import tn.core.presentation.base.MyFragment;
import net.ekhdemni.model.models.Experience;
import net.ekhdemni.utils.TextUtils;
import net.ekhdemni.model.oldNet.Action;
import net.ekhdemni.model.oldNet.Ekhdemni;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 *
 * A simple {@link Fragment} subclass.
 */
public class UserExperienceFragment extends MyFragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    public Experience experience;
    CheckBox untilNow;
    Spinner spinner;
    TextInputLayout title, description, company, position, address, country, city, url, started_at, ended_at;
    Button send, delete;

    Calendar myCalendar, myCalendar2;
    DatePickerDialog.OnDateSetListener date, date2;

    public UserExperienceFragment() {
        // Required empty public constructor
    }

    public static UserExperienceFragment getInstance(Experience  model) {
        UserExperienceFragment f = new UserExperienceFragment();
        if (model!=null){
            Bundle bundle= new Bundle();
            bundle.putSerializable("model", model);
            f.setArguments(bundle);
        }
        return f;
    }

    void v(View view, boolean b){
        if (b){
            view.animate()
                    .alpha(1.0f)
                    .setDuration(300)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            view.setVisibility(View.VISIBLE);
                        }
                    });
        }else{
            view.animate()
                    .alpha(0.0f)
                    .setDuration(300)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            view.setVisibility(View.GONE);
                        }
                    });
        }
    }
    void setViewsVisibility(List<String> fields){
        if (!fields.contains("title")) {v(title, false);} else {v(title, true);}
        if (!fields.contains("company")) {v(company, false);} else {v(company, true);}
        if (!fields.contains("position")) {v(position, false);} else {v(position, true);}
        if (!fields.contains("address")) {v(address, false);} else {v(address, true);}
        if (!fields.contains("country")) {v(country, false);} else {v(country, true);}
        if (!fields.contains("city")) {v(city, false);} else {v(city, true);}
        if (!fields.contains("url")) {v(url, false);} else {v(url, true);}
        if (!fields.contains("description")) {v(description, false);} else {v(description, true);}
        if (!fields.contains("started_at")) {v(started_at, false);} else {v(started_at, true);}
        if (!fields.contains("ended_at")) {v(ended_at, false);v(untilNow, false);} else {v(ended_at, true);v(untilNow, true);}
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_experience, container, false);

        title = view.findViewById(R.id.titleWrapper);
        company = view.findViewById(R.id.companyWrapper);
        position = view.findViewById(R.id.positionWrapper);
        address = view.findViewById(R.id.addressWrapper);
        country = view.findViewById(R.id.countryWrapper);
        city = view.findViewById(R.id.cityWrapper);
        url = view.findViewById(R.id.urlWrapper);
        started_at = view.findViewById(R.id.started_atWrapper);
        ended_at = view.findViewById(R.id.endDateWrapper);
        description = view.findViewById(R.id.descriptionWrapper);

        untilNow = view.findViewById(R.id.untilNow);
        spinner = view.findViewById(R.id.type);
        send = view.findViewById(R.id.send);
        delete = view.findViewById(R.id.delete);
        send.setOnClickListener(this::onClick);
        untilNow.setOnCheckedChangeListener(this::onCheckedChanged);
        delete.setOnClickListener(view1 -> deleteMe());

        setDates();
        experience = (Experience) getArgs().getSerializable("model");
        if (experience==null) experience = new Experience();


        bind();
        return view;
    }



    private List<String> types, keys;
    private List<List<String>> fields;

    public void bind() {
        MyActivity.log("type is => "+experience.type);

        if (experience.id!=null && !experience.id.isEmpty()) delete.setVisibility(View.VISIBLE);

        if(null!=experience.title) title.getEditText().setText(experience.title);
        if(null!=experience.company) company.getEditText().setText(experience.company);
        if(null!=experience.position) position.getEditText().setText(experience.position);
        if(null!=experience.address) address.getEditText().setText(experience.address);
        if(null!=experience.country) country.getEditText().setText(experience.country);
        if(null!=experience.city) city.getEditText().setText(experience.city);
        if(null!=experience.url) url.getEditText().setText(experience.url);
        if(null!=experience.started_at) title.getEditText().setText(experience.started_at);
        if(null!=experience.ended_at) title.getEditText().setText(experience.ended_at);
        if(null!=experience.description) title.getEditText().setText(experience.description);

        types = new ArrayList();
        keys = new ArrayList();
        fields = new ArrayList();

        types.add("Experience"); keys.add("0");     fields.add(new ArrayList<String>(Arrays.asList("title", "company", "position", "address", "country", "city","url", "started_at", "ended_at", "description")));
        types.add("Study"); keys.add("1");          fields.add(new ArrayList<String>(Arrays.asList("title", "company", "position", "country", "city", "started_at", "ended_at", "description")));
        types.add("Certification"); keys.add("2");  fields.add(new ArrayList<String>(Arrays.asList("title","company","position", "url", "started_at", "ended_at", "description")));
        types.add("Publication"); keys.add("3");    fields.add(new ArrayList<String>(Arrays.asList("title","company", "url","started_at", "description")));
        types.add("Organization"); keys.add("4");   fields.add(new ArrayList<String>(Arrays.asList("title","company", "started_at","ended_at", "description")));
        types.add("Award"); keys.add("5");          fields.add(new ArrayList<String>(Arrays.asList("title","company","position","address", "url","started_at", "description")));

        fill(experience.type, types, keys, spinner);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                experience.type = keys.get(position);
                setViewsVisibility(fields.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    void fill(final String current, final List<String> list, final List<String> keys, final Spinner spinner){
        if(spinner!=null && getContext()!=null){
            spinner.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.spinner_item, list));
            if (current!=null) {
                spinner.setSelection(keys.indexOf(current), true);
                if (keys.indexOf(current)>0 && keys.indexOf(current)<fields.size())
                    setViewsVisibility(fields.get(keys.indexOf(current)));
            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (b){
            getView().findViewById(R.id.endDateWrapper).setVisibility(View.GONE);
        }else{
            getView().findViewById(R.id.endDateWrapper).setVisibility(View.VISIBLE);
        }
    }

    public void onClick(View v) {
        String t = TextUtils.getText(title);
        String cm = TextUtils.getText(company);
        String d = TextUtils.getText(description);
        String p = TextUtils.getText(position);
        String a = TextUtils.getText(address);
        String c = TextUtils.getText(country);
        String ct = TextUtils.getText(city);
        String u = TextUtils.getText(url);
        String s = TextUtils.getText(started_at);
        String e = TextUtils.getText(ended_at);
        if(true){ //!t.isEmpty()
            experience.title = t;
            experience.description = d;
            experience.company = cm;
            experience.position = p;
            experience.address = a;
            experience.country = c;
            experience.city = ct;
            experience.url = u;
            experience.started_at = s;
            experience.ended_at = e;
            save(experience);
        }
    }




    void setDates(){
        myCalendar = myCalendar2 = Calendar.getInstance();
        date = (view1, year, monthOfYear, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        };
        date2 = (view1, year, monthOfYear, dayOfMonth) -> {
            myCalendar2.set(Calendar.YEAR, year);
            myCalendar2.set(Calendar.MONTH, monthOfYear);
            myCalendar2.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        };
        started_at.getEditText().setOnClickListener(v -> {new DatePickerDialog(getContext(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();});
        ended_at.getEditText().setOnClickListener(v -> {new DatePickerDialog(getContext(), date, myCalendar2.get(Calendar.YEAR), myCalendar2.get(Calendar.MONTH), myCalendar2.get(Calendar.DAY_OF_MONTH)).show();});
    }


    private void updateLabel() {
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        started_at.getEditText().setText(sdf.format(myCalendar.getTime()));
        ended_at.getEditText().setText(sdf.format(myCalendar2.getTime()));
    }


    public void save(Experience exp){
        MyActivity.log("save Experience: "+exp);
        Action action = new Action(getContext()) {
            public void doFunction(String s) {
                if(parse(s))
                    getActivity().onBackPressed();
            }
        };
        if (experience.id==null || experience.id.isEmpty()){
            action.method = action.POST;
            action.url = Ekhdemni.experiences;
        }else {
            action.method = action.POST;
            action.url = Ekhdemni.experiences+"/"+experience.id;
        }
        action.params.put("id", exp.id);
        action.params.put("type", exp.type);
        action.params.put("title", exp.title);
        action.params.put("company", exp.company);
        action.params.put("description", exp.description);
        action.params.put("country", exp.country);
        action.params.put("city", exp.city);
        action.params.put("position", exp.position);
        action.params.put("address", exp.address);
        action.params.put("started_at", exp.started_at);
        if (!untilNow.isChecked()) action.params.put("ended_at", exp.ended_at);
        action.params.put("url", exp.url);
        action.isJson = true;
        action.run();
    }
    void deleteMe(){
        MyActivity.log("delete Experience: "+experience.title);
        Action action = new Action(getContext()) {
            public void doFunction(String s) {
                if(parse(s))
                    getActivity().onBackPressed();
            }
        };
        action.method = action.DELETE;
        action.url = Ekhdemni.experiences+"/"+experience.id;
        action.run();
    }
}