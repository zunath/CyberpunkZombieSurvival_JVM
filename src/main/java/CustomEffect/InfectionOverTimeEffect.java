package CustomEffect;

import GameSystems.DiseaseSystem;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;

import java.util.Random;

@SuppressWarnings("unused")
public class InfectionOverTimeEffect implements ICustomEffectHandler {
    @Override
    public void run(NWObject oCaster, NWObject oTarget) {

        if(!NWScript.getIsPC(oTarget)) return;

        Random random = new Random();
        DiseaseSystem.IncreaseDiseaseLevel(oTarget, random.nextInt(5));
    }
}
