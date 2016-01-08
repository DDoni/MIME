package com.example.mime;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * Created by 돈영 on 2015-11-24.
 */
public class ProjectInfo_GifImageAdapter extends BaseAdapter {

    private Context mContext;
    int count;
    DisplayMetrics mMetrics;

    ImageView imageView;

    public ProjectInfo_GifImageAdapter(Context c, DisplayMetrics m) {
        // TODO Auto-generated constructor stub
        this.mContext = c;
        mMetrics = new DisplayMetrics();
        this.mMetrics = m;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
//        count = GifCamera.test.size();
        return count;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return GifCamera.image_temp.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        int rowWidth = (mMetrics.widthPixels) / 3;

        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setAdjustViewBounds(true);
            imageView.setLayoutParams(new GridView.LayoutParams(rowWidth - 10, rowWidth));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setPadding(10, 1, 10, 1);
        } else
            imageView = (ImageView) convertView;

//        imageView.setImageBitmap(rotate(GifCamera.test.get(position), 90));

        return imageView;
    }

    static public Bitmap rotate(Bitmap bitmap, int degrees) {
        if (degrees != 0 && bitmap != null) {
            Matrix m = new Matrix();
            m.setRotate(degrees);
            try {
                Bitmap converted = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
                if (bitmap != converted) {
                    bitmap = null;
                    bitmap = converted;
                    converted = null;
                }
            } catch (OutOfMemoryError ex) {
                 System.out.println("pjinfo_gifAdapter oom");
            }
        }
        return bitmap;
    }

}
