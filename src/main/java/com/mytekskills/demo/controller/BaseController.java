package com.mytekskills.demo.controller;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mytekskills.demo.BO.World;
import com.mytekskills.demo.service.BaseService;
import com.mytekskills.demo.utils.CheckConnectivity_SingletonHelper;
import com.mytekskills.demo.utils.CheckDownload_SingletonHelper;

@RestController
@CrossOrigin(origins="http://localhost:4200")
public class BaseController {

	private static Logger logger = LogManager.getLogger(BaseController.class);
	
	@Autowired
	BaseService service;
	
	@GetMapping(value = "/hier-barchart")
	public World hierBarchart() {
		
		logger.info("Working on fetching Hierarchial bar chart data");
		return service.hierBarchartService();
		
	}
	
	
	@GetMapping(value = "/force-directed")
	public World disjointForce() {
		
		logger.info("Working on fetching disjoint force chart data");
		return service.disjointForceService();
		
	}
	
	@GetMapping(value = "/connectivity-stats")
	public String checkInternetConnectivity() throws IOException {
		
		logger.info("Working on fetching realtime internet connectivity test chart data");
		return CheckConnectivity_SingletonHelper.getInstance().list.toString();
		
	}
	
	@GetMapping(value = "/download-speed")
	public String checkDownloadSpeed() throws IOException {
		
		logger.info("Working on fetching realtime download speed test chart data");
		return CheckDownload_SingletonHelper.getInstance().list.toString();
		
	}
	
}