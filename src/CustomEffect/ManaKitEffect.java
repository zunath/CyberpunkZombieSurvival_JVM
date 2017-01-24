package CustomEffect;

import GameSystems.MagicSystem;
import org.nwnx.nwnx2.jvm.NWObject;

public class ManaKitEffect implements ICustomEffectHandler {
    @Override
    public void run(NWObject oPC) {
        MagicSystem.RestoreMana(oPC, 2);
    }
}
