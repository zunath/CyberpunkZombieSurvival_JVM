package Abilities;

import org.nwnx.nwnx2.jvm.NWObject;

public interface IAbility {
    boolean CanCastSpell();
    String CannotCastSpellMessage();
    int ManaCost(NWObject oPC, int baseCost);
    float CastingTime(NWObject oPC, float baseCastingTime);
    float CooldownTime(NWObject oPC, float baseCooldownTime);
    void OnImpact(NWObject oPC, NWObject oTarget);
    void OnEquip(NWObject oPC);
    void OnUnequip(NWObject oPC);

}
