package net.ekhdemni.presentation.ui.fragments;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import android.widget.ImageView;

import org.json.JSONException;
import org.json.JSONObject;

import net.ekhdemni.R;
import net.ekhdemni.presentation.base.MyActivity;
import tn.core.presentation.base.MyFragment;
import net.ekhdemni.utils.ImageHelper;
import tn.core.util.Utilities;
import net.ekhdemni.model.oldNet.Action;
import net.ekhdemni.model.oldNet.Ekhdemni;

import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class UploadFragment extends MyFragment implements View.OnClickListener  {

    Bitmap bmp;
    Button button;
    ImageView imageView;

    @Override
    public void clean() {
        super.clean();
        if(button != null) button.setOnClickListener(null);
        button = null;
        try {
            if (noNeed && bmp != null && !bmp.isRecycled()) {
                //bmp.recycle();
                bmp = null;
            }
        }catch (RuntimeException e){
            MyActivity.log("RuntimeException"+getClass().getName()+" "+e.getMessage());
        }
    }
    boolean noNeed = false;



    @Override
    public void init() {
        super.init();
        View v = getView();
        imageView = v.findViewById(R.id.picture);
        button = v.findViewById(R.id.upload);
        imageView.setOnClickListener(this);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upload();
            }
        });
    }

    public UploadFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_upload, container, false);
    }


    void upload(){
        Action action = new Action(getContext()) {
            public void doFunction(String s) throws JSONException {
                JSONObject json = new JSONObject(s);
                int code = json.getInt("code");
                if(code==200){
                    imageView = null;
                    noNeed = true;
                    Toast.makeText(getContext(), "Done", Toast.LENGTH_SHORT).show();
                    ((MyActivity) getActivity()).setFragment(UsersShowFragment.newInstance(0));
                }else{
                    Toast.makeText(getContext(), "Failure", Toast.LENGTH_SHORT).show();
                }
            }
        };
        action.method = action.POST;
        action.url = Ekhdemni.users+"/photo";
        action.params.put("data", ImageHelper.toBase64(bmp));
        action.run();
    }


    public void onClick(View v) {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(getContext(), android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(getContext());
        }
        builder.setTitle("") //Delete entry
                .setMessage(R.string.please_choose)//Are you sure you want to delete this entry?
                .setPositiveButton(R.string.capture_photo, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue to camera
                        if(!isCameraPermissionGranted()){
                        }
                        else{
                            launchCamera();
                        }
                    }
                })
                .setNegativeButton(R.string.choose_photo, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if(!checkPermissionREAD_EXTERNAL_STORAGE(getActivity())){
                        }
                        else{
                            Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(pickPhoto , 1);
                        }
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
    //with revoked permission android.permission.CAMERA

    String filePath;
    void launchCamera(){
        final String dir =  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+ "/Folder/";
        File newdir = new File(dir);
        newdir.mkdirs();
        filePath = dir+ DateFormat.format("yyyy-MM-dd_hhmmss", new Date()).toString()+".jpg";
        File newfile = new File(filePath);
        try {
            newfile.createNewFile();
        } catch (IOException e) {}
        Uri outputFileUri = Uri.fromFile(newfile);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePicture, 0);
    }


    public  boolean isCameraPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (getContext().checkSelfPermission(Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("CAMERA","Permission is granted");
                return true;
            } else {

                Log.v("CAMERA","Permission is revoked");
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("CAMERA","Permission is granted");
            return true;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case  1:
                if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    Log.v("CAMERA","Permission: "+permissions[0]+ "was "+grantResults[0]);
                    Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);
                }
                else{
                    new AlertDialog.Builder(getContext())
                            .setMessage("التطبيق يحتاج صلاحيات إستعمال الكاميرا")
                            .setNegativeButton("إلغاء", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                            .setPositiveButton("حسنا", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    isCameraPermissionGranted();
                                }
                            }).create()
                            .show();
                }
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // do your stuff
                } else {
                    Toast.makeText(getContext(), "Denied", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    Uri image;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case 0:
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        image = data.getData();
                        setImageView(image);
                    }
                    if (image == null && filePath != null) {
                        image = Uri.fromFile(new File(filePath));
                        setImageView(image);
                    }
                    File file = new File(filePath);
                    if (!file.exists()) {
                        file.mkdir();
                    }
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    // User cancelled the image capture
                } else {
                    // Image capture failed, advise user
                }

                break;
            case 1:
                if (resultCode == Activity.RESULT_OK) {
                    setImageView(data.getData());
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    // User cancelled the image capture
                } else {
                    // Image capture failed, advise user
                }
                break;
        }
    }

    void setImageView(Uri uri){
        bmp = BitmapFactory.decodeFile(ImageHelper.resizeAndCompressImageBeforeSend(getContext(), Utilities.getPath(uri, getContext()), "cache_.jpg"));
        if (bmp==null){
            MyActivity.log("☻ Can't make bitmap from => "+uri);
        }else{
            Drawable ob = new BitmapDrawable(getResources(), bmp);
            imageView.setBackgroundDrawable(ob);
        }
    }



    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

    public boolean checkPermissionREAD_EXTERNAL_STORAGE(
            final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        (Activity) context,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    showDialog("External storage", context,
                            Manifest.permission.READ_EXTERNAL_STORAGE);

                } else {
                    ActivityCompat
                            .requestPermissions(
                                    (Activity) context,
                                    new String[] { Manifest.permission.READ_EXTERNAL_STORAGE },
                                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }

        } else {
            return true;
        }
    }
    public void showDialog(final String msg, final Context context,
                           final String permission) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle("Permission necessary");
        alertBuilder.setMessage(msg + " permission is necessary");
        alertBuilder.setPositiveButton(android.R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions((Activity) context,
                                new String[] { permission },
                                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    }
                });
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }


}
