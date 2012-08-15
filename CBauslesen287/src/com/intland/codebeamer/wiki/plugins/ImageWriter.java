package com.intland.codebeamer.wiki.plugins;

import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

public class ImageWriter {

	public void writeImage(List positive, List negative)
	{
		DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("positive", new Double(positive.size()));
        dataset.setValue("negative", new Double(negative.size()));
		
        JFreeChart chart = ChartFactory.createPieChart(
                "Analyse Results",  // chart title
                dataset,             // data
                true,               // include legend
                true,
                false
            );

            PiePlot plot = (PiePlot) chart.getPlot();
            plot.setLabelFont(new Font("SansSerif", Font.PLAIN, 12));
            plot.setNoDataMessage("No data available");
            plot.setCircular(false);
            plot.setLabelGap(0.02);
            
            BufferedImage image = chart.createBufferedImage(500, 300);
            try {
				ImageIO.write(image, "png", new File( "d:/circle.png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	
}
