package org.chargecar.lcddisplay.demo.menu.action;

import edu.cmu.ri.createlab.display.character.CharacterDisplay;
import edu.cmu.ri.createlab.display.character.menu.CharacterDisplayMenuItemAction;
import edu.cmu.ri.createlab.menu.MenuItem;
import edu.cmu.ri.createlab.menu.MenuStatusManager;

/**
 * @author Chris Bartley (bartley@cmu.edu)
 */
public final class FirmwareVersionMenuItemAction extends CharacterDisplayMenuItemAction
   {
   public FirmwareVersionMenuItemAction(final MenuItem menuItem,
                                        final MenuStatusManager menuStatusManager,
                                        final CharacterDisplay characterDisplay)
      {
      super(menuItem, menuStatusManager, characterDisplay);
      }

   public void activate()
      {
      getCharacterDisplay().setLine(0, "Firmware");
      getCharacterDisplay().setLine(1, "version 1.0.0f");
      }
   }