package Event.Module;

import Common.IScriptEventHandler;
import GameSystems.ActivityLoggingSystem;
import GameSystems.PlayerDescriptionSystem;
import GameSystems.RadioSystem;
import GameSystems.StructureSystem;
import org.nwnx.nwnx2.jvm.NWObject;

public class OnNWNXChat implements IScriptEventHandler {
    @Override
    public void runScript(NWObject sender) {
        RadioSystem radioSystem = new RadioSystem();
        radioSystem.OnNWNXChat(sender);
        ActivityLoggingSystem.OnModuleNWNXChat(sender);
        PlayerDescriptionSystem.OnModuleNWNXChat(sender);
        StructureSystem.OnModuleNWNXChat(sender);
    }
}
