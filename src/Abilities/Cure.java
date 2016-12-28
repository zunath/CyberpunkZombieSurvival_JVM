package Abilities;

import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;

public class Cure implements IAbility {
    @Override
    public boolean CanCastSpell() {
        return true;
    }

    @Override
    public String CannotCastSpellMessage() {
        return null;
    }

    @Override
    public void OnImpact(NWObject oPC, NWObject oTarget) {
        NWScript.sendMessageToPC(oPC, "Firing impact");
    }
}
