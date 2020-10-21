package com.mytekskills.demo.service.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.mytekskills.demo.BO.City;
import com.mytekskills.demo.BO.Continent;
import com.mytekskills.demo.BO.Country;
import com.mytekskills.demo.BO.State;
import com.mytekskills.demo.BO.World;
import com.mytekskills.demo.service.BaseService;
import com.mytekskills.demo.utils.ConnectionUtils;

@Service
public class ServiceImpl implements BaseService {

	private static Logger logger = LogManager.getLogger(ServiceImpl.class);

	/**
	 * Return's World object
	 * Order of saving the data in world object : World -> Continents -> Countries -> States -> Cities
	 * 
	 * Uses Hashmap to remove iterations and achieve constant time retrieval in below cases
	 * 		- when inserting Country in a World -> Continent
	 * 		- when inserting State in a World -> Continent -> Country
	 * 		- when inserting City in a World -> Continent -> Country -> State
	 * 	 	- when retrieving Country from World -> Continent
	 * 		- when retrieving State from World -> Continent -> Country
	 * 		- when retrieving City from World -> Continent -> Country -> State
	 */
	@Override
	public World hierBarchartService() {

		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

		World world = new World();
		world.setName("World");
		world.setChildren(new ArrayList<>());

		try {

			conn = ConnectionUtils.getH2Connection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery("select continent,country,state,city,outage_time from city_outage_details");

			Map<String, Integer> indexMap = new HashMap<>();

			//just for readability duing debugging
			String keyPrefix = "Key#";
			
			String delimiter = "#";

			while (rs.next()) {

				String continent = rs.getString("continent");
				String country = rs.getString("country");
				String state = rs.getString("state");
				String city = rs.getString("city");
				int value = rs.getInt("outage_time");

				String continentKey = keyPrefix + continent;
				String countryKey = keyPrefix + continent + delimiter + country;
				String stateKey = keyPrefix + continent + delimiter + country + delimiter + state;

				int contNum = indexMap.containsKey(continentKey) ? indexMap.get(continentKey) : -1;
				int cntryNum = indexMap.containsKey(countryKey) ? indexMap.get(countryKey) : -1;
				int stateNum = indexMap.containsKey(stateKey) ? indexMap.get(stateKey) : -1;

				indexMap.put(continentKey, contNum);
				indexMap.put(countryKey, cntryNum);
				indexMap.put(stateKey, stateNum);

				if (contNum < 0) {

					//Continent itself is not present, so we have to add continent to the end of continents list in the world object
					// followed by below operations
					// add new continent
					// add new country
					// add new state
					// to existing world
					
					int totalContinents = world.getChildren().size();

					indexMap.put(continentKey, totalContinents);
					indexMap.put(countryKey, 0);
					indexMap.put(stateKey, 0);

					world.getChildren().add(new Continent(continent, new ArrayList<>()));
					world.getChildren().get(indexMap.get(continentKey)).getChildren()
							.add(new Country(country, new ArrayList<>()));
					world.getChildren().get(indexMap.get(continentKey)).getChildren().get(0).getChildren()
							.add(new State(state, new ArrayList<>()));
					world.getChildren().get(indexMap.get(continentKey)).getChildren().get(0).getChildren().get(0)
							.getChildren().add(new City(city, value));

				} else if (cntryNum < 0 && contNum >= 0) {

					//Country is not present in the Continent, so we have to add Country to the end of Countries list in the Continents object
					// followed by below operations
					// add new country
					// add new state
					// to existing continent
					
					int totalCountries = world.getChildren().get(contNum).getChildren().size();

					indexMap.put(countryKey, totalCountries);
					indexMap.put(stateKey, 0);

					world.getChildren().get(indexMap.get(continentKey)).getChildren()
							.add(new Country(country, new ArrayList<>()));

					world.getChildren().get(indexMap.get(continentKey)).getChildren().get(indexMap.get(countryKey))
							.getChildren().add(new State(state, new ArrayList<>()));

					world.getChildren().get(indexMap.get(continentKey)).getChildren().get(indexMap.get(countryKey))
							.getChildren().get(0).getChildren().add(new City(city, value));

				} else if (stateNum < 0 && contNum >= 0 && cntryNum >= 0) {

					//State is not present in the Country, so we have to add State to the end of States list in the Countries object
					// followed by below operations
					// add new state
					// to existing country
					
					int totalStates = world.getChildren().get(contNum).getChildren().get(indexMap.get(countryKey))
							.getChildren().size();

					indexMap.put(stateKey, totalStates);

					world.getChildren().get(indexMap.get(continentKey)).getChildren().get(indexMap.get(countryKey))
							.getChildren().add(new State(state, new ArrayList<>()));

					world.getChildren().get(indexMap.get(continentKey)).getChildren().get(indexMap.get(countryKey))
							.getChildren().get(indexMap.get(stateKey)).getChildren().add(new City(city, value));

				} else {

					world.getChildren().get(contNum).getChildren().get(cntryNum).getChildren().get(stateNum)
							.getChildren().add(new City(city, value));

				}

			}

		} catch (Exception e) {
			logger.log(Level.INFO, ", Error while working on hier-barcharts : ", e);
		} finally {
			ConnectionUtils.closeConnection(conn, stmt, rs);
		}

		return world;

	}

	
	/**
	 * Return's World object
	 * Order of saving the data in world object : World -> Continents -> Countries -> States -> Cities
	 * 
	 * Uses Hashmap to remove iterations and achieve constant time retrieval in below cases
	 * 		- when inserting Country in a World -> Continent
	 * 		- when inserting State in a World -> Continent -> Country
	 * 		- when inserting City in a World -> Continent -> Country -> State
	 * 	 	- when retrieving Country from World -> Continent
	 * 		- when retrieving State from World -> Continent -> Country
	 * 		- when retrieving City from World -> Continent -> Country -> State
	 */
	@Override
	public World disjointForceService() {

		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

		World world = new World();
		world.setName("World");
		world.setChildren(new ArrayList<>());

		try {

			conn = ConnectionUtils.getH2Connection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery("select continent,country,state,city,health_score from city_network_health limit 120");

			Map<String, Integer> indexMap = new HashMap<>();

			//just for readability duing debugging
			String keyPrefix = "Key#";
			
			String delimiter = "#";

			while (rs.next()) {

				String continent = rs.getString("continent");
				String country = rs.getString("country");
				String state = rs.getString("state");
				String city = rs.getString("city");
				int value = rs.getInt("health_score");
System.out.println("health score = " +value );
				String continentKey = keyPrefix + continent;
				String countryKey = keyPrefix + continent + delimiter + country;
				String stateKey = keyPrefix + continent + delimiter + country + delimiter + state;

				int contNum = indexMap.containsKey(continentKey) ? indexMap.get(continentKey) : -1;
				int cntryNum = indexMap.containsKey(countryKey) ? indexMap.get(countryKey) : -1;
				int stateNum = indexMap.containsKey(stateKey) ? indexMap.get(stateKey) : -1;

				indexMap.put(continentKey, contNum);
				indexMap.put(countryKey, cntryNum);
				indexMap.put(stateKey, stateNum);

				if (contNum < 0) {

					//Continent itself is not present, so we have to add continent to the end of continents list in the world object
					// followed by below operations
					// add new continent
					// add new country
					// add new state
					// to existing world
					int totalContinents = world.getChildren().size();

					indexMap.put(continentKey, totalContinents);
					indexMap.put(countryKey, 0);
					indexMap.put(stateKey, 0);

					world.getChildren().add(new Continent(continent, new ArrayList<>()));
					world.getChildren().get(indexMap.get(continentKey)).getChildren()
							.add(new Country(country, new ArrayList<>()));
					world.getChildren().get(indexMap.get(continentKey)).getChildren().get(0).getChildren()
							.add(new State(state, new ArrayList<>()));
					world.getChildren().get(indexMap.get(continentKey)).getChildren().get(0).getChildren().get(0)
							.getChildren().add(new City(city, value));

				} else if (cntryNum < 0 && contNum >= 0) {

					//Country is not present in the Continent, so we have to add Country to the end of Countries list in the Continents object
					// followed by below operations
					// add new country
					// add new state
					// to existing continent
					
					int totalCountries = world.getChildren().get(contNum).getChildren().size();

					indexMap.put(countryKey, totalCountries);
					indexMap.put(stateKey, 0);

					world.getChildren().get(indexMap.get(continentKey)).getChildren()
							.add(new Country(country, new ArrayList<>()));

					world.getChildren().get(indexMap.get(continentKey)).getChildren().get(indexMap.get(countryKey))
							.getChildren().add(new State(state, new ArrayList<>()));

					world.getChildren().get(indexMap.get(continentKey)).getChildren().get(indexMap.get(countryKey))
							.getChildren().get(0).getChildren().add(new City(city, value));

				} else if (stateNum < 0 && contNum >= 0 && cntryNum >= 0) {

					//State is not present in the Country, so we have to add State to the end of States list in the Countries object
					// followed by below operations
					// add new state
					// to existing country
					
					int totalStates = world.getChildren().get(contNum).getChildren().get(indexMap.get(countryKey))
							.getChildren().size();

					indexMap.put(stateKey, totalStates);

					world.getChildren().get(indexMap.get(continentKey)).getChildren().get(indexMap.get(countryKey))
							.getChildren().add(new State(state, new ArrayList<>()));

					world.getChildren().get(indexMap.get(continentKey)).getChildren().get(indexMap.get(countryKey))
							.getChildren().get(indexMap.get(stateKey)).getChildren().add(new City(city, value));

				} else {

					world.getChildren().get(contNum).getChildren().get(cntryNum).getChildren().get(stateNum)
							.getChildren().add(new City(city, value));

				}

			}

		} catch (Exception e) {
			logger.log(Level.INFO, ", Error while working on hier-barcharts : ", e);
		} finally {
			ConnectionUtils.closeConnection(conn, stmt, rs);
		}

		return world;
	}

}
