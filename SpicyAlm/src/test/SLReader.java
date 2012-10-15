/**
 * 
 */
package test;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.ecyrd.jspwiki.WikiContext;
import com.intland.codebeamer.manager.ArtifactManager;
import com.intland.codebeamer.manager.AssociationManager;
import com.intland.codebeamer.manager.ProjectManager;
import com.intland.codebeamer.manager.TrackerItemAttachmentManager;
import com.intland.codebeamer.manager.TrackerItemManager;
import com.intland.codebeamer.persistence.dto.ArtifactDto;
import com.intland.codebeamer.persistence.dto.AssociationDto;
import com.intland.codebeamer.persistence.dto.ProjectDto;
import com.intland.codebeamer.persistence.dto.TrackerItemAttachmentDto;
import com.intland.codebeamer.persistence.dto.TrackerItemDto;
import com.intland.codebeamer.persistence.dto.TrackerItemDto.Flag;
import com.intland.codebeamer.persistence.dto.UserDto;
import com.intland.codebeamer.remoting.RemoteApi;
import com.intland.codebeamer.remoting.RemoteApiFactory;

/**Reader Class for Service Layer
 * Provides Core Business Logic of Codebeamer version 6.0.2
 * @author Alexander Börsch
 * September 2012
 *
 */
public class SLReader {
	private UserDto user=null;
	
	public SLReader(UserDto user) {
		this.user = user;
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
	//TODO: still no response by inteland for requested methode which is missing
	public List<Object> readAllAssociations()	//from current project
	{
		//all Associations, unabhängig welcher Tracker
		List<AssociationDto<?,?>> tempAllAssoc = AssociationManager.getInstance().findAll(user);
		List<Object> allAssoc = new ArrayList<Object>();
		
			//for (int s = 0; s< tempAllAssoc.length;s++) 
			//	for (int s = 0; s< 10;s++) 
		{
			//AssociationDto assoc =tempAllAssoc[s];				
			//if (assoc.getTo().getId() >1000)//because 1000 is not allowed as an Artifact-id nor trackerItem-id
			{
				
			//*************löschbar, nur für Testzwecke*************
				String serviceUrl = "http://localhost:8080/cb/remote-api";
				String login ="bond";
				String password = "007";	
			  RemoteApi api =null;
			try {
				api = RemoteApiFactory.getInstance().connect(serviceUrl);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			   String token = api.login(login, password);

				//*******************Dummy values********************** 
				Object[] tempAssoc = new Object [3]; //[0]=assoc-object [1]=trackerItem-object [2]=attachment-object)
				tempAssoc[0] = api.findAssociationById(token, 5);
				tempAssoc[1] = api.findTrackerItemById(token,1013 );  //trackeritem
				tempAssoc[2] = api.findArtifactById(token, 1028);	//artifact
				allAssoc.add(tempAssoc);
				
				 Object[] tempAssocA = new Object [3];
				 tempAssocA[0] =  api.findAssociationById(token, 4);
				 tempAssocA[1] = api.findTrackerItemById(token,1003 );  //trackeritem
				 tempAssocA[2] = api.findArtifactById(token, 1027);	//artifact
				allAssoc.add(tempAssocA);
				
				 Object[] tempAssocB = new Object [3];
				 tempAssocB[0] =  api.findAssociationById(token, 8);
				 tempAssocB[1] = api.findTrackerItemById(token,1002 );  //trackeritem
				 tempAssocB[2] = api.findArtifactById(token, 1021);	//artifact
				allAssoc.add(tempAssocB);
			
				 Object[] tempAssocC = new Object [3];
				 tempAssocC[0] =  api.findAssociationById(token, 3);
				 tempAssocC[1] = api.findTrackerItemById(token,1000 );  //trackeritem
				 tempAssocC[2] = api.findArtifactById(token, 1024);	//artifact
				allAssoc.add(tempAssocC);
				
				 Object[] tempAssocD = new Object [3];
				 tempAssocD[0] =  api.findAssociationById(token, 9);
				 tempAssocD[1] = api.findTrackerItemById(token,1016 );  //trackeritem
				 tempAssocD[2] = api.findArtifactById(token, 1036);	//artifact
				allAssoc.add(tempAssocD);
				
				 Object[] tempAssocE = new Object [3];
				 tempAssocE[0] =  api.findAssociationById(token, 10);
				 tempAssocE[1] = api.findTrackerItemById(token,1009 );  //trackeritem
				 tempAssocE[2] = api.findArtifactById(token, 1021);	//artifact
				allAssoc.add(tempAssocE);
				
				 Object[] tempAssocF = new Object [3];
				 tempAssocF[0] =  api.findAssociationById(token, 11);
				 tempAssocF[1] = api.findTrackerItemById(token,1009 );  //trackeritem
				 tempAssocF[2] = api.findArtifactById(token, 1025);	//artifact
				allAssoc.add(tempAssocF);
					//System.out.println (tempAssoc[0] +" "+ tempAssoc[1] + " " +tempAssoc[2]  );
					
					//***********löschen nur zum testen********
				}			
			}
		return allAssoc;
	} 

}
