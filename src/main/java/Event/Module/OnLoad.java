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
			NWNX_Object.SetEventHandler(area, AreaObjectScript.OnEnter, "area_enter");
			NWNX_Object.SetEventHandler(area, AreaObjectScript.OnExit, "area_exit");
			NWNX_Object.SetEventHandler(area, AreaObjectScript.OnHeartbeat, "area_heartbeat");
			NWNX_Object.SetEventHandler(area, AreaObjectScript.OnUserDefinedEvent, "area_user");

			area = NWScript.getNextArea();
		}
	}

    private void AddGlobalEventHandlers()
    {
        NWNX_Events_Old.SetGlobalEventHandler(NWNX_Events_Old.EVENT_TYPE_ATTACK, "mod_on_attack");
        NWNX_Events_Old.SetGlobalEventHandler(NWNX_Events_Old.EVENT_TYPE_CAST_SPELL, "mod_on_castspell");

        NWNX_Events.SubscribeEvent("NWNX_ON_EXAMINE_OBJECT_BEFORE", "mod_on_examine");

        NWNX_Events_Old.SetGlobalEventHandler(NWNX_Events_Old.EVENT_TYPE_PICKPOCKET, "mod_on_pickpock");
        NWNX_Events_Old.SetGlobalEventHandler(NWNX_Events_Old.EVENT_TYPE_SAVE_CHAR, "mod_on_save");
        NWNX_Events_Old.SetGlobalEventHandler(NWNX_Events_Old.EVENT_TYPE_TOGGLE_MODE, "mod_on_toggmode");
        NWNX_Events_Old.SetGlobalEventHandler(NWNX_Events_Old.EVENT_TYPE_TOGGLE_PAUSE, "mod_on_toggpause");
        NWNX_Events_Old.SetGlobalEventHandler(NWNX_Events_Old.EVENT_TYPE_USE_FEAT, "mod_on_usefeat");
        NWNX_Events_Old.SetGlobalEventHandler(NWNX_Events_Old.EVENT_TYPE_USE_ITEM, "mod_on_useitem");

		NWNX_DMActions_Old.SetDMActionScript(DMActionType.CREATE_ITEM_ON_AREA, "dm_areaitem");
		NWNX_DMActions_Old.SetDMActionScript(DMActionType.CREATE_ITEM_ON_OBJECT, "dm_objitem");
		NWNX_DMActions_Old.SetDMActionScript(DMActionType.CREATE_PLACEABLE, "dm_placeable");
		NWNX_DMActions_Old.SetDMActionScript(DMActionType.GIVE_GOLD, "dm_gold");
		NWNX_DMActions_Old.SetDMActionScript(DMActionType.GIVE_LEVEL, "dm_level");
		NWNX_DMActions_Old.SetDMActionScript(DMActionType.GIVE_XP, "dm_xp");
		NWNX_DMActions_Old.SetDMActionScript(DMActionType.HEAL_CREATURE, "dm_heal");
		NWNX_DMActions_Old.SetDMActionScript(DMActionType.REST_CREATURE, "dm_rest");
		NWNX_DMActions_Old.SetDMActionScript(DMActionType.RUNSCRIPT, "dm_runscript");
		NWNX_DMActions_Old.SetDMActionScript(DMActionType.SPAWN_CREATURE, "dm_spawn");
		NWNX_DMActions_Old.SetDMActionScript(DMActionType.TOGGLE_IMMORTALITY, "dm_immortal");
		NWNX_DMActions_Old.SetDMActionScript(DMActionType.TOGGLE_INVULNERABILITY, "dm_invuln");
    }
}
