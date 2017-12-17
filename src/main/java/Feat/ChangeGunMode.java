package Feat;

import Common.IScriptEventHandler;
import NWNX.NWNX_Events_Old;
import GameSystems.CombatSystem;
import org.nwnx.nwnx2.jvm.NWObject;

@SuppressWarnings("UnusedDeclaration")
public class ChangeGunMode implements IScriptEventHandler {
    @Override
    public void runScript(NWObject objSelf) {
        CombatSystem combatSystem = new CombatSystem();
        NWObject oItem = NWNX_Events_Old.GetEventItem();
        combatSystem.ChangeWeaponMode(oItem);
    }
}
