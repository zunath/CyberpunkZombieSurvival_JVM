package Item;

import Common.IScriptEventHandler;
import Enumerations.AbilityType;
import GameSystems.MagicSystem;
import NWNX.NWNX_Events;
import GameSystems.FoodSystem;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;

@SuppressWarnings("unused")
public class Food implements IScriptEventHandler {
    @Override
    public void runScript(NWObject oPC) {
        NWObject oItem = NWNX_Events.OnItemUsed_GetItem();
        int amount = NWScript.getLocalInt(oItem, "HUNGER_RESTORE");

        // Snake Eater ability grants +50% to hunger restore.
        if(MagicSystem.IsAbilityEquipped(oPC, AbilityType.SnakeEater))
        {
            amount *= 1.5f;
        }

        FoodSystem.IncreaseHungerLevel(oPC, amount);
    }
}
