package Event.Module;

import Common.IScriptEventHandler;
import NWNX.NWNX_Events;
import org.nwnx.nwnx2.jvm.NWObject;

public class OnUseFeat implements IScriptEventHandler {
    @Override
    public void runScript(NWObject objSelf) {
        NWObject oPC = objSelf;
        int iFeatID = NWNX_Events.GetEventSubType();
        NWObject oTarget = NWNX_Events.GetEventTarget();
    }
}
