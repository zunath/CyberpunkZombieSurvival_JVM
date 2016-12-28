package Event.Module;
import Common.IScriptEventHandler;
import GameSystems.RadioSystem;
import org.nwnx.nwnx2.jvm.*;

@SuppressWarnings("unused")
public class OnAcquireItem implements IScriptEventHandler {
	@Override
	public void runScript(NWObject objSelf) {
		RadioSystem radioSystem = new RadioSystem();

		// Bioware Default
		NWScript.executeScript("x2_mod_def_aqu", objSelf);

		radioSystem.OnModuleAcquire();

		// Key Item GameSystems
		NWScript.executeScript("key_item_modacq", objSelf);
	}
}
