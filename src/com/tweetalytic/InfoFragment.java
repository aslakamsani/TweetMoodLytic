package com.tweetalytic;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class InfoFragment extends Fragment {
	private Bundle args; 
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		args = getArguments();
	}
	
	// displays emotions' prevalence in the tweet as a percentage
	// PROOF OF CONCEPT
	// TODO: display as a pie chart by using an SDK
	public View onCreateView(LayoutInflater inf, ViewGroup vg, Bundle savedInstanceState){
		View v = inf.inflate(R.layout.info_fragment_layout, vg, false);
		TextView tv = (TextView)(v.findViewById(R.id.text));
		
		// total positive values for emotions
		double total = 0;
		total += (args.getDouble("aff_fri") > 0) ? args.getDouble("aff_fri") : 0;
		total += (args.getDouble("amu_exc") > 0) ? args.getDouble("amu_exc") : 0;
		total += (args.getDouble("ang_loa") > 0) ? args.getDouble("ang_loa") : 0;
		total += (args.getDouble("con_gra") > 0) ? args.getDouble("con_gra") : 0;
		total += (args.getDouble("enj_ela") > 0) ? args.getDouble("enj_ela") : 0;
		total += (args.getDouble("fea_une") > 0) ? args.getDouble("fea_une") : 0;
		total += (args.getDouble("hum_sha") > 0) ? args.getDouble("hum_sha") : 0;
		total += (args.getDouble("sad_gri") > 0) ? args.getDouble("sad_gri") : 0;
		
		// construct string with percentages for emotions
		String text = "";
		if(args.getString("tweet") != null){
			text += "\"" + args.getString("tweet") + "\"\n";
			text += "- " + args.getString("user") + "\n\n";
		}else{
			text += "Average Tweet for \"" + args.getString("search") + "\"\n\n";
		}
		text += "Affection:\t" + ((args.getDouble("aff_fri") > 0) ?
								  (args.getDouble("aff_fri") / total) : 0) + "%\n";
		text += "Enjoyment:\t" + ((args.getDouble("enj_ela") > 0) ?
				  				  (args.getDouble("enj_ela") / total) : 0) + "%\n";
		text += "Amusement:\t" + ((args.getDouble("amu_exc") > 0) ?
								  (args.getDouble("amu_exc") / total) : 0) + "%\n";
		text += "Contentment:\t" + ((args.getDouble("con_gra") > 0) ?
				  				    (args.getDouble("con_gra") / total) : 0) + "%\n";
		text += "Sadness:\t" + ((args.getDouble("sad_gri") > 0) ?
				  				(args.getDouble("sad_gri") / total) : 0) + "%\n";
		text += "Anger:\t" + ((args.getDouble("ang_loa") > 0) ?
				  			  (args.getDouble("ang_loa") / total) : 0) + "%\n";
		text += "Fear:\t" + ((args.getDouble("fea_une") > 0) ?
				      		 (args.getDouble("fea_une") / total) : 0) + "%\n";
		text += "Shame:\t" + ((args.getDouble("hum_sha") > 0) ?
				  			  (args.getDouble("hum_sha") / total) : 0) + "%\n";
		
		tv.setText(text);
		return v;
	}
}
