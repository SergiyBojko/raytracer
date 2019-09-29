package com.serhii.ray_tracer.camera;

import com.serhii.ray_tracer.drawable.Scene;
import com.serhii.ray_tracer.file.ImageWriter;
import com.serhii.ray_tracer.vector.Vec3;

public class BatchingCamera extends Camera{

	int batchSize;
	int maxBatchSize;
	double maxDeviation;
	
	public BatchingCamera(Vec3 origin, Vec3 lookAt, double fow, double aspectRatio, Scene scene, int batchSize,
			int maxBatchSize, double maxDeviation) {
		super(origin, lookAt, fow, aspectRatio, scene);
		this.batchSize = batchSize;
		this.maxBatchSize = maxBatchSize;
		this.maxDeviation = maxDeviation;
	}

	@Override
	public Vec3[] getImageData(int width, int height){
		Vec3[] imageData = new Vec3[width*height];
		Vec3[] sampleData = new Vec3[width*height];
		int maxSampleCount = 0;
		for (int x = 0; x < width; x++){
			for (int y = 0; y < height; y++){
				Vec3 avgColor = new Vec3(0);
				
				int sampleCount = 0;
				int currentBatchSize = batchSize;
				Vec3 batch1Color = getTotalPixelColor(x, y, width, height, currentBatchSize);
				Vec3 batch2Color = new Vec3(0);
				for (sampleCount = 2*currentBatchSize; true; sampleCount += currentBatchSize){
					batch2Color = getTotalPixelColor(x, y, width, height, currentBatchSize);
					avgColor = batch1Color.add(batch2Color);
					if(currentBatchSize >= maxBatchSize){
						avgColor = avgColor.div(2*currentBatchSize);
						maxSampleCount = sampleCount;
						break;
					}
					double diff = batch1Color.sub(batch2Color).getLength();
					if(diff == 0 || Math.pow(diff, 2)/(avgColor.getLength()/2) < maxDeviation){
						avgColor = avgColor.div(2*currentBatchSize);
						if (sampleCount > maxSampleCount){
							maxSampleCount = sampleCount;
						}
						break;
					} else {
						batch1Color = avgColor;
						batch2Color = null;
						currentBatchSize *= 2;
					}
				}
				
				sampleData[x + y * width] = new Vec3 (sampleCount);
				
				imageData[x + y * width] = 
						new Vec3 (
						Math.sqrt(Math.min(1, avgColor.get(0))),
						Math.sqrt(Math.min(1, avgColor.get(1))),
						Math.sqrt(Math.min(1, avgColor.get(2)))).mult(255.99);
			}
		}
		
		for (int i = 0; i < sampleData.length; i++){
			sampleData[i] = sampleData[i].mult(255.99).div(maxSampleCount);
		}
		
		String sampleImgName = String.format("target/img/samples-max=%d.bmp", maxSampleCount);
		ImageWriter.writeImage(sampleData, sampleImgName, width, height);
		return imageData;
	}
}
