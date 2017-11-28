package Abilities.Passive;

import Abilities.IAbility;
import NWNX.NWNX_Funcs;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;

// Increases max hit points by 10 points.
public class Sturdiness implements IAbility {
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
        NWNX_Funcs.SetMaxHitPointsByLevel(oPC, 1, NWScript.getMaxHitPoints(oPC) + 10);
    }

    @Override
    public void OnUnequip(NWObject oPC) {
        NWNX_Funcs.SetMaxHitPointsByLevel(oPC, 1, NWScript.getMaxHitPoints(oPC) - 10);

        if(NWScript.getCurrentHitPoints(oPC) > NWScript.getMaxHitPoints(oPC))
        {
            NWNX_Funcs.SetCurrentHitPoints(oPC, NWScript.getMaxHitPoints(oPC));
        }
    }

    @Override
    public boolean IsHostile() {
        return false;
    }
}
