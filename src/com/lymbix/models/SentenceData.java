package com.lymbix.models;

import com.google.gson.annotations.*;

public class SentenceData {
	
	@SerializedName("sentence") 
	public String Sentence;
	@SerializedName("dominant_emotion")
	public String DominantEmotion;
	
	@SerializedName("affection_friendliness")
	public Double AffectionFriendliness;
	@SerializedName("enjoyment_elation")
	public Double EnjoymentElation;
	@SerializedName("amusement_excitement")
	public Double AmusementExcitement;
	@SerializedName("contentment_gratitude")
	public Double ContentmentGratitude;
	@SerializedName("sadness_grief")
	public Double SadnessGrief;
	@SerializedName("anger_loathing")
	public Double AngerLoathing;
	@SerializedName("fear_uneasiness")
	public Double FearUneasiness;
	@SerializedName("humiliation_shame")
	public Double HumiliationShame;
	
	@SerializedName("sentence_sentiment")
	public Sentiment SentenceSentiment;
	@SerializedName("ignored_terms")
	public String[] IgnoredTerms;
	@SerializedName("coverage")
	public Integer Coverage;
	@SerializedName("clarity")
	public Double Clarity;
}
