/**
 * 
 */
package com.ase0401.msfsdemo.management;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ase0401.msfsdemo.factory.ModelFactory;
import com.ase0401.msfsdemo.repository.UserRepository;

import msfs_0401.Expense;
import msfs_0401.FinancialEntry;
import msfs_0401.IncomeStatement;
import msfs_0401.MicroFarmer;
import msfs_0401.OperationalCost;
import msfs_0401.Revenue;
import msfs_0401.User;

import java.util.*;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.text.ParseException;


/**
 * @author Stela & Eiman
 *
 */
@Component


public class FinancialManagement {

	@Autowired
	private ModelFactory factory;
	
	private static IncomeStatement incomeStatement = null;
	
	private static int userId;
	
	private UserRepository repo;
	
	private boolean isWithinRange(Date testDate, Date startDate, Date endDate ) {
		 return !(testDate.before(startDate) || testDate.after(endDate));
		};
		
	private Date parseDate(String date) {
		try {
				return new SimpleDateFormat("yyyy-MM-dd").parse(date);
			}
	    catch (ParseException e) {
		   return null;
	 }
	} 	
	
	private double getRevenueSumBetween(Date from, Date to) {
	ArrayList<Revenue> allRevenues = getRevenuesOfUser();
	double sum = 0.0;
	for (Iterator<Revenue> iterator = allRevenues.iterator(); iterator.hasNext();) {
	Revenue revenue = (Revenue) iterator.next();
	if(isWithinRange(revenue.getDate(), from, to)){
	sum += revenue.getAmount();
			}
		}
	return sum;
	}
	
	private double getExpensesSumBetween(Date from, Date to) {
	ArrayList<Expense> allExpenses = getExpensesOfUser();
	double sum = 0.0;
	for (Iterator<Expense> iterator = allExpenses.iterator(); iterator.hasNext();) {
	Expense expense = (Expense) iterator.next();
	if(isWithinRange(expense.getDate(), from, to)){
	sum += expense.getAmount();
			}
		}
	return sum;
	}
	
	private double getOperationalCostsSumBetween(Date from, Date to) {
	ArrayList<OperationalCost> allOperationalCosts = getOperationalCostsOfUser(userId);
	double sum = 0.0;
	for (Iterator<OperationalCost> iterator = allOperationalCosts.iterator(); iterator.hasNext();) {
	OperationalCost cost = (OperationalCost) iterator.next();
	if(isWithinRange(cost.getDate(), from, to)){
	sum += cost.getAmount();
			}
		}
	return sum;
	}
	
	public void setUpUserFinancesById(int id, UserRepository repository) {
		repo = repository;
		userId = id;
		incomeStatement = getIncomeStatementByUserId();
		if(factory == null ) factory = new ModelFactory();
		if(incomeStatement == null) {
			incomeStatement = factory.createIncomeStatement();
			addIncomeStatementToUser();
		}		
	}
	
	public IncomeStatement getIncomeStatementByUserId() {
		System.out.println("User repo: " + repo);
		User user = repo.getUserById(userId);
		if(user != null && user instanceof MicroFarmer) {
			MicroFarmer farmer = (MicroFarmer) user;
			return farmer.getIncomeStatement();
		}
		return null;
	}
	
	public void addIncomeStatementToUser() {
		User user = repo.getUserById(userId);
		if(user != null && user instanceof MicroFarmer) {
			MicroFarmer farmer = (MicroFarmer) user;
			farmer.setIncomeStatement(incomeStatement);
			repo.saveData(farmer);
		}
	}
	
	public ArrayList<FinancialEntry> getAllFinancesOfUser(){
		ArrayList<FinancialEntry> list = new ArrayList<>();
		if(incomeStatement != null) {
			for (Iterator<FinancialEntry> iterator = incomeStatement.getFinancialEntries().iterator(); iterator.hasNext();) {
				FinancialEntry financialEntry = (FinancialEntry) iterator.next();
				list.add(financialEntry);
			}
		}
		return list;
	}
	
