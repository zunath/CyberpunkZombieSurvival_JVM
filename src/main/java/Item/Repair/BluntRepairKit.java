package Item.Repair;

import Common.IScriptEventHandler;
import Enumerations.AbilityType;
import GameSystems.DurabilitySystem;
import GameSystems.MagicSystem;
import GameSystems.ProgressionSystem;
import Helper.ItemHelper;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;

@SuppressWarnings("unused")
public class BluntRepairKit implements IScriptEventHandler {
    @Override
    public void runScript(NWObject oPC) {
        NWObject target = NWScript.getItemActivatedTarget();
        NWObject item = NWScript.getItemActivated();

        if(!ItemHelper.IsBlunt(target))
        {
            NWScript.sendMessageToPC(oPC, "You cannot repair that item with this kit.");
            return;
        }

        int skill = ProgressionSystem.GetPlayerSkillLevel(oPC, ProgressionSystem.SkillType_ITEM_REPAIR) * 2;
        float repairAmount = 10.0f + skill;

        if(MagicSystem.IsAbilityEquipped(oPC, AbilityType.Fixer))
        {
            repairAmount *= 1.25f;
        }

        if(DurabilitySystem.RunItemRepair(oPC, target, repairAmount))
        {
            NWScript.destroyObject(item, 0.0f);
        }
    }

}
