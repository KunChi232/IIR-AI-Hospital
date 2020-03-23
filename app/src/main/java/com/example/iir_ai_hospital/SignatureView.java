package com.example.iir_ai_hospital;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.io.ByteArrayOutputStream;

public class SignatureView extends View {
    private Path path = new Path();
    private Paint paint = new Paint();
    private PathEffect pathEffect = new CornerPathEffect(200);
    public  SignatureView(Context context) {
        this(context, null);
    }
    public SignatureView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint.setStrokeWidth(10);
        paint.setStyle(Paint.Style.STROKE);
    }

    public void clearCanvas() {
        path.reset();
        invalidate();
        Log.d("clear","clear canvas");
    }

    public String saveImageToBase64() {
        Bitmap bitmap = Bitmap.createBitmap(this.getWidth(), this.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        this.draw(canvas);
        try {
            String encoded = new SaveImage(bitmap).execute().get();
            return encoded;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
//                path.reset();
                path.moveTo(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_MOVE :
                path.lineTo(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_CANCEL :
            case MotionEvent.ACTION_UP :
                break;
        }
        postInvalidate();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();
        paint.setColor(Color.BLACK);
        paint.setColor(Color.BLACK);
        paint.setPathEffect(pathEffect);
        canvas.drawPath(path, paint);
        canvas.restore();
    }


    private class SaveImage extends AsyncTask<Void, Void, String> {

        Bitmap bitmap;

        private SaveImage(Bitmap bitmap) {
            this.bitmap = bitmap;
        }

        @Override
        protected String doInBackground(Void ... voids) {
            String encoded = "";
            try {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG,100, byteArrayOutputStream);
                byte[] btyeArray = byteArrayOutputStream.toByteArray();

                encoded = Base64.encodeToString(btyeArray, Base64.DEFAULT);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return encoded;
        }
    }
}
