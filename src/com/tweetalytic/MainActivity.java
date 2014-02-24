package com.tweetalytic;

import java.util.HashMap;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;

import com.lymbix.LymbixClient;
import com.lymbix.models.ArticleInfo;

public class MainActivity extends Activity implements OnQueryTextListener {
	Twitter twitter;
	SearchView search;
	ActionBar actionBar;
	Activity curActivity = this;
	
	private static final String TWITTER_ACCESS =
			"1682049438-iD8VzJyzqdhmjekMQp0NVn0oOuzmOg7LUqzC7cz";
	private static final String TWITTER_SECRET =
			"9hlpNolXBmsqjEC9Mhcux2R1v1ZZsVNY2JfDumA1o9G32";
	private static final String TWITTER_KEY =
			"VFyPHw9h6Atj480pESXWQ";
	private static final String TWITTER_CONSUMER =
			"mxG5nfYTCsRqybBfoEVFtUFWlDSB3LHCU35Q7vo";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setActionBar();
		
		// set-up Twitter searches
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
				.setOAuthAccessToken(TWITTER_ACCESS)
				.setOAuthAccessTokenSecret(TWITTER_SECRET)
				.setOAuthConsumerKey(TWITTER_KEY)
				.setOAuthConsumerSecret(TWITTER_CONSUMER);
		TwitterFactory tf = new TwitterFactory(cb.build());
		twitter = tf.getInstance();

