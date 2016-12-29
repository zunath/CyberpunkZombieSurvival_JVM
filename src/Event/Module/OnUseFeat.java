package Event.Module;

import Common.IScriptEventHandler;
import GameSystems.MagicSystem;
import NWNX.NWNX_Events;
import org.nwnx.nwnx2.jvm.NWObject;

public class OnUseFeat implements IScriptEventHandler {
    @Override
    public void runScript(NWObject oPC) {
        MagicSystem.OnFeatUsed(oPC);
    }
}
