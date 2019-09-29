package com.serhii.ray_tracer.drawable;

import com.serhii.ray_tracer.vector.HitRecord;
import com.serhii.ray_tracer.vector.Ray;

public interface Hitable {
	HitRecord tryHit(Ray ray);
}
