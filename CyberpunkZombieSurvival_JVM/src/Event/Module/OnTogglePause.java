package Event.Module;

import Common.IScriptEventHandler;
import NWNX.NWNX_Events;
import org.nwnx.nwnx2.jvm.NWObject;

public class OnTogglePause implements IScriptEventHandler {
    @Override
    public void runScript(NWObject objSelf) {
        NWNX_Events.BypassEvent();
    }
}
