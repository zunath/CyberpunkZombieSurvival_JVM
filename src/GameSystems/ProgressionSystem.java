package GameSystems;

import Common.Constants;
import Data.Repository.*;
import Entities.*;
import Enumerations.CustomFeat;
import Enumerations.CustomItemProperty;
import Enumerations.ProfessionType;
import GameObject.ItemGO;
import GameObject.PlayerGO;
import Helper.ColorToken;
import Helper.ItemHelper;
import NWNX.NWNX_Funcs;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.nwnx.nwnx2.jvm.NWItemProperty;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.Scheduler;
import org.nwnx.nwnx2.jvm.constants.*;

import java.util.List;
import java.util.Objects;

@SuppressWarnings("UnusedDeclaration")
public class ProgressionSystem {

    // Configuration
    public static final int LevelCap = 50;
    private static final int SPEarnedOnLevelUp = 10;
    private static final int ResetCooldownDays = 3;
    public static final int ResetTokensMaxStock = 3;


    public static final int SkillType_INVALID                    = 0;
    public static final int SkillType_HP                         = 1;
    public static final int SkillType_INVENTORY_SPACE            = 2;
    public static final int SkillType_ARMOR                      = 3;
    public static final int SkillType_HANDGUN_PROFICIENCY        = 4;
    public static final int SkillType_SHOTGUN_PROFICIENCY        = 5;
    public static final int SkillType_RIFLE_PROFICIENCY          = 6;
    public static final int SkillType_SMG_PROFICIENCY            = 7;
    public static final int SkillType_MAGNUM_PROFICIENCY         = 8;
    public static final int SkillType_MELEE                      = 9;
    public static final int SkillType_SEARCH                     = 10;
    public static final int SkillType_HIDE                       = 11;
    public static final int SkillType_MOVE_SILENTLY              = 12;
    public static final int SkillType_FIRST_AID                  = 13;
    public static final int SkillType_LOCKPICKING                = 14;
    public static final int SkillType_MIXING                     = 15;
    public static final int SkillType_SPRING_ATTACK              = 16;
    public static final int SkillType_POWER_ATTACK               = 17;
    public static final int SkillType_AMBIDEXTERITY              = 18;
    public static final int SkillType_TWO_WEAPON_FIGHTING        = 19;
    public static final int SkillType_ITEM_REPAIR                = 20;
    public static final int SkillType_DISEASE_RESISTANCE         = 21;
    public static final int SkillType_HANDGUN_ACCURACY           = 22;
    public static final int SkillType_SHOTGUN_ACCURACY           = 23;
    public static final int SkillType_RIFLE_ACCURACY             = 24;
    public static final int SkillType_SMG_ACCURACY               = 25;
    public static final int SkillType_MAGNUM_ACCURACY            = 26;
    public static final int SkillType_NATURAL_REGENERATION       = 27;
    public static final int SkillType_COMPUTER_LITERACY          = 28;
    public static final int SkillType_SPEECH                     = 29;
    public static final int SkillType_STRENGTH                   = 30;
    public static final int SkillType_CONSTITUTION               = 31;
    public static final int SkillType_DEXTERITY                  = 32;
    public static final int SkillType_WISDOM                     = 33;
    public static final int SkillType_INTELLIGENCE               = 34;
    public static final int SkillType_CHARISMA                   = 35;
    public static final int SkillType_MANA                       = 36;
    public static final int SkillType_ABILITY_SLOTS              = 37;
    public static final int SkillType_HOLY_AFFINITY              = 38;
    public static final int SkillType_EVOCATION_AFFINITY         = 39;
    public static final int SkillType_ENHANCEMENT_AFFINITY       = 40;
    public static final int SkillType_RESEARCHING                = 41;



