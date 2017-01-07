package Item;

import Enumerations.CustomEffectType;
import Helper.ColorToken;
import Common.IScriptEventHandler;
import GameSystems.CustomEffectSystem;
import GameSystems.DiseaseSystem;
import GameSystems.ProgressionSystem;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.constants.Spell;
import java.util.concurrent.ThreadLocalRandom;

@SuppressWarnings("UnusedDeclaration")
public class ZombieClaw_OnHit implements IScriptEventHandler {
    @Override
    public void runScript(final NWObject oZombie) {

        NWObject oPC = NWScript.getSpellTargetObject();

        NWObject oBite = NWScript.getSpellCastItem();
        String itemTag = NWScript.getTag(oBite);
        if (!NWScript.getIsPC(oPC) || !itemTag.equals("reo_zombie_claw") || NWScript.getIsDM(oPC)) return;

        RunInfectionRoutine(oPC);
        RunBleedingRoutine(oPC, oZombie);
    }

    private void RunInfectionRoutine(NWObject oPC)
    {
        int iChanceToInfect = ThreadLocalRandom.current().nextInt(100) + 1;

        if (iChanceToInfect <= 20 && !NWScript.getHasSpellEffect(Spell.SANCTUARY, oPC))
        {
            int iDiseaseCheck = ThreadLocalRandom.current().nextInt(20) + 1;
            int iDiseaseDC = DiseaseSystem.DCCheck + ThreadLocalRandom.current().nextInt(6);
            int iDiseaseResistance = ProgressionSystem.GetPlayerSkillLevel(oPC, ProgressionSystem.SkillType_DISEASE_RESISTANCE);
            iDiseaseCheck = iDiseaseCheck + iDiseaseResistance;

            NWScript.sendMessageToPC(oPC, ColorToken.SkillCheck() + "Resist disease roll: " + iDiseaseCheck + " VS " + iDiseaseDC + ColorToken.End());
            if (iDiseaseCheck < iDiseaseDC)
            {
                DiseaseSystem.IncreaseDiseaseLevel(oPC, ThreadLocalRandom.current().nextInt(5) + 1);

                if(ThreadLocalRandom.current().nextInt(100) <= 2)
                {
                    CustomEffectSystem.ApplyCustomEffect(oPC, CustomEffectType.InfectionOverTime, 6);
                }
            }
        }
    }

    private void RunBleedingRoutine(NWObject oPC, NWObject oZombie)
    {
        if(ThreadLocalRandom.current().nextInt(100) <= 5)
        {
            CustomEffectSystem.ApplyCustomEffect(oPC, CustomEffectType.Bleeding, 6);
        }
    }
}