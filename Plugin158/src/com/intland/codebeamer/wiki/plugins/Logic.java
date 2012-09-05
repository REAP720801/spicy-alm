package com.intland.codebeamer.wiki.plugins;

import java.net.MalformedURLException;
import java.security.AccessControlException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.ecyrd.jspwiki.WikiContext;
import com.intland.codebeamer.persistence.dto.ArtifactDto;
import com.intland.codebeamer.persistence.dto.AssociationDto;
import com.intland.codebeamer.persistence.dto.ProjectDto;
import com.intland.codebeamer.persistence.dto.TrackerDto;
import com.intland.codebeamer.persistence.dto.TrackerItemDto;
import com.intland.codebeamer.persistence.dto.UserDto;
import com.intland.codebeamer.remoting.RemoteApi;
import com.intland.codebeamer.remoting.RemoteApiFactory;
import com.intland.codebeamer.remoting.bean.ServerInfo;



public class Logic {
	
	private static RemoteApi api;
	private static String token;

	//TODO: Fehlerabfangen
	//TODO: fehler bei connecten: connection refused: connect abfangen
	public static void main(String[] args) throws Exception {
	}
	
	public void login (String serviceUrl, UserDto user)
	{
		login(serviceUrl, user.getName(), user.getPassword());
	}
	
	
	/**Check if every Attachment has minimum one association, 
	 * positiveArtifactList -->[0]=assoc, [1]=item, [2]=artifact, 
	 * if yes, add to List positiveAttachements
	 * if no, add to List negativeAttachments
	 * return List where both Lists are added [positiveAttachments],[negativeAttachments],[associations]
	 * @param List<int[]> positiveArtifactList
	 * @param ArtifactDto[] projectAllAttachment
	 * @return List<List<ArtifactDto>>
	 */
	public List<List<Object>> checkMissingAttachment(List<int[]> positiveArtifactList, List<ArtifactDto>  projectAllAttachment)
	{			
		List<Object> positiveAttachments = new ArrayList<Object>();
		List<Object> negativeAttachments = new ArrayList<Object>();
		List<Object> trackerItems = new ArrayList<Object>();
		List<Object> associations = new ArrayList<Object>();	//same order as positiveAttachments, based on this a logical theoretical link is generated
		List<List<Object>> resultsCheckedAtt = new ArrayList<List<Object>>();
		 
		Iterator<ArtifactDto> itr = projectAllAttachment.iterator();
	   	while(itr.hasNext()) {
	   		ArtifactDto artifact = itr.next();
	    	boolean tempMarker=false;
	    	Iterator<int[]> litr = positiveArtifactList.iterator();
			while(litr.hasNext()) {
				int[] array = litr.next();
				ArtifactDto tempArtifact = readArtifactByID (array[2]);
			    
			    if (artifact.compareTo(tempArtifact)==0)	//yes, if attachement == tempArtifact
				{
			    	if (!positiveAttachments.contains(tempArtifact))	//yes, if  tempArtifact does not exist in List already
			    	{
			    		//System.out.println ("positive Findings for " + artifact.getName());
			    		positiveAttachments.add(artifact);
			    		TrackerItemDto trackerItem = readTrackerItemByID (array[1]);
			    		trackerItems.add(trackerItem);
			    		AssociationDto tempAssoc = readAssocationByID(array[0]);
			    		associations.add(tempAssoc);
			    		tempMarker =true;
			    	}
			    	else
			    	{
			    		tempMarker =true;
			    	}
				}  
			}
			if (tempMarker==false )
			{
				if (!negativeAttachments.contains(artifact.getName()))	//yes, if ProjectAllAttachment does not already exist in List 
		    	{
				 //System.out.println ("negative Findings for " + artifact.getName());
				 negativeAttachments.add(artifact);
		    	}
			}
		}
	    resultsCheckedAtt.add(positiveAttachments);
	    resultsCheckedAtt.add(negativeAttachments);
	    resultsCheckedAtt.add(trackerItems);
	    resultsCheckedAtt.add(associations);
		return resultsCheckedAtt;
	}
	
	
	
	

