package com.mytekskills.demo.utils;

import com.mytekskills.demo.datastructures.CustomLimitedSize_LinkedList;

public class CheckConnectivity_SingletonHelper {

	// static variable single_instance of type Singleton 
    private static CheckConnectivity_SingletonHelper instance = null; 
  
    // defining required variable 
    public CustomLimitedSize_LinkedList list; 
  
    // private constructor restricted to this class itself
    private CheckConnectivity_SingletonHelper() 
    { 
        // initializing list with max size of 100
    	list = new CustomLimitedSize_LinkedList(100); 
    } 
  
    // static method to create instance of CheckConnectivity_SingletonHelper class 
    public static CheckConnectivity_SingletonHelper getInstance() 
    { 
        if (instance == null) 
        	instance = new CheckConnectivity_SingletonHelper(); 
  
        return instance; 
    } 
	
}