    public static PlayerEntity InitializePlayer(NWObject oPC)
    {
        if(!NWScript.getIsPC(oPC)) return null;
        PlayerGO pcGO = new PlayerGO(oPC);
        PlayerRepository playerRepo = new PlayerRepository();
        PlayerProgressionSkillsRepository playerSkillRepo = new PlayerProgressionSkillsRepository();
        PlayerEntity entity = playerRepo.getByUUID(pcGO.getUUID());

        NWScript.setXP(oPC, 0);
        int numberOfFeats = NWNX_Funcs.GetTotalKnownFeats(oPC);
        for(int currentFeat = 1; currentFeat <= numberOfFeats; currentFeat++)
        {
            NWNX_Funcs.SetKnownFeat(oPC, currentFeat, -1);
        }

        NWNX_Funcs.AddKnownFeat(oPC, Feat.ARMOR_PROFICIENCY_LIGHT, -1);
        NWNX_Funcs.AddKnownFeat(oPC, Feat.ARMOR_PROFICIENCY_MEDIUM, -1);
        NWNX_Funcs.AddKnownFeat(oPC, Feat.ARMOR_PROFICIENCY_HEAVY, -1);
        NWNX_Funcs.AddKnownFeat(oPC, Feat.SHIELD_PROFICIENCY, -1);
        NWNX_Funcs.AddKnownFeat(oPC, Feat.WEAPON_PROFICIENCY_EXOTIC, -1);
        NWNX_Funcs.AddKnownFeat(oPC, Feat.WEAPON_PROFICIENCY_MARTIAL, -1);
        NWNX_Funcs.AddKnownFeat(oPC, Feat.WEAPON_PROFICIENCY_SIMPLE, -1);
        NWNX_Funcs.AddKnownFeat(oPC, CustomFeat.Reload, -1);

        NWNX_Funcs.SetAbilityScore(oPC, Ability.STRENGTH, Constants.BaseStrength);
        NWNX_Funcs.SetAbilityScore(oPC, Ability.DEXTERITY, Constants.BaseDexterity);
        NWNX_Funcs.SetAbilityScore(oPC, Ability.CONSTITUTION, Constants.BaseConstitution);
        NWNX_Funcs.SetAbilityScore(oPC, Ability.WISDOM, Constants.BaseWisdom);
        NWNX_Funcs.SetAbilityScore(oPC, Ability.CHARISMA, Constants.BaseCharisma);
        NWNX_Funcs.SetAbilityScore(oPC, Ability.INTELLIGENCE, Constants.BaseIntelligence);

        NWNX_Funcs.SetMaxHitPointsByLevel(oPC, 1, Constants.BaseHitPoints);
        NWNX_Funcs.SetCurrentHitPoints(oPC, NWScript.getMaxHitPoints(oPC));

        for(int iCurSkill = 1; iCurSkill <= 27; iCurSkill++)
        {
            NWNX_Funcs.SetSkillRank(oPC, iCurSkill, 0);
        }
        NWNX_Funcs.SetSavingThrowBonus(oPC, SavingThrow.FORT, 0);
        NWNX_Funcs.SetSavingThrowBonus(oPC, SavingThrow.REFLEX, 0);
        NWNX_Funcs.SetSavingThrowBonus(oPC, SavingThrow.WILL, 0);

        int classID = NWScript.getClassByPosition(1, oPC);

        // Removing known spells doesn't work, so I clear memorized spells instead.
        for(int index = 0; index <= 255; index++)
        {
            NWNX_Funcs.ClearMemorizedSpell(oPC, classID, 0, index);
        }

        playerSkillRepo.deleteAllByPlayerUUID(pcGO.getUUID());
        entity.setUnallocatedSP(SPEarnedOnLevelUp * entity.getLevel());
        entity.setRegenerationTick(Constants.BaseHPRegenRate);
        entity.setHpRegenerationAmount(Constants.BaseHPRegenAmount);
        entity.setInventorySpaceBonus(0);
        entity.setMaxMana(0);
        entity.setCurrentManaTick(20);
        entity.setCurrentMana(0);

        playerRepo.save(entity);

        entity = ApplyProfessionStatBonuses(oPC);

        return entity;
    }

