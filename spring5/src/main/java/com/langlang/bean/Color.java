package com.langlang.bean;

public class Color {

	
	private Car car;
	
	public void setCar(Car car) {
		this.car = car;
	}
	
	public Car getCar() {
		return car;
	}

	@Override
	public String toString() {
		return "Color [car=" + car + "]";
	}
	
	
	
}
