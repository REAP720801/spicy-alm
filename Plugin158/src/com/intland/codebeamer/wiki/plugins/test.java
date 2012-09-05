package com.intland.codebeamer.wiki.plugins;

import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

import com.intland.codebeamer.persistence.dto.ArtifactDto;
import com.intland.codebeamer.persistence.dto.AssociationDto;
import com.intland.codebeamer.persistence.dto.ProjectDto;
import com.intland.codebeamer.persistence.dto.TrackerDto;
import com.intland.codebeamer.persistence.dto.TrackerItemDto;

public class test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub	
		Logic logicObject =new Logic();
		Reader readerObject = new Reader();
		Printer printer = new Printer();
	
		//Dummy-Werte zum arbeiten, TODO: noch nicht scharf ob übergabe von Plugin
		String serviceUrl = "http://localhost:8080/cb/remote-api";
		String login ="bond";
		String password = "007";	
		
		readerObject.setUrl(serviceUrl);
		readerObject.setUser(login);
		readerObject.setPassword(password);
		
		readerObject.setUrl("http://localhost:8080/cb/remote-api");
		
		
	logicObject.login(readerObject.getUrl(), readerObject.getUser(), readerObject.getPassword());
	
	readerObject.setAttachment("true");
	readerObject.setTracker("false");
	
	readerObject.setNumber("true");
	readerObject.setName("false");
	
	readerObject.setPositve("true");
	readerObject.setNegative("false");
	readerObject.setAll("false");
	
	readerObject.setTrackerID("-1");
	readerObject.setProjectName("TestProject1");

	
	ProjectDto projects[] = logicObject.readAllProjects();		
	
	//find a specific project in codebeamer
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
						 printer.printAttachmentNumber(resultsCheckedAtt, readerObject.getPositve());
					}
					else if (readerObject.getNumber() && readerObject.getAll())
					{
							printer.printAllAttachmentsByProjectNumber(projectAllAttachments);
					}
					else if (readerObject.getName() && readerObject.getAll())
					{
						printer.printAllAttachmentsByProject(projectAllAttachments);	
					}
					else if (readerObject.getName() && (readerObject.getPositve() || readerObject.getNegative()))
						printer.printAttachmentName(resultsCheckedAtt, readerObject.getPositve());
					
				}
				else if (readerObject.getTracker())
				{
					if ((readerObject.getNumber() && readerObject.getAll()==false) && (readerObject.getPositve() || readerObject.getNegative()) )
					{
						 printer.printTrackerNumber(resultsCheckedItem, readerObject.getPositve());
					}
					else if (readerObject.getName() && readerObject.getAll())
					{
							printer.printAllTrackerByProject(trackerAllItems);
					}
					else if (readerObject.getNumber() && readerObject.getAll())
					{
						printer.printAllTrackerByProjectNumber(trackerAllItems);
					}
					else if (readerObject.getName() && (readerObject.getPositve() || readerObject.getNegative()))
						printer.printTrackerName(resultsCheckedItem, readerObject.getPositve());
				}
	}
		

	//possitive Ausgabe der funde, detailierte Ausgabe
		// printer.printPositiveAssoc(resultsCheckedAtt);
	
				
				 //TODO:Tracker-ID nutzen
				//List<TrackerItemDto> allTrackerItems= logicObject.getAllTrackerItems();
				//List<List<Object>>  resultsCheckedItem = logicObject.checkMissingTrackerItem(positiveArtifactList, allTrackerItems);
		

				
			
			
		         

	
	
	}
	
	 private static void generateCsvFile(String sFileName)
	   {
		try
		{
		    FileWriter writer = new FileWriter(sFileName);
	 
		    writer.append("DisplayName");
		    writer.append(','); 
		    writer.append("Age");
		    writer.append('\n');
	 
		    writer.append("MKYONG");
		    writer.append(',');
		    writer.append("26");
	            writer.append('\n');
	 
		    writer.append("YOUR NAME");
		    writer.append(',');
		    writer.append("29");
		    writer.append('\n');
	 
		    //generate whatever data you want
	 
		    writer.flush();
		    writer.close();
		}
		catch(IOException e)
		{
		     e.printStackTrace();
		} 
	    }

}
