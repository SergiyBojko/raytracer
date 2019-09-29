package com.serhii.ray_tracer.material;

import com.serhii.ray_tracer.util.Rand;
import com.serhii.ray_tracer.vector.HitRecord;
import com.serhii.ray_tracer.vector.Ray;
import com.serhii.ray_tracer.vector.Vec3;

public class Matte extends Material{

	public Matte(Vec3 color, Vec3 luminosity) {
		super(color, luminosity, 999);
	}

	public Matte(Vec3 color) {
		super(color);
	}
	
	@Override
	public Ray getScatteredRay(HitRecord hr, Ray ray) {
		double dot = 0;
		Vec3 randDirection = null;
		do {
			randDirection = Rand.randInSphere();
			dot = hr.getNormal().dot(randDirection);
		} while (dot < 0);
		return new Ray(hr.getPoint(), randDirection);
	}
	
}
