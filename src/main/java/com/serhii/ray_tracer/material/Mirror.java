package com.serhii.ray_tracer.material;

import com.serhii.ray_tracer.util.Rand;
import com.serhii.ray_tracer.vector.HitRecord;
import com.serhii.ray_tracer.vector.Ray;
import com.serhii.ray_tracer.vector.Vec3;

public class Mirror extends Material{

	public Mirror(Vec3 color, Vec3 luminosity, double scattering) {
		super(color, luminosity, scattering);
	}
	
	public Mirror(Vec3 color, double scattering) {
		super(color, scattering);
	}
	
	public Mirror(Vec3 color) {
		super(color);
	}
	
	public Mirror(){
		this(new Vec3(1));
	}

	@Override
	public Ray getScatteredRay(HitRecord hr, Ray ray) {

		Vec3 normal = hr.getNormal();
		Vec3 direction = ray.getDirection();
		Vec3 reflectedDirection = getReflected(normal, direction);
		if (scattering != 0){
			reflectedDirection = reflectedDirection.getUnitVector().add(Rand.randInSphere().mult(scattering));
		}
		if(reflectedDirection.dot(normal) * direction.dot(normal) > 0){
			reflectedDirection = getReflected(normal, reflectedDirection);
		}
		return new Ray(hr.getPoint(), reflectedDirection);
	}
	
	private Vec3 getReflected(Vec3 normal, Vec3 direction){
		return normal.mult(-2*normal.dot(direction)).add(direction);
	}

}
