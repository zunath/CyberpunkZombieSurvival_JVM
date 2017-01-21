package Door;

import Common.IScriptEventHandler;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.Scheduler;

public class AutoClose implements IScriptEventHandler {
    @Override
    public void runScript(final NWObject objSelf) {
        Scheduler.delay(objSelf, 6000, new Runnable() {
            @Override
            public void run() {
                Scheduler.assign(objSelf, new Runnable() {
                    @Override
                    public void run() {
                        NWScript.actionCloseDoor(objSelf);
                    }
                });
            }
        });
    }
}
