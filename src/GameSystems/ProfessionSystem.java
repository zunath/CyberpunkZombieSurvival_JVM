package GameSystems;

import Data.Repository.PlayerRepository;
import Data.Repository.ProfessionRepository;
import Entities.PlayerEntity;
import Entities.ProfessionEntity;
import Enumerations.AbilityType;
import Enumerations.ProfessionType;
import GameObject.PlayerGO;
import NWNX.NWNX_Funcs;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;

import java.util.List;
import java.util.Objects;

public class ProfessionSystem {

    public static void OnModuleEnter()
    {
        PlayerRepository repo = new PlayerRepository();
        NWObject oPC = NWScript.getEnteringObject();
        if(!NWScript.getIsPC(oPC) || NWScript.getIsDM(oPC)) return;

        PlayerGO pcGO = new PlayerGO(oPC);
        NWObject token = NWScript.getItemPossessedBy(oPC, "prof_token");
        PlayerEntity entity = repo.getByUUID(pcGO.getUUID());

        if(entity.getProfessionID() > 0) return;

        if(!NWScript.getIsObjectValid(token))
        {
            NWScript.createItemOnObject("prof_token", oPC, 1, "");
        }
    }

    public static void OnAreaEnter(NWObject oArea)
    {
        if(!NWScript.getIsAreaInterior(oArea))
        {
            PlayerRepository repo = new PlayerRepository();
            NWObject oPC = NWScript.getEnteringObject();
            if(!NWScript.getIsPC(oPC) || NWScript.getIsDM(oPC)) return;

            PlayerGO pcGO = new PlayerGO(oPC);
            PlayerEntity entity = repo.getByUUID(pcGO.getUUID());

            if(entity.getProfessionID() == ProfessionType.Cartographer)
            {
                NWScript.exploreAreaForPlayer(oArea, oPC, true);
            }
        }
    }

    public static List<ProfessionEntity> GetActiveProfessions()
    {
        ProfessionRepository repo = new ProfessionRepository();
        return repo.GetActiveProfessions();
    }

    public static void SelectProfession(NWObject oPC, ProfessionEntity entity)
    {
        PlayerRepository repo = new PlayerRepository();
        PlayerGO pcGO = new PlayerGO(oPC);
        PlayerEntity pcEntity = repo.getByUUID(pcGO.getUUID());
        pcEntity.setProfessionID(entity.getProfessionID());
        repo.save(pcEntity);
        ApplyProfessionBonuses(oPC, entity.getProfessionID());

        NWObject token = NWScript.getItemPossessedBy(oPC, "prof_token");
        NWScript.destroyObject(token, 0.0f);

        NWScript.floatingTextStringOnCreature("Profession confirmed!", oPC, false);
    }

    private static void ApplyProfessionBonuses(NWObject oPC, int professionID)
    {
        switch(professionID)
        {
            case ProfessionType.Merchant:
                // No actions necessary - check is made in the inventory system.
                break;
            case ProfessionType.Vagabond:
                // No actions necessary - progression system handles the bonus
                break;
            case ProfessionType.ForestWarden:
                NWScript.createItemOnObject("cleiyraforest_ke", oPC, 1, "");
                // HP upgrade is handled by progression system
                break;
            case ProfessionType.PoliceOfficer:
                NWScript.createItemOnObject("cop_gun", oPC, 1, "");
                // Handgun proficiency upgrade is handled by progression system
                // +3 critical chance is handled by combat system
                break;
            case ProfessionType.Cartographer:
                // Search Upgrade is handled by progression system.
                // Additional search chance is handled by search system.
                break;
            case ProfessionType.HolyMage:
                // Mana upgrade is handled by progression system.
                MagicSystem.LearnAbility(oPC, NWObject.INVALID, AbilityType.Cure);
                break;
            case ProfessionType.EvocationMage:
                // Mana upgrades is handled by progression system.
                MagicSystem.LearnAbility(oPC, NWObject.INVALID, AbilityType.Flame);
                break;
        }

        ProgressionSystem.ApplyProfessionStatBonuses(oPC);
    }


}
