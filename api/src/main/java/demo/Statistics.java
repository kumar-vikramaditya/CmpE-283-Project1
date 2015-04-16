/*================================================================================
Copyright (c) 2008 VMware, Inc. All Rights Reserved.

Redistribution and use in source and binary forms, with or without modification, 
are permitted provided that the following conditions are met:

 * Redistributions of source code must retain the above copyright notice, 
this list of conditions and the following disclaimer.

 * Redistributions in binary form must reproduce the above copyright notice, 
this list of conditions and the following disclaimer in the documentation 
and/or other materials provided with the distribution.

 * Neither the name of VMware, Inc. nor the names of its contributors may be used
to endorse or promote products derived from this software without specific prior 
written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. 
IN NO EVENT SHALL VMWARE, INC. OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, 
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT 
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR 
PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
POSSIBILITY OF SUCH DAMAGE.
================================================================================*/

package demo;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import com.vmware.vim25.PerfCompositeMetric;
import com.vmware.vim25.PerfCounterInfo;
import com.vmware.vim25.PerfEntityMetric;
import com.vmware.vim25.PerfEntityMetricBase;
import com.vmware.vim25.PerfEntityMetricCSV;
import com.vmware.vim25.PerfMetricId;
import com.vmware.vim25.PerfMetricIntSeries;
import com.vmware.vim25.PerfMetricSeries;
import com.vmware.vim25.PerfMetricSeriesCSV;
import com.vmware.vim25.PerfQuerySpec;
import com.vmware.vim25.PerfSampleInfo;
import com.vmware.vim25.mo.InventoryNavigator;
import com.vmware.vim25.mo.PerformanceManager;
import com.vmware.vim25.mo.ServiceInstance;
import com.vmware.vim25.mo.VirtualMachine;
 

public class Statistics 
{
	ArrayList<Long> cpuStatInfoArrayList = new ArrayList<Long>();
	ArrayList<Long> memoryStatInfoArrayList = new ArrayList<Long>();
	ArrayList<String> dateInfo= new ArrayList<String>();
	
	ArrayList<ArrayList> statInfo= new ArrayList<ArrayList>();
	SimpleDateFormat sdf = new SimpleDateFormat("YYY,MM,dd,HH,mm");

  public ArrayList getStatistics(String urlString, String userName, String password, String vmName) throws Exception
  {
/*    if(args.length != 4)
    {
      System.out.println("Usage: java GetMultiPerf " + "<url> <username> <password> <vmname>");
      return;
    }
*/
	//ServiceInstance si = new ServiceInstance(new URL(args[0]), args[1], args[2], true);
    URL url = new URL(urlString);
	ServiceInstance si = new ServiceInstance(url, userName, password, true);
    //String vmname = args[3];
	String vmname = vmName;
    VirtualMachine vm = (VirtualMachine) new InventoryNavigator(si.getRootFolder()).searchManagedEntity("VirtualMachine", vmname);

    if(vm == null)
    {
      System.out.println("Virtual Machine " + vmname + " cannot be found.");
      si.getServerConnection().logout();
      return null;
    }
    System.out.println("VM: "+vm.getName());
    System.out.println("get_value: "+vm.getSummary().getVm().get_value());
    PerformanceManager perfMgr = si.getPerformanceManager();

 /*   for(PerfCounterInfo pci:perfMgr.getPerfCounter()){
    	System.out.println(pci);
    }
  */  
    //int perfInterval = 1800; // 30 minutes for PastWeek
    int perfInterval = 300;
    
    // retrieve all the available perf metrics for vm
    //PerfMetricId[] pmis = perfMgr.queryAvailablePerfMetric(vm, null, null, perfInterval);
    PerfMetricId[] pmis = perfMgr.queryAvailablePerfMetric(vm, null, null, perfInterval);
    PerfCounterInfo[] o = perfMgr.getPerfCounter();
    Map<String, Integer> perfCounterInfoMap = new HashMap<String, Integer>();
    for(int k=0;k<o.length;k++)
    {
    	perfCounterInfoMap.put(o[k].getNameInfo().key, o[k].getKey());
 //   	System.out.println(o[k].getNameInfo().key + "," + o[k].getNameInfo().summary + "," + o[k].getKey() );
    	//usage,CPU usage as a percentage during the interval,2
    	//usage,Memory usage as percentage of total configured or available memory,24
    }
    //-->>>>>>>>>
    for(PerfMetricId id : pmis)
        {id.setInstance("");}
    //-->>>>>>>>>
    
    Calendar curTime = si.currentTime();
    
    PerfQuerySpec qSpec = new PerfQuerySpec();
    qSpec.setEntity(vm.getRuntime().getHost());
    //metricIDs must be provided, or InvalidArgumentFault 
    qSpec.setMetricId(pmis);
    qSpec.setFormat("normal"); //optional since it's default
    qSpec.setIntervalId(perfInterval); 

    Calendar startTime = (Calendar) curTime.clone();
    startTime.roll(Calendar.DATE, -1);
    System.out.println("start:" + startTime.getTime());
    qSpec.setStartTime(startTime);
    
    Calendar endTime = (Calendar) curTime.clone();
    endTime.roll(Calendar.DATE, 0);
    System.out.println("end:" + endTime.getTime());
    qSpec.setEndTime(endTime);
    
    PerfCompositeMetric pv = perfMgr.queryPerfComposite(qSpec);
    if(pv != null)
    {
      //printPerfMetric(pv.getEntity());
      PerfEntityMetricBase[] pembs = pv.getChildEntity();
      System.out.println("pembs length: "+pembs.length);
      for(int i=0; pembs!=null && i< pembs.length; i++)
      {
    	  if(vm.getSummary().getVm().get_value().equals(pembs[i].getEntity().get_value())){
    	  printPerfMetric(pembs[i]);
    	  }
      }
    }
    si.getServerConnection().logout();
    return statInfo;
 
  }

