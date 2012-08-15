package com.intland.codebeamer.wiki.plugins;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import com.intland.codebeamer.persistence.dto.ArtifactDto;
import com.intland.codebeamer.persistence.dto.AssociationDto;
import com.intland.codebeamer.persistence.dto.ProjectDto;
import com.intland.codebeamer.persistence.dto.TrackerItemDto;

public class Printer {
	
	//[positiveItems],[negativeItems],[associations]
	public List printPositiveTrackerItem (List<List<Object>> resultsCheckedItem )
	{
		List trackerItemsList= new ArrayList(); 
		try {
		Iterator<List<Object>> litr = resultsCheckedItem.iterator();
		List<Object> trackerItems= litr.next();	//[ positiveResults]
			Iterator<Object> positiveItr = trackerItems.iterator();
			while(positiveItr.hasNext()) {
				TrackerItemDto tempItem = (TrackerItemDto) positiveItr.next();
				//System.out.println(tempItem.getName());
				trackerItemsList.add(tempItem);
				}
		}
		catch (NullPointerException e)
		{	System.out.println ("printPositiveTrackerItem-Error: " + e.toString() );
		}	//TODO: generic PrintErrorMessage
		
		catch (NoSuchElementException e)
		{	System.out.println ("printPositiveTrackerItem-Error: " + e.toString());
		}
			return trackerItemsList;
	}
	
	public List printNegativeTrackerItem (List<List<Object>> resultsCheckedItem )
	{
		List trackerItemsList= new ArrayList(); 
		try {
		Iterator<List<Object>> litr = resultsCheckedItem.iterator();
		litr.next();	//skip positiveResults
		List<Object> trackerItems= litr.next();	//negativeResults
			Iterator<Object> positiveItr = trackerItems.iterator();
			while(positiveItr.hasNext()) {
				TrackerItemDto tempItem = (TrackerItemDto) positiveItr.next();
				//System.out.println(tempItem.getName());
				trackerItemsList.add(tempItem);
				}
		}
		catch (NullPointerException e)
		{	System.out.println ("printPositiveTrackerItem-Error: " + e.toString() );
		}	//TODO: generic PrintErrorMessage
		
		catch (NoSuchElementException e)
		{	System.out.println ("printPositiveTrackerItem-Error: " + e.toString());
		}
			return trackerItemsList;
	}
	
	
	public List printPositiveMatchingResults (List<List<Object>> resultsCheckedAtt )
	{	
		List positveResults = new ArrayList(); 
		try 
		{
		Iterator<List<Object>> litr = resultsCheckedAtt.iterator();
		List<Object> positiveResultAttachments= litr.next();
			Iterator<Object> positiveItr = positiveResultAttachments.iterator();
			while(positiveItr.hasNext()) {
				ArtifactDto tempArtifact = (ArtifactDto) positiveItr.next();
				System.out.println(tempArtifact.getName());
				positveResults.add(tempArtifact);
				}
		}
		catch (NullPointerException e)
		{	System.out.println ("printPositiveMatchingResults-Error: " + e.toString() );
		}	//TODO: generic PrintErrorMessage
		
		catch (NoSuchElementException e)
		{	System.out.println ("printPositiveMatchingResults-Error: " + e.toString());
		}
		
			return positveResults;
	}
	
	public List printPositiveAssoc (List<List<Object>> resultsCheckedAtt )
	{
		List assoc= new ArrayList(); 
		try {
		Iterator<List<Object>> litr = resultsCheckedAtt.iterator();
		litr.next();	//skip positiveResults
		litr.next();	//skip negativeResults
		litr.next();	//skip trackerItems
		List<Object> positiveResultAttachments= litr.next();	//Associations which are "linked" to positiveResults
			Iterator<Object> positiveItr = positiveResultAttachments.iterator();
			while(positiveItr.hasNext()) {
				AssociationDto tempAssoc = (AssociationDto) positiveItr.next();
				//System.out.println(tempAssoc.getName());
				assoc.add(tempAssoc);
				}
		}
		catch (NullPointerException e)
		{	System.out.println ("positiveResultAttachments-Error: " + e.toString() );
		}	//TODO: generic PrintErrorMessage
		
		catch (NoSuchElementException e)
		{	System.out.println ("positiveResultAttachments-Error: " + e.toString());
		}
			return assoc;
	}