	public List<List<Object>> checkMissingTrackerItem(List<int[]> positiveArtifactList, TrackerItemDto[] projectAllTrackerItems)
	{			
		List<Object> positiveTrackers = new ArrayList<Object>();
		List<Object> negativeTrackers = new ArrayList<Object>();
		List<Object> associations = new ArrayList<Object>();	//same order as positiveAttachments, based on this a logical theoretical link is generated
		List<List<Object>> resultsCheckedItem = new ArrayList<List<Object>>();
		 
		for (int i = 0; i< projectAllTrackerItems.length;i++) 
		{
	   		TrackerItemDto item = projectAllTrackerItems[i];
	    	boolean tempMarker=false;
	    	Iterator<int[]> litr = positiveArtifactList.iterator();
			while(litr.hasNext()) {
				int[] array = litr.next();
				TrackerItemDto tempItem = readTrackerItemByID (array[1]);
			    
			    if (item.compareTo(tempItem)==0)	//yes, if item == tempItem (from Association)
				{
			    	if (!positiveTrackers.contains(tempItem))	//yes, if  tempArtifact does not exist in List already
			    	{
			    		//System.out.println ("positive Findings for " + item.getName());
			    		positiveTrackers.add(item);
			    		AssociationDto tempAssoc = readAssocationByID(array[0]);
			    		associations.add(tempAssoc);
			    		tempMarker =true;
			    	}
			    	else
			    	{
			    		tempMarker =true;
			    	}
				}  
			}
			if (tempMarker==false )
			{
				if (!negativeTrackers.contains(item.getName()))	//yes, if ProjectAllAttachment does not already exist in List 
		    	{
				 //System.out.println ("negative Findings for " + item.getName());
				 negativeTrackers.add(item);
		    	}
			}
		}
	   	resultsCheckedItem.add(positiveTrackers);
	   	resultsCheckedItem.add(negativeTrackers);
	   	resultsCheckedItem.add(associations);
		return resultsCheckedItem;
	}
	
	
	
