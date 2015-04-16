package demo;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicLong;

import javax.validation.Valid;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.vmware.vim25.Description;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.VirtualDeviceConfigSpec;
import com.vmware.vim25.VirtualDeviceConfigSpecFileOperation;
import com.vmware.vim25.VirtualDeviceConfigSpecOperation;
import com.vmware.vim25.VirtualDisk;
import com.vmware.vim25.VirtualDiskFlatVer2BackingInfo;
import com.vmware.vim25.VirtualEthernetCard;
import com.vmware.vim25.VirtualEthernetCardNetworkBackingInfo;
import com.vmware.vim25.VirtualLsiLogicController;
import com.vmware.vim25.VirtualMachineCloneSpec;
import com.vmware.vim25.VirtualMachineConfigSpec;
import com.vmware.vim25.VirtualMachineFileInfo;
import com.vmware.vim25.VirtualMachinePowerState;
import com.vmware.vim25.VirtualMachineRelocateSpec;
import com.vmware.vim25.VirtualPCNet32;
import com.vmware.vim25.VirtualSCSISharing;
import com.vmware.vim25.mo.Datacenter;
import com.vmware.vim25.mo.Folder;
import com.vmware.vim25.mo.InventoryNavigator;
import com.vmware.vim25.mo.ManagedEntity;
import com.vmware.vim25.mo.ResourcePool;
import com.vmware.vim25.mo.ServiceInstance;
import com.vmware.vim25.mo.Task;
import com.vmware.vim25.mo.VirtualMachine;

@RestController
@RequestMapping("/api/v1")
public class AppController {

	private final AtomicLong counter = new AtomicLong();
	private long userId;
	
	private final AtomicLong counter2 = new AtomicLong();
	private long vmId;
	private int tempIndex;
	private User newUser;
	SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy'T'hh:mm:ss.SSS'T'");
	@Autowired
	private UserRepository userRespository;

	@Autowired
	private VMRepository vmRespository;
	
	public  static ServiceInstance si;
	public static Folder rootFolder;
	public static ResourcePool rp;
	public  static Datacenter dc;
	public  static ManagedEntity[] hosts;
	public  static ManagedEntity[] vms;
	
	public static String vCenterURL = "https://130.65.132.116/sdk";
	public static String userName = "administrator";
	public static String vCenterUserName = "student@vsphere.local";
	public static String password = "12!@qwQW";
	public static ServiceInstance vCenterManagerSi;
	public static Folder vCenterManagerRootFolder;
	public static String dcName = "T16-DC";
	public static Folder vmFolder;
	
	
	public  AppController() throws Exception{
		// TODO Auto-generated constructor stub
		si = new ServiceInstance(new URL(vCenterURL), userName,password, true);

	    rootFolder = si.getRootFolder();
	    
	    dc = (Datacenter) new InventoryNavigator(
	        rootFolder).searchManagedEntity("Datacenter", dcName);
	    rp = (ResourcePool) new InventoryNavigator(
	        dc).searchManagedEntities("ResourcePool")[0];
	    
	    vmFolder = dc.getVmFolder();
	}
	
