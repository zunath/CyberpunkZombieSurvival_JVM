package Abilities.EnhancingMagic;

import Abilities.IAbility;
import Enumerations.AbilityType;
import GameObject.PlayerGO;
import GameSystems.MagicSystem;
import GameSystems.ProgressionSystem;
import org.nwnx.nwnx2.jvm.NWEffect;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.constants.Ability;
import org.nwnx.nwnx2.jvm.constants.EffectType;

import static org.nwnx.nwnx2.jvm.constants.All.DURATION_TYPE_INSTANT;
import static org.nwnx.nwnx2.jvm.constants.All.DURATION_TYPE_TEMPORARY;

public class BoostINT implements IAbility {
    @Override
    public boolean CanCastSpell(NWObject oPC, NWObject oTarget) {
        return true;
    }

    @Override
    public String CannotCastSpellMessage() {
        return null;
    }

    @Override
    public int ManaCost(NWObject oPC, int baseManaCost) {
        if(MagicSystem.IsAbilityEquipped(oPC, AbilityType.TouchedByEnhancement))
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
        PlayerGO pcGO = new PlayerGO(oPC);
        int skill = ProgressionSystem.GetPlayerSkillLevel(oPC, ProgressionSystem.SkillType_ENHANCEMENT_AFFINITY);
        int wisdom = NWScript.getAbilityScore(oPC, Ability.WISDOM, false) - 10;
        int itemBonus = pcGO.CalculateEnhancementBonus();
        float baseLengthSeconds = 120;
        float extensionSeconds = 20 * (wisdom + skill + itemBonus);
        float totalLength = baseLengthSeconds + extensionSeconds;
        int visualID = 0;
        int amount = 2 + (itemBonus / 3);
        NWEffect abilityEffect = NWScript.effectAbilityIncrease(Ability.INTELLIGENCE, amount);

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

    @Override
    public void OnEquip(NWObject oPC) {

    }

    @Override
    public void OnUnequip(NWObject oPC) {

    }

    @Override
    public boolean IsHostile() {
        return false;
    }
}