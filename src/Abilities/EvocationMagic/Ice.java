package Abilities.EvocationMagic;

import Abilities.IAbility;
import Enumerations.AbilityType;
import GameSystems.MagicSystem;
import GameSystems.ProgressionSystem;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.constants.Ability;
import org.nwnx.nwnx2.jvm.constants.DamagePower;
import org.nwnx.nwnx2.jvm.constants.DamageType;

import java.util.concurrent.ThreadLocalRandom;

import static org.nwnx.nwnx2.jvm.constants.All.*;

public class Ice implements IAbility {
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
        int intelligence = NWScript.getAbilityScore(oPC, Ability.INTELLIGENCE, false) - 10;
        int minimumDamage = 1 + (int)((skill + intelligence) * 0.10f);
        int maximumDamage = 4 + (int)((skill + intelligence) * 0.25f);
        int damage = ThreadLocalRandom.current().nextInt(minimumDamage, maximumDamage + 1);

        float slowLength = 2.0f + (skill * 0.5f) + (intelligence * 0.75f);

        NWScript.applyEffectToObject(DURATION_TYPE_INSTANT, NWScript.effectVisualEffect(VFX_FNF_ICESTORM, false), oTarget, 0.0f);
        NWScript.applyEffectToObject(DURATION_TYPE_TEMPORARY, NWScript.effectDamage(damage, DamageType.COLD, DamagePower.NORMAL), oTarget, 0.0f);
        NWScript.applyEffectToObject(DURATION_TYPE_TEMPORARY, NWScript.effectSlow(), oTarget, slowLength);
    }

    @Override
    public void OnEquip(NWObject oPC) {

    }

    @Override
    public void OnUnequip(NWObject oPC) {

    }

    @Override
    public boolean IsHostile() {
        return true;
    }
}
