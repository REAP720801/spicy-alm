/**
 * 
 */
package com.intland.codebeamer.wiki.plugins;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.intland.codebeamer.manager.ArtifactManager;
import com.intland.codebeamer.manager.AssociationManager;
import com.intland.codebeamer.manager.ProjectManager;
import com.intland.codebeamer.manager.TrackerItemManager;
import com.intland.codebeamer.manager.WikiPageManager;
import com.intland.codebeamer.persistence.dto.ArtifactDto;
import com.intland.codebeamer.persistence.dto.AssociationDto;
import com.intland.codebeamer.persistence.dto.ProjectDto;
import com.intland.codebeamer.persistence.dto.TrackerItemDto;
import com.intland.codebeamer.persistence.dto.TrackerItemDto.Flag;
import com.intland.codebeamer.persistence.dto.UserDto;
import com.intland.codebeamer.persistence.dto.WikiPageDto;
import com.intland.codebeamer.persistence.dto.base.ReferableDto;
import com.intland.codebeamer.wiki.plugins.core.Reader;
import com.intland.codebeamer.wiki.plugins.support.GlobalVariable;


/**Reader Class for Service Layer
 * Provides Core Business Logic of Codebeamer version 6.0.2
 * @author Alexander Börsch
 * September 2012
 *
 */
public class SLReader {
	private UserDto user=null;
	private Reader readerObject = null;
	
	public SLReader(UserDto user, Reader readerObject) {
		this.user = user;
		this.readerObject = readerObject;
	}
	
	/**Reads all user available Projects from CodeBeamer
	 * @return int [] projectIDs
	 */
	public int[] readAllProjects () 
	{
		List<ProjectDto> projects = ProjectManager.getInstance().findAll(user);
		int[] tempArray = new int[projects.size()];
		int i=0;
		Iterator<ProjectDto> itrProjects = projects.iterator();
	   	while(itrProjects.hasNext()) {
	   		ProjectDto tempArtifact = itrProjects.next();	
	   		tempArray[i]=tempArtifact.getId();
	   		i++;
		}
		return tempArray;	
	}
	
	/**Returns all the artifacts in a single project, with given ArtifactType type.
	 * Artifact type has to be an integer variable with the value "1", for external Files
	 * @param int [] projectids, int
	 * @return List<ArtifactDto>
	 */
	//TODO: artifakt limiation einbauen
	public List<ArtifactDto> readAllAttachmentsByProject (int[] projects, int type)
	{
		List<ArtifactDto> attachments = new ArrayList<ArtifactDto>();
		
		for (int i = 0; i<projects.length; i++)
		{
		List<ArtifactDto> tempAtt= ArtifactManager.getInstance().findByProjectAndType(user, projects[i], type);
		attachments.addAll(tempAtt);
		}
		return attachments;
	}
	
	/**Returns all TrackerItems from a single Project, based on Tracker-Type and TrackerItem-Status
	 * Tracker-Type can be:
	 *  Requirement 	: 3
 		Change Request  : 4
 		Bug				: 5
 		Task 			: 6
	 * @param trackerID Integer array
	 * @parm TrackerItem.Flag Set<TrackerItemDto.Flag> 
	 * @return List <TrackerItemDto> List of TrackerItemDto Objects
	 */
	public List <TrackerItemDto> readAllTrackerItems (int[] trackerID, Set<Flag> aggregated)
	{	
		//Items auslesen
		List<Integer> list1 = new ArrayList<Integer>();
	    for (int index = 0; index < trackerID.length; index++)
	    {
	    	list1.add(trackerID[index]);
	    }

		List <TrackerItemDto> items= TrackerItemManager.getInstance().findByTracker(user, list1, aggregated);
		return items;
	}
	
	/**Finds specific CB-Project by Name
	 * @param projects Integer Array
	 * @param projectName String
	 * @return int[] with projectID
	 */
	public int[] findProject (int[] projects, String projectName)
	{
		ProjectDto tempProject = ProjectManager.getInstance().findByName(user, projectName);
		int[] tempArray = {tempProject.getId()};
		return tempArray;
	}

