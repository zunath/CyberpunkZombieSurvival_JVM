package Event.Area;

import Common.IScriptEventHandler;
import GameSystems.SpawnSystem;
import org.nwnx.nwnx2.jvm.NWObject;

@SuppressWarnings("UnusedDeclaration")
public class OnExit implements IScriptEventHandler {
    @Override
    public void runScript(NWObject objSelf) {
        SpawnSystem spawnSystem = new SpawnSystem();

        // Spawn GameSystems
        spawnSystem.OnAreaExit(objSelf);
    }
}
