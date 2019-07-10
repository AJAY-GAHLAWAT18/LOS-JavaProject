package com.brainmentors.los.utilis;

import java.util.Scanner;

public class Utility implements stageConstants {
	public static int serialCounter=1;
	private Utility() {}
	public static Scanner scanner=new Scanner(System.in);
	public static String  getStageName(int stageId) {
		switch(stageId)
		{
		case SOURCING:
			return "sourcing stage";
		case QDE:
			return "Quick Data Entry stage";
			case DEDUPE:
				return "dedupe stage";
			case APPROVAL:
				return "approval stage";
			case REJECT:
				return "rejection stage";
			default :
				return "invalid stage";
		}
	}
}
