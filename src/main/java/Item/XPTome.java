package Item;

import Common.IScriptEventHandler;
import GameSystems.ProgressionSystem;
import org.nwnx.nwnx2.jvm.NWObject;

import static org.nwnx.nwnx2.jvm.NWScript.*;

public class XPTome implements IScriptEventHandler {
    @Override
    public void runScript(NWObject oPC) {

        NWObject oItem = getItemActivated();
        int amount = getLocalInt(oItem, "XP_AMOUNT");

        if(amount <= 0 || !getIsPC(oPC) || getIsDM(oPC) || getIsDMPossessed(oPC)) return;

        ProgressionSystem.GiveExperienceToPC(oPC, amount);
        destroyObject(oItem, 0.0f);

    }
}
