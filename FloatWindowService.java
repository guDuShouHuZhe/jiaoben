package com.mycompany.application3;
import android.animation.ObjectAnimator;
import android.annotation.NonNull;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.hardware.display.DisplayManager;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Vibrator;
import android.provider.Settings;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.text.style.BackgroundColorSpan;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.R;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;


public class FloatWindowService extends Service {
	int aass;
    private int touchx,touchy,touchrawx,touchrawy;
    //touchx定义大悬浮窗相对触摸坐标
    //touchrawx大悬浮触摸窗绝对坐标
    //public static Intent data;
    // public static Intent data;
    public MediaProjection mMediaProjection;
    // public static MediaProjectionManager mMediaProjectionManager;
    public static LinearLayout floatlayout;
    private LinearLayout floatlayout1;
    public static myvp setc;
    private  Button setcbt5;
    public   ViewPager2 setcll;
    private int w,h,dpi;//定义屏幕宽度高度
    public WindowManager.LayoutParams params,params1;  public static WindowManager.LayoutParams params2;//定义windowmanger.layoutparams
    public static WindowManager manager;//定义悬浮窗管理对象
    private mycountdowntimer time;//定义定时器实现自动隐藏窗口
    private CountDownTimer time2;//脚本定时
    private  TextView xfctv5,xfctv2;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;//定义画笔
    private int  scree_orientation,scree_orientation1;
    private DisplayManager displayManager;
    private DisplayManager.DisplayListener mDisplayListener;
    private DisplayMetrics outMetrics;
    private ColorStateList c;
    private ObjectAnimator oa1;
    private Vibrator vibrator;
    //private boolean start_jb=false;
    public static myhand hand;
    private int my_ds_time=0;
    private RecyclerView.Adapter adapter;
    public static boolean setc3switch=false;
    private String infor="",xy_color="";//页面三加载的字符串和坐标
    private int tv_touchX,tv_touchY,tv_start,tv_end;
    private boolean tv_select=false,  tv_select_l_down=false,
                    tv_select_r_down=false,tv_select_down=false;
    private PopupWindow PopupmenuCopy;
    private BackgroundColorSpan colorSpan;
    private SpannableStringBuilder span;
    private String setc2edittext="";//现在和之前的string
    private long setc2edittext_int;//转换后的long
    public static boolean isStart=false;//是否开机启动
    private boolean setc2edittext_state=false;
    
    private static class myhand extends Handler{
        /* WeakReference 弱引用防止内存泄露*/
        private WeakReference<FloatWindowService> fws;

        public myhand(FloatWindowService fws){
            super(Looper.myLooper());
            this.fws=new WeakReference<FloatWindowService>(fws);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //myaccessibity.es.shutdownNow();
            FloatWindowService fws=this.fws.get();
            if(fws!=null){
                

                /*if(fws.time2!=null){
                    fws.time2.cancel();
                    fws.time2=null;
                    
                }*/

                if(msg.what==1){
                    fws.xfctv2.setText("△");
                    Toast.makeText(fws.getApplication(), "app已切换", 3000).show();
                }
                if(msg.what==3){
                    fws.xfctv2.setText("△");
                    Toast.makeText(fws.getApplication(), "画面未滑动", 3000).show();
                    
                }
                if(msg.what==2){
                    fws.xfctv2.setText("△");
                    Toast.makeText(fws.getApplication(), msg.getData().getString("app"),4000).show();
                
                }
                if(msg.what==4){
                    //Toast.makeText(fws.getApplication(), msg.getData().getString("app"),4000).show();
                    fws.infor=fws.infor+msg.getData().getString("app")+"\n";
                    fws.adapter.notifyDataSetChanged();
                }
                if(msg.what==5){
                    fws.xfctv2.setText("△");
                }

            }
        }

    }
    private static class myrunnable implements Runnable{
        private WeakReference<FloatWindowService> reference;
        public myrunnable(FloatWindowService fws){
            reference = new WeakReference<FloatWindowService>(fws);
        }
        @Override
        public void run() {
            FloatWindowService fws = reference.get();
            if(fws!=null){

                if(!Settings.canDrawOverlays(fws.getApplication())||
                   !qdactivity. isAccessibilitySettingsOn(fws.getApplication(),myaccessibity.class.getName())||
                   !qdactivity.hasPermission(fws.getApplication())){
                    Toast.makeText(fws.getApplication(), "权限异常程序将退出，请检查权限", Toast.LENGTH_SHORT).show();

                    fws.stopSelf();
                   
                    //fws.hand1.removeCallbacks(this);
                }else fws.hand1.postDelayed(this,1000);
            }
        }
    }
    private myrunnable runnable=new FloatWindowService.myrunnable(this);
    private Handler hand1=new Handler(Looper.myLooper());

    @Override

    public int onStartCommand(final Intent intent, int flags, int startId) {
        createNotificationChannel();
        //if(myaccessibity.imageReader==null){
        int  mResultCode = intent.getIntExtra("code",1);
        Intent mResultData =(Intent) intent.getParcelableExtra("data");
        
        //mResultData = intent.getSelector();

        MediaProjectionManager mMediaProjectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);

        myaccessibity. mMediaProjection = mMediaProjectionManager.getMediaProjection(mResultCode, mResultData /* Objects.requireNonNull(mResultData)*/);
        myaccessibity.imageReader = ImageReader.newInstance(w, h, PixelFormat.RGBA_8888, 2);
        myaccessibity.vd= myaccessibity. mMediaProjection.createVirtualDisplay("ScreenCapture",
                          w, h, dpi,
                          DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                          myaccessibity.imageReader.getSurface(), null, null);
        //imageReader对象获取需要等待imageReader.getSurface缓存区加载完，立即获取可能为null                                                       
       
