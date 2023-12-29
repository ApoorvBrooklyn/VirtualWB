package com.example.virtualwb;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.samsung.android.sdk.penremote.AirMotionEvent;
import com.samsung.android.sdk.penremote.ButtonEvent;
import com.samsung.android.sdk.penremote.SpenEvent;
import com.samsung.android.sdk.penremote.SpenEventListener;
import com.samsung.android.sdk.penremote.SpenRemote;
import com.samsung.android.sdk.penremote.SpenUnit;
import com.samsung.android.sdk.penremote.SpenUnitManager;

import java.io.File;
import java.io.IOException;

/* loaded from: classes.dex */
public class MainActivity extends AppCompatActivity {
    int x;
    int y;
    int a;
    int b;

    public int counter = 0;
    public static final int REQUEST_AUDIO_PERMISSION_CODE = 1;
    private static String mFileName = null;
    static Boolean running = false;
    Sensor accelerometersensor;
    SensorManager accelerometersensorManager;
    private File fileToPlay;
    Sensor gyroscopesensor;
    SensorManager gyroscopesensorManager;
    private MediaPlayer mPlayer;
    private MediaRecorder mRecorder;
    int millisecs;
    int mins;
    Button reset;
    int secs;
    Button startstoprec;
    Button stopwriting;
    DrawView view;
    private SpenUnitManager mSpenUnitManager = null;
    public boolean mButtonPressed = false;
    String recordPath = null;
    Handler customhandler = new Handler();
    long starttime = 0;
    long timeinmillis = 0;
    long timeswap = 0;
    long updatetime = 0;




    Runnable updateTimerThread = new Runnable() { // from class: com.example.drawwithaccelerometer3.MainActivity.1
        @Override // java.lang.Runnable
        public void run() {
            MainActivity.this.timeinmillis = SystemClock.uptimeMillis() - MainActivity.this.starttime;
            MainActivity mainActivity = MainActivity.this;
            mainActivity.updatetime = mainActivity.timeswap + MainActivity.this.timeinmillis;
            MainActivity mainActivity2 = MainActivity.this;
            mainActivity2.secs = (int) (mainActivity2.updatetime / 1000);
            MainActivity mainActivity3 = MainActivity.this;
            mainActivity3.mins = mainActivity3.secs / 60;
            MainActivity.this.secs %= 60;
            MainActivity mainActivity4 = MainActivity.this;
            mainActivity4.millisecs = (int) (mainActivity4.updatetime % 1000);
            MainActivity.this.customhandler.postDelayed(this, 0L);
        }
    };

    private static final String TAG = "MainActivity";



    private SpenEventListener mSpenButtonEventListener = new SpenEventListener() { // from class: com.example.drawwithaccelerometer3.MainActivity.4
        @Override // com.samsung.android.sdk.penremote.SpenEventListener
        public void onEvent(SpenEvent spenEvent) {
            final AirMotionEvent motionEvent = new AirMotionEvent(spenEvent);
            ButtonEvent buttonEvent = new ButtonEvent(spenEvent);

            view.setCount(counter);


            switch (buttonEvent.getAction()) {
                case ButtonEvent.ACTION_DOWN:

                    Log.d(TAG, "Spen Button Pressed");
                    break;
                case ButtonEvent.ACTION_UP:

                    Log.d(TAG, "Spen Button Released");

                    counter++;





                    Log.d(TAG, "Counter Value: " + counter); // Log counter value
                    if(counter>0)
                    {
                        MainActivity.this.mButtonPressed = true;
                    }
                    else  {MainActivity.this.mButtonPressed = false;}
                    break;

            }

            DisplayMetrics displayMetrics = new DisplayMetrics();

            int height = displayMetrics.heightPixels;
            int width = displayMetrics.widthPixels;
            System.out.println("width    " + width + "    height     " + height);
            if (motionEvent.getDeltaX() < width / 2) {
                x = (int) ((width / 2) - (((width / 2) - motionEvent.getDeltaX()) / MainActivity.this.view.scaleFactor));
            } else if (motionEvent.getDeltaX() > width / 2) {
                x = (int) (((motionEvent.getDeltaX() - (width / 2)) / MainActivity.this.view.scaleFactor) + (width / 2));
            }
            if (motionEvent.getDeltaY() < height / 2) {
                y = (int) (((motionEvent.getDeltaY() - (height / 2)) / MainActivity.this.view.scaleFactor) + (height / 2));
            } else if (motionEvent.getDeltaY() > height / 2) {
                y = (int) ((height / 2) - (((height / 2) - motionEvent.getDeltaY()) / MainActivity.this.view.scaleFactor));
            }
            if(counter%2==0) {
               view.setXY(x, y);
             //  view.setAB(a,b);
                System.out.println("xy coordinates   [" + x + "," + y + "]");
            }



        }
    };


