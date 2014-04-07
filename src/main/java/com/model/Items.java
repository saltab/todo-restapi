package com.model;

/**
 * @author Saurabh Talbar (saurabh.talbar at gmail.com)
 */

import java.util.List;
import java.util.ArrayList;

// Class used to save list of ItemBeans
public class Items {
	List<ItemBean> items;// = new ArrayList<ItemBean>();

	public Items() {
	}
	
	public Items(List<ItemBean> list) {
		this.items = new ArrayList<ItemBean>(list);
	}

	public List<ItemBean> getItems() {
		return this.items;
	}

	public void setItems(List<ItemBean> input) {
		this.items = input;
	}
}