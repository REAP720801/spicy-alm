package com.intland.codebeamer.wiki.plugins;



import java.util.List;
import java.util.Map;

import com.ecyrd.jspwiki.WikiContext;

import com.intland.codebeamer.persistence.dto.ArtifactDto;
import com.intland.codebeamer.persistence.dto.AssociationDto;
import com.intland.codebeamer.persistence.dto.ProjectDto;
import com.intland.codebeamer.persistence.dto.TrackerItemDto;
import com.intland.codebeamer.wiki.plugins.base.AbstractCodeBeamerWikiPlugin;



public class read2 extends AbstractCodeBeamerWikiPlugin {
	public String execute(WikiContext context, Map params) {
		//TODO: wenn nicht alle parameter ausgefüllt sind = NullpointerException. wieso? 	
		
		//[{HelloWorldDemoPlugin name='Joe'}]
				String format = (String)params.get("format");	
				String url = (String)params.get("url");	//per hand, oder automatisch auslesen, oder wenn leer
				
				String projectName = (String)params.get("projectName");	//number
				String trackerId = (String)params.get("trackerId");	//String
				String loginName = (String)params.get("user");
				String loginPw = (String)params.get("password");
				
				String attachment = (String)params.get("attachment");	//true or false
				String tracker = (String)params.get("tracker");	//true or false
				
				String positive = (String)params.get("positive");	//true or false
				String negative = (String)params.get("negative");	//true or false
				String all = (String)params.get("all");	//true or false
				
				String output ="TestPlugin made by Alexander";
							
				Logic logicObject =new Logic();
				Reader readerObject = new Reader();
				Printer printer = new Printer();
				
				readerObject.setAttachment(attachment);
				readerObject.setTracker(tracker);
				
				readerObject.setPositve(positive);
				readerObject.setNegative(negative);
				readerObject.setAll(all);
				
				readerObject.setTrackerID(trackerId);
				readerObject.setProjectName(projectName);
				readerObject.setFormat(format);


		
			//Dummy-Werte zum arbeiten, TODO: noch nicht scharf ob übergabe von Plugin
			//String serviceUrl = "http://localhost:8080/cb/remote-api";
			//String login ="bond";
			//String password = "007";	
			
			readerObject.setUrl(url);
			readerObject.setUser(loginName);
			readerObject.setPassword(loginPw);
			
		logicObject.login(readerObject.getUrl(), readerObject.getUser(), readerObject.getPassword());
		
		ProjectDto projects[] = logicObject.readAllProjects();		
		
		//find a specific project in codebeamer
		//String selectProject = "TestProject1";
		ProjectDto project = logicObject.findProject(projects, readerObject.getProjectName());
		
		//find and print all Attachments from specific project
		List<int[]> positiveArtifactList=null;
		List<ArtifactDto>  projectAllAttachments = logicObject.readAllAttachmentsByProject(project);
		
		//find all Associations in this specific project
		AssociationDto[] allAssoc = logicObject.readAllAssociations();	
		if (allAssoc!= null && projectAllAttachments != null)
		{
					//match Associations to Attachments
					positiveArtifactList = logicObject.checkAssociationToAttachments (allAssoc, projectAllAttachments, null, -1);
					
					//check if every Attachment has minimum one association
					List<List<Object>>  resultsCheckedAtt = logicObject.checkMissingAttachment(positiveArtifactList,projectAllAttachments);
				   
					TrackerItemDto[] trackerAllItems = logicObject.readTrackerItemsByTracker(readerObject.getTrackerID());	//spezifischer Tracker

					List<List<Object>>  resultsCheckedItem = logicObject.checkMissingTrackerItem(positiveArtifactList, trackerAllItems);
					
					if (readerObject.getAttachment())
					{	
						 if ((readerObject.getNumber() && readerObject.getAll()==false) && (readerObject.getPositve() || readerObject.getNegative()) )
						{
							output = printer.printAttachmentNumber(resultsCheckedAtt, readerObject.getPositve());
						}
						else if (readerObject.getNumber() && readerObject.getAll())
						{
							output = printer.printAllAttachmentsByProjectNumber(projectAllAttachments);
						}
						else if (readerObject.getName() && readerObject.getAll())
						{
							//List-Ausgabe
							//output = printer.printAllAttachmentsByProject(projectAllAttachments);	
							output = "This feature does not work: list output";
						}
						else if (readerObject.getName() && (readerObject.getPositve() || readerObject.getNegative()))
						{
							//List-Ausgabe
							//output = printer.printAttachmentName(resultsCheckedAtt, readerObject.getPositve());
							output = "This feature does not work: list output";
						}
					}
					else if (readerObject.getTracker())
					{
						if ((readerObject.getNumber() && readerObject.getAll()==false) && (readerObject.getPositve() || readerObject.getNegative()) )
						{
							output = printer.printTrackerNumber(resultsCheckedItem, readerObject.getPositve());
						}
						else if (readerObject.getName() && readerObject.getAll())
						{
							//List-Ausgabe
							//output = printer.printAllTrackerByProject(trackerAllItems);
							output = "This feature does not work: list output";
						}
						else if (readerObject.getNumber() && readerObject.getAll())
						{
							output = printer.printAllTrackerByProjectNumber(trackerAllItems);
							output = "asdfasdf";
						}
						else if (readerObject.getName() && (readerObject.getPositve() || readerObject.getNegative()))
						{
							//List-Ausgabe
							//output = printer.printTrackerName(resultsCheckedItem, readerObject.getPositve());
							output = "This feature does not work: list output";
						}
						else
						output = " project:" + readerObject.getProjectName()+" attachment:" +readerObject.getAttachment() + " tracker:" +readerObject.getTracker() + " id:" + readerObject.getTrackerID() + " name:" + readerObject.getName() + " number:" + readerObject.getNumber() +" all:" + readerObject.getAll() + " positive:" + readerObject.getPositve() + " negative:" + readerObject.getNegative();
					}
		}
		logicObject.logout ();
		
		return output;
		

		//possitive Ausgabe der funde, detailierte Ausgabe
			// printer.printPositiveAssoc(resultsCheckedAtt);
		
					
					 //TODO:Tracker-ID nutzen
					//List<TrackerItemDto> allTrackerItems= logicObject.getAllTrackerItems();
					//List<List<Object>>  resultsCheckedItem = logicObject.checkMissingTrackerItem(positiveArtifactList, allTrackerItems);
			
			//	printer.printPositiveTrackerItem(resultsCheckedItem);
			//  printer.printNegativeTrackerItem(resultsCheckedItem);
					
					
					
			         
		

		
		
		
			
			//alternatives Login aus Context	TODO: password kann nicht verwendet werden
			//UserDto user = getUserFromContext(context);
			//logicObject.login(serviceUrl, user);
			
				
			//return readerObject.getUrl() + " " + readerObject.getUser() + " "  +readerObject.getPassword() + " "  ;
			
	/*		
			// set up Velocity context
			VelocityContext velocityContext = getDefaultVelocityContextFromContext(context);
			
			String selectProject = "TestProject1";
			velocityContext.put("project", selectProject);
			
			//find and print all codebeamer projects
			ProjectDto projects[] = logicObject.readAllProjects();		
			velocityContext.put("projects", printer.printAllProjects(projects));
			
			//find a specific project in codebeamer
			ProjectDto project = logicObject.findProject(projects, selectProject);
			
			//find and print all Attachments from this specific project
			List<int[]> positiveArtifactList=null;
			List<ArtifactDto>  projectAllAttachments = logicObject.readAllAttachmentsByProject(project);
			velocityContext.put("attachments", printer.printAllAttachmentsByProject(projectAllAttachments));
			
			//find all Associations in this specific project
			AssociationDto[] allAssoc = logicObject.readAllAssociations();	
			if (allAssoc!= null && projectAllAttachments != null)
			{
						//match Associations to Attachments
						positiveArtifactList = logicObject.checkAssociationToAttachments (allAssoc, projectAllAttachments, null, -1);
						
						//check if every Attachment has minimum one association
						List<List<Object>>  resultsCheckedAtt = logicObject.checkMissingAttachment(positiveArtifactList,projectAllAttachments);
						
						//print results 
						velocityContext.put("positiveAttResults", printer.printPositiveMatchingResults(resultsCheckedAtt));
						velocityContext.put("negativeAttResults", printer.printNegativeMatchingResults(resultsCheckedAtt));
						velocityContext.put("positiveAssocs", printer.printPositiveAssoc(resultsCheckedAtt));
			
						//TrackerItemDto[] trackerAllItems = logicObject.readTrackerItemsByTracker(5);	//spezifischer Tracker
						//List<List<Object>>  resultsCheckedItem = logicObject.checkMissingTrackerItem(positiveArtifactList, trackerAllItems);
	
						List<TrackerItemDto> allTrackerItems= logicObject.getAllTrackerItems();
						List<List<Object>>  resultsCheckedItem = logicObject.checkMissingTrackerItem(positiveArtifactList, allTrackerItems);
						velocityContext.put("positiveItems", printer.printPositiveTrackerItem(resultsCheckedItem));
						velocityContext.put("negativeItems", printer.printNegativeTrackerItem(resultsCheckedItem));
						
					
				            
				            ImageWriter imageWriter = new ImageWriter ();
							imageWriter.writeImage(printer.printPositiveTrackerItem(resultsCheckedItem), printer.printNegativeTrackerItem(resultsCheckedItem));
			
				      
				          
				            //velocityContext.put("ImageURL", "d:\\circle.png");
				            velocityContext.put("ImageURL", "http://kress.de/uploads/pics/Bild-bild-Logo_30.jpg");
				           // http://www.liferay.com/community/forums/-/message_boards/message/4280641 //TODO: datentyp?
					           
				            
				            //http bilder ja, locale nein. wieso?
				            
				            //http://www.liferay.com/web/guest/community/wiki/-/wiki/Main/Velocity+for+dummies
				            //https://community.nomagic.com/add-external-images-to-generated-reports-using-velocity-t952.html
			}
			else
				System.out.println("no Results possible");

			logicObject.logout ();

		// render template
			
			
		return renderPluginTemplate("pluginTest.vm", velocityContext); */
			
			
			

			
	}
	
	
	
}
