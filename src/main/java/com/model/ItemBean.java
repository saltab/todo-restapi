package com.model;

/**
 * @author Saurabh Talbar (saurabh.talbar at gmail.com)
 */

import org.bson.types.ObjectId;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Indexed;

@Entity("items")
public class ItemBean {

	@Id
	ObjectId id;
	
	@Indexed
    private String orderId;
	
	// Definition of our todo task
	String title, body; boolean done;
	
	public ItemBean(){
		
	}
	
	public ItemBean(String title, String body, boolean done){
		this.title = title;
		this.body = body;
		this.done = done;
	}
	public ObjectId getId(){
		return id;
	}
	
	public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public boolean getDone() {
        return done;
    }

//    public void setId(ObjectId id){
//    	this.id = id;
//    }
	public void setTitle(String title) {
        this.title = title;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setDone(boolean done) {
        this.done = done;
    }    

	/*@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ItemBean ItemBean = (ItemBean) o;
        if(title != ItemBean.title) return false;
        //if(body == ItemBean.body) return false;        
        
        return true;
    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + Integer.parseInt(body);
        result = 31 * result + (int) (Integer.parseInt(title) ^ (Integer.parseInt(title) >>> 32));
        return result;
    }
*/
}
