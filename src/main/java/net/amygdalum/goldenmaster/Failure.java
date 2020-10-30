package net.amygdalum.goldenmaster;

import java.io.Serializable;

public class Failure {

	private String group;
	private String test;
	private Serializable value;

	public Failure(String group, String test, Serializable value) {
		this.group = group;
		this.test = test;
		this.value = value;
	}
	
	public String getGroup() {
		return group;
	}
	
	public String getTest() {
		return test;
	}
	
	public Serializable getValue() {
		return value;
	}

}
