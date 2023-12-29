package com.example.virtualwb;

//import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import com.samsung.android.sdk.penremote.AirMotionEvent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.util.BitSet;

/* loaded from: classes.dex */
public class DrawView extends View {

    int count;
    int set;







    //int x=0;
    int y=0;
    int CpointX;
    int CpointY;
    private float Xdot;
    private float Ydot;
    boolean activate;
    int bottom;
    Canvas canvas;
    float centerX;
    float centerY;
    private ScaleGestureDetector detector;
    DecimalFormat df;
    Paint dot;
    Paint dot_hover;
    boolean draggin;
    Paint draw;
    boolean drawing;
    GestureDetector gestureDetector;
    int hlocal;
    int left;
    String[] mapping;
    public MotionEvent motionEvent;
    Bitmap myBitmap;
    Path path;
    int pointX;
    int pointY;
    private float previousTranslateX;
    private float previousTranslateY;
    int right;
    String[] rmapping;
    public float scaleFactor;
    private float startX;
    private float startY;
    String text;
    String textbitstring;
    int top;
    private float translateX;
    private float translateY;
    int wlocal;
    boolean zooming;




    // Method to set the count value
    public void setXY(int pointX,int pointY) {
        this.pointX = pointX;
        this.pointY=pointY;
    }
  /*  public void setAB(int CpointX,int CpointY) {
        this.CpointX = CpointX;
        this.CpointY=CpointY;
    }*/

    public boolean setCount(int counte) {
        this.count = counte;
        return true;
    }


    /* loaded from: classes.dex */
    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        private ScaleListener() {
        }