	/**Returns all available Associations for current user
	 * @return  List<Object> List of Object[] pattern: [0]=assoc-object [1]=trackerItem-object [2]=attachment-object)
	 */
	public List<Object> readAllAssociationsTracker()	//from current project
	{
		List<Object> allAssoc = new ArrayList<Object>();
		String tempString ="";
		
		try {
		//all Associations, unabhängig welcher Tracker
		List<AssociationDto<?,?>> tempAllAssoc = AssociationManager.getInstance().findAll(user);
		
		Iterator<AssociationDto<?, ?>> itrAllAssoc =null;
		try {
			itrAllAssoc = tempAllAssoc.iterator();
   		}
			catch (NullPointerException e)
		{
			readerObject.errorMessage = readerObject.errorMessage +  " 1.0 " + e.getMessage();
		}
		
	   	while(itrAllAssoc.hasNext()) {	//goes through AssociationDto<?,?>-List
	   		AssociationDto<?, ?> tempAssoc = itrAllAssoc.next();
	   		AssociationDto<?, ?> assoc = null;
	   		
	   		try {
	   			assoc = AssociationManager.getInstance().findById(user, tempAssoc.getId());
	   			tempString =  tempAssoc.getId().toString();
	   		}
   			catch (NullPointerException e)
			{
				readerObject.errorMessage = readerObject.errorMessage +  " 1.1 " + e.getMessage();
			}
	   		
	   		ReferableDto originTo = null;
	   		ReferableDto originFrom = null;

	   		try {
				 originTo = assoc.getTo().getDto();
				 originFrom = assoc.getFrom().getDto();				
	   		}
	   		catch (NullPointerException e)
			{
				readerObject.errorMessage = readerObject.errorMessage +  " 1.2 " + e.getMessage() + " " +tempString;
			}		

			ArtifactDto toArtifact =null;
			TrackerItemDto fromItem =null;
			
			if (originTo instanceof TrackerItemDto) {
				try {		//to avoid CastExceptions
					fromItem = (TrackerItemDto) originTo;
					toArtifact = (ArtifactDto) originFrom;
					}
					catch (ClassCastException e)
					{
						readerObject.errorMessage = readerObject.errorMessage +  " 1.3 " + e.getMessage() + " " +tempString;
					}
		   			catch (NullPointerException e)
					{
						readerObject.errorMessage = readerObject.errorMessage +  " 1.4 " + e.getMessage() + " " +tempString;
					}
			}

			else if (originFrom instanceof TrackerItemDto) {
					try {	  //to avoid CastExceptions
						fromItem = (TrackerItemDto) originFrom;
						toArtifact =(ArtifactDto) originTo;
					}
					catch (ClassCastException e)
					{
						readerObject.errorMessage = readerObject.errorMessage + " 1.5 " + e.getMessage() + " " +tempString;
					}
		   			catch (NullPointerException e)
					{
						readerObject.errorMessage = readerObject.errorMessage +  " 1.6 " + e.getMessage() + " " +tempString;
					}
			}	
				
				if (toArtifact!=null && fromItem !=null)
				{
				Object[] tempFinalAssoc = new Object [3]; //[0]=AssociationDto object [1]=trackerItem object [2]=attachment object)
				tempFinalAssoc[0] = assoc;
				tempFinalAssoc[1] = fromItem;  //trackeritem
				tempFinalAssoc[2] = toArtifact;	//artifact
				
				if (readerObject.getArtifactLimitation() && (toArtifact.getTypeId() == GlobalVariable.attachmentType_externalFile)  )
					allAssoc.add(tempFinalAssoc);	//add only Association which has am artifact of type "external file" according to user input
				if (!readerObject.getArtifactLimitation()) //for no user artifact limitation 
					allAssoc.add(tempFinalAssoc);	
				//System.out.println(assoc.getId() +" " +assoc.getFrom().getId() + " " + assoc.getTo().getId());
				}
			}
		}
		catch (NullPointerException e)
		{
			readerObject.errorMessage = readerObject.errorMessage +  " 1.7 " + e.getMessage();
		}
		return allAssoc;
	} 

	
	/**Get wikiPage out of Context
	 * Cast String value to int and find one WikiPageDto.
	 * @param tempWikiID String value contains id
	 * @return List<WikiPageDto> Returns List of WikiPageDto items
	 */
	public List<WikiPageDto> readWikiPage (int wikiID)
	{
		
		WikiPageDto wikiPage = WikiPageManager.getInstance().findById(user, wikiID);
		List <WikiPageDto> items= new ArrayList<WikiPageDto>();
		items.add(wikiPage);
		return items;
	}
	
