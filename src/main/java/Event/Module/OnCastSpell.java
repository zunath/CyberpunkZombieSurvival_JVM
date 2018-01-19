package Event.Module;

import Common.IScriptEventHandler;
import NWNX.NWNX_Events;
import NWNX.NWNX_Events_Old;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;

public class OnCastSpell implements IScriptEventHandler {
    @Override
    public void runScript(NWObject oPC) {
        NWObject oItem = NWNX_Events.OnCastSpell_GetItem();
        String sTag = NWScript.getTag(oItem);

        // Tag based scripting
        NWScript.executeScript(sTag, oPC);
    }
}
