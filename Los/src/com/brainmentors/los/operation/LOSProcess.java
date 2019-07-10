package com.brainmentors.los.operation;

import com.brainmentors.los.customer.Customer;
import com.brainmentors.los.customer.LoanDetails;
import com.brainmentors.los.customer.PersonalInformation;
import com.brainmentors.los.utilis.CommonConstants;
import com.brainmentors.los.utilis.LoanConstants;
import com.brainmentors.los.utilis.Utility;
import com.brainmentors.los.utilis.stageConstants;

import static com.brainmentors.los.utilis.Utility.scanner;
import static com.brainmentors.los.utilis.Utility.serialCounter;

import java.util.ArrayList;

public class LOSProcess implements stageConstants,CommonConstants{
	//private Customer customers[]=new Customer[100];                 //static
	private ArrayList<Customer> customers = new ArrayList<>();
	
	//run time grow kr sakta h ///only store type customer
	public void approval(Customer customer) {
		customer.setStage(APPROVAL);
		int score=customer.getLoanDetails().getScore();
		System.out.println("id "+customer.getId());
		System.out.println("Nmae is "+customer.getPersonal().getFirstName()+" "+customer.getPersonal().getLastName());
		System.out.println("score is "+customer.getLoanDetails().getScore());
		System.out.println("loan "+customer.getLoanDetails().getType()+" Amount "
						+customer.getLoanDetails().getAmount()+" duration "
						+customer.getLoanDetails().getDuration());
		double approvedAmount=customer.getLoanDetails().getAmount()*(score/100);
		System.out.println("loan approved amount is"+approvedAmount);
		System.out.println("do u want to Bring this loan or Not ");
		char choice=scanner.next().toUpperCase().charAt(0);
		if(choice==NO) {
			customer.setStage(REJECT);
			customer.setRemarks("customer denied the approved Amount" +" "+approvedAmount);
			return;
		}
		else {
			showEMI(customer);
		}
	}
	private void showEMI(Customer customer) {
		// TODO Auto-generated method stub
		System.out.println("EMI is ");
		if(customer.getLoanDetails().getType().equalsIgnoreCase(LoanConstants.HOME_LOAN)) {
			customer.getLoanDetails().setRoi(LoanConstants.HOME_LOAN_ROI);
		}
		if(customer.getLoanDetails().getType().equalsIgnoreCase(LoanConstants.AUTO_LOAN)) {
			customer.getLoanDetails().setRoi(LoanConstants.AUTO_LOAN_ROI);
		}
		if(customer.getLoanDetails().getType().equalsIgnoreCase(LoanConstants.PERSONAL_LOAN)) {
			customer.getLoanDetails().setRoi(LoanConstants.PERSONAL_LOAN_ROI);
		}
		double perMonthPrincipal = customer.getLoanDetails().getAmount() / customer.getLoanDetails().getDuration();
		double interest =perMonthPrincipal*customer.getLoanDetails().getRoi();
		double totalEmi =perMonthPrincipal+interest;
		System.out.println("your emi is "+totalEmi);
		
		
	}
	public void qde(Customer customer ) {
		customer.setStage(QDE);
		System.out.println("Application Number "+customer.getId());
		System.out.println("Name "+customer.getPersonal().getFirstName()+" "+customer.getPersonal().getLastName());
		System.out.println("You applied for a "+customer.getLoanDetails().getType()+" Duration "+customer.getLoanDetails().getDuration()+
				" Amount "+customer.getLoanDetails().getAmount());
		System.out.println("enter the pan card number ");
		String panCard=scanner.next();
		System.out.println("enter the voterId ");
		String voterId=scanner.next();
		System.out.println("enter the Income ");
		double income=scanner.nextDouble();
		System.out.println("enter the Liability ");
		double liability =scanner.nextDouble();
		System.out.println("Enter the phone ");
		String phone=scanner.next();
		System.out.println("enter the Email ");
		String email=scanner.next();
		customer.getPersonal().setVoterId(voterId);
		customer.getPersonal().setPanCard(panCard);
		customer.getPersonal().setPhone(phone);
		customer.getPersonal().setEmail(email);
		//customer.getPersonal().setVoterId(voterId);
		customer.setIncome(income);
		customer.setLiability(liability);
	}
	
