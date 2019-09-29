package com.serhii.ray_tracer.camera;

import java.util.Random;

import com.serhii.ray_tracer.App;
import com.serhii.ray_tracer.drawable.Hitable;
import com.serhii.ray_tracer.drawable.Scene;
import com.serhii.ray_tracer.material.Material;
import com.serhii.ray_tracer.util.Rand;
import com.serhii.ray_tracer.vector.HitRecord;
import com.serhii.ray_tracer.vector.Ray;
import com.serhii.ray_tracer.vector.Vec3;

public abstract class Camera {
	
	protected Vec3 origin;
	protected Vec3 lookAt;
	protected double fow;
	protected double aspectRatio;	
	protected Scene scene;
	protected Lense lense;
	
	protected Vec3 horizontal;
	protected Vec3 vertical;
	
	protected Vec3 uHorizontal;
	protected Vec3 uVertical;
	
	protected Vec3 cameraDirection;
	
	protected Random rand = new Random();
	
	public Camera(Vec3 origin, Vec3 lookAt, double fow, double aspectRatio, Scene scene) {
		this.origin = origin;
		this.lookAt = lookAt;
		this.fow = fow;
		this.aspectRatio = aspectRatio;
		this.scene = scene;
		
		cameraDirection = lookAt.sub(origin).getUnitVector();
		double verticalLength = Math.tan(fow) * cameraDirection.getLength();
		double horizonlatLength = verticalLength * aspectRatio;
		
		
		uHorizontal = new Vec3(0, 1, 0).cross(cameraDirection).getUnitVector();
		uVertical = cameraDirection.cross(uHorizontal).getUnitVector();
		horizontal = uHorizontal.mult(horizonlatLength);
		vertical = uVertical.mult(verticalLength);
	}
	
	public void setLense(Lense lense){
		this.lense = lense;
		cameraDirection = cameraDirection.getUnitVector().mult(lense.getFocusDistance());
		double verticalLength = Math.tan(fow) * cameraDirection.getLength();
		double horizonlatLength = verticalLength * aspectRatio;
		horizontal = uHorizontal.mult(horizonlatLength);
		vertical = uVertical.mult(verticalLength);
	}

	public Ray getRay(double h, double w){
		if(lense == null){
			return new Ray(origin, cameraDirection.add(horizontal.mult(h)).add(vertical.mult(w)));
		} else {
			Vec3 randInDisc = Rand.randInDisc().mult(lense.getAperture());
			Vec3 offset = uHorizontal.mult(randInDisc.get(0)).add(uVertical.mult(randInDisc.get(1)));
			return new Ray(origin.add(offset), cameraDirection.add(horizontal.mult(h)).add(vertical.mult(w)).sub(offset));
		}
	}
	
	protected Vec3 getTotalPixelColor(int x, int y, int width, int height, int samples){
		Vec3 totalColor = new Vec3(0);
		for (int i = 0; i < samples; i++){
			Ray ray = getRay(1-((double)2*x+rand.nextDouble())/width, 1-((double)2*y+rand.nextDouble())/height);
			totalColor = totalColor.add(color(ray, 0));
		}
		return totalColor;
	}
	
	protected Vec3 color (Ray ray, int bounce){
		if (ray == null){
			return new Vec3(0);
		}
		for (Hitable hitable : scene.getObjects()){
			ray.setHitRecord(hitable.tryHit(ray));
		}
		if (ray.getHitRecord() != null){
			HitRecord hr = ray.getHitRecord();
			Material mat = hr.getMaterial();
			if (bounce < App.MAX_BOUNCES){
				return color(ray.getScatteredRay(), ++bounce).add(mat.getLuminosity()).mult(mat.getColor());
			} else {
				return new Vec3(0, 0, 0);
			}
		}
		
		return scene.getSky().getColor(ray);
	}
	
	public abstract Vec3[] getImageData(int height, int width);
	
	public static class Lense {
		private double aperture;
		private double focusDistance;
		
		public Lense(double aperture, double focusDistance) {
			this.aperture = aperture;
			this.focusDistance = focusDistance;
		}

		public double getAperture() {
			return aperture;
		}

		public double getFocusDistance() {
			return focusDistance;
		}
		
	}
	
}
