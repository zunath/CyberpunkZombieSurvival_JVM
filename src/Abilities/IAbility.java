package Abilities;

import org.nwnx.nwnx2.jvm.NWObject;

public interface IAbility {
    boolean CanCastSpell();
    String CannotCastSpellMessage();
    void OnImpact(NWObject oPC, NWObject oTarget);

}