  void printPerfMetric(PerfEntityMetricBase val)
  {
    String entityDesc = val.getEntity().getType() + ":" + val.getEntity().get_value();
    System.out.println("Entity:" + entityDesc);
    System.out.println();
    
    if(val instanceof PerfEntityMetric)
    {
//    	ManagedObjectReference source = val.getEntity();
//    	System.out.println("MOR: >>>>>>>>>>>>>>>>>>>>>>"+source.get_value());
      printPerfMetric((PerfEntityMetric)val);
    }
    else if(val instanceof PerfEntityMetricCSV)
    {
      printPerfMetricCSV((PerfEntityMetricCSV)val);
    }
    else
    {
      System.out.println("UnExpected sub-type of " + "PerfEntityMetricBase.");
    }
  }
  
  void printPerfMetric(PerfEntityMetric pem)
  {
	  System.out.println("PerfEntityMetric for: "+pem.getEntity().get_value());
    PerfMetricSeries[] vals = pem.getValue();
    PerfSampleInfo[]  infos = pem.getSampleInfo();
    
    long[] statValue;
    for(int i=0; vals!=null && i<vals.length; i++){
    	if(vals[i].getId().getCounterId() == 6 ){
    		System.out.println("CPU statistics >>>>>>>>>>>>>>>>>>>");
	    	if(vals[i] instanceof PerfMetricIntSeries)
		    {
				PerfMetricIntSeries val = (PerfMetricIntSeries) vals[i];
				statValue = val.getValue();
				for(int k=0; k<statValue.length-1; k++) 
				{
					//cpuStatInfoArrayList.add(sdf.format(infos[k].getTimestamp().getTime()).toString()+"#"+statValue[k]+"*");
					cpuStatInfoArrayList.add(statValue[k]);
					System.out.println("[new Date("+sdf.format(infos[k].getTimestamp().getTime()).toString()+") , "+statValue[k]+"],");
			//		CPUStatInfoMap.put(sdf.format(infos[k].getTimestamp().getTime()).toString(), statValue[k]);
				}
				//cpuStatInfoArrayList.add(sdf.format(infos[statValue.length-1].getTimestamp().getTime()).toString()+"#"+statValue[statValue.length-1]);
			//	System.out.println("[new Date("+sdf.format(infos[statValue.length-1].getTimestamp().getTime()).toString()+") , "+statValue[statValue.length-1]+"]");
		    }
    	}
    	if(vals[i].getId().getCounterId() == 24){
    		System.out.println("Memory statistics >>>>>>>>>>>>>>>>>>>");
	    	if(vals[i] instanceof PerfMetricIntSeries)
		    {
				PerfMetricIntSeries val = (PerfMetricIntSeries) vals[i];
				statValue = val.getValue();
				for(int k=0; k<statValue.length-1; k++) 
				{
					dateInfo.add(sdf.format(infos[k].getTimestamp().getTime()).toString());
					memoryStatInfoArrayList.add(statValue[k]);
					System.out.println("[new Date("+sdf.format(infos[k].getTimestamp().getTime()).toString()+") , "+statValue[k]+"],");
					//memoryStatInfoMap.put(sdf.format(infos[k].getTimestamp().getTime()).toString(), statValue[k]);
				}
				//memoryStatInfoArrayList.add(sdf.format(infos[statValue.length-1].getTimestamp().getTime()).toString()+"# "+statValue[statValue.length-1]);
			//	System.out.println("[new Date("+sdf.format(infos[statValue.length-1].getTimestamp().getTime()).toString()+") , "+statValue[statValue.length-1]+"]");
		    }
    	}
    }
    /*	System.out.println("CPU Statistics: ");
    	for(Map.Entry<String, Long> entry: CPUStatInfoMap.entrySet()){
    		System.out.println(entry.getKey()+" "+entry.getValue());
    	}
    	System.out.println("Memory Statistics");
    	for(Map.Entry<String, Long> entry: memoryStatInfoMap.entrySet()){
    		System.out.println(entry.getKey()+" "+entry.getValue());
    	}
    */		
    	statInfo.add(dateInfo);
    	statInfo.add(cpuStatInfoArrayList);
    	statInfo.add(memoryStatInfoArrayList);
    
    //	statInfo.add(cpuStatInfoArrayList1);
     //   statInfo.add(cpuStatInfoArrayList2);
//    	Object[] statInfoAll = (Object[]) new Object();
//    	statInfoAll.
  }
    
  void printPerfMetricCSV(PerfEntityMetricCSV pems)
  {
    System.out.println("SampleInfoCSV:" + pems.getSampleInfoCSV());
    PerfMetricSeriesCSV[] csvs = pems.getValue();
    for(int i=0; i<csvs.length; i++)
    {
      System.out.println("PerfCounterId:" + csvs[i].getId().getCounterId());
      System.out.println("CSV sample values:" + csvs[i].getValue());
    }
  }
  
}