package net.ekhdemni.presentation.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.ekhdemni.R;
import net.ekhdemni.presentation.base.MyActivity;
import net.ekhdemni.model.models.Job;
import net.ekhdemni.presentation.mchUI.vms.VMJob;
import tn.core.util.Const;
import net.ekhdemni.utils.TextUtils;

import androidx.lifecycle.ViewModelProviders;

public class JobDetailsActivity extends MyActivity {


    TextView title, description, place, username, time, work_system, category,speciality , salary, degree, gender, experience;
    Button connect;
    LinearLayout skillsLayout;
    VMJob mViewModel;

    @Override
    public void clean() {
        super.clean();
        skillsLayout = null;
        if(connect != null) connect.setOnClickListener(null);
        title = description = place = username = time = work_system = category = speciality = salary = degree = gender = experience = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_details);
        skillsLayout = findViewById(R.id.requiredSkills);
        title = findViewById(R.id.title);
        description = findViewById(R.id.description);
        place = findViewById(R.id.place);
        username = findViewById(R.id.username);
        time = findViewById(R.id.time);
        work_system = findViewById(R.id.work_system);
        category = findViewById(R.id.category);
        speciality = findViewById(R.id.profile);
        salary = findViewById(R.id.salary);
        degree = findViewById(R.id.degree);
        gender = findViewById(R.id.gender);
        experience = findViewById(R.id.experience);
        connect = findViewById(R.id.connect);


        mViewModel = ViewModelProviders.of(this).get(VMJob.class);
        mViewModel.getLiveData().observe(this, this::bind);
        int id = getIntent().getIntExtra(Const.ID, 0);
        if (id!=0)
            mViewModel.init(id);
    }




    public void bind(Job job){
        if(job.getSkills()!=null && job.getSkills().size()>0){
            findViewById(R.id.requiredSkillsCard).setVisibility(View.VISIBLE);
            MyActivity.log("skills: "+job.getSkills().size()+" => "+job.getSkills().toString());
            LinearLayout.LayoutParams mRparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            for (int i = 0; i < job.getSkills().size(); i++) {
                TextView text = new TextView(this);
                text.setLayoutParams(mRparams);
                text.setText(job.getSkills().get(i));
                skillsLayout.addView(text);
            }
        }
        title.setText(job.getTitle());
        //description.setText(job.getDescription());
        TextUtils.htmlToViewNonClickable(description,job.getDescription());
        place.setText(job.getCountry());
        username.setText(job.getUname());
        time.setText(job.getTimeAgo());
        work_system.setText(job.getWorkType());
        category.setText(job.getCategoryName());
        speciality.setText(job.getSpeciality());
        salary.setText(job.getSalary());
        degree.setText(job.getDegree());
        gender.setText(job.getGender());
        experience.setText(job.getExperienceYears()+"");
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(JobDetailsActivity.this, ConversationsActivity.class);
                i.putExtra("to", job.getUid());
                i.putExtra("newConv", true);
                startActivity(i);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