	public ArrayList<Revenue> getRevenuesOfUser(){
		ArrayList<Revenue> list = new ArrayList<>();
		if(incomeStatement != null) {
			for (Iterator<FinancialEntry> iterator = incomeStatement.getFinancialEntries().iterator(); iterator.hasNext();) {
				FinancialEntry financialEntry = (FinancialEntry) iterator.next();
				if(financialEntry instanceof Revenue) {
					Revenue revenue = (Revenue) financialEntry;
					list.add(revenue);
				}
			}
		}
		return list;
	}
	
	public ArrayList<Expense> getExpensesOfUser(){
		ArrayList<Expense> list = new ArrayList<>();
		if(incomeStatement != null) {
			for (Iterator<FinancialEntry> iterator = incomeStatement.getFinancialEntries().iterator(); iterator.hasNext();) {
				FinancialEntry financialEntry = (FinancialEntry) iterator.next();
				if(financialEntry instanceof Expense) {
					Expense expense = (Expense) financialEntry;
					list.add(expense);
				}
			}
		}
		return list;
	}
	
	public ArrayList<OperationalCost> getOperationalCostsOfUser(int userId){
		ArrayList<OperationalCost> list = new ArrayList<>();
		if(incomeStatement != null) {
			for (Iterator<FinancialEntry> iterator = incomeStatement.getFinancialEntries().iterator(); iterator.hasNext();) {
				FinancialEntry financialEntry = (FinancialEntry) iterator.next();
				if(financialEntry instanceof OperationalCost) {
					OperationalCost cost = (OperationalCost) financialEntry;
					list.add(cost);
				}
			}
		}
		return list;
	}
	
	public void addFinancialEntry(String type, Double amount, String description, String currency, Date date) {
		FinancialEntry entry = factory.createFinancialEntry(type, amount, description, currency, date);
		incomeStatement.getFinancialEntries().add(entry);
		MicroFarmer farmer = (MicroFarmer) repo.getUserById(userId);
		farmer.setIncomeStatement(incomeStatement);
		repo.saveData(farmer);
	}
	
	public ArrayList<Double> getMonthlyRevenuesOfUser(){
		ArrayList<Double> resultList = new ArrayList<>();
		ArrayList<String[]> dateList = new ArrayList<>();
		
		String[] jan = {"2020-01-01", "2020-01-31"};
		String[] feb = {"2020-02-01", "2020-02-29"};
		String[] mar = {"2020-03-01", "2020-03-31"};
		String[] apr = {"2020-04-01", "2020-04-30"};
		String[] may = {"2020-05-01", "2020-05-31"};
		String[] jun = {"2020-06-01", "2020-06-30"};
		String[] jul = {"2020-07-01", "2020-07-31"};
		String[] aug = {"2020-08-01", "2020-08-31"};
		String[] sep = {"2020-09-01", "2020-09-30"};
		String[] oct = {"2020-10-01", "2020-10-31"};
		String[] nov = {"2020-11-01", "2020-11-30"};
		String[] dec = {"2020-12-01", "2020-12-31"};
		
		dateList.add(jan);
		dateList.add(feb);
		dateList.add(mar);
		dateList.add(apr);
		dateList.add(may);
		dateList.add(jun);
		dateList.add(jul);
		dateList.add(aug);
		dateList.add(sep);
		dateList.add(oct);
		dateList.add(nov);
		dateList.add(dec);
		
		for (Iterator<String[]> iterator = dateList.iterator(); iterator.hasNext();) {
			String[] month = (String[]) iterator.next();
			resultList.add(getRevenueSumBetween(parseDate(month[0]),
			parseDate(month[1])));
			}
		return resultList;
	}
	
