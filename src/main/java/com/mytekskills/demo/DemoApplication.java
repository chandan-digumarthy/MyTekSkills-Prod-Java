package com.mytekskills.demo;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mytekskills.demo.utils.ConnectionUtils;

@EnableScheduling
@SpringBootApplication
public class DemoApplication implements CommandLineRunner  {

	private static Logger logger = LogManager.getLogger(DemoApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	/**
	 * load the predefined data from csv files into H2 database
	 */
	@Override
	public void run(String... args) throws Exception {

		Connection conn = null;
		Statement stmt = null;
		
		try {
			
			conn = ConnectionUtils.getH2Connection();
			stmt = conn.createStatement();
			
			stmt.execute("drop table if exists city_details");
			stmt.execute("drop table if exists city_outage_details");
			stmt.execute("drop table if exists city_network_health");

			stmt.execute("drop table if exists cities");
			stmt.execute("drop table if exists states");
			stmt.execute("drop table if exists countries");
			stmt.execute("drop table if exists continents");

			stmt.execute("create table city_details (continent varchar(255), country varchar(255), state varchar(255), city varchar(255)) as select * from CSVREAD('./city_details.csv')");
			stmt.execute("create table city_outage_details (continent varchar(255), country varchar(255), state varchar(255), city varchar(255), outage_time int) as select * from CSVREAD('./city_outage_details.csv')");
			stmt.execute("create table city_network_health (continent varchar(255), country varchar(255), state varchar(255), city varchar(255), health_score int) as select * from CSVREAD('./city_network_health.csv')");

			//create relational database structure from existing table
			//this is just creating the schema, data will be filled in the next steps
			stmt.execute("create table continents (id int primary key auto_increment, continent varchar(50))");
			stmt.execute("create table countries (id int primary key auto_increment, continent_id int, country varchar(255), FOREIGN KEY (continent_id) REFERENCES continents(id))");
			stmt.execute("create table states (id int primary key auto_increment, continent_id int, country_id int, state varchar(255), FOREIGN KEY (continent_id) REFERENCES continents(id), FOREIGN KEY (country_id) REFERENCES countries(id))");
			stmt.execute("create table cities (id int primary key auto_increment, continent_id int, country_id int, state_id int, city varchar(255), FOREIGN KEY (continent_id) REFERENCES continents(id), FOREIGN KEY (country_id) REFERENCES countries(id), FOREIGN KEY (state_id) REFERENCES states(id))");
			
			/*
			 
			 Transform the flat csv kind of a table - city_details
			 with to traditional relational data into the created tables
			 
			 Now continents, countries, states, cities will have their own table
			 
			  - countries will be linked to continents : 
			  			- primary key of continents will be a foreign key in countries
			  - states will be linked to countries and continents : 
			  			- primary key of countries will be a foreign key in states
			  			- primary key of continents will be a foreign key in states
			  - cities will be linked to states, countries and continents : 
			  			- primary key of states will be a foreign key in cities
			  			- primary key of countries will be a foreign key in cities
			  			- primary key of continents will be a foreign key in cities
			  
			 By doing this we can achieve better retrieval of all cities in a state, all countries in a continent and etc
			 Also we ensure unwanted data / wrong data will not be ingested, allowed
			 As primary keys are efficient ways to index data, we can achieve better performace
			 
			*/
			stmt.execute("insert into continents (continent) select distinct continent from city_details");
			stmt.execute("insert into countries (country, continent_id) select distinct city_details.country as country,continents.id as continent_id from city_details,continents where city_details.continent=continents.continent");
			stmt.execute("insert into states (state, country_id, continent_id) select distinct city_details.state as state,countries.id as country_id,continents.id as continent_id from countries,continents,city_details where countries.country=city_details.country and continents.continent=city_details.continent and countries.continent_id=continents.id");
			stmt.execute("insert into cities (city, state_id, country_id, continent_id) select distinct city_details.city as city,states.id as state_id,countries.id as country_id,continents.id as continent_id from states,countries,continents,city_details where countries.country=city_details.country and continents.continent=city_details.continent and states.state=city_details.state and continents.id=countries.continent_id and states.country_id=countries.id");

		} catch (SQLException e) {
			logger.log(Level.INFO, ", Error loading initial data to H2 DB : ", e);
		} finally {
			ConnectionUtils.closeConnection(conn, stmt, null);
		}
		
	}
	
}
