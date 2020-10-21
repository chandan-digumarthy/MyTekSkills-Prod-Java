package com.mytekskills.demo.utils;

import com.mytekskills.demo.datastructures.CustomLimitedSize_LinkedList;

public class CheckDownload_SingletonHelper {

	// static variable single_instance of type Singleton 
    private static CheckDownload_SingletonHelper instance = null; 
  
    // defining required variable 
    public CustomLimitedSize_LinkedList list; 
  
    // private constructor restricted to this class itself
    private CheckDownload_SingletonHelper() 
    { 
        // initializing list with max size of 100
    	list = new CustomLimitedSize_LinkedList(100); 
    } 
  
    // static method to create instance of CheckConnectivity_SingletonHelper class 
    public static CheckDownload_SingletonHelper getInstance() 
    { 
        if (instance == null) 
        	instance = new CheckDownload_SingletonHelper(); 
  
        return instance; 
    } 
	


}
