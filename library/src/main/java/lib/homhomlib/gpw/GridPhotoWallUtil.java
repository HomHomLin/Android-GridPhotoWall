package lib.homhomlib.gpw;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by Linhh on 16/5/6.
 */
public class GridPhotoWallUtil {
    @SuppressLint("NewApi")
    public static Point getScreenSize(Context context){
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point pt = new Point();
        if(Build.VERSION.SDK_INT < 13){
            pt.x = display.getWidth();
            pt.y = display.getHeight();
        }else{
            display.getSize(pt);
        }

        return pt;
    }

    /**
     * 获取当前分辨率下指定单位对应的像素大小（根据设备信息）
     * px,dip,sp -> px
     *
     * Paint.setTextSize()单位为px
     *
     * @param unit  TypedValue.COMPLEX_UNIT_*
     * @param size
     * @return
     */
    public static float getRawSize(int unit, float size, Context context) {
        Resources r;
        r = context.getResources();
        return TypedValue.applyDimension(unit, size, r.getDisplayMetrics());
    }
}
