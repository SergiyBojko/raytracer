package com.serhii.ray_tracer.material;

import com.serhii.ray_tracer.vector.HitRecord;
import com.serhii.ray_tracer.vector.Ray;
import com.serhii.ray_tracer.vector.Vec3;

public abstract class Material {
	
	protected Vec3 color;
	protected Vec3 luminosity;
	protected double scattering;
	
	public Material (Vec3 color){
		this(color, 0);
	}
	
	public Material (Vec3 color, double scattering){
		this(color, new Vec3(0), scattering);
	}
	
	public Material (Vec3 color, Vec3 luminosity, double scattering){
		this.color = color;
		this.luminosity = luminosity;
		this.scattering = scattering;
	}
	
	public abstract Ray getScatteredRay(HitRecord hr, Ray ray);

	public Vec3 getColor() {
		return color;
	}

	public Vec3 getLuminosity() {
		return luminosity;
	}
}