        @Override // android.view.ScaleGestureDetector.SimpleOnScaleGestureListener, android.view.ScaleGestureDetector.OnScaleGestureListener
        public boolean onScale(ScaleGestureDetector detector) {
            double d = DrawView.this.scaleFactor;
            DrawView.this.scaleFactor *= detector.getScaleFactor();
            DrawView.this.invalidate();
            return true;
        }
    }

    /* loaded from: classes.dex */
    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        private GestureListener() {
        }


    }


    public DrawView(Context context) {
        super(context);
        this.CpointX = 20;
        this.CpointY = 20;
        this.text = "N\n";
        this.textbitstring = "";
        this.mapping = new String[]{"00000", "00001", "00010", "00011", "00100", "00101", "00110", "00111", "01000", "01001", "01010", "01011", "01100", "01101", "01110", "01111", "10000", "10001"};
        this.rmapping = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", ",", "\n", "-", "N", "W", "S", ".", "R"};
        this.df = new DecimalFormat();
        this.left = 0;
        this.right = 0;
        this.top = 0;
        this.bottom = 0;
        this.centerX = (0 + 0) / 2;
        this.centerY = (0 + 0) / 2;
        this.startX = 0.0f;
        this.startY = 0.0f;
        this.translateX = 0.0f;
        this.translateY = 0.0f;
        this.previousTranslateX = 0.0f;
        this.previousTranslateY = 0.0f;
        this.draggin = false;
        this.zooming = false;
        this.drawing = false;
        this.activate = false;
        this.scaleFactor = 1.0f;
        init(null);
    }

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.CpointX = 20;
        this.CpointY = 20;
        this.text = "N\n";
        this.textbitstring = "";
        this.mapping = new String[]{"00000", "00001", "00010", "00011", "00100", "00101", "00110", "00111", "01000", "01001", "01010", "01011", "01100", "01101", "01110", "01111", "10000", "10001"};
        this.rmapping = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", ",", "\n", "-", "N", "W", "S", ".", "R"};
        this.df = new DecimalFormat();
        this.left = 0;
        this.right = 0;
        this.top = 0;
        this.bottom = 0;
        this.centerX = (0 + 0) / 2;
        this.centerY = (0 + 0) / 2;
        this.startX = 0.0f;
        this.startY = 0.0f;
        this.translateX = 0.0f;
        this.translateY = 0.0f;
        this.previousTranslateX = 0.0f;
        this.previousTranslateY = 0.0f;
        this.draggin = false;
        this.zooming = false;
        this.drawing = false;
        this.activate = false;
        this.scaleFactor = 1.0f;
        this.detector = new ScaleGestureDetector(getContext(), new ScaleListener());
        this.gestureDetector = new GestureDetector(context, new GestureListener());
        init(attrs);
    }

    public DrawView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.CpointX = 20;
        this.CpointY = 20;
        this.text = "N\n";
        this.textbitstring = "";
        this.mapping = new String[]{"00000", "00001", "00010", "00011", "00100", "00101", "00110", "00111", "01000", "01001", "01010", "01011", "01100", "01101", "01110", "01111", "10000", "10001"};
        this.rmapping = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", ",", "\n", "-", "N", "W", "S", ".", "R"};
        this.df = new DecimalFormat();
        this.left = 0;
        this.right = 0;
        this.top = 0;
        this.bottom = 0;
        this.centerX = (0 + 0) / 2;
        this.centerY = (0 + 0) / 2;
        this.startX = 0.0f;
        this.startY = 0.0f;
        this.translateX = 0.0f;
        this.translateY = 0.0f;
        this.previousTranslateX = 0.0f;
        this.previousTranslateY = 0.0f;
        this.draggin = false;
        this.zooming = false;
        this.drawing = false;
        this.activate = false;
        this.scaleFactor = 1.0f;
        this.detector = new ScaleGestureDetector(getContext(), new ScaleListener());
        this.gestureDetector = new GestureDetector(context, new GestureListener());
        init(attrs);
    }

    public DrawView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.CpointX = 20;
        this.CpointY = 20;
        this.text = "N\n";
        this.textbitstring = "";
        this.mapping = new String[]{"00000", "00001", "00010", "00011", "00100", "00101", "00110", "00111", "01000", "01001", "01010", "01011", "01100", "01101", "01110", "01111", "10000", "10001"};
        this.rmapping = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", ",", "\n", "-", "N", "W", "S", ".", "R"};
        this.df = new DecimalFormat();
        this.left = 0;
        this.right = 0;
        this.top = 0;
        this.bottom = 0;
        this.centerX = (0 + 0) / 2;
        this.centerY = (0 + 0) / 2;
        this.startX = 0.0f;
        this.startY = 0.0f;
        this.translateX = 0.0f;
        this.translateY = 0.0f;
        this.previousTranslateX = 0.0f;
        this.previousTranslateY = 0.0f;
        this.draggin = false;
        this.zooming = false;
        this.drawing = false;
        this.activate = false;
        this.scaleFactor = 1.0f;
        init(attrs);
    }

    private void init(AttributeSet set) {
        Paint paint = new Paint();
        this.dot_hover = paint;
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        this.dot_hover.setStyle(Paint.Style.STROKE);
        this.dot_hover.setStrokeWidth(5.0f);
        this.dot_hover.setColor(Color.GRAY);
        Paint paint2 = new Paint();
        this.dot = paint2;
        paint2.setFlags(Paint.ANTI_ALIAS_FLAG);
        this.dot.setStyle(Paint.Style.FILL);
        this.dot.setStrokeWidth(5.0f);
        this.dot.setColor(Color.GRAY);
        this.path = new Path();
        Paint paint3 = new Paint();
        this.draw = paint3;
        paint3.setColor(Color.GRAY);
        this.draw.setStyle(Paint.Style.STROKE);
        this.draw.setStrokeWidth(10.0f);
    }



    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.drawBitmap(this.myBitmap, 0.0f, 0.0f, (Paint) null);

        if (count % 2 != 0) {
            canvas.drawCircle(this.CpointX, this.CpointY, 15.0f, this.dot_hover);
            this.centerX = (float) (getWidth() / 2);
            this.centerY = (float) (getHeight() / 2);
            this.left = (int) (this.centerX - ((((float) getWidth()) / this.scaleFactor) / 2.0f));
            this.right = (int) (this.centerX + ((((float) getWidth()) / this.scaleFactor) / 2.0f));
            this.top = (int) (this.centerY - ((getHeight() / this.scaleFactor) / 2.0f));
            this.bottom = (int) (this.centerY + ((getHeight() / this.scaleFactor) / 2.0f));
            Log.d("left", String.valueOf(this.left));
            Log.d("right", String.valueOf(this.right));
            Log.d("top", String.valueOf(this.top));
            Log.d("bottom", String.valueOf(this.bottom));
            Log.d("Scale Factor", String.valueOf(this.scaleFactor));
            float f3 = this.scaleFactor;
            canvas.scale(f3, f3, this.centerX, this.centerY);

            canvas.drawPath(this.path, this.draw);
            canvas.restore();

        }
        if (count != 0 && count % 2 == 0) {
            if (this.CpointX > getWidth() / 2) {
                this.Xdot = (int) (((this.scaleFactor + 1.0f) * (getWidth() / 2)) - ((getWidth() - this.CpointX) * this.scaleFactor));
            } else if (this.CpointX < getWidth() / 2) {
                float f = this.scaleFactor;
                this.Xdot = (int) ((this.CpointX * f) + ((1.0f - f) * (getWidth() / 2)));
            }
            if (this.CpointY > getHeight() / 2) {
                this.Ydot = (int) (((this.scaleFactor + 1.0f) * (getHeight() / 2)) - ((getHeight() - this.CpointY) * this.scaleFactor));
            } else if (this.CpointY < getHeight() / 2) {
                float f2 = this.scaleFactor;
                this.Ydot = (int) ((this.CpointY * f2) + ((1.0f - f2) * (getHeight() / 2)));
            }
            // if (count % 2 == 0 && count!=0 ) {
            canvas.drawCircle(this.Xdot, this.Ydot, 15.0f, this.dot);
            Log.d("pointX and pointY fn", this.pointX + "  " + this.pointY);
            this.centerX = (float) (getWidth() / 2);
            this.centerY = (float) (getHeight() / 2);
            this.left = (int) (this.centerX - ((((float) getWidth()) / this.scaleFactor) / 2.0f));
            this.right = (int) (this.centerX + ((((float) getWidth()) / this.scaleFactor) / 2.0f));
            this.top = (int) (this.centerY - ((getHeight() / this.scaleFactor) / 2.0f));
            this.bottom = (int) (this.centerY + ((getHeight() / this.scaleFactor) / 2.0f));
            Log.d("left", String.valueOf(this.left));
            Log.d("right", String.valueOf(this.right));
            Log.d("top", String.valueOf(this.top));
            Log.d("bottom", String.valueOf(this.bottom));
            Log.d("Scale Factor", String.valueOf(this.scaleFactor));
            float f3 = this.scaleFactor;
            canvas.scale(f3, f3, this.centerX, this.centerY);
            canvas.drawPath(this.path, this.draw);
            canvas.restore();
        }
    }




    @Override // android.view.View
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.wlocal = w;
        this.hlocal = h;
        super.onSizeChanged(w, h, oldw, oldh);
        this.myBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        this.centerX = this.wlocal / 2;
        this.centerY = this.hlocal / 2;
        this.right = w;
        this.bottom = h;
        this.canvas = new Canvas(this.myBitmap);
    }

    @Override // android.view.View
    public boolean performClick() {
        return super.performClick();
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent event) {

        int actionMasked = event.getActionMasked();
        if (actionMasked == 0) {
            if (!this.zooming) {
                // this.path.moveTo(this.pointX, this.pointY);
            }
            this.drawing = true;
            if (com.example.virtualwb.MainActivity.running.booleanValue()) {
                this.text += "W\n";
            }
            Log.d("first touch", String.valueOf(this.zooming));
        } else if (actionMasked == 1) {
            Log.d("remove touch", String.valueOf(this.zooming));
            this.zooming = false;
        } else if (actionMasked==2) {
            if (!this.zooming) {
                // this.path.moveTo(this.pointX, this.pointY);
            }
            Log.d("move touch", String.valueOf(this.zooming));
            invalidate();
        } else if (actionMasked == 5) {
            this.zooming = true;
        } else if (actionMasked != 6) {
            return false;
        } else {
            Log.d("finger 2 touch removed", String.valueOf(this.zooming));
            this.zooming = false;
        }
        this.gestureDetector.onTouchEvent(event);
        if (this.draggin) {
            int action = event.getAction() & 255;
            if (action == 0) {
                this.startX = event.getX() - this.previousTranslateX;
                this.startY = event.getY() - this.previousTranslateY;
            } else if (action == 1) {
                this.previousTranslateX = this.translateX;
                this.previousTranslateY = this.translateY;
            } else if (action == 2) {
                this.previousTranslateX = this.translateX;
                this.previousTranslateY = this.translateY;
                this.translateX = event.getX() - this.startX;
                this.translateY = event.getY() - this.startY;
                Log.d("translateX", String.valueOf(this.translateX));
                Log.d("translateY", String.valueOf(this.translateY));
                double distance = Math.sqrt(Math.pow(event.getX() - (this.startX + this.previousTranslateX), 2.0d) + Math.pow(event.getY() - (this.startY + this.previousTranslateY), 2.0d));
                if (distance > 0.0d) {
                    this.draggin = true;
                }

            }
        } else if (this.zooming) {
            this.detector.onTouchEvent(event);
            if (com.example.virtualwb.MainActivity.running.booleanValue()) {
                this.df.setMaximumFractionDigits(2);
                String str = this.df.format(this.scaleFactor);
                if (Float.valueOf(str).floatValue() != 1.0d) {
                    this.text += "S" + Float.valueOf(str) + "\n";
                }
            }
        }

        if (this.zooming || this.draggin) {
            this.path.moveTo((this.left + this.right) / 2, (this.top + this.bottom) / 2);
            invalidate();
        }
        return true;
    }

    private static final String TAG = "DrawView";


    public void clear() {
        this.path = new Path();
        this.pointX = 0;
        this.pointY = 0;
        this.CpointX = 60;
        this.CpointY = 60;
        this.scaleFactor = 1.0f;
        this.draggin = false;
        this.zooming = false;
        this.drawing = false;
        if (com.example.virtualwb.MainActivity.running.booleanValue()) {
            this.text += "R\nN\n";
        }
        invalidate();
    }

    public void stopwriting() {
        this.pointX = 0;
        this.pointY = 0;
        this.CpointX = 60;
        this.CpointY = 60;
        this.drawing = false;
        if (com.example.virtualwb.MainActivity.running.booleanValue()) {
            this.text += "N\n";
        }
        invalidate();
    }

    public void scale() {
        double d = this.scaleFactor;
        Double.isNaN(d);
        this.scaleFactor = (float) (d * 0.99d);
    }
    private static final float ALPHA = 0.1f;
    public void getgyroevent(AirMotionEvent event, int mins, int secs, int millisecs) {
        if (!this.zooming) {
            if (this.pointX > this.right) {
                scale();
            }
            if (this.pointX < this.left) {
                scale();
            }
            if (this.pointY > this.bottom) {
                scale();
            }
            if (this.pointY < this.top) {
                scale();
            }
            if (com.example.virtualwb.MainActivity.running.booleanValue()) {
                this.df.setMaximumFractionDigits(2);
            }
            String str = this.df.format(this.scaleFactor);
            if (Float.valueOf(str).floatValue() != 1.0d) {
                this.text += "S" + Float.valueOf(str) + "\n";
            }
        }


        if (count % 2 != 0) {
//            this.CpointX = (int) (this.CpointX + (event.getDeltaX() * 800.0f));
//            this.CpointY = (int) (this.CpointY - (event.getDeltaY() * 800.0f));
//
//
//            System.out.println("Pointer Coordinates->   [" + this.CpointX + "," + this.CpointY + "]");
//            System.out.println("The count variable value is: " + this.count);
//
//            if (com.example.virtualwb.MainActivity.running.booleanValue()) {
//                this.text += this.CpointX + "," + this.CpointY + "\n";
//
//            }
//            invalidate();
            float deltaX = event.getDeltaX();
            float deltaY = event.getDeltaY();
            this.CpointX = (int) (ALPHA * deltaX + (1 - ALPHA) * this.CpointX);
            this.CpointY = (int) (ALPHA * deltaY + (1 - ALPHA) * this.CpointY);

            // Add smoothed data to the text
            if (com.example.virtualwb.MainActivity.running.booleanValue()) {
                this.text += this.CpointX + "," + this.CpointY + "\n";
            }

            invalidate();

            return;
        }
        if (count != 0 && count % 2 == 0) {
            float deltaX = event.getDeltaX();
            float deltaY = event.getDeltaY();
            this.pointX = (int) (ALPHA * deltaX + (1 - ALPHA) * this.pointX);
            this.pointY = (int) (ALPHA * deltaY + (1 - ALPHA) * this.pointY);



           this.pointX=this.CpointX;
           this.pointY=this.CpointY;
           this.path.moveTo(this.CpointX,this.CpointY);
           int i = this.pointX;
//           this.pointX = (int) (i + (event.getDeltaX() * 800.0f));
//        int deltaY = (int) (this.pointY - (event.getDeltaY() * 800.0f));
//        this.pointY = deltaY;
        int i2 = this.pointX;
        this.CpointX = i2;
        this.CpointY = (int) deltaY;
        this.path.lineTo(i2, deltaY);
            System.out.println("coordinates_time->   [" + this.pointX + "," + this.pointY + "," + mins + ':' + secs + ':' + millisecs + "]");
            System.out.println("coordinates_time->   [" + this.pointX + "," + this.pointY + "," + mins + ':' + secs + ':' + millisecs + "]");
            if (com.example.virtualwb.MainActivity.running.booleanValue()) {
                this.text += this.pointX + "," + this.pointY + "\n";
            }
            System.out.println("From gyro2");
             invalidate();


        }
    }

    public void savedrecording(Context context) {
        int i = 0;
        while (i <= this.text.length() - 1) {
            if (this.text.charAt(i) == ',') {
                this.textbitstring += this.mapping[10];
            } else if (this.text.charAt(i) == '\n') {
                this.textbitstring += this.mapping[11];
            } else if (this.text.charAt(i) == 'N') {
                this.textbitstring += this.mapping[13];
            } else if (this.text.charAt(i) == 'S') {
                this.textbitstring += this.mapping[15];
                i++;
            } else if (this.text.charAt(i) == 'W') {
                this.textbitstring += this.mapping[14];
            } else if (this.text.charAt(i) == '-') {
                this.textbitstring += this.mapping[12];
            } else if (this.text.charAt(i) == '.') {
                this.textbitstring += this.mapping[16];
            } else if (this.text.charAt(i) == 'R') {
                this.textbitstring += this.mapping[17];
            } else {
                this.textbitstring += this.mapping[this.text.charAt(i) - '0'];
            }
            i++;
        }
        BitSet bitSet = new BitSet(this.textbitstring.length());
        int bitcounter = 0;
        for (char c : this.textbitstring.toCharArray()) {
            Character c2 = Character.valueOf(c);
            if (c2.equals('1')) {
                bitSet.set(bitcounter);
            }
            bitcounter++;
        }
        File path = new File("/storage/emulated/0/WhiteBoardPlayer/Coordinates");
        if (!path.exists()) {
            path.mkdirs();
        }
        File pathaudio = new File("/storage/emulated/0/WhiteBoardPlayer/Audio");
        if (!pathaudio.exists()) {
            pathaudio.mkdirs();
        }
        long totalNumFiles = path.listFiles().length + 1;
        String coordinates_file_name = path.getAbsolutePath() + "/Coordinates" + totalNumFiles + ".txt";
        File coordinates_file = new File(coordinates_file_name);
        String audio_file_name = path.getAbsolutePath() + "/Audio" + totalNumFiles + ".bin";
        new File(audio_file_name);
        try {
            FileOutputStream fOut = new FileOutputStream(coordinates_file, true);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.append((CharSequence) this.text);
            myOutWriter.close();
            fOut.flush();
            fOut.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
        System.out.println("Stopped");
        this.text = "N\n";
        this.textbitstring = "";
    }
}
