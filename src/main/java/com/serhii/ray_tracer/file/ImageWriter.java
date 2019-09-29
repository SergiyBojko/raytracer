package com.serhii.ray_tracer.file;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;

import com.serhii.ray_tracer.vector.Vec3;

public class ImageWriter {

	private static final String DEFAULT_OUTPUT = "target/img/out.bmp";
	private static final int DEFAULT_IMAGE_TYPE = BufferedImage.TYPE_INT_RGB;
	
	public static void writeImage(Vec3[] imageData, int width, int height){
		writeImage(imageData, DEFAULT_OUTPUT, width, height);
	}
	
	public static void writeImage(Vec3[] imageData, String path, int width, int height){
		BufferedImage image = new BufferedImage(width, height, DEFAULT_IMAGE_TYPE);
		Raster raster = image.getData();
		WritableRaster wr = raster.createCompatibleWritableRaster();
		wr.setPixels(0, 0, wr.getWidth(), wr.getHeight(), flatArray(imageData));
		image.setData(wr);
		try {
			ImageIO.write(image, "bmp", new File(path));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void writeImage (int[] imageData, int width, int height, String path, int imgType){
		BufferedImage image = new BufferedImage(width, height, imgType);
		Raster raster = image.getData();
		WritableRaster wr = raster.createCompatibleWritableRaster();
		wr.setPixels(0, 0, wr.getWidth(), wr.getHeight(), imageData);
		image.setData(wr);
		try {
			ImageIO.write(image, "bmp", new File(path));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void writeImage (int[] imageData, int width, int height){
		writeImage(imageData, width, height, DEFAULT_OUTPUT, DEFAULT_IMAGE_TYPE);
	}
	
	private static int[] flatArray(Vec3[] array){
		int[] result = new int[array.length * array[0].getDimensions()];
		for (int i = 0; i < array.length; i++){
			Vec3 vec = array[i];
			for(int j = 0; j < vec.getDimensions(); j++){
				result[i*vec.getDimensions() + j] = (int)vec.get(j);
			}
		}
		return result;
	}
}
