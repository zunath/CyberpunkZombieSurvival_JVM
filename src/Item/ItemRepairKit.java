package Item;

import Common.IScriptEventHandler;
import Enumerations.AbilityType;
import GameSystems.DurabilitySystem;
import GameSystems.MagicSystem;
import GameSystems.ProgressionSystem;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;

import java.util.Objects;

public class ItemRepairKit implements IScriptEventHandler {
    @Override
    public void runScript(NWObject objSelf) {

        NWObject oPC = NWScript.getItemActivator();
        NWObject oKit = NWScript.getItemActivated();
        NWObject oItem = NWScript.getItemActivatedTarget();
        String sResref = NWScript.getResRef(oKit);
        int iSkill = ProgressionSystem.GetPlayerSkillLevel(oPC, ProgressionSystem.SkillType_ITEM_REPAIR);
        int iRepairAmount = 0;

        // Repair Kit Alpha - Restores 10% - 30% durability
        if(Objects.equals(sResref, "repair_kit_alpha"))
        {
            iRepairAmount = 10 + (int)((iSkill * 1.5));
            if(iRepairAmount > 25) iRepairAmount = 25;
        }
        // Repair Kit Beta - Restores 20% - 70% durability
        else if(Objects.equals(sResref, "repair_kit_beta"))
        {
            iRepairAmount = 20 + (int)((iSkill * 2.5));
            if(iRepairAmount > 50) iRepairAmount = 50;
        }
        // Repair Kit Gamma - Restores 35% - 90% durability
        else if(Objects.equals(sResref, "repair_kit_gamma"))
        {
            iRepairAmount = 35 + (int)((iSkill * 3.5));
            if(iRepairAmount > 75) iRepairAmount = 75;
        }

        // Fixer ability increases repair amount by 25%
        if(MagicSystem.IsAbilityEquipped(oPC, AbilityType.Fixer))
        {
            iRepairAmount *= 1.25;
        }

        // Run the repair function
        DurabilitySystem.RunItemRepair(oPC, oItem, iRepairAmount);

        // Remove the kit from the game
        NWScript.destroyObject(oKit, 0.0f);
    }
}
