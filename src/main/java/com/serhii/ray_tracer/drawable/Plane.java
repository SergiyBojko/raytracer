package com.serhii.ray_tracer.drawable;

import com.serhii.ray_tracer.material.Material;
import com.serhii.ray_tracer.vector.HitRecord;
import com.serhii.ray_tracer.vector.Ray;
import com.serhii.ray_tracer.vector.Vec3;

public class Plane implements Hitable{
	private Vec3 origin;
	private Vec3 normal;
	private Material material;
	
	public Plane(Vec3 origin, Vec3 normal, Material material) {
		this.origin = origin;
		this.normal = normal;
		this.material = material;
	}

	@Override
	public HitRecord tryHit(Ray ray) {
		HitRecord hitRecord = null;
		double angle = ray.getDirection().dot(normal);
		double distance = origin.sub(ray.getOrigin()).dot(normal)/angle;
		if (distance > 0.00001){
			hitRecord = new HitRecord(distance, ray.getPointAt(distance), normal, material);
		}
		return hitRecord;
	}
	
	

}
