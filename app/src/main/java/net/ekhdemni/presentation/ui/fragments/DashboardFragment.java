package net.ekhdemni.presentation.ui.fragments;


import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.ekhdemni.R;
import net.ekhdemni.presentation.ui.activities.ForumsActivity;
import net.ekhdemni.presentation.ui.activities.JobsActivity;
import net.ekhdemni.presentation.ui.activities.MainActivity;
import net.ekhdemni.presentation.ui.activities.UsersActivity;
import net.ekhdemni.presentation.ui.activities.WorksActivity;
import tn.core.presentation.base.MyFragment;
import net.ekhdemni.utils.TextUtils;
import net.ekhdemni.model.oldNet.Ekhdemni;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends MyFragment {
    //View view;

    public DashboardFragment() {}
    View forums,projects,profiles,jobs;

    @Override
    public void clean() {
        super.clean();
        if (forums!=null) forums.setOnClickListener(null);
        if (projects!=null) projects.setOnClickListener(null);
        if (profiles!=null) profiles.setOnClickListener(null);
        if (jobs!=null) jobs.setOnClickListener(null);
        forums=projects=profiles=jobs=null;
    }

    @Override
    public void init() {
        super.init();
        jobs = getView().findViewById(R.id.cardJobs);
        forums = getView().findViewById(R.id.cardForums);
        projects = getView().findViewById(R.id.cardProjects);
        profiles = getView().findViewById(R.id.cardProfiles);
        jobs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), JobsActivity.class));
            }
        });
        profiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().startActivity(new Intent(getActivity(), UsersActivity.class));
            }
        });
        forums.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().startActivity(new Intent(getActivity(), ForumsActivity.class));
            }
        });
        projects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), WorksActivity.class);
                i.putExtra("url" , Ekhdemni.works);
                startActivity(i);
            }
        });
        //((MainActivity) getActivity()).slideUp();

        showMsg();
        showCounts();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    public static int jobsCount=0, worksCount=0,postsCount=0,usersCount=0;
    void showCounts(){
        if (jobsCount>0){
            TextView j = getView().findViewById(R.id.jobs);
            j.setText(getString(R.string.jobs)+" ("+jobsCount+")");
        }
        if (worksCount>0){
            TextView w = getView().findViewById(R.id.works);
            w.setText(getString(R.string.works)+" ("+worksCount+")");
        }
        if (postsCount>0){
            TextView c = getView().findViewById(R.id.coffee);
            c.setText(getString(R.string.coffee)+" ("+postsCount+")");
        }
        if (usersCount>0){
            TextView u = getView().findViewById(R.id.users);
            String dot = "<font color='green'><b color='green'>•</b></font>";
            TextUtils.htmlToViewNonClickable(u, getString(R.string.profiles)+" ("+dot+" "+usersCount+")");
        }

    }
    void showMsg(){
        if (MainActivity.alert!=null && !MainActivity.alert.equals("") && getView()!=null){
            TextView text = getView().findViewById(R.id.msgView);
            TextUtils.htmlToView(text, MainActivity.alert);
            getView().findViewById(R.id.msgContainer).setVisibility(View.VISIBLE);
        }
    }

}
