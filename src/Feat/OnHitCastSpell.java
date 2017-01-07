package Feat;

import Common.IScriptEventHandler;
import GameSystems.DurabilitySystem;
import Helper.ScriptHelper;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.constants.BaseItem;
import org.nwnx.nwnx2.jvm.constants.InventorySlot;

import java.util.Objects;

@SuppressWarnings("UnusedDeclaration")
public class OnHitCastSpell implements IScriptEventHandler {
    @Override
    public void runScript(NWObject oTarget) {
        NWObject oSpellOrigin = NWScript.getSpellCastItem();

        if(!oTarget.equals(NWObject.INVALID))
        {
            // Durability system
            if(NWScript.getBaseItemType(oSpellOrigin) == BaseItem.ARMOR)
            {
                NWObject oBelt = NWScript.getItemInSlot(InventorySlot.BELT, oSpellOrigin);
                if(!oBelt.equals(NWObject.INVALID))
                {
                    DurabilitySystem.RunItemDecay(oSpellOrigin, oBelt, 3, NWScript.random(2), true);
                }
            }

            // Item specific
            String javaScript = NWScript.getLocalString(oSpellOrigin, "JAVA_SCRIPT");

            if(!Objects.equals(javaScript, ""))
            {
                ScriptHelper.RunJavaScript(oTarget, "Item." + javaScript);
            }
        }

    }
}
