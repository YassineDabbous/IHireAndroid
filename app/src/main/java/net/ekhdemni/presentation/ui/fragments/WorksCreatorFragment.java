package net.ekhdemni.presentation.ui.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import net.ekhdemni.R;
import net.ekhdemni.presentation.ui.activities.WorksActivity;
import net.ekhdemni.presentation.base.MyActivity;
import tn.core.presentation.base.MyFragment;
import net.ekhdemni.utils.AlertUtils;
import net.ekhdemni.utils.ImageHelper;
import net.ekhdemni.utils.ProgressUtils;
import net.ekhdemni.model.oldNet.Action;
import net.ekhdemni.model.oldNet.AsynClient;
import net.ekhdemni.model.oldNet.Ekhdemni;

/**
 * A simple {@link Fragment} subclass.
 */
public class WorksCreatorFragment extends MyFragment implements View.OnClickListener  {
    EditText title,description;
    ImageView imageView;
    Spinner categoriesSpinner, specialitySpinner;
    Button send;
    ArrayList<String> categoriesList =new ArrayList<>(), categoriesListKeys =new ArrayList<>();
    ArrayList<String> specialitiesList =new ArrayList<>(), specialitiesListKeys =new ArrayList<>();
    public final int RESULT_GALLERY = 11, MAX_PHOTOS = 4;
    public ArrayList<Img> listImages = new ArrayList<>();
    private int countPhotos = 0;
    private LinearLayout containerPhotos;
    int uploaded = 0;
    int workId = 0;
    String cat="", d, t, sp="0"; ProgressDialog pd;
    @Override
    public void clean() {
        super.clean();
        title = description = null;
        categoriesSpinner = specialitySpinner = null;
        imageView = null;
        send = null;
        //containerPhotos = null;
    }

    @Override
    public void init() {
        View v = getView();
        categoriesSpinner = v.findViewById(R.id.category);
        specialitySpinner = v.findViewById(R.id.speciality);
        title = v.findViewById(R.id.title);
        description = v.findViewById(R.id.description);
        send = v.findViewById(R.id.submit);
        imageView = v.findViewById(R.id.picture);
        containerPhotos = v.findViewById(R.id.photo_container);
        pd = ProgressUtils.getProgressDialog(getContext());
        imageView.setOnClickListener(this);
        if (listImages==null)
            listImages = new ArrayList<>();
        ((WorksActivity) getActivity()).setToolbarTitle(R.string.add_work);
        setListeners();
        if(categoriesList.size()==0)
            getSpinnerList(Ekhdemni.categories, categoriesList, categoriesListKeys, categoriesSpinner, getContext().getString(R.string.choose_category));
    }


