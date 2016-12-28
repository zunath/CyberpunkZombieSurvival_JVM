package Event.Module;

import Common.IScriptEventHandler;
import GameSystems.RadioSystem;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;

public class OnNWNXChat implements IScriptEventHandler {
    @Override
    public void runScript(NWObject objSelf) {
        RadioSystem radioSystem = new RadioSystem();

        NWScript.executeScript("fky_chat", objSelf);

        radioSystem.OnNWNXChat(objSelf);

    }
}
