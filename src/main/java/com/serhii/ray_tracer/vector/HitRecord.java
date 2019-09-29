package com.serhii.ray_tracer.vector;

import com.serhii.ray_tracer.material.Material;

public class HitRecord {
	private double distance;
	private Vec3 point;
	private Vec3 normal;
	private Material material;
	
	public HitRecord(double t, Vec3 point, Vec3 normal, Material material) {
		super();
		this.distance = t;
		this.point = point;
		this.normal = normal;
		this.material = material;
	}

	public double getDistance() {
		return distance;
	}

	public Vec3 getPoint() {
		return point;
	}

	public Vec3 getNormal() {
		return normal;
	}
	
	public Material getMaterial(){
		return material;
	}
	
	public Ray getScatteredRay(Ray ray){
		return material.getScatteredRay(this, ray);
	}
}
