package com.example.pixman;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class OperationsActivity extends AppCompatActivity {

    Bitmap updatedBitmap;
    ImageView imageView;
    Bitmap bitmap;

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
        matrix.postScale(-1, 1, updatedBitmap.getWidth() / 2f, updatedBitmap.getHeight() / 2f);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(updatedBitmap, updatedBitmap.getWidth(), updatedBitmap.getHeight(), true);
        Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
        imageView.setImageBitmap(rotatedBitmap);
        updateBitmap(rotatedBitmap);
    }

    public void flipVertically(View view) {
        Matrix matrix = new Matrix();
        matrix.postScale(1, -1, updatedBitmap.getWidth() / 2f, updatedBitmap.getHeight() / 2f);
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


    public void drawTextOnBitmap(View view) {
        String text = "GreedyGame";
        Bitmap bitmap = Bitmap.createBitmap(updatedBitmap.getWidth(), updatedBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(20);
        paint.setColor(Color.BLACK);
        Paint.FontMetrics fm = new Paint.FontMetrics();
        paint.getFontMetrics(fm);

        // draw text to the Canvas center
        Rect boundsText = new Rect();
        paint.getTextBounds(text, 0, text.length(), boundsText);

        int x = (bitmap.getWidth() - boundsText.width()) / 2;
        int y = (bitmap.getHeight() + boundsText.height()) / 2;

        canvas.drawRect(bitmap.getWidth() / 2 - boundsText.width() / 2,
                bitmap.getHeight() / 2 - boundsText.height() / 2,
                bitmap.getWidth() / 2 + boundsText.width() / 2,
                bitmap.getHeight() / 2 + boundsText.height() / 2,
                paint);

        paint.setColor(Color.GREEN);
        canvas.drawText(text, x, y, paint);

        Bitmap bmOverlay = Bitmap.createBitmap(updatedBitmap.getWidth(), updatedBitmap.getHeight(), updatedBitmap.getConfig());
        Canvas canvas1 = new Canvas(bmOverlay);
        canvas1.drawBitmap(updatedBitmap, new Matrix(), null);
        canvas1.drawBitmap(bitmap, (updatedBitmap.getWidth() - bitmap.getWidth()) / 2, (updatedBitmap.getHeight() - bitmap.getHeight()) / 2, null);

        imageView.setImageBitmap(bmOverlay);
        updateBitmap(bmOverlay);


    }

    public void drawLogoAndTextOnBitmap(View view) {
        String text = "GreedyGame";
        Bitmap bitmap = Bitmap.createBitmap(updatedBitmap.getWidth(), updatedBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        //add logo
        Bitmap logo = BitmapFactory.decodeResource(getResources(), R.drawable.greedygame);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(20);
        paint.setColor(Color.BLACK);
        Paint.FontMetrics fm = new Paint.FontMetrics();
        paint.getFontMetrics(fm);

        // draw text to the Canvas center
        Rect boundsText = new Rect();
        paint.getTextBounds(text, 0, text.length(), boundsText);

        int x = (bitmap.getWidth() - boundsText.width());
        int y = (bitmap.getHeight() + boundsText.height()) / 2;
        int y1 = bitmap.getHeight()/2 - logo.getHeight();

        canvas.drawRect(bitmap.getWidth() - boundsText.width() ,
                bitmap.getHeight() / 2 - boundsText.height() / 2,
                bitmap.getWidth() ,
                bitmap.getHeight() / 2 + boundsText.height() / 2,
                paint);

        paint.setColor(Color.GREEN);
        canvas.drawText(text, x, y, paint);
        canvas.drawBitmap(logo, 0, y1, new Paint());

        Bitmap bmOverlay = Bitmap.createBitmap(updatedBitmap.getWidth(), updatedBitmap.getHeight(), updatedBitmap.getConfig());
        Canvas canvas1 = new Canvas(bmOverlay);
        canvas1.drawBitmap(updatedBitmap, new Matrix(), null);
        canvas1.drawBitmap(bitmap, (updatedBitmap.getWidth() - bitmap.getWidth()) / 2, (updatedBitmap.getHeight() - bitmap.getHeight()) / 2, null);
        imageView.setImageBitmap(bmOverlay);
        updateBitmap(bmOverlay);
    }


    public void updateBitmap(Bitmap bitmap) {
        updatedBitmap = bitmap.copy(bitmap.getConfig(), true);
    }

    public void saveImage(View view) {
        String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString() + "/Camera/Your_Directory_Name";
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