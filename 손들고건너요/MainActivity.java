package kr.ac.hallym.skeleton;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.vrtoolkit.cardboard.CardboardActivity;
import com.google.vrtoolkit.cardboard.CardboardView;
import com.google.vrtoolkit.cardboard.Eye;
import com.google.vrtoolkit.cardboard.HeadTransform;
import com.google.vrtoolkit.cardboard.Viewport;
import com.google.vrtoolkit.cardboard.sensors.SensorConnection;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Random;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL;

public class MainActivity extends CardboardActivity implements CardboardView.StereoRenderer, SensorEventListener {

    private tri tris;
    private Ground ground, load, loads;
    private Tree treeobj,home;
    private TreeHead head,homeHead;
    private Wall wall;
    private Door door, gameClear;
    private Car car;
    private CarGrass grass;
    private ball balls;
    private OverlayView overlayView;

    private float[] carmera;
    private float[] view;
    private float[] modelViewPro;
    private float carmeraMove;
    private float moving = 0.0f;
    private float move = -50.0f, move2 = 50.0f;

    private Random random;

    private CardboardView cardboardView;

    private long lastTime;
    private float speed, lastX, lastY, lastZ, x, y, z;
    private static final int SHAKE_THRESHOLD = 1000;


    private static final int DATA_X = SensorManager.DATA_X;

    private int count=0, lastCount;
    private int[] mapInfo ;
    private float[] carRand;
    private int carIndex = 0, loadcount = 0;
    private int j = 0,rm = 0;
    private float carcount = 0.0f;
    private int mapLength = 50;

    private SensorManager sensorManager;
    private Sensor accelerormeterSensor;

