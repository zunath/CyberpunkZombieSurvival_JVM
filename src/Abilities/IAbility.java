package Abilities;

import org.nwnx.nwnx2.jvm.NWObject;

public interface IAbility {
    boolean CanCastSpell();
    String CannotCastSpellMessage();
    String Name();
    int ManaCost();
    float CastingTime();
    float CooldownTime();
    void OnImpact(NWObject oPC, NWObject oTarget);

}
