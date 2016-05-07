package lib.homhomlib.gpw;

/**
 * Created by Linhh on 16/5/6.
 */
public class GridPhotoItemBean{
    public String mImgUrl;
    public int mImgWidth;
    public int mImgHeight;
    public GridPhotoItemBean(){
        mImgUrl = "";
        mImgHeight = 0;
        mImgWidth = 0;
    }

    public void release(){
        mImgUrl = null;
        mImgHeight = 0;
        mImgWidth = 0;
    }
}
