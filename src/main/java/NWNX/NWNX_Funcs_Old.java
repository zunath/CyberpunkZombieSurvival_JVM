package NWNX;

import org.nwnx.nwnx2.jvm.*;

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
}