	public void moveToNextStage(Customer customer) {
	while(true){
		if(customer.getStage()==SOURCING)
		{
			System.out.println(" WANT TO MOVE TO NEXT STAGE Y/N ");
			char choice=scanner.next().toUpperCase().charAt(0);
			if(choice==YES) {
				qde(customer);
			}
			else {
				return;
			}
		}
		else if(customer.getStage()==QDE)
		{
		System.out.println("QDE WANT TO MOVE TO NEXT STAGE Y/N ");
		char choice=scanner.next().toUpperCase().charAt(0);
		if(choice==YES) {
			dedupe(customer);
		}
		else {
			return;
		}
	}
	else if(customer.getStage()==DEDUPE)
	{
		System.out.println("DEDUPE WANT TO MOVE TO NEXT STAGE Y/N ");
		char choice=scanner.next().toUpperCase().charAt(0);
		if(choice==YES) {
			scoring(customer);
		}
		else {
			return;
		}
	}
	else if(customer.getStage()==SCORING)
	{
		System.out.println(" Scoring WANT TO MOVE TO NEXT STAGE Y/N ");
		char choice=scanner.next().toUpperCase().charAt(0);
		if(choice==YES) {
			approval(customer);
		}
		else {
			return;
		}
	}
	}
	}

public void scoring(Customer customer) {
	customer.setStage(SCORING);
	int score=0;
	double totalIncome=customer.getIncome()-customer.getLiability();
	if(customer.getPersonal().getAge()>=21 && customer.getPersonal().getAge()<=35) {
		score+=10;
	}
		if(totalIncome>=200000) {
			score+=50;
	}
		customer.getLoanDetails().setScore(score);
}
	
	public void dedupe(Customer customer) {
		//System.out.println("Inside Dedupe ");
		customer.setStage(DEDUPE);
		boolean isNegativefound=false;
		for(Customer negativeCustomer : DB.getNegativeCustomers()) {
			int negativeScore=isNegative(customer, negativeCustomer);
			if(negativeScore>0) {
				System.out.println("customer record is found in Dedupe and score is "+negativeScore);
				isNegativefound=true;
				break;
			}
		}
		if(isNegativefound) {
			System.out.println("do u want to proceed this loan "+customer.getId());
			char choice=scanner.next().toUpperCase().charAt(0);
			if(choice==NO) {
				customer.setRemarks("loan is rejected due to high score in dedupe check");
				customer.setStage(REJECT);
				return;
			}
		}
	}
	private int isNegative(Customer customer,Customer negative)
	{
		int percentageMatch=0;
		if(customer.getPersonal().getPhone().equals(negative.getPersonal().getPhone())) {
			percentageMatch+=10;
		}
		if(customer.getPersonal().getVoterId().equals(negative.getPersonal().getVoterId())) {
			percentageMatch+=10;
		}
		if(customer.getPersonal().getEmail().equals(negative.getPersonal().getEmail())) {
			percentageMatch+=10;
		}
		if(customer.getPersonal().getPanCard().equals(negative.getPersonal().getPanCard())) {
			percentageMatch+=10;
		}
		if(customer.getPersonal().getAge()==negative.getPersonal().getAge() && 
			customer.getPersonal().getFirstName().equalsIgnoreCase(negative.getPersonal().getFirstName())){
			percentageMatch+=20;
		}
		return percentageMatch;
	}
	public void sourcing() {
		Customer customer=new Customer();
		customer.setId(serialCounter);
		customer.setStage(SOURCING);
		System.out.println("Enter tHE First Name ");
		String firstName=scanner.next();
		System.out.println("Enter tHE Last Name ");
		String lastName=scanner.next();
		System.out.println("Enter tHE Age ");
		int age=scanner.nextInt();
		System.out.println(" enter the Loan type HL, AL, PL ");
		String type=scanner.next();
		System.out.println("enter the amount ");
		double amount=scanner.nextDouble();
		System.out.println("Duration of Loan");
		double duration= scanner.nextDouble();
		PersonalInformation pd=new PersonalInformation();
		pd.setFirstName(firstName);
		pd.setLastName(lastName);
		pd.setAge(age);
		customer.setPersonal(pd);
		LoanDetails loanDetails = new LoanDetails();
		loanDetails.setType(type);
		loanDetails.setAmount(amount);
		loanDetails.setDuration(duration);
		customer.setLoanDetails(loanDetails);
		customers.add(customer);    //array list m daal diya
		serialCounter++;
		System.out.println("sourcing done.....");
			
	
	
	}

	public void checkStage(int applicationNumber)
	
	{
		boolean isStageFound=false;
		if(customers!=null && customers.size()>0) {
		for(Customer customer: customers)
		{
			if(customer.getId()==applicationNumber) {
					System.out.println("you are on "+Utility.getStageName(customer.getStage()));
					 isStageFound = true;
					 moveToNextStage(customer);
					break;
		}
	}

		}
	if(!isStageFound) {
		System.out.println("invalid Application number");
	}
}
}