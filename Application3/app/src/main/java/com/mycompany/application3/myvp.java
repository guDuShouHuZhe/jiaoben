package com.mycompany.application3;
import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.Nullable;
import android.view.MotionEvent;

public class myvp extends LinearLayout {
    private Context mContext;
    private double preX ;
    private double preY;
    public myvp(Context c){
        
        super(c);
        mContext=c;
    }
    public myvp(Context c, @Nullable AttributeSet a){
       
        super(c,a);
        mContext=c;
    }

    public myvp(Context c, @Nullable AttributeSet a, int defStyleAttr){
       
        super(c,a,defStyleAttr);
        mContext=c;
    }

   @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if(event.getKeyCode()  ==KeyEvent.KEYCODE_BACK&& event.getAction() == KeyEvent.ACTION_UP){
         // FloatWindowService.setc.clearFocus(); 
          FloatWindowService.params2.flags=WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM  ;
          FloatWindowService.manager.updateViewLayout(FloatWindowService.setc,FloatWindowService.params2);
         // 
            //Toast.makeText(my_app.ct, "fghuuuu", Toast.LENGTH_SHORT).show();
            
            //return false;
        }
        //Toast.makeText(my_app.ct, "ghj", Toast.LENGTH_SHORT).show();
        return super.dispatchKeyEvent(event);
    }

    

    @Override
    public boolean dispatchTouchEvent  (MotionEvent ev) {
        if(ev.getAction() == MotionEvent.ACTION_DOWN) {
            preX = ev.getX();
            preY=ev.getY();
        } 
        if(ev.getAction() == MotionEvent.ACTION_MOVE){
            double endX=Math.abs(ev.getX()-preX);
            double endY=Math.abs(ev.getY()-preY);
            if(endX<endY*2) {
               ((my_app)mContext).vp2.setUserInputEnabled(false);
            }   
               
            


        }
        if(ev.getAction() == MotionEvent.ACTION_UP){
           ((my_app)mContext).vp2.setUserInputEnabled(true);
        }
        //FloatWindowService.setcll.setUserInputEnabled(false);
        return super.dispatchTouchEvent(ev);
    
    }//此中断拦截为解决viewpager2与scrollview滑动冲突
     //也可重写dispatchTouchEvent
     //有时须用compile去依赖viewpager2  alpha02版本原因不明
    //新特性setUserInputEnabled 用户输入使能
 
    
}   
