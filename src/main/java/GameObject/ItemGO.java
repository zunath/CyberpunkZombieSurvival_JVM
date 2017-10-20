package GameObject;

import Enumerations.CustomItemProperty;
import GameSystems.DurabilitySystem;
import Helper.ItemPropertyHelper;
import org.nwnx.nwnx2.jvm.NWItemProperty;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;

public class ItemGO {
    private String tag;
    private String resref;
    private String script;
    private NWObject item;

    public ItemGO(NWObject oItem)
    {
        this.item = oItem;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getResref() {
        return resref;
    }

    public void setResref(String resref) {
        this.resref = resref;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public int getDurability()
    {
        int itemType = NWScript.getBaseItemType(item);
        if(!DurabilitySystem.GetValidDurabilityTypes().contains(itemType))
            return -1;

        addDurabilityIfNotExist();

        NWItemProperty[] itemProperties = NWScript.getItemProperties(item);
        for(NWItemProperty ip : itemProperties)
        {
            int ipType = NWScript.getItemPropertyType(ip);

            if(ipType == CustomItemProperty.ItemDurability)
            {
                return 101 - NWScript.getItemPropertyCostTableValue(ip);
            }
        }

        return -1;
    }

    public void setDurability(int durability)
    {
        int itemType = NWScript.getBaseItemType(item);
        if(!DurabilitySystem.GetValidDurabilityTypes().contains(itemType))
            return;

        addDurabilityIfNotExist();

        if(durability < 0) durability = 0;
        else if(durability > 100) durability = 100;
        int row2DA = 101 - durability;

        ItemPropertyHelper.AddCustomItemProperty(item, CustomItemProperty.ItemDurability, 0, 35, row2DA, 0, 0);
    }

    private void addDurabilityIfNotExist()
    {
        boolean foundIP = false;
        for(NWItemProperty ip : NWScript.getItemProperties(item))
        {
            if(NWScript.getItemPropertyType(ip) == CustomItemProperty.ItemDurability)
            {
                foundIP = true;
                break;
            }
        }

        if(!foundIP)
        {
            ItemPropertyHelper.AddCustomItemProperty(item, CustomItemProperty.ItemDurability, 0, 35, 1, 0, 0);
        }
    }


    public void StripAllItemProperties()
    {
        for(NWItemProperty prop : NWScript.getItemProperties(item))
        {
            NWScript.removeItemProperty(item, prop);
        }
    }

    public boolean HasItemProperty(int itemPropertyID)
    {
        boolean hasItemProperty = false;
        for(NWItemProperty ip : NWScript.getItemProperties(item))
        {
            if(NWScript.getItemPropertyType(ip) == itemPropertyID)
            {
                hasItemProperty = true;
                break;
            }
        }

        return hasItemProperty;
    }

}
