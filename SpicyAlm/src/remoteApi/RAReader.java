package remoteApi;

import java.net.MalformedURLException;
import java.security.AccessControlException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;


import com.intland.codebeamer.remoting.RemoteApi;
import com.intland.codebeamer.remoting.RemoteApiFactory;
import com.intland.codebeamer.remoting.bean.ServerInfo;
import com.intland.codebeamer.wiki.plugins.support.GlobalVariable;

import com.intland.codebeamer.persistence.dto.ArtifactDto;
import com.intland.codebeamer.persistence.dto.AssociationDto;
import com.intland.codebeamer.persistence.dto.ProjectDto;
import com.intland.codebeamer.persistence.dto.TrackerDto;
import com.intland.codebeamer.persistence.dto.UserDto;

import com.intland.codebeamer.persistence.dto.TrackerItemDto;



/**RemoteApi Reader Class
 * Provides Remote Api Logic for Codebeamer Api 6.0.2
 * @author Alexander Börsch
 *
 */
public class RAReader {
	
	private static RemoteApi api;	//Object of current connection
	private static String token;	//contains login data
	
			/**Connects to codebeamer server
			 * @param serviceUrl String value of codebeamer server
			 * @param login User name
			 * @param password 
			 */
			public static void login (String serviceUrl, String login, String password)
			{

				System.out.println("Connecting to CodeBeamer web service at " + serviceUrl + "...");
				try {
					
						api =RemoteApiFactory.getInstance().connect(serviceUrl);	//instantiate Connection Object
						
						System.out.println("Signing in...");
						token = api.login(login, password);	//contains login data
					
				} catch (AccessControlException e)
				{
					e.printStackTrace();
				}
				 catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if(api == null) {
					System.err.println("Couldn't connect, is the service URL correct?");
					System.exit(-1);
				}
				
				ServerInfo serverInfo = api.getServerInfo();
				System.out.println("Signed in to CodeBeamer " + serverInfo.getMajorVersion() + serverInfo.getMinorVersion() + " (" + serverInfo.getBuildDate() + ") running on " + serverInfo.getOs() + "/Java " + serverInfo.getJavaVersion());
			}
			
			/**
			 * Logout from codebeamer
			 */
			public void logout ()
			{
				System.out.println("Signing out...");
				api.logout(token);

				System.out.println("Done");
			}
			
			/**Provides all TrackerItems
			 * @return List<TrackerItemDto> List of TrackerItemDto objects
			 */
			public List<TrackerItemDto> getAllTrackerItems()
			{
				List<TrackerItemDto> allTrackerItems = new ArrayList <TrackerItemDto>();
				List<TrackerDto>  allTrackers= readAllTrackers();
				
				Iterator<TrackerDto> itrallTrackers = allTrackers.iterator();
			   	while(itrallTrackers.hasNext()) {
			   		TrackerDto tempTracker = itrallTrackers.next();	
			   		List<TrackerItemDto> allItems = readAllTrackerItems(new int [tempTracker.getId()]);	
					
			   		Iterator<TrackerItemDto> itrallItems = allItems.iterator();
				   	while(itrallItems.hasNext()) {
				   		TrackerItemDto tempTrackerItem = itrallItems.next();	
				   		allTrackerItems.add(tempTrackerItem);
					    //System.out.println("Items information: " + item.getName() + " ID: " + item.getId() + " type: " + item.getTypeName());
					}
				}
				return allTrackerItems;
			}
	
	/**Provides all available projects for user
	 * @return int [] Array with projectIDs
	 */
	public int[] readAllProjects () 
	{
		ProjectDto projects[] = api.findAllProjects(token);
		int[] tempArray = new int[projects.length];
		for (int i=0; i <projects.length; i++)
		{
			tempArray[i]=projects[i].getId();
		}
		return tempArray;
	}

	
	/**Finds Project by ID
	 * @param projectId 
	 * @return ProjectDto
	 */
	public ProjectDto readProject(int projectId)
	{
		ProjectDto project = api.findProjectById(token, projectId);
		return project;
	}
	
		/**Provides all Attachments by Project, independently whether it is top or sub-artifact
		 * @param project
		 * @return List<ArtifactDto> List of Artifact objects
		 */
	    //TODO: in process runs through all projects, not project specific
		public List<ArtifactDto> readAllAttachmentsByProject (int[] projects){
			List<ArtifactDto> attachments = new ArrayList<ArtifactDto>();
			List<ArtifactDto> artifacts = new ArrayList <ArtifactDto>();
			
			if (projects != null)
			{
				for (int i=0; i<projects.length;i++)
				{
					ArtifactDto tempArtifacts[] =   api.findTopArtifactsByProject(token,  projects[i]);	//TODO: nullpointerexception abfangen
					for (int tempI=0; tempI <tempArtifacts.length; tempI++)	//convertion array[] to List, because many projects mit various artifacts can be searched
					{
						artifacts.add(tempArtifacts[tempI]);
					}
				}
				//artifacts pro Project auslesen, nur Dateien (SubArtifacts, weil in Ordner liegen)
				Iterator<ArtifactDto> itrArtifacts = artifacts.iterator();
			   	while(itrArtifacts.hasNext()) {
			   		ArtifactDto tempArtifact = itrArtifacts.next();	
					  if (tempArtifact.getTypeId()!= GlobalVariable.attachmentType_Folder)	//TypeID==2 is Folder
					  {
						  attachments.add(tempArtifact); 
						 	
					  }
					  ArtifactDto[] artifactsSub = api.findArtifactsByParentArtifact(token, tempArtifact.getId());
					  for (int p = 0; p< artifactsSub.length; p++) 
						{
						  attachments.add(artifactsSub[p]);
						  //System.out.println(artifactsSub[p].getId() +" Name: " + artifactsSub[p].getName() );//+ " " + artifactsSub[p].getDescription() +" Type-ID:"+ artifactsSub[p].getTypeId()); 
						}
					}
			  
			}
			return attachments; 
		}
	