		// search bar
		search = (SearchView) findViewById(R.id.searchView1);
		search.setOnQueryTextListener(this);
		int id = search.getContext().getResources()
				.getIdentifier("android:id/search_src_text", null, null);
		TextView textView = (TextView) search.findViewById(id);
		textView.setTextColor(Color.WHITE);
	}

	public void setActionBar() {
		actionBar = getActionBar();
		actionBar.setDisplayShowHomeEnabled(false);
		View mActionBarView = getLayoutInflater().inflate(R.layout.actionbar,
				null);
		actionBar.setCustomView(mActionBarView);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
	}

	class QueryWord extends AsyncTask<String, Void, Void> {
		private static final int NUM_TWEETS = 50;		
		private static final int NUM_EMOTIONS = 8;
		
		// emotion constants
		private static final int AFFECT_FRIEND = 0;
		private static final int ENJOY_ELATE = 1;
		private static final int AMUSE_EXCITE = 2;
		private static final int CONTENT_GRAT = 3;
		private static final int SAD_GRIEF = 4;
		private static final int ANGER_LOATHE = 5;
		private static final int FEAR_UNEASE = 6;
		private static final int HUMIL_SHAME = 7;
		
		private static final String LYMBIX_AUTH = "f6212b1c1cd687144b1e89ec8475cb312091dc97"; 
		
		private boolean noTweets = false; // no tweets retrieved
		HashMap<String, String> tweets;	  // tweets to usernames
		private double[] emotions = new double[NUM_EMOTIONS]; // values of emotions
		private ArticleInfo[] tweetInfos; // emotional data
		
		@Override
		protected Void doInBackground(String... params) {
			Query query = new Query(params[0]);
			query.count(NUM_TWEETS);
			QueryResult result = null;
			
			//contains tweets mapped to usernames
			tweets = new HashMap<String, String>();
			try {
				result = twitter.search(query);
			} catch (TwitterException e) {
				e.printStackTrace();
			}
			
			for (twitter4j.Status status : result.getTweets()) {
				System.out.println("@" + status.getUser().getScreenName() + ":"
						+ status.getText());
				tweets.put(status.getText(), status.getUser().getScreenName());
			}
			
			try{
				LymbixClient lymbix = new LymbixClient(LYMBIX_AUTH);
				// tweets' text
				String[] tweetTexts = (String[])(tweets.keySet().toArray());
				
				// analyze tweets
				tweetInfos = lymbix.tonalizeMultiple(tweetTexts, null);
				
				if(tweetInfos == null || tweetInfos.length == 0)
					throw new IllegalArgumentException();
				
				// sum up emotions
				for(ArticleInfo ai : tweetInfos){	
					emotions[AFFECT_FRIEND] += ai.AffectionFriendliness;
					emotions[ENJOY_ELATE] += ai.EnjoymentElation;
					emotions[AMUSE_EXCITE] += ai.AmusementExcitement;
					emotions[CONTENT_GRAT] += ai.ContentmentGratitude;
					emotions[SAD_GRIEF] += ai.SadnessGrief;
					emotions[ANGER_LOATHE] += ai.AngerLoathing;
					emotions[FEAR_UNEASE] += ai.FearUneasiness;
					emotions[HUMIL_SHAME] += ai.HumiliationShame;
				}
				
				// average out all emotions
				emotions[AFFECT_FRIEND] /= tweetInfos.length;
				emotions[ENJOY_ELATE] /= tweetInfos.length;
				emotions[AMUSE_EXCITE] /= tweetInfos.length;
				emotions[CONTENT_GRAT] /= tweetInfos.length;
				emotions[SAD_GRIEF] /= tweetInfos.length;
				emotions[ANGER_LOATHE] /= tweetInfos.length;
				emotions[FEAR_UNEASE] /= tweetInfos.length;
				emotions[HUMIL_SHAME] /= tweetInfos.length;
				
				// determine average ratios among emotions
				double[] avgRatios = new double[NUM_EMOTIONS];
				avgRatios[AFFECT_FRIEND] = emotions[AFFECT_FRIEND] / emotions[ENJOY_ELATE];
				avgRatios[ENJOY_ELATE] = emotions[ENJOY_ELATE] / emotions[AMUSE_EXCITE];
				avgRatios[AMUSE_EXCITE] = emotions[AMUSE_EXCITE] / emotions[CONTENT_GRAT];
				avgRatios[CONTENT_GRAT] = emotions[CONTENT_GRAT] / emotions[SAD_GRIEF];
				avgRatios[SAD_GRIEF] = emotions[SAD_GRIEF] / emotions[ANGER_LOATHE];
				avgRatios[ANGER_LOATHE] = emotions[ANGER_LOATHE] / emotions[FEAR_UNEASE];
				avgRatios[FEAR_UNEASE] = emotions[FEAR_UNEASE] / emotions[HUMIL_SHAME];
				avgRatios[HUMIL_SHAME] = emotions[HUMIL_SHAME] / emotions[AFFECT_FRIEND];
				
				// find average difference between these ratios for each tweet
				for(ArticleInfo ai : tweetInfos){
					ai.avgDiff += Math.abs(avgRatios[AFFECT_FRIEND] -
								  (ai.AffectionFriendliness / ai.EnjoymentElation));
					ai.avgDiff += Math.abs(avgRatios[ENJOY_ELATE] -
								  (ai.EnjoymentElation / ai.AmusementExcitement));
					ai.avgDiff += Math.abs(avgRatios[AMUSE_EXCITE] -
								  (ai.AmusementExcitement / ai.ContentmentGratitude));
					ai.avgDiff += Math.abs(avgRatios[CONTENT_GRAT] -
								  (ai.ContentmentGratitude / ai.SadnessGrief));
					ai.avgDiff += Math.abs(avgRatios[SAD_GRIEF] -
								  (ai.SadnessGrief / ai.AngerLoathing));
					ai.avgDiff += Math.abs(avgRatios[ANGER_LOATHE] -
								  (ai.AngerLoathing / ai.FearUneasiness));
					ai.avgDiff += Math.abs(avgRatios[FEAR_UNEASE] -
								  (ai.FearUneasiness / ai.HumiliationShame));
					ai.avgDiff += Math.abs(avgRatios[HUMIL_SHAME] -
								  (ai.HumiliationShame / ai.AffectionFriendliness));
					ai.avgDiff /= NUM_EMOTIONS;
				}
				
				// sort low difference to high difference
				ascendMergeSort(tweetInfos);

			}catch(IllegalArgumentException iae){
				noTweets = true; // nothing retrieved
			}catch(Exception e){
				e.printStackTrace();
			}
			return null;
		}
		
		protected void onPostExecute(){
			// nothing retrieved
			if(noTweets){
				Toast.makeText(curActivity, R.string.no_tweets,
						Toast.LENGTH_SHORT).show();
				return;
			}
			
			Bundle args = new Bundle();
			
			actionBar.removeAllTabs();
			actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
			
			// average tweet's data
//			args.putDoubleArray("emotions", emotions);
//			actionBar.addTab(actionBar.newTab()
//							 .setText("Average")
//							 .setTabListener(new TabListener<InfoFragment>(
//									 curActivity, "average", InfoFragment.class, args)));
			
			for(int i = 0; i < 5; i++){
				ArticleInfo ai = tweetInfos[i];
				if(ai == null)
					break;
				
				args = getArgs(ai);
				actionBar.addTab(actionBar.newTab()
						 		 .setText("Tweet " + (i+1))
						 		 .setTabListener(new TabListener<InfoFragment>(
						 				 curActivity, ("tweet_" + (i+1)), InfoFragment.class, args)));
			}
		}
		
		// standard mergesort
		private void ascendMergeSort(ArticleInfo[] arr){
			ArticleInfo[] tmp = new ArticleInfo[arr.length];
			mergeSort(arr, tmp, 0, arr.length - 1);
		}
		
		private void mergeSort(ArticleInfo[] arr, ArticleInfo[] tmp, int left, int right){
			if(left < right){
				int center = (left + right) / 2;
				mergeSort(arr, tmp, left, center);
				mergeSort(arr, tmp, center + 1, right);
				merge(arr, tmp, left, center + 1, right);
			}
		}
		
		private void merge(ArticleInfo[] arr, ArticleInfo[] tmp, int left, int right, int rEnd){
			int lEnd = right - 1;
			int tmpPos = left;
			int numElements = rEnd - left + 1;
			
			while(left <= lEnd && right <= rEnd){
				if(arr[left].avgDiff <= arr[right].avgDiff)
					tmp[tmpPos++] = arr[left++];
				else
					tmp[tmpPos++] = arr[right++];
			}
			
			while(left <= lEnd)
				tmp[tmpPos++] = arr[left++];
			while(right <= rEnd)
				tmp[tmpPos++] = arr[right++];
			
			for(int i = 0; i < numElements; i++, rEnd--)
				arr[rEnd] = tmp[rEnd];
		}
		
		// creates Bundle for each tweet
		private Bundle getArgs(ArticleInfo ai){
			Bundle args = new Bundle();
			
			args.putString("tweet", ai.Article);
			args.putString("user", tweets.get(ai.Article));
			args.putDouble("aff_fri", ai.AffectionFriendliness);
			args.putDouble("amu_exc", ai.AmusementExcitement);
			args.putDouble("ang_loa", ai.AngerLoathing);
			args.putDouble("con_gra", ai.ContentmentGratitude);
			args.putDouble("enj_ela", ai.EnjoymentElation);
			args.putDouble("fea_une", ai.FearUneasiness);
			args.putDouble("hum_sha", ai.HumiliationShame);
			args.putDouble("sad_gri", ai.SadnessGrief);
			
			return args;
		}
	}
	
	@Override
	public boolean onQueryTextChange(String arg0) {
		return false;
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		new QueryWord().execute(query);
		return true;
	}
}
