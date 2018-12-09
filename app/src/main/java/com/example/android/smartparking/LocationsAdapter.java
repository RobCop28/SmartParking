package com.example.android.smartparking;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;

import java.util.List;

public class LocationsAdapter extends ArrayAdapter<Locations> {
     public LocationsAdapter(Context context, int resource, List<Locations> objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.item_location, parent, false);
            }


            TextView messageTextView = (TextView) convertView.findViewById(R.id.LocationName);


            Locations location = getItem(position);



                messageTextView.setVisibility(View.VISIBLE);
                messageTextView.setText(location.getLocationName());
                return convertView;
        }
}
