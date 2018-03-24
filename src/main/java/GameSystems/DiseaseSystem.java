package GameSystems;

import Entities.PlayerEntity;
import Enumerations.AbilityType;
import Enumerations.CustomEffectType;
import GameObject.PlayerGO;
import Helper.ColorToken;
import Helper.MenuHelper;
import NWNX.*;
import Data.Repository.PlayerRepository;
import org.nwnx.nwnx2.jvm.NWLocation;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.Scheduler;
import org.nwnx.nwnx2.jvm.constants.*;
import org.nwnx.nwnx2.jvm.constants.Package;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import static org.nwnx.nwnx2.jvm.NWScript.*;

public class DiseaseSystem {

    private static int DCCheck = 10;
    private static String CrudResref = "infection_crud";


    public static void IncreaseDiseaseLevel(final NWObject oPC, int iIncreaseBy)
    {
        PlayerGO pcGO = new PlayerGO(oPC);
        PlayerRepository repo = new PlayerRepository();
        PlayerEntity entity = repo.GetByPlayerID(pcGO.getUUID());
        entity.setCurrentInfection(entity.getCurrentInfection() + iIncreaseBy);

        applyEffectToObject(DurationType.INSTANT, effectVisualEffect(Vfx.IMP_DISEASE_S, false), oPC, 0.0f);
        sendMessageToPC(oPC, "Infection Level: " + MenuHelper.BuildBar(entity.getCurrentInfection(), 100, 100));
        ModifyInfectionCrudItems(oPC, entity.getCurrentInfection());

        if(entity.getCurrentInfection() >= entity.getInfectionCap())
        {
            CreateZombieClone(oPC);
            applyEffectToObject(Duration.TYPE_INSTANT, effectHeal(getMaxHitPoints(oPC)), oPC, 0.0f);
            applyEffectToObject(Duration.TYPE_TEMPORARY, effectCutsceneImmobilize(), oPC, 6.0f);

            Scheduler.assign(oPC, () -> actionJumpToLocation(getLocation(getWaypointByTag("DEATH_REALM_LOST_SOULS"))));

            floatingTextStringOnCreature("The infection has taken over your body!", oPC, false);
        }

        repo.save(entity);
    }

    public static void DecreaseDiseaseLevel(final NWObject oPC, int decreaseBy)
    {
        PlayerGO pcGO = new PlayerGO(oPC);
        PlayerRepository repo = new PlayerRepository();
        PlayerEntity entity = repo.GetByPlayerID(pcGO.getUUID());
        entity.setCurrentInfection(entity.getCurrentInfection() - decreaseBy);

        applyEffectToObject(DurationType.INSTANT, effectVisualEffect(VfxImp.REMOVE_CONDITION, false), oPC, 0.0f);
        sendMessageToPC(oPC, "Infection Level: " + MenuHelper.BuildBar(entity.getCurrentInfection(), 100, 100));

        repo.save(entity);
        ModifyInfectionCrudItems(oPC, entity.getCurrentInfection());
    }

    public static PlayerEntity RunDiseaseRemovalProcess(NWObject oPC, PlayerEntity entity)
    {
        entity.setInfectionRemovalTick(entity.getInfectionRemovalTick() - 1);

        if(entity.getInfectionRemovalTick() <= 0)
        {
            if(entity.getCurrentInfection() > 0)
            {
                int infectionToRemove = ThreadLocalRandom.current().nextInt(1, 5);
                if(MagicSystem.IsAbilityEquipped(oPC, AbilityType.InfectionRecovery))
                {
                    infectionToRemove += 3;
                }

                int infection = entity.getCurrentInfection() - infectionToRemove;
                if(infection < 0) infection = 0;

                entity.setCurrentInfection(infection);
                sendMessageToPC(oPC, "Your body fights off some of the infection...");
            }

            entity.setInfectionRemovalTick(600);
        }

        return entity;
    }

