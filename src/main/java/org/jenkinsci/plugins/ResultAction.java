package org.jenkinsci.plugins;

import java.io.IOException;

import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class ResultAction {

	public void tweet(String str) throws TwitterException, IOException {

		ConfigurationBuilder builder = new ConfigurationBuilder();
		builder.setDebugEnabled(true);
		builder.setOAuthConsumerKey("");
		builder.setOAuthConsumerSecret("");
		builder.setOAuthAccessToken("");
		builder.setOAuthAccessTokenSecret("");

		new TwitterFactory(builder.build()).getInstance().updateStatus(str);
	}

}
