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
    public String Name() {
        return "Cure";
    }

    @Override
    public int ManaCost() {
        return 5;
    }

    @Override
    public float CastingTime() {
        return 2.0f;
    }

    @Override
    public float CooldownTime() {
        return 1.0f;
    }

    @Override
    public void OnImpact(NWObject oPC, NWObject oTarget) {
        NWScript.sendMessageToPC(oPC, "Firing impact");
    }
}
