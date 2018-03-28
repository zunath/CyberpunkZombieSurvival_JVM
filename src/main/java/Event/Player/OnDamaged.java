package Event.Player;

import Common.IScriptEventHandler;
import org.nwnx.nwnx2.jvm.NWObject;

public class OnDamaged implements IScriptEventHandler {
    @Override
    public void runScript(NWObject oPC) {
        System.out.println("Running OnDamaged for oPC");
    }
}
