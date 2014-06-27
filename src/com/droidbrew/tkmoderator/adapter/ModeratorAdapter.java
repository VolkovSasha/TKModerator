package com.droidbrew.tkmoderator.adapter;

import java.util.List;

import com.droidbrew.tkmoderator.R;
import com.droidbrew.travelkeeper.model.entity.Place;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ModeratorAdapter extends BaseAdapter {

    private class ViewHolder {
        public TextView textView1, textView2;
    }
    
    private List<Place> place;
    private LayoutInflater  mInflater;
    Context context;
    
    public ModeratorAdapter(Context context, List<Place> place) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.place = place;
        this.context = context;
    }
    
    public void setData(List<Place> list) {
    	place = list;
    }
    
    @Override
    public int getCount() {
        if (place != null) {
            return place.size();
        }
        
        return 0;
    }

    @Override
    public Place getItem(int position) {
        if (place != null && position >= 0 && position < getCount()) {
            return place.get(position);
        }
        
        return null;
    }

    @Override
    public long getItemId(int position) {
        if (place != null && position >= 0 && position < getCount()) {
            return place.get(position).getId();
        }
        
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        
        View       view = convertView;
        ViewHolder viewHolder;
        
        if (view == null) {
            view = mInflater.inflate(R.layout.item_list, parent, false);
            
            viewHolder = new ViewHolder();
            viewHolder.textView1 = (TextView) view.findViewById(R.id.textView1);
            viewHolder.textView2 = (TextView) view.findViewById(R.id.textView2);
                        
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }
        
       		String name = place.get(position).getTitle();
        	viewHolder.textView1.setText(name);
        	String comment = place.get(position).getComment();
        	viewHolder.textView2.setText(comment);
        
        return view;
    }

}
