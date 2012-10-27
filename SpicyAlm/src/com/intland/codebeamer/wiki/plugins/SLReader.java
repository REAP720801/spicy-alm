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
import com.intland.codebeamer.persistence.dto.ArtifactDto;
import com.intland.codebeamer.persistence.dto.AssociationDto;
import com.intland.codebeamer.persistence.dto.ProjectDto;
import com.intland.codebeamer.persistence.dto.TrackerItemDto;
import com.intland.codebeamer.persistence.dto.TrackerItemDto.Flag;
import com.intland.codebeamer.persistence.dto.UserDto;
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
	//TODO: not always TrackerItemDto object
	public List<Object> readAllAssociations()	//from current project
	{
		//all Associations, unabhängig welcher Tracker
		List<AssociationDto<?,?>> tempAllAssoc = AssociationManager.getInstance().findAll(user);
		List<Object> allAssoc = new ArrayList<Object>();
		
		String test = "";
		Iterator<AssociationDto<?, ?>> itrAllAssoc = tempAllAssoc.iterator();
	   	while(itrAllAssoc.hasNext()) {	//goes through AssociationDto<?,?>-List
	   		AssociationDto<?, ?> tempAssoc = itrAllAssoc.next();
	   		AssociationDto<?, ?> assoc = AssociationManager.getInstance().findById(user, tempAssoc.getId());	
	   		
				ReferableDto originTo = assoc.getTo().getDto();
				ReferableDto originFrom = assoc.getFrom().getDto();

				ArtifactDto toArtifact =null;
				if (originTo instanceof ArtifactDto) {
					 toArtifact = (ArtifactDto) originTo;
				}
					
				TrackerItemDto fromItem =null;
				if (originFrom instanceof TrackerItemDto) {
					fromItem = (TrackerItemDto) originFrom;
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
		return allAssoc;
	} 

}
