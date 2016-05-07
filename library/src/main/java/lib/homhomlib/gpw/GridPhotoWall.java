package lib.homhomlib.gpw;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.v4.view.PagerAdapter;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

import java.lang.reflect.Field;
import java.util.ArrayList;

import lib.lhh.fiv.library.FrescoImageView;
import lib.lhh.fiv.library.zoomable.ZoomableDraweeView;

/**
 * Created by linhonghong on 2016/5/5.
 */
public class GridPhotoWall extends LinearLayout{

    private Context mContext;
    public ArrayList<CommonBean> mData;

    public ArrayList<CommonBean> mPhotoList;//照片的list

    private Animator mCurrentAnimator;

    private int mShortAnimationDuration = 300;

    private View mParentView;

    public ArrayList<View> mViewList;
    public int mPicSize = 0;//default
    public GridPhotoWall(Context context) {
        super(context);
    }

    public GridPhotoWall(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void initView(Context context,ArrayList<CommonBean> list,View parent, GridPhotoWallViewPager vp, int size){
        if(this.getChildCount() > 0 ) {
            this.removeAllViews();
        }
        mContext = context;

        viewPager = vp;

        if(size != 0)
            mPicSize = size;

        mData = list;
        if(mData == null && mData.size() == 0){
            return;
        }

        mParentView = parent;

        if(mViewList == null){
            mViewList = new ArrayList<>();
        }
        mViewList.clear();

        if(mPhotoList == null){
            mPhotoList = new ArrayList<>();
        }

        mPhotoList.clear();

        mPosition = 0;

        this.setOrientation(LinearLayout.VERTICAL);

        if(mData.size() == 1){
            //只有一个的大图
            setOneLayout();
        }else if(mData.size() > 4 || mData.size() % 3 == 0){
            //大于4或能被3整除
            setFullLayout();
        }else{
            setTianLayout();
        }
    }

    public CommonBean getItem(int i) {
        return mData == null ? null : mData.get(i);
    }

    public void setOneLayout(){

        CommonBean item = getItem(0);
        int[] size = new int[2];
        if(item == null){
            return;
        }
        FrescoImageView view = (FrescoImageView) LayoutInflater.from(mContext).inflate(R.layout.grid_photo_item, null);

        GridPhotoItemBean bean = (GridPhotoItemBean) item.mObject;
        size = calSize(bean.mImgHeight, bean.mImgWidth);

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(size[0], size[1]);
        loadView(view, item);
        this.addView(view,layoutParams);
    }

    /**
     * 我是一个3列，算法逻辑如果有缺漏，请@linhonghong
     */
    public void setFullLayout(){
        int height = 0;
        int padding = (int)getResources().getDimension(R.dimen.classify_view_list_padding);
        if(mPicSize == 0 ) {
            height = (160 * GridPhotoWallUtil.getScreenSize(mContext).x) / 720;
        }else{
            height = (mPicSize - 2 * padding)/3;
        }
//        int height = (int)getResources().getDimension(R.dimen.news_pic_height);
        int size = (mData.size() / 3 + (mData.size() % 3 == 0 ? 0 : 1));

        for(int i = 0 ; i < size ; i ++){
            int layoutH = height ;
            if(i + 1 < size) {
                layoutH = height + padding;
            }
            RelativeLayout.LayoutParams layoutParams =
                    new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                            layoutH);
            LinearLayout layout = new LinearLayout(mContext);
            layout.setOrientation(LinearLayout.HORIZONTAL);
//            if(i + 1 < size) {
//                layoutParams.bottomMargin = padding;
//            }
            for(int x = i * 3 ; x < ( (i + 1) * 3 > mData.size() ? mData.size() : (i + 1) * 3);  x ++){
                CommonBean item = getItem(x);
                if(item == null){
                    return;
                }
                FrescoImageView view = (FrescoImageView)LayoutInflater.from(mContext).inflate(R.layout.grid_photo_item, null);
                LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(height, height);
                linearParams.rightMargin = padding;
                layout.addView(view, linearParams);
                loadView(view, item);

            }
            this.addView(layout, layoutParams);
        }
    }

