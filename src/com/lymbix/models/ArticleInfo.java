package com.lymbix.models;

import com.google.gson.annotations.*;

public class ArticleInfo {
	
	@SerializedName("article") 
	public String Article;
	@SerializedName("ignored_terms")
	public String[] IgnoredTerms;
	
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
	
	@SerializedName("dominant_emotion")
	public String DominantCategory;
	@SerializedName("article_sentiment")
	public Sentiment ArticleSentiment;
	@SerializedName("coverage")
	public Integer Coverage;
	@SerializedName("intense_sentence")
	public IntenseSentence IntenseSentence;
	@SerializedName("reference_id")
	public Integer ReferenceId;
	@SerializedName("clarity")
	public Double Clarity;
	
	@SerializedName("sentences_data")
	public SentenceData[] SentenceData;
}
