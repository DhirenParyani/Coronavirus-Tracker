package com.java.coronavirustracker.services;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.java.coronavirustracker.models.LocationStats;

import lombok.Getter;

@Service
//Spring is going to create an instance of this since it is annotated as a service
public class CoronaVirusDataService {
	private static String VIRUS_DATA_URL="https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";
	@Getter(lazy = true)
	private List<LocationStats> allStats;
	//execute this when application starts
	@PostConstruct
	//We want it to run on regular basis
	//this method schedules the run of a method on regular basis
	//this registers with spring
	//sec min hour
	//1st hour of everyday
	//once everydy
	@Scheduled(cron="* * 1 * * *")
	public void fetchVirusData() throws IOException, InterruptedException
	{
		List<LocationStats> newStats=new ArrayList<>();
		HttpClient  client=HttpClient.newHttpClient();
		//
		//creating a new request using Builder pattern
		HttpRequest request=HttpRequest.newBuilder().uri(URI.create(VIRUS_DATA_URL)).build();
		//HttpResponse.BodyHandlers.ofString() - take the body and return it as a string
		HttpResponse<String> httpResponse=client.send(request, HttpResponse.BodyHandlers.ofString());
		System.out.println(httpResponse.body());
		//String Reader is an instance of reader which parses string
		StringReader csvBodyReader=new StringReader(httpResponse.body());
		//parsing CSV
		Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvBodyReader);
		
		for (CSVRecord record : records) {
			LocationStats locationStats=new LocationStats();
			locationStats.setState(record.get("Province/State"));
			locationStats.setCountry(record.get("Country/Region"));
			locationStats.setLatestTotalCases(Integer.parseInt(record.get(record.size()-1)));
			System.out.println(locationStats);
			newStats.add(locationStats);
		
		    String state = record.get("Province/State");
		    System.out.println(state);
		   
		}
		this.allStats=newStats;
		
	}

}
