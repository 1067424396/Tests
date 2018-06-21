package com.example.administrator.test;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.bumptech.glide.Glide;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


public class AutoRollLayout extends FrameLayout {

    protected static final long ROLL_DELAY = 3000;

    private static Handler timer = new Handler();
    private List<? extends IRollItem> items;
    private LinearLayout dotContainer;
    private TextView titleTv;
    private ViewPager viewPager;
    private int startX;
    private int startY;
    private boolean isAuto = false;
    private boolean isMove = false;
    private MainActivity fragment;
    //private PullToRefreshView srl_refresh;


    public AutoRollLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public AutoRollLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoRollLayout(Context context) {
        this(context, null);
    }
    private RelativeLayout mRelativeLayout;
    private void init() {
        View.inflate(getContext(), R.layout.view_arl_layout, this);
        viewPager = (ViewPager) findViewById(R.id.arl_view_pager);
        titleTv = (TextView) findViewById(R.id.arl_text_view);
        dotContainer = (LinearLayout) findViewById(R.id.arl_dot_container);

        viewPager.setPageMargin(dp2px(getContext(), 5));
        viewPager.setOffscreenPageLimit(3);

    }
    /**
     * dp转px
     *
     * @param context
     * @param dpVal
     * @return
     */
    public static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, context.getResources()
                .getDisplayMetrics());
    }
    /**
     * set the duration between two slider changes.
     * @param period
     * @param interpolator
     */
    public void setSliderTransformDuration(int period,Interpolator interpolator){
        try{
            Field mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(viewPager.getContext(),interpolator, period);
            mScroller.set(viewPager,scroller);
        }catch (Exception e){

        }
    }
    public void setAutoRun(boolean isAuto) {
        this.isAuto = isAuto;
        if (isAuto) {
            timer.postDelayed(autoRun, ROLL_DELAY);
        } else {
            timer.removeCallbacks(autoRun);
        }
    }

    public void setItems(List<? extends IRollItem> items) {
        this.items = items;
        // 先移除所有的点
        dotContainer.removeAllViews();
        // 设置文字是空
        titleTv.setText("666");


        if (items == null || items.isEmpty()) {
            return;
        }
        // 刷新viewpager，
        viewPager.setAdapter(adapter);
        // 搞定文字 ,对viewpager设置监听，在监听中感知到页面变化，改变文字arl_dot_container
        viewPager.setOnPageChangeListener(opcl);
        // 搞定点 有多少个item，就往容器中添加多少个点
        // 在监听中改变点的enable（改变背景）
        addDots();

//        dotContainer.getChildAt((Integer.MAX_VALUE / 2 - Integer.MAX_VALUE / 2 % items.size()) % items.size()).setEnabled(true);
//        viewPager.setCurrentItem(Integer.MAX_VALUE / 2 - Integer.MAX_VALUE / 2 % items.size());
        viewPager.setCurrentItem(items.size()*20);

        opcl.onPageSelected(Integer.MAX_VALUE / 2 - Integer.MAX_VALUE / 2 % items.size());
    }

    public int getCurrentItems(){
        return viewPager.getCurrentItem();
    }
    // 外界如果想知道当前显示的页面的角标，就调用此方法
    public int getCurrentItemIndex() {
        return viewPager.getCurrentItem() % items.size();
    }

    public void setPageTransformer(boolean reverseDrawingOrder, ViewPager.PageTransformer transformer){
        viewPager.setPageTransformer(reverseDrawingOrder, transformer);
    }
    private ArrayList<View> dots = new ArrayList<View>();// 显示点的集合
    private void addDots() {
        // 6dp
        int dotWidth = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 6, getResources()
                        .getDisplayMetrics());

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(dotWidth,
                dotWidth);
        lp.setMargins(0, 0, dotWidth, 0);
//
//        for (int i = 0; i < items.size(); i++) {
//            View dot = new View(getContext());
//            dot.setBackgroundResource(R.drawable.hot_dot_selector);
//            dotContainer.addView(dot, lp);
//        }


