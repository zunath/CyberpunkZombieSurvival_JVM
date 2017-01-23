package Item.Repair;

import Common.IScriptEventHandler;
import GameSystems.DurabilitySystem;
import GameSystems.ProgressionSystem;
import NWNX.NWNX_Events;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.constants.BaseItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SuppressWarnings("unused")
public class BluntRepairKit implements IScriptEventHandler {
    @Override
    public void runScript(NWObject oPC) {
        NWObject target = NWNX_Events.GetEventTarget();
        NWObject item = NWNX_Events.GetEventItem();
        int targetType = NWScript.getBaseItemType(target);
        List<Integer> validTypes = BuildValidTypes();

        if(!validTypes.contains(targetType))
        {
            NWScript.sendMessageToPC(oPC, "You cannot repair that item with this kit.");
            return;
        }

        int skill = ProgressionSystem.GetPlayerSkillLevel(oPC, ProgressionSystem.SkillType_ITEM_REPAIR) * 2;
        Random random = new Random();
        int repairAmount = random.nextInt(20) + skill + 5;
        DurabilitySystem.RunItemRepair(oPC, item, repairAmount);

        NWScript.destroyObject(item, 0.0f);
    }


    private List<Integer> BuildValidTypes()
    {
        ArrayList<Integer> validTypes = new ArrayList<>();
        validTypes.add(BaseItem.CLUB);
        validTypes.add(BaseItem.DIREMACE);
        validTypes.add(BaseItem.HEAVYFLAIL);
        validTypes.add(BaseItem.LIGHTFLAIL);
        validTypes.add(BaseItem.LIGHTHAMMER);
        validTypes.add(BaseItem.LIGHTMACE);
        validTypes.add(BaseItem.MORNINGSTAR);
        validTypes.add(BaseItem.QUARTERSTAFF);
        validTypes.add(BaseItem.WARHAMMER);
        validTypes.add(BaseItem.WHIP);

        return validTypes;
    }

}
