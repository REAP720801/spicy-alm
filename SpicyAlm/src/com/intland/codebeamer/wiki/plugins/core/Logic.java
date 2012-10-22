package com.intland.codebeamer.wiki.plugins.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.intland.codebeamer.persistence.dto.ArtifactDto;
import com.intland.codebeamer.persistence.dto.TrackerItemDto;
import com.intland.codebeamer.wiki.plugins.support.TicketResults;

/**Centralized Logic Class for SpicyAlm Plugin and StandAloneApplication
 * Provides logic to identify if a trackeritem has an attachment or not
 * Provides logic to identify if a attachment has a trackeritem or not
 * @author Alexander B�rsch
 *
 */
public class Logic {

	/**Checks Attachment if it has minimum one Association to a TrackerItem
	 * @param List<TrackerItemDto> List of TrackerItemDto objects
	 * @param List<ArtifactDto> List of ArtifactDto objects
	 * @param List<int[]> Expecting a List filled with an Array (Pattern: [0]=assoc-object [1]=trackerItem-object [2]=attachment-object)
	 * @return List<List<Object>>  List of Object-List this has following pattern [0] List<Object> (positive linked Attachments) [1] List<Object> (negative linked Attachments)
	 */ 
	//TODO: in Process: Matching has some errors
	public List<List<Object>> checkAttachments (List <TrackerItemDto> allTrackerItems, List <ArtifactDto> allAttachments, List<Object> AllAssociations )
	{
		List<Object> positiveAttachments = new ArrayList<Object>();	//list for linked attachements with trackeritems
		List<Object> negativeAttachments = new ArrayList<Object>(); //list for not linked attachements with trackeritems
		List<List<Object>> resultsCheckedAttachments = new ArrayList<List<Object>>();
		
		/*
		//iterate through all Attachments
		Iterator<ArtifactDto> itrAllAttachments = allAttachments.iterator();
	   	while(itrAllAttachments.hasNext()) {
	   		ArtifactDto tempAttachment = itrAllAttachments.next();
	    	
	   		boolean tempMarker=false;
	   		
	   		//iterate through all Associations
	    	Iterator<Object> itrAssoc = AllAssociations.iterator();	
			while(itrAssoc.hasNext()) {
				Object[] tempAssoc = (Object[]) itrAssoc.next();	//Pattern: [0]=AssociationDto [1]=TrackerItemDto [2]=AttachmentDto)
				ArtifactDto attachmentA = (ArtifactDto) tempAssoc[2];	
					System.out.println(attachmentA.getName() + " " +tempAttachment.getName() );
					//compare if Attachment-ID (from Association) equal to Attachment-ID
					if (attachmentA.equals(tempAttachment.getId())) //compare Attachment (out of AllAssociations-List) to all available Attachment
					{
						//yes, if Attachment does not exist in positiveAttachment-List already, try to avoid double entries
						if (!positiveAttachments.contains(tempAttachment))	
				    	{
						positiveAttachments.add(tempAttachment);
						tempMarker=true;
				    	}
					}	
			}
			
			//compare if not existing Attachment-Association is already noted in negative list, try to avoid double entries
			if (!negativeAttachments.contains(tempAttachment))	//yes, if tempTrackerItem does not exist in List already, try to avoid double entries
	    	{	
				if (!tempMarker)	//if tempMarker still "false"
				{
					negativeAttachments.add(tempAttachment);
				}
	    	}
		}
	   	*/
	   	resultsCheckedAttachments.add(positiveAttachments);
	   	resultsCheckedAttachments.add(negativeAttachments);
		return resultsCheckedAttachments;
	}
	
	/**Checks TrackerItem if it has minimum one Association to a Attachment
	 * @param List<TrackerItemDto> List of TrackerItemDto objects
	 * @param List<ArtifactDto> List of ArtifactDto objects
	 * @param List<int[]> Expecting a List filled with an Array (Pattern: [0]=assoc-object [1]=trackerItem-object [2]=attachment-object)
	 * @return List<List<Object>> List of Object-List this has following pattern [0] List<Object> (positive linked TrackerItems) [1] List<Object> (negative linked TrackerItems)
	 */
	public List<List<Object>> checkTrackerItems (List <TrackerItemDto> allTrackerItems, List <ArtifactDto> allAttachments, List<Object> AllAssociations )
	{
		//List<Object> positiveTrackerItems = new ArrayList<Object>(); //of type TrackerItemDto
		List<Object> negativeTrackerItems = new ArrayList<Object>(); //of type TrackerItemDto
		List<Object> positiveTrackerItems = new ArrayList<Object>();	//of type TicketResults
		List<List<Object>> resultsCheckedTracker = new ArrayList<List<Object>>();
		
		Iterator<TrackerItemDto> itrTrackerItems = allTrackerItems.iterator();
	   	while(itrTrackerItems.hasNext()) {	//goes through AllTrackerItems-List
	   		TrackerItemDto tempTrackerItem = itrTrackerItems.next();
	   		boolean tempMarker=false;
	   		
	    	Iterator<Object> itrAssoc = AllAssociations.iterator();
			while(itrAssoc.hasNext()) {	//goes through AllAssociations-List
				Object[] tempAssoc = (Object[]) itrAssoc.next();	//Pattern: [0]=AssociationDto [1]=TrackerItemDto [2]=AttachmentDto)
				TrackerItemDto trackerItemA = (TrackerItemDto) tempAssoc[1];
				if (trackerItemA.equals(tempTrackerItem)) //compare TrackerItem (out of AllAssociations-List) to all available TrackerItems
					{
				    		//check if trackerA is alreday included
				    		TicketResults tempTicketResult=null;	//for positive finding 
					    	Iterator<Object> itrPositiveTrackerItems = positiveTrackerItems.iterator();
							while(itrPositiveTrackerItems.hasNext()) {	//goes through AllAssociations-List
								TicketResults tempTicketResults = (TicketResults) itrPositiveTrackerItems.next();
								if (tempTicketResults.getTicket().getId().compareTo(trackerItemA.getId()) ==0)
								{
									tempTicketResult = tempTicketResults;
								}
							}
				    	if (tempTicketResult==null)	//no entry exits
				    	{
				    		tempTicketResult = new TicketResults(tempTrackerItem);
				    		tempTicketResult.setArtifacts((ArtifactDto)tempAssoc[2]);
				    		positiveTrackerItems.add(tempTicketResult);	//add entry only once to List, not multiple times 
				    	}
				    	else	//entry already exists
				    	{
				    		tempTicketResult.setArtifacts((ArtifactDto)tempAssoc[2]); //add value in existing list entry
				    	}
						tempMarker=true;	
					}	
			}

			//compare if not existing TrackerItem-Association is already noted in negative, try to avoide double entries
			if (!negativeTrackerItems.contains(tempTrackerItem))	
	    	{	
				if (!tempMarker)	//if tempMarker still "false"
				{
					negativeTrackerItems.add(tempTrackerItem);
				}
	    	}
		}
	   	resultsCheckedTracker.add(positiveTrackerItems);
	   	resultsCheckedTracker.add(negativeTrackerItems);
		return resultsCheckedTracker;
	}


}
