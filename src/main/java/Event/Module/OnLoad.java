package Event.Module;

import Common.IScriptEventHandler;
import GameSystems.AreaInstanceSystem;
import NWNX.*;
import GameSystems.DeathSystem;
import GameSystems.SpawnSystem;
import GameSystems.StructureSystem;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.constants.EventScript;

import static org.nwnx.nwnx2.jvm.NWScript.setEventScript;

@SuppressWarnings("unused")
public class OnLoad implements IScriptEventHandler {
	@Override
	public void runScript(NWObject objSelf) {
		SpawnSystem spawnSystem = new SpawnSystem();

		NWNX_Chat.RegisterChatScript("mod_on_nwnxchat");
        AddGlobalEventHandlers();
		AddAreaEventHandlers();

		// Bioware default
		NWScript.executeScript("x2_mod_def_load", objSelf);
        DeathSystem.OnModuleLoad();
		StructureSystem.OnModuleLoad();
		// Spawn system
		spawnSystem.OnModuleLoad();
		AreaInstanceSystem.OnModuleLoad();

	}

	private void AddAreaEventHandlers()
	{
		NWObject area = NWScript.getFirstArea();
		while(NWScript.getIsObjectValid(area))
		{
			setEventScript(area, EventScript.AREA_ON_ENTER, "area_enter");
			setEventScript(area, EventScript.AREA_ON_EXIT, "area_exit");
			setEventScript(area, EventScript.AREA_ON_HEARTBEAT, "area_heartbeat");
			setEventScript(area, EventScript.AREA_ON_USER_DEFINED_EVENT, "area_user");

			area = NWScript.getNextArea();
		}
	}

    private void AddGlobalEventHandlers()
    {
        NWNX_Events.SubscribeEvent(EventType.StartCombatRoundBefore, "mod_on_attack");
        NWNX_Events.SubscribeEvent(EventType.ExamineObjectBefore, "mod_on_examine");

        //NWNX_Events_Old.SetGlobalEventHandler(NWNX_Events_Old.EVENT_TYPE_TOGGLE_PAUSE, "mod_on_toggpause");

        NWNX_Events.SubscribeEvent(EventType.UseFeatBefore, "mod_on_usefeat");
        NWNX_Events.SubscribeEvent(EventType.UseItemBefore, "mod_on_useitem");

		//NWNX_DMActions_Old.SetDMActionScript(DMActionType.GIVE_LEVEL, "dm_level");
		//NWNX_DMActions_Old.SetDMActionScript(DMActionType.GIVE_XP, "dm_xp");
		//NWNX_DMActions_Old.SetDMActionScript(DMActionType.HEAL_CREATURE, "dm_heal");


    }
}
