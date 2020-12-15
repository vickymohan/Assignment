package com.allocator.main;

import com.allocator.service.ResourceAllocator;

public class MainClass {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		ResourceAllocator resource = new ResourceAllocator();
		resource.getCosts(29.00,8);//Cost given but No CPU Constraint
		
		/*Uncomment the below lines to execute for the given constraints*/
		
		//resource.getCosts(20, 8);//CPU Count given but No Cost Constraint
		//resource.getCosts(30.0,40,8);// Both Cost and CPU Constraint given
		
	}

}
