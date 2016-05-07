package lib.homhomlib.gpw;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by Linhh on 16/5/6.
 */
public class GridPhotoWallViewPager extends RelativeLayout implements ViewPager.OnPageChangeListener{

    private View mRootView;

    private ViewPager mViewPager;
    private LinearLayout mTabs;

    private boolean mIsInited = false;

    public GridPhotoWallViewPager(Context context) {
        super(context);
    }

    public GridPhotoWallViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GridPhotoWallViewPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public boolean isInited(){
        return mIsInited;
    }

    public void initView(Context context){
        if(mIsInited){
            return;
        }
        mIsInited = true;
        mRootView = View.inflate(context ,R.layout.view_photoview, null);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);

        this.addView(mRootView, layoutParams);

        mViewPager = (ViewPager) mRootView.findViewById(R.id.vp_photoview);
        mViewPager.addOnPageChangeListener(this);
        mTabs = (LinearLayout)mRootView.findViewById(R.id.ll_tabs);
    }

    public void setCurrentItem(int item) {
        mViewPager.setCurrentItem(item);
        showDot(item);
    }

    public PagerAdapter getAdapter() {
        return mViewPager.getAdapter();
    }

    public void showDot(int item){
        for(int i = 0 ; i < mTabs.getChildCount(); i ++) {
            if(i == item) {
                mTabs.getChildAt(i).setBackgroundResource(R.drawable.ad_radio_mark);
            }else{
                mTabs.getChildAt(i).setBackgroundResource(R.drawable.ad_radio_unmark);
            }
        }
    }

    public void setAdapter(PagerAdapter adapter,Context context) {
        mViewPager.setAdapter(adapter);
        if(mTabs != null && mTabs.getChildCount() > 0){
            mTabs.removeAllViews();
        }
        for(int i = 0 ; i < adapter.getCount(); i ++){
            ImageView rb = new ImageView(context);

            if(i == 0){
                rb.setBackgroundResource(R.drawable.ad_radio_mark);
            }else {
                rb.setBackgroundResource(R.drawable.ad_radio_unmark);
            }
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins((int) GridPhotoWallUtil.getRawSize(TypedValue.COMPLEX_UNIT_DIP, 2, context), 0 , (int)GridPhotoWallUtil.getRawSize(TypedValue.COMPLEX_UNIT_DIP, 2 ,context),0);

            rb.setClickable(false);
            mTabs.addView(rb, i, layoutParams);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for(int i = 0 ; i < mTabs.getChildCount(); i ++) {
            if(i == position) {
                mTabs.getChildAt(i).setBackgroundResource(R.drawable.ad_radio_mark);
            }else{
                mTabs.getChildAt(i).setBackgroundResource(R.drawable.ad_radio_unmark);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
