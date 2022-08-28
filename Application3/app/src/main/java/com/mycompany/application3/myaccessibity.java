package com.mycompany.application3;
import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Path;
import android.graphics.Rect;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import com.googlecode.tesseract.android.ResultIterator;
import com.googlecode.tesseract.android.TessBaseAPI;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.graphics.Point;

public class myaccessibity extends AccessibilityService {   /* 无障碍服务
     * 当手动打开无障碍权限时
     * 系统会自动调用startService
     * 并且会先走application类
     */
    //public static Boolean isaccessi=false;
    //public static String s="null";
    public static volatile MediaProjection mMediaProjection;
    public static volatile ImageReader imageReader=null;
    public static volatile boolean subThread=false;

    public static myaccessibity myac;
    public   int w,h,dpi;

    public static volatile ExecutorService es;
    public static VirtualDisplay vd;
    private volatile String activityName="";
    private volatile String activityName_run="";
    //private boolean state=false;//选项3的程序控制语句

    public  my_app myapp;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //s=intent.getStringExtra("app");


        return super.onStartCommand(intent, flags, startId);

    }
    @Override
    protected void onServiceConnected() {

        /*
         AccessibilityServiceInfo info = getServiceInfo();  
         info.packageNames=new  String[]{
         "com.ss.android.ugc.aweme.lite",
         "com.kuaishou.nebula"
         };*/
        //m = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
        // setServiceInfo(info);  

        myapp = (my_app)getApplicationContext();
        w = myapp.w;
        h = myapp.h;
        dpi = myapp.dpi;
        myac = this;

        super.onServiceConnected();
    }



    public  void startApp(final int[] app, final int[] app_spinner, final long time) {
        es = Executors.newSingleThreadExecutor();
        es.execute(new Runnable() {

                @Override
                public void run() {
                    pHand("app", "∧", 7);
                    subThread = false;
                    //System.nanoTime()//获取纳秒精度相当高
                    //此时间是从1970.1.1.0.0.0.0开始
                    //因此要对24小时的毫秒数取余得到当天的时间毫秒数
                    //还需要加上8小时转变北京时间，因为获取的是格林威治时间
                    //与北京时区相差8小时
                    while (time != -1) {
                        long  sys_time=sys_time = (System.currentTimeMillis() + (8 * 60 * 60 * 1000)) % (24 * 60 * 60 * 1000);
                        if (time <= sys_time) {
                            break;
                        }
                    }
                    try {
                        if (app[0] == 1) {
                            activityName_run = "com.yxcorp.gifshow.HomeActivity";
                            //包名"com.kuaishou.nebula";
                            // while(subThread);
                            if (!activityName.equals(activityName_run)) {
                                if (imageReader == null)
                                    Thread.sleep(500);//此处延时为等待imagareader创建完毕否则getbitmap返回null
                                ArrayList<Integer> xy=find_xy(getBitmap(imageReader),
                                                              871, 248, -221919,
                                                              906, 292, -1114119,
                                                              966, 323, -808005);
                                if (xy.size() >= 2) {
                                    //pHand("app",xy.get(0)+","+xy.get(1)+","+xy.size(),2);
                                    myac.mygesture(false, 100, xy.get(0), xy.get(1), xy.get(0), xy.get(1));
                                    xy.clear();
                                    Thread.sleep(10000);
                                } //else {pHand("app", "单前页面未找到图标" + pkgName, 2);}

                            }
                            //if (activityName.equals(activityName_run)) {

							switch (app_spinner[0]) {

								case 0:
									break;
								case 1 : case 2: case 3:
									long sys_time=System.currentTimeMillis();
									myac.mygesture(true, 100, myapp.w / 4, myapp.h - 40, myapp.w / 4, myapp.h - 40);  
									Thread.sleep(5000);//1次返回
									myac.mygesture(true, 100, myapp.w / 4, myapp.h - 40, myapp.w / 4, myapp.h - 40);  
									Thread.sleep(3000);//2次返回
									myac.mygesture(true, 100, 108, 1752, 108, 1752);  
									Thread.sleep(3000);//首页
									myac.mygesture(true, 100, 603, 110, 603, 110);  
									Thread.sleep(3000);//发现
									myac.mygesture(true, 100, 762, 1754, 762, 1754);  
									Thread.sleep(10000);//红包
									myac.mygesture(false, 100, myapp.w / 4, myapp.h - 40, myapp.w / 4, myapp.h - 40);  
									Thread.sleep(3000);//返回                       

									int[] pix=getColor(getBitmap(imageReader), myapp.w / 2, myapp.h / 2, myapp.w / 3, myapp.h / 3, myapp.w / 4, myapp.h / 4);    
									while (true) {                                                                       
										if (app_spinner[0] != 4 && System.currentTimeMillis() - sys_time >= app_spinner[0] * 3600 * 1000) {
											break;
										}

										int i=0;
										int[] p;
										int x1=myrandom(myapp.w / 2, myapp.w / 2 + 100);
										int y1=myrandom(myapp.h / 2 - 50, myapp.h / 2 + 500);
										int x2=myrandom(myapp.w / 2, myapp.w / 2 + 100);
										int y2;
										if (y1 > myapp.h / 2) { y2 = y1 - myrandom(300, 500);
										} else {y2 = y1 + myrandom(300, 500);}
										//int t=10*Math.abs(y2-y1)/300;

										myac.mygesture(true, myrandom(10, 12), x1, y1, x2, y2);
										Thread.sleep(2000);
										Bitmap bit=getBitmap(imageReader);

										if (find_xy(bit,
													955, 478, -47804,
													969, 487, -506299,
													985, 513, -125417
													).size() >= 2) {    
											Thread.sleep(myrandom(10000, 15000));
											//xy.clear();
										} else if (find_xy(bit,
														   987, 469, -150519,
														   972, 488, -7397,
														   986, 508, -96255

														   ).size() >= 2) {
											Thread.sleep(myrandom(10000, 15000));
										}
										p = pix = getColor(getBitmap(imageReader), myapp.w / 2, myapp.h / 2, myapp.w / 3, myapp.h / 3, myapp.w / 4, myapp.h / 4);
										if (Arrays.equals(pix, p)) {
											i = i + 1;
										} else {
											pix = p.clone();
											i = 0;
										}            
										if (i == 3)break; //{ pHand("app", "页面未滑动", 2);es = null;                                                                                         
									}   
									break;
							}
                            //}
                        }//app1
                        if (app[1] == 1) {
                            //String pkgName = "com.ss.android.ugc.aweme.lite";
                        }    
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        Thread.currentThread().interrupt();

                    } finally {
                        pHand("app", "运行完毕", 2);
                        pHand("app", "X", 7);
                        es = null;
                    }                                   



                }


            });

		/* rrentTimeMillis()+(8*60*60*1000))%(24*60*60*1000);
         if(time<sys_time){//pdd的判定不能是等于        
         /*  Looper.prepare();
         imageReader.setOnImageAvailableListener(new  ImageReader.OnImageAvailableListener(){

         @Override
         public void onImageAvailable(ImageReader p1) {

         {state=true;Looper.myLooper().quit(); }
         }                                                                           
         },null);
         Looper.loop();*/                                                        
        /* while(true){                           
         Bitmap b=getBitmap(imageReader);//获取位图时，
         //当画面静止时不一定为空，因为沉侵式状态栏
         //的存在，可能会因电量条重绘，只是状态栏重绘的时候是
         //透明不可见的，但仍然会往surface写入数据     
         }*/
    }
    public static void pHand(String app, String text, int what) {
        if (FloatWindowService.hand != null) {
            Message msg=FloatWindowService.hand.obtainMessage();
            //Message msg=new Message();
            Bundle bd=new Bundle();
            bd.putString(app, text);
            msg.setData(bd);
            msg.what = what;
            FloatWindowService.hand.sendMessage(msg);
        } 
    }
    @Override
    public void onAccessibilityEvent(AccessibilityEvent p1) {
        int eventType = p1.getEventType();
        switch (eventType) {
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:


				
				if (p1.getPackageName() != null && FloatWindowService.floatlayout != null && FloatWindowService.setc3switch) {
					
					String str=p1.getPackageName().toString();
					String  group1=str;
					pHand("app", "包名:" + p1.getPackageName().toString(), 4);
					Pattern pattern = Pattern.compile("\\.([0-9a-zA-Z]+$)");
					Matcher matcher = pattern.matcher(str);
					if (matcher.find(1))group1 = "···" + matcher.group(1); 
					
                    ComponentName componentName = new ComponentName(
						str, p1.getClassName().toString());
					try {
						activityName = getPackageManager().getActivityInfo(componentName, 0).toString();
						if (activityName != null) {
							activityName = activityName.substring(activityName.indexOf(" "), activityName.indexOf("}"));
							activityName = activityName.replace(" ", "");//替换所有空格

							// Toast.makeText(getApplication(), activityName, 5000).show();
							if (FloatWindowService.floatlayout != null && FloatWindowService.setc3switch) {

								//String def=str.replace("\\.[0-9a-zA-Z]+$","……");
								pHand("app", "activity:" + group1 + "包下" + activityName, 4);
							}
						}

					} catch (PackageManager.NameNotFoundException e) {
						e.printStackTrace();
						if (FloatWindowService.floatlayout != null && FloatWindowService.setc3switch)
							pHand("app", "activity:" + group1+ "包下未找到activity", 4);
					}
					if (p1.getClassName() == null) {
                        if (FloatWindowService.floatlayout != null && FloatWindowService.setc3switch) {
                            pHand("app", "类名:" + group1+ "包下类名获取为null", 4);}                      
                    }
                }
                break;

            case AccessibilityEvent.TYPE_VIEW_CLICKED:
                if (p1.getPackageName() != null
                    && FloatWindowService.setc != null
                    && !p1.getPackageName().toString().equals("com.mycompany.application3")             
                    && FloatWindowService.setc.getParent() != null
                    && FloatWindowService.params2.flags == WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL) {
                    
                    FloatWindowService.params2.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM  ;
                    FloatWindowService.manager.updateViewLayout(FloatWindowService.setc, FloatWindowService.params2);

                }
                break;
            case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:
                break;  
            case AccessibilityEvent.TYPE_VIEW_LONG_CLICKED:
                break;
            case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:
                break;
        }



    }

    @Override
    public void onInterrupt() {
        //disableSelf();
        //isaccessi=false;
        // TODO: Implement this method
    }//当无障碍中断时调用

    @Override
    public void onDestroy() {
        //isaccessi=false;
        super.onDestroy();
        // es.shutdownNow();
    }

    public  void mygesture(boolean b,  int t, int x1, int y1, int x2, int y2) throws InterruptedException {

        if (subThread && es != null) {
            synchronized (es) { es.wait();}
        }
        if (!activityName.equals(activityName_run) &&
			es != null && b == true) {//b为true时判断当前activity
            es.shutdownNow();
            es = null;
            pHand("app", "当前页面已切换", 2);
            pHand("app", "X", 7);
            return;
        }
        Path path = new Path();
        path.moveTo(x1, y1);
        path.lineTo(x2, y2);
        final GestureDescription.StrokeDescription strokeDescription 
            = new GestureDescription.StrokeDescription(path, 0, t);
        dispatchGesture(
            new GestureDescription.Builder().addStroke(strokeDescription).build(), 
            new AccessibilityService.GestureResultCallback() {
                @Override
                public void onCompleted(GestureDescription gestureDescription) {
                    super.onCompleted(gestureDescription);

                }

                @Override
                public void onCancelled(GestureDescription gestureDescription) {
                    super.onCancelled(gestureDescription);

                }
            }, null);
    }
    //param:flag=0 返回退出应用包名，1返回当前应用包名
    public static String getAppString(int flag) {
        UsageStatsManager m = (UsageStatsManager) myac.getSystemService(Context.USAGE_STATS_SERVICE);
        // setServiceInfo(info); 
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP &&
            m != null && flag < 2 && flag >= 0) {
            long endTime = System.currentTimeMillis();
            long beginTime = endTime - 10000;
            String result = "";
            if (flag == 1) {
                UsageEvents.Event event = new UsageEvents.Event();
                UsageEvents usageEvents = m.queryEvents(beginTime, endTime);
                while (usageEvents.hasNextEvent()) {
                    usageEvents.getNextEvent(event);
                    if (event.getEventType() == UsageEvents.Event.MOVE_TO_BACKGROUND) {
                        result = event.getPackageName();
                    }
                }
                if (!android.text.TextUtils.isEmpty(result)) {
                    return result;}          
            }
            if (flag == 0) {//获取10秒之内的应用数据
                List<UsageStats> stats = m.queryUsageStats(UsageStatsManager.INTERVAL_BEST, endTime - 10 * 1000, endTime);
                if ((stats != null) && (!stats.isEmpty())) {
                    int j = 0;
                    for (int i = 0; i < stats.size(); i++) {
                        if (stats.get(i).getLastTimeUsed() > stats.get(j).getLastTimeUsed()) {
                            j = i;
                        }
                    }                                            
                    result = stats.get(j).getPackageName();
                    if (!android.text.TextUtils.isEmpty(result)) {
                        return result;}
                }
            }
        }
        return "空";
    }
    public static Bitmap binarymap(Bitmap bitmap, int style, int rgb, int threshold) {

		if (style > 2)return bitmap;
		int width = bitmap.getWidth(); 
		int height = bitmap.getHeight(); 
		//创建二值化图像 
		//Bitmap binarymap = null; 
		//binarymap = bitmap.copy(Bitmap.Config.ARGB_8888, true); 
		//依次循环，对图像的像素进行处理 
		for (int i = 0; i < width; i++) { 
			for (int j = 0; j < height; j++) { 
				//得到当前像素的值 
				int col = bitmap.getPixel(i, j); 
				//得到alpha通道的值 
				int alpha = col & 0xFF000000; 
				//得到图像的像素RGB的值 
				int red = (col & 0x00FF0000) >> 16; 
				int green = (col & 0x0000FF00) >> 8; 
				int blue = (col & 0x000000FF); 
				int gray;
				int newColor;

				switch (style) {
					case 0:             
                        //float mean=((float)(red+green+blue))/3;//rgb通道平均值
                        //对图像进行二值化处理 
                        //int gray=(int)(Math.abs(mean-red)+Math.abs(mean-green)+Math.abs(mean-blue));

                        if (red == green && red == blue && red >= threshold) {
							gray = 255;
                        } else gray = 0;
                        //if (gray <= threshold&&mean>127) { 
                        //   gray = 0; 
                        //} else gray = 255;
                        newColor = alpha | (gray << 16) | (gray << 8) | gray; 
                        //设置新图像的当前像素值 
                        bitmap.setPixel(i, j, newColor); 
                        break;

					case 1:
                        // 用公式X = 0.3×R+0.59×G+0.11×B计算出X代替原来的RGB 
                        gray = (int) ((float) red * 0.3 + (float) green * 0.59 + (float) blue * 0.11);
                        if (gray >= threshold) {
							gray = 255;
                        } else gray = 0;
                        newColor = alpha | (gray << 16) | (gray << 8) | gray; 

                        bitmap.setPixel(i, j, newColor); 
                        break;  

					case 2:
                        if (col == rgb) {
							gray = 255;
                        } else gray = 0;
                        newColor = alpha | (gray << 16) | (gray << 8) | gray; 
                        bitmap.setPixel(i, j, newColor); 
                        break;
                        //default: break;
				} 
				// 新的ARGB 
				//int newColor = alpha | (gray << 16) | (gray << 8) | gray; 
				//设置新图像的当前像素值 
				//bitmap.setPixel(i, j, newColor); 
			} 
		} 

        return bitmap;
	}
    //获取text所在位置的rect[]
    public static ArrayList<Rect> getRect(Context c, Bitmap bitmap, String text) {

        String path=c.getExternalFilesDir(null).getPath() + "/";
        TessBaseAPI tessBaseAPI = new TessBaseAPI();
        tessBaseAPI.init(path , "chi_sim");  
        tessBaseAPI.setPageSegMode(TessBaseAPI.PageSegMode.PSM_SINGLE_BLOCK_VERT_TEXT);
        tessBaseAPI.setImage(bitmap);          
        tessBaseAPI.getUTF8Text();  

        ResultIterator iterator=tessBaseAPI.getResultIterator();
        iterator.begin();
		ArrayList<Rect>  r=  new ArrayList<>();
        
        String s,sub_text;
        int text_bottom=-1000;//乱填的尽量避开实际坐标
        int lengtch=0;
        
        do{
            s = iterator.getUTF8Text(TessBaseAPI.PageIteratorLevel.RIL_WORD);
            sub_text = String.valueOf(text.charAt(lengtch));//char转string才能equals
            if (s.equals(sub_text)) {
                if (lengtch == 0)text_bottom = iterator.getBoundingRect(TessBaseAPI.PageIteratorLevel.RIL_WORD).bottom;
                lengtch = lengtch + 1;                 
            } else lengtch = 0;         
            if (lengtch == text.length()) {
				Rect rect= iterator.getBoundingRect(TessBaseAPI.PageIteratorLevel.RIL_WORD);
                int x=rect.bottom; //iterator.getBoundingRect(TessBaseAPI.PageIteratorLevel.RIL_WORD).bottom;
                if (x <= text_bottom + 10 && x >= text_bottom - 10) {//判断最后一个字符是否在一个水平                  			                   					
					r.add(rect);
                }
            }
        }while(iterator.next(TessBaseAPI.PageIteratorLevel.RIL_WORD));
        iterator.delete();
        tessBaseAPI.recycle();
        return r;
	}
    public static Bitmap getBitmap(ImageReader imageReader) throws InterruptedException  {
        Thread.sleep(20);
        //if(imageReader==null){return null;}
        //请求最新图像
        Image image = imageReader.acquireLatestImage();
        if (image == null) {  return null;}

        int width = image.getWidth();
        int height = image.getHeight();
        //获取图像位面，即像素矩阵
        //一个像素由三原色组成，因此不同编码格式plane个数也不同
        final Image.Plane[] planes = image.getPlanes();
        //复制缓存
        final ByteBuffer buffer = planes[0].getBuffer();
        //像素跨度  为1时代表没有间隔两个相临像素点是连续存储
        //即某个分量像素占据的字节宽度(这里指的是plans[0-2],依次是y,u,v分量)
        int pixelStride = planes[0].getPixelStride();
        //行跨度 即一行共多少字节
        int rowStride = planes[0].getRowStride();
        //行填充 即一行填充多少字节
        int rowPadding = rowStride - pixelStride * width;
        //创建空位图
        Bitmap bitmap = Bitmap.createBitmap(width + rowPadding / pixelStride, height, Bitmap.Config.ARGB_8888);
        //拷贝缓存
        bitmap.copyPixelsFromBuffer(buffer);
        //释放资源,否则将无法请求下一个image
        image.close();
        //位图剪裁
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height);

        return bitmap;

    }
    public static int[] getColor(Bitmap bit, int... x) {
        if (x.length == 0 || x.length % 2 != 0)
            throw new IllegalArgumentException("int[]实参长度必须为偶数");
        if (bit == null)
        //throw new IllegalArgumentException("Bitmap为空对象");
            return null;
        int sum = x.length / 2; 
        int a[]=new int[sum];

        for (int i = 1; i <=  sum; i++) { 

            a[i - 1] = bit.getPixel(x[i * 2 - 2], x[i * 2 - 1]);
        } 
        return a;
    }

    public static int myrandom(int a, int b) {
        return (int)(a + Math.random() * (b - a + 1));
    }

    public static ArrayList<Integer> find_xy(Bitmap b, int... color) {
        if (color.length == 0 || color.length % 3 != 0)
            throw new IllegalArgumentException("int[]实参长度必须为3倍数");
        if (b == null)
            throw new IllegalArgumentException("Bitmap为空对象");
        /*for(int i=1;i<color.length/3;i++){
         for(int j=i;j<color.length/3;j++){
         if(color[3*i-2]>color[3*(j+1)-2]){
         int tmp1=color[3*(j+1)-2];
         color[3*(j+1)-2]=color[3*i-2];
         color[3*i-2]=tmp1;}}}
         */ //y坐标排序由小到大
        int bitmap_w=b.getWidth();
        int bitmap_h=b.getHeight();
        int[] bitmap_pixels=new int[bitmap_w * bitmap_h];
        b.getPixels(bitmap_pixels, 0, bitmap_w, 0, 0, bitmap_w, bitmap_h);
        ArrayList<Integer> xy = new ArrayList<Integer>();
        int le=0;
        int ri=0;
        int tom=0;
        int top=0;
        for (int i=1;i < color.length / 3;i++) {
            int tmp1=color[0] - color[i * 3];
            int tmp2=color[i * 3] - color[0];
            int tmp3=color[i * 3 + 1] - color[1];
            int tmp4=color[1] - color[i * 3 + 1];
            if (tmp1 > le) {le = tmp1;}
            if (tmp2 > ri) {ri = tmp2;}
            if (tmp3 > tom) {tom = tmp3;}
            if (tmp4 > top) {top = tmp4;}
        }//xy坐标的左右，底部顶部极值
        for (int y=0;y <= bitmap_h - tom - top;y++) {
            for (int x=0;x <= bitmap_w - le - ri;x++) {
                for (int kk=0;kk < color.length / 3;kk++) {  
                    int yy=y + color[3 * kk + 1] - color[1];//x坐标偏移量
                    int xx=x + color[3 * kk] - color[0];  //y坐标偏移量
                    if (color[(kk + 1) * 3 - 1] == bitmap_pixels[(yy + top) * bitmap_w + xx + le]) {
                        if (kk == color.length / 3 - 1) {
                            xy.add(x + le);
                            xy.add(y + top);

                        }
                    } else break;
                }
            }
        }//将一维数组转换成二维屏幕坐标并初始化动态数组       
        return xy;
    }

}

  

