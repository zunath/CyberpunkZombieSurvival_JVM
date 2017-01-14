package Feat;

import Common.IScriptEventHandler;
import GameSystems.DurabilitySystem;
import Helper.ScriptHelper;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.constants.BaseItem;
import org.nwnx.nwnx2.jvm.constants.InventorySlot;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

@SuppressWarnings("UnusedDeclaration")
public class OnHitCastSpell implements IScriptEventHandler {
    @Override
    public void runScript(NWObject oTarget) {
        NWObject oSpellOrigin = NWScript.getSpellCastItem();

        if(!oTarget.equals(NWObject.INVALID))
        {
            String name =NWScript.getName(oSpellOrigin, false);
            int itemType = NWScript.getBaseItemType(oSpellOrigin);
            // Durability system
            if(itemType == BaseItem.ARMOR)
            {
                NWObject oBelt = NWScript.getItemInSlot(InventorySlot.BELT, oTarget);
                if(!oBelt.equals(NWObject.INVALID))
                {
                    DurabilitySystem.RunItemDecay(oTarget, oBelt, 18, ThreadLocalRandom.current().nextInt(2), true);
                }
            }
            else if(DurabilitySystem.GetValidDurabilityTypes().contains(itemType))
            {
                DurabilitySystem.RunItemDecay(oTarget, oSpellOrigin, 3, ThreadLocalRandom.current().nextInt(2), true);
            }

            // Item specific
            String javaScript = NWScript.getLocalString(oSpellOrigin, "JAVA_SCRIPT");

            if(!Objects.equals(javaScript, ""))
            {
                // Remove "Item." prefix if it exists.
                if(javaScript.startsWith("Item."))
                    javaScript = javaScript.substring(5);
                ScriptHelper.RunJavaScript(oTarget, "Item." + javaScript);
            }
        }

    }
}
