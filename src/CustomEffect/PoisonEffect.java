package CustomEffect;

import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.constants.DamagePower;
import org.nwnx.nwnx2.jvm.constants.DamageType;
import org.nwnx.nwnx2.jvm.constants.DurationType;

public class PoisonEffect implements ICustomEffectHandler {
    @Override
    public void run(NWObject oPC) {
        NWScript.applyEffectToObject(DurationType.TEMPORARY, NWScript.effectMovementSpeedDecrease(20), oPC, 6.0f);
        NWScript.applyEffectToObject(DurationType.INSTANT, NWScript.effectDamage(1, DamageType.MAGICAL, DamagePower.NORMAL), oPC, 0.0f);
    }
}
