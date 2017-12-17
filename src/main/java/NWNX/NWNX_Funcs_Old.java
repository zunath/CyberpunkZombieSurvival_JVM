package NWNX;

import Helper.ScriptHelper;
import org.nwnx.nwnx2.jvm.*;

import java.util.Objects;

public class NWNX_Funcs_Old {

    public static void SetRawQuickBarSlot (NWObject oPC, String sSlot) {
        NWScript.setLocalString(oPC, "NWNX!FUNCS!SETQUICKBARSLOT", sSlot);
    }



    public static String GetConversation (NWObject oCreature) {
        NWScript.setLocalString(oCreature, "NWNX!FUNCS!GETCONVERSATION", "                ");
        return NWScript.getLocalString(oCreature, "NWNX!FUNCS!GETCONVERSATION");
    }


    public static String SetEventHandler (NWObject oObject, int nEvent, String sScript) {
        NWScript.setLocalString(oObject, "NWNX!FUNCSEXT!SETSCRIPT", NWScript.intToString(nEvent)+":"+sScript+"          ");
        NWScript.deleteLocalString(oObject, "NWNX!FUNCSEXT!SETSCRIPT");
        return "";

    }



    public static void StartTimingBar(final NWObject oCreature, int nSeconds, final String sJavaScript) {
        NWScript.setLocalString(oCreature, "NWNX!FUNCSEXT!STARTTIMINGBAR", nSeconds * 1000 + "    ");
        NWScript.deleteLocalString(oCreature, "NWNX!FUNCSEXT!STARTTIMINGBAR");
        Scheduler.delay(oCreature, nSeconds * 1000, () -> StopTimingBar(oCreature, sJavaScript));
    }

    public static void StopTimingBar(NWObject oCreature, String sJavaScript) {
        NWScript.setLocalString(oCreature, "NWNX!FUNCSEXT!STOPTIMINGBAR", "    ");
        NWScript.deleteLocalString(oCreature, "NWNX!FUNCSEXT!STOPTIMINGBAR");
        if(!Objects.equals(sJavaScript, "")) {
            ScriptHelper.RunJavaScript(oCreature, sJavaScript);
        }
    }


}
