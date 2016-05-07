package lib.homhomlib.gpw;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import lib.lhh.fiv.library.FrescoZoomImageView;

/**
 * Created by Linhh on 16/5/6.
 */
public class GridPhotoView extends RelativeLayout {

    private FrescoZoomImageView mPic;
    private OnClickListener mListener;
    public GridPhotoView(Context context) {
        super(context);
    }

    public GridPhotoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GridPhotoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setViewClickListener(OnClickListener l){
        mListener = l;
        if(mPic != null){
            mPic.setOnDraweeClickListener(mListener);
        }
    }

    public void performViewClick(){
        if(mListener != null){
            mListener.onClick(mPic);
        }
    }

    /**
     * 添加布局
     * @param context
     */
    public void initView(Context context, CommonBean item){
        View view = View.inflate(context, R.layout.photo_view_item, null);
        this.addView(view,new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mPic = (FrescoZoomImageView) view.findViewById(R.id.pic);
        String uri = ((GridPhotoItemBean)item.mObject).mImgUrl;
        mPic.loadView(item.mThumbnail, R.drawable.default_color_bg);
    }
}