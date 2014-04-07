package com.model;

public class Message {
	
	String message ="";	
	public Message(){
		
	}
	
	public Message(String message){
		this.message=message;
	}
	
	public String getMessage(){
		return this.message;
	}
	public void setMessage(String message){
		this.message = message;
	}

}