		/**Provides various Trackers, each Tracker has null or unlimited TrackerItems
		 * @return List TrackerDto[] List of TrackerDto Array 
		 */
		public List<TrackerDto> readAllTrackers()
		{
			//Tracker auslesen
			TrackerDto trackers[] = api.findAllTrackers(token);	
			List<TrackerDto> list = Arrays.asList(trackers);
			return list;
		}
		
		/**Provides search for TrackerItem by OBject
		 * @param trackerItemID int value
		 * @return TrackerItemDto TrackerItemDto Object
		 */
		private static TrackerItemDto readTrackerItemByID (int trackerItemID)
		{
			TrackerItemDto tempItem = api.findTrackerItemById(token, trackerItemID);
			return tempItem;
		}
		
		/**Reads TrackerItem objects by a particular amount of TrackerIDs
		 * @param trackerItemIDs Integer Array of minimum one Tracker id
		 * @return List<TrackerItemDto> List of TrackerItemDto objects
		 */
		private static List<TrackerItemDto> readTrackerItemByID (int[] trackerIDs)
		{
			List <TrackerItemDto> list = new ArrayList ();
			for (int i = 0; i < trackerIDs.length; i++)
			{
				 TrackerItemDto[] tempItems = api.findTrackerItemsByTrackerId(token, trackerIDs[i]);
				 for (int j = 0; j < tempItems.length; j++ )
				 {
					 list.add(tempItems[j]);
				 }
			}
			return list;
		
		}
		
		
	/**Provides TrackerItems searched by one or multiple TrackerIds
	 * @param integer Integer Array of minimum one Tracker id
	 * @return List<TrackerItemDto> List of TrackerItemDto objects
	 */
	public List<TrackerItemDto> readAllTrackerItems (int[] trackerID)
	{
		//if TrackerID <0 = than find allTrackerItems, instead of one specifc type
		List<TrackerItemDto> list = null;
		if (trackerID.length == 0)
		{
			return readAllTrackerItems();
		}
		if (trackerID.length >1)
		{
			list = readTrackerItemByID(trackerID);
		}
		else
		{
		      //reads Items 
			  TrackerItemDto items[] = api.findTrackerItemsByTrackerId(token, trackerID[0]);
			  list = Arrays.asList(items);
		}
		return list;
	}
	
	/**Find all TrackerItems but TrackerType-independent 
	 * @return List<TrackerItemDto> List of TrackerItemDto objects
	 * TODO: change number "10" if you have more TrackerTypes
	 */
	public List<TrackerItemDto> readAllTrackerItems()
	{
		List<TrackerItemDto> items = new ArrayList<TrackerItemDto> ();
		TrackerItemDto temp[];
		for (int i=1; i<10; i++)	//TODO: change number "10" if you have more TrackerTypes
		{
				 temp =api.findTrackerItemsByTrackerId(token, i);
		         // Returns the tracker items in the trackers with the given IDs.
					 for (int j = 0; j< temp.length;j++) 
						{
							TrackerItemDto item = temp[j];
							//System.out.println("Items information: " + item.getName() + " ID: " + item.getId() + " type: " + item.getTypeName());
							items.add(item);
						}
		}
		return items;
	}
	
	/**Provides all available user on codebeamer
	 * @return UserDto[] Array of UserDto Objects
	 */
	private static UserDto[] readAllUsers()
	{
				UserDto users[] = api.findAllUsers(token);
			return users;
	}
	
		/**Provides all available associations (with linked TrackerItem and Artifact)  for current user 
		 * @return List<Object> List includes Arrays of Objects, Array positions [0]=AssociationDto Object [1]=TrackerItemDto Object [2]=ArtifactDto Object
		 */
		public List<Object> readAllAssociations()	
		{
			//all Associations
			AssociationDto[] tempAllAssoc = api.findAllAssociations(token);
			List <Object> allAssoc = new ArrayList<Object>();
			String test = "";
			for (int s = 0; s< tempAllAssoc.length;s++) 
			{
				AssociationDto assoc =tempAllAssoc[s];	
				test = test + " "+ Integer.toString(assoc.getId());
				
				
				if (assoc.getTo().getId() >1000)//because 1000 is not allowed as an Artifact-id nor trackerItem-id
				{
					Object[] tempAssoc = new Object [3]; //[0]=AssociationDto object [1]=trackerItem object [2]=attachment object)
					tempAssoc[0] = assoc;
					tempAssoc[1] = api.findTrackerItemById(token, assoc.getFrom().getId());  //trackeritem
					tempAssoc[2] = api.findArtifactById(token, assoc.getTo().getId());	//artifact
					allAssoc.add(tempAssoc);
					System.out.println(assoc.getId() +" " +assoc.getFrom().getId() + " " + assoc.getTo().getId());
				}
			}
			System.out.println(test);	//print out all available association ids
			return allAssoc;
		}
		
		/**Finds specific CB-Project by Name
		 * @param projects
		 * @param projectName
		 * @return int[] Integer Array with projectIDs
		 */
		public int[] findProject (int[] projects, String projectName)
		{
			ProjectDto tempProject = api.findProjectByName(token, projectName);
			int[] tempArray = {tempProject.getId()};
			return tempArray;
		}



}
