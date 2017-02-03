package com.rmtd.melo.customviewpager;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * Created by melo on 2017/2/3.
 */
public class MyViewPager extends ViewGroup implements View.OnTouchListener{
    private GestureDetector mGestureDetector;
    private Scroller mScroller;
    private static final int FLING_MIN_DISTANCE = 50;
    private static final int FLING_MIN_VELOCITY = 0;
    private String direction;

    public MyViewPager(Context context) {
        super(context, null);
        init();
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyViewPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count=getChildCount();
        for (int i=0;i<count;i++){
            //遍历所有子控件，手动安放每个子控件的位置
            getChildAt(i).layout(i*getWidth(),0,(i+1)*getWidth(),getHeight());
            //左上右下，分别为相对距离
        }

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //事件委托交手势识别器
        mGestureDetector.onTouchEvent(event);
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                //松开手的时候，根据当前位置，来确定下一个页面
                int scrollX=getScrollX();
                //获得当前页面索引下标
                int currentIndex=scrollX/getWidth();
                int offset = scrollX % getWidth();
                if ((offset>getWidth()/4)&&("left").equals(direction)){
                    currentIndex++;
                    Log.i("tag","++");
                }else if((offset>getWidth()/4)&&("right").equals(direction)) {
                    currentIndex--;
                    Log.i("tag","--");
                }
                //处理越界问题
                if(currentIndex > getChildCount()-1){
                    currentIndex = getChildCount()-1;
                }
                if (currentIndex<0){
                    currentIndex = 0;
                }

                Log.i("tag","scrollX:"+String.valueOf(scrollX)
                        +",getWidth:"+getWidth()+",currentIndex:"+currentIndex+",offset:"+offset);
                goCurrentPage(currentIndex);
                break;
            default:
                break;
        }

        return true;
    }






    /**
     * 根据当前
     * @param pageIndex
     *      当前的page页面
     */
    private void goCurrentPage(int pageIndex) {
        //scrollTo(pageIndex*getWidth(),0);

        int dx = pageIndex*getWidth() - getScrollX();

        Log.e("YDL",dx+"-------");

        //参数1：x的起始值；参数2：y的起始值；参数3：x的偏移量；参数4：y的偏移量
        //对于参数3：dx>0往左移动；dx<0往右移动
        mScroller.startScroll(getScrollX(),0,dx,0,Math.abs(dx));//dx绝对值作为时间值，按比例可以实现了匀速移动
        //使用Scroller必须重新刷新界面,不刷新的话不会滑动
        invalidate();
    }


    //Scroller使用调用invalidate();后，会同步调用computeScroll()方法
    @Override
    public void computeScroll() {
        if(mScroller.computeScrollOffset()){
            int currX = mScroller.getCurrX();
            Log.e("YDL",currX+"");
            scrollTo(mScroller.getCurrX(),0);
            //也要刷新界面
            invalidate();
        }
    }

    //添加滑动，利用手势和scoller
    private void init() {
        mGestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
            //手势识别器移动的监听回调。每次移动，都会回调该方法
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                //参数1：起点动作封装；参数2：终点动作封装；参数3：x方向的移动距离；参数4：y方向滑动距离
                scrollBy((int) distanceX, 0);
                return true;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                Log.i("tag","onFling");
                if (e1.getX()-e2.getX() > FLING_MIN_DISTANCE
                        && Math.abs(velocityX) > FLING_MIN_VELOCITY) {
                    // Fling left
                    direction="left";
                    Log.i("tag","left");

                } else if (e2.getX()-e1.getX() > FLING_MIN_DISTANCE
                        && Math.abs(velocityX) > FLING_MIN_VELOCITY) {
                    // Fling right
                    direction="right";
                    Log.i("tag","right");
                }
                return false;
            }
        });

        //初始化Scoller
        mScroller = new Scroller(getContext());
    }
}
