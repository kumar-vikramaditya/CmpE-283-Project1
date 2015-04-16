package demo;

import java.io.IOException;
import java.net.InetAddress;

public class operations {
	
	public static byte[] convertIpToByteArray(String strIp)
	{
		byte[] addr = new byte[4];
		String[] strArr = strIp.split(".");
		for(int i=0;i<strArr.length;i++)
		{
			addr[i] = Byte.parseByte(strArr[i]);
		}
		return addr;
	}
	
	public static boolean pingMachine(byte[] arrip) throws IOException
	{
		InetAddress inet = InetAddress.getByAddress(arrip);
		System.out.println("Sending Ping Request to " + inet);
		return inet.isReachable(5000);

	}
	
	public static boolean pingmachine(String strip) throws IOException, InterruptedException
	{
		/*Process p1 = java.lang.Runtime.getRuntime().exec("ping -c 1 130.65.133.48");
		int returnVal = p1.waitFor();
		boolean reachable = (returnVal==0);
		return reachable;*/
		return  pingMachine(convertIpToByteArray(strip));
	}
	public static String geturl()
	{
		//College Address
		return "https://130.65.132.116/sdk";
		//Home address
		//return "https://10.0.0.26/sdk";
	}
	
	public static String getTemplateName(){
		
		return "T16-Template-256MB-Ubu";
	}
	

}
