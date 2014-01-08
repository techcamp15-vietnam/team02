package com.shinkansen.touchcolor.DataModel;

/**
 * Color Model
 * @author huuthang
 */
public class Color {
	private String colorName;
	private String colorHexaCode;
	private String colorSoundName;
	
	public void setColorName(String colorN){
		this.colorName = colorN;
	}
	
	public String getColorName(){
		return this.colorName;
	}
	
	public void setColorHexaCode(String colorCode){
		this.colorHexaCode = colorCode;
	}
	
	public String getColorHexaCode(){
		return this.colorHexaCode;
	}
	public void setColorSoundName(String colorSN){
		this.colorSoundName = colorSN;
	}
	
	public String getColorSoundName(){
		return this.colorSoundName;
	}
	
}
