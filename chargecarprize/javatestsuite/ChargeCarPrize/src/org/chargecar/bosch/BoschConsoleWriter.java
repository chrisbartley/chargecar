package org.chargecar.bosch;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.chargecar.prize.battery.BatteryModel;
import org.chargecar.prize.util.DriverResults;
import org.chargecar.prize.util.SimulationResults;
import org.chargecar.prize.util.Trip;
import org.chargecar.prize.visualization.Visualizer;

/**
 * DO NOT EDIT
 * 
 * A simple visualizer that outputs simulation details to the console screen.
 * 
 * @author Alex Styler
 */
public class BoschConsoleWriter implements Visualizer {
    DecimalFormat d = new DecimalFormat("0.000E0");
    DecimalFormat p = new DecimalFormat("0.000%");
    
    public void visualizeSummary(List<SimulationResults> results) {
	int tripsTested = 0;
	List<Double> currentSquaredSums = new ArrayList<Double>();
	List<Double> chargeSpentSums = new ArrayList<Double>();
	for (SimulationResults r : results) {
	    tripsTested = r.getTripStrings().size();
	    double currentSquaredSum = 0.0;
	    for (Double d : r.getBatteryCurrentSquaredIntegrals()) {
		currentSquaredSum += d;
	    }
	    currentSquaredSums.add(currentSquaredSum);
	    double chargeSpentSum = 0.0;
	    System.out.println(r.getPolicyName() +", charge spent: ");
			//	+ ", charge spent: " + d.format(baseChargeSpentSum));
	    for (Double c : r.getWhSpent()) {
		chargeSpentSum += c;
		System.out.println("\t "+d.format(c));
	    }
	    chargeSpentSums.add(chargeSpentSum);
	}
	
	double i2BaseSum = currentSquaredSums.get(0);
	double baseChargeSpentSum = chargeSpentSums.get(0);
	//System.out.println("Trips tested: "+tripsTested);
	//System.out.println("Baseline, " + results.get(0).getPolicyName()
	//	+ ", i^2: " + d.format(i2BaseSum));
	//System.out.println("Baseline, " + results.get(0).getPolicyName()
	//	+ ", charge spent: " + d.format(baseChargeSpentSum));
	
	for (int i = 1; i < results.size(); i++) {
	    double i2Percent = 1 - (currentSquaredSums.get(i) / i2BaseSum);
	    System.out.println(results.get(i).getPolicyName()
		    + " i^2, vs. baseline: "
		    + d.format(currentSquaredSums.get(i)) + " :: "
		    + p.format(i2Percent) + " reduction");
	    double rangePercent = Math.abs((baseChargeSpentSum / chargeSpentSums.get(i))-1);
	    System.out.println(results.get(i).getPolicyName()
		    + " charge spent, range vs. baseline: "
		    + d.format(chargeSpentSums.get(i)) + " :: "
		    + p.format(rangePercent));
	}
    }
    
    public void visualizeTrip(Trip trip, BatteryModel battery,
	    BatteryModel capacitor) {
	System.out.println(trip);
	System.out.println("Integral of current squared: "
		+ d.format(battery.getCurrentSquaredIntegral()));
    }
    
    public void visualizeTrips(SimulationResults simResults) {
	String policyName = simResults.getPolicyName();
	System.out.println("=============");
	System.out.println(policyName + " i^2 results:");
	for (int i = 0; i < simResults.getTripStrings().size(); i++) {
	    String trip = simResults.getTripStrings().get(i);
	    double currentSquaredIntegral = simResults
		    .getBatteryCurrentSquaredIntegrals().get(i);
	    System.out.println(d.format(currentSquaredIntegral) + "; " + trip);
	}
    }

    @Override
    public void visualizeDrivers(List<DriverResults> driverResults) {
	// TODO Auto-generated method stub
	
    }
}
