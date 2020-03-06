package com.example.pixman;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class OperationsActivity extends AppCompatActivity {

    Bitmap updatedBitmap;
    ImageView imageView;
    Bitmap bitmap;
    BitmapDrawable bitmapDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operations);

        bitmap = null;
        String filename = getIntent().getStringExtra("image");
        try {
            FileInputStream fileInputStream = this.openFileInput(filename);
            bitmap = BitmapFactory.decodeStream(fileInputStream);
            imageView = findViewById(R.id.imageView);
            imageView.setImageBitmap(bitmap);
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        updatedBitmap = bitmap.copy(bitmap.getConfig(), true);
    }

    public void flipHorizontally(View view) {
        Matrix matrix = new Matrix();
        matrix.postScale(-1, 1, updatedBitmap.getWidth()/2f, updatedBitmap.getHeight()/2f);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(updatedBitmap, updatedBitmap.getWidth(), updatedBitmap.getHeight(), true);
        Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
        imageView.setImageBitmap(rotatedBitmap);
        updateBitmap(rotatedBitmap);
    }

    public void flipVertically(View view) {
        Matrix matrix = new Matrix();
        matrix.postScale(1, -1, updatedBitmap.getWidth()/2f, updatedBitmap.getHeight()/2f);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(updatedBitmap, updatedBitmap.getWidth(), updatedBitmap.getHeight(), true);
        Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
        imageView.setImageBitmap(rotatedBitmap);
        updateBitmap(rotatedBitmap);
    }

    public void changeOpacity(View view) {
        Bitmap opaqueBitmap = Bitmap.createBitmap(updatedBitmap.getWidth(), updatedBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(opaqueBitmap);
        Paint alphaPaint = new Paint();
        alphaPaint.setAlpha(50);
        canvas.drawBitmap(updatedBitmap, 0, 0, alphaPaint);
        imageView.setImageBitmap(opaqueBitmap);
        updateBitmap(opaqueBitmap);
    }

    public static int convertToPixels(Context context, int nDP)
    {
        final float conversionScale = context.getResources().getDisplayMetrics().density;

        return (int) ((nDP * conversionScale) + 0.5f) ;

    }

    public Bitmap addText(Context context, String text) {
        Resources resources = context.getResources();
        float scale = resources.getDisplayMetrics().density;
        Bitmap bitmap = Bitmap.createBitmap(updatedBitmap.getWidth(), updatedBitmap.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.GREEN);
        paint.setTextSize(12);
        paint.setShadowLayer(1f, 0f, 1f, Color.DKGRAY);
//        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
  //      canvas.drawBitmap(bitmap, 0, 0, paint);
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        int x = (updatedBitmap.getWidth() - bounds.width())/2;
        int y = (updatedBitmap.getHeight() + bounds.height())/2;
        canvas.drawText(text, x*scale, y*scale, paint);
        return bitmap;
    }

    public void drawTextToBitmap() {
        Bitmap newBitmap = addText(getApplicationContext(), "GreedyGame");
        imageView.setImageBitmap(newBitmap);
        updateBitmap(newBitmap);
    }

    public void updateBitmap(Bitmap bitmap) {
        updatedBitmap = bitmap.copy(bitmap.getConfig(), true);
    }

    public void saveImage(View view) {
        String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString()+ "/Camera/Your_Directory_Name";
        File myDir = new File(root);
        myDir.mkdirs();
        String fname = "Image-" + System.currentTimeMillis() + ".png";
        File file = new File(myDir, fname);
        System.out.println(file.getAbsolutePath());
        if (file.exists()) file.delete();
        Log.i("LOAD", root + fname);
        try {
            FileOutputStream out = new FileOutputStream(file);
            updatedBitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        MediaScannerConnection.scanFile(getApplicationContext(), new String[]{file.getPath()}, new String[]{"image/jpeg"}, null);
        Toast.makeText(this, "Image has been saved to gallery!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}