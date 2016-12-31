package Abilities.EvocationMagic;

import Abilities.IAbility;
import Enumerations.AbilityType;
import GameSystems.MagicSystem;
import GameSystems.ProgressionSystem;
import org.nwnx.nwnx2.jvm.NWEffect;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.constants.Ability;

import static org.nwnx.nwnx2.jvm.constants.All.DURATION_TYPE_INSTANT;
import static org.nwnx.nwnx2.jvm.constants.All.DURATION_TYPE_TEMPORARY;
import static org.nwnx.nwnx2.jvm.constants.All.VFX_IMP_STUN;

public class Stun implements IAbility {
    @Override
    public boolean CanCastSpell(NWObject oPC) {
        return true;
    }

    @Override
    public String CannotCastSpellMessage() {
        return null;
    }

    @Override
    public int ManaCost(NWObject oPC, int baseManaCost) {
        if(MagicSystem.IsAbilityEquipped(oPC, AbilityType.TouchedByEvocation))
            baseManaCost--;

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
        int skill = ProgressionSystem.GetPlayerSkillLevel(oPC, ProgressionSystem.SkillType_EVOCATION_AFFINITY);
        int stat = NWScript.getAbilityScore(oPC, Ability.INTELLIGENCE, false) - 10;
        float baseDuration = 1.0f;
        float bonusDuration = (skill * 0.20f) + (stat * 0.50f);
        float duration = baseDuration + bonusDuration;

        NWEffect effect = NWScript.effectStunned();
        NWScript.applyEffectToObject(DURATION_TYPE_TEMPORARY, effect, oTarget, duration);
        NWScript.applyEffectToObject(DURATION_TYPE_INSTANT, NWScript.effectVisualEffect(VFX_IMP_STUN, false), oTarget, 0.0f);
    }

    @Override
    public void OnEquip(NWObject oPC) {

    }

    @Override
    public void OnUnequip(NWObject oPC) {

    }
}
