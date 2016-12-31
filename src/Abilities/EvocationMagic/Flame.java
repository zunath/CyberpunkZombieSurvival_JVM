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

import static org.nwnx.nwnx2.jvm.constants.All.DURATION_TYPE_INSTANT;
import static org.nwnx.nwnx2.jvm.constants.All.DURATION_TYPE_TEMPORARY;
import static org.nwnx.nwnx2.jvm.constants.All.VFX_IMP_FLAME_M;

public class Flame implements IAbility {
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
        int intelligence = NWScript.getAbilityScore(oPC, Ability.INTELLIGENCE, false) - 10;
        int minimumDamage = 2 + (int)((skill + intelligence) * 0.25f);
        int maximumDamage = 6 + (int)((skill + intelligence) * 0.50f);

        int damage = ThreadLocalRandom.current().nextInt(minimumDamage, maximumDamage + 1);

        NWScript.applyEffectToObject(DURATION_TYPE_INSTANT, NWScript.effectVisualEffect(VFX_IMP_FLAME_M, false), oTarget, 0.0f);
        NWScript.applyEffectToObject(DURATION_TYPE_TEMPORARY, NWScript.effectDamage(damage, DamageType.MAGICAL, DamagePower.NORMAL), oTarget, 0.0f);
    }

    @Override
    public void OnEquip(NWObject oPC) {

    }

    @Override
    public void OnUnequip(NWObject oPC) {

    }
}
