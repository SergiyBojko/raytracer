package com.serhii.ray_tracer.camera;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.serhii.ray_tracer.drawable.Scene;
import com.serhii.ray_tracer.file.ImageWriter;
import com.serhii.ray_tracer.vector.Vec3;

public class DifferenceMinimazingCamera extends Camera{

	private double maxDiff;
	private int sampleCount;

	public DifferenceMinimazingCamera(Vec3 origin, Vec3 lookAt, double fow, double aspectRatio, Scene scene,
			double maxDiff, int sampleCount) {
		super(origin, lookAt, fow, aspectRatio, scene);
		this.maxDiff = maxDiff;
		this.sampleCount = sampleCount;
	}

	@Override
	public Vec3[] getImageData(int width, int height) {
		Vec3[] imageData = new Vec3[width*height];
		int[] sampleData = new int[width*height];
		
		for (int x = 0; x < width; x++){
			for (int y = 0; y < height; y++){
				Vec3 avgColor = getTotalPixelColor(x, y, width, height, sampleCount).div(sampleCount);
				sampleData[x + y * width] +=(sampleCount);
				imageData[x + y * width] = avgColor;
			}
		}
		
		List<Integer> highContrastPixels = findHighContrastPixels(imageData, width);
		resampleHighContrastPixels(highContrastPixels, imageData, sampleData, width, height);
//		maxSampleCount = additionalSamples + sampleCount;
		for (int i = 0; i < imageData.length; i++){
			imageData[i] = new Vec3 (
					Math.sqrt(Math.min(1, imageData[i].get(0))),
					Math.sqrt(Math.min(1, imageData[i].get(1))),
					Math.sqrt(Math.min(1, imageData[i].get(2)))).mult(255.99);
		}
		

		analyzeAndSaveSampleData(width, height, sampleData);
		return imageData;
	}
	
	private void resampleHighContrastPixels(
			List<Integer> highContrastPixels, Vec3[] imageData, int[] sampleData,
			int width, int height) {
		Set<Integer> neighbours = new HashSet<>();
		Set<Integer> filteredPixels = new HashSet<>();
		
		for (Integer pos : highContrastPixels){
			int x = pos%width;
			int y = pos/width;
			Vec3 additioanlColor = getTotalPixelColor(x, y, width, height, sampleData[pos]).div(sampleData[pos]);
			Vec3 avg = imageData[pos].add(additioanlColor).div(2);
			double diff = imageData[pos].sub(additioanlColor).getLength();
			if (diff/avg.getLength() > maxDiff*sampleData[pos]/sampleCount){
				filteredPixels.add(pos);
			}
			imageData[pos] = avg;
			sampleData[pos] += sampleData[pos];		
		}
		
		for (Integer pos : highContrastPixels){
			Vec3 avgColor = neighboursAvgColor(imageData, pos, width);
			Vec3 diff = avgColor.sub(imageData[pos]);
			if (isHighContrast(imageData, maxDiff*sampleData[pos]/sampleCount, pos, avgColor, diff) ){
				filteredPixels.add(pos);
			}
			neighbours.addAll(neighbourPositions(imageData, pos, width));
		}
		for (Integer pos : neighbours){
			Vec3 avgColor = neighboursAvgColor(imageData, pos, width);
			Vec3 diff = avgColor.sub(imageData[pos]);
			if (isHighContrast(imageData, maxDiff*sampleData[pos]/sampleCount, pos, avgColor, diff) ){
				filteredPixels.add(pos);
			}
		}
		List<Integer> pixelsToResample = new ArrayList<>(filteredPixels);
		if (filteredPixels.isEmpty()){
			return;
		} else {
			System.out.println("resampling " + filteredPixels.size());
			resampleHighContrastPixels(pixelsToResample, imageData, sampleData, width, height);
		}
	}

	private boolean isHighContrast(Vec3[] imageData, double maxDiff, Integer pos, Vec3 avgColor, Vec3 diff) {
		return diff.getLength()/((avgColor.getLength()+imageData[pos].getLength())/2) > maxDiff;
	}

	private List<Integer> findHighContrastPixels(Vec3[] imageData, int width){
		
		List<Integer> highContrastPixels = new ArrayList<>();
		
		for (int i = 0; i < imageData.length; i++){
			Vec3 avgColor = neighboursAvgColor(imageData, i, width);
			Vec3 diff = avgColor.sub(imageData[i]);
			if (isHighContrast(imageData, maxDiff, i, avgColor, diff)){
				highContrastPixels.add(i);
			}
		}
		System.out.println("found " + highContrastPixels.size() + " pixels to resample");
		return highContrastPixels;
	}
	
	private void analyzeAndSaveSampleData(int width, int height, int[] sampleData) {
		int maxSampleCount = 0;
		int totalSamples = 0;
		for (int i = 0; i < sampleData.length; i++){
			totalSamples += sampleData[i];
			maxSampleCount = Math.max(maxSampleCount, sampleData[i]);
		}
		for (int i = 0; i < sampleData.length; i++){
			sampleData[i] = (int) (sampleData[i]*255.99/maxSampleCount);
		}
		System.out.println("Avg samples per pixel " + (double)totalSamples/sampleData.length);
		System.out.println("Max samples " + (double)maxSampleCount);
		String sampleImgName = String.format("target/img/samples-max=%d.bmp", maxSampleCount);
		ImageWriter.writeImage(sampleData, width, height, sampleImgName, BufferedImage.TYPE_BYTE_GRAY);
	}
	
	private List<Integer> neighbourPositions (Vec3[] imageData, int position, int width){
		List<Integer> positions = new ArrayList<>();
		if (position%width != 0){
			positions.add(position-1);
		}
		if (position >= width){
			positions.add(position-width);
		}
		if ((position+1)%width != 0){
			positions.add(position+1);
		}
		if (imageData.length - position > width){
			positions.add(position+width);
		}
		return positions;
	}
	
	private Vec3 neighboursAvgColor(Vec3[] imageData, int position, int width){
		Vec3 avg = new Vec3(0);
		List<Integer> neighbourPositions = neighbourPositions(imageData, position, width);
		for (Integer pos : neighbourPositions){
			avg = avg.add(imageData[pos]);
		}
		avg = avg.div(neighbourPositions.size());
		return avg;
	}
}