    static PlayerEntity ApplyProfessionStatBonuses(NWObject oPC)
    {
        PlayerRepository repo = new PlayerRepository();
        PlayerProgressionSkillsRepository skillRepo = new PlayerProgressionSkillsRepository();
        PlayerGO pcGO = new PlayerGO(oPC);
        PlayerEntity entity = repo.getByUUID(pcGO.getUUID());
        PlayerProgressionSkillEntity skillEntity;

        switch(entity.getProfessionID())
        {
            case ProfessionType.Vagabond:
                entity.setUnallocatedSP(entity.getUnallocatedSP() + 10);
                repo.save(entity);
                break;
            case ProfessionType.ForestWarden:
                NWNX_Funcs.SetMaxHitPointsByLevel(oPC, 1, NWScript.getMaxHitPoints(oPC) + 9);
                break;
            case ProfessionType.PoliceOfficer:
                skillEntity = skillRepo.GetByUUIDAndSkillID(pcGO.getUUID(), SkillType_HANDGUN_PROFICIENCY);
                skillEntity.setUpgradeLevel(skillEntity.getUpgradeLevel() + 1);
                skillRepo.save(skillEntity);
                break;
            case ProfessionType.Cartographer:
                skillEntity = skillRepo.GetByUUIDAndSkillID(pcGO.getUUID(), SkillType_SEARCH);
                skillEntity.setUpgradeLevel(skillEntity.getUpgradeLevel() + 1);
                skillRepo.save(skillEntity);
                entity = ApplyCustomUpgradeEffects(oPC, SkillType_SEARCH, skillEntity.getUpgradeLevel());
                break;
            case ProfessionType.HolyMage:
            case ProfessionType.EvocationMage:
                skillEntity = skillRepo.GetByUUIDAndSkillID(pcGO.getUUID(), SkillType_ABILITY_SLOTS);
                skillEntity.setUpgradeLevel(skillEntity.getUpgradeLevel() + 1);
                skillRepo.save(skillEntity);

                skillEntity = skillRepo.GetByUUIDAndSkillID(pcGO.getUUID(), SkillType_MANA);
                skillEntity.setUpgradeLevel(skillEntity.getUpgradeLevel() + 1);
                skillRepo.save(skillEntity);
                entity = ApplyCustomUpgradeEffects(oPC, SkillType_MANA, skillEntity.getUpgradeLevel());
                break;
        }

        return entity;
    }

    public static void GiveExperienceToPC(NWObject oPC, int amount)
    {
        if(amount <= 0 || NWScript.getIsDM(oPC) || !NWScript.getIsPC(oPC)) return;

        PlayerGO pcGO = new PlayerGO(oPC);
        PlayerRepository repo = new PlayerRepository();
        PlayerEntity entity = repo.getByUUID(pcGO.getUUID());
        entity.setExperience(entity.getExperience() + amount);

        ProgressionLevelRepository levelRepo = new ProgressionLevelRepository();
        ProgressionLevelEntity levelEntity = levelRepo.getByLevel(entity.getLevel());

        NWScript.floatingTextStringOnCreature("You earned experience points.", oPC, false);

        if(entity.getLevel() >= LevelCap && entity.getExperience() >= levelEntity.getExperience())
        {
            entity.setExperience(levelEntity.getExperience() - 1);
        }

        while(entity.getExperience() >= levelEntity.getExperience())
        {
            entity.setExperience(entity.getExperience() - levelEntity.getExperience());
            entity.setUnallocatedSP(entity.getUnallocatedSP() + SPEarnedOnLevelUp);
            entity.setLevel(entity.getLevel() + 1);
            NWScript.floatingTextStringOnCreature("You attained level " + entity.getLevel() + "!", oPC, false);

            levelEntity = levelRepo.getByLevel(entity.getLevel());
        }

        repo.save(entity);
    }

