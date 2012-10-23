package com.intland.codebeamer.wiki.plugins.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


import com.intland.codebeamer.persistence.dto.ArtifactDto;
import com.intland.codebeamer.persistence.dto.TrackerItemDto;
import com.intland.codebeamer.wiki.plugins.support.AttachmentTable;
import com.intland.codebeamer.wiki.plugins.support.GlobalVariable;
import com.intland.codebeamer.wiki.plugins.support.TicketResults;
import com.intland.codebeamer.wiki.plugins.support.VelocityTable;

/**Centralized Printer Class for SpicyAlm Plugin and StandAloneApplication
 * Provides various methods for two output templates (velocity and chart plugin).
 * Each result (attachment and trackeritem) has it own method regarding to particular classes
 * @author Alexander Börsch
 *
 */
public class Printer {
	
	private GlobalVariable globalVariable = null;
	private Reader reader = null;
	
	public Printer (GlobalVariable globalVariable, Reader reader)
	{
		this.globalVariable =globalVariable;
		this.reader = reader;
	}
	
	/**Usable for Attachment & TrackerItem-Prints in Plugin-Style
	 * @param List<List<Object>>  @return List<List<Object>>  List of Object-List this has following pattern [0] List<Object> (positive linked TrackerItems/Attachments) [1] List<Object> (negative linked TrackerItems/Attachments)
	 * @param Boolean Required to identify if attachment or tracker-specific output shall printed
	 * @return String Returns Pattern "String, int \r\n String, int \r\n" on first position is the positive and on the second negative results
	 */
	public String printPluginPattern (List<List<Object>> results, boolean attachment)
	{
		String output ="";
		int[]sizes = new int [2];
		int counter=0;
		
		Iterator<List<Object>> itrResults= results.iterator();
			
	   	while(itrResults.hasNext()) {	//runs just once [0]=posTrackerItems [1]=negTrackerItems
	   		List<Object> tempResult = itrResults.next();
	   		sizes[counter]= tempResult.size();
			counter++;
	   	}
	   	
	   	if (attachment)	//different output strings
	   	{
	   		
	   		output = " Attachments mit Tracker, " + sizes[0] + "\r\n " + "Attachments ohne Tracker, " + sizes[1] + "\r\n ";
	   	}
	   	else
	   		 output = " Tracker mit Attachments, " + sizes[0] + "\r\n " + "Tracker ohne Attachments, " + sizes[1] + "\r\n ";
	 	
	 	return output; 
		
	}

		/**Translate Integer value to String value which is readable for users
		 * @param Integer artifactType
		 * @return String
		 */
		private  String matchingArtifactType(int artifactType)
			{
				if (artifactType == GlobalVariable.attachmentType_externalFile)
					return "external File";
				if (artifactType == GlobalVariable.attachmentType_WikiPage)
					return "WikiPage";
				else
					return "<<no value matching: " + String.valueOf(artifactType) + ">>";
			}
		
		/**Provides output in table structure for velocity template
		 * @param results List of List with position [0] List<Object> (positive linked TrackerItems) [1] List<Object> (negative linked TrackerItems)
		 * @return List <VelocityTable> (getTicketID, getTicketLink, getAttachmentLink)
		 * Change "limit" paramater, otherwise standard value for ouptut limitation is 100 
		 */
		public List printTrackerItemsAscii (List<List<Object>> results, boolean notLinked)
		{	int counter =0; 
			List<VelocityTable> list = new ArrayList<VelocityTable>();
			
			Iterator<List<Object>> itrResults= results.iterator();
		   	itrResults.hasNext(); {	
		   	
		   		if (notLinked)			//if "notlinked" is true than 
		   			itrResults.next();	//jump over the positveTrackerItems
		   		List<Object> tempResult = itrResults.next();
		   		
		   		if (notLinked)	//print negative TrackerItmes which are not linked to Attachments
		   		{
		   			Iterator<Object> itrResult= tempResult.iterator();
		   			while(itrResult.hasNext()) {	
		   				TrackerItemDto tempTrackerResults = (TrackerItemDto) itrResult.next();	
		   				if (counter < reader.getOutputLimitation())	//output limitation 
		   				{
		   					Integer trackerID =  tempTrackerResults.getId();
		   					String trackerName = tempTrackerResults.getName();
		   					String trackerUrl= buildUrl ( tempTrackerResults.getUrlLink()) ;
		   					List<AttachmentTable> artifact = null;		//do not exist, because notLinked TrackerItems
		   				
		   					list.add(new VelocityTable(trackerID, trackerName,  trackerUrl , artifact));	   					
		   					
		   				}
		   				counter++;
		   				}
		   			
		   		}
		   		else	//print positive TrackerItems linked to Attachments 
		   		{
			   		Iterator<Object> itrResult= tempResult.iterator();
		   			while(itrResult.hasNext()) {	
		   				TicketResults tempTrackerResults = (TicketResults) itrResult.next();	
		   				if (counter < reader.getOutputLimitation())	//output limitation 
		   				{
		   					Integer trackerID =  tempTrackerResults.getTicket().getId();
		   					String trackerName = tempTrackerResults.getTicket().getName();
		   					String trackerUrl= buildUrl ( tempTrackerResults.getTicket().getUrlLink()) ; 
		   					
		   					List<ArtifactDto> attachments=  tempTrackerResults.getArtifacts();
		   					List<AttachmentTable> artifact = convertArtifact(attachments);	//convert Attachments to velocity template usable pattern
		   		
		   					list.add(new VelocityTable(trackerID, trackerName,  trackerUrl , artifact));	   					
		   				
		   				}
		   				counter++;
		   				}
		   			}
		   		}
		   	
		   	return list;
		}
		