	public List<List<Object>> checkMissingTrackerItem(List<int[]> positiveArtifactList, List<TrackerItemDto> allTrackerItems)
	{			
		List<Object> positiveTrackers = new ArrayList<Object>();
		List<Object> negativeTrackers = new ArrayList<Object>();
		List<Object> associations = new ArrayList<Object>();	//same order as positiveAttachments, based on this a logical theoretical link is generated
		List<List<Object>> resultsCheckedItem = new ArrayList<List<Object>>();
		 
		Iterator<TrackerItemDto> itr = allTrackerItems.iterator();
	   	while(itr.hasNext())
		{
	   		TrackerItemDto item = itr.next();
	    	boolean tempMarker=false;
	    	Iterator<int[]> litr = positiveArtifactList.iterator();
			while(litr.hasNext()) {
				int[] array = litr.next();
				TrackerItemDto tempItem = readTrackerItemByID (array[1]);
			    
			    if (item.compareTo(tempItem)==0)	//yes, if item == tempItem (from Association)
				{
			    	if (!positiveTrackers.contains(tempItem))	//yes, if  tempArtifact does not exist in List already
			    	{
			    		//System.out.println ("positive Findings for " + item.getName());
			    		positiveTrackers.add(item);
			    		AssociationDto tempAssoc = readAssocationByID(array[0]);
			    		associations.add(tempAssoc);
			    		tempMarker =true;
			    	}
			    	else
			    	{
			    		tempMarker =true;
			    	}
				}  
			}
			if (tempMarker==false )
			{
				if (!negativeTrackers.contains(item.getName()))	//yes, if ProjectAllAttachment does not already exist in List 
		    	{
				 //System.out.println ("negative Findings for " + item.getName());
				 negativeTrackers.add(item);
		    	}
			}
		}
	   	resultsCheckedItem.add(positiveTrackers);
	   	resultsCheckedItem.add(negativeTrackers);
	   	resultsCheckedItem.add(associations);
		return resultsCheckedItem;
	}
	
	
	public ProjectDto[] readAllProjects () 
	{
		ProjectDto projects[] = api.findAllProjects(token);
		return projects;
	}
	

	
	public ProjectDto findProject (ProjectDto[] projects, String projectName)
	{
		for(int i = 0; i < projects.length; i++) {
		  ProjectDto project = projects[i];
		  if (project.getName().matches(projectName))
		  {
			  //System.out.println("Project information: \n-Name: " +project.getName() + "\n-ID: " + project.getId());
			  return project;
		  }
		}
		return null;
	}
	

	
	/**
	 * @param allAssoc
	 * @param attachments
	 * @param trackerItemType
	 * @param artifactType
	 * @return
	 * if trackerItemType ==null && artifactType ==-1 than general path without any implicit typing
	 */
	//TODO: fehler-behandlung ist nicht generisch!! erste association ist es immer??
	//TODO: typisierung ausgeklammert, 
	//TODO: nullpointer-exception abfangen wenn typisierung nicht funktioniert
	public List<int[]> checkAssociationToAttachments (AssociationDto[] allAssoc, List<ArtifactDto> attachments, String trackerItemType, int artifactType) 
	{	
		List<int[]> positiveArtifactList = new ArrayList<int[]>();
		
		for (int k = 0; k < allAssoc.length; k++) {
			TrackerItemDto trackerItem = readTrackerItemByID (allAssoc[k].getFrom().getId());
			//if (allAssoc[k].getTo().getId() != 1002 )//&& trackerItem !=null && attachments.length !=0)	//weil sonst Fehler, bei TrackerItem Artifact-Erzeugung durch Object-ID: 1002, und Nullpointer-Exception
			if(allAssoc[k].getId() !=1)
			{
				ArtifactDto assocArtifact = readArtifactByID (allAssoc[k].getTo().getId());
				//System.out.println("aaaaaaaaaaa----------"+trackerItem.getTypeName());	//nullpointerexception
				//System.out.println (trackerItem.getId());									//nullpointerexception
				//System.out.println("aaaaaaaaaaa----------"+trackerItem.getName());		//nullpointerexception
				//System.out.println("........---" + assocArtifact.getTypeId());
				Iterator<ArtifactDto> itr = attachments.iterator();
				if (trackerItemType ==null && artifactType ==-1) {	
					//System.out.println(attachments.length);
				   	while(itr.hasNext()) {
				   		ArtifactDto artifact = itr.next(); 
						if (artifact.compareTo(assocArtifact)==0)	
						{
							int [] tempArray = new int [3];	//[0]=assoc, [1]=item, [2]=artifact
							tempArray[0]=allAssoc[k].getId();
							tempArray[1]=allAssoc[k].getFrom().getId();
							tempArray[2]=assocArtifact.getId();	 
							positiveArtifactList.add(tempArray);
							//System.out.println ("aaa" + trackerItem.getTypeName()+ " " +assocArtifact.getTypeId());
						}
					} 
				}			//TODO: notwendig?
				else if (trackerItem.getTypeName().contains(trackerItemType)&& assocArtifact.getTypeId()==artifactType) {	//check if TrackerITemType && artifactType correct
					//System.out.println(attachments.length);
					while(itr.hasNext()) {
				   		ArtifactDto artifact = itr.next(); 
						if (artifact.compareTo(assocArtifact)==0)	
						{
							int [] tempArray = new int [3];	//[0]=assoc, [1]=item, [2]=artifact
							tempArray[0]=allAssoc[k].getId();
							tempArray[1]=allAssoc[k].getFrom().getId();
							tempArray[2]=assocArtifact.getId();	 
							positiveArtifactList.add(tempArray);
							//System.out.println (trackerItem.getTypeName()+ " " +assocArtifact.getTypeId());
						}
					} 
				}
				//Generale Matching Methode without implicit Item- nor Artifact-Typing 	
				else 
				{
					System.out.println ("ouuuut: " + trackerItem.getTypeName()+ " " +assocArtifact.getTypeId());
				}
			}
			else
			{
			//System.out.println ("Fehler: TrackerItem bzw Artifact ist fehlerhaft.");
			}
		}
		
		//System.out.println(positiveArtifactList.size());
		return positiveArtifactList;
	}
	
