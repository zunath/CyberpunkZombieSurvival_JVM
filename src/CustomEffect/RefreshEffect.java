package CustomEffect;

import GameSystems.MagicSystem;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;

public class RefreshEffect implements ICustomEffectHandler {
    @Override
    public void run(NWObject oCaster, NWObject oTarget) {

        if(!NWScript.getIsPC(oTarget)) return;

        MagicSystem.RestoreMana(oTarget, 2);
    }
}
