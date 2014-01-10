package com.tweetalytic;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
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
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;

public class MainActivity extends Activity implements OnQueryTextListener {
	Twitter twitter;
	SearchView search;
	ActionBar actionBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setActionBar();
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
				.setOAuthAccessToken(
						"1682049438-iD8VzJyzqdhmjekMQp0NVn0oOuzmOg7LUqzC7cz")
				.setOAuthAccessTokenSecret(
						"9hlpNolXBmsqjEC9Mhcux2R1v1ZZsVNY2JfDumA1o9G32")
				.setOAuthConsumerKey("VFyPHw9h6Atj480pESXWQ")
				.setOAuthConsumerSecret(
						"mxG5nfYTCsRqybBfoEVFtUFWlDSB3LHCU35Q7vo");
		TwitterFactory tf = new TwitterFactory(cb.build());
		twitter = tf.getInstance();

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

		@Override
		protected Void doInBackground(String... params) {
			Query query = new Query(params[0]);
			QueryResult result = null;

			try {
				result = twitter.search(query);
			} catch (TwitterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for (twitter4j.Status status : result.getTweets()) {
				System.out.println("@" + status.getUser().getScreenName() + ":"
						+ status.getText());
			}
			return null;
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
