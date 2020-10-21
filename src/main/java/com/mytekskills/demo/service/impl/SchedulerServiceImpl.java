package com.mytekskills.demo.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.mytekskills.demo.constants.URLConstants;
import com.mytekskills.demo.utils.CheckConnectivity_SingletonHelper;
import com.mytekskills.demo.utils.CheckDownload_SingletonHelper;

@Service
public class SchedulerServiceImpl {

	private static Logger logger = LogManager.getLogger(SchedulerServiceImpl.class);
	
	/**
	 * Scheduled to run every 10 seconds, 
	 * Downloads a file from my Git repository,
	 * Calculates download speed
	 * Updates Custom Limited Size List with the download speed in kbps
	 * Later, data in this list will be consumed by Real Time Network Health Component on the UI
	 * 
	 * Note: File will be downloaded to user.home directory with file name - test_speed.zip
	 */
	@Scheduled(cron = "*/10 * * * * *")
	public void checkDownloadSpeed() {

		try {
			// milli seconds precision
			long time1 = new Date().getTime();

			String downloadUrl = URLConstants.checkDownloadSpeed_Url;
			String downloadFileName = System.getProperty("user.home")+"/test_speed.zip";
			
			InputStream in = new URL(downloadUrl).openStream();

			Files.copy(in, Paths.get(downloadFileName), StandardCopyOption.REPLACE_EXISTING);

			long time2 = new Date().getTime();

			long timeTaken = time2 - time1;
			
			//filesize calculation in kilo bytes
			long fileSize = new File(downloadFileName).length() / 1024L;

			//convert kiblobytes per milli seconds to kile bytes / seconds
			//kile bytes / (milli seconds / 1000)
			long kbps = fileSize / (timeTaken / 1000);
			
			logger.log(Level.INFO, "Time taken to download file of size " + fileSize
					+ " kb = " + timeTaken + " milli seconds");
			
			logger.log(Level.INFO, "Observed download speed = "+kbps + "kbps");
			
			//saving the time we made request in seconds by dividing it with 1000
			//updating data to the singleton instance so that same can be retrieved all the time
			CheckDownload_SingletonHelper.getInstance().list.push(kbps);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * Scheduled to run every 10 seconds, 
	 * Will connect to www.google.com,
	 * Updates Custom Limited Size List with the time taken to connect in milli seconds
	 * Later, data in this list will be consumed by Real Time Network Health Component on the UI
	 */
	@Scheduled(cron = "*/10 * * * * *")
	public void checkConnectivity() throws IOException {

		InputStream responseStream = null;

		try {

			// milli seconds precision
			long time1 = new Date().getTime();

			URL url = new URL(URLConstants.checkConnectivity_Url);
			
			// Open a connection on the URL and cast the response
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			// initiate the request
			responseStream = connection.getInputStream();

			long time2 = new Date().getTime();

			int timeTaken = (int) (time2 - time1);
			logger.log(Level.INFO, "Time taken for request = " + timeTaken);
			
			//saving the time we made request in seconds by dividing it with 1000
			//updating data to the singleton instance so that same can be retrieved all the time
			CheckConnectivity_SingletonHelper.getInstance().list.push(timeTaken);
			
		} catch (IOException e) {
			logger.log(Level.ERROR, ", Error while working checkConnectivity: ", e);
		} finally {
			responseStream.close();
		}
		
	}
	
}
