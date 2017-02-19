package com.example.hppc.mychatdemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by HP PC on 2/19/2017.
 */
public class ArrayAdapterJsonList extends ArrayAdapter<DataModelJson> {

    List<DataModelJson> modelList;
    Context context;
    private LayoutInflater mInflater;

    public ArrayAdapterJsonList(Context context, List<DataModelJson> objects) {
        super(context, 0, objects);
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        modelList = objects;
    }

    @Override
    public DataModelJson getItem(int position) {
        return modelList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder vh;
        if (convertView == null) {
            View view = mInflater.inflate(R.layout.listview_json, parent, false);
            vh = ViewHolder.create((LinearLayout) view);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        DataModelJson item = getItem(position);

        vh.textViewName.setText(item.getName());
        vh.textViewMessege.setText(item.getMessage());
        Picasso.with(context).load(item.getProfile_image()).placeholder(R.drawable.ic_account_circle_black).error(R.drawable.ic_account_circle_black).into(vh.imageView);

        return vh.rootView;
    }

    private static class ViewHolder {
        public final LinearLayout rootView;
        public final ImageView imageView;
        public final TextView textViewName;
        public final TextView textViewMessege;

        private ViewHolder(LinearLayout rootView, ImageView imageView, TextView textViewName, TextView textViewMessege) {
            this.rootView = rootView;
            this.imageView = imageView;
            this.textViewName = textViewName;
            this.textViewMessege = textViewMessege;
        }

        public static ViewHolder create(LinearLayout rootView) {
            ImageView imageView = (ImageView) rootView.findViewById(R.id.imageViewPic);
            TextView textViewName = (TextView) rootView.findViewById(R.id.textViewJName);
            TextView textViewMessege = (TextView) rootView.findViewById(R.id.textViewMessege);
            return new ViewHolder(rootView, imageView, textViewName, textViewMessege);
        }
    }
}
