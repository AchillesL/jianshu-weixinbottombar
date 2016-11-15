package com.achillesl.bottombar;

import android.animation.ArgbEvaluator;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ViewPager mViewPager;

    private List<TextView> mTipList = new ArrayList<>();
    //使用List存放ImageView对象
    private List<ImageView> mBottomImageViews = new ArrayList<>();
    //使用List存放ImageView对应的图片资源
    private List<Drawable> mBottomImageDrawables = new ArrayList<>();

    private ArgbEvaluator mArgbEvaluator = new ArgbEvaluator();
    private int mBottomColorSelect;
    private int mBottomColorUnSelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initTipList();
        initImageViewDrawable();

        // TODO: 2016/11/13 设置ViewPager的页面数
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mViewPager.setAdapter(new PagerAdapter() {

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(mTipList.get(position));
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                TextView textView = mTipList.get(position);
                container.addView(textView);
                return textView;
            }

            @Override
            public int getCount() {
                return 4;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }
        });
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            private final int DELAY_TIME = 100;
            private Handler handler = new Handler();

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                changeImageViewDrawable(position,positionOffset);
            }

            @Override
            public void onPageSelected(final int position) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setImageViewSelect(position);
                    }
                },DELAY_TIME);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void changeImageViewDrawable(int position, float positionOffset) {
        ImageView ivFrom = null;
        ImageView ivTo = null;

        Drawable drawableFrom = null;
        Drawable drawableTo = null;

        ivFrom = mBottomImageViews.get(position);
        drawableFrom = mBottomImageDrawables.get(position);

        if (position != mBottomImageDrawables.size() - 1) {
            ivTo = mBottomImageViews.get(position + 1);
            drawableTo = mBottomImageDrawables.get(position + 1);
        } else {
            ivTo = null;
            drawableTo = null;
        }

        if (ivFrom != null) {
            int colorStart = (int) mArgbEvaluator.evaluate(positionOffset, mBottomColorSelect,
                    mBottomColorUnSelect);
            Drawable drawableColorStart = tintDrawable(drawableFrom, ColorStateList.valueOf
                    (colorStart));
            ivFrom.setImageDrawable(drawableColorStart);
        }
        if (ivTo != null) {
            int colorStart = (int) mArgbEvaluator.evaluate(positionOffset, mBottomColorUnSelect,
                    mBottomColorSelect);
            Drawable drawableColorEnd = tintDrawable(drawableTo, ColorStateList.valueOf
                    (colorStart));
            ivTo.setImageDrawable(drawableColorEnd);
        }
    }

    private void initImageViewDrawable() {
        /*Add begin*/
        mBottomColorSelect = ContextCompat.getColor(this, R.color.buttonSelect);
        mBottomColorUnSelect = ContextCompat.getColor(this, R.color.buttonUnSelect);
        /*Add end*/

        mBottomImageViews.add((ImageView) findViewById(R.id.ivOne));
        mBottomImageViews.add((ImageView) findViewById(R.id.ivTwo));
        mBottomImageViews.add((ImageView) findViewById(R.id.ivThree));
        mBottomImageViews.add((ImageView) findViewById(R.id.ivFour));

        mBottomImageDrawables.add(ContextCompat.getDrawable(this,R.drawable.ic_action_alarm).mutate());
        mBottomImageDrawables.add(ContextCompat.getDrawable(this,R.drawable.ic_action_amazon).mutate());
        mBottomImageDrawables.add(ContextCompat.getDrawable(this,R.drawable.ic_action_anchor).mutate());
        mBottomImageDrawables.add(ContextCompat.getDrawable(this,R.drawable.ic_action_android).mutate());

        for (int i = 0; i < mBottomImageViews.size(); i++) {
            ImageView imageView = mBottomImageViews.get(i);
            final int index = i;
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mViewPager.setCurrentItem(index);
                }
            });
        }

        /*Add begin*/
        setImageViewSelect(0);
        /*Add end*/
    }

    private void setImageViewSelect(int selectIndex) {
        for (int index = 0; index < mBottomImageViews.size(); index++) {
            ImageView imageView = mBottomImageViews.get(index);
            Drawable drawable = mBottomImageDrawables.get(index);
            if (index == selectIndex) {
                imageView.setImageDrawable(tintDrawable(drawable, ColorStateList.valueOf
                        (mBottomColorSelect)));
            } else {
                imageView.setImageDrawable(tintDrawable(drawable, ColorStateList.valueOf
                        (mBottomColorUnSelect)));
            }
        }
    }

    public Drawable tintDrawable(Drawable drawable, ColorStateList colors) {
        final Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTintList(wrappedDrawable, colors);
        return wrappedDrawable;
    }

    private void initTipList() {
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams
                .MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        for (int i = 0; i < 4; i++) {
            TextView textView = new TextView(this);
            textView.setText("第" + (i + 1) + "个页面");
            textView.setLayoutParams(layoutParams);
            textView.setGravity(Gravity.CENTER);
            mTipList.add(textView);
        }
    }
}