    /**
     * 我是一个“田”，算法逻辑如果有缺漏，请@linhonghong
     */
    public void setTianLayout(){
        int height = (160 * GridPhotoWallUtil.getScreenSize(mContext).x) / 720;
//        int height = (int)getResources().getDimension(R.dimen.news_pic_height);
        int padding = (int)getResources().getDimension(R.dimen.classify_view_list_padding);

        for(int i = 0 ; i < mData.size() / 2 ; i ++){
            int layoutH = height ;
            if(i + 1 < mData.size() / 2) {
                layoutH = height + padding;
            }
            RelativeLayout.LayoutParams layoutParams =
                    new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                            layoutH);
            LinearLayout layout = new LinearLayout(mContext);
            layout.setOrientation(LinearLayout.HORIZONTAL);
            for(int x = i * 2 ; x < (i + 1) *2 ;  x ++){
                CommonBean item = getItem(x);
                if(item == null){
                    return;
                }
                FrescoImageView view = (FrescoImageView)LayoutInflater.from(mContext).inflate(R.layout.grid_photo_item, null);
                LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(height, height);
                linearParams.rightMargin = padding;
                layout.addView(view, linearParams);
                loadView(view, item);

            }
            this.addView(layout,layoutParams);
        }
    }

    public int mPosition = 0;

    public void loadView(FrescoImageView view, CommonBean item){
        //图片
        mPhotoList.add(item);
        view.setTag(R.id.tag_position, mPosition);
        mPosition ++;
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = (int) view.getTag(R.id.tag_position);
                zoomImageFromThumb(view, position);
            }
        });
        String url = item.mThumbnail;
        view.loadView(url, R.drawable.default_color_bg);

        mViewList.add(view);
    }

    public View getViewChildPhotoAt(int index) {
        return mViewList.get(index);
    }

    public int[] calSize(int height, int width){
        int[] size = new int[2];

        if(height > width){
            //长图
            size[0] = (GridPhotoWallUtil.getScreenSize(mContext).x * 300) / 720;
            size[1] = (GridPhotoWallUtil.getScreenSize(mContext).y * 400) / 1280;
            return size;
        }else if(height < width){
            //宽图
            size[0] = (GridPhotoWallUtil.getScreenSize(mContext).x * 400) / 720;
            size[1] = (GridPhotoWallUtil.getScreenSize(mContext).y * 300) / 1280;
            return size;
        }
        //正方形
        size[0] = (GridPhotoWallUtil.getScreenSize(mContext).x * 400) / 720;
        size[1] = (GridPhotoWallUtil.getScreenSize(mContext).y * 400) / 1280;
        return size;
    }

    float startScale;
    GridPhotoWallViewPager viewPager;
    Rect startBounds;
    float startScaleFinal;
    private void zoomImageFromThumb(View thumbView, int position) {

        if (mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
        }

//        viewPager = (GridPhotoWallViewPager)((Activity)mContext).findViewById(R.id.image);
        if(!viewPager.isInited()){
            viewPager.initView(mContext);
        }
        viewPager.setAdapter(new SamplePagerAdapter(mContext), mContext);
        viewPager.setCurrentItem(position);

        startBounds = new Rect();
        Point pt = GridPhotoWallUtil.getScreenSize(mContext);
        int barheight = getStatusBarHeight(mContext);
        final Rect finalBounds = new Rect(0, barheight, pt.x, pt.y);
        final Point globalOffset = new Point(0, barheight);

        thumbView.getGlobalVisibleRect(startBounds);
//		((Activity)mContext).findViewById(R.id.container).getGlobalVisibleRect(finalBounds, globalOffset);
//		mGridView.getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        if ((float) finalBounds.width() / finalBounds.height() > (float) startBounds
                .width() / startBounds.height()) {
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        viewPager.setVisibility(View.VISIBLE);

        AnimatorSet animSet = new AnimatorSet();
        animSet.setDuration(1);
        animSet.play(ObjectAnimator.ofFloat(viewPager, "pivotX", 0f))
                .with(ObjectAnimator.ofFloat(viewPager, "pivotY", 0f))
                .with(ObjectAnimator.ofFloat(viewPager, "alpha", 1.0f));
        animSet.start();


        AnimatorSet set = new AnimatorSet();
//		ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(mGridView, "alpha", 1.0f, 0.f);
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(viewPager, "x", startBounds.left, finalBounds.left);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(viewPager, "y", startBounds.top, finalBounds.top);
        ObjectAnimator animatorScaleX = ObjectAnimator.ofFloat(viewPager, "scaleX", startScale, 1f);
        ObjectAnimator animatorScaleY = ObjectAnimator.ofFloat(viewPager, "scaleY", startScale, 1f);

        set.play(animatorX).with(animatorY).with(animatorScaleX).with(animatorScaleY);
        set.setDuration(mShortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {

            public void onAnimationEnd(Animator animation) {
                mCurrentAnimator = null;
            }

            public void onAnimationCancel(Animator animation) {
                mCurrentAnimator = null;
            }
        });
        set.start();
        mCurrentAnimator = set;

        startScaleFinal = startScale;

    }


    private boolean getScaleFinalBounds(int position) {
        View childView = getViewChildPhotoAt(position);

        startBounds = new Rect();

        Point pt = GridPhotoWallUtil.getScreenSize(mContext);
        int barheight = getStatusBarHeight(mContext);
        final Rect finalBounds = new Rect(0, barheight, pt.x, pt.y);
        final Point globalOffset = new Point(0, barheight);

        try {
            childView.getGlobalVisibleRect(startBounds);
        } catch (Exception e) {
            return false;
        }

        Rect parentBounds = new Rect();

        try {
            mParentView.getGlobalVisibleRect(parentBounds);
        } catch (Exception e) {
            return false;
        }

        //吐血
        if(startBounds.top < parentBounds.top){
            return false;
        }

//        int[] loc1 = new int[2];
//        childView.getLocationOnScreen(loc1);
//
//        int[] loc2 = new int[2];
//        mParentView.getLocationOnScreen(loc1);


//		((Activity) mContext).findViewById(R.id.container)
//				.getGlobalVisibleRect(finalBounds, globalOffset);
//		mGridView.getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);


        if ((float) finalBounds.width() / finalBounds.height() > (float) startBounds
                .width() / startBounds.height()) {
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }
        startScaleFinal = startScale;
        return true;
    }

    public class SamplePagerAdapter extends PagerAdapter {

        private Context mContext;

        private GridPhotoView mCurrentView;

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            mCurrentView = (GridPhotoView)object;
        }

        public GridPhotoView getPrimaryItem() {
            return mCurrentView;
        }
        public SamplePagerAdapter(Context context) {
            this.mContext = context;
        }
        @Override
        public int getCount() {
            return mPhotoList == null ? 0 : mPhotoList.size();
        }

        @Override
        public View instantiateItem(ViewGroup container, final int position) {
            GridPhotoView view = new GridPhotoView(mContext);


//            photoView.setImageResource(sDrawables[position]);
            if(mPhotoList == null){
                return view;
            }

            if((position + 1) > mPhotoList.size()){
                return view;
            }
            CommonBean item = mPhotoList.get(position);

            view.initView(mContext, item);

            view.setViewClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mCurrentAnimator != null) {
                        mCurrentAnimator.cancel();
                    }
                    ((ZoomableDraweeView)view).clearZoom();

                    boolean scaleResult = getScaleFinalBounds(position);

                    AnimatorSet as = new AnimatorSet();