	//TODO: .next verbessern, unsauber gelöst
		public List printNegativeMatchingResults (List<List<Object>> resultsCheckedAtt )
		{
			List negativeResults = new ArrayList(); 
			try {
			Iterator<List<Object>> litr = resultsCheckedAtt.iterator();
			List<Object> positiveResultAttachments= litr.next();
			List<Object> negativeResultAttachments= litr.next();
				Iterator<Object> negativeItr = negativeResultAttachments.iterator();
				while(negativeItr.hasNext()) {
					ArtifactDto tempArtifact = (ArtifactDto) negativeItr.next();
					//System.out.println(tempArtifact.getName());
					negativeResults.add(tempArtifact);
					}
			}
			catch (NullPointerException e)
			{	System.out.println ("negativeResultAttachments-Error: " + e.toString() );
			}	//TODO: generic PrintErrorMessage
			
			catch (NoSuchElementException e)
			{	System.out.println ("negativeResultAttachments-Error: " + e.toString());
			}
				return negativeResults;
		}
		
		public List printAllAssociations (AssociationDto[] allAssoc)
		{
			List assoc = new ArrayList();
			try {
				for (int s = 0; s< allAssoc.length;s++) 
				{
					assoc.add(allAssoc[s]);
					System.out.println(allAssoc[s].getId() +" " + allAssoc[s].getTypeId() );
				}			
			}
			catch (NullPointerException e)
			{	System.out.println (e.toString() );
			}	//TODO: generic PrintErrorMessage
			
			return assoc;
		}

		
		/**
		 * @param artifacts
		 * @return List<ArtifactDto>  artifacts
		 * Additionally, commandoline prints for user and debugging
		 */
		public List printAllAttachmentsByProject (List<ArtifactDto>  artifacts){
			System.out.println("----------Artifact Finding-------------");
			try 
			{
			if (artifacts.size() ==0)
				 System.out.println("No Artifacts existing");
			  else {
				   	Iterator<ArtifactDto> itr = artifacts.iterator();
				   	while(itr.hasNext()) {
				   		ArtifactDto artifact = itr.next(); 
					    System.out.println("Artifact: " + artifact.getId() + " " +artifact.getName() + artifact.getDescription() +" " + artifact.getTypeId()); 

				   		}
					}
			}
			catch (NullPointerException e)
			{
				System.out.println (e.toString());
			}
			System.out.println("----------End of Artifact Finding-------------");
			return artifacts; 
		}

		public List printAllProjects (ProjectDto projects[]) 
		{
			//ProjectDto projects[] = api.findAllProjects(token);	//TODO: löschen
			List projectsList = new ArrayList(); 
			try {
				for(int i = 0; i < projects.length; i++) {
					  ProjectDto project = projects[i];			  
					  projectsList.add(project);
					  System.out.println("Project information: \n-Name: " +project.getName() + "\n-ID: " + project.getId());
				}
			}
			catch (NullPointerException e)
			{
				System.out.println(e.toString());
			}
			return projectsList;
		}
		
		//Translation of Type to readable String for User
		private  String matchingArtifactType(int artifactType)
			{
				if (artifactType == GlobalVariable.attachmentType_externalFile)
					return "external File";
				if (artifactType == GlobalVariable.attachmentType_WikiPage)
					return "WikiPage";
				else
					return "<<no value matching: " + String.valueOf(artifactType) + ">>";
					//return String.valueOf(artifactType);
			}
		
		//Translation of Type to readable String for User
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
				//return String.valueOf(associationType);
		}
}
