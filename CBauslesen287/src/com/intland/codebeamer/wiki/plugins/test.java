package com.intland.codebeamer.wiki.plugins;

import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
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
		Reader reader =new Reader();
		Printer printer = new Printer();
		String serviceUrl = "http://localhost:8080/cb/remote-api";
		String login = "bond";
		String password = "007";
		reader.login(serviceUrl, login, password);
	
		
		ProjectDto projects[] = reader.readAllProjects();		
		ProjectDto project = reader.findProject(projects, "TestProject1");
		
		List<int[]> positiveArtifactList=null;
		List<ArtifactDto> projectAllAttachment = reader.readAllAttachmentsByProject(project);
		printer.printAllAttachmentsByProject(projectAllAttachment);
		AssociationDto[] allAssoc = reader.readAllAssociations();	
		
		if (allAssoc!= null && projectAllAttachment != null)
		{
					positiveArtifactList = reader.checkAssociationToAttachments (allAssoc, projectAllAttachment, null, -1);
					//printer.printPositiveMatching(positiveArtifactList);
					List<List<Object>>  resultsCheckedAtt = reader.checkMissingAttachment(positiveArtifactList,projectAllAttachment);
					
					//TrackerItemDto[] trackerAllItems = reader.readTrackerItemsByTracker(5);	
					//List<List<Object>>  resultsCheckedItem = reader.checkMissingTrackerItem(positiveArtifactList, trackerAllItems);
					
					List<TrackerItemDto> allTrackerItems= reader.getAllTrackerItems();
					List<List<Object>>  resultsCheckedItem = reader.checkMissingTrackerItem(positiveArtifactList, allTrackerItems);
					List negative =printer.printNegativeTrackerItem(resultsCheckedItem);
					List positive =printer.printPositiveTrackerItem(resultsCheckedItem);
					
					ImageWriter imageWriter = new ImageWriter ();
					imageWriter.writeImage(positive, negative);
					
					
					//List al =printer.printPositiveAssoc(resultsCheckedAtt);
		/*			Iterator itr = al.iterator();
			while(itr.hasNext()) {
	
				TrackerItemDto element = (TrackerItemDto) itr.next(); 
			    System.out.println(element.getId() + " " + element.getName());
			}
		*/          
           
		}

	}

}
