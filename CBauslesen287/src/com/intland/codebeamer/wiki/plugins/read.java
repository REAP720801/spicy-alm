package com.intland.codebeamer.wiki.plugins;



import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.velocity.VelocityContext;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

import com.ecyrd.jspwiki.WikiContext;

import com.intland.codebeamer.persistence.dto.ArtifactDto;
import com.intland.codebeamer.persistence.dto.AssociationDto;
import com.intland.codebeamer.persistence.dto.ProjectDto;
import com.intland.codebeamer.persistence.dto.TrackerItemDto;
import com.intland.codebeamer.wiki.plugins.base.AbstractCodeBeamerWikiPlugin;
import com.keypoint.PngEncoder;



public class read extends AbstractCodeBeamerWikiPlugin {
	public String execute(WikiContext context, Map params) {
			Reader reader =new Reader();
			Printer printer = new Printer();
			String serviceUrl = "http://localhost:8080/cb/remote-api";
			String login = "bond";
			String password = "007";
			reader.login(serviceUrl, login, password);
			
			// set up Velocity context
			VelocityContext velocityContext = getDefaultVelocityContextFromContext(context);
			
			String selectProject = "TestProject1";
			velocityContext.put("project", selectProject);
			
			//find and print all codebeamer projects
			ProjectDto projects[] = reader.readAllProjects();		
			velocityContext.put("projects", printer.printAllProjects(projects));
			
			//find a specific project in codebeamer
			ProjectDto project = reader.findProject(projects, selectProject);
			
			//find and print all Attachments from this specific project
			List<int[]> positiveArtifactList=null;
			List<ArtifactDto>  projectAllAttachments = reader.readAllAttachmentsByProject(project);
			velocityContext.put("attachments", printer.printAllAttachmentsByProject(projectAllAttachments));
			
			//find all Associations in this specific project
			AssociationDto[] allAssoc = reader.readAllAssociations();	
			if (allAssoc!= null && projectAllAttachments != null)
			{
						//match Associations to Attachments
						positiveArtifactList = reader.checkAssociationToAttachments (allAssoc, projectAllAttachments, null, -1);
						
						//check if every Attachment has minimum one association
						List<List<Object>>  resultsCheckedAtt = reader.checkMissingAttachment(positiveArtifactList,projectAllAttachments);
						
						//print results 
						velocityContext.put("positiveAttResults", printer.printPositiveMatchingResults(resultsCheckedAtt));
						velocityContext.put("negativeAttResults", printer.printNegativeMatchingResults(resultsCheckedAtt));
						velocityContext.put("positiveAssocs", printer.printPositiveAssoc(resultsCheckedAtt));
			
						//TrackerItemDto[] trackerAllItems = reader.readTrackerItemsByTracker(5);	//spezifischer Tracker
						//List<List<Object>>  resultsCheckedItem = reader.checkMissingTrackerItem(positiveArtifactList, trackerAllItems);
	
						List<TrackerItemDto> allTrackerItems= reader.getAllTrackerItems();
						List<List<Object>>  resultsCheckedItem = reader.checkMissingTrackerItem(positiveArtifactList, allTrackerItems);
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

			reader.logout ();

		// render template
		return renderPluginTemplate("pluginTest.vm", velocityContext);
	}
}
