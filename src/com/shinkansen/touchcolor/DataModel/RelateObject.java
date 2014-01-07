package com.shinkansen.touchcolor.DataModel;

/**
RelateObject Data model
@param 
@author huuthang
*/
public class RelateObject {
	private int rObjectId;
	private String rObjName;
	private String rObjColor;
	private String rObjImageName;
	private String rObjSoundPath;
	
	public void setRObjId(int id){
		this.rObjectId = id;
	}
	public int getRObjectId(){
		return this.rObjectId;
	}
	public void setObjectName(String rObjName) {
		this.rObjName = rObjName;
	}
	public String getRObjectName() {
		return this.rObjName;
	}
	public void setObjectColor(String colorName) {
		this.rObjColor = colorName;
	}
	public String getRObjectColor() {
		return this.rObjColor;
	}
	public void setObjectImageName(String imgName) {
		this.rObjImageName = imgName;
	}
	public String getRObjectImageName() {
		return this.rObjImageName;
	}
	public void setObjectSoundPath(String path) {
		this.rObjSoundPath = path;
	}
	public String getRObjectSoundPath() {
		return this.rObjSoundPath;
	}
}
