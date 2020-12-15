package com.allocator.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;

import com.allocator.model.Allocation;
import com.allocator.resource.CPUResource;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ResourceAllocator {
	/**
	Service class that has all the main logic to compute the results
	*/
	CPUResource resource = null;
	private int tempCPU;
	private double tempCost;
	Map<String,Object> jsonMap;
	
	public ResourceAllocator() {
		try {
			resource = new CPUResource();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		jsonMap= resource.getResourceDetails();
	}
	
	private int getCountForCost(Double cost) {
		int count=0;
		/**
		Get total number of server required for the given cost
		
		@param
		cost : cost of the CPUType e.g. xlarge : $0.32
		
		@return total count of servers required for that particular cpuType under the given cost
		*/
		while(tempCost>0&&tempCost>=cost) {
			tempCost = tempCost - cost;
			count++;
		}
		return count;
	}
	
	
	private int getCountForCPU(int numOfCPU) {
		int count=0;
		/**
		Get total number of server required for the given CPU Count
		
		@param
		numCPU : cost of the CPUType e.g. xlarge : 32
		
		@return total count of servers required for that particular cpuType under the total CPU constraint
		*/
		while(tempCPU>0&&tempCPU>=numOfCPU) {
			tempCPU = tempCPU - numOfCPU;
			count++;
		}
		return count;
	}
	
	private int getCountForCostAndCPU(Double cost,int numOfCPU) {
		int count=0;
		/**
		Get total number of server required for the given CPU Count and Cost
		We would iterate until one of it reaches the limit
		
		We stop the iteration and update the count for the first one(either CPUCount or Cost)
		to satisfy the condition
		
		@param
		numCPU : cost of the CPUType e.g. xlarge : 32
		cost : cost of the CPUType e.g. xlarge : $0.32
		
		@return total count of servers required for that particular cpuType under the given cost and CPU constraint
		*/
		while(tempCPU>0&&tempCPU>=numOfCPU&&tempCost>0&&tempCost>=cost) {
			tempCPU = tempCPU - numOfCPU;
			tempCost = tempCost - cost;
			count++;
		}
		return count;
	}
	public void getCosts(double maxCost,int totalHours) {
		getCosts(maxCost,0,totalHours);
	}
	public void getCosts(int cpu,int totalHours) {
		getCosts(0.0,cpu,totalHours);
	}
	public void getCosts(double maxCost,int cpu, int totalHours) {
		/**
		Compute the output based on the inputs given
		Fetch the largets input first and keep on reducing the cost or cpu value
		until it reaches zero or becomes smaller than the value.
		If its smaller than the processing value then fetch the next greatest input and do the same thing again.
		Here greatest or smallest is based on the CPU or Cost
		
		@param 
		maxCost : maxium cost that customer can bear
		cpu : minimum number of CPU required by the cutomer
		totalHours : Total Hours the server is required by the customer
		
		*/
		
		List<Allocation> listOfCPU = new ArrayList<Allocation>();
		
		for(Map.Entry<String, Object> e :jsonMap.entrySet()) {
			tempCPU = cpu;
			tempCost = maxCost;
			Allocation allocate = new Allocation();
			double totalCost=0;
			@SuppressWarnings("unchecked")
			Map<String,Double> values = (LinkedHashMap<String,Double>)(e.getValue());
			
			for(Map.Entry<String, Double> value : values.entrySet()) {
				String cpuType = value.getKey();
				Double cost = value.getValue();
				int count;
				int numOfCPU = resource.getNumberOfCPUForCPUType().get(cpuType);
				//for CPU Only constraint
				if(maxCost==0.0&&cpu!=0)
					count = getCountForCPU(numOfCPU);
				//for Cost Only COnstraint
				else if(cpu==0&&maxCost!=0.0)
					count = getCountForCost(cost);
				//For Cost and CPU COnstraint
				else
					count = getCountForCostAndCPU(cost, numOfCPU);
				//Update the object if there are servers available
				if(count!=0) {
					totalCost+= count*cost;
					allocate.servers.put(cpuType, count);
				}
			}
			
			allocate.totalCosts = BigDecimal.valueOf(totalCost)
	    			  			 .setScale(2, RoundingMode.HALF_UP)
	    			  			 .doubleValue();;
			allocate.region = e.getKey();
			// add the servers to the list and iterate for the next servers
			listOfCPU.add(allocate);
			         
		}
		printOutput(listOfCPU);
	}
	
	
	private void printOutput(Object obj) {
		/**
		Print the output in the given JSON format
		
		@param 
		obj :  object to be printed in JSON format
		*/
		ObjectMapper mapper = new ObjectMapper();
		try {
			System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj));
		} catch (JsonProcessingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
