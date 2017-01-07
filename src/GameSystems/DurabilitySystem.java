package GameSystems;

import Common.Constants;
import GameObject.ItemGO;
import Helper.ColorToken;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.Scheduler;
import org.nwnx.nwnx2.jvm.constants.BaseItem;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class DurabilitySystem {
    private static final int DurabilityLossChance = 2;

    public static void OnModuleEquip() {
        NWObject oPC = NWScript.getPCItemLastEquippedBy();
        final NWObject oItem = NWScript.getPCItemLastEquipped();
        ItemGO itemGO = new ItemGO(oItem);
        int iDurability = itemGO.getDurability();

        if (iDurability <= 0) {
            Scheduler.assign(oPC, new Runnable() {
                @Override
                public void run() {
                    NWScript.clearAllActions(false);
                    NWScript.actionUnequipItem(oItem);
                }
            });

            NWScript.floatingTextStringOnCreature(ColorToken.Red() + "That item is broken and must be repaired before you can use it." + ColorToken.End(), oPC, false);
        }
    }

    public static void RunItemDecay(NWObject oPC, NWObject oItem, int decayChanceModifier, int decayAmountModifier, boolean displayMessage) {
        // Item decay doesn't run for any items if Invincible is in effect
        // Or if the item is unbreakable (e.g profession items)
        // Or if the item is not part of the valid list of base item types
        if (NWScript.getPlotFlag(oPC) ||
            NWScript.getLocalInt(oItem, "UNBREAKABLE") == 1 ||
            !GetValidDurabilityTypes().contains(NWScript.getBaseItemType(oItem)))
            return;
        ItemGO itemGO = new ItemGO(oItem);

        int iDurability = itemGO.getDurability();

        int iRoll = ThreadLocalRandom.current().nextInt(1, 100);
        String sItemName = NWScript.getName(oItem, true);
        // Take the base decay loss chance and subtract it by player's skill in Maintenance.
        int iDC = DurabilityLossChance + decayChanceModifier;

        // Player failed the roll - lower durability by a few points
        if (iRoll <= iDC) {
            // Lose 1-4 points plus iDecayAmountModifier
            iRoll = NWScript.random(4) + 1 + decayAmountModifier;
            if (iRoll < 1) {
                iRoll = 1;
            }

            iDurability = iDurability - iRoll;
            // Durability has hit 0% - force PC to unequip the item immediately by copying it and destroying the old one
            if (iDurability <= 0) {
                NWObject oCopy = NWScript.copyItem(oItem, oPC, true);
                NWScript.destroyObject(oItem, 0.0f);
                ItemGO copyGO = new ItemGO(oCopy);
                copyGO.setDurability(0);

                NWScript.deleteLocalInt(oCopy, Constants.GunEquippedTemporaryVariable);

                if (displayMessage) {
                    NWScript.sendMessageToPC(oPC, ColorToken.Red() + "Your " + sItemName + " has broken!" + ColorToken.End());
                }
            }
            // Update the item's durability and change its name
            else {
                // Inform player that their weapon has decayed
                if (displayMessage) {
                    NWScript.sendMessageToPC(oPC, ColorToken.Red() + "Your " + sItemName + " has been damaged. (" + iDurability + "%)" + ColorToken.End());
                }
                itemGO.setDurability(iDurability);
            }
        }
    }

    public static void RunItemRepair(NWObject oPC, NWObject oItem, int amount) {
        // Prevent repairing for less than 1%
        if (amount < 1) return;
        ItemGO itemGO = new ItemGO(oItem);

        // Item has no durability - inform player they can't repair it
        if (itemGO.getDurability() == -1) {
            NWScript.sendMessageToPC(oPC, ColorToken.Red() + "You cannot repair that item." + ColorToken.End());
            return;
        }

        itemGO.setDurability(itemGO.getDurability() + amount);

        NWScript.sendMessageToPC(oPC, ColorToken.Green() + "You repaired your " + NWScript.getName(oItem, true) + ". (" + itemGO.getDurability() + "%)" + ColorToken.End());
    }


    private static List<Integer> GetValidDurabilityTypes() {
        Integer[] result = {
                BaseItem.ARMOR,
                BaseItem.BASTARDSWORD,
                BaseItem.BATTLEAXE,
                BaseItem.BELT,
                BaseItem.BOOTS,
                BaseItem.BRACER,
                BaseItem.CLOAK,
                BaseItem.CLUB,
                BaseItem.DAGGER,
                BaseItem.DIREMACE,
                BaseItem.DOUBLEAXE,
                BaseItem.DWARVENWARAXE,
                BaseItem.GLOVES,
                BaseItem.GREATAXE,
                BaseItem.GREATSWORD,
                BaseItem.HALBERD,
                BaseItem.HANDAXE,
                BaseItem.HEAVYCROSSBOW,
                BaseItem.HEAVYFLAIL,
                BaseItem.HELMET,
                BaseItem.KAMA,
                BaseItem.KATANA,
                BaseItem.KUKRI,
                BaseItem.LARGESHIELD,
                BaseItem.LIGHTCROSSBOW,
                BaseItem.LIGHTFLAIL,
                BaseItem.LIGHTHAMMER,
                BaseItem.LIGHTMACE,
                BaseItem.LONGBOW,
                BaseItem.LONGSWORD,
                BaseItem.MORNINGSTAR,
                BaseItem.QUARTERSTAFF,
                BaseItem.RAPIER,
                BaseItem.SCIMITAR,
                BaseItem.SCYTHE,
                BaseItem.SHORTBOW,
                BaseItem.SHORTSPEAR,
                BaseItem.SHORTSWORD,
                BaseItem.SHURIKEN,
                BaseItem.SICKLE,
                BaseItem.SLING,
                BaseItem.SMALLSHIELD,
                BaseItem.TOWERSHIELD,
                BaseItem.TRIDENT,
                BaseItem.TWOBLADEDSWORD,
                BaseItem.WARHAMMER,
                BaseItem.WHIP
        };

        return Arrays.asList(result);
    }

}
