package com.brainmentors.los.start;

import com.brainmentors.los.operation.LOSProcess;
import com.brainmentors.los.utilis.Utility;


public class Application {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		LOSProcess process=new LOSProcess();
		while(true) {
		System.out.println(" DO U HAVE ANY ApplicationNumber OR NOT (enter 0) or -1 to Exit ");
		int applicationNumber=Utility.scanner.nextInt();
		if (applicationNumber==-1)
		{
			System.out.println("thanks for using");
			System.exit(0);
		}
		if (applicationNumber==0)
		{
			//new customer
			process.sourcing();
		}
		else {
			//Existing Customer
			process.checkStage(applicationNumber);
		}
	
	}
	}
}
