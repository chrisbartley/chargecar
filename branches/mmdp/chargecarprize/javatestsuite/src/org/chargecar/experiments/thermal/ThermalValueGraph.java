package org.chargecar.experiments.thermal;

import java.util.List;

import org.chargecar.algodev.predictors.Prediction;
import org.chargecar.prize.battery.BatteryModel;
import org.chargecar.prize.battery.SimpleCapacitor;
import org.chargecar.prize.util.PointFeatures;
import org.chargecar.prize.util.PowerFlowException;

//TODO TABULATE DYNAMICS!!!!!

public class ThermalValueGraph
{    
    final double[] U;
    final double[] temps;
    final double lambda;//discount factor
    final ThermalBattery batt;  
    
    public ThermalValueGraph(double[] temps, double[] massFlows, double discountRate, ThermalBattery batt){
	this.U = massFlows;
	this.temps = temps;
	this.lambda = discountRate;
	this.batt = batt;
    }
    
    public double[][] getValues(List<PointFeatures> points){
	int T = points.size()+1; //how many Time States we have
	int X = temps.length;
	//look for null in case data overlaps new trip
	//there will be a null in the power set to signify a trip
	//break
//	for(int t=0;t<powers.size();t++){
//	    if(powers.get(t) == null){
//		T = t+1;
//		break;
//	    }
//	}
	
	//can decrease time resolution as t -> T
	
	
	final double[][] valueFunction = new double[X][T];
	final ThermalBattery[] xstates = new ThermalBattery[X];
	
	
	for(int x=0;x<X;x++){
	    xstates[x]=batt.createClone();
	    xstates[x].temp = temps[x];
	    for(int t=0;t<T-1;t++){
		valueFunction[x][t] = Double.MAX_VALUE;
	    }
	    valueFunction[x][T-1] = 0;
	}
	
	//djikstra shortest path search
	//TODO change to A*?
	for(int t=T-2;t>=1;t--){
	    double power = 0;
	    power = points.get(t).getPowerDemand();
	    for(int x=0;x<X;x++){
		ThermalBattery state = xstates[x];
		double minValue = Double.MAX_VALUE;
		for(int u=0;u<U.length;u++){
		    double massFlow = U[u];
		    ControlResult result = testControl(state, power, massFlow);
		    
		    double value = result.cost;
		    
		    int floor = 0;
		    int ceil = 0;			
	    		for(int i=0;i < temps.length; i++){
	    		    if(result.temp >= temps[i]){
	    			floor = i;
	    			ceil = i+1;
	    			break;
	    		    }
	    		}
	    		if(ceil >= temps.length){
	    		    ceil = floor;;
	    		}		    
                    
                    double fVal = valueFunction[floor][t+1];

                    if(floor==ceil){
                        value += lambda*fVal;
                    }
                    else{
                        double cVal = valueFunction[ceil][t+1];
                        value += lambda*(fVal + ((cVal - fVal)*(result.temp - temps[floor])/(temps[ceil]-temps[floor]) ));
                    }		    
		    
		    if(value < minValue){
			minValue = value;
		    }
		}
		valueFunction[x][t] = minValue;
	    }
	}
	
	return valueFunction;
	/*
	double percentCharge = cap.getWattHours() / cap.getMaxWattHours();
	int index = (int)(percentCharge*X);
	if(index == X) index = X-1;
	
	return controls[index][0];
*/
	}
    
    public static ControlResult testControl(ThermalBattery batteryState, double powerDraw, double control){
	ThermalBattery batt = batteryState.createClone();
	batt.drawPower(powerDraw, control);
	
	double cost = 0.0;
	
	//cost = Math.pow(control,2); 
	cost = control;//penalize control linearly
	
	//penalize excess of 35C
	double temp = batt.temp;
	/*if(temp > 40){
	    cost = cost + 400;
	}
	else*/ if(temp > 35){
	    cost = cost + Math.pow((temp - 35),2);
	}	
	
	return new ControlResult(batt.temp,cost);
    }
    
    static class ControlResult {
	public final double temp;
	public final double cost;
	public ControlResult(double t,double c){
	    temp = t;
	    cost=c;
	}
    }

}