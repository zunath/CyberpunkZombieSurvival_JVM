package Abilities.HolyMagic;

import Abilities.IAbility;
import Enumerations.AbilityType;
import GameSystems.MagicSystem;
import GameSystems.ProgressionSystem;
import org.nwnx.nwnx2.jvm.*;
import org.nwnx.nwnx2.jvm.constants.DurationType;

import java.util.Objects;

import static org.nwnx.nwnx2.jvm.constants.All.*;

public class Escape implements IAbility {
    @Override
    public boolean CanCastSpell(NWObject oPC) {
        NWObject oArea = NWScript.getArea(oPC);
        String escapePoint = NWScript.getLocalString(oArea, "ESCAPE_POINT");
        return !Objects.equals(escapePoint, "");
    }

    @Override
    public String CannotCastSpellMessage() {
        return "Escape cannot be cast in this area.";
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
        int wisdom = NWScript.getAbilityScore(oPC, ABILITY_WISDOM, false) - 10;
        float castingTimeReduction = (skill) + (wisdom * 2);
        float castingTime = baseCastingTime - castingTimeReduction;

        if(castingTime < 6.0f)
            castingTime = 6.0f;

        return castingTime;
    }

    @Override
    public float CooldownTime(NWObject oPC, float baseCooldownTime) {
        return baseCooldownTime;
    }


    @Override
    public void OnImpact(NWObject oPC, NWObject oTarget) {
        NWObject oArea = NWScript.getArea(oTarget);
        String waypointTag = NWScript.getLocalString(oArea, "ESCAPE_POINT");
        final NWLocation location = NWScript.getLocation(NWScript.getWaypointByTag(waypointTag));

        NWObject[] members = NWScript.getFactionMembers(oTarget, true);

        for(NWObject member : members)
        {
            if(NWScript.getArea(member) == oArea && NWScript.getDistanceBetween(member, oPC) <= 5.0f)
            {
                final NWObject memberFinal = member;
                NWScript.applyEffectToObject(DurationType.TEMPORARY, NWScript.effectVisualEffect(0, false), member, 1.9f);
                Scheduler.delay(memberFinal, 2000, new Runnable() {
                    @Override
                    public void run() {
                        Scheduler.assign(memberFinal, new Runnable() {
                            @Override
                            public void run() {
                                NWScript.actionJumpToLocation(location);
                            }
                        });
                    }
                });
            }
        }

    }

    @Override
    public void OnEquip(NWObject oPC) {

    }

    @Override
    public void OnUnequip(NWObject oPC) {

    }
}
