package com.rmtd.melo.customviewpager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    MyViewPager mViewPage;
    int[] imageIds = new int[]{
            R.drawable.pic_0,
            R.drawable.pic_1,
            R.drawable.pic_2,
            R.drawable.pic_3,
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mViewPage = (MyViewPager) findViewById(R.id.myviewpager);

        //给自定义ViewPage添加孩子组件
        for (int i = 0; i < imageIds.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(imageIds[i]);
            mViewPage.addView(imageView);
        }

    }
}