	/**Provides all available associations (with linked WikiPages and Artifact)  for current user 
	 * @return List<Object> List includes Arrays of Objects, Array positions [0]=AssociationDto Object [1]=WikiPageDto Object [2]=ArtifactDto Object
	  pre-check if associations are linked to wikpages, artifact's limitation parameter only for "file" (external attachments)
	 */
	public List<Object> readAllAssociationsWiki()	
	{
		List<Object> allAssoc = new ArrayList<Object>();
		String tempString ="";
		
		try {
		//all Associations
		List<AssociationDto<?,?>> tempAllAssoc = AssociationManager.getInstance().findAll(user);
		
		Iterator<AssociationDto<?, ?>> itrAllAssoc =null;
		try {
			itrAllAssoc = tempAllAssoc.iterator();
   		}
			catch (NullPointerException e)
		{
			readerObject.errorMessage = readerObject.errorMessage +  " 2.0 " + e.getMessage();
		}
		
	   	while(itrAllAssoc.hasNext()) {	//goes through AssociationDto<?,?>-List
	   		AssociationDto<?, ?> tempSingleAssoc = itrAllAssoc.next();
	   		AssociationDto<?, ?> assoc = null;
	   		
	   		try {
	   			assoc = AssociationManager.getInstance().findById(user, tempSingleAssoc.getId());	
	   			tempString =  tempSingleAssoc.getId().toString();
	   		}
   			catch (NullPointerException e)
			{
				readerObject.errorMessage = readerObject.errorMessage +  " 2.1 " + e.getMessage();
			}		
	   		
	   		ReferableDto originFrom = null;
	   		ReferableDto originTo = null;
	   		
	   		try {
				originFrom = assoc.getFrom().getDto();
				originTo = assoc.getTo().getDto();
	   		}
   			catch (NullPointerException e)
			{
				readerObject.errorMessage = readerObject.errorMessage +  " 2.2 " + e.getMessage() + " " +tempString;
			}	

			WikiPageDto wikiPage =null;
			ArtifactDto artifact = null;
			
			if (originFrom instanceof WikiPageDto) {
				try {		//to avoid CastExceptions
				wikiPage = (WikiPageDto) originFrom;
				artifact = (ArtifactDto)originTo;
				}
				catch (ClassCastException e)
				{
					readerObject.errorMessage = readerObject.errorMessage +  " 2.3 " + e.getMessage() + " " +tempString;
				}
			}
			
		if (originTo instanceof WikiPageDto) {
				try {		//to avoid CastExceptions
					wikiPage = (WikiPageDto) originTo;
					artifact = (ArtifactDto) originFrom;
				}
				catch (ClassCastException e)
				{
					readerObject.errorMessage = readerObject.errorMessage +  " 2.4 " + e.getMessage() + " " +tempString;
				}
			
			if (artifact instanceof WikiPageDto)
			{
				//change objects, one use case could be that two wikipages are linked to each other and this part corrects the order
				try {		//to avoid CastExceptions
					WikiPageDto temp = wikiPage;
					wikiPage = (WikiPageDto)artifact;
					artifact = temp;
				}
				catch (ClassCastException e)
				{
					readerObject.errorMessage = readerObject.errorMessage +  " 2.5 " + e.getMessage() + " " +tempString;
				}
			}	
		}
				
				if (wikiPage !=null && artifact != null){
				//System.out.println("READ: " + wikiPage.getName() + " "  + wikiPage.getId());
				Object[] tempAssoc = new Object [3]; //[0]=AssociationDto object [1]=trackerItem object [2]=attachment object)
				tempAssoc[0] = assoc;
				tempAssoc[1] = wikiPage;  //wikiPage
				tempAssoc[2] = artifact; //artifact
								
				//limitation check "file" 
				if (readerObject.getArtifactLimitation() && (artifact.getTypeId() == GlobalVariable.attachmentType_externalFile)  )
					allAssoc.add(tempAssoc);	//add only Association which has an artifact of type "external file" according to user input
				if (!readerObject.getArtifactLimitation()) //for no user artifact limitation 
					allAssoc.add(tempAssoc);	
			
				
				//System.out.println(assoc.getId() +" " +assoc.getFrom().getId() + " " + assoc.getTo().getId());
				}
			}
		}
		catch (NullPointerException e)
		{
			readerObject.errorMessage = readerObject.errorMessage +  " 2.6 " + e.getMessage();
		}
		
		return allAssoc;
	}
}
