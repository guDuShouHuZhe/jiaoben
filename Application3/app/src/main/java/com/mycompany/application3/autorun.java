package com.mycompany.application3;


    import android.content.*;
    import android.widget.*;

    public class autorun extends BroadcastReceiver
    {

        @Override
        public void onReceive(Context p1, Intent p2)
        {   
            // TODO: Implement this method
            if (p2.getAction()==Intent.ACTION_BOOT_COMPLETED) {
                Intent intent = new Intent();
                intent.setClass(p1, qdactivity.class);// 开机后指定要执行程序的界面文件
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //Toast.makeText(p1,"fggg",5000).show();
                p1.startActivity(intent);
                FloatWindowService.isStart=true;
            }
        }

    }