    public WorksCreatorFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_works_creator, container, false);
    }
    void setListeners(){
        pd.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                if (send!=null){
                    send.setAlpha(1f);
                    send.setEnabled(true);
                }
            }
        });
        pd.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if (send!=null){
                    send.setAlpha(1f);
                    send.setEnabled(true);
                }
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(cat==null || cat.equals("")){
                    Toast.makeText(getContext(), getString(R.string.choose_category), Toast.LENGTH_SHORT).show();
                    return;
                }


                t = title.getText().toString();
                d = description.getText().toString();


                MyActivity.log("images count: "+listImages.size());
                if(listImages.size()>0){
                    try {
                        launchCompressing();
                    } catch (OutOfMemoryError outOfMemoryError) {
                        //Toast.makeText(getContext(), "OutOfMemoryError", Toast.LENGTH_SHORT).show();
                    }
                }
                else Toast.makeText(getContext(), getString(R.string.min_photos), Toast.LENGTH_SHORT).show();
            }
        });
        categoriesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position>0){
                    cat = categoriesListKeys.get(position-1);
                    sp="0";
                    getSpinnerList(Ekhdemni.categories+"/"+cat+"/children", specialitiesList, specialitiesListKeys, specialitySpinner, getContext().getText(R.string.choose_speciality).toString());
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                cat="1";
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
    }
    public void store(){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (pd!=null){
                    pd.setMessage(getResources().getString(R.string.saving_work));
                    pd.show();
                }
            }
        });
        Action action = new Action(getContext()) {
            public void doFunction(String s) throws JSONException {
                Log.wtf("ekhdemni.net","job result:"+s);
                JSONObject json = new JSONObject(s);
                int code = json.optInt("code");
                if(code==200){
                    workId = json.optInt("data");
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (pd != null) pd.dismiss();
                            getActivity().getIntent().putExtra("url",Ekhdemni.categories+"/"+((sp!="0") ? sp : cat)+"/works");
                            containerPhotos = null;
                            ((MyActivity) getActivity()).setFragment(new WorksFragment());
                        }
                    });
                }else{
                    final String m = json.optString("message");
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (send!=null){
                                send.setAlpha(1f);
                                send.setEnabled(true);
                            }
                            if (pd != null) pd.dismiss();
                            Toast.makeText(getActivity(), m, Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        };
        action.method = action.POST;
        action.url = Ekhdemni.works;
        action.params.put("title", t);
        action.params.put("description", d);
        if(!cat.equals("0")) action.params.put("category_id",  cat);
        if(!sp.equals("0")) action.params.put("speciality",  sp);
        action.run();
    }

    void launchCompressing(){

        if (send!=null){
            send.setAlpha(0.5f);
            send.setEnabled(false);
        }

        MyActivity.log("START Compressing ☺☺☺ ...");
        if (pd!=null){
            pd.setMessage("Compressing");
            pd.show();
        }
        imagesUploadSucceed = true; //reset
        int i=0;
        for (Img img: listImages) {
            if(!img.compressed){
                try {
                    compressMe(img, i);
                } catch (Exception e) {
                    MyActivity.log(e.getMessage());
                }
            }
            i++;
        }
        MyActivity.log("END Compressing ☺☺☺");
        if (pd!=null){
            pd.dismiss();
        }
        launchUploading();
    }
    public void compressMe(Img img,final int index) {
        try {
            MyActivity.log("compressing done");
            File f = new File(ImageHelper.resizeAndCompressImageBeforeSend(getContext(), getRealPathFromUri(getContext(),img.uri), "cache_"+(index)+".jpg"));
            final Uri uri = Uri.fromFile(f);
            MyActivity.log("compressing done");
            Img newImG = new Img(false, uri);
            newImG.compressed = true;
            listImages.set(index, newImG);
            MyActivity.log("Image "+index+" was compressed and added to list !! ☺ , path is: "+newImG.path);
        } catch (Exception e) {
            MyActivity.log("compressing failed, Image "+index+" was not compressed !! ☻☻");
            e.printStackTrace();
        }

    }





    void launchUploading(){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (pd!=null){
                    pd.setMessage(getResources().getString(R.string.uploading_pictures));
                    pd.show();
                }
            }
        });
        imagesUploadSucceed = true; //reset
        int i=0;
        for (Img img: listImages) {
            if(!img.uploaded){
                try {
                    upload("3" ,img, i);
                } catch (Exception e) {
                    MyActivity.log(e.getMessage());
                }
            }
            i++;
        }
    }


    public void getSpinnerList(String url, final List<String> list, final List<String> keys, final Spinner spinner, final String chooseText){
        Action action = new Action(getContext()) {
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
                if(spinner!=null && getContext()!=null)
                    spinner.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.spinner_item, list));
            }
        };
        action.url = url;
        action.run();
    }

    public void onClick(View v) {
        if (countPhotos < MAX_PHOTOS) {
            startGalleryIntent();
        } else {
            Toast.makeText(getContext(), getResources().getString(R.string.max_photos), Toast.LENGTH_SHORT).show();
        }
    }


    ///////////////////////////////////////////////////////////////////////////////////

    class Img{
        public boolean compressed = false;
        boolean uploaded = false;
        String path = "";
        Uri uri;
        public Img(boolean uploaded, Uri uri) {
            this.uploaded = uploaded;
            this.uri = uri;
            this.path = uri.getPath();//getRealPathFromUri(getContext(), uri);
            MyActivity.log("the new PATH is: "+path);
        }
    }


    public void startGalleryIntent() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, RESULT_GALLERY);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode== RESULT_GALLERY && resultCode == getActivity().RESULT_OK) {
            createNewPhoto(data.getData());
        }
    }

    private void createNewPhoto(final Uri uri1) {
        try {

            int width = (int) getResources().getDimension(R.dimen.with_simple_drawee);
            int raduis = (int) getResources().getDimension(R.dimen.raduis_img);
            int marginLeft = (int) getResources().getDimension(R.dimen.raduis_img);
            int widthBtn = (int) getResources().getDimension(R.dimen.with_btn);
            countPhotos++;

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, width);
            layoutParams.setMargins(0, 0, marginLeft, 0);
            LinearLayout.LayoutParams lpPhoto = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            RelativeLayout.LayoutParams lpBtn = new RelativeLayout.LayoutParams(widthBtn, widthBtn);
            lpBtn.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

            Button btnDelete = new Button(getContext());
            btnDelete.setBackgroundResource(R.drawable.close);
            btnDelete.setLayoutParams(lpBtn);

            final RelativeLayout relativeLayout = new RelativeLayout(getContext());
            relativeLayout.setLayoutParams(layoutParams);

            ImageView simpleDraweeView = new ImageView(getContext());
            simpleDraweeView.setLayoutParams(lpPhoto);

            relativeLayout.addView(simpleDraweeView);
            relativeLayout.addView(btnDelete);
            containerPhotos.addView(relativeLayout,0);

            //RoundingParams roundingParams = RoundingParams.fromCornersRadius(raduis);
            //simpleDraweeView.getHierarchy().setRoundingParams(roundingParams);
            simpleDraweeView.setImageURI(uri1);
            listImages.add(new Img(false, uri1));

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deletePhoto(relativeLayout, uri1);
                }
            });
        } catch (Exception e) {
            MyActivity.log("compressing filed");
            e.printStackTrace();
        }
    }

    private void deletePhoto(RelativeLayout relativeLayout, Uri uri) {
        int i = getPositionOfUri(uri);
        if(i<listImages.size()){
            listImages.remove(i);
            countPhotos--;
            containerPhotos.removeView(relativeLayout);
        }

    }

    private int getPositionOfUri(Uri uri) {
        for (int i = 0; i < listImages.size(); i++) {
            if (listImages.get(i).uri == uri)
                return i;
        }
        return -1;
    }

    boolean imagesUploadSucceed = true;
    public void upload(String type, Img path,final int index) {
        try {
            okhttpForm(type, path, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    // Something went wrong
                    MyActivity.log("okhttp errorrororo getCause : "+e.getCause());
                    MyActivity.log("okhttp errorrororo getClass : "+e.getClass());
                    MyActivity.log("okhttp errorrororo getMessage : "+e.getMessage());
                    MyActivity.log("okhttp errorrororo call: "+call.isExecuted());

                    MyActivity.log("image ("+index+") failed");
                    listImages.get(index).uploaded = false;
                    imagesUploadSucceed=false;
                    uploaded++;
                    checkCompletion();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    uploaded++;
                    if (response.isSuccessful()) {
                        listImages.get(index).uploaded = true;
                        MyActivity.log("image ("+index+") is successfuly uploaded");
                        String responseStr = response.body().string();
                        MyActivity.log("rrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr : "+response.toString());
                        MyActivity.log("rrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr responseStr : "+responseStr);
                    } else {
                        imagesUploadSucceed=false;
                        MyActivity.log("image ("+index+") failed");
                        MyActivity.log("okhttp !response.isSuccessful() : "+response.toString());
                        MyActivity.log("okhttp => response body : "+response.body().string());
                        MyActivity.log("okhttp => code : "+response.code());
                    }
                    checkCompletion();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    void checkCompletion(){
        MyActivity.log(uploaded+" uploaded >= listImages.size() "+listImages.size());
        if(uploaded >= listImages.size()){
            if (imagesUploadSucceed){
                store();
            }else{
                int j=0;
                for (Img img: listImages) {
                    if(!img.uploaded){
                        j++;
                    }
                }
                if (pd!=null){
                    pd.dismiss();
                }
                MyActivity.log(j+" images not uploaded");
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (pd != null) pd.dismiss();
                        AlertUtils.Action action = new AlertUtils.Action() {
                            public void doFunction(Object o) {
                                launchUploading();
                            }
                        };
                        action.message = getContext().getText(R.string.error_images_upload_failed).toString();
                        AlertUtils.alert(getContext(), action);
                    }
                });
            }

        }else{
            MyActivity.log("Mazalet Ka3ba");
        }
    }


    Action action;


    public Call okhttpForm(String type, Img img, Callback callback) throws Exception {
        if(action==null) action = new Action(getContext()) {
            @Override
            public void doFunction(String s) throws JSONException {}
        };

        String realPath = img.path;//getRealPathFromUri(getContext(), img.uri);
        File file = new File(realPath);
        final MediaType MEDIA_TYPE = realPath.endsWith("png") ?
                MediaType.parse("image/png") : MediaType.parse("image/jpeg");
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("type", type)
                .addFormDataPart("image", file.getName(), RequestBody.create(MEDIA_TYPE, file))
                .build();

        String finalUrl = Ekhdemni.photos;//+"?api_key="+Action.key+"&api_token="+ YDUserManager.get(getContext(), YDUserManager.TOKEN_KEY);
        MyActivity.log(finalUrl);
        MyActivity.log(realPath);
        Request request = AsynClient.getRequestBuilder(action)
                .url(finalUrl)
                .post(requestBody)
                .build();
        MyActivity.log("request buided");
        OkHttpClient client = new OkHttpClient();
        MyActivity.log("execute request "+ request.toString());
        Call call = client.newCall(request);
        call.enqueue(callback);
        return call;
    }
    public String getRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }



}