//        for (int i = 0; i < items.size(); i++) {
//            LinearLayout.LayoutParams margin = new LinearLayout.LayoutParams(
//                    LinearLayout.LayoutParams.WRAP_CONTENT,
//                    LinearLayout.LayoutParams.WRAP_CONTENT);
//            // 设置每个小圆点距离左边的间距
//            margin.setMargins(20, 0, 0, 0);
//            ImageView imageView = new ImageView(getContext());
//            // 设置每个小圆点的宽高
//            imageView.setLayoutParams(new ViewGroup.LayoutParams(10, 10));
//            dots.add(imageView);
//            if (i == 0) {
//                // 默认选中第一张图片
//                dots.get(i).setBackgroundResource(
//                        R.mipmap.page_indicator_focused);
//            } else {
//                // 其他图片都设置未选中状态
//                dots.get(i).setBackgroundResource(
//                        R.mipmap.page_indicator_unfocused);
//            }
//            dotContainer.addView(dots.get(i), margin);
//        }

    }

    private PagerAdapter adapter = new PagerAdapter() {

        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        public int getCount() {
            return items.size() == 1 ? 1 : Integer.MAX_VALUE;
        }

        public Object instantiateItem(ViewGroup container, int position) {
          //  ImageView iv = new ImageView(getContext());
            View view= LayoutInflater.from(getContext()).inflate(R.layout.item_image, null);
            ZQImageViewRoundOval iv= (ZQImageViewRoundOval) view.findViewById(R.id.roundRect);

            TextView tv_title= (TextView) view.findViewById(R.id.tv_title);
            // tScaleType(ScaleType.FIT_XY);
           // ZQImageViewRoundOval iv=new ZQImageViewRoundOval(getContext());
            iv.setType(ZQImageViewRoundOval.TYPE_ROUND);
            iv.setRoundRadius(30);//矩形凹行大小
            final int pos = position;
            tv_title.setText(""+items.get(position % items.size()).getTitle());

            Glide.with(getContext()).load(items.get(position % items.size()).getImageUrl()).into(iv);
            ViewGroup parent = (ViewGroup) iv.getParent();
            if (parent != null) {
                parent.removeAllViews();
            }
            container.addView(iv);
            return iv;
        }

        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    };

    private OnPageChangeListener opcl = new OnPageChangeListener() {

        public void onPageSelected(int position) {
            try {
                if (items == null || items.size() <=position % items.size()) {
                    return;
                }
            }catch (Exception e){}

            titleTv.setText(""+items.get(position % items.size()).getTitle());

            // dotContainer.getChildAt(position).setEnabled(false);
//            for (int i = 0; i < dotContainer.getChildCount(); i++) {
//                if (i == position % items.size()) {
//                    dotContainer.getChildAt(i).setEnabled(false);
//                } else {
//                    dotContainer.getChildAt(i).setEnabled(true);
//                }
//            }
//            for (int i = 0; i < dots.size(); i++) {
//                View view = dots.get(i);
//                if (i == position % dots.size()) {
//                    view.setBackgroundResource(R.mipmap.page_indicator_focused);
//                } else {
//                    view.setBackgroundResource(R.mipmap.page_indicator_unfocused);
//                }
//            }

        }

        public void onPageScrolled(int position, float positionOffset,
                                   int positionOffsetPixels) {

        }

        public void onPageScrollStateChanged(int state) {
        }
    };

    /**
     * 事件分发
     */
    public boolean dispatchTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
            //    srl_refresh.setEnabled(false);
              //  ToastUtils.showToast(fragment.mActivity,"ACTION_DOWN");
                timer.removeCallbacks(autoRun);
                this.isMove = true;
                startX = (int) ev.getX();
                startY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
               // ToastUtils.showToast(fragment.mActivity,"ACTION_MOVE");
                this.isMove = false;
                timer.postDelayed(autoRun, ROLL_DELAY);
                if (Math.abs((ev.getX() - startX)) > Math.abs((ev.getY() - startY)) && Math.abs((ev.getX() - startX)) > 60) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                break;

            case MotionEvent.ACTION_UP:
              //  srl_refresh.setEnabled(true);
              //  ToastUtils.showToast(fragment.mActivity,"ACTION_UP");
                this.isMove = false;
                timer.postDelayed(autoRun, ROLL_DELAY);
                if (Math.abs((ev.getX() - startX)) > Math.abs((ev.getY() - startY)) && Math.abs((ev.getX() - startX)) > 60) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }


    public Runnable autoRun = new Runnable() {

        public void run() {
            timer.removeCallbacks(autoRun);
            if (isAuto && !isMove) {
                int currentItem = viewPager.getCurrentItem();
                viewPager.setCurrentItem(++currentItem);
                opcl.onPageSelected(currentItem);
                timer.postDelayed(this, ROLL_DELAY);
            }
        }
    };

    public void setFragment(MainActivity hotFragment) {
        this.fragment = hotFragment;
    }

//    public void setSrlRefresh(PullToRefreshView srl_refresh) {
//        this.srl_refresh=srl_refresh;
//    }

}
