package net.ekhdemni.presentation.ui.fragments;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.ekhdemni.R;
import net.ekhdemni.presentation.mchUI.vms.VMUser;
import net.ekhdemni.presentation.ui.activities.ConversationsActivity;
import net.ekhdemni.presentation.ui.activities.ForumsActivity;
import net.ekhdemni.presentation.ui.activities.UsersActivity;
import net.ekhdemni.presentation.ui.activities.WorksActivity;
import net.ekhdemni.presentation.ui.activities.auth.LoginActivity;
import net.ekhdemni.presentation.ui.activities.auth.YDUserManager;
import net.ekhdemni.presentation.base.MyActivity;
import tn.core.presentation.base.MyFragment;
import net.ekhdemni.model.models.user.User;
import tn.core.util.Const;
import net.ekhdemni.utils.ImageHelper;
import net.ekhdemni.utils.ProgressUtils;
import net.ekhdemni.model.oldNet.Action;
import net.ekhdemni.model.oldNet.Ekhdemni;
import tn.core.model.net.net.Progress;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import androidx.lifecycle.ViewModelProviders;
import okhttp3.Response;
import okio.BufferedSink;
import okio.Okio;

/**
 * A simple {@link Fragment} subclass.
 */
public class UsersShowFragment extends MyFragment<VMUser> {

    public User user;
    ImageView photo;
    TextView name, post, university, address, summary, email, phone, website , worksCount, postsCount, friendsCount;
    Button message, follow;
    View online, portfelio, posts, friends, showDetails;
    public Integer id, me = 0;


