package Abilities;

import GameSystems.ProgressionSystem;
import org.nwnx.nwnx2.jvm.NWEffect;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.constants.Ability;
import org.nwnx.nwnx2.jvm.constants.EffectType;

import static org.nwnx.nwnx2.jvm.constants.All.DURATION_TYPE_INSTANT;
import static org.nwnx.nwnx2.jvm.constants.All.DURATION_TYPE_TEMPORARY;

public class BoostSTR implements IAbility {
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
        return "Boost STR";
    }

    @Override
    public int ManaCost() {
        return 7;
    }

    @Override
    public float CastingTime() {
        return 4.0f;
    }

    @Override
    public float CooldownTime() {
        return 3.0f;
    }

    @Override
    public void OnImpact(NWObject oPC, NWObject oTarget) {
        int skill = ProgressionSystem.GetPlayerSkillLevel(oPC, ProgressionSystem.SkillType_ENHANCEMENT_AFFINITY);
        int wisdom = NWScript.getAbilityScore(oPC, Ability.WISDOM, false) - 10;
        float baseLengthSeconds = 120;
        float extensionSeconds = 20 * (wisdom + skill);
        float totalLength = baseLengthSeconds + extensionSeconds;
        int visualID = 0;
        NWEffect abilityEffect = NWScript.effectAbilityIncrease(Ability.STRENGTH, 2);

        // Remove existing bonuses to prevent stacking.
        for(NWEffect effect : NWScript.getEffects(oTarget))
        {
            if(NWScript.getEffectType(effect) == EffectType.ABILITY_INCREASE)
            {
                NWScript.removeEffect(oTarget, effect);
            }
        }

        NWScript.applyEffectToObject(DURATION_TYPE_INSTANT, NWScript.effectVisualEffect(visualID, false), oTarget, 0.0f);
        NWScript.applyEffectToObject(DURATION_TYPE_TEMPORARY, abilityEffect, oTarget, totalLength);
    }
}
