package Abilities;

import GameSystems.ProgressionSystem;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.constants.Ability;

import java.util.Random;

import static org.nwnx.nwnx2.jvm.constants.All.DURATION_TYPE_INSTANT;

public class Cure implements IAbility {
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
        return "Cure";
    }

    @Override
    public int ManaCost() {
        return 5;
    }

    @Override
    public float CastingTime() {
        return 2.0f;
    }

    @Override
    public float CooldownTime() {
        return 1.0f;
    }

    @Override
    public void OnImpact(NWObject oPC, NWObject oTarget) {
        Random random = new Random();
        int skill = ProgressionSystem.GetPlayerSkillLevel(oPC, ProgressionSystem.SkillType_HOLY_AFFINITY);
        int wisdom = NWScript.getAbilityScore(oPC, Ability.WISDOM, false) - 10;
        int baseRecovery = 4 + (skill / 2) + (wisdom / 2);
        int hp = random.nextInt(10 + (skill / 2) + wisdom) + 1;
        int visualID = 0;

        if(hp < baseRecovery) hp = baseRecovery;


        NWScript.applyEffectToObject(DURATION_TYPE_INSTANT, NWScript.effectVisualEffect(visualID, false), oTarget, 0.0f);
        NWScript.applyEffectToObject(DURATION_TYPE_INSTANT, NWScript.effectHeal(hp), oTarget, 0.0f);
    }
}