    public static void GiveLevelToPC(NWObject oPC, int givenLevels)
    {
        if(givenLevels <= 0 || NWScript.getIsDM(oPC) || !NWScript.getIsPC(oPC)) return;

        int currentLevel = GetPlayerLevel(oPC);
        if(currentLevel >= LevelCap) return;
        PlayerGO pcGO = new PlayerGO(oPC);
        PlayerRepository playerRepo = new PlayerRepository();
        ProgressionLevelRepository levelRepo = new ProgressionLevelRepository();
        PlayerEntity entity = playerRepo.getByUUID(pcGO.getUUID());
        List<ProgressionLevelEntity> levelList = levelRepo.getAll();

        for(int x = entity.getLevel(); x < entity.getLevel() + givenLevels; x++)
        {
            int exp = levelList.get(x-1).getExperience();
            GiveExperienceToPC(oPC, exp);
        }

    }

    public static void PurchaseSkillUpgrade(NWObject oPC, int skillID)
    {
        PlayerGO pcGO = new PlayerGO(oPC);
        PlayerRepository playerRepo = new PlayerRepository();
        PlayerEntity playerEntity = playerRepo.getByUUID(pcGO.getUUID());

        ProgressionSkillRepository skillRepo = new ProgressionSkillRepository();
        ProgressionSkillEntity skillEntity = skillRepo.getByID(skillID);

        PlayerProgressionSkillsRepository playerSkillRepo = new PlayerProgressionSkillsRepository();
        PlayerProgressionSkillEntity playerSkillEntity = playerSkillRepo.GetByUUIDAndSkillID(pcGO.getUUID(), skillID);

        int requiredSP = skillEntity.getInitialPrice() + playerSkillEntity.getUpgradeLevel();
        int upgradeCap = playerSkillEntity.isSoftCapUnlocked() ? skillEntity.getMaxUpgrades() : skillEntity.getSoftCap();

        if(playerSkillEntity.getUpgradeLevel() >= upgradeCap)
        {
            NWScript.sendMessageToPC(oPC, ColorToken.Red() + "You cannot increase that skill any further." + ColorToken.End());
            return;
        }

        if(playerEntity.getUnallocatedSP() < requiredSP)
        {
            NWScript.sendMessageToPC(oPC, ColorToken.Red() + "You do not have enough SP to make that purchase." + ColorToken.End());
            return;
        }

        playerEntity.setUnallocatedSP(playerEntity.getUnallocatedSP() - requiredSP);
        playerSkillEntity.setUpgradeLevel(playerSkillEntity.getUpgradeLevel() + 1);
        playerRepo.save(playerEntity);
        playerSkillRepo.save(playerSkillEntity);

        ApplyCustomUpgradeEffects(oPC, skillID, playerSkillEntity.getUpgradeLevel());
    }


