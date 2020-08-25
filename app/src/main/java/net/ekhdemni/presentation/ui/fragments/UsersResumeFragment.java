package net.ekhdemni.presentation.ui.fragments;


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
import android.widget.LinearLayout;

import net.ekhdemni.R;
import net.ekhdemni.presentation.mchUI.adapters.ExperiencesAdapter;
import net.ekhdemni.presentation.base.MyActivity;
import tn.core.presentation.base.MyFragment;
import net.ekhdemni.model.models.Experience;
import net.ekhdemni.model.models.user.User;
import net.ekhdemni.model.models.user.UserLanguage;
import net.ekhdemni.presentation.mchUI.vms.VMUser;

import android.widget.TextView;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class UsersResumeFragment extends MyFragment<VMUser> {

    public User user;


    public UsersResumeFragment() {
        // Required empty public constructor
    }
    LinearLayout skillsLayout;
    LinearLayout languagesLayout;
    RecyclerView recyclerView;
    TextView name, gender, birthday, address, country, email, phone, web, description,
            category, degree, university, school, speciality, graduation,
            permis, military, salary, jobType;
    String me = "";


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(VMUser.class);
        mViewModel.callErrors.observe(this, this::onError);
        mViewModel.loadStatus.observe(this, this::onStatusChanged);
        mViewModel.experiences.observe(this, this::onDataReceived);
    }


    public void onDataReceived(List<Experience> lista) {
        Log.wtf("ekhdemni.net","Alerts count:"+lista.size());
        if(lista.size() == 0){
            if (getView()!=null) getView().findViewById(R.id.experiences).setVisibility(View.GONE);
        }else {
            recyclerView.setAdapter(new ExperiencesAdapter(null, lista));
        }
    }


    @Override
    public void clean() {
        super.clean();
        languagesLayout = skillsLayout =null;
        name = gender = birthday = address = country = email = phone = web = description = category = degree = university = school = speciality = graduation = permis = military = salary = jobType = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_users_resume, container, false);

        user = (User) getArgs().getSerializable("user");

        recyclerView = v.findViewById(R.id.recycler_view);
        skillsLayout = v.findViewById(R.id.skillsLayout);
        languagesLayout = v.findViewById(R.id.languagesLayout);
        name = v.findViewById(R.id.name);
        gender = v.findViewById(R.id.gender);
        birthday = v.findViewById(R.id.birthday);
        address = v.findViewById(R.id.address);
        country = v.findViewById(R.id.country);
        email = v.findViewById(R.id.email);
        description = v.findViewById(R.id.description);
        phone = v.findViewById(R.id.phone);
        web = v.findViewById(R.id.website);
        category = v.findViewById(R.id.category);
        degree = v.findViewById(R.id.degree);
        university = v.findViewById(R.id.university);
        school = v.findViewById(R.id.school);
        speciality = v.findViewById(R.id.speciality);
        graduation = v.findViewById(R.id.graduation);
        permis = v.findViewById(R.id.permis);
        military = v.findViewById(R.id.military);
        salary = v.findViewById(R.id.salary);
        jobType = v.findViewById(R.id.work_type);

        name.setText(user.getName());
        gender.setText(user.getGender());
        birthday.setText(user.getBirthday());
        address.setText(user.getAddress());
        country.setText(user.getCountry());
        email.setText(user.getEmail());
        description.setText(user.getDescription());
        phone.setText(user.getPhone());
        web.setText(user.getWebsite());
        category.setText(user.getCategory());
        degree.setText(user.getDegree());
        university.setText(user.getUniversity());
        school.setText(user.getSchool());
        speciality.setText(user.getSpeciality());
        graduation.setText(user.getYearGraduation());
        permis.setText(user.getPermis());
        military.setText(user.getMilitaryService());
        salary.setText(user.getMinSalary()+"");
        jobType.setText(user.getWorkTypeName());

        if (user.getLanguages()!=null && user.getLanguages().size()>0){
            LayoutInflater li = LayoutInflater.from(getActivity());
            for (UserLanguage userLanguage: user.getLanguages()){
                View layout = li.inflate(R.layout.item_user_language, null, false);
                TextView lang = layout.findViewById(R.id.lang);
                TextView langLevel = layout.findViewById(R.id.langLevel);
                lang.setText(userLanguage.getName());
                langLevel.setText(userLanguage.getLevelName());
                languagesLayout.addView(layout);
            }
        }else {
            v.findViewById(R.id.languages).setVisibility(View.GONE);
        }
        MyActivity.log("user.getLanguages(): "+user.getLanguages());


        if (user.getSkills().size()>0){
            LayoutInflater li = LayoutInflater.from(getActivity());
            for (String skill: user.getSkills()){
                View layout = li.inflate(R.layout.item_user_skill, null, false);
                TextView skl = layout.findViewById(R.id.skill);;
                skl.setText(skill);
                skillsLayout.addView(layout);
            }
        }else {
            v.findViewById(R.id.skillsCard).setVisibility(View.GONE);
        }
        MyActivity.log("user.getSkills(): "+user.getSkills());


        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mViewModel.experiences(user.getId());
        return v;
    }


}
