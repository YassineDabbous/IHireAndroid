package net.ekhdemni.utils;

/**
 * Created by X on 1/19/2018.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Base64;


import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import net.ekhdemni.R;
import net.ekhdemni.presentation.base.MyActivity;
import tn.core.model.net.net.NetworkUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;

public class ImageHelper {

    public static void loadFit(final ImageView img, String urlStr){
        if (urlStr==null || urlStr.isEmpty()){
            MyActivity.log("Path must not be empty.");
            return;
        }
        /*img.post(new Runnable() {
            @Override public void run() {
                Picasso.get()
                        .load(urlStr)
                        .resize(0, img.getHeight())
                        .onlyScaleDown()
                        .into(img);
            }
        });*/
        Transformation transformation = new Transformation() {

            @Override
            public Bitmap transform(Bitmap source) {
                int targetWidth = img.getWidth();

                double aspectRatio = (double) source.getHeight() / (double) source.getWidth();
                int targetHeight = (int) (targetWidth * aspectRatio);
                if (targetWidth>0 && targetHeight>0){
                    Bitmap result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false);
                    if (result != source) {
                        // Same bitmap is returned if sizes are the same
                        source.recycle();
                    }
                    return result;
                }else {
                    return source;
                }
            }

            @Override
            public String key() {
                return "transformation" + " desiredWidth";
            }
        };

        Picasso.get()
                .load(NetworkUtils.correctUrl(urlStr))
                .placeholder(R.drawable.logo_circle)
                .error(android.R.drawable.stat_notify_error)
                .transform(transformation)
                .into(img, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
    }
    public static void load(final ImageView img, String urlStr, int width, int height){
        if (urlStr==null || urlStr.isEmpty()){
            MyActivity.log("Path must not be empty.");
            return;
        }
        urlStr = NetworkUtils.correctUrl(urlStr);
        MyActivity.log("image path --> "+urlStr);
        Picasso.get().load(urlStr)
                .fit()
                .centerCrop()
                .placeholder(R.drawable.logo_circle).into(img);
    }
    public static void load(ImageView img, String url){
        load(img, url, 0,0);
    }



    public static String resizeAndCompressImageBeforeSend(Context context, String filePath, String fileName){
        final int MAX_IMAGE_SIZE = 200 * 1024; // max final file size in kilobytes

        // First decode with inJustDecodeBounds=true to check dimensions of image
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath,options);

        // Calculate inSampleSize(First we are going to resize the image to 800x800 image, in order to not have a big but very low quality image.
        //resizing the image will already reduce the file size, but after resizing we will check the file size and start to compress image
        options.inSampleSize = calculateInSampleSize(options, 800, 800);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        options.inPreferredConfig= Bitmap.Config.ARGB_8888;

        Bitmap bmpPic = BitmapFactory.decodeFile(filePath,options);


        int compressQuality = 100; // quality decreasing by 5 every loop.
        int streamLength;
        do{
            ByteArrayOutputStream bmpStream = new ByteArrayOutputStream();
            MyActivity.log("compressBitmap Quality: " + compressQuality);
            bmpPic.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream);
            byte[] bmpPicByteArray = bmpStream.toByteArray();
            streamLength = bmpPicByteArray.length;
            compressQuality -= 5;
            MyActivity.log("compressBitmap Size: " + streamLength/1024+" kb");
        }while (streamLength >= MAX_IMAGE_SIZE);

        try {
            //save the resized and compressed file to disk cache
            MyActivity.log("compressBitmap cacheDir: "+context.getCacheDir());
            FileOutputStream bmpFile = new FileOutputStream(context.getCacheDir()+fileName);
            bmpPic.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpFile);
            bmpFile.flush();
            bmpFile.close();
        } catch (Exception e) {
            MyActivity.log("compressBitmap Error on saving file");
        }
        //return the path of resized and compressed file
        return  context.getCacheDir()+fileName;
    }



    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        String debugTag = "MemoryInformation";
        // Image nin islenmeden onceki genislik ve yuksekligi
        final int height = options.outHeight;
        final int width = options.outWidth;
        MyActivity.log(debugTag+" image height: "+height+ "---image width: "+ width);
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        MyActivity.log(debugTag+"inSampleSize: "+inSampleSize);
        return inSampleSize;
    }


    public static String toBase64(Bitmap bitmap) {
        bitmap = compressImage(bitmap);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
    public static Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//Compression quality, here 100 means no compression, the storage of compressed data to baos
        int percent = 90;
        while (baos.toByteArray().length / 1024 > 400) {  //Loop if compressed picture is greater than 400kb, than to compression
            baos.reset();//Reset baos is empty baos
            image.compress(Bitmap.CompressFormat.JPEG, percent, baos);//The compression options%, storing the compressed data to the baos
            percent -= 10;//Every time reduced by 10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//The storage of compressed data in the baos to ByteArrayInputStream
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Config.RGB_565;
        options.inSampleSize = 2;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, options);//The ByteArrayInputStream data generation
        return bitmap;
    }



    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }
}