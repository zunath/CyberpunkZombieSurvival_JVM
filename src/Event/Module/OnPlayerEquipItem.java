package Event.Module;
import Common.IScriptEventHandler;
import GameSystems.*;
import org.nwnx.nwnx2.jvm.*;

@SuppressWarnings("unused")
public class OnPlayerEquipItem implements IScriptEventHandler {
	@Override
	public void runScript(final NWObject objSelf) {
        CombatSystem combatSystem = new CombatSystem();

		// Bioware Default
		NWScript.executeScript("x2_mod_def_equ", objSelf);
		ProgressionSystem.OnModuleEquip();
		// Combat GameSystems
        combatSystem.OnModuleEquip();
		// Item Durability GameSystems
        DurabilitySystem.OnModuleEquip();
		ArmorSystem.OnModuleEquipItem();
	}
}