    public static UsersShowFragment newInstance(Integer id) {
        Bundle args = new Bundle();
        args.putInt(Const.ID, id);
        UsersShowFragment fragment = new UsersShowFragment();
        fragment.setArguments(args);
        return fragment;
    }
    public UsersShowFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(VMUser.class);
        mViewModel.callErrors.observe(this, this::onError);
        mViewModel.loadStatus.observe(this, this::onStatusChanged);
        mViewModel.getLiveData().observe(this, this::onDataReceived);
        if(user==null){
            identify();
            getData();
        }
    }

    public void identify(){
        Integer who = getArgs().getInt(Const.ID, 0);
        if (who!=null && who!=0)
            id = who;
        else if (YDUserManager.auth() != null)
            id = YDUserManager.auth().getId();
    }

    @Override
    public void getData() {
        super.getData();
        if (id!=null && id!=0)
            mViewModel.init(id);
    }

    //@Override
    public void onDataReceived(User data) {
        //super.onDataReceived(data);
        user = data;
        resetRelation();
    }


    @Override
    public void onDetach() {
        if(getActivity() instanceof UsersActivity)
            ((UsersActivity) getActivity()).setupToolbar();
        super.onDetach();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_users_show, container, false);

        if(id != 0 && id != null){
            worksCount = v.findViewById(R.id.worksCount);
            postsCount = v.findViewById(R.id.postsCount);
            friendsCount = v.findViewById(R.id.friendsCount);
            portfelio = v.findViewById(R.id.portfelio);
            posts = v.findViewById(R.id.posts);
            friends = v.findViewById(R.id.friends);
            online = v.findViewById(R.id.online);
            name = v.findViewById(R.id.tvName);
            email = v.findViewById(R.id.email);
            phone = v.findViewById(R.id.phone);
            website = v.findViewById(R.id.website);
            university = v.findViewById(R.id.tvEducation);
            address = v.findViewById(R.id.tvAddress);
            summary = v.findViewById(R.id.tvSummary);
            showDetails = v.findViewById(R.id.showDetails);
            post = v.findViewById(R.id.tvTitle);
            photo = v.findViewById(R.id.photo);
            message = v.findViewById(R.id.btnMessage);
            follow = v.findViewById(R.id.follow);
            MyActivity.log("user id: " + id);
        }
        else
            shouldLogin(v);
        return v;
    }




    public void follow(String url){
        Action action = new Action(getContext()) {
            public void doFunction(String s) {
                Log.wtf("ekhdemni.net","get user profile");
                try {
                    JSONObject o = new JSONObject(s);
                    int code = o.optInt("code");
                    if(code==200){
                        user.setRelation(o.optInt("relation"));
                        resetRelation();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        action.url = url; //Ekhdemni.users+"/"+user.id+"/follow";
        action.run();
    }
    void resetRelation(){
        if (user.getRelation()==null) user.setRelation(0);
        if(user.getRelation()==0){
            relations = relationsNew;
        }else if(user.getRelation()==1){
            relations = relationsRequest;
        }else if(user.getRelation()==2){
            relations = relationsFriends;
        }else if(user.getRelation()==3){
            getActivity().onBackPressed();
            return ;
        }
        bind();
    }

    String[] relations = {"Follow", "Block"};
    String[] relationsNew = {"Follow", "Block"};
    String[] relationsFriends = {"remove", "Block"};
    String[] relationsRequest = {"accept", "remove", "Block"};
    void popupList(){
        AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
        b.setItems(relations, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if(relations.length==2)
                    switch(which){
                        case 0:
                            follow(Ekhdemni.relations+"/"+user.getId());
                            break;
                        case 1:
                            follow(Ekhdemni.relations+"/block/"+user.getId());
                            break;
                    }
                else
                    switch(which){
                        case 0:
                            follow(Ekhdemni.relations+"/"+user.getId());
                            break;
                        case 1:
                            follow(Ekhdemni.relations+"/"+user.getId()+"/remove");
                            break;
                        case 2:
                            follow(Ekhdemni.relations+"/block/"+user.getId());
                            break;
                    }
            }
        });

        b.show();
    }

    public void bind(){
        if(user!=null){
            if(name!=null) name.setText(user.getName());
            if(post!=null) post.setText(user.getSpeciality());
            if(photo!=null) ImageHelper.load(photo, user.getPhoto(), 200,200);
            Log.wtf("ekhdemni.net","user photo: " + user.getPhoto());
            if(online!=null)
                if (user.getOnline()){
                    online.setVisibility(View.VISIBLE);
                }else{
                    online.setVisibility(View.GONE);
                }
            if(email!=null) email.setText(user.getEmail());
            if(phone!=null) phone.setText(user.getPhone());
            if(website!=null) website.setText(user.getWebsite()!=null ? user.getWebsite() : "");
            if(university!=null) university.setText(user.getUniversity());
            if(address!=null) address.setText(user.getAddress());
            if(summary!=null) summary.setText(user.getDescription());
            if(postsCount!=null) postsCount.setText(user.getPostsCount()+"");
            if(worksCount!=null) worksCount.setText(user.getWorksCount()+"");
            if(friendsCount!=null) friendsCount.setText(user.getFriendsCount()+"");
            setListeners();
        }
    }


    void setListeners(){
        if(message!=null){
            if(id.equals(me)){
                photo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ((MyActivity) getActivity()).setFragment(new UploadFragment());
                    }
                });
                follow.setText(getText(R.string.resume).toString());
                String updateTxt = getText(R.string.update_profile).toString();
                if(user.getPercent()<100) updateTxt += " ("+user.getPercent()+"%)";
                message.setText(updateTxt);
                message.setOnClickListener(view -> {
                    ((MyActivity) getActivity()).setFragment(UsersUpdateFragment.newInstance(user.getId()));
                });
                follow.setOnClickListener(view -> {
                    checkResumeAvailability();
                });
            }else{
                message.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(getContext(), ConversationsActivity.class);
                        i.putExtra("to", user.getId());
                        i.putExtra("newConv", true);
                        getContext().startActivity(i);
                    }
                });
                follow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popupList();
                    }
                });
            }
            getView().findViewById(R.id.btns).setVisibility(View.VISIBLE);
            portfelio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getActivity(), WorksActivity.class);
                    i.putExtra("url" , Ekhdemni.users+"/"+user.getId()+"/works");
                    startActivity(i);
                }
            });
            posts.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getActivity(), ForumsActivity.class);
                    i.putExtra("showPosts", true);
                    i.putExtra(Const.UID, user.getId());
                    //TopicsFragment.url = Ekhdemni.users+"/"+user.getId()+"/posts";
                    startActivity(i);
                }
            });
            friends.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((MyActivity) getActivity()).setFragment(RelationsFragment.newInstance(user.getId()));
                }
            });
            summary.setOnClickListener(new showResumeListener());
            showDetails.setOnClickListener(new showResumeListener());
        }else init();
    }

    public class showResumeListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            UsersResumeFragment f = new UsersResumeFragment();
            Bundle bundle= new Bundle();
            bundle.putSerializable("user", user);
            f.setArguments(bundle);
            ((MyActivity) getActivity()).setFragment(f);
        }
    }


    public void shouldLogin(View v){
        Log.wtf("ekhdemni.net","shouldLogin");
        View hide = v.findViewById(R.id.profile);
        hide.setVisibility(View.GONE);
        View show = v.findViewById(R.id.shouldLogin);
        show.setVisibility(View.VISIBLE);
        Button goToLogin = show.findViewById(R.id.goToLogin);
        goToLogin.setOnClickListener(view -> getActivity().startActivity(new Intent(getActivity(), LoginActivity.class)));
    }

    public void checkResumeAvailability(){
        if (user.getPercent()<100){
            Toast.makeText(getContext(), "Please complete your profile first", Toast.LENGTH_SHORT).show();
        }else{
            popupResumeOptions();
        }
    }

    void popupResumeOptions(){
        AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
        String[] options = {"Download resume", "Request new resume"};
        b.setItems(options, (dialog, which) -> {
            dialog.dismiss();
            switch(which){
                case 0:
                    download();
                    break;
                case 1:
                    generate();
                    break;
            }
        });

        b.show();
    }


    void generate(){
        Action action = new Action(getContext()) {
            public void doFunction(String s) {
                MyActivity.log("ekhdemni.net","request new resume file");
                try {
                    JSONObject o = new JSONObject(s);
                    //int code = o.optInt("code");
                    JSONArray JA = o.optJSONArray("messages");
                    if (JA!=null && JA.length()>0){
                        String message = JA.getString(0);
                        if (message!=null)
                            Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        action.url = Ekhdemni.requestNewResume;
        action.run();
    }



    ProgressDialog pd;
    void download(){
        if (pd==null)
            pd = ProgressUtils.getProgressDialog(getActivity());
        //pd.setCancelable(false);
        if (!pd.isShowing()) pd.show();
        try {
            new Progress(user.getCv(),  new Progress.ProgressListener() {
                File folder, file;
                boolean firstUpdate = true;

                @Override
                public void onFailed() {
                    if (pd!=null) pd.dismiss();
                }

                @Override public void update(long bytesRead, long contentLength, boolean done) {
                    int per = Math.round(((100 * bytesRead) / contentLength));
                    if (done) {
                        MyActivity.log("completed");
                        if (pd!=null) pd.dismiss();
                    } else {
                        if (firstUpdate) {
                            firstUpdate = false;
                            if (contentLength == -1) {
                                MyActivity.log("content-length: unknown");
                            } else {
                                getActivity().runOnUiThread(() -> {
                                    pd.setProgress(per);
                                    pd.setMessage("Downloading: "+per+"%");
                                });
                                MyActivity.log("content-length: "+ contentLength);
                            }
                        }

                        System.out.println(per+"%");
                        /*if (contentLength != -1) {
                            System.out.format("%d%% done\n", (100 * bytesRead) / contentLength);
                        }*/
                    }
                }

                @Override public void save(Response response){
                    folder = new File(Environment.getExternalStorageDirectory(), "/Downloads");
                    if (!folder.exists()) {
                        boolean folderCreated = folder.mkdir();
                        MyActivity.log("folderCreated", folderCreated + "");
                    }
                    file = new File(folder.getPath() + "/my_resume.pdf");
                    if (file.exists()) {
                        boolean fileDeleted = file.delete();
                        MyActivity.log("fileDeleted", fileDeleted + "");
                    }
                    boolean fileCreated;
                    try {
                        fileCreated = file.createNewFile();
                        MyActivity.log("fileCreated", fileCreated + "");
                    } catch (IOException e) {
                        MyActivity.log("file.createNewFile IOException"+e.getMessage());
                    }
                    BufferedSink sink = null;
                    try {
                        sink = Okio.buffer(Okio.sink(file));
                    } catch (FileNotFoundException e) {
                        MyActivity.log("Okio.buffer IOException"+e.getMessage());
                    }
                    try {
                        //sink.close();
                        sink.writeAll(response.body().source());
                    } catch (IOException e) {
                        MyActivity.log("sink.writeAll IOException"+e.getMessage());
                    }
                    try {
                        sink.close();
                    } catch (IOException e) {
                        MyActivity.log("sink.close() IOException"+e.getMessage());
                    }
                    openFile(file);
                }
            }).run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void openFile(File file){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType( Uri.fromFile( file ), "application/pdf" );
        startActivity(intent);
    }
}
