package Abilities.HolyMagic;

import Abilities.IAbility;
import Enumerations.AbilityType;
import Enumerations.CustomEffectType;
import GameSystems.CustomEffectSystem;
import GameSystems.MagicSystem;
import GameSystems.ProgressionSystem;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.constants.Ability;

public class Antidote implements IAbility {
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
        if(MagicSystem.IsAbilityEquipped(oPC, AbilityType.TouchedByHoly))
            baseManaCost--;

        return baseManaCost;
    }

    @Override
    public float CastingTime(NWObject oPC, float baseCastingTime) {
        int skill = ProgressionSystem.GetPlayerSkillLevel(oPC, ProgressionSystem.SkillType_HOLY_AFFINITY);
        int wisdom = NWScript.getAbilityScore(oPC, Ability.WISDOM, false) - 10;
        float castingTimeReduction = (skill + wisdom) * 0.5f;
        float castingTime = baseCastingTime - castingTimeReduction;

        if(castingTime < 0.5f)
            castingTime = 0.5f;

        return castingTime;
    }

    @Override
    public float CooldownTime(NWObject oPC, float baseCooldownTime) {
        return baseCooldownTime;
    }


    @Override
    public void OnImpact(NWObject oPC, NWObject oTarget) {

        if(!CustomEffectSystem.HasCustomEffect(oTarget, CustomEffectType.Poison))
        {
            NWScript.sendMessageToPC(oPC, "Your target is not afflicted by poison.");
            return;
        }

        CustomEffectSystem.RemoveCustomEffect(oTarget, CustomEffectType.Poison);
    }

    @Override
    public void OnEquip(NWObject oPC) {

    }

    @Override
    public void OnUnequip(NWObject oPC) {

    }
}