    final Handler handler = new Handler();
    private SpenEventListener mSpenAirMotionEventListener = new SpenEventListener() { // from class: com.example.drawwithaccelerometer3.MainActivity.5


        @Override
        public void onEvent(SpenEvent spenEvent) {
            final AirMotionEvent motionEvent = new AirMotionEvent(spenEvent);

            Log.d("air event", "air motion event listener being called");

            if (MainActivity.running.booleanValue() && (MainActivity.this.millisecs % 30 != 0)) {
                MainActivity.this.handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (MainActivity.this.mButtonPressed) {

                            MainActivity.this.view.getgyroevent(motionEvent, MainActivity.this.mins, MainActivity.this.secs, MainActivity.this.millisecs);
                        } else {


                            StringBuilder sb = new StringBuilder();
                            sb.append(MainActivity.this.view.text);

                            sb.append("\n");
                            MainActivity.this.view.text = sb.toString();
                        }
                    }
                }, 30 - (MainActivity.this.millisecs % 30));
            } else if (MainActivity.this.mButtonPressed) {

                MainActivity.this.view.getgyroevent(motionEvent, MainActivity.this.mins, MainActivity.this.secs, MainActivity.this.millisecs);
            }
        }

    };

    /* JADX INFO: Access modifiers changed from: protected */

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        initSpenRemote();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.virtualwb.R.layout.activity_main);



        final File path = new File(Environment.getExternalStorageDirectory(), "WhiteBoardPlayer/Audio");
        if (!path.exists()) {
            path.mkdirs();
        }
        if (!CheckPermissions()) {
            RequestPermissions();
        }
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        this.accelerometersensorManager = sensorManager;
        this.accelerometersensor = sensorManager.getDefaultSensor(10);
        this.view = (DrawView) findViewById(R.id.drawview);
        this.reset = (Button) findViewById(com.example.virtualwb.R.id.reset);
        this.stopwriting = (Button) findViewById(com.example.virtualwb.R.id.Stopwriting);
        this.reset.setOnClickListener(new View.OnClickListener() { // from class: com.example.drawwithaccelerometer3.-$$Lambda$MainActivity$cCtTdS4MfvBxAe0aNx6XM8wxIJk
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                counter=0;
                MainActivity.this.lambda$onCreate$0$MainActivity(view);
            }
        });
        final Context context = getApplicationContext();
        this.stopwriting.setOnClickListener(new View.OnClickListener() { // from class: com.example.drawwithaccelerometer3.-$$Lambda$MainActivity$Mf2h38eSlTMpi_SRBmJWKMRYoIA
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                counter=0;
                MainActivity.this.lambda$onCreate$1$MainActivity(view);
            }
        });
        SensorManager sensorManager2 = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        this.gyroscopesensorManager = sensorManager2;
        this.gyroscopesensor = sensorManager2.getDefaultSensor(4);
        Button button = (Button) findViewById(R.id.startstoprec);
        this.startstoprec = button;
        button.setOnClickListener(new View.OnClickListener() { // from class: com.example.drawwithaccelerometer3.MainActivity.2
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (!MainActivity.running.booleanValue()) {
                    MainActivity.this.startRecording(path);
                    MainActivity.this.startstoprec.setText("Stop Recording");
                    MainActivity.this.starttime = SystemClock.uptimeMillis();
                    MainActivity.this.customhandler.postDelayed(MainActivity.this.updateTimerThread, 0L);
                    MainActivity.running = true;
                    if (MainActivity.this.view.drawing) {
                        MainActivity.this.view.text = MainActivity.this.view.text + "W\n";
                        return;
                    }
                    return;
                }
                MainActivity.this.startstoprec.setText("Start Recording");
                MainActivity.this.timeswap += MainActivity.this.timeinmillis;
                MainActivity.this.customhandler.removeCallbacks(MainActivity.this.updateTimerThread);
                MainActivity.running = false;
                MainActivity.this.view.savedrecording(context);
                MainActivity.this.pauseRecording();
                MainActivity.this.view.stopwriting();
                MainActivity.this.mins = 0;
                MainActivity.this.secs = 0;
                MainActivity.this.millisecs = 0;
            }
        });
    }

    public /* synthetic */ void lambda$onCreate$0$MainActivity(View v) {
        this.view.clear();
    }

    public /* synthetic */ void lambda$onCreate$1$MainActivity(View v) {
        this.view.stopwriting();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onPause() {
        super.onPause();
        releaseSpenRemote();
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0) {
            boolean permissionToRecord = grantResults[0] == 0;
            boolean permissionToStore = grantResults[1] == 0;
            if (!permissionToRecord || !permissionToStore) {
                Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_LONG).show();
            }
        }
    }

    public boolean CheckPermissions() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), "android.permission.WRITE_EXTERNAL_STORAGE");
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), "android.permission.RECORD_AUDIO");
        return result == 0 && result1 == 0;
    }

    private void RequestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{"android.permission.RECORD_AUDIO", "android.permission.WRITE_EXTERNAL_STORAGE"}, 1);
    }

    public void pauseRecording() {
        if (CheckPermissions()) {
            this.mRecorder.stop();
            this.mRecorder.release();
            this.mRecorder = null;
        }
    }

    public void startRecording(File path) {
        if (CheckPermissions()) {
            long totalNumFiles = path.listFiles().length + 1;
            this.recordPath = path.getPath() + "/Audio" + totalNumFiles + ".3gp";
            System.out.println(this.recordPath);
            MediaRecorder mediaRecorder = new MediaRecorder();
            this.mRecorder = mediaRecorder;
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            this.mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            this.mRecorder.setOutputFile(this.recordPath);
            this.mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            try {
                this.mRecorder.prepare();
            } catch (IOException e) {
                Log.e("TAG", "prepare() failed");
                System.out.println("" + e);
            }
            this.mRecorder.start();
            return;
        }
        RequestPermissions();
    }
    public boolean isSpenConnected;
    private void initSpenRemote() {
        Log.d("failure", " initSPenRemote being called");
        Toast.makeText(getApplicationContext(), "S Pen Intitialising", Toast.LENGTH_SHORT).show();
        SpenRemote spenRemote = SpenRemote.getInstance();
        if (!spenRemote.isFeatureEnabled(1)) {
            Toast.makeText(getApplicationContext(), "S Pen Feature not enable", Toast.LENGTH_SHORT).show();
        } else if (!spenRemote.isFeatureEnabled(0)) {
            Toast.makeText(getApplicationContext(), "S Pen Feature not enable", Toast.LENGTH_SHORT).show();
        } else if (spenRemote.isConnected()) {
            Toast.makeText(getApplicationContext(), "S Pen already Connected", Toast.LENGTH_SHORT).show();
        } else {
            this.mSpenUnitManager = null;
            spenRemote.connect(this, new SpenRemote.ConnectionResultCallback() { // from class: com.example.drawwithaccelerometer3.MainActivity.3
                @Override // com.samsung.android.sdk.penremote.SpenRemote.ConnectionResultCallback
                public void onSuccess(SpenUnitManager spenUnitManager) {
                    isSpenConnected=true;
                    MainActivity.this.mSpenUnitManager = spenUnitManager;
                    Toast.makeText(MainActivity.this.getApplicationContext(), "S Pen Connected", Toast.LENGTH_SHORT).show();
                    MainActivity.this.initSpenEventListener();
                }

                @Override // com.samsung.android.sdk.penremote.SpenRemote.ConnectionResultCallback
                public void onFailure(int e) {
                    Log.e("failure", "Unable to connect to S Pen Remote, e = " + e);
                    Toast.makeText(MainActivity.this.getApplicationContext(), "S Pen Connection Failed", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initSpenEventListener() {
        SpenUnit buttonUnit = this.mSpenUnitManager.getUnit(0);
        if (buttonUnit == null) {
            Log.d("failure", "button unit  Null");
        } else {
            this.mSpenUnitManager.registerSpenEventListener(this.mSpenButtonEventListener, buttonUnit);
        }
        SpenUnit airMotionUnit = this.mSpenUnitManager.getUnit(1);
        if (airMotionUnit != null) {
            Log.d("failure", "airMotion unit not Null");
            this.mSpenUnitManager.registerSpenEventListener(this.mSpenAirMotionEventListener, airMotionUnit);
            return;
        }
        Log.d("failure", "airMotion unit Null");
    }

    private void releaseSpenRemote() {
        SpenRemote spenRemote = SpenRemote.getInstance();
        if (spenRemote.isConnected()) {
            spenRemote.disconnect(this);
        }
        this.mSpenUnitManager = null;
    }
}
