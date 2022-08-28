package com.mycompany.application3;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.widget.Toast;
import java.util.List;

public class qdactivity extends Activity
{
    private static final int REQUEST_MEDIA_PROJECTION = 1;
    private MediaProjectionManager mMediaProjectionManager;

    /*@Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
    }*/
    //private MediaProjection mMediaProjection;
    //private VirtualDisplay mVirtualDisplay;   

    /*@Override
     public void onPointerCaptureChanged(boolean p1)
     {
     // TODO: Implement this method
     }*/
    /* @Override
     public void onStart() {
     super.onStart();

     Window window = getWindow();
     WindowManager.LayoutParams windowParams = window.getAttributes();
     windowParams.dimAmount = 0.0f;

     window.setAttributes(windowParams);
     }*/
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // TODO: Implement this method
        //requestScreenShot();

        super.onCreate(savedInstanceState);
        start_service();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==REQUEST_MEDIA_PROJECTION&&
            resultCode == RESULT_OK && data != null){
            Intent service = new Intent(this, FloatWindowService.class);
            service.putExtra("code", resultCode);
            service.putExtra("data", data);
			if (Build.VERSION.SDK_INT >=29) {
			   //安卓10开始截图必须前台服务中进行
               startForegroundService(service);
			}else startService(service);
            finish();
            
        } else finish();

    }

    @Override
    protected void onResume()
    {
        // TODO: Implement this method
        super.onResume();

        //finish();
    }
    /*
     * 跳转到无障碍服务设置页面
     * @param context 设备上下文
     * Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
     *intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
     *context.startActivity(intent);
     * @return true 已开启false 未开启
     */
    public static  boolean isAccessibilitySettingsOn(Context context,String className){
        if (context == null){
            return false;
        }
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServices =
            activityManager.getRunningServices(100);// 获取正在运行的服务列表
        if (runningServices.size()<0){
            return false;
        }
        for (int i=0;i<runningServices.size();i++){
            ComponentName service = runningServices.get(i).service;
            if (service.getClassName().equals(className)){
                return true;
            }
        }
        return false;
    } 
    /*public static boolean hasPermission(Context c) {
        AppOpsManager appOps = (AppOpsManager)
             c.getSystemService(Context.APP_OPS_SERVICE);
        int mode = 0;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                                         android.os.Process.myUid(), c.getPackageName());
        }
        return mode == AppOpsManager.MODE_ALLOWED;
    }*/
    private void requestScreenShot() {
        mMediaProjectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        if(mMediaProjectionManager!=null){

            startActivityForResult(
                mMediaProjectionManager.createScreenCaptureIntent(),
                REQUEST_MEDIA_PROJECTION);
            /*new Handler().postDelayed(new Runnable(){

                    @Override
                    public void run() {
                        myaccessibity.myac.mygesture(80,835,2242,835,2242);
                    }
                }, 800);*/
                
        }else Toast.makeText(getApplication(), "未获取到:MediaProjectionManager", Toast.LENGTH_SHORT).show();
    }
    private void start_service(){
        String a="",b="";//c="";
        if(FloatWindowService.floatlayout==null){
            if(!Settings.canDrawOverlays(this))
                a="\n悬浮窗权限";
            if(!isAccessibilitySettingsOn(this,myaccessibity.class.getName()))
                b="\n无障碍权限";
            //if(!hasPermission(this))
            //   c="\n使用情况访问权限";
            if(a.equals("")&&b.equals("")/*&&c.equals("")*/){
              
                {requestScreenShot();}
            
            }else{
                Toast.makeText(getApplication(),"请手动打开以下权限:"+a+b/*+c*/,3000).show();
                finish();
            }
        }else {
            Toast.makeText(getApplication(),"程序已启动",3000).show();
            finish();
        }
    }
}