	public ArrayList<Double> getMonthlyExpensesOfUser(){
		ArrayList<Double> resultList = new ArrayList<>();
		ArrayList<String[]> dateList = new ArrayList<>();
		
		String[] jan = {"2020-01-01", "2020-01-31"};
		String[] feb = {"2020-02-01", "2020-02-29"};
		String[] mar = {"2020-03-01", "2020-03-31"};
		String[] apr = {"2020-04-01", "2020-04-30"};
		String[] may = {"2020-05-01", "2020-05-31"};
		String[] jun = {"2020-06-01", "2020-06-30"};
		String[] jul = {"2020-07-01", "2020-07-31"};
		String[] aug = {"2020-08-01", "2020-08-31"};
		String[] sep = {"2020-09-01", "2020-09-30"};
		String[] oct = {"2020-10-01", "2020-10-31"};
		String[] nov = {"2020-11-01", "2020-11-30"};
		String[] dec = {"2020-12-01", "2020-12-31"};
		
		dateList.add(jan);
		dateList.add(feb);
		dateList.add(mar);
		dateList.add(apr);
		dateList.add(may);
		dateList.add(jun);
		dateList.add(jul);
		dateList.add(aug);
		dateList.add(sep);
		dateList.add(oct);
		dateList.add(nov);
		dateList.add(dec);
		
		for (Iterator<String[]> iterator = dateList.iterator(); iterator.hasNext();) {
			String[] month = (String[]) iterator.next();
			resultList.add(getExpensesSumBetween(parseDate(month[0]),
			parseDate(month[1])));
			}
		return resultList;
	}
	
	public ArrayList<Double> getMonthlyOperationalCostsOfUser(){
		ArrayList<Double> resultList = new ArrayList<>();
		ArrayList<String[]> dateList = new ArrayList<>();
		
		String[] jan = {"2020-01-01", "2020-01-31"};
		String[] feb = {"2020-02-01", "2020-02-29"};
		String[] mar = {"2020-03-01", "2020-03-31"};
		String[] apr = {"2020-04-01", "2020-04-30"};
		String[] may = {"2020-05-01", "2020-05-31"};
		String[] jun = {"2020-06-01", "2020-06-30"};
		String[] jul = {"2020-07-01", "2020-07-31"};
		String[] aug = {"2020-08-01", "2020-08-31"};
		String[] sep = {"2020-09-01", "2020-09-30"};
		String[] oct = {"2020-10-01", "2020-10-31"};
		String[] nov = {"2020-11-01", "2020-11-30"};
		String[] dec = {"2020-12-01", "2020-12-31"};
		
		dateList.add(jan);
		dateList.add(feb);
		dateList.add(mar);
		dateList.add(apr);
		dateList.add(may);
		dateList.add(jun);
		dateList.add(jul);
		dateList.add(aug);
		dateList.add(sep);
		dateList.add(oct);
		dateList.add(nov);
		dateList.add(dec);
		
		for (Iterator<String[]> iterator = dateList.iterator(); iterator.hasNext();) {
			String[] month = (String[]) iterator.next();
			resultList.add(getOperationalCostsSumBetween(parseDate(month[0]),
			parseDate(month[1])));
			}
		return resultList;
	}
	
	public ArrayList<Double> getMonthlyProfitsOfUser(){
		ArrayList<Double> resultList = new ArrayList<>();
		ArrayList<Double> monthlyRevenues = getMonthlyRevenuesOfUser();
		ArrayList<Double> monthlyExpenses = getMonthlyExpensesOfUser(); 
		ArrayList<Double> monthlyOperationalCosts = getMonthlyOperationalCostsOfUser(); 
		
		for (int i = 0; i < 12; i++) {
			resultList.add(monthlyRevenues.get(i) - monthlyExpenses.get(i) - monthlyOperationalCosts.get(i));
		}
		return resultList;
	}
	
	public String getDataPoints() {
		Gson gsonObj = new Gson();
		Map<Object,Object> map = null;
		List<Map<Object,Object>> list = new ArrayList<Map<Object,Object>>();
		ArrayList<Double> monthlyProfits = getMonthlyProfitsOfUser();
		for (int i = 0; i < 12; i++) {
			map = new HashMap<Object,Object>();
			map.put("label", "M"+(i+1));
			map.put("y", monthlyProfits.get(i));
			list.add(map);
		}
		return gsonObj.toJson(list);
	}
	
	
}

