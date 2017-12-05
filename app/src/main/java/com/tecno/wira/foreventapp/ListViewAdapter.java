package com.tecno.wira.foreventapp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by wira on 12/5/17.
 */

public class ListViewAdapter extends BaseAdapter

    {
        Context context;

        List<Subject> TempSubjectList;

    public ListViewAdapter(List<Subject> listValue, Context context)
        {
            this.context = context;
            this.TempSubjectList = listValue;
        }

        @Override
        public int getCount()
        {
            return this.TempSubjectList.size();
        }

        @Override
        public Object getItem(int position)
        {
            return this.TempSubjectList.get(position);
        }

        @Override
        public long getItemId(int position)
        {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            ViewItem viewItem = null;

            if(convertView == null)
            {
                viewItem = new ViewItem();

                LayoutInflater layoutInfiater = (LayoutInflater)this.context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

                convertView = layoutInfiater.inflate(R.layout.listview_items, null);

                viewItem.IdTextView = (TextView)convertView.findViewById(R.id.textviewID);

                viewItem.NameTextView = (TextView)convertView.findViewById(R.id.textviewSubjectName);
                viewItem.imggambar = (ImageView)convertView.findViewById(R.id.imgevent);

                convertView.setTag(viewItem);
            }
            else
            {
                viewItem = (ViewItem) convertView.getTag();
            }

            viewItem.IdTextView.setText(TempSubjectList.get(position).Subject_ID);

            viewItem.NameTextView.setText(TempSubjectList.get(position).Subject_Name);

            //--
            byte[] decodedBytes = Base64.decode(TempSubjectList.get(position).Subject_gambar,Base64.DEFAULT);
            Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
            viewItem.imggambar.setImageBitmap(decodedBitmap);
            // set ukuran dan margin
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(200, 200);
            layoutParams.setMargins(20, 60, 0,0);
            viewItem.imggambar.setLayoutParams(layoutParams);
            //--



            return convertView;
        }
    }

    class ViewItem
    {
        TextView IdTextView;
        TextView NameTextView;
        ImageView imggambar;
    }
