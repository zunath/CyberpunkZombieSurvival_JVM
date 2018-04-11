package CustomEffect;

import org.nwnx.nwnx2.jvm.NWEffect;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.Scheduler;
import org.nwnx.nwnx2.jvm.constants.DamagePower;
import org.nwnx.nwnx2.jvm.constants.DamageType;
import org.nwnx.nwnx2.jvm.constants.DurationType;

import java.util.concurrent.ThreadLocalRandom;

import static org.nwnx.nwnx2.jvm.NWScript.*;

public class BurnEffect implements ICustomEffectHandler {
    @Override
    public void run(NWObject oCaster, NWObject oTarget) {
        int damage = ThreadLocalRandom.current().nextInt(3, 5);
        NWEffect damageEffect = effectDamage(damage, DamageType.FIRE, DamagePower.NORMAL);

        Scheduler.assignNow(oCaster, () -> applyEffectToObject(DurationType.INSTANT, damageEffect, oTarget, 0.0f));
    }
}
