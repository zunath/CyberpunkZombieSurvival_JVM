package CustomEffect;

import GameSystems.DiseaseSystem;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;

import java.util.Random;

@SuppressWarnings("unused")
public class InfectionOverTimeEffect implements ICustomEffectHandler {
    @Override
    public void run(NWObject oPC) {

        if(!NWScript.getIsPC(oPC)) return;

        Random random = new Random();
        DiseaseSystem.IncreaseDiseaseLevel(oPC, random.nextInt(5));
    }
}
