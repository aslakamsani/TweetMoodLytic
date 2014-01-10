package com.lymbix.models;

import com.google.gson.annotations.*;

public class IntenseSentence {

	@SerializedName("sentence") 
	public String Sentence;
	@SerializedName("dominant_emotion") 
	public String DominantEmotion;
	@SerializedName("intensity") 
	public Double Intensity;

}
