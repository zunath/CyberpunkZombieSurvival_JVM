package CustomEffect;

import GameSystems.MagicSystem;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;

public class ManaKitEffect implements ICustomEffectHandler {
    @Override
    public void run(NWObject oPC) {

        if(!NWScript.getIsPC(oPC)) return;

        MagicSystem.RestoreMana(oPC, 2);
    }
}
