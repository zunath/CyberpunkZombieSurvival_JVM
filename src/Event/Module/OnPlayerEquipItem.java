package Event.Module;
import Common.IScriptEventHandler;
import GameSystems.*;
import Helper.ColorToken;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.nwnx.nwnx2.jvm.*;
import org.nwnx.nwnx2.jvm.constants.InventorySlot;

import java.util.Objects;

@SuppressWarnings("unused")
public class OnPlayerEquipItem implements IScriptEventHandler {
	@Override
	public void runScript(final NWObject objSelf) {
        CombatSystem combatSystem = new CombatSystem();

		// Bioware Default
		NWScript.executeScript("x2_mod_def_equ", objSelf);
		ProgressionSystem.OnModuleEquip();
        combatSystem.OnModuleEquip();
        DurabilitySystem.OnModuleEquip();
		ArmorSystem.OnModuleEquipItem();
		InventorySystem.OnModuleEquipItem();

		//HandleEquipmentSwappingDelay();
	}

	// Players abuse a bug in NWN which allows them to gain an extra attack.
	// To work around this I force a delay before they can equip weapons during combat.
	private void HandleEquipmentSwappingDelay()
	{
		NWObject oPC = NWScript.getPCItemLastEquippedBy();
		final NWObject oItem = NWScript.getPCItemLastEquipped();
		NWObject rightHand = NWScript.getItemInSlot(InventorySlot.RIGHTHAND, oPC);
		NWObject leftHand = NWScript.getItemInSlot(InventorySlot.LEFTHAND, oPC);

		if(!NWScript.getIsInCombat(oPC)) return;
		if(Objects.equals(oItem, rightHand) && Objects.equals(oItem, leftHand)) return;

		DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssz");
		String delayUntilTimestamp = NWScript.getLocalString(oPC, "TEMP_DELAY_WEAPON_SWITCHING");
		long delayMillis = 0;
		if(!Objects.equals(delayUntilTimestamp, ""))
		{
			DateTime dateTime = formatter.withZone(DateTimeZone.UTC).parseDateTime(delayUntilTimestamp);
			delayMillis = dateTime.getMillis();
		}

		if(DateTime.now(DateTimeZone.UTC).getMillis() < delayMillis)
		{
			NWScript.sendMessageToPC(oPC, ColorToken.Red() + "You must wait longer before equipping that item during combat." + ColorToken.End());

			Scheduler.assign(oPC, new Runnable() {
				@Override
				public void run() {
					NWScript.actionUnequipItem(oItem);
				}
			});
		}

		delayUntilTimestamp = DateTime.now(DateTimeZone.UTC).plusSeconds(6).toString(formatter);
		NWScript.setLocalString(oPC, "TEMP_DELAY_WEAPON_SWITCHING", delayUntilTimestamp);


		NWScript.sendMessageToPC(oPC, delayUntilTimestamp);
	}

}