    private static void CreateZombieClone(final NWObject oPC)
    {
        PlayerGO pcGO = new PlayerGO(oPC);
        NWLocation lLocation = getLocation(oPC);
        String sClawResref = "reo_zombie_claw";
        final NWObject oClone = copyObject(oPC, lLocation, NWObject.INVALID, "reo_zombie_000");
        applyEffectToObject(DurationType.INSTANT, effectHeal(getMaxHitPoints(oClone)), oClone, 0.0f);

        NWNX_Creature.RemoveFeat(oClone, Feat.WEAPON_PROFICIENCY_EXOTIC);
        NWNX_Creature.RemoveFeat(oClone, Feat.WEAPON_PROFICIENCY_MARTIAL);
        NWNX_Creature.RemoveFeat(oClone, Feat.WEAPON_PROFICIENCY_SIMPLE);
        NWNX_Creature.RemoveFeat(oClone, Feat.SHIELD_PROFICIENCY);

        // All inventory items need to be set to droppable, except undroppable ones
        // Undroppable items are destroyed

        NWObject[] items = getItemsInInventory(oClone);
        for(NWObject item : items)
        {
            if(!getItemCursedFlag(item))
            {
                setDroppableFlag(item, true);
            }
            else
            {
                destroyObject(item, 0.0f);
            }
        }

        // Loop through PC's equipped slots and do the same
        int iSlot;
        for(iSlot = 0; iSlot < 18; iSlot++)
        {
            final NWObject oInventory = getItemInSlot(iSlot, oClone);

            if(!getItemCursedFlag(oInventory))
            {
                setDroppableFlag(oInventory, true);
                Scheduler.assign(oClone, () -> actionUnequipItem(oInventory));

            }
            else
            {
                destroyObject(oInventory, 0.0f);
            }
        }

        // Zombie Claws and Hide needs to be equipped
        final NWObject belt = createItemOnObject("ZombieCloneBelt", oClone, 1, "");
        setDroppableFlag(belt, false);
        Scheduler.assign(oClone, () -> actionEquipItem(belt, InventorySlot.BELT));

        final NWObject claw1 = createItemOnObject(sClawResref, oClone, 1, "");
        setDroppableFlag(claw1, false);
        Scheduler.assign(oClone, () -> actionEquipItem(claw1, InventorySlot.CWEAPON_B));


        final NWObject claw2 = createItemOnObject(sClawResref, oClone, 1, "");
        setDroppableFlag(claw2, false);
        Scheduler.assign(oClone, () -> actionEquipItem(claw2, InventorySlot.CWEAPON_L));

        final NWObject claw3 = createItemOnObject(sClawResref, oClone, 1, "");
        setDroppableFlag(claw3, false);
        Scheduler.assign(oClone, () -> actionEquipItem(claw3, InventorySlot.CWEAPON_R));

        final NWObject creatureArmor = createItemOnObject("rotd_zombieprop", oClone, 1, "");
        setDroppableFlag(creatureArmor, false);
        Scheduler.assign(oClone, () -> actionEquipItem(claw1, InventorySlot.CARMOUR));

        applyEffectToObject(DurationType.PERMANENT, supernaturalEffect(effectMovementSpeedDecrease(15)), oClone, 0.0f);
        levelUpHenchman(oClone, ClassType.UNDEAD, false, Package.UNDEAD);

        changeToStandardFaction(oClone, StandardFaction.HOSTILE);

        Scheduler.assign(oClone, () -> {
            clearAllActions(false);
            actionUnequipItem(getItemInSlot(InventorySlot.LEFTHAND, oClone));
            actionUnequipItem(getItemInSlot(InventorySlot.RIGHTHAND, oClone));
        });

        setName(oClone, "(Zombie) " + getName(oPC, false));

        NWNX_Creature.SetMovementRate(oClone, MovementRate.Slow);
        setColor(oClone, ColorChannel.SKIN, 12);

        setEventScript(oClone, EventScript.CREATURE_ON_MELEE_ATTACKED, "zom_on_attacked");
        setEventScript(oClone, EventScript.CREATURE_ON_BLOCKED_BY_DOOR, "zom_on_blocked");
        setEventScript(oClone, EventScript.CREATURE_ON_DIALOGUE, "zom_on_convo");
        setEventScript(oClone, EventScript.CREATURE_ON_DAMAGED, "zom_on_damaged");
        setEventScript(oClone, EventScript.CREATURE_ON_DEATH, "zom_on_death");
        setEventScript(oClone, EventScript.CREATURE_ON_DISTURBED, "zom_on_disturbed");
        setEventScript(oClone, EventScript.CREATURE_ON_END_COMBATROUND, "zom_on_roundend");
        setEventScript(oClone, EventScript.CREATURE_ON_HEARTBEAT, "zom_on_heartbeat");
        setEventScript(oClone, EventScript.CREATURE_ON_NOTICE, "zom_on_percep");
        setEventScript(oClone, EventScript.CREATURE_ON_RESTED, "zom_on_rested");
        setEventScript(oClone, EventScript.CREATURE_ON_SPAWN_IN, "zom_on_spawn");
        setEventScript(oClone, EventScript.CREATURE_ON_SPELLCASTAT, "zom_on_spellcast");
        setEventScript(oClone, EventScript.CREATURE_ON_USER_DEFINED_EVENT, "zom_on_userdef");

        pcGO.destroyAllInventoryItems(false);
        pcGO.destroyAllEquippedItems();

        executeScript("zom_clonefight", oClone);
    }

