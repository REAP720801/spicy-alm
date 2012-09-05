package junit;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.intland.codebeamer.persistence.dto.ArtifactDto;
import com.intland.codebeamer.persistence.dto.AssociationDto;
import com.intland.codebeamer.persistence.dto.ProjectDto;
import com.intland.codebeamer.persistence.dto.TrackerItemDto;
import com.intland.codebeamer.wiki.plugins.Printer;

public class TestCasePrinter {

	Printer printer = null;
	@Before 
	public void method()
	{
		printer = new Printer();
	}
	
	//Funktionalität testene
	//Vorbedingung
	//Fehler	bsp. durch @Test(expected = IndexOutOfBoundsException.class)
	//TODO: doppelte Cases
	
	//TODO: Ausgaben in Methoden anpassen
	
	@Test
	public void testPrintPositiveMatchingResults() {
		List<List<Object>> resultsCheckedAtt = null;
		
		//testing NullPointerException
		assertNotNull(printer.printPositiveAttachments(resultsCheckedAtt));
		
		//testing NoSuchElementException
		resultsCheckedAtt = new ArrayList<List<Object>>();
	assertNotNull( printer.printPositiveAttachments(resultsCheckedAtt));
		
		//testing NullPointerException through Return-Object-Type-Check
	assertNotNull( printer.printPositiveAttachments(null));
		
		//testing NullPointerException through Return-Object-Type-Check, with real data	//TODO: ??sinn
		List<Object> tempObject = new ArrayList<Object>();
		ArtifactDto tempArtifact = new ArtifactDto();
		tempObject.add(tempArtifact);	//create Artifact which are "positive matched"
		tempObject.add(tempArtifact);	//create Artifact 
		resultsCheckedAtt.add(tempObject);	
	assertNotNull( printer.printPositiveAttachments(resultsCheckedAtt));
	
		//testing if numbers of input is equal to numbers of output
		//input = 2 Artifacte, expected output is 2
	assertTrue (printer.printPositiveAttachments(resultsCheckedAtt).size() ==2 );
		
	}	

	/*
	@Test
	public void testPrintPositiveTrackerItem() {
	List<List<Object>> resultsCheckedAtt = null;
		
		//Testing NullPointerException AND return-values is null
	assertNotNull(printer.printPositiveTrackerItem(resultsCheckedAtt));
		
		//testing NoSuchElementException
		resultsCheckedAtt = new ArrayList<List<Object>>();
	assertNotNull( printer.printPositiveTrackerItem(resultsCheckedAtt));
		
		//testing NullPointerException through Return-Object-Type	
	assertNotNull( printer.printPositiveTrackerItem(null));
	
		List<Object> tempObject = new ArrayList<Object>();
		TrackerItemDto tempAssoc  = new TrackerItemDto();
		tempObject.add(tempAssoc);	//create TrackerItem which are "positive matched"
		tempObject.add(tempAssoc);	//create TrackerItem 
		resultsCheckedAtt.add(tempObject);
		resultsCheckedAtt.add(tempObject);
		resultsCheckedAtt.add(tempObject);	////List [positiveAttachments],[negativeAttachments],[trackerItems],[associations]
	assertNotNull(printer.printPositiveTrackerItem(resultsCheckedAtt));
	
		//testing if numbers of input is equal to numbers of output
		//input = 2 Artifacte, expected output is 2
	assertTrue (printer.printPositiveTrackerItem(resultsCheckedAtt).size() ==2 );
	}
*/
	@Test
	public void testPrintPositiveAssoc() {
		List<List<Object>> resultsCheckedAtt = null;
		
		//Testing NullPointerException AND return-values is null
	assertNotNull(printer.printPositiveAssoc(resultsCheckedAtt));
		
		//testing NoSuchElementException
		resultsCheckedAtt = new ArrayList<List<Object>>();
	assertNotNull( printer.printPositiveAssoc(resultsCheckedAtt));
		
		//testing NullPointerException through Return-Object-Type	
	assertNotNull( printer.printPositiveAssoc(null));
	
		List<Object> tempObject = new ArrayList<Object>();
		AssociationDto tempAssoc  = new AssociationDto();
		tempObject.add(tempAssoc);	//create Association which are "positive matched"
		tempObject.add(tempAssoc);	//create Association 
		resultsCheckedAtt.add(tempObject);
		resultsCheckedAtt.add(tempObject);
		resultsCheckedAtt.add(tempObject);
		resultsCheckedAtt.add(tempObject);	////List [positiveAttachments],[negativeAttachments],[trackerItems],[associations]
	assertNotNull(printer.printPositiveAssoc(resultsCheckedAtt));
	
		//testing if numbers of input is equal to numbers of output
		//input = 2 Artifacte, expected output is 2
	assertTrue (printer.printPositiveAssoc(resultsCheckedAtt).size() ==2 );

	}

