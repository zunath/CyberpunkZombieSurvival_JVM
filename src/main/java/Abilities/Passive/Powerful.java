package Abilities.Passive;

import Abilities.IAbility;
import NWNX.NWNX_Creature;
import NWNX.NWNX_Funcs_Old;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.constants.Ability;

// Increases strength by 2 points.
public class Powerful implements IAbility {
    @Override
    public boolean CanCastSpell(NWObject oPC, NWObject oTarget) {
        return false;
    }

    @Override
    public String CannotCastSpellMessage() {
        return null;
    }

    @Override
    public int ManaCost(NWObject oPC, int baseManaCost) {
        return baseManaCost;
    }

    @Override
    public float CastingTime(NWObject oPC, float baseCastingTime) {
        return baseCastingTime;
    }

    @Override
    public float CooldownTime(NWObject oPC, float baseCooldownTime) {
        return baseCooldownTime;
    }

    @Override
    public void OnImpact(NWObject oPC, NWObject oTarget) {

    }

    @Override
    public void OnEquip(NWObject oPC) {
        int score = NWScript.getAbilityScore(oPC, Ability.STRENGTH, true) + 2;
        NWNX_Creature.SetAbilityScore(oPC, Ability.STRENGTH, score);
    }

    @Override
    public void OnUnequip(NWObject oPC)
    {
        int score = NWScript.getAbilityScore(oPC, Ability.STRENGTH, true) - 2;
        NWNX_Creature.SetAbilityScore(oPC, Ability.STRENGTH, score);
    }
    @Override
    public boolean IsHostile() {
        return false;
    }
}
