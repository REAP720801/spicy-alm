package junit;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.intland.codebeamer.persistence.dto.ArtifactDto;
import com.intland.codebeamer.persistence.dto.TrackerItemDto;
import com.intland.codebeamer.wiki.plugins.core.Logic;

public class TestLogic {

	Logic logic =null;
	List<Object> allAssoc =null;
	List<TrackerItemDto> allTrackerItems =null;
	List<ArtifactDto> allAttachments = null;
	List<List<Object>>  results = null;
	
	@Before
	//called before all other Tests
	public void method()
	{
		logic = new Logic();
		
		GenerateTestData generateTestData = new GenerateTestData();
		
		allAssoc = generateTestData.createAssocation();
		allTrackerItems = generateTestData.createTrackerItem();
		allAttachments = generateTestData.createAttachment();
		
	}
	
	@Test
	public void testCheckAttachments() {
		fail("Not yet implemented");
	}

	@Test
	public void testCheckTrackerItems() {
		
		List<Object> positiveAttachments = new ArrayList<Object>();	//list for linked attachements with trackeritems
		List<Object> negativeAttachments = new ArrayList<Object>(); //list for not linked attachements with trackeritems
		List<List<Object>> resultsCheckedAttachments = new ArrayList<List<Object>>();
		
		resultsCheckedAttachments.add(positiveAttachments);
		resultsCheckedAttachments.add(negativeAttachments);
		
		results =logic.checkTrackerItems(allTrackerItems, null, allAssoc);
		//assertEquals (resultsCheckedAttachments,results);	//only test if output value is correct, not check if right calculation
		
		//check 
		Iterator<List<Object>> itrResults= results.iterator();
		int counter=0;
		int[]sizes = new int [2];
	   	while(itrResults.hasNext()) {	//runs just once [0]=posTrackerItems [1]=negTrackerItems
	   		List<Object> tempResult = itrResults.next();
	   		sizes[counter]= tempResult.size();
			counter++;
	   	}
	   	
	   	assertEquals (6,sizes[0]);
	   	assertEquals (4,sizes[1]);
	}

}

