package junit;

import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.intland.codebeamer.persistence.dto.ArtifactDto;
import com.intland.codebeamer.persistence.dto.TrackerItemDto;
import com.intland.codebeamer.wiki.plugins.core.Logic;
import com.intland.codebeamer.wiki.plugins.core.Printer;
import com.intland.codebeamer.wiki.plugins.support.AttachmentTable;
import com.intland.codebeamer.wiki.plugins.support.GlobalVariable;
import com.intland.codebeamer.wiki.plugins.support.VelocityTable;

public class TestPrinter {

	Logic logic =null;
	Printer printer = null;
	
	List<Object> allAssoc =null;
	List<TrackerItemDto> allTrackerItems =null;
	List<ArtifactDto> allAttachments = null;
	
	List<List<Object>>  results = null;
	
	@Before
	//called before all other Tests
	public void method()
	{
		logic = new Logic();
		
		GlobalVariable globalVariable = new GlobalVariable();
		globalVariable.setUrl("http://"+ "localhost" + ":" + 8080 + "/cb" ); 
		printer = new Printer(globalVariable, null);
		
		//generate test data 
		GenerateTestData generateTestData = new GenerateTestData();
		
		allAssoc = generateTestData.createAssocation();
		allTrackerItems =generateTestData.createTrackerItem();
		allAttachments = generateTestData.createAttachment();
		
		//calculate generated data 
		results =logic.checkTrackerItems(allTrackerItems, null, allAssoc);
	}
	
	@Test
	public void testPrintPluginPattern() {

		String output = printer.printPluginPattern(results, false);
		String original = " Tracker mit Attachments, " + 6 + "\r\n " + "Tracker ohne Attachments, " + 4 + "\r\n ";
		assertEquals (original, output);	//compares both string objects
	}

	@Test
	public void testPrintTrackerItemsAscii() {
		List<VelocityTable> table = printer.printTrackerItemsAscii(results,false);
		
		//Print simulation
		Iterator<VelocityTable> itrResults= table.iterator();
		int counter=0;
	   	while(itrResults.hasNext()) {	//runs just once, later //VelocityTable-Object 
	   		VelocityTable tempResult =  itrResults.next();
	   			System.out.println (tempResult.getTicketID() + " " + tempResult.getTicketLink()); 
	   			
	   			List<AttachmentTable> attResults = tempResult.getAttachment();
	   			counter++;
	   	}
	   	assertEquals(6,counter);	//just check the quantity, not content
	   	
		
		/*
	   	table = printer.printTrackerItemsAscii(results,true);
		//Print simulation
		    itrResults= table.iterator();
		   	while(itrResults.hasNext()) {	//runs just once, later //VelocityTable-Object 
		   		VelocityTable tempResult =  itrResults.next();
		   			System.out.println (tempResult.getTicketID() + " " + tempResult.getTicketLink()); 
		   			
		   			List<AttachmentTable> attResults = tempResult.getAttachment();
		   			if (true) //needs it, otherwise nullpointerException of calling not existing Artifacts
			   			{
			   			Iterator<AttachmentTable> itrAttResults= attResults.iterator();
					   	while(itrAttResults.hasNext())
					   	{
					   		AttachmentTable tempAttResult =  itrAttResults.next();
					   		System.out.println("--" + tempAttResult.getName() + " " + tempAttResult.getUrl() );
					   	}
		   			}
		   	}
		   	*/
	}

	@Test
	public void testPrintAttachmentsAscii() {
		fail("Not yet implemented");
	}

}
