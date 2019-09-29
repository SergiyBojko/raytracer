package com.serhii.ray_tracer.drawable;

import com.serhii.ray_tracer.material.Material;
import com.serhii.ray_tracer.vector.HitRecord;
import com.serhii.ray_tracer.vector.Ray;
import com.serhii.ray_tracer.vector.Vec3;

public class Sphere implements Hitable{
	
	private Vec3 center;
	private double radius;
	private Material material;
	
	public Sphere (Vec3 center, double radius, Material material){
		this.center = center;
		this.radius = radius;
		this.material = material;
	}
	

	@Override
	public HitRecord tryHit(Ray ray) {
		double t = getDistance(ray);
		if (t > 0){
			Vec3 intersectionPoint = ray.getPointAt(t);
			Vec3 normal = intersectionPoint.sub(center).getUnitVector();
			return new HitRecord(t, intersectionPoint, normal, material);
		}
		return null;
	}
	
	public double getDistance (Ray ray){
		Vec3 oc = ray.getOrigin().sub(center);
		double a = ray.getDirection().dot(ray.getDirection());
		double b = 2 * oc.dot(ray.getDirection());
		double c = oc.dot(oc) - Math.pow(radius, 2);
		double discriminant = Math.pow(b, 2) - 4 * a * c;
		if (discriminant < 0){
			return -1;
		} else {
			double t1 = (-b - Math.sqrt(discriminant))/(2*a);
			if (t1 > 0.00001)
				return t1;
			double t2 = (-b + Math.sqrt(discriminant))/(2*a);
			if (t2 > 0.1)
				return t2;
			return -1;
		}
	}

}