    private static PlayerEntity ApplyCustomUpgradeEffects(NWObject oPC, int skillID, int rank)
    {
        PlayerGO pcGO = new PlayerGO(oPC);
        PlayerRepository repo = new PlayerRepository();
        PlayerEntity entity = repo.getByUUID(pcGO.getUUID());

        switch (skillID)
        {
            case SkillType_HP:
                NWNX_Funcs.SetMaxHitPointsByLevel(oPC, 1, NWNX_Funcs.GetMaxHitPointsByLevel(oPC, 1) + 3);
                break;
            case SkillType_STRENGTH:
                NWNX_Funcs.ModifyAbilityScore(oPC, Ability.STRENGTH, 1);
                if(rank % 2 != 0)
                {
                    entity.setInventorySpaceBonus(entity.getInventorySpaceBonus() + 1);
                }
                break;
            case SkillType_CONSTITUTION:
                NWNX_Funcs.ModifyAbilityScore(oPC, Ability.CONSTITUTION, 1);
                break;
            case SkillType_DEXTERITY:
                NWNX_Funcs.ModifyAbilityScore(oPC, Ability.DEXTERITY, 1);
                if(rank % 2 != 0)
                {
                    NWNX_Funcs.ModifySkillRank(oPC, Skill.TUMBLE, 1);
                }
                break;
            case SkillType_WISDOM:
                NWNX_Funcs.ModifyAbilityScore(oPC, Ability.WISDOM, 1);
                break;
            case SkillType_INTELLIGENCE:
                NWNX_Funcs.ModifyAbilityScore(oPC, Ability.INTELLIGENCE, 1);
                break;
            case SkillType_CHARISMA:
                NWNX_Funcs.ModifyAbilityScore(oPC, Ability.CHARISMA, 1);
                break;
            case SkillType_SEARCH:
                NWNX_Funcs.ModifySkillRank(oPC, Skill.SEARCH, 1);
                break;
            case SkillType_HIDE:
                NWNX_Funcs.ModifySkillRank(oPC, Skill.HIDE, 1);
                break;
            case SkillType_MOVE_SILENTLY:
                NWNX_Funcs.ModifySkillRank(oPC, Skill.MOVE_SILENTLY, 1);
                break;
            case SkillType_SPRING_ATTACK:
                NWNX_Funcs.AddKnownFeat(oPC, Feat.SPRING_ATTACK, 0);
                break;
            case SkillType_POWER_ATTACK:
                NWNX_Funcs.AddKnownFeat(oPC, Feat.POWER_ATTACK, 0);
                break;
            case SkillType_AMBIDEXTERITY:
                NWNX_Funcs.AddKnownFeat(oPC, Feat.AMBIDEXTERITY, 0);
                break;
            case SkillType_TWO_WEAPON_FIGHTING:
                NWNX_Funcs.AddKnownFeat(oPC, Feat.TWO_WEAPON_FIGHTING, 0);
                break;
            case SkillType_MANA:
                entity.setMaxMana(entity.getMaxMana() + 5);
                repo.save(entity);
                break;

        }

        return entity;
    }

    public static boolean DoesPlayerMeetItemSkillRequirements(NWObject oPC, NWObject oItem)
    {
        boolean canWear = true;
        PlayerGO pcGO = new PlayerGO(oPC);
        ProgressionSkillRepository skillRepo = new ProgressionSkillRepository();
        PlayerProgressionSkillsRepository repo = new PlayerProgressionSkillsRepository();

        NWItemProperty[] itemProperties = NWScript.getItemProperties(oItem);
        for(NWItemProperty ip : itemProperties)
        {
            if(NWScript.getItemPropertyType(ip) == CustomItemProperty.SkillRequirement)
            {
                int skillID = NWScript.getItemPropertySubType(ip);
                int skillRequired = NWScript.getItemPropertyCostTableValue(ip);
                PlayerProgressionSkillEntity playerSkillEntity = repo.GetByUUIDAndSkillID(pcGO.getUUID(), skillID);
                ProgressionSkillEntity skillEntity = skillRepo.getByID(skillID);

                if(playerSkillEntity == null || playerSkillEntity.getUpgradeLevel() < skillRequired)
                {
                    canWear = false;
                    NWScript.sendMessageToPC(oPC, ColorToken.Red() + "You lack the required " + skillEntity.getName() + " skill to equip that item. (Required: " + skillRequired + ")" + ColorToken.End());
                }
            }
        }

        return canWear;
    }

    public static int GetPlayerLevel(NWObject oPC)
    {
        PlayerGO pcGO = new PlayerGO(oPC);
        PlayerRepository repo = new PlayerRepository();
        PlayerEntity entity = repo.getByUUID(pcGO.getUUID());

        return entity.getLevel();
    }

    public static int GetPlayerSkillLevel(NWObject oPC, int skillID)
    {
        PlayerGO pcGO = new PlayerGO(oPC);
        PlayerProgressionSkillsRepository repo = new PlayerProgressionSkillsRepository();
        PlayerProgressionSkillEntity entity = repo.GetByUUIDAndSkillID(pcGO.getUUID(), skillID);

        return entity == null ? 0 : entity.getUpgradeLevel();
    }

