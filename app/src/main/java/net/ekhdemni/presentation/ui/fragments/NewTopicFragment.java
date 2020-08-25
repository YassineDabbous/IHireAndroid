package net.ekhdemni.presentation.ui.fragments;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
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
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import android.widget.ImageView;

import net.ekhdemni.R;
import net.ekhdemni.presentation.base.MyActivity;
import tn.core.presentation.base.MyFragment;
import net.ekhdemni.model.models.Forum;
import net.ekhdemni.utils.ImageHelper;
import tn.core.util.Utilities;
import net.ekhdemni.model.oldNet.Action;
import net.ekhdemni.model.oldNet.Ekhdemni;

/**
 *
 * A simple {@link Fragment} subclass.
 */
public class NewTopicFragment extends MyFragment implements View.OnClickListener{

    public Forum forum;
    EditText title, description;
    Button send;
    ImageView imageView;
    Bitmap bmp;

    @Override
    public void clean() {
        super.clean();
        title = description = null;
        if(send!=null) send.setOnClickListener(null);
        if(imageView!=null) imageView.setOnClickListener(null);
        send = null;
        try {
            if (noNeed && bmp != null && !bmp.isRecycled()) {
                //bmp.recycle(); it make an error (trying to use a recycled bitmap)
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
        View view = getView();
        if(title==null && view!=null){
            title = view.findViewById(R.id.title);
            description = view.findViewById(R.id.description);
            send = view.findViewById(R.id.send);
            imageView = view.findViewById(R.id.picture);

            imageView.setOnClickListener(this);
            send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String t = title.getText().toString();
                    String d = description.getText().toString();
                    if(!t.isEmpty() && !d.isEmpty()){
                        pushTopic(t,d);
                    }
                }
            });
        }
    }
    public void pushTopic(String titleTxt, String descriptionTxt){
        MyActivity.log("pushTopic:"+titleTxt);
        Action action = new Action(getContext()) {
            public void doFunction(String s) {
                MyActivity.log("pushTopic: "+s);
                imageView = null;
                noNeed = true;
                TopicsFragment f = new TopicsFragment();
                f.forum = forum;
                ((MyActivity) getActivity()).setFragment(f);
            }
        };
        action.method = action.POST;
        action.url = Ekhdemni.posts;
        action.params.put("forum_id", forum.getId());
        action.params.put("title", titleTxt);
        action.params.put("description",descriptionTxt);
        if(bmp != null)
            action.params.put("data", ImageHelper.toBase64(bmp));
        else MyActivity.log("YOu Didn't upload a Picture :/");
        action.run();
    }


    public NewTopicFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_topic, container, false);
    }



    public void onClick(View v) {
        //Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        //startActivityForResult(pickPhoto , 1);
        showFileChooser();
    }
    /*
    * @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            setImageView(data.getData());
        }
    }*/
    void setImageView(Uri uri){
        if(uri!=null){
            String path = Utilities.getPath(uri, getContext());
            MyActivity.log("el paaaaaaaaaaaaaaaaath => "+path);
            imageView.setImageURI(Uri.parse(path));
            bmp = BitmapFactory.decodeFile(ImageHelper.resizeAndCompressImageBeforeSend(getContext(), path, "cache_post.jpg"));
            if (bmp==null){
                MyActivity.log("☻ Can't make bitmap from => "+path);
            }else{
                Drawable ob = new BitmapDrawable(getResources(), bmp);
                imageView.setBackgroundDrawable(ob);
            }

        }else
            MyActivity.log("☻☻☻ NEED URI");
        //imageView.setImageBitmap(bmp);
    }


















    private void showExplanation(String title, String message, final String permission, final int permissionRequestCode) {
        MyActivity.log("Buiding AlertDialog");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                    }
                });
        MyActivity.log("show AlertDialog");
        builder.create().show();
    }



    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (getActivity().checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                MyActivity.log("Permission is granted");
                return true;
            } else {
                MyActivity.log("Permission is revoked");
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    showExplanation("Permission Needed", "Rationale", Manifest.permission.WRITE_EXTERNAL_STORAGE, 1);
                } else {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                }
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            MyActivity.log("Permission is granted");
            return true;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            MyActivity.log("Permission: "+permissions[0]+ "was "+grantResults[0]);
            //resume tasks needing this permission
            showFileChooser();
        }
    }


    private static final int PICK_FILE_REQUEST = 1;
    private void showFileChooser() {
        if(isStoragePermissionGranted()){
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            //starts new activity to select file and return data
            startActivityForResult(Intent.createChooser(intent,"Choose File to Upload.."),PICK_FILE_REQUEST);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == PICK_FILE_REQUEST){
                if (Build.VERSION.SDK_INT >= 16) {
                    ClipData clipData = data.getClipData();
                    if (clipData != null) {
                        MyActivity.log("FROM Gallery data.getClipData => "+data.getClipData().getItemCount());
                        for (int i = 0; i < clipData.getItemCount(); i++) {
                            setImageView(clipData.getItemAt(i).getUri());
                        }
                    }else{
                        MyActivity.log("FROM Gallery 1 data.getData() => "+data.getData());
                        setImageView(data.getData());
                    }
                }else{
                    MyActivity.log("FROM Gallery 2 data.getData() => "+data.getData());
                    setImageView(data.getData());
                }

            }
        }
    }

}