//							ObjectAnimator containAlphaAnimator = ObjectAnimator.ofFloat(mGridView, "alpha", 0.f, 1.0f);
                    if (scaleResult) {
                        ObjectAnimator animatorX = ObjectAnimator.ofFloat(viewPager, "x", startBounds.left);
                        ObjectAnimator animatorY = ObjectAnimator.ofFloat(viewPager, "y",  startBounds.top);
                        ObjectAnimator animatorScaleX = ObjectAnimator.ofFloat(viewPager, "scaleX", startScaleFinal);
                        ObjectAnimator animatorScaleY = ObjectAnimator.ofFloat(viewPager, "scaleY", startScaleFinal);

                        as.play(animatorX).with(animatorY).with(animatorScaleX).with(animatorScaleY);
                    }else {

                        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(viewPager, "alpha", 0.1f);
                        as.play(alphaAnimator);
                    }
                    as.setDuration(mShortAnimationDuration);
                    as.setInterpolator(new DecelerateInterpolator());
                    as.addListener(new AnimatorListenerAdapter() {

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            viewPager.clearAnimation();
                            viewPager.setVisibility(View.GONE);
                            mCurrentAnimator = null;
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {
                            viewPager.clearAnimation();
                            viewPager.setVisibility(View.GONE);
                            mCurrentAnimator = null;
                        }
                    });
                    as.start();
                    mCurrentAnimator = as;

                }
            });
            container.addView(view,0);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }

    private static int getStatusBarHeight(Context context){

        Class<?> c = null;
        Object obj = null;
        Field field = null;

        String bar = "status_bar_height";
        int height = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField(bar);
            int x = Integer.parseInt(field.get(obj).toString());
            height = context.getResources().getDimensionPixelSize(x);
        } catch(Exception e1) {
            height = 0;
        }
        height = Math.max(0, height);
        return height;
    }

}


