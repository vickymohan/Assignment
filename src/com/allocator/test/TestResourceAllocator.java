package com.allocator.test;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.allocator.model.Allocation;
import com.allocator.service.ResourceAllocator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TestResourceAllocator {
	ResourceAllocator resources;
	private final PrintStream standardOut = System.out;
	private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
	private ObjectMapper mapper;
	List<Allocation> result;
	@Before
	public void setUp() throws Exception {
		resources = new ResourceAllocator();
		System.setOut(new PrintStream(outputStreamCaptor));
		mapper = new ObjectMapper();
		result=null;
	}

	@Test
	public void testServersForAGivenCPUCount() {
		resources.getCosts(0.0, 20, 8);//CPU Count given but No Cost Constraint
		//read the console output(JSON Output)
		String output = outputStreamCaptor.toString();
		try {
			result = mapper.readValue(output,new TypeReference<List<Allocation>>(){});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals(result.get(2).region,"asia");
		assertEquals(result.get(2).totalCosts,1.58,0.001);
		assertEquals(result.get(2).servers.size(),2);
	}
	
	@Test
	public void testServersForAGivenCost() {
		resources.getCosts(29.00, 0, 8);//Cost given but No CPU Constraint
		//read the console output(JSON Output)
		String output = outputStreamCaptor.toString();
		try {
			result = mapper.readValue(output,new TypeReference<List<Allocation>>(){});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals(result.get(1).region,"us-west");
		assertEquals(result.get(1).totalCosts,28.92,0.001);
		assertEquals(result.get(1).servers.size(),3);
	}
	
	@Test
	public void testServersForAGivenCostAndCPUCOunt() {
		resources.getCosts(30.0,40,8);// Both Cost and CPU Constraint given
		//read the console output(JSON Output)
		String output = outputStreamCaptor.toString();
		try {
			result = mapper.readValue(output,new TypeReference<List<Allocation>>(){});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals(result.get(0).region,"us-east");
		assertEquals(result.get(0).totalCosts,3.59,0.001);
		assertEquals(result.get(0).servers.size(),2);
	}
	

}
