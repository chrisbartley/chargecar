package org.chargecar.lcddisplay.menu.action;

import edu.cmu.ri.createlab.display.character.CharacterDisplay;
import edu.cmu.ri.createlab.display.character.menu.RepeatingActionCharacterDisplayMenuItemAction;
import edu.cmu.ri.createlab.menu.MenuItem;
import edu.cmu.ri.createlab.menu.MenuStatusManager;
import org.apache.log4j.Logger;
import org.chargecar.honda.bms.BMSAndEnergy;
import org.chargecar.lcddisplay.BMSManager;
import org.chargecar.lcddisplay.LCD;
import org.chargecar.lcddisplay.LCDConstants;
import org.chargecar.lcddisplay.LCDProxy;

import java.util.concurrent.TimeUnit;

/**
 * @author Paul Dille (pdille@andrew.cmu.edu)
 */
public final class CurrentsMenuItemAction extends RepeatingActionCharacterDisplayMenuItemAction {
    private static final Logger LOG = Logger.getLogger(CurrentsMenuItemAction.class);

    public CurrentsMenuItemAction(final MenuItem menuItem,
                                  final MenuStatusManager menuStatusManager,
                                  final CharacterDisplay characterDisplay) {
        super(menuItem, menuStatusManager, characterDisplay, 0, 1, TimeUnit.SECONDS);
    }

    @Override
    protected void performAction() {
        final LCD lcd = LCDProxy.getInstance();
        final BMSManager manager = BMSManager.getInstance();
        final BMSAndEnergy data = (manager == null) ? null : manager.getData();

        if (manager == null || data == null) {
            LOG.error("CurrentsMenuItemAction.performAction(): bms is null");
            getCharacterDisplay().setLine(0, "No connection to BMS.");
            getCharacterDisplay().setCharacter(LCDConstants.NUM_ROWS - 1, 0, " ");
            return;
        } else if (lcd == null) {
            LOG.error("CurrentsMenuItemAction.performAction(): lcd is null");
            return;
        }
        final double loadCurrent = Math.round(data.getBmsState().getLoadCurrentAmps() * 100.0) / 100.0;
        final double sourceCurrent = Math.round(data.getBmsState().getSourceCurrentAmps() * 100.0) / 100.0;

        LOG.trace("CurrentsMenuItemAction.activate(): updating currents");
        getCharacterDisplay().setLine(0, "Load Current: " + loadCurrent);
        getCharacterDisplay().setLine(1, "Src Current: " + sourceCurrent);
        getCharacterDisplay().setCharacter(LCDConstants.NUM_ROWS - 1, 0, " ");
    }
}
