package org.chargecar.lcddisplay.lcd.menu.action;

import edu.cmu.ri.createlab.LCD;
import edu.cmu.ri.createlab.LCDConstants;
import edu.cmu.ri.createlab.LCDProxy;
import edu.cmu.ri.createlab.display.character.CharacterDisplay;
import edu.cmu.ri.createlab.display.character.menu.CharacterDisplayMenuItemAction;
import edu.cmu.ri.createlab.menu.MenuItem;
import edu.cmu.ri.createlab.menu.MenuStatusManager;
import edu.cmu.ri.createlab.util.thread.DaemonThreadFactory;
import org.apache.log4j.Logger;
import org.chargecar.lcddisplay.lcd.SensorBoard;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author Paul Dille (pdille@andrew.cmu.edu)
 */
public final class VoltagesMenuItemAction extends CharacterDisplayMenuItemAction {
    private static final Logger LOG = Logger.getLogger(VoltagesMenuItemAction.class);

    public VoltagesMenuItemAction(final MenuItem menuItem,
                                  final MenuStatusManager menuStatusManager,
                                  final CharacterDisplay characterDisplay) {
        super(menuItem, menuStatusManager, characterDisplay);
    }

    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor(new DaemonThreadFactory("VoltagesMenuItemAction.executor"));
    private ScheduledFuture<?> scheduledFuture = null;
    final LCD lcd = LCDProxy.getInstance();
    final SensorBoard sensorboard = SensorBoard.getInstance();

    public void activate() {
        try {
            scheduledFuture = executor.scheduleWithFixedDelay(new Runnable() {
                @Override
                public void run() {
                    if (sensorboard == null || sensorboard.getBmsAndEnergy() == null) {
                        LOG.debug("VoltagesMenuItemAction.run(): bms is null");
                        getCharacterDisplay().setLine(0, "No connection to BMS.");
                        getCharacterDisplay().setCharacter(LCDConstants.NUM_ROWS-1,0," ");
                        return;
                    }else if (lcd == null) {
                        getCharacterDisplay().setLine(0, "No connection to LCD.");
                        getCharacterDisplay().setCharacter(LCDConstants.NUM_ROWS-1,0," ");
                        return;
                    }
                    final double minVoltage = Math.round(sensorboard.getBmsAndEnergy().getBmsState().getMinimumCellVoltage() * 100.0) / 100.0;
                    final double maxVoltage = Math.round(sensorboard.getBmsAndEnergy().getBmsState().getMaximumCellVoltage() * 100.0) / 100.0;
                    final double averageVoltage = Math.round(sensorboard.getBmsAndEnergy().getBmsState().getAverageCellVoltage() * 100.0) / 100.0;

                    LOG.debug("VoltagesMenuItemAction.activate(): updating voltages");
                    getCharacterDisplay().setLine(0, "Min Voltage: " + minVoltage);
                    getCharacterDisplay().setLine(1, "Max Voltage: " + maxVoltage);
                    getCharacterDisplay().setLine(2, "Average Voltage: " + averageVoltage);
                    getCharacterDisplay().setCharacter(LCDConstants.NUM_ROWS-1,0," ");
                }
            }, 0, 200, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            // TODO: make this error message better
            LOG.error("VoltagesMenuItemAction.activate(): failed to schedule task", e);
        }
    }

    @Override
    public void deactivate() {
        if (scheduledFuture != null) {
            LOG.debug("VoltagesMenuItemAction.deactivate(): cancelling task");
            scheduledFuture.cancel(true);
        }
    }
}