	@RequestMapping(value = "/signUp", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public User signUp(@RequestBody User user) {

		List<User> myList=userRespository.findAll();
		
		//System.out.println("myList.size()"+myList.size());
		
		if(userRespository.findById(myList.size())!=null){
			userId=(userRespository.findById(myList.size()).getUserIdCounter())+1;
		}else{
			userId = 1;//counter.incrementAndGet();
		}
		
		//System.out.println("userId"+userId);
		newUser = new User();
		newUser.setUserId(userId);
		newUser.setUserIdCounter(userId);
		newUser.setFirstName(user.getFirstName());
		newUser.setLastName(user.getLastName());
		newUser.setPhoneNo(user.getPhoneNo());
		newUser.setDob(user.getDob());
		newUser.setEmail(user.getEmail());
		newUser.setCardDetails(user.getCardDetails());
		newUser.setPassword(user.getPassword());
		newUser.setUserCreatedDate(df.format(new Date()));
		userRespository.save(newUser);

		return newUser;

	}

	@RequestMapping(value = "/signIn", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public User signIn(@RequestBody User user) {
		String tempEmail = user.getEmail();
		String tempPassword = user.getPassword();

		User tempUser = userRespository.findByEmail(tempEmail);

		System.out.println("tempUser:" + tempEmail);
		System.out.println("tempUser:" + tempUser.getEmail());
		System.out.println("tempUser:" + tempUser.getPassword());
		System.out.println("tempUser:" + tempPassword );
		
		if (tempUser != null) {
			if (tempUser.getPassword().equals(tempPassword)) {
				return tempUser;
			}
		}
		return null;

	}

	@RequestMapping(value = "/{user_id}/getProfile", method = RequestMethod.GET, consumes = "application/json", produces = "application/json")
	public LinkedHashMap getProfile(@PathVariable("user_id") long user_id) {
		// System.out.print("inGetProfile");
		User tempUser = userRespository.findById(user_id);
		LinkedHashMap tempMap = null;
		if (tempUser != null) {
			tempMap = new LinkedHashMap();
			tempMap.put("id", tempUser.getUserId());
			tempMap.put("firstName", tempUser.getFirstName());
			tempMap.put("lastName", tempUser.getLastName());
			tempMap.put("email", tempUser.getEmail());
			tempMap.put("dob", tempUser.getDob());
			tempMap.put("phoneNo", tempUser.getPhoneNo());
			tempMap.put("cardDetails", tempUser.getCardDetails());
			tempMap.put("userCreatedDate", tempUser.getUserCreatedDate());
			tempMap.put("userEditedDate", tempUser.getUserEditedDate());
			return tempMap;
		} else {
			return null;
		}

	}

	@RequestMapping(value = "/{user_id}/listAllVMs", method = RequestMethod.GET, consumes = "application/json", produces = "application/json")
	public ArrayList<VM> getVMData(@PathVariable("user_id") long user_id) {

		User tempUser = userRespository.findById(user_id);
		if (tempUser != null) {
			return tempUser.getvMachine();
		} else {
			return null;
		}

	}

	// ******************************************************************************************************//
	@RequestMapping(value = "/{user_id}/vm/create", method = RequestMethod.POST, produces = "application/json")
	public String createVM(@PathVariable("user_id") long user_id,
			@RequestBody VM tempVM) throws Exception {
		
		List<VM> myNewList=vmRespository.findAll();
		
		//System.out.println("myList.size()"+myList.size());
		
		if(vmRespository.findById(myNewList.size())!=null){
			vmId=(vmRespository.findById(myNewList.size()).getVmIdCounter())+1;
		}else{
			vmId = 1;//counter.incrementAndGet();
		}
		
		VM vmachine;
		User tempUser=userRespository.findById(user_id);
		System.out.println("vmIdvmIdvmId---"+vmId);
		vmachine = tempVM;
		//vmachine = new VM();
		//vmId = counter2.incrementAndGet();
		//vmachine.setTemplate("T16-Template-256MB-Ubu");
		System.out.println("Teamplate id is" + tempVM.getTemplate());
		if(tempVM.getTemplate().equals("1"))
		{
			vmachine.setVmCPU("2");
			vmachine.setVmDisk("20");
			vmachine.setVmOS("ubuntu");
			vmachine.setVmRAM("256");
			vmachine.setTemplate("T16-Template-256MB-Ubu");
		}
		else if(vmachine.getTemplate().equals("2"))
		{
			vmachine.setVmCPU("2");
			vmachine.setVmDisk("20");
			vmachine.setVmOS("ubuntu");
			vmachine.setVmRAM("512");
			vmachine.setTemplate("T16-Template-256MB-Ubu");
		}
		else if(vmachine.getTemplate().equals("3"))
		{
			vmachine.setVmCPU("2");
			vmachine.setVmDisk("20");
			vmachine.setVmOS("windows");
			vmachine.setVmRAM("1024");
			vmachine.setTemplate("T16-Template-1024MB-Win");
		}

	    
	    String vmName=user_id +"-"+ vmId + "-OS-" + vmachine.getVmOS();
	    System.out.println(vmName);
		ServiceInstance si = null;
		try {
			URL url = new URL(operations.geturl());
			si = new ServiceInstance(url, "administrator", "12!@qwQW", true);
			Folder rootFolder = si.getRootFolder();

			String clonedName = user_id +"-"+ vmId+"-OS-" + vmachine.getVmOS();
			System.out.println("rootFolder"+rootFolder);
			VirtualMachine vm = (VirtualMachine) new InventoryNavigator(
					rootFolder).searchManagedEntity("VirtualMachine",vmachine.getTemplate());
			
			System.out.println("VirtualMachine"+vm);
			Datacenter dc = (Datacenter) si.getSearchIndex()
					.findByInventoryPath("T16-DC");
			System.out.println("Datacenter"+dc);
			ManagedEntity[] rpool = new InventoryNavigator(rootFolder)
					.searchManagedEntities("ResourcePool");

			if (vm == null) {
				System.out
						.println("VirtualMachine path is NOT correct. Pls double check. ");
				return "VirtualMachine path is NOT correct. Pls double check. 1";
			}
			if (dc == null) {
				System.out
						.println("Datacenter path is NOT correct. Pls double check. ");
				return "Datacenter path is NOT correct. Pls double check.1";
			}
			Folder vmFolder = dc.getVmFolder();

			VirtualMachineCloneSpec cloneSpec = new VirtualMachineCloneSpec();
			VirtualMachineRelocateSpec vLocation = new VirtualMachineRelocateSpec();
			vLocation.setPool(rpool[0].getMOR());
			cloneSpec.setLocation(vLocation);
			// ComputeResource cr = (ComputeResource)vm.getParent();
			//
			// cloneSpec.location.pool = vm.getResourcePool().getMOR();
			cloneSpec.setPowerOn(false);
			cloneSpec.setTemplate(false);

		      System.out.println("VM Created Sucessfully"+tempVM.getVmCPU());
		      vmachine.setId(vmId);
		      vmachine.setUserId(user_id);
		      vmachine.setVmIdCounter(vmId);
		      vmachine.setVmName(clonedName);
		      vmachine.setVmDisk(tempVM.getVmDisk());
		      vmachine.setVmCPU(tempVM.getVmCPU());
		      vmachine.setVmOS(tempVM.getVmOS());
		      vmachine.setVmRAM(tempVM.getVmRAM());
		      vmachine.setVmStatus("Initiating");
		      tempUser.setvMachine(vmachine);
		      userRespository.save(tempUser);
		      vmRespository.save(vmachine);
		      
		      System.out.print(tempUser.getvMachine().size());
		      
		      
			Task task = vm.cloneVM_Task(vmFolder, clonedName, cloneSpec);
			//String result=task.waitForTask();
			
			 //if(result == Task.SUCCESS) 
			   // {
			    //}
			    //else 
			    //{
			      //System.out.println("VM could not be created");
			    //  return "VM could not be created";
			    //}
			return "VM Created Sucessfully";
			

		} catch (Exception ex) {
			ex.printStackTrace();
			return null;

		}
	}

	@RequestMapping(value = "/{user_id}/vm/{vm_id}/stop", method = RequestMethod.POST, produces = "application/json")
	public String stopVM(@PathVariable("user_id") long user_id,
			@PathVariable("vm_id") long vm_id, @RequestBody VM tempVM)
			throws Exception {
		
		VM tempNewVm=vmRespository.findById(vm_id);
		VirtualMachine tempNewVm2 =(VirtualMachine) new InventoryNavigator(
				rootFolder).searchManagedEntity("VirtualMachine",tempNewVm.getVmName()); 
		
		System.out.print("tempNewVm2"+tempNewVm2);
		
		System.out.println(tempNewVm2.getName()+":::::"+tempNewVm.getVmName());
		Task task=tempNewVm2.powerOffVM_Task();
		String result = "Success"; 
		if(result == Task.SUCCESS) 
	    {
			tempNewVm.setVmStatus("Stopped");
			return "Virtual Machine powered off successfully";
	    }else{
	    	return "Could not power off Virtual Machine";
	    }
		 /* for (int i=0;i<vms.length;i++)
		  {
			  System.out.println(vms[i].getName()+":::::"+tempNewVm.getVmName());
			  if(vms[i].getName().equals(tempVM.getVmName()))
			  {
				  vms[i].destroy_Task();
			  }
		  }*/
		/*VM vmachine;
		vmachine = tempVM;
		ServiceInstance si = null;
		try {
			URL url = new URL(operations.geturl());
			si = new ServiceInstance(url, "administrator", "12!@qwQW", true);
			Folder rootFolder = si.getRootFolder();

			// String clonedName = vmachine.getUID() + "-OS-" +
			// vmachine.getOS();

			VirtualMachine vm = (VirtualMachine) new InventoryNavigator(
					rootFolder).searchManagedEntity("VirtualMachine",
					vmachine.getTemplate());
			Datacenter dc = (Datacenter) si.getSearchIndex()
					.findByInventoryPath("T16-DC");

			ManagedEntity[] rpool = new InventoryNavigator(rootFolder)
					.searchManagedEntities("ResourcePool");

			if (vm == null) {
				System.out
						.println("VirtualMachine path is NOT correct. Pls double check. ");
				return "VirtualMachine path is NOT correct. Pls double check. ";
			}
			if (dc == null) {
				System.out
						.println("Datacenter path is NOT correct. Pls double check. ");
				return "Datacenter path is NOT correct. Pls double check. ";
			}
			Folder vmFolder = dc.getVmFolder();

			vm.powerOnVM_Task(null);

			// Task task = vm.cloneVM_Task(vmFolder, clonedName, cloneSpec);

		} catch (Exception ex) {
			ex.printStackTrace();
			return null;

		}

		return null;*/
	}
	@RequestMapping(value = "/{user_id}/vm/{vm_id}/vm/getState", method = RequestMethod.POST, produces = "application/json")
	public String stVM(@PathVariable("user_id") long user_id,
			@PathVariable("vm_id") long vm_id, @RequestBody VM tempVM) throws Exception {
		System.out.println("This is VM name: " + user_id);
		
		User user=userRespository.findById(user_id);
		VM tempNewVm=vmRespository.findById(vm_id);
		System.out.println("This is VM name:" + tempNewVm.getVmName());
		String vmName=tempNewVm.getVmName();
		
		for(int i=0;i<user.getvMachine().size();i++){
			System.out.println("myTmpVM.getId()"+user.getvMachine().get(i)+" "+tempNewVm.getId());
			if(user.getvMachine().get(i).getId()==tempNewVm.getId()){
				System.out.println("myTmpVMmyTmpVMtrue ");
				//tempIndex=i;//user.getvMachine().indexOf(tempNewVm);
				System.out.println("VM name ----------> " + vmName);
				VirtualMachine tempNewVm1 =(VirtualMachine) new InventoryNavigator(
						rootFolder).searchManagedEntity("VirtualMachine",vmName); 
				
				System.out.println(tempNewVm1.getRuntime().powerState.poweredOff);
				return tempNewVm1.getRuntime().powerState.toString();

			}
		}
		return "Unable to find VM";
		
			
	}
	
	@RequestMapping(value = "/{user_id}/vm/{vm_id}/vm/getip", method = RequestMethod.POST, produces = "application/json")
	public String stIP(@PathVariable("user_id") long user_id,
			@PathVariable("vm_id") long vm_id, @RequestBody VM tempVM) throws Exception {
		
		System.out.println("This is VM name: " + user_id);
		
		User user=userRespository.findById(user_id);

		
		VM tempNewVM=vmRespository.findById(vm_id);
		String vmName=tempNewVM.getVmName();
		System.out.println("VM name ----------> " + vmName);
		VirtualMachine tempNewVm1 =(VirtualMachine) new InventoryNavigator(
				rootFolder).searchManagedEntity("VirtualMachine",vmName); 
		
		System.out.println(tempNewVm1.getGuest().getIpAddress());
		return tempNewVm1.getGuest().getIpAddress();
	
	}
	

	@RequestMapping(value = "/{user_id}/vm/{vm_id}/vm/start", method = RequestMethod.POST, produces = "application/json")
	public String startVM(@PathVariable("user_id") long user_id,
			@PathVariable("vm_id") long vm_id, @RequestBody VM tempVM) throws Exception {
		
		VM tempNewVm=vmRespository.findById(vm_id);
		VirtualMachine tempNewVm2 =(VirtualMachine) new InventoryNavigator(
				rootFolder).searchManagedEntity("VirtualMachine",tempNewVm.getVmName()); 
		
		System.out.print("tempNewVm2"+vm_id);
		
		System.out.println(tempNewVm2.getName()+":::::"+tempNewVm.getVmName());
		Task task=tempNewVm2.powerOnVM_Task(null);
		String result = "Success"; 
		if(result.equals("Success")) 
	    {
			tempNewVm.setVmStatus("Started");
			return "Virtual Machine powered off successfully";
	    }else{
	    	return "Could not power off Virtual Machine";
	    }
	}

	@RequestMapping(value = "/{user_id}/vm/{vm_id}/terminate", method = RequestMethod.POST, produces = "application/json")
	public String terminateVM(@PathVariable("user_id") long user_id,
			@PathVariable("vm_id") long vm_id, @RequestBody VM tempVM)
			throws Exception {
		
		User user=userRespository.findById(user_id);
		VM tempNewVm=vmRespository.findById(vm_id);
		VirtualMachine tempNewVm2 =(VirtualMachine) new InventoryNavigator(
				rootFolder).searchManagedEntity("VirtualMachine",tempNewVm.getVmName()); 
		
		System.out.print("tempNewVm2"+tempNewVm2);
		
		//System.out.println(tempNewVm2.getName()+":::::"+tempNewVm.getVmName());
		
		Task task=tempNewVm2.destroy_Task();
		//String result = task.waitForMe(); 
		//if(result == Task.SUCCESS) 
	    //{
		System.out.print("user.getvMachine()"+user.getvMachine().size());
			for(int i=0;i<user.getvMachine().size();i++){
				System.out.println("myTmpVM.getId()"+user.getvMachine().get(i)+" "+tempNewVm.getId());
				if(user.getvMachine().get(i).getId()==tempNewVm.getId()){
					System.out.println("myTmpVMmyTmpVMtrue ");
					tempIndex=i;//user.getvMachine().indexOf(tempNewVm);
				}
			}
			System.out.print("tempIndex"+tempIndex);
			user.getvMachine().remove(tempIndex);
			userRespository.save(user);
			vmRespository.delete(tempNewVm);
			return "Virtual Machine terminated successfully";
	    //}else{
	    	//return "Could not terminate Virtual Machine";
	    //}
	}
	
	@RequestMapping(value = "/{user_id}/vm/{vm_id}/vm/getstats", method = RequestMethod.POST, produces = "application/json")
	public ArrayList<ArrayList<String>> getStats(@PathVariable("user_id") long user_id , @PathVariable("vm_id") long vm_id) {
		

		VM tempVM = vmRespository.findById(vm_id);
		
		Statistics stat = new Statistics();
		try {
			ArrayList<ArrayList<String>> statInfo = new ArrayList<ArrayList<String>>();	
		//	ArrayList<String> statInfo1 = new ArrayList<String>();
			statInfo = stat.getStatistics(vCenterURL, "administrator", "12!@qwQW", tempVM.getVmName());
//			for(String s:statInfo1){
			System.out.println(">>>>>>>>>>> "+statInfo);
//			}
			return statInfo;
		
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	

}
