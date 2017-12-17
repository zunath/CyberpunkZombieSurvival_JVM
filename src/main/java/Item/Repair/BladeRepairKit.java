package Item.Repair;

import Common.IScriptEventHandler;
import Enumerations.AbilityType;
import GameSystems.DurabilitySystem;
import GameSystems.MagicSystem;
import GameSystems.ProgressionSystem;
import Helper.ItemHelper;
import NWNX.NWNX_Events_Old;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;

import java.util.concurrent.ThreadLocalRandom;

@SuppressWarnings("unused")
public class BladeRepairKit implements IScriptEventHandler {
    @Override
    public void runScript(NWObject oPC) {
        NWObject target = NWNX_Events_Old.GetEventTarget();
        NWObject item = NWNX_Events_Old.GetEventItem();

        if(!ItemHelper.IsBlade(target))
        {
            NWScript.sendMessageToPC(oPC, "You cannot repair that item with this kit.");
            return;
        }

        int skill = ProgressionSystem.GetPlayerSkillLevel(oPC, ProgressionSystem.SkillType_ITEM_REPAIR) * 2;
        int repairAmount = ThreadLocalRandom.current().nextInt(20) + skill + 5;

        if(MagicSystem.IsAbilityEquipped(oPC, AbilityType.Fixer))
        {
            repairAmount *= 1.25f;
        }

        DurabilitySystem.RunItemRepair(oPC, item, repairAmount);

        NWScript.destroyObject(item, 0.0f);
    }

}