	@Test
	public void testPrintNegativeMatchingResults() {
		//fail("Not yet implemented");
		
		List positveResults = new ArrayList(); 
		List<List<Object>> resultsCheckedAtt = null;
		
		//testing NullPointerException printPositiveAttachments
		assertNotNull(printer.printNegativeAttachments(resultsCheckedAtt));
		
		//testing NoSuchElementException
		resultsCheckedAtt = new ArrayList<List<Object>>();
	assertNotNull( printer.printNegativeAttachments(resultsCheckedAtt));
		
		//testing NullPointerException through Return-Object-Type	
	assertNotNull( printer.printNegativeAttachments(null));
		
		//testing NullPointerException through Return-Object-Type
		List<Object> tempObject = new ArrayList<Object>();
		ArtifactDto tempArtifact = new ArtifactDto();
		tempObject.add(tempArtifact);	//create Artifact which are "positive matched"
		tempObject.add(tempArtifact);	//create Artifact 
		resultsCheckedAtt.add(tempObject);	
		resultsCheckedAtt.add(tempObject);	//List [positiveAttachments],[negativeAttachments],[trackerItems],[associations]
	assertNotNull( printer.printNegativeAttachments(resultsCheckedAtt));
	
		//testing if numbers of input is equal to numbers of output
		//input = 2 Artifacte, expected output is 2
	assertTrue (printer.printNegativeAttachments(resultsCheckedAtt).size() ==2 );
	
	}

	@Test
	public void testPrintAllAssociations() {

		AssociationDto[] allAssoc = null;
		//testing NullPointerException
	assertNotNull(printer.printAllAssociations(allAssoc));
		
		//testing return 
		allAssoc = new AssociationDto[1];
		allAssoc[0] = new AssociationDto();
	assertNotNull( printer.printAllAssociations(allAssoc));
	
		//input = 1 Association, expected output is 1
	assertTrue (printer.printAllAssociations(allAssoc).size() ==1 );
	
		//TODO: test wrong input-Type
		
	}

	@Test
	//TODO: test einbauen für objekt-typ als input und output bei ALLEN
	public void testPrintAllAttachmentsByProject() {
		List<ArtifactDto>  attachments = null;
		//Testing NullPointerException AND return-values is null
		assertNull(printer.printAllAttachmentsByProject (attachments));
		
		//Testing output
		attachments = new ArrayList<ArtifactDto>();
		assertNotNull(printer.printAllAttachmentsByProject (attachments));
		
		//input = 1 attachments, expected output is 1
		attachments.add(new ArtifactDto());
		assertTrue (printer.printAllAttachmentsByProject(attachments).size() ==1 );
	}

	@Test
	public void testPrintAllProjects() {
		
		ProjectDto[] allProjects = null;
		//testing NullPointerException
	assertNotNull(printer.printAllProjects(allProjects));
		
		//testing return Object 
		allProjects = new ProjectDto[1];
		allProjects[0] = new ProjectDto();	
	assertNotNull( printer.printAllProjects(allProjects));
	
		//input = 1 prject, expected output is 1
	assertTrue (printer.printAllProjects(allProjects).size() ==1 );
	}

}
