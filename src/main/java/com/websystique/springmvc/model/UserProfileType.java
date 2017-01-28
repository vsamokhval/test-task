package com.websystique.springmvc.model;

import java.io.Serializable;

public enum UserProfileType implements Serializable{
	PUBLISHER("PUBLISHER"),
	OPERATOR("OPERATOR"),
	ADMIN("ADMIN");
	
	String userProfileType;
	
	private UserProfileType(String userProfileType){
		this.userProfileType = userProfileType;
	}
	
	public String getUserProfileType(){
		return userProfileType;
	}
	
}