		/**Converts attachments to velocity template style.
		 * After this it is possible to call a standardized AttachmentTable object through velocity 
		 * @param List<ArtifactDto> List of ArtifactDto objects are required
		 * @return  List<AttachmentTable> Returns List of AttachmentTable objects, 
		 */ 
		private List<AttachmentTable> convertArtifact(List<ArtifactDto> list)
		{
			List<AttachmentTable> outcomes = new ArrayList<AttachmentTable>();
			if (list.size()>0){
			Iterator<ArtifactDto> itrResults= list.iterator();
			while(itrResults.hasNext())
			{
				ArtifactDto tempArtifact = itrResults.next();	
				String artifactName = tempArtifact.getName();
				String artifactUrl = buildUrl(tempArtifact.getUrlLink());
				outcomes.add(new AttachmentTable (artifactName, artifactUrl) );
			}
			}
			return outcomes;
		}
		
		
		/**
		 * @param results
		 * @return List <VelocityTable> (getTicketID, getTicketLink, getAttachmentLink)
		 */
		//TODO: in process
		public List printAttachmentsAscii (List<List<Object>> results)
		{
			
			List<VelocityTable> list = new ArrayList<VelocityTable>();
			/*
			Iterator<List<Object>> itrResults= results.iterator();
		   	itrResults.hasNext(); {	//runs just once, but can goes twice: [0]=posTrackerItems [1]=negTrackerItems
		   		List<Object> tempResult = itrResults.next();
		   		
		   		Iterator<Object> itrResult= tempResult.iterator();
	   			while(itrResult.hasNext()) {	
	   				Object tempAttachment = itrResult.next();	
	   					//Integer trackerID = (((ArtifactDto)) tempAttachment).getId();
	   					String trackerUrl= buildUrl (((TrackerItemDto) tempAttachment).getUrlLink()) ; 
	   					//String attachmentUrl= ((TrackerItemDto) tempAttachment).getAttachments();	//TODO: getUrlLink -->	funktioniert dort auch	
	   					String attachmentUrl= "testUrl";
	   					
	   					
	   				//	list.add(new VelocityTable(trackerID, trackerUrl , attachmentUrl));	   					
	   					//List<ArtifactDto> a = (List<ArtifactDto>) ((TrackerItemDto) tempTrackerItem).getAttachments();	//prüfen???
	
	   				}
	   			}
	   	*/
		   	return list;
		   
		}
		
		/**Creates Url out of multiple elements
		 * @param String 
		 * @return String "http://.../ticket- or attachmenturl
		 */
		private String buildUrl(String adresseEnding)
		{
			String adresseBeginning = globalVariable.getUrl(); //"localhost:8080/cb";
			return adresseBeginning +  adresseEnding;
		}
		
		/**Translate Integer value to String value which is readable for users
		 * @param Integer associationType
		 * @return String 
		 */
		private  String matchingAssociationType(int associationType)
		{
			if (associationType == GlobalVariable.assocationType_depends)
				return "depends";
			if (associationType == GlobalVariable.assocationType_parent)
				return "parent";
			if (associationType == GlobalVariable.assocationType_child)
				return "child";
			if (associationType == GlobalVariable.assocationType_related)
				return "related";
			if (associationType == GlobalVariable.assocationType_derived)
				return "derived";
			if (associationType == GlobalVariable.assocationType_violates)
				return "violates";
			if (associationType == GlobalVariable.assocationType_invalidates)
				return "invalidates";
			if (associationType == GlobalVariable.assocationType_excludes)
				return "excludes";
			else
				return "<<no value matching: " + String.valueOf(associationType) + ">>";
		}
}
