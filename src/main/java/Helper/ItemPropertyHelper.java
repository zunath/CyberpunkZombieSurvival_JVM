package Helper;

import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;

public class ItemPropertyHelper {

    // Direct calls to NWNX_Structs's SetItemPropertyInteger don't work from Java.
    // For this reason, I've had to make a script that calls the methods inside NWScript.
    public static void AddCustomItemProperty(NWObject oItem,
                                             int type,
                                             int subType,
                                             int costTable,
                                             int costValue,
                                             int paramTable,
                                             int paramValue)
    {
        NWScript.setLocalInt(NWObject.MODULE, "NWNX_JVM_IPDIRECT_TYPE", type);
        NWScript.setLocalInt(NWObject.MODULE, "NWNX_JVM_IPDIRECT_SUBTYPE", subType);
        NWScript.setLocalInt(NWObject.MODULE, "NWNX_JVM_IPDIRECT_COSTTABLE", costTable);
        NWScript.setLocalInt(NWObject.MODULE, "NWNX_JVM_IPDIRECT_COSTVALUE", costValue);
        NWScript.setLocalInt(NWObject.MODULE, "NWNX_JVM_IPDIRECT_PARAMTABLE", paramTable);
        NWScript.setLocalInt(NWObject.MODULE, "NWNX_JVM_IPDIRECT_PARAMVALUE", paramValue);

        //NWScript.executeScript("nwnx_jvm_ip", oItem);

        // TODO: Update for EE.
    }
}
