package Event.Module;

import Common.IScriptEventHandler;
import GameSystems.AreaInstanceSystem;
import NWNX.*;
import GameSystems.DeathSystem;
import GameSystems.SpawnSystem;
import GameSystems.StructureSystem;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;

@SuppressWarnings("unused")
public class OnLoad implements IScriptEventHandler {
	@Override
	public void runScript(NWObject objSelf) {
		SpawnSystem spawnSystem = new SpawnSystem();

        AddGlobalEventHandlers();
		AddAreaEventHandlers();

		// NWNX Setup
		NWScript.setLocalString(objSelf, "NWNX!INIT", "1");
		NWScript.getLocalObject(objSelf, "NWNX!INIT");
		NWScript.deleteLocalString(objSelf, "NWNX!INIT");

		// Bioware default
		NWScript.executeScript("x2_mod_def_load", objSelf);
		// SimTools and NWNX
		NWScript.executeScript("fky_chat_modload", objSelf);
        DeathSystem.OnModuleLoad();
		StructureSystem.OnModuleLoad();
		// Spawn system
		spawnSystem.OnModuleLoad();
		AreaInstanceSystem.OnModuleLoad();

	}

	private void AddAreaEventHandlers()
	{
		NWObject area = NWNX_Funcs.GetFirstArea();
		while(NWScript.getIsObjectValid(area))
		{
			String result = NWNX_Funcs.SetEventHandler(area, AreaScript.OnEnter, "area_enter");
			NWNX_Funcs.SetEventHandler(area, AreaScript.OnExit, "area_exit");
			NWNX_Funcs.SetEventHandler(area, AreaScript.OnHeartbeat, "area_heartbeat");
			NWNX_Funcs.SetEventHandler(area, AreaScript.OnUserDefinedEvent, "area_user");

			area = NWNX_Funcs.GetNextArea();
		}
	}

    private void AddGlobalEventHandlers()
    {
        NWNX_Events.SetGlobalEventHandler(NWNX_Events.EVENT_TYPE_ATTACK, "mod_on_attack");
        NWNX_Events.SetGlobalEventHandler(NWNX_Events.EVENT_TYPE_CAST_SPELL, "mod_on_castspell");
        NWNX_Events.SetGlobalEventHandler(NWNX_Events.EVENT_TYPE_EXAMINE, "mod_on_examine");
        NWNX_Events.SetGlobalEventHandler(NWNX_Events.EVENT_TYPE_PICKPOCKET, "mod_on_pickpock");
        NWNX_Events.SetGlobalEventHandler(NWNX_Events.EVENT_TYPE_SAVE_CHAR, "mod_on_save");
        NWNX_Events.SetGlobalEventHandler(NWNX_Events.EVENT_TYPE_TOGGLE_MODE, "mod_on_toggmode");
        NWNX_Events.SetGlobalEventHandler(NWNX_Events.EVENT_TYPE_TOGGLE_PAUSE, "mod_on_toggpause");
        NWNX_Events.SetGlobalEventHandler(NWNX_Events.EVENT_TYPE_USE_FEAT, "mod_on_usefeat");
        NWNX_Events.SetGlobalEventHandler(NWNX_Events.EVENT_TYPE_USE_ITEM, "mod_on_useitem");

		NWNX_DMActions.SetDMActionScript(DMActionType.CREATE_ITEM_ON_AREA, "dm_areaitem");
		NWNX_DMActions.SetDMActionScript(DMActionType.CREATE_ITEM_ON_OBJECT, "dm_objitem");
		NWNX_DMActions.SetDMActionScript(DMActionType.CREATE_PLACEABLE, "dm_placeable");
		NWNX_DMActions.SetDMActionScript(DMActionType.GIVE_GOLD, "dm_gold");
		NWNX_DMActions.SetDMActionScript(DMActionType.GIVE_LEVEL, "dm_level");
		NWNX_DMActions.SetDMActionScript(DMActionType.GIVE_XP, "dm_xp");
		NWNX_DMActions.SetDMActionScript(DMActionType.HEAL_CREATURE, "dm_heal");
		NWNX_DMActions.SetDMActionScript(DMActionType.REST_CREATURE, "dm_rest");
		NWNX_DMActions.SetDMActionScript(DMActionType.RUNSCRIPT, "dm_runscript");
		NWNX_DMActions.SetDMActionScript(DMActionType.SPAWN_CREATURE, "dm_spawn");
		NWNX_DMActions.SetDMActionScript(DMActionType.TOGGLE_IMMORTALITY, "dm_immortal");
		NWNX_DMActions.SetDMActionScript(DMActionType.TOGGLE_INVULNERABILITY, "dm_invuln");
    }
}
