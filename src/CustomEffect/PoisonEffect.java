package CustomEffect;

import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.constants.DamagePower;
import org.nwnx.nwnx2.jvm.constants.DamageType;
import org.nwnx.nwnx2.jvm.constants.DurationType;

import java.util.concurrent.ThreadLocalRandom;

public class PoisonEffect implements ICustomEffectHandler {
    @Override
    public void run(NWObject oCaster, NWObject oTarget) {
        int damage = ThreadLocalRandom.current().nextInt(3, 7);
        NWScript.applyEffectToObject(DurationType.INSTANT, NWScript.effectDamage(damage, DamageType.MAGICAL, DamagePower.NORMAL), oTarget, 0.0f);
    }
}
