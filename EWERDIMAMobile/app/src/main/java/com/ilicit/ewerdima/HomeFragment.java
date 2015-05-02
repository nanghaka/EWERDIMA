package com.ilicit.ewerdima;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ListView;

public class HomeFragment extends Fragment {
	private List<RowItem> rowItems;
	 
    private static Integer[] images = {
            R.drawable.congo,
            R.drawable.iceland,
            R.drawable.allmap,
            R.drawable.allmap
    };
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View homefrag = inflater.inflate(R.layout.fragment_home, container,
				false);
		
		Button alertBTN = (Button) homefrag
				.findViewById(R.id.alert_button);
		final Animation animScale = AnimationUtils.loadAnimation(getActivity() ,    R.anim.anim_scale);
		final Animation animTranslate = AnimationUtils.loadAnimation(getActivity(),    R.anim.anim_translate);
		AnimationSet sets = new AnimationSet(false);
		ListView lv = (ListView) homefrag.findViewById(R.id.myList);
		  rowItems = new ArrayList<RowItem>();
		    
		         String[] titles = {"Democratic Republic Of Congo","Iceland","America","China"};
		         String[] descriptions = {"Magnitude 4.5M - 2014-10-31","Magnitude 5M - 2014-10-31","Magnitude 3M - 2014-10-30","Magnitude 2M - 2014-10-29"};
		         //Populate the List
		         for (int i = 0; i < titles.length; i++) {
		             RowItem item = new RowItem(images[i], titles[i], descriptions[i]);
		             rowItems.add(item);
		         }
		 
		         // Set the adapter on the ListView
		         LazyAdapter adapter = new LazyAdapter(getActivity(), R.layout.list_row, rowItems);
		         lv.setAdapter(adapter);
		sets.addAnimation(animScale);
		sets.addAnimation(animTranslate);
		alertBTN.startAnimation(sets);
		
		alertBTN.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent toAlert = new Intent(getActivity(), Alert.class);
				startActivity(toAlert);
			}
		});
		
		//to fix overlap
		homefrag.setOnTouchListener(new View.OnTouchListener() {
	        public boolean onTouch(View v, MotionEvent event) {
	            return true;
	        }
	    });
		return homefrag;
	}

}
