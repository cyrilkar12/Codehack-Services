package com.codehack.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codehack.domain.Recommendation;
import com.codehack.domain.Recommendations;

@RestController
@RequestMapping("/recommender")
public class RecommendationService {
	
	@RequestMapping(value = "/getFPNextPages", method= RequestMethod.POST)
	public Recommendations retrieveNextPage(@RequestParam("visitedPages")
	final String visitedPages){
		//visitedPages is Json with the below format
		/*
		 * {
    "visitedPages": [
        {
            "pageName": "CaisoWatch",
        },
        {
            "pageName": "CasioGshock",
        },
        {
            "pageName": "CasioProTrack",
        },
        {
            "pageName": "CasioEdifice",
        }
    ]
}
		 */
		Recommendations recommedations = new Recommendations();
/*		Recommendation re1 = new Recommendation();
		re1.setPageName("test1");
		re1.setPageURL("test1");
		Recommendation re2 = new Recommendation();
		re2.setPageName("test2");
		re2.setPageURL("test2");
		List<Recommendation> lstReco = new ArrayList<>();
		lstReco.add(re1);
		lstReco.add(re2);
		recommedations.setRecommendations(lstReco);
*/		
		return recommedations;
	}
	
	
	@RequestMapping(value = "/getRatingBasedRecommends", method= RequestMethod.POST)
	public Recommendations ratingBasedRecommendations(@RequestParam("userId")
	final String userId){
		//userId is Json with the below format
		/*
		 * {"userID":"user1"}
		 */
		Recommendations recommedations = new Recommendations();
		return recommedations;
	}
	
	

}