		//alle artifakte, unabhängig ob ober oder unter-artifakt werden aneinander in list gefüllt
	//TODO:NullPointerException
		public List<ArtifactDto> readAllAttachmentsByProject (ProjectDto project){
			List<ArtifactDto> attachments = new ArrayList<ArtifactDto>();
			ArtifactDto artifacts[] =   api.findTopArtifactsByProject(token,  project.getId());	//TODO: nullpointerexception abfangen
			
			  //artifacts pro Project auslesen, nur Dateien (SubArtifacts, weil in Ordner liegen)
			 // System.out.println("----------Artifact Finding-------------");
			  if (artifacts.length ==0)
				  System.out.println("No Artifacts existing");
			  else {
				  for (int s = 0; s< artifacts.length;s++) 
					{  
					  if (artifacts[s].getTypeId()!= GlobalVariable.attachmentType_Folder)	//TypeID==2 is Folder
					  {
						  attachments.add(artifacts[s]);
					  }
					  //System.out.println("Artifact: " + artifacts[s].getName() + artifacts[s].getDescription() + artifacts[s].getTypeId()); 
					  ArtifactDto[] artifactsSub = api.findArtifactsByParentArtifact(token, artifacts[s].getId());
					  for (int p = 0; p< artifactsSub.length; p++) 
						{
						  attachments.add(artifactsSub[p]);
						 // System.out.println(artifactsSub[p].getId() +" Name: " + artifactsSub[p].getName() );//+ " " + artifactsSub[p].getDescription() +" Type-ID:"+ artifactsSub[p].getTypeId()); 
						}
					}
			  }
			  //System.out.println("----------End of Artifact Finding-------------");
			return attachments; 
		}
	

