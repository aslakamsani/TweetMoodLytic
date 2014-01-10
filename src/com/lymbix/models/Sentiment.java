package com.lymbix.models;

import com.google.gson.annotations.*;

public class Sentiment {
	
	@SerializedName("sentiment") 
	public String SentimentType;
	@SerializedName("score")
	public Double Score;
}