    public static void OnModuleEquip()
    {
        final NWObject oPC = NWScript.getPCItemLastEquippedBy();

        if(!NWScript.getIsPC(oPC) || NWScript.getIsDM(oPC)) return;

        final NWObject oItem = NWScript.getPCItemLastEquipped();
        String resref = NWScript.getResRef(oItem);
        String name = NWScript.getName(oItem, false);

        if(Objects.equals(name, "PC Properties") || !NWScript.getIsObjectValid(oItem)) return;

        if(!DoesPlayerMeetItemSkillRequirements(oPC, oItem))
        {
            Scheduler.delay(oPC, 100, () -> {

                for(NWObject item : NWScript.getItemsInInventory(oPC))
                {
                    if(NWScript.getBaseItemType(item) == BaseItem.ARROW)
                        NWScript.destroyObject(item, 0.0f);
                }

                NWScript.clearAllActions(false);
                NWScript.actionUnequipItem(oItem);
            });
        }
    }

    public static void OnModuleEnter()
    {
        NWObject oPC = NWScript.getEnteringObject();

        if(!NWScript.getIsPC(oPC) || NWScript.getIsDM(oPC)) return;
        PlayerGO pcGO = new PlayerGO(oPC);
        PlayerRepository playerRepo = new PlayerRepository();
        ForcedSPResetRepository spRepo = new ForcedSPResetRepository();

        PlayerEntity playerEntity = playerRepo.getByUUID(pcGO.getUUID());
        ForcedSPResetEntity spEntity = spRepo.GetLatestForcedSPResetDate();

        if(spEntity == null) return;

        if(playerEntity.getDateLastForcedSPReset() == null ||
                playerEntity.getDateLastForcedSPReset().before(spEntity.getDateOfReset()))
        {
            PerformSkillReset(oPC, true);
            playerEntity = playerRepo.getByUUID(pcGO.getUUID());
            playerEntity.setDateLastForcedSPReset(DateTime.now(DateTimeZone.UTC).toDate());
            playerRepo.save(playerEntity);
        }

    }

    public static boolean CanResetSkills(NWObject oPC)
    {
        PlayerGO pcGO = new PlayerGO(oPC);
        PlayerRepository repo = new PlayerRepository();
        PlayerEntity entity = repo.getByUUID(pcGO.getUUID());
        DateTime resetDate = entity.getNextSPResetDate() == null ?
                DateTime.now().minusSeconds(1) :
                new DateTime(entity.getNextSPResetDate());

        return DateTime.now().isAfter(resetDate) &&
                entity.getResetTokens() > 0;

    }

    public static void PerformSkillReset(final NWObject oPC, boolean forceReset)
    {
        PlayerGO pcGO = new PlayerGO(oPC);

        if(!CanResetSkills(oPC) && !forceReset)
        {
            NWScript.sendMessageToPC(oPC, "You cannot reset your skills yet.");
            return;
        }

        pcGO.UnequipAllItems();
        PlayerRepository repo = new PlayerRepository();
        PlayerEntity entity = InitializePlayer(oPC);

        if(!forceReset)
        {
            DateTime nextReset = DateTime.now().plusDays(ResetCooldownDays);
            entity.setResetTokens(entity.getResetTokens() - 1);
            entity.setNextSPResetDate(nextReset.toDate());
            entity.setNumberOfSPResets(entity.getNumberOfSPResets() + 1);
        }

        repo.save(entity);

        for(int index = 1; index <= 10; index++)
        {
            MagicSystem.UnequipAbility(oPC, index);
        }

        if(forceReset)
        {
            Scheduler.delay(oPC, 8000, () -> NWScript.floatingTextStringOnCreature(ColorToken.Green() + "You have received a free skill reset due to changes in the skill list." + ColorToken.End(), oPC, false));
        }
        else
        {
            NWScript.floatingTextStringOnCreature(ColorToken.Green() + "Skill reset completed successfully." + ColorToken.End(), oPC, false);
        }

    }

    public static int GetLevelExperienceRequired(int level)
    {
        ProgressionLevelRepository repo = new ProgressionLevelRepository();
        ProgressionLevelEntity entity = repo.getByLevel(level);

        return entity.getExperience();
    }

}
