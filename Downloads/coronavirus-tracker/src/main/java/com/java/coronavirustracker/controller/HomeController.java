package com.java.coronavirustracker.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.java.coronavirustracker.models.LocationStats;
import com.java.coronavirustracker.services.CoronaVirusDataService;

@Controller
//@controller returns a HTML UI
//@restController returns a JSON response
public class HomeController {
	
	@Autowired
	CoronaVirusDataService coronaVirusDataService;
	@GetMapping("/")
	//When you call controller, you can put things in the model and access things in the html
   public String home(Model model) {
		//this works because of the thymleaf dependency
		List<LocationStats> allStats=coronaVirusDataService.getAllStats();
		int totalReportedCases=allStats.stream().mapToInt(stat->stat.getLatestTotalCases()).sum();
		int totalNewCases=allStats.stream().mapToInt(stat->stat.getDiffFromPrevDay()).sum();
		model.addAttribute("locationStats", coronaVirusDataService.getAllStats());
		model.addAttribute("totalReportedCases",totalReportedCases);
		model.addAttribute("totalNewCases",totalNewCases);
		//return home template
	   return "home";
   }
}
