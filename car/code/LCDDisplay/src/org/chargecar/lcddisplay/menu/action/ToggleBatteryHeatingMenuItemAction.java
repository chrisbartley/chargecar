package org.chargecar.lcddisplay.menu.action;

import edu.cmu.ri.createlab.display.character.CharacterDisplay;
import edu.cmu.ri.createlab.display.character.menu.TwoOptionMenuItemAction;
import edu.cmu.ri.createlab.menu.MenuItem;
import edu.cmu.ri.createlab.menu.MenuStatusManager;
import org.apache.log4j.Logger;
import org.chargecar.lcddisplay.LCD;
import org.chargecar.lcddisplay.LCDConstants;
import org.chargecar.lcddisplay.LCDProxy;

import java.util.Map;

/**
 * @author Paul Dille (pdille@andrew.cmu.edu)
 */
public final class ToggleBatteryHeatingMenuItemAction extends TwoOptionMenuItemAction {
    private static final Logger LOG = Logger.getLogger(ToggleBatteryHeatingMenuItemAction.class);
    private boolean isBatteryHeatingEnabled = false;

    public ToggleBatteryHeatingMenuItemAction(final MenuItem menuItem,
                                              final MenuStatusManager menuStatusManager,
                                              final CharacterDisplay characterDisplay) {
        this(menuItem, menuStatusManager, characterDisplay, null);
    }

    public ToggleBatteryHeatingMenuItemAction(final MenuItem menuItem,
                                              final MenuStatusManager menuStatusManager,
                                              final CharacterDisplay characterDisplay,
                                              final Map<String, String> properties) {
        super(menuItem, menuStatusManager, characterDisplay, properties);
    }

    protected boolean shouldOption1BeSelectedUponActivation() {
        return isBatteryHeatingEnabled;
    }

    protected void executeOption1Action() {
        setBatteryHeatingEnabled(true);
    }

    protected void executeOption2Action() {
        setBatteryHeatingEnabled(false);
    }

    private void setBatteryHeatingEnabled(final boolean actionState) {
        final LCD lcd = LCDProxy.getInstance();
        if (lcd == null) {
            LOG.error("ToggleBatteryHeatingMenuItemAction.setBatteryHeatingEnabled(): lcd is null");
            return;
        }
        getCharacterDisplay().setCharacter(LCDConstants.NUM_ROWS - 1, 0, " ");

        this.isBatteryHeatingEnabled = actionState;

        if (actionState)
            lcd.turnOnBatteryHeating();
        else
            lcd.turnOffBatteryHeating();

    }
}
