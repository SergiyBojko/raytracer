package com.serhii.ray_tracer.material;

import java.util.Random;

import com.serhii.ray_tracer.util.Rand;
import com.serhii.ray_tracer.vector.HitRecord;
import com.serhii.ray_tracer.vector.Ray;
import com.serhii.ray_tracer.vector.Vec3;

public class Transparent extends Material{
	
	private double refractionIndex;

	public Transparent(Vec3 color, Vec3 luminosity, double scattering, double refraction) {
		super(color, luminosity, scattering);
		this.refractionIndex = refraction;
	}
	
	public Transparent(Vec3 color, Vec3 luminosity, double refraction) {
		this(color, luminosity, 0, refraction);
	}
	
	public Transparent(Vec3 color, double refraction) {
		this(color, new Vec3(0, 0, 0), refraction);
	}
	
	public Transparent(double refraction) {
		this(new Vec3(1, 1, 1), refraction);
	}
	
	public Transparent(double scattering, double refraction){
		this(new Vec3(1, 1, 1), new Vec3(0, 0, 0), scattering, refraction);
	}



	@Override
	public Ray getScatteredRay(HitRecord hr, Ray ray) {
		Ray scattered = null;
		Vec3 normal = hr.getNormal().getUnitVector();
		Vec3 direction = ray.getDirection().getUnitVector();
		double dot = normal.dot(direction);
		double refraction = 0;
		if (dot > 0){
			refraction = this.refractionIndex;
			dot = -1 * dot;
			
			if (scattering > 0){
				//try scatter ray when inside
				double maxTravelledDistance = ray.getOrigin().sub(hr.getPoint()).getLength();
				double scatterDistance = scatterDistance();
				if (maxTravelledDistance > scatterDistance){
					Vec3 scatterPoint = ray.getOrigin().add(direction.mult(scatterDistance));
					Vec3 scatterDirection;
					if (Rand.rand.nextDouble() < scattering){
						scatterDirection = Rand.randInSphere();
					} else {
						scatterDirection = direction.add(Rand.randInSphere().mult(scattering));
					}
					return new Ray(scatterPoint, scatterDirection);
				}
			}
			
		} else {
			refraction = 1/this.refractionIndex;
		}
		
		double reflectionProbability = schlickReflectionProbability(Math.abs(dot), 1/refraction);
		if (Rand.rand.nextDouble() < reflectionProbability){
			Vec3 reflected = reflect(direction, normal);
			scattered = new Ray(hr.getPoint(), reflected);
		} else {
			double refDot = 1 - refraction * refraction * (1 - dot * dot);
			if (refDot > 0){
				Vec3 refractedDirection = direction.sub(normal.mult(dot)).mult(refraction).sub(normal.mult(Math.sqrt(refDot)));
				scattered = new Ray(hr.getPoint(), refractedDirection);
			} else {
				Vec3 reflected = reflect(direction, normal);
				scattered = new Ray(hr.getPoint(), reflected);
			}
		}
		return scattered;
	}
	
	private double scatterDistance(){
		return -1/scattering * Math.log(Rand.rand.nextDouble());
	}
	
	private double schlickReflectionProbability(double cosine, double refIndex){
		double r0 = (1-refIndex)/(1+refractionIndex);
		r0 = r0 * r0;
		return r0 + (1 - r0) * Math.pow((1 - cosine), 5);
	}
	
	private Vec3 reflect (Vec3 direction, Vec3 normal){
		return normal.mult(-2*normal.dot(direction)).add(direction);
	}

}