    public static void RunDiseaseDCCheck(NWObject oAttacker, NWObject oPC, int chanceModifier, int dcModifier, int infectionOverTimeChanceModifier)
    {
        int iChanceToInfect = ThreadLocalRandom.current().nextInt(100);
        int percentChanceToInfect = 20 + chanceModifier;

        if (iChanceToInfect <= percentChanceToInfect && !getHasSpellEffect(Spell.SANCTUARY, oPC))
        {
            int iDiseaseCheck = ThreadLocalRandom.current().nextInt(0,20);
            int iDiseaseDC = DiseaseSystem.DCCheck + ThreadLocalRandom.current().nextInt(6) + dcModifier;
            int iDiseaseResistance = ProgressionSystem.GetPlayerSkillLevel(oPC, ProgressionSystem.SkillType_DISEASE_RESISTANCE);
            int conBonus = (getAbilityScore(oPC, Ability.CONSTITUTION, false) - 10) / 2;
            iDiseaseCheck = iDiseaseCheck + iDiseaseResistance + conBonus;

            if(MagicSystem.IsAbilityEquipped(oPC, AbilityType.ImmuneSystem))
            {
                iDiseaseCheck += 3;
            }

            sendMessageToPC(oPC, ColorToken.SkillCheck() + "Resist disease roll: " + iDiseaseCheck + " VS " + iDiseaseDC + ColorToken.End());
            if (iDiseaseCheck < iDiseaseDC)
            {
                DiseaseSystem.IncreaseDiseaseLevel(oPC, ThreadLocalRandom.current().nextInt(5) + 1);
                int iotChance = 2 + infectionOverTimeChanceModifier;

                if(ThreadLocalRandom.current().nextInt(100) <= iotChance)
                {
                    int ticks = 6;

                    if(MagicSystem.IsAbilityEquipped(oPC, AbilityType.SlowedInfection))
                    {
                        if(ThreadLocalRandom.current().nextInt(1, 100) <= 5)
                        {
                            ticks = 3;
                        }
                    }

                    CustomEffectSystem.ApplyCustomEffect(oAttacker, oPC, CustomEffectType.InfectionOverTime, ticks);
                }
            }
        }
    }

    private static void ModifyInfectionCrudItems(NWObject oPC, int infectionAmount)
    {
        int numberOfCruds = infectionAmount / 10;
        ArrayList<NWObject> crudObjects = new ArrayList<>();

        for(NWObject item: getItemsInInventory(oPC))
        {
            String resref = getResRef(item);
            if(resref.equals(CrudResref))
            {
                crudObjects.add(item);
            }
        }

        if(numberOfCruds == crudObjects.size()) return;

        if(numberOfCruds > crudObjects.size())
        {
            int numberToAdd = numberOfCruds - crudObjects.size();
            for(int cruds = 1; cruds <= numberToAdd; cruds++)
            {
                createItemOnObject(CrudResref, oPC, 1, "");
            }
        }
        else
        {
            int numberToRemove = crudObjects.size() - numberOfCruds;
            for(int cruds = 1; cruds <= numberToRemove; cruds++)
            {
                destroyObject(crudObjects.get(cruds-1), 0.0f);
            }
        }

    }

}