		//TODO: ConnectException
		//java.net.MalformedURLException
		public static void login (String serviceUrl, String login, String password)
		{

			System.out.println("Connecting to CodeBeamer web service at " + serviceUrl + "...");
			try {
				api =RemoteApiFactory.getInstance().connect(serviceUrl);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (AccessControlException e)
			{
				e.printStackTrace();
			}
			if(api == null) {
				System.err.println("Couldn't connect, is the service URL correct?");
				System.exit(-1);
			}

			System.out.println("Signing in...");
			token = api.login(login, password);
			ServerInfo serverInfo = api.getServerInfo();
			System.out.println("Signed in to CodeBeamer " + serverInfo.getMajorVersion() + serverInfo.getMinorVersion() + " (" + serverInfo.getBuildDate() + ") running on " + serverInfo.getOs() + "/Java " + serverInfo.getJavaVersion());

		}
	public void logout ()
	{
		System.out.println("Signing out...");
		api.logout(token);

		System.out.println("Done");
	}
	private static UserDto[] readAllUsers()
	{
		//User abfragen
				UserDto users[] = api.findAllUsers(token);
				for (int i = 0; i< users.length; i++)
				{
					UserDto user = users[i];
					//System.out.println("user information: "+ user.getName() + " ...");
				}
			return users;
	}
	
	/**various Trackers, each Tracker has null or unlimited TrackerItems
	 * @return TrackerDto[]
	 */
	public TrackerDto[] readAllTrackers()
	{
		//Tracker auslesen
		TrackerDto trackers[] = api.findAllTrackers(token);
		for (int i = 0; i< trackers.length;i++) 
		{
			TrackerDto tracker = trackers[i];
	System.out.println("Tracker information: "  + tracker.getName()+ " Type: " + tracker.getType() + " ID: "+tracker.getId());
			//readTrackerItemsByTracker (trackers[i].getId());	//return-Value verwenden
			
		}
		return trackers;
	}
	
	public List<TrackerItemDto> getAllTrackerItems()
	{
		List<TrackerItemDto> allTrackerItems = new ArrayList <TrackerItemDto>();
		TrackerDto[] allTrackers= readAllTrackers();
		for (int i = 0; i< allTrackers.length;i++) 
		{	
			TrackerItemDto[] AllItems = readTrackerItemsByTracker(allTrackers[i].getId());
			for (int j = 0; j< AllItems.length;j++) 
			{
			allTrackerItems.add(AllItems[j]);
			//System.out.println("Items information: " + item.getName() + " ID: " + item.getId() + " type: " + item.getTypeName());
			}
		}
		return allTrackerItems;
	}
	
	private static TrackerItemDto readTrackerItemByID (int trackerItemID)
	{
		TrackerItemDto tempItem = api.findTrackerItemById(token, trackerItemID);
		//if (tempItem !=null)
		//System.out.println("item-id: " + tempItem.getId() + " item-name: " + tempItem.getName() );
		return tempItem;
	}
	
	/**Find all TrackerItems TrackerTypes-independent 
	 * @return TrackerItemDto []
	 */
	public TrackerItemDto[] readAllTrackerItems()
	{
		ArrayList<TrackerItemDto> items = new ArrayList ();
		TrackerItemDto temp[];
		for (int i=1; i<10; i++)	//TODO: change number "10" if you have more TrackerTypes
		{
			 
				 temp =api.findTrackerItemsByTrackerId(token, i);		 
					 for (int j = 0; j< temp.length;j++) 
						{
							TrackerItemDto item = temp[j];
							//System.out.println("Items information: " + item.getName() + " ID: " + item.getId() + " type: " + item.getTypeName());
							items.add(item);
						}
				 
		}
		temp = new TrackerItemDto[items.size()];
		items.toArray(temp);	
		return temp;
	}
	
	
	/**
	 * if TrackerID =-1 than find allTrackerItems, instead of one specifc type
	 * @param trackerID
	 * @return
	 */
	public TrackerItemDto[] readTrackerItemsByTracker (int trackerID)
	{
		if (trackerID == -1)
		{
			return readAllTrackerItems();
		}
		
		//Items auslesen
		TrackerItemDto items[] = api.findTrackerItemsByTrackerId(token, trackerID);
		for (int j = 0; j< items.length;j++) 
		{
			TrackerItemDto item = items[j];
			//System.out.println("Items information: " + item.getName() + " ID: " + item.getId() + " type: " + item.getTypeName());
		}
		return items;
	}

	//TODO: wenn nullPointerException abfangen
	public AssociationDto[] readAllAssociations()	//from current project
	{
		//all Associations, unabhängig welcher Tracker
		AssociationDto[] allAssoc = api.findAllAssociations(token);
		
		for (int s = 0; s< allAssoc.length;s++) 
		{
			AssociationDto assoc =allAssoc[s];
			//System.out.println(assoc.getId() +" " + assoc.getTypeId() );
		}
		
		return allAssoc;
	}
	
	private static AssociationDto readAssocationByID(int assocID)
	{
		return api.findAssociationById(token, assocID);
	}
	
	private static ArtifactDto readArtifactByID (int artifactID)
	{
		ArtifactDto artifact = api.findArtifactById(token, artifactID);
		//System.out.println (artifact.getId() + " " + artifact.getTypeId() + " " +artifact.getName() + " " +artifact.getFileSize());
		return artifact;
	}
	
	
	
//print-Ausgabe, mit generischem Objekt erstellen
	
	



}
