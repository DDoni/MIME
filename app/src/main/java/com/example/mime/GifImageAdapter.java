package com.example.mime;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;

public class GifImageAdapter extends BaseAdapter {

    private Context mContext;
    int count;
    DisplayMetrics mMetrics;
    int mCellLayout;
    LayoutInflater mLinflater;

    static class ImageViewHolder {
        static ImageView ivImage;
        static CheckBox chk_select;


    }

    public GifImageAdapter(Context c, DisplayMetrics m, int cellLayout) {
        // TODO Auto-generated constructor stub
        this.mContext = c;
        mMetrics = new DisplayMetrics();
        this.mMetrics = m;
        this.mCellLayout = cellLayout;
        mLinflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        count = GifCamera.image_temp.size();

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
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        int rowWidth = (mMetrics.widthPixels) / 3 - 30;
        ImageViewHolder holder;

        if (convertView == null) {
            convertView = mLinflater.inflate(mCellLayout, null);
            holder = new ImageViewHolder();
            holder.ivImage = (ImageView) convertView.findViewById(R.id.thumbImage);
            holder.chk_select = (CheckBox) convertView.findViewById(R.id.itemCheckBox);

            convertView.setLayoutParams(new GridView.LayoutParams(rowWidth, rowWidth));

            holder.ivImage.setScaleType(ImageView.ScaleType.FIT_XY);
            if (position % 3 == 0)
                holder.ivImage.setPadding(15, 1, -7, 1);
            else if (position % 3 == 1)
                holder.ivImage.setPadding(10, 1, 0, 1);
            else if (position % 3 == 2)
                holder.ivImage.setPadding(10, 1, 0, 1);

            convertView.setTag(holder);

        } else
            holder = (ImageViewHolder) convertView.getTag();

        holder.ivImage.setImageBitmap(GifCamera.image_temp.get(position));
        holder.ivImage.invalidate();

        holder.chk_select.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                EditPics.drawble_temp_flag.add(position, !EditPics.drawble_temp_flag.get(position));
                EditPics.drawble_temp_flag.remove(position + 1);
                EditPics.drawble_temp_rev_flag.add(EditPics.drawble_temp_rev_flag.size() - 1 - position, !EditPics.drawble_temp_rev_flag.get(EditPics.drawble_temp_rev_flag.size() - 1 - position));
                EditPics.drawble_temp_rev_flag.remove(EditPics.drawble_temp_rev_flag.size() - 1 - position);

                if (EditPics.rev_Check.isChecked() == false) {
                    EditPics.aniSetting(EditPics.drawble_temp, EditPics.drawble_temp_flag, EditPics.speed_temp);
                } else {
                    EditPics.aniSetting(EditPics.drawble_temp_rev, EditPics.drawble_temp_rev_flag, EditPics.speed_temp);
                }
                EditPics.pick_Cnt.setText("Pick : " + EditPics.ani.getNumberOfFrames());
            }
        });

        return convertView;
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

            }
        }
        return bitmap;
    }
}