        return START_NOT_STICKY;//返回此flag是禁止kill后重启
    }

    @Override
    public IBinder onBind(Intent p1) {

        return null;
    }

    @Override
    public void onCreate() {
        // TODO: Implement this method
        super.onCreate();
        setc=(myvp) LayoutInflater.from(getApplication())
            .inflate(R.layout.setc, null);
        
        setcbt5=(Button)setc.findViewById(R.id.setcbt5);
        //myaccessibity. mMediaProjection = mMediaProjectionManager.getMediaProjection(Activity.RESULT_OK, data);
        hand1.post(runnable);
        hand=new myhand(this);

        //mol.enable();
        vibrator = (Vibrator)getApplication()
            .getSystemService(getApplication().VIBRATOR_SERVICE);
        sp = getSharedPreferences("data", Context.MODE_PRIVATE);//创建data文件
        editor = sp.edit();//创建编辑对象
        params = new WindowManager.LayoutParams();
        params1 = new WindowManager.LayoutParams();
        params2 = new WindowManager.LayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            params1.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            params2.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            //设置悬浮窗可以出现在应用程序窗口之上    
            params.type = WindowManager.LayoutParams.TYPE_PHONE;
            params1.type = WindowManager.LayoutParams.TYPE_PHONE;
            params2.type = WindowManager.LayoutParams.TYPE_PHONE;
          }
        manager = (WindowManager) 
            getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        outMetrics = new DisplayMetrics();
        /*getDefaultDisplay()在api30中已弃用*/
        manager.getDefaultDisplay().getRealMetrics(outMetrics);
        w = outMetrics.widthPixels;

        h = outMetrics.heightPixels;
        //my_app.fs=this;
        ((my_app)getApplication()).w=w;
        ((my_app)getApplication()).h=h;
        dpi=outMetrics.densityDpi;
        ((my_app)getApplication()).dpi=dpi;
        scree_orientation = manager.getDefaultDisplay().getRotation();
        create_float_view();
        xfc2();
        setxfc();
        time=new mycountdowntimer(3000,3000);
        mDisplayListener = new DisplayManager.DisplayListener() {

            @Override
            public void onDisplayRemoved(int p1) {
            }

            @Override
            public void onDisplayAdded(int displayId) {
                // android.util.Log.i(TAG, "Display #" + displayId + " added.");
            }

            @Override
            public void onDisplayChanged(int displayId) {
                if (/*manager.getDefaultDisplay().getRotation()==Surface.ROTATION_90&&*/
                    scree_orientation != manager.getDefaultDisplay().getRotation()) {
                    time.restart();
                    manager.getDefaultDisplay().getRealMetrics(outMetrics);
                    w = outMetrics.widthPixels;
                    h = outMetrics.heightPixels;
                    myaccessibity.imageReader = ImageReader.newInstance(w, h, PixelFormat.RGBA_8888, 1);
                    myaccessibity.vd=myaccessibity. mMediaProjection.createVirtualDisplay("ScreenCapture",
                                                              w, h, dpi,
                                                              DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                                                              myaccessibity.imageReader.getSurface(), null, null);
                    ((my_app)getApplication()).w=w;
                    ((my_app)getApplication()).h=h;
                    ((my_app)getApplication()).dpi=outMetrics.densityDpi;
                    scree_orientation = manager.getDefaultDisplay().getRotation();
                    Rect r=new Rect();

                    
                    if (floatlayout.getParent() != null) {

                        floatlayout.getWindowVisibleDisplayFrame(r);
                        params.x = r.right-80-r.left;
                        params.y = (r.bottom-r.top) / 3;
                        manager.updateViewLayout(floatlayout, params);
                    }
                    if (floatlayout1.getParent() != null) {
                        //floatlayout1.getWindowVisibleDisplayFrame(r);
                        params1.x = params.x-350;
                        params1.y = params.y;
                        manager.updateViewLayout(floatlayout1, params1);
                    }
                    if (setc.getParent() != null) {
                        setc.getWindowVisibleDisplayFrame(r);
                        params2.x = (r.right  - r.left-800)/2;
                        params2.y = ( r.bottom  - r.top-800)/2;
                        manager.updateViewLayout(setc, params2);
                    }
                    //Toast.makeText(getApplication(), ""+w+","+h, Toast.LENGTH_SHORT).show(); 
                }
            }
        };
        displayManager =  
            (DisplayManager) getApplicationContext().getSystemService(DISPLAY_SERVICE);
        displayManager.registerDisplayListener(mDisplayListener,null);
        //第二个参数handler类型:侦听器应该被调用的处理程序，如果侦听器应该在调用线程的循环器上被调用，则为null
        my_ds();
        String str_tmp=sp.getString("dsyx","空");
        if(str_tmp.equals("空")
           ||str_tmp.equals("")){
           setc2edittext_int=-1;
        }else{
            String h=str_tmp.substring(0,2);
            String m=str_tmp.substring(3,5);
            String s=str_tmp.substring(6,8);
            String ms=str_tmp.substring(9,12);
            setc2edittext_int=Integer.parseInt(h)*3600000
                +Integer.parseInt(m)*60000
                +Integer.parseInt(s)*1000
                +Integer.parseInt(ms); 
        }
        if(isStart)
        xfctv2.performClick();
    }
    private void create_float_view() {

        // params.layoutInDisplayCutoutMode=WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;

        /*设置图片格式，效果为背景透明*/     
        params.format = PixelFormat.RGBA_8888;
        /*设置无焦点，可扩展到屏幕之外，可配合软键盘输出焦点*/     
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
            //WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS|
            WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM;
        params.gravity = Gravity.LEFT | Gravity.TOP;  
        params.x = w - 80;
        params.y = h / 3;     
        params.width = 80;
        params.height = 80;
        floatlayout = (LinearLayout) 
            LayoutInflater.from(getApplication())
            .inflate(R.layout.xfc4, null);
        /*设置布局约束即测量*/  
        floatlayout.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        /*开启轮廓剪裁，安卓5.0以下系统会报错*/
        floatlayout.setClipToOutline(true);
        manager.addView(floatlayout, params);
        // params.layoutInDisplayCutoutMode=WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_ALWAYS;
        xfctv5  = (TextView)floatlayout.findViewById(R.id.xfcTextView5);
        xfctv5.setOnTouchListener(new View.OnTouchListener() {
                @Override

                public boolean onTouch(View v, MotionEvent event) {

                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            time.cancel_time();
                            touchx = (int)(event.getX() + v.getLeft());
                            touchy = (int)(event.getY() + v.getTop());
                            touchrawx = (int)event.getRawX();
                            touchrawy = (int)event.getRawY();

                            break;
                        case MotionEvent.ACTION_MOVE:
                            Rect r=new Rect();
                            v.getWindowVisibleDisplayFrame(r);
                            int a = (int)event.getRawX() - touchx - r.left;
                            int b = (int)event.getRawY() - touchy - r.top;
                            if(a>0&&a<r.right -80-r.left)params.x=a;
                            if(b>0&&b<r.bottom-80-r.top)params.y=b;
                            manager.updateViewLayout(floatlayout, params);
                            //Toast.makeText(getApplication(), ""+r.left+","+r.right, Toast.LENGTH_SHORT).show();
                            if (floatlayout1.getParent() != null) {
                                if (params.x - 350 >= 1) {
                                    params1.x = params.x - 350;
                                } else params1.x = params.x + 80;

                                params1.y=params.y;
                                manager.updateViewLayout(floatlayout1, params1);
                            }
                            break;
                        case MotionEvent.ACTION_UP:
                            long t=event.getEventTime()-event.getDownTime();
                            
                            
                            if(t>=100&&
                               touchrawx==(int)event.getRawX()&&
                               touchrawy==(int)event.getRawY()){
                                c=xfctv5.getTextColors();
                                if (c.getDefaultColor() == -15598848) {
                                    xfctv5.setTextColor(-4184037);//红色
                                   
                                }else{
                                    xfctv5.setTextColor(-15598848);//绿色
                               
                                }    

                            }else if(t<100&&t>0&&
                                     touchrawx==(int)event.getRawX()&&
                                     touchrawy==(int)event.getRawY()){
                                if (floatlayout1.getParent() == null) {
                                    if (params.x - 350 >= 1) {
                                        params1.x = params.x - 350;
                                    } else params1.x = params.x + 80;
                                    params1.y = params.y;
                                    oa1=ObjectAnimator.ofFloat(floatlayout1,"scaleX", 0.2f,0.4f,0.6f,0.8f,1f);
                                    manager.addView(floatlayout1, params1);
                                    oa1.setDuration(300);
                                    oa1.start();
                                    
                                } else {
                                    manager.removeView(floatlayout1);

                                }   
                            }
                            c=xfctv5.getTextColors();
                            if (c.getDefaultColor() == -15598848)
                                time.start_time();
                            break;

                    }
                    return true; 

                }
            });



    }
    public void xfc2() {
        params1.format = PixelFormat.RGBA_8888;
        params1.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|
            WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM;
        params1.gravity = Gravity.LEFT | Gravity.TOP;
        params1.width = 350;
        params1.height = 80;
        //设置悬浮窗screeOrientation属性当悬浮窗作为顶层窗时
        //会导致此悬浮窗锁定screen方向
        //params1.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        floatlayout1 = (LinearLayout)LayoutInflater.from(getApplication())
            .inflate(R.layout.xfc3, null);
        floatlayout1.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                             View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

        xfctv2=(TextView)floatlayout1.findViewById(R.id.xfcTextView2);
        TextView xfctv3=(TextView)floatlayout1.findViewById(R.id.xfctextview3);
        xfctv3.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    time.cancel_time();
                    Rect r=new Rect();

                    v.getWindowVisibleDisplayFrame(r);
                    scree_orientation1 = manager.getDefaultDisplay().getRotation();
                    if ((xfctv2.getText()).equals("△")){
                        params2.x = (r.right  - r.left-800)/2;
                        params2.y = ( r.bottom  - r.top-800)/2;
                        manager.removeView(floatlayout);
                        manager.removeView(floatlayout1);
                        manager.addView(setc, params2);
                
                    }
                    Bitmap bit;
                    try {
                        bit=myaccessibity.getBitmap(myaccessibity.imageReader);
                    } catch (InterruptedException e) {bit=null;}
                    if (bit != null) {
                       int a []=myaccessibity.getColor(bit, params.x+40+r.left,params.y+40+r.top);
                       xy_color=(params.x+40+r.left)+","+(params.y+40+r.top)+"("+a[0]+")";
                       setcbt5.setText(xy_color);
                    }
                    /*String path = getExternalFilesDir(null).getPath() + "/crash_logInfo/";
                    File fl = new File(path);
                    //创建文件夹
                    if(!fl.exists()) {
                        fl.mkdirs();


                    }
                    try {
                        FileOutputStream fileOutputStream = new FileOutputStream(path + "jt.jpg");
                        //fileOutputStream.write(writer.toString().getBytes());
                        myaccessibity.getBitmap(myaccessibity.imageReader) .compress(Bitmap.CompressFormat.JPEG,100,fileOutputStream);
                        fileOutputStream.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e){}*/
                }
            });      
        TextView xfctv1 = (TextView)floatlayout1.findViewById(R.id.xfctextview1);

        xfctv1.setOnClickListener(new View.OnClickListener() {


                @Override

                public void onClick(View v) {

                    /*之后停止桌面窗口服务*/               
                    stopSelf();}

            });
        xfctv2.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v){
                    time.restart();
                    String app=sp.getString("app","null");
                    String t=xfctv2.getText().toString();
                    if(t.equals("‖")){
                        xfctv2.setText("△");
                        //myaccessibity.state=false;
                        if(myaccessibity.es!=null)myaccessibity.es.shutdownNow();
                    }else if(t.equals("△")){ 
                        my_vibrator(100);//单位是ms
                        switch  (app){
                            case "null":
                                xfctv2.setText("△");
                                Toast.makeText(getApplicationContext(),app,3000).show();
                                break;
                            case "快手":
                                if(getapp("com.kuaishou.nebula")){
                                    xfctv2.setText("‖");
                                    myaccessibity.myac. startApp("com.kuaishou.nebula",setc2edittext_int);
                                 
                                }else{
                                    Toast.makeText(getApplicationContext(),"未找到"+app,3000).show();
                                    xfctv2.setText("△");
                                }
                                break;
                            case "抖音":
                                if(getapp("com.ss.android.ugc.aweme.lite")){
                                    xfctv2.setText("‖");
                                    myaccessibity.myac.startApp("com.ss.android.ugc.aweme.lite",setc2edittext_int);

                                }else{
                                    Toast.makeText(getApplicationContext(),"未找到"+app,3000).show();
                                    xfctv2.setText("△");
                                }
                                break;
                            case "拼多多抢卷":
                                if(getapp("com.xunmeng.pinduoduo")){
                                    xfctv2.setText("‖");
                                    myaccessibity.myac.startApp("com.xunmeng.pinduoduo",setc2edittext_int);

                                }else{
                                    Toast.makeText(getApplicationContext(),"未找到"+app,3000).show();
                                    xfctv2.setText("△");
                                }
                                break;
                            case "微信约苗":
                                if(getapp("com.tencent.mm")){
                                    xfctv2.setText("‖");
                                    myaccessibity.myac.startApp("com.tencent.mm",setc2edittext_int);

                                }else{
                                    Toast.makeText(getApplicationContext(),"未找到"+app,3000).show();
                                    xfctv2.setText("△");
                                }
                                break;
                            default: 
                                xfctv2.setText("△");
                                break;
                        }
                        //startjb(sp.getString("app","null"),sp.getInt("ds",0));

                    }
                }
            });
    }
    public void setxfc(){
        params2.format = PixelFormat.RGBA_8888;
        //params2.type= WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY;
        
        params2.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM/*|WindowManager. LayoutParams.FLAG_NOT_TOUCH_MODAL*/ ;
        params2.gravity = Gravity.LEFT | Gravity.TOP;
        params2.width = 800;
        params2.height = 800;
       
       // setc= (LinearLayout)LayoutInflater.from(getApplication())
          //  .inflate(R.layout.setc, null);
        setc.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                     View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        setcll=(ViewPager2)setc.findViewById(R.id.setcll1);   
        ( (my_app)getApplication()).vp2=setcll;
        //getApplicationContext() //setcll.isVerticalScrollBarEnabled()
        adapter=  new RecyclerView.Adapter(){
                
                @Override
                public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup p1, int p2) {
                    switch (p2){
                        case 0:
                            View item1 =LayoutInflater.from(getApplication())
                                .inflate(R.layout.setc1, p1,false);
                            setc1holder vh1=new setc1holder(item1);
                            //RecyclerView.ViewHolder vh1 = new RecyclerView.ViewHolder(item1){};
                            return vh1;
                        case 1:
                            View item2 = LayoutInflater.from(getApplication())
                                .inflate(R.layout.setc2, p1,false);
                            setc2holder vh2=new setc2holder(item2);
                            //RecyclerView.ViewHolder vh2 = new RecyclerView.ViewHolder(item2){};
                            return vh2;
                        case 2:
                            View item3 = LayoutInflater.from(getApplication())
                                .inflate(R.layout.setc3, p1,false);
                            setc3holder  vh3 = new setc3holder(item3);
                            return vh3;
                        case 3:
                            View item4 = LayoutInflater.from(getApplication())
                                .inflate(R.layout.setc4, p1,false);
                            RecyclerView.ViewHolder vh4 = new RecyclerView.ViewHolder(item4){};
                            return vh4; 
                    }
                    return null;
                }

                @Override
                public void onBindViewHolder(RecyclerView.ViewHolder p1, int p2) {
                    switch (p2){
                        
                        case 0:
                            setc1holder rview=(setc1holder)p1;
                            if(sp.getString("app","null").equals("null")){
                                rview.setc1rb1.setChecked(false);
                                rview.setc1rb2.setChecked(false);
                                rview.setc1rb3.setChecked(false);
                                rview.setc1rb4.setChecked(false);
                            }else if(sp.getString("app","null").equals("快手")){
                                rview.setc1rb1.setChecked(true);
                                rview.setc1rb2.setChecked(false);
                                rview.setc1rb3.setChecked(false);
                                rview.setc1rb4.setChecked(false);
                            }else if(sp.getString("app","null").equals("抖音")){
                                rview.setc1rb1.setChecked(false);
                                rview.setc1rb2.setChecked(true);
                                rview.setc1rb3.setChecked(false);
                                rview.setc1rb4.setChecked(false);
                            }else if(sp.getString("app","null").equals("拼多多抢卷")){
                                rview.setc1rb1.setChecked(false);
                                rview.setc1rb2.setChecked(false);  
                                rview.setc1rb3.setChecked(true);
                                rview.setc1rb4.setChecked(false);
                            }else if(sp.getString("app","null").equals("微信约苗")){
                                rview.setc1rb1.setChecked(false);
                                rview.setc1rb2.setChecked(false);
                                rview.setc1rb3.setChecked(false);
                                rview.setc1rb4.setChecked(true);
                            }
                            rview.setc1rb1.setOnClickListener( new OnClickListener(){
                                    @Override
                                    public void onClick(View v){
                                        editor.putString("app","快手");
                                        editor.commit();
                                    }
                                });
                            rview.setc1rb2.setOnClickListener( new OnClickListener(){
                                    @Override
                                    public void onClick(View v){
                                        //Toast.makeText(getApplicationContext(),sp.getString("app",null),3000).show();
                                        editor.putString("app","抖音");
                                        editor.commit();
                                    }
                                });
                            rview.setc1rb3.setOnClickListener( new OnClickListener(){
                                    @Override
                                    public void onClick(View v){
                                        editor.putString("app","拼多多抢卷");
                                        editor.commit();
                                    }
                                });
                            rview.setc1rb4.setOnClickListener( new OnClickListener(){
                                    @Override
                                    public void onClick(View v){
                                        editor.putString("app","微信约苗");
                                        editor.commit();
                                    }
                                });    
                            break;
                        case 1:
                            setc2holder rview1=(setc2holder)p1;
                            final TextView tv=rview1.setc2tv;
                            int t=sp.getInt("ds",0);
                            if(sp.getString("kjqd","null").equals("null")||
                               sp.getString("kjqd","null").equals("关闭")){
                               rview1.setc2s1.setChecked(false);
                            }else rview1.setc2s1.setChecked(true);
                            rview1.setc2s1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

                                    @Override
                                    public void onCheckedChanged(CompoundButton p1, boolean p2)
                                    {
                                        // TODO: Implement this method
                                        ComponentName receiver = new ComponentName(getApplicationContext(),
                                                                                   autorun.class);
                                        PackageManager pm = getApplicationContext().getPackageManager();
                                        if(p2){
                                            pm.setComponentEnabledSetting(receiver,
                                                                          PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                                                                          PackageManager.DONT_KILL_APP);
                                            editor.putString("kjqd","开启");
                                            editor.commit();
                                        }else{
                                            pm.setComponentEnabledSetting(receiver,
                                                                          PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                                                                          PackageManager.DONT_KILL_APP);
                                            editor.putString("kjqd","关闭");
                                            editor.commit();
                                        }
                                    }

                                });
                            if(sp.getString("zdts","null").equals("null")||
                               sp.getString("zdts","null").equals("关闭")){
                               rview1.setc2s2.setChecked(false);
                            }else rview1.setc2s2.setChecked(true);
                            rview1.setc2s2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

                                    @Override
                                    public void onCheckedChanged(CompoundButton p1, boolean p2)
                                    {
                                        // TODO: Implement this method
                                        if(p2){
                                            editor.putString("zdts","开启");
                                            editor.commit();
                                        }else{
                                            editor.putString("zdts","关闭");
                                            editor.commit();
                                        }
                                    }

                                });

                            tv.setText("(0为不退出)"+t+"/1000M");
                            rview1.setc2sb.setProgress(t);
                            rview1.setc2sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

                                    @Override
                                    public void onStartTrackingTouch(SeekBar p1)
                                    {
                                        // TODO: Implement this method
                                    }

                                    @Override
                                    public void onStopTrackingTouch(SeekBar p1)
                                    {
                                        // TODO: Implement this method
                                    }

                                    @Override
                                    public void onProgressChanged(SeekBar bar, int i, boolean b) {
                                        tv.setText("(0为不退出)"+i+"/1000M");
                                        editor.putInt("ds",i);
                                        editor.commit();//委托画笔存入共享数据
                                    }   
                                });
                                
                            rview1.setc2sb.setOnTouchListener( new View.OnTouchListener() {
                                    @Override
                                    public boolean onTouch(View v, MotionEvent me) {
                                        v.getParent().requestDisallowInterceptTouchEvent(true);
                                        /*v.requestFocus();
                                        InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                        inputmanger.showSoftInput(v,0);*/
                                        return false;
                                    }
                                });//解决滑条与滑动页冲突
                            setc2edittext=sp.getString("dsyx","");
                            rview1.setc2et.setText(setc2edittext);
                            rview1.setc2et.setOnTouchListener (new View.OnTouchListener (){
                                    @Override
                                    public boolean onTouch (View v,MotionEvent m){
                                        
                                      if(params2.flags!=WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL){
                                          //setc.clearFocus();  
                                          params2.flags=WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
                                          manager.updateViewLayout(setc,params2);}
                                      return false;
                                      //((EditText)v).setFocusable(true);
                                        //View view = setc.getRootView();
                                      /*  if (v!= null) {
                                            Toast.makeText(getApplication(), "太古汇", Toast.LENGTH_SHORT).show();
                                            InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                            inputmanger.hideSoftInputFromWindow(v.getWindowToken(), 0);
                                        }*/
                                    //( (setc2holder)p1).setc2et.setfo(true);
                                     // }
                                        /*try {
                                            Thread.sleep(500);
                                        } catch (InterruptedException e) {}
                                        v.requestFocus();
                                        InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                        inputmanger.showSoftInput(v,0);*/
                                    }
                                    
                            });//输入框点击获得焦点
                           
                            
                           rview1 .setc2et.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
                                    @Override
                                    public void onFocusChange(View v, boolean hasFocus) {
                                        if (hasFocus) {
                                            
                                        } else {
                                            InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                            inputmanger.hideSoftInputFromWindow(v.getWindowToken(), 0);
                                            //params2.flags=WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM  ;
                                            //manager.updateViewLayout(FloatWindowService.setc,FloatWindowService.params2);
                                           // Toast.makeText(getApplication(), "焦点改变", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                            });//焦点监听
                            
                            rview1.setc2et.addTextChangedListener(new TextWatcher() {

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                                        //text正在改变过程中调用，s代表已经改变完成的文本，start已经改变的字符下标，
                                        //before被替换的原字符个数，count从start开始替换的新字符个数
                                        // TODO Auto-generated method stub

                                    }

                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count,                                       
                                         int after) {
                                        //将要改变还没改变时调用，s改变前的字符，start将要改变时的字符下表，
                                        //count从start开始将要改变的原字符个数，after从start开始将要替换成新字符的个数
                                        // TODO Auto-generated method stub

                                    }

                                    @Override
                                    public void afterTextChanged(Editable s) {//s 改变后调用
                                        // TODO Auto-generated method stub
                                        
                                        setc2edittext=s.toString();
                                        setc2edittext=setc2edittext.replace(" ","");
                                        setc2edittext_state=true;
                                        //setc2edittext=setc2edittext.replace(":","");
                                    }
                            });//text监听
                            
                            break;
                        case 2:
                            final setc3holder rview2=(setc3holder)p1;
                            rview2.setc3s1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

                                    @Override
                                    public void onCheckedChanged(CompoundButton p1, boolean p2)
                                    {                                
                                           setc3switch=p2;
                                                              
                                    }

                            });
                            rview2.setc3bt1.setOnClickListener(new View.OnClickListener(){

                                    @Override
                                    public void onClick(View p1) {
                                        infor="";
                                        rview2.setc3tv1.setText(infor);
                                        
                                    }
                                    
                                
                            });
                            //rview2.setc3tv1.setKeyListener(null);
                            rview2.setc3tv1.setMovementMethod(ScrollingMovementMethod.getInstance());
                            //Spanned spa =Html.fromHtml( "我叫<font color=\"#FF0000\">张小龙</font>",Html.FROM_HTML_MODE_COMPACT);

                            rview2.setc3tv1.setText(infor);
                           
                            rview2.setc3tv1.post(new Runnable() { //新开一个线程进行行数的获取
                                    @Override
                                    public void run() {
                                       
                                        int scrollAmount = rview2.setc3tv1.getLineCount()*rview2.setc3tv1.getLineHeight();
                                        if (scrollAmount > rview2.setc3tv1.getHeight())
                                            rview2.setc3tv1.scrollTo(0, scrollAmount-rview2.setc3tv1.getHeight());
                                    }
                            });
                            
                            
                           
                            rview2.setc3tv1.setOnTouchListener(new View.OnTouchListener(){

                                    @Override
                                    
                                    public boolean onTouch(View p1, MotionEvent p2) {
                                        int action=p2.getAction();
                                        switch(action){
                                            case MotionEvent.ACTION_DOWN:
                                                tv_touchX=(int)p2.getX();
                                                tv_touchY=(int)p2.getY();
                                                int ii=TextViewUtils.getTextViewSelectionIndexByTouch((TextView)p1,tv_touchX,tv_touchY);
                                                if(tv_select==true&& ii==tv_start){
                                                   {tv_select_l_down=true;}
                                                }else tv_select_l_down=false;
                                                if(tv_select==true&& ii==tv_end){
                                                   {tv_select_r_down=true;}
                
                                                }else  tv_select_r_down=false;
                                                if(tv_select==true&& ii>=tv_start&&ii<=tv_end){
                                                   {tv_select_down=true;
                                                    /*return true;((TextView)p1).setMovementMethod(null)*/}

                                                }else tv_select_down=false;
                                                if(tv_select_l_down==true||tv_select_r_down==true)
                                                    return true;//返回真消费事件，解决与textview的滑动冲突
                                            break;
                                            case MotionEvent.ACTION_MOVE:
                                              if(tv_select==true){
                                                   p1.getParent().requestDisallowInterceptTouchEvent(true);
                                                  int tmpx=(int)p2.getX();
                                                  int tmpy=(int)p2.getY();
                                                  int i=TextViewUtils.getTextViewSelectionIndexByTouch((TextView)p1,tmpx,tmpy);
                                                  if(tv_select_l_down==true&& i!=-1){  
                                                     if(i<=tv_end){
                                                         span.setSpan(colorSpan, i, tv_end+1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                                                         ( (TextView)p1).setText (span);
                                                         tv_start=i;
                                                         tv_select_r_down=false;
                                                     }
                                                     if(i>tv_end){
                                                         tv_select_l_down=false;
                                                         tv_select_r_down=true;   
                                                         tv_start=tv_end;
                                                         tv_end=i;
                                                     }  
                                                        
                                                  }
                                                  if(tv_select_r_down==true&& i!=-1){  
                                                      if(i>=tv_start){
                                                          span.setSpan(colorSpan, tv_start, i+1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                                                          ( (TextView)p1).setText (span);
                                                          tv_end=i;
                                                          tv_select_l_down=false;
                                                      }
                                                      if(i<tv_start){
                                                          tv_select_l_down=true;
                                                          tv_select_r_down=false;
                                                          tv_end=tv_start;
                                                          tv_start=i;
                                                          
                                                      }  

                                                  }
                                  
                                            }
                                            break;
                                            case MotionEvent.ACTION_UP:
                                                 //setcll.setUserInputEnabled(true);
                                                long t=p2.getEventTime()-p2.getDownTime();
                                                 if(tv_touchX==p2.getX()&&tv_touchY==p2.getY()){
                                                     if(t<500){
                                                     if(tv_select_down&&!setc3switch){                                                                                                                                                                                        
                                                                 if(PopupmenuCopy==null)
                                                                    initPopu(p1);
                                                                 PopupmenuCopy.showAtLocation(p1,Gravity.RIGHT|Gravity.CENTER_VERTICAL,0,0);                                                                                                                          
                                                         }else  if(tv_select){
                                                             ( (TextView)p1).setText(infor);
                                                             tv_select=false;
                                                         }
                                                     }
                                                     if(t>=500){
                                                         int index= TextViewUtils.getTextViewSelectionIndexByTouch((TextView)p1,tv_touchX,tv_touchY);
                                                         if(index!=-1){
                                                             //if(span==null)
                                                             span=new SpannableStringBuilder(((TextView)p1).getText().toString() );
                                                             if(colorSpan==null)
                                                                 colorSpan=new BackgroundColorSpan(Color.parseColor("#ff0090ff"));
                                                             //span.append(((TextView)p1).getText().toString() );
                                                             //ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#FF0090FF"));

                                                             /*  ClickableSpan Cspan=new ClickableSpan(){

                                                              @Override
                                                              public void onClick(View v) {
                                                              }

                                                              @Override 
                                                              public void updateDrawState(TextPaint ds) { 
                                                              ds.setColor(Color.parseColor("#ff0090ff")ds.linkColor); 
                                                              ds.setUnderlineText(true); 
                                                              }

                                                              };
                                                              ( (TextView)p1) .setMovementMethod(LinkMovementMethod.getInstance());*/

                                                             //setSpan():设置容器内文本样式
                                                             //第二，三个参数代表start,end,start从零开始，但不包括end,好处是end-start=span字符个数
                                                             //第四个参数
                                                             //int SPAN_EXCLUSIVE_EXCLUSIVE = 33; //在Span前后输入的字符都不应用Span效果
                                                             //int SPAN_EXCLUSIVE_INCLUSIVE = 34; //在Span前面输入的字符不应用Span效果，后面输入的字符应用Span效果
                                                             //int SPAN_INCLUSIVE_EXCLUSIVE = 17; //在Span前面输入的字符应用Span效果，后面输入的字符不应用Span效果
                                                             //int SPAN_INCLUSIVE_INCLUSIVE = 18; //在Span前后输入的字符都应用Span效果
                                                             span.setSpan(colorSpan, index, index+1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                                                             ( (TextView)p1).setText (span);
                                                             tv_start=index;
                                                             tv_end=index;
                                                             tv_select=true;
                                                        }
                                                     }
                                                     
                                                 }
                                                //( (TextView)p1).setMovementMethod(ScrollingMovementMethod.getInstance());
                                                
                                            break;
                                            
                                        }                                                                  
                                        
                                        return false;
                                    }
                                    
                                
                            });                      
                            //rview2.setc3sc1.fullScroll(ScrollView.FOCUS_DOWN);
                         
                           //rview2. setc3tv1.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {});

                    
                           /*rview2.setc3tv1.addOnAttachStateChangeListener(
                                new View.OnAttachStateChangeListener() {

                                    @Override
                                    public void onViewAttachedToWindow(View p1) {
                                    }

                                    @Override
                                    public void onViewDetachedFromWindow(View p1) {
                                        rview2.setc3tv1.setCursorVisible(false);
                                        rview2.setc3tv1.setCursorVisible(true);
                                   }                                   
                            });*/                        
                            break;
                        case 3:
                            break;
                    }
                }

                @Override
                public int getItemCount() {
                    return 4;
                }

                @Override
                public int getItemViewType(int p){

                    return p;
                }
            };
        setcll.setAdapter(adapter);
        RadioGroup setcrg1=(RadioGroup)setc.findViewById(R.id.setcrg1);
        setcll.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback(){
                @Override
                public void onPageSelected(int p){
                    switch (p){
                        case 0:
                            ((RadioButton)setc.findViewById(R.id.setcbt1)).setChecked(true);
                            break;
                        case 1:
                            ((RadioButton)setc.findViewById(R.id.setcbt2)).setChecked(true);
                            break;
                        case 2:
                            ((RadioButton)setc.findViewById(R.id.setcbt3)).setChecked(true);
                            break;
                        case 3:
                            ((RadioButton)setc.findViewById(R.id.setcbt4)).setChecked(true);
                            break;
                    }
                }
            });
        setcrg1.setOnCheckedChangeListener( new OnCheckedChangeListener(){
                @Override
                public void onCheckedChanged(RadioGroup rg,int id){
                    switch (id){
                        case R.id.setcbt1:
                            setcll.setCurrentItem(0);
                            break;
                        case R.id.setcbt2:
                            setcll.setCurrentItem(1);
                            break;
                        case R.id.setcbt3:
                            setcll.setCurrentItem(2);
                            break;
                        case R.id.setcbt4:
                            setcll.setCurrentItem(3);
                            break;
                    }
                }
            });

      

        setcbt5.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){              
                    if(setc2edittext_state){
                        if(setc2edittext.length()==12
                           &&setc2edittext.substring(2,3).equals(":")
                           &&setc2edittext.substring(5,6).equals(":")
                           &&setc2edittext.substring(8,9).equals(":")){
                           String text=setc2edittext.replace(":","");                     
                            if(text.matches("^[0-9]*$")){
                               String h=setc2edittext.substring(0,2);
                               String m=setc2edittext.substring(3,5);
                               String s=setc2edittext.substring(6,8);
                               String ms=setc2edittext.substring(9,12);
                               if(Integer.parseInt(h)<24
                                  &&Integer.parseInt(m)<60
                                  &&Integer.parseInt(s)<60
                                  &&Integer.parseInt(ms)<1000){                            
                                  setc2edittext_int=Integer.parseInt(h)*3600000
                                                    +Integer.parseInt(m)*60000
                                                    +Integer.parseInt(s)*1000
                                                    +Integer.parseInt(ms);
                                  setc2edittext_state=false;
                                  editor.putString("dsyx",setc2edittext);
                                  editor.commit();  
                               }else{
                                   Toast.makeText(getApplication(), "定时运行参数设置越界，请重新设置", Toast.LENGTH_SHORT).show();
                                   return;        
                               }
                           }else{
                               Toast.makeText(getApplication(), "定时运行参数设置错误，请重新设置", Toast.LENGTH_SHORT).show();
                               return;                     
                               }
                        }else if(setc2edittext.equals("")){
                               setc2edittext_int=-1;
                               setc2edittext_state=false;
                               editor.putString("dsyx",setc2edittext);
                               editor.commit();             
                               }else{
                                   Toast.makeText(getApplication(), "定时运行参数长度设置错误，请重新设置", Toast.LENGTH_SHORT).show();
                                  
                                   return;                         
                               }

                        
                    }
                    
                    //Toast.makeText(getApplication(), "fgjjkk", Toast.LENGTH_SHORT).show();   
                    
                    Rect r=new Rect();
                    v.getWindowVisibleDisplayFrame(r);
                   
                    if(scree_orientation==scree_orientation1){
                        manager.addView(floatlayout,params);
                        manager.addView(floatlayout1,params1);
                    }else  {
                        params.x = r.right - 80-r.left;
                        params.y = h / 3;
                        params1.x=params.x-350;
                        params1.y=params.y;
                        manager.addView(floatlayout,params);
                        manager.addView(floatlayout1,params1);
                    }
                   
                    params2.flags=WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM;
                    manager.removeView(setc);
                    my_ds();
                    c=xfctv5.getTextColors();
                    if (c.getDefaultColor() == -15598848) {
                        time.start_time();
                    }
                }
              
       });
       Button setcbt6=(Button)setc.findViewById(R.id.setcbt6);
       setcbt6.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    //tv.setText(Html.fromHtml("我是<font color=blue>danyijiangnan</font>"));
                   // infor = infor+"<font color=red>坐标:</font>"+"\n";
                    infor=infor+"坐标:"+xy_color+"\n";
                   
                  
                    adapter.notifyDataSetChanged();
                }
                
       });   
    }
    @Override
    public void onDestroy() {
        //exitjb();
        if(myaccessibity.es!=null)myaccessibity.es.shutdownNow();
        //myaccessibity.state=false;

        if (hand != null /*&& hand.hasMessages(PROGRESS)*/) {
            hand.removeCallbacksAndMessages(null);
        }//null  代表移除所有消息和回调
        if (hand1!= null /*&& hand.hasMessages(PROGRESS)*/) {
            hand1.removeCallbacksAndMessages(null);
        }
        if(time!=null){
            time.cancel_time();
            time=null;

        }
        if(time2!=null){        
            time2.cancel();
            time2=null;
        }
        if (floatlayout.getParent()!= null){

            manager.removeView(floatlayout);
            floatlayout=null;
        }
        if (floatlayout1.getParent()!=null){
            manager.removeView(floatlayout1);
            //floatlayout1=null;
        }
        if (setc.getParent()!=null){
            manager.removeView(setc);
            //setc=null;
        }
        displayManager.unregisterDisplayListener(mDisplayListener);
        //mol.disable();
        //myaccessibity.imageReader=null;
        isStart=false;
        super.onDestroy();
    }
 
    private boolean getapp(String app){

        PackageManager pi = getPackageManager();
        Intent intent =pi.getLaunchIntentForPackage(app);   
        if (intent == null ) {
            return false;              
        }
        /*FLAG_ACTIVITY_RESET_TASK_IF_NEEDED此标志必须在activity中设置allowTaskReparenting为true
         代表允许task重排父目录,让已存在的activity始终置顶*/
       // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
       // startActivity(intent);
        
        //startActivityForResult();
        return true;
    }
    private void my_vibrator(int t){
        if(sp.getString("zdts","null").equals("开启")){
            vibrator.vibrate(t);                      
        }
    }
    private void my_ds(){
        final int t=sp.getInt("ds",0); 
        
        
        if(t!=0&&my_ds_time!=t){    
            if(time2!=null){time2.cancel();time2=null;}
            time2= new CountDownTimer(t*60000, t*60000){

                @Override
                public void onTick(long p1) {
                }                

                @Override                           
                public void onFinish() {
                    time2=null;
                    
                    stopSelf();
                }
            };
            time2.start();
            
        }
        if(t==0&&time2!=null){time2.cancel();time2=null;}
        my_ds_time=t;
    }
    public class mycountdowntimer extends CountDownTimer{
        private boolean state=false;

        public mycountdowntimer(int p1,int p2){
            super (p1,p2);
        }
        @Override                                       
        public void onFinish(){
            if(floatlayout1.getParent()!=null){
                manager.removeView(floatlayout1);
            }         
        }
        @Override 
        public void onTick(long p){

        }

        public void restart(){
            if(state){
                cancel();         
                start();
            }
        }
        public void start_time() {
            if(!state){
                start();
                state=true;
            }
        }
        public void cancel_time() {
            if(state){
                cancel();
                state=false;
            }
        }

    }     
   
    private void createNotificationChannel() {
        Notification.Builder builder = new Notification.Builder(this.getApplicationContext()); //获取一个Notification构造器
        Intent nfIntent = new Intent(this, qdactivity.class); //点击后跳转的界面，可以设置跳转数据

        builder.setContentIntent(PendingIntent.getActivity(this, 0, nfIntent, 0)) // 设置PendingIntent
            .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_launcher)) // 设置下拉列表中的图标(大图标)
            //.setContentTitle("SMI InstantView") // 设置下拉列表里的标题
            .setSmallIcon(R.drawable.ic_launcher) // 设置状态栏内的小图标
            .setContentText("is running......") // 设置上下文内容
            .setWhen(System.currentTimeMillis()); // 设置该通知发生的时间

        /*以下是对Android 8.0的适配*/
        //普通notification适配
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId("notification_id");
        }
        //前台服务notification适配
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
            NotificationChannel channel = new NotificationChannel("notification_id", "notification_name", NotificationManager.IMPORTANCE_LOW);
            notificationManager.createNotificationChannel(channel);
        }

        Notification notification = builder.build(); // 获取构建好的Notification
        notification.defaults = Notification.DEFAULT_SOUND; //设置为默认的声音
        startForeground(110, notification);
       
    }

    private void initPopu(final View tv){
        LayoutInflater layoutInflater = (LayoutInflater) getApplication()
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layoutView = layoutInflater.inflate(
            R.layout.popup_copy, null);
        PopupmenuCopy = new PopupWindow(getApplication());
        PopupmenuCopy.setContentView(layoutView);
        PopupmenuCopy.setWidth(LayoutParams.WRAP_CONTENT);
        PopupmenuCopy.setHeight(LayoutParams.WRAP_CONTENT);
        //ColorDrawable dw = new ColorDrawable(0xb0000000);
        PopupmenuCopy.setBackgroundDrawable(null);
        //null为透明背景
        PopupmenuCopy.setFocusable(true);// 点击空白处时，隐藏掉pop窗口
        PopupmenuCopy.setOutsideTouchable(true);//窗口外可点击
        Button popBt1 = (Button) layoutView
            .findViewById(R.id.popupcopyButton1);
        Button popBt2 = (Button) layoutView
            .findViewById(R.id.popupcopyButton2);
        Button popBt3 = (Button) layoutView
            .findViewById(R.id.popupcopyButton3);
        Button popBt4 = (Button) layoutView
            .findViewById(R.id.popupcopyButton4);
        popBt1.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View p) {
                    ((TextView)tv).setText(infor);
                    PopupmenuCopy.dismiss();
                }
                
            
        });
        popBt2.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View p) {
                    if(tv_start-1>=0){
                        span.setSpan(colorSpan,tv_start-1,tv_end+1,Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                        ((TextView)tv).setText(span);
                        tv_start=tv_start-1;
                    }
                }


        });
        popBt3.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View p) {
                    int l=((TextView)tv).getText().length();
                    if(tv_end+1<=l-1){
                        tv_end=tv_end+1;
                        span.setSpan(colorSpan,tv_start,tv_end+1,Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                        ((TextView)tv).setText(span);
                        
                    }
                }


        });
        popBt4.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View p) {
                     
                    if(tv!=null&&((TextView)tv).getText()!=null){
                         ClipboardManager manager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                         String s=((TextView)tv).getText().toString();                      
                         if(tv_end<=s.length()-1){
                            s=s.substring(tv_start,tv_end+1);
                            ClipData clipData = ClipData.newPlainText("text", s);
                            manager.setPrimaryClip(clipData);
                            Toast.makeText(getApplication(), "文本复制成功", Toast.LENGTH_SHORT).show();
                         }else Toast.makeText(getApplication(), "复制错误，文本索引越界", Toast.LENGTH_SHORT).show();
                    }else Toast.makeText(getApplication(), "复制错误，获取文本为空", Toast.LENGTH_SHORT).show();
                }


        });
    }
}
class setc1holder extends RecyclerView.ViewHolder{
    public RadioButton setc1rb1,setc1rb2,setc1rb3,setc1rb4;
    public setc1holder (@NonNull View v){
        super(v);
        setc1rb1=(RadioButton)v.findViewById(R.id.setcrb1);
        setc1rb2=(RadioButton)v.findViewById(R.id.setcrb2);
        setc1rb3=(RadioButton)v.findViewById(R.id.setcrb3);
        setc1rb4=(RadioButton)v.findViewById(R.id.setcrb4);
    }                                            
}
class setc2holder extends RecyclerView.ViewHolder{
    public Switch setc2s1,setc2s2;
    public SeekBar setc2sb;
    public TextView setc2tv;
    public EditText setc2et;
    public setc2holder (@NonNull View v){
        super(v);
        setc2s1=(Switch)v.findViewById(R.id.setc2s1);
        setc2s2=(Switch)v.findViewById(R.id.setc2s2);
        setc2sb=(SeekBar)v.findViewById(R.id.setc2sb);
        setc2et=(EditText)v.findViewById(R.id.setc2et);
        setc2tv=(TextView)v.findViewById(R.id.setc2tv);
        
    }
}   
class setc3holder extends RecyclerView.ViewHolder {

    
    
    public Switch setc3s1;
    public TextView setc3tv1;
    public Button setc3bt1;
   
    
    public setc3holder (@NonNull View v){
        super(v);
        setc3s1=(Switch)v.findViewById(R.id.setc3Switch1);
        setc3tv1=(TextView)v.findViewById(R.id.setc3TextView1);
        setc3bt1=(Button)v.findViewById(R.id.setc3Button1);
        
    }
}

