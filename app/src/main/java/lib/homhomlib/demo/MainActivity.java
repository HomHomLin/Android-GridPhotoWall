package lib.homhomlib.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

import lib.homhomlib.gpw.CommonBean;
import lib.homhomlib.gpw.GridPhotoItemBean;
import lib.homhomlib.gpw.GridPhotoWall;
import lib.homhomlib.gpw.GridPhotoWallUtil;
import lib.homhomlib.gpw.GridPhotoWallViewPager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GridPhotoWall wall = (GridPhotoWall)findViewById(R.id.wall);
        GridPhotoWallViewPager vp = (GridPhotoWallViewPager)findViewById(R.id.expand_pager);
        ArrayList<CommonBean> list = new ArrayList<>();
        for(int i = 0 ; i < 9; i ++){
            CommonBean commonBean = new CommonBean();
            commonBean.mThumbnail = "https://avatars1.githubusercontent.com/u/8758713?v=3&s=460";
            GridPhotoItemBean bean = new GridPhotoItemBean();
            bean.mImgHeight = 100;
            bean.mImgWidth = 500;
            bean.mImgUrl = "https://avatars1.githubusercontent.com/u/8758713?v=3&s=460";
            commonBean.mObject = bean;
            list.add(commonBean);
        }
//        CommonBean commonBean = new CommonBean();
//        commonBean.mThumbnail = "https://avatars1.githubusercontent.com/u/8758713?v=3&s=460";
//        GridPhotoItemBean bean = new GridPhotoItemBean();
//        bean.mImgHeight = 100;
//        bean.mImgWidth = 500;
//        bean.mImgUrl = "https://avatars1.githubusercontent.com/u/8758713?v=3&s=460";
//        commonBean.mObject = bean;
//        list.add(commonBean);
        wall.initView(this,list,findViewById(R.id.parent),vp, GridPhotoWallUtil.getScreenSize(this).x);
    }
}
