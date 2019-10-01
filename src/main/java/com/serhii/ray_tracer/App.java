package com.serhii.ray_tracer;

import java.util.ArrayList;

import com.serhii.ray_tracer.camera.Camera;
import com.serhii.ray_tracer.camera.Camera.Lense;
import com.serhii.ray_tracer.camera.DifferenceMinimazingCamera;
import com.serhii.ray_tracer.drawable.*;
import com.serhii.ray_tracer.file.ImageWriter;
import com.serhii.ray_tracer.material.Material;
import com.serhii.ray_tracer.material.Matte;
import com.serhii.ray_tracer.material.Mirror;
import com.serhii.ray_tracer.material.Transparent;
import com.serhii.ray_tracer.vector.Vec3;

public class App 
{
	public static final int MAX_BOUNCES = 200;
	public final static int WIDTH = 300;
	public final static int HEIGHT = 200;
	public final static int SAMPLE_BATCH = 1;
//	public static final int MAX_BATCH_SIZE = 256;
	public final static double MAX_DEVIATION = 0.01;
	
	private static ArrayList<Hitable> objects = new ArrayList<>();
	private static Scene scene = new Scene(objects);
	
    public static void main( String[] args )
    {
    	Material brightYellow = new Matte(new Vec3(1, 1, 0.2), new Vec3(10));
    	Material brightRed = new Matte(new Vec3(1, 0.2, 0.2), new Vec3(2));
    	Material matteRed = new Matte(new Vec3(0.9, 0, 0));
    	Material mirror = new Mirror(new Vec3(0.9));
    	Material matteWhite = new Matte(new Vec3(0.5));
    	Material matteGreen = new Matte(new Vec3(0.2, 0.95, 0.2));
    	Material matteBlue = new Matte(new Vec3(0.2, 0.95, 0.95));
    	Material transparentBlue = new Transparent(new Vec3(0.3, 0.3, 1), 3);
    	
//    	spheres.add(new Sphere(new Vec3(100, 40, -80), 20, new Vec3(1, 0, 0)));
//    	spheres.add(new Sphere(new Vec3(-50, 50, -70), 50, new Vec3(0, 1, 0)));
		objects.add(new Parallelogram(new Vec3(20, -10, -25), new Vec3(0, 10, 0), new Vec3(-10, 0, -10), new Matte(new Vec3(0.5))));
    	objects.add(new Sphere(new Vec3(-25, 0, -100), 30, matteRed));
    	objects.add(new Sphere(new Vec3(35, -20, -20), 10, brightRed));
    	objects.add(new Sphere(new Vec3(-40, 20, -65), 8, brightYellow));
    	objects.add(new Sphere(new Vec3(0, -14, -64), 16, transparentBlue));
    	objects.add(new Sphere(new Vec3(-40, -14, -55), 16, new Transparent(new Vec3(0.95), new Vec3(0), 0.05, 2)));
    	objects.add(new Sphere(new Vec3(40, 0, -80), 30, mirror));
    	objects.add(new Plane(new Vec3(0, -30, 0), new Vec3(0, 1, 0), matteWhite));
//    	scene.add(new Plane(new Vec3(0, 100, 0), new Vec3(0, 1, 0), matteWhite));
//		objects.add(new Plane(new Vec3(0, 0, -50), new Vec3(0, 0, -1), new Transparent(new Vec3(1), new Vec3(0), 0.15, 1)));
//    	scene.add(new Plane(new Vec3(-65, 0, -1 20), new Vec3(1, 0, 1), mirror));
//    	scene.add(new Plane(new Vec3(65, 0, -120), new Vec3(1, 0, -1), mirror));
//    	
		Vec3 origin = new Vec3(0, -5, 0);
		Vec3 lookAt = new Vec3(0, -14, -55);
//		Camera camera = new BatchingCamera(
//				origin, lookAt, Math.PI/4, (double)WIDTH/HEIGHT, scene,
//				SAMPLE_BATCH, MAX_BATCH_SIZE, MAX_DEVIATION);
		Camera camera = new DifferenceMinimazingCamera(
				origin, lookAt, Math.PI/4, (double)WIDTH/HEIGHT, scene,
				MAX_DEVIATION, SAMPLE_BATCH);
		camera.setLense(new	 Lense(1, 90));
		
        Vec3[] imageData = camera.getImageData(WIDTH, HEIGHT);
        ImageWriter.writeImage(imageData, WIDTH, HEIGHT);
    }
}
