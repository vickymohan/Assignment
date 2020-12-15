package com.allocator.model;
import java.util.LinkedHashMap;
import java.util.Map;
public class Allocation {
	/**
	Plain class that is used to de-serialize the JSON object to the class attributes.
	This class structure is used to build the resultant output.
	
	@param 
	region : the region of the server
	totalCost :  total cost of that region
	servers : Map of cpuType and the number of such type needed.
	
	*/
	public String region;
	public double totalCosts;
	public Map<String,Integer> servers;
	public Allocation() {
		this.servers = new LinkedHashMap<String,Integer>();
	}
	
	
}
