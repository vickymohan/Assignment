package com.allocator.resource;


import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.json.JSONException;
import com.allocator.util.Converter;

public class CPUResource {
	/**
	Class that contains the logics related the available resources (hardcoded JSON)
	*/
	private String cpuTypeVsCostJson;
	private Map<String,Integer> cpuTypeVsNumOfCPU;
	public CPUResource() throws JSONException{
		//CPUType vs CostOfCPU
		this.cpuTypeVsCostJson = "{"
				  +"'us-east': {"
				    +"'large': 0.12,"
				    +"'xlarge': 0.23,"
				    +"'2xlarge': 0.45,"
				    +"'4xlarge': 0.774,"
				    +"'8xlarge': 1.4,"
				    +"'10xlarge': 2.82"
				  +"},"
				  +"'us-west': {"
				    +"'large': 0.14,"
				    +"'2xlarge': 0.413,"
				    +"'4xlarge': 0.89,"
				    +"'8xlarge': 1.3,"
				    +"'10xlarge': 2.97"
				  +"},"
				  +"'asia': {"
				    +"'large': 0.11,"
				    +"'xlarge': 0.20,"
				    +"'4xlarge': 0.67,"
				    +"'8xlarge': 1.18"
				  +"}"
				+"}";
		
		//CPUType vs Number of CPU
		this.cpuTypeVsNumOfCPU = new HashMap<String,Integer>();
		this.cpuTypeVsNumOfCPU.put("large", 1);
		this.cpuTypeVsNumOfCPU.put("xlarge", 2);
		this.cpuTypeVsNumOfCPU.put("2xlarge", 4);
		this.cpuTypeVsNumOfCPU.put("4xlarge", 8);
		this.cpuTypeVsNumOfCPU.put("8xlarge", 16);
		this.cpuTypeVsNumOfCPU.put("10xlarge", 32);
		
	}
	
	private Map<String, Object> convertJsonToHashMap(){
		/**
		Convert the JSON into HashMap object to make operations easy.
		@return The converted HashMap
		@throws JSONException 
		*/
		Map<String, Object> jsonMap=null;
		try {
			jsonMap = new LinkedHashMap<>(Converter.jsonToMap(this.cpuTypeVsCostJson));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonMap;
	}

	public Map<String,Integer> getNumberOfCPUForCPUType(){
		/**
		Get the number of CPU assigned for each CPUType
		Sort the Map in DESC order to ensure that we assign maximum number of CPU for minimum cost.
		@return the CPUType Vs. number of CPU key-value pair(HashMap) in DESC order of No. of CPU
		*/
		Map<String,Integer> map;
		
		map = this.cpuTypeVsNumOfCPU.entrySet()
		          .stream()
		          .sorted((e1, e2) -> Integer.compare((Integer)e2.getValue(), (Integer)e1.getValue()))
		          .collect(Collectors.toMap(Map.Entry::getKey,
		                                    Map.Entry::getValue,
		                                    (e1, e2) -> e1,
		                                    LinkedHashMap::new));
		return map;
	}
	public Map<String,Object> getResourceDetails(){
		/**
		Sort the created HashMap converted JSON in accordance to the price of each server
		Follow descending order so as to provide more CPU's for the given amount
		@return Sorted Hashmap (Sorted on price)
		*/
		Map<String,Object> jsonMap = convertJsonToHashMap();
		for(Map.Entry<String, Object> e :jsonMap.entrySet()) {
			@SuppressWarnings("unchecked")
			Map<String,Double> values = (LinkedHashMap<String,Double>)(e.getValue());
			
			Map<String, Double> m1s =
			        values.entrySet()
			          .stream()
			          .sorted((e1, e2) -> Double.compare((Double)e2.getValue(), (Double)e1.getValue()))
			          .collect(Collectors.toMap(Map.Entry::getKey,
			                                    Map.Entry::getValue,
			                                    (e1, e2) -> e1,
			                                    LinkedHashMap::new));
			jsonMap.put(e.getKey(), m1s);
		}
		return jsonMap;
	}
	
}
