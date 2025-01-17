/**
 * 
 */
package org.chargecar.prize.policies;

import org.chargecar.prize.battery.BatteryModel;
import org.chargecar.prize.util.PointFeatures;
import org.chargecar.prize.util.PowerFlows;
import org.chargecar.prize.util.TripFeatures;

/**
 * DO NOT EDIT
 * 
 * Policy interface for compound energy storage policies.
 * 
 * @author Alex Styler
 * 
 */
public interface Policy {
    public String getName();
    
    public void loadState();
    
    public void clearState();
    
    public void endTrip();
    
    public PowerFlows calculatePowerFlows(PointFeatures pointFeatures);
    
    public void beginTrip(TripFeatures tripFeatures, BatteryModel batteryClone,
	    BatteryModel capacitorClone);

    public String getShortName();
}