    private SoundPool soundPool;
    private int soundID;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onStop() {
        super.onStop();
        if (sensorManager != null)
            sensorManager.unregisterListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (accelerormeterSensor != null)
            sensorManager.registerListener(this, accelerormeterSensor,
                    SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cardboardView = (CardboardView) findViewById(R.id.cardbord_view);
        cardboardView.setRenderer(this);
        setCardboardView(cardboardView);

        overlayView = (OverlayView) findViewById(R.id.overlay);


        carmera = new float[16];
        view = new float[16];
        modelViewPro = new float[16];

        mapInfo = new int[mapLength];

        carmeraMove = -0.0f;

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerormeterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        random = new Random();
        int serch = 0;
        for(int i = 0; i < mapInfo.length; i++){
            serch = random.nextInt(3);

            if( i != 0&&serch == mapInfo[i-1]) {
                i = i-1;
                continue;
            }
            mapInfo[i] = serch;
            Log.i("mapInfo",mapInfo[i]+"");
            if(mapInfo[i] == 2) carIndex++;
        }
        carRand = new float[carIndex];
        for(int i = 0; i < carIndex; i++)   carRand[i] = (random.nextInt(5)+1)/2;

        soundPool = new SoundPool.Builder().build();
        try{
            AssetFileDescriptor descriptor = getAssets().openFd("braek.mp3");
            soundID = soundPool.load(descriptor,1);
        }catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),"Error: "+ e.toString(),Toast.LENGTH_SHORT).show();
        }

        mediaPlayer = new MediaPlayer();
        try{
            AssetFileDescriptor descriptor = getAssets().openFd("background.mp3");
            mediaPlayer.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
            mediaPlayer.prepare();;
            mediaPlayer.setLooping(true);
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"Error: "+ e.toString(),Toast.LENGTH_SHORT).show();
            mediaPlayer = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mediaPlayer != null) mediaPlayer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mediaPlayer != null){
            mediaPlayer.pause();
            if(isFinishing()){
                mediaPlayer.stop();
                mediaPlayer.release();
            }
        }
    }

    public void playSound(){
        try{
            soundPool.play(soundID, 1.0f, 1.0f, 0, 0, 1.0f);
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"Error: "+ e.toString(),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onNewFrame(HeadTransform headTransform) {

    }

    @Override
    public void onDrawEye(Eye eye) {
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glClearColor(0.03f,0.59f,1.0f,1.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        float[] model, trans, rot, scale;
        model = new float[16];
        Matrix.setIdentityM(model,0);
        Matrix.multiplyMM(view,0,eye.getEyeView(),0,carmera,0);

        float[] per = eye.getPerspective(0.1f,50.0f);

        //Home(per, view, model,-15.0f);


        Map(view, per);

    }

    private void Home(float[] per, float[] view , float[] model, float z){
        float[] trans, scale, rot;
        model = new float[16];
        Matrix.setIdentityM(model,0);
        scale = Scale(14.0f, 8.0f, 6.0f);
        trans = Translate(0.0f,18.9f,z);
        Matrix.multiplyMM(model,0,trans,0,scale,0);
        homeHead.draw(per,view,model);

        Matrix.setIdentityM(model,0);
        trans = Translate(0.0f,5.0f,z);
        scale = Scale(13.0f, 20.0f, 10.0f);
        Matrix.multiplyMM(model,0,trans,0,scale,0);
        home.draw(per,view,model);

        Matrix.setIdentityM(model,0);
        model = Rotate(90,1.0f,0.0f,0.0f);
        trans = Translate(0.0f, 0.0f, z+5.1f);
        scale = Scale(1.0f,1.0f,2.0f);
        Matrix.multiplyMM(model,0 , model, 0 , scale, 0);
        Matrix.multiplyMM(model, 0, trans, 0 , model, 0);
        door.draw(per,view,model);

        Matrix.setIdentityM(model,0);
        model = Rotate(90,1.0f,0.0f,0.0f);
        trans = Translate(0.0f, -1.0f, z);
        Matrix.multiplyMM(model, 0, trans, 0 , model, 0);
        rot = Rotate(180,0.0f,0.0f,1.0f);
        Matrix.multiplyMM(model, 0 ,model, 0, rot,0);
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        gameClear.draw(per,view,model);
    }
    private void Car(float[] per, float[] view, float[] model){
        car.draw(per,view,model);
        grass.draw(per,view,model);
        balls.draw(per,view,model);
    }

    private void Map(float[] view, float[] per){
        float[] model, trans, rot, scale;
        model = new float[16];

        Matrix.setIdentityM(model,0);

        if(count <= 6) {
            scale = Scale(500.0f, 0.0f, 500.0f);
            rot = Rotate(90.0f, 1.0f, 0.0f, 0.0f);
            Matrix.multiplyMM(model, 0, rot, 0, scale, 0);
            trans = Translate(0.0f, 0.0f, 2.0f);

            Matrix.multiplyMM(model, 0, trans, 0, model, 0);
            wall.draw(per, view, model);

            Matrix.setIdentityM(model, 0);
            scale = Scale(50.0f, 0.0f, -2.0f);
            trans = Translate(0.0f, -2.0f, -1.0f);
            Matrix.multiplyMM(model, 0, trans, 0, scale, 0);
            ground.draw(per, view, model);

            TreeObj(per, view, 10.0f, -0.5f);
            TreeObj(per, view, -10.0f, -0.5f);
        }



        for(int i = 0 + rm; i < 6+j; i++){
            RandLoad(view, per, model,mapInfo[i],7.0f+(float) i*8.0f);
            if(i == mapLength-1){
                RandLoad(view, per, model,0,7.0f+(float) (i+1)*8.0f);
                RandLoad(view, per, model,0,7.0f+(float) (i+2)*8.0f);
                Home(per,view,model,-(7.0f+(float) (i+1)*8.0f+7.0f));
            }
        }

        if((count+1) /2 * 50 >= 5100 && carcount < 8.0f){
            carcount += 0.1f;

            carmeraMove -= 0.1f;
        }
        if(lastCount != count){

            if(count/4 >= 4 && count %4 == 0){
                j++;
                rm++;
            }
            //Log.i("length","rm =" + rm + " j ="+j);
        }

        if(j >= mapInfo.length-6) j = mapInfo.length-6;

        lastCount = count;
    }

    private void RandLoad(float[] view, float[] per, float[] model, int num, float posZ){
        float[] trans, rot, scale;
        model = new float[16];
        scale = Scale(50.0f, 0.0f, -4.0f);
        Matrix.setIdentityM(model,0);

        if(num == 0){
            trans = Translate(0.0f, -2.0f, -posZ);
            Matrix.multiplyMM(model, 0, trans, 0, scale, 0);
            ground.draw(per, view, model);

            for(float i = 10.0f; i <= 60.0f; i += 10.0f){
                TreeObj(per, view, i + 5.0f, -(posZ));
                TreeObj(per, view, -(i + 5.0f), -(posZ));
                TreeObj(per, view, i, -(posZ-2.0f));
                TreeObj(per, view, -i, -(posZ-2.0f));
            }
        }else if(num == 1){
            trans = Translate(0.0f, -2.0f, -posZ);
            Matrix.multiplyMM(model, 0, trans, 0, scale, 0);
            loads.draw(per, view, model);
        }else{

            trans = Translate(0.0f, -2.0f, -posZ);
            Matrix.multiplyMM(model, 0, trans, 0, scale, 0);
            load.draw(per, view, model);

            if (move >= 50.0)    move = -50.0f;
            if (move2 <= -50.0)   move2 = 50.0f;

            move += 0.2 ;
            move2 -= 0.2 ;

            float carPos = move+(float)carRand[loadcount]*3.0f;
            float carPos2 = move+(float)carRand[loadcount]*3.4f;

            scale = Scale(1.0f, 1.0f, 1.0f);
            trans = Translate(carPos, -1.75f, -posZ+2.0f);
            Matrix.multiplyMM(model, 0, trans, 0, scale, 0);
            Car(per, view, model);

            trans = Translate(move2+(float)carRand[loadcount]*5.4f, -1.75f, -posZ-2.0f);
            Matrix.multiplyMM(model, 0, trans, 0, scale, 0);
            Car(per, view, model);

            if(carPos >= -2.5f && carPos <= 2.5f ){
                if(carmeraMove>= -1.3f- posZ+2.0f && carmeraMove <= 1.3f - posZ+2.0f){
                    playSound();
                    carmeraMove = 0.0f;
                    count = 0;
                    rm = 0;
                    j = 0;
                }
            }

            if(carPos2 >= -2.5f && carPos2 <= 2.5f ){
                if(carmeraMove>= -1.3f- posZ-2.0f && carmeraMove <= 1.3f - posZ-2.0f){
                    playSound();
                    carmeraMove = 0.0f;
                    count = 0;
                    rm = 0;
                    j = 0;
                }
            }
            //Log.i("carmera",moving+"");
        }
    }
    private void TreeObj(float[] per, float[] view, float x, float z){
        float[] model, scale, trans;
        model = new float[16];

        Matrix.setIdentityM(model,0);

        trans = Translate(x, 0.0f, z);
        Matrix.multiplyMM(model, 0,model,0,trans,0);

        scale = Scale(1.5f, 7.0f, 1.5f);
        Matrix.multiplyMM(model, 0, model, 0, scale, 0);

        treeobj.draw(per,view,model);

        Matrix.setIdentityM(model,0);
        trans = Translate(x, 6.0f, z);
        Matrix.multiplyMM(model, 0,model,0,trans,0);
        scale = Scale(4.5f, 4.0f, 4.5f);
        Matrix.multiplyMM(model, 0, model, 0, scale, 0);

        head.draw(per, view, model);
    }

    @Override
    public void onFinishFrame(Viewport viewport) {
        Matrix.setLookAtM(carmera,0,
                0.0f,-1.0f,carmeraMove + 0.01f,
                0.0f,-1.0f, carmeraMove,
                0.0f,1.0f,0.0f);

    }

    @Override
    public void onSurfaceChanged(int i, int i1) {

    }

    @Override
    public void onSurfaceCreated(EGLConfig eglConfig) {

        wall = new Wall(this, loadBitmap("wall.png"));
        ground = new Ground(this, loadBitmap("GroundNomal.jpg"));
        load = new Ground(this, loadBitmap("GroundLoad.jpg"));
        loads = new Ground(this, loadBitmap("BackgroundLoads.jpeg"));
        treeobj = new Tree(this, loadBitmap("Tree.jpg"));
        head = new TreeHead(this, loadBitmap("GroundNomal.jpg"));
        car = new Car(this, loadBitmap("red.jpg"));
        grass = new CarGrass(this, loadBitmap("grass.jpg"));
        balls = new ball(this, loadBitmap("gray.jpeg"));
        home = new Tree(this, loadBitmap("homebody.jpg"));
        homeHead = new TreeHead(this, loadBitmap("homehead.JPG"));
        door = new Door (this, loadBitmap("door.jpeg"));
        gameClear = new Door (this, loadBitmap("gameClear.png"));
    }

    @Override
    public void onRendererShutdown() {

    }

    private float[] Rotate(float a ,float x ,float y ,float z){
        float[] rotate = new float[16];
        Matrix.setIdentityM(rotate,0);
        Matrix.rotateM(rotate,0, a, x, y, z);
        return rotate;
    }

    private float[] Scale(float x, float y, float z){
        float[] scale = new float[16];
        Matrix.setIdentityM(scale, 0);
        Matrix.scaleM(scale, 0, x, y, z);
        return scale;
    }

    private float[] Translate(float x, float y, float z){
        float[] translate = new float[16];
        Matrix.setIdentityM(translate, 0);
        Matrix.translateM(translate,0, x, y, z);
        return translate;
    }

    public int loadShader(int type, int resID){
        // type = vertex or fragment Shader type
        //resID = Shader 리소스 ID가 반환된 int
        //return = Shader id 반환

        String code = readRawTextFile(resID);
        int shader = GLES20.glCreateShader(type);

        GLES20.glShaderSource(shader, code);
        GLES20.glCompileShader(shader);

        return shader;
    }

    private String readRawTextFile(int resID){
        InputStream inputStream = getResources().openRawResource(resID);
        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null){
                sb.append(line).append("\n");
            }
            reader.close();

            return sb.toString();
        }catch (IOException e){

        }
        return null;
    }

    public Bitmap loadBitmap(String filname){
        Bitmap bitmap = null;
        try{
            AssetManager manager = getAssets();
            BufferedInputStream inputStream = new BufferedInputStream(manager.open(filname));
            bitmap = BitmapFactory.decodeStream(inputStream);
        }catch (Exception e){

        }
        return bitmap;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
        {
            long currentTime = System.currentTimeMillis();
            long gabOfTime = (currentTime - lastTime);

            //센서 동작이 이전/이후 시간의 차이가 0.1초 이내이면
            if (gabOfTime > 150 && (count+1) /2 * 50 < 5100)
            {
                lastTime = currentTime;

                x = event.values[SensorManager.DATA_X];


                speed = Math.abs( x - lastX) / gabOfTime * 10000;


                if (speed > SHAKE_THRESHOLD)
                {
                    // 이벤트 발생!!
                    count++;

                    if(count %2 == 0) {
                        carmeraMove -= 4.0f;
                        //moving -= 4.5f;

                    }

                    //Log.i("test", count+"");

                }
                overlayView.addContext((count+1) /2 * 50+"");
                lastX = event.values[DATA_X];
                try{
                    Thread.sleep(100);
                }catch (Exception e){
                }
            }

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {


    }
}
