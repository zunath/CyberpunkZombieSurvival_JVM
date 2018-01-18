package Event.Module;

import Common.IScriptEventHandler;
import GameSystems.MagicSystem;
import NWNX.NWNX_Events;
import NWNX.NWNX_Events_Old;
import org.nwnx.nwnx2.jvm.NWObject;

public class OnExamine implements IScriptEventHandler {
    @Override
    public void runScript(NWObject examiner) {
        NWObject examinedObject = NWNX_Events.OnExamineObject_GetTarget();
        MagicSystem.OnModuleExamine(examiner, examinedObject);
    }
}
