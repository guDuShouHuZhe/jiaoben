package com.mycompany.application3;


import android.graphics.Rect;
import android.text.Layout;
import android.widget.TextView;
import android.text.TextWatcher;

public class TextViewUtils
{
    /**
     获取TextView某一个字符的坐标位置
     @return 返回的是相对坐标
     @parms tv
     @parms index 字符索引
     */
    /*public static Rect getTextViewSelectionRect(TextView tv, int index) {
        Layout layout = tv.getLayout();
        Rect bound = new Rect();
        int line = layout.getLineForOffset(index);
        layout.getLineBounds(line, bound);
        int yAxisBottom = bound.bottom;//字符底部y坐标
        int yAxisTop = bound.top;//字符顶部y坐标
        int xAxisLeft = (int) layout.getPrimaryHorizontal(index);//字符左边x坐标
        int xAxisRight = (int) layout.getSecondaryHorizontal(index);//字符右边x坐标
        //xAxisRight 位置获取后发现与字符左边x坐标相等，如知道原因请告之。暂时加上字符宽度应对。
        if (xAxisLeft == xAxisRight)
        {
            String s = tv.getText().toString().substring(index, index + 1);//当前字符
            xAxisRight = xAxisRight + (int) tv.getPaint().measureText(s);//加上字符宽度
        }
        //int tvTop=tv.getScrollY();//tv绝对位置*/
       // return new Rect(xAxisLeft, yAxisTop/*+ tvTop*/, xAxisRight, yAxisBottom/*+tvTop */);

   // }

    /**获取TextView触点坐标下的字符
     @param tv tv
     @param x 触点x坐标
     @param y 触点y坐标

     @return 当前字符
     */
   /* public static String getTextViewSelectionByTouch(TextView tv, int x, int y) {
        String s = "";
        for (int i = 0; i < tv.getText().length(); i++)
        {
            Rect rect = getTextViewSelectionRect(tv, i);
            if (x < rect.right && x > rect.left && y < rect.bottom && y > rect.top)
            {
                s = tv.getText().toString().substring(i, i + 1);//当前字符
                break;
            }
        }
        return s;
    }
    public static int getTextViewSelectionIndexByTouch(TextView tv, int x, int y) {
        //String s = "";
        for (int i = 0; i < tv.getText().length(); i++)
        {
            Rect rect = getTextViewSelectionRect(tv, i);
            if (x < rect.right && x > rect.left && y+tv.getScrollY() < rect.bottom && y+tv.getScrollY() > rect.top)
            {
                //s = tv.getText().toString().substring(i, i + 1);//当前字符
                //break;
                return i;
            }
        }
        return -1;
    }*/
    public static int getTextViewSelectionIndexByTouch(TextView textView, int x, int y) {
        Layout layout = textView.getLayout();
        if (layout != null&&textView.getText()!=null) {
            int topVisibleLine = layout.getLineForVertical(y+textView.getScrollY());
            //if(topVisibleLine>textView.getLineCount())return -1;
            //获取位置与指定水平位置最接近的指定行上的字符偏移量
            int offset = layout.getOffsetForHorizontal(topVisibleLine, x);
            //此处有坑 如果textview最后一行有换行符 获取到的offset将
            //超过文本字符长度-1(即越界了，offset从零开始)因此进行越界检查
            if(offset>textView.getText().length()-1)
                return -1;
            //获取指定文本偏移的主要水平位置
            //int offsetX = (int) layout.getPrimaryHorizontal(offset);
            //int offsetY = (int) layout.getp
            //此处主要是考虑文本末尾没有换行符的时候，获取到的offset
            //是文本最后一个字符，并不是实际最接近的字符，因此做了一个
            //x坐标对比，只筛选左边的offset，以达到点击textview空白位置
            //也可以选择的问题
             // if (offsetX > x) {
                 
             //    return layout.getOffsetToLeftOf(offset);
             // } else return offset;
            Rect bound = new Rect();
            //int line = layout.getLineForOffset(offset);
            layout.getLineBounds(topVisibleLine, bound);
            int offsetY = bound.bottom;//字符底部y坐标
            String s = textView. getText().toString().substring(offset, offset + 1);//当前字符
            int offsetX = (int) layout.getPrimaryHorizontal(offset);
            offsetX=offsetX+ (int) textView.getPaint().measureText(s);//加上字符宽度
         
            if (offsetX >x&&offsetY>y+textView.getScrollY()&&!s.equals("\n")) {

                return offset;//  layout.getOffsetToLeftOf(offset);
            }// else return offset;
            
        } 
             
            return -1;
        
    }
}

