package Abilities.HolyMagic;

import Abilities.IAbility;
import Enumerations.AbilityType;
import GameSystems.MagicSystem;
import GameSystems.ProgressionSystem;
import org.nwnx.nwnx2.jvm.NWLocation;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.Scheduler;

import java.util.concurrent.ThreadLocalRandom;

import static org.nwnx.nwnx2.jvm.constants.All.ABILITY_WISDOM;

public class TeleportCleiyra implements IAbility {
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
        String waypointTagBase = "TELEPORT_CLEIYRA_";
        for(NWObject member : NWScript.getFactionMembers(oPC, true))
        {
            int waypointID = ThreadLocalRandom.current().nextInt(1, 10);
            String waypointTag = waypointTagBase + waypointID;
            NWObject waypoint = NWScript.getWaypointByTag(waypointTag);
            final NWLocation location = NWScript.getLocation(waypoint);

            Scheduler.assign(member, new Runnable() {
                @Override
                public void run() {
                    NWScript.actionJumpToLocation(location);
                }
            });
        }
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
