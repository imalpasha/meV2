package com.fly.firefly.utils.LazyList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.fly.firefly.R;
import com.koushikdutta.ion.Ion;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImageLoader {

    MemoryCache memoryCache=new MemoryCache();
    FileCache fileCache;
    private Map<ImageView, String> imageViews= Collections.synchronizedMap(new WeakHashMap<ImageView, String>());
    ExecutorService executorService;
    Context context;
    Handler handler=new Handler();//handler to display images in UI thread

    public ImageLoader(Context context){
        fileCache=new FileCache(context);
        executorService= Executors.newFixedThreadPool(5);
        this.context = context;
    }

    final int stub_id= R.drawable.loading1;
    public void DisplayImage(String url, ImageView imageView, ProgressBar loader) {
        imageViews.put(imageView, url);
        Bitmap bitmap = memoryCache.get(url);
        if (bitmap != null)
        {

        imageView.setImageBitmap(bitmap);
         if (loader !=null){
                loader.setVisibility(View.INVISIBLE);
          }

        }
        else
        {
            queuePhoto(url, imageView, loader);
            imageView.setImageResource(stub_id);
        }
    }

    private void queuePhoto(String url, ImageView imageView, ProgressBar holder)
    {
        PhotoToLoad p=new PhotoToLoad(url, imageView, holder);
        executorService.submit(new PhotosLoader(p));
    }

    private Bitmap getBitmap(String url)
    {
        File f=fileCache.getFile(url);

        //from SD cache
        Bitmap b = decodeFile(f);
        if(b!=null)
        {
            return b;
        }

        try {

            Log.d("url---", url);
            Uri uri = Uri.parse(url);
            Map<String, Object> params = new HashMap<String, Object>();
            String o = uri.getQueryParameter("o");
            String i = uri.getQueryParameter("i");
            String k = uri.getQueryParameter("k");
            params.put("o", o);
            params.put("i", i);
            params.put("k", k);
            Bitmap imageTemp = Ion.with(context)
                    .load(url)
                    .setBodyParameter("o", o)
                    .setBodyParameter("i", i)
                    .setBodyParameter("k", k)
                    .asBitmap()
                    .get();
            return imageTemp;
        } catch (Throwable ex){
            ex.printStackTrace();
            if(ex instanceof OutOfMemoryError)
                memoryCache.clear();
            return null;
        }
    }

    //decodes image and scales it to reduce memory consumption
    private Bitmap decodeFile(File f){
        try {
            Log.d("get image", "image----");
            //decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            FileInputStream stream1=new FileInputStream(f);
            BitmapFactory.decodeStream(stream1, null, o);
            stream1.close();

            //Find the correct scale value. It should be the power of 2.
            final int REQUIRED_SIZE=70;
            int width_tmp=o.outWidth, height_tmp=o.outHeight;
            int scale=1;
            while(true){
                if(width_tmp/2<REQUIRED_SIZE || height_tmp/2<REQUIRED_SIZE)
                    break;
                width_tmp/=2;
                height_tmp/=2;
                scale*=2;
            }

            //decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize=scale;
            FileInputStream stream2=new FileInputStream(f);
            Bitmap bitmap= BitmapFactory.decodeStream(stream2, null, o2);
            stream2.close();
            return bitmap;
        } catch (FileNotFoundException e) {
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //Task for the queue
    private class PhotoToLoad
    {
        public String url;
        public ImageView imageView;
        public ProgressBar holder;
        public PhotoToLoad(String u, ImageView i, ProgressBar h){
            url=u;
            imageView=i;
            holder = h;
        }
    }

    class PhotosLoader implements Runnable {
        PhotoToLoad photoToLoad;
        PhotosLoader(PhotoToLoad photoToLoad){
            this.photoToLoad=photoToLoad;
        }

        @Override
        public void run() {
            try{
                if(imageViewReused(photoToLoad))
                    return;
                Bitmap bmp=getBitmap(photoToLoad.url);
                memoryCache.put(photoToLoad.url, bmp);
                if(imageViewReused(photoToLoad))
                    return;
                BitmapDisplayer bd=new BitmapDisplayer(bmp, photoToLoad);
                handler.post(bd);
            }catch(Throwable th){
                th.printStackTrace();
            }
        }
    }

    boolean imageViewReused(PhotoToLoad photoToLoad){
        String tag=imageViews.get(photoToLoad.imageView);
        if(tag==null || !tag.equals(photoToLoad.url))
            return true;
        return false;
    }

    //Used to display bitmap in the UI thread
    class BitmapDisplayer implements Runnable
    {
        Bitmap bitmap;
        PhotoToLoad photoToLoad;
        public BitmapDisplayer(Bitmap b, PhotoToLoad p){bitmap=b;photoToLoad=p;}
        public void run()
        {
            if(imageViewReused(photoToLoad))
                return;
            if(bitmap!=null) {
                Log.d("bitmap ", "bitmap displayer");
                photoToLoad.imageView.setImageBitmap(bitmap);
                if (photoToLoad.holder != null){
                    photoToLoad.holder.setVisibility(View.INVISIBLE);
                }

            }
            else {
                photoToLoad.imageView.setImageResource(stub_id);
                if (photoToLoad.holder != null) {
                    photoToLoad.holder.setVisibility(View.INVISIBLE);
                }
            }
        }
    }
}