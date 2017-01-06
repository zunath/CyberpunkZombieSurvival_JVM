package Creature.Zombie;

import Entities.PlayerEntity;
import GameObject.PlayerGO;
import Common.IScriptEventHandler;
import Data.Repository.PlayerRepository;
import GameSystems.ProgressionSystem;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

@SuppressWarnings("unused")
public class Zombie_OnDeath implements IScriptEventHandler {
    @Override
    public void runScript(NWObject objSelf) {

        NWScript.executeScript("nw_c2_default7", objSelf);
        NWScript.setIsDestroyable(false, true, false);
        NWScript.executeScript("gb_loot_corpse", objSelf);
        IncrementKillCount();
        GiveExperienceToPCParty(objSelf);
    }

    private void IncrementKillCount()
    {
        NWObject oPC = NWScript.getLastKiller();
        if(!NWScript.getIsPC(oPC) || NWScript.getIsDM(oPC)) return;

        PlayerGO pcGO = new PlayerGO(oPC);
        PlayerRepository repo = new PlayerRepository();
        PlayerEntity entity = repo.getByUUID(pcGO.getUUID());

        if(entity != null)
        {
            entity.setZombieKillCount(entity.getZombieKillCount() + 1);
            repo.save(entity);

            if(entity.getZombieKillCount() % 10 == 0)
            {
                ProgressionSystem.GiveExperienceToPC(oPC, 200);
            }
        }
    }

    private void GiveExperienceToPCParty(NWObject objSelf)
    {
        NWObject oPC = NWScript.getLastKiller();
        if(!NWScript.getIsPC(oPC) || NWScript.getIsDM(oPC)) return;

        String zombieAreaResref = NWScript.getResRef(NWScript.getArea(objSelf));
        NWObject[] partyMembers = NWScript.getFactionMembers(oPC, true);

        for(NWObject member : partyMembers)
        {
            String areaResref = NWScript.getResRef(NWScript.getArea(member));
            float distance = NWScript.getDistanceBetween(member, objSelf);
            if(distance <= 20.0f && Objects.equals(areaResref, zombieAreaResref))
            {
                int level = ProgressionSystem.GetPlayerLevel(member);

                if(level <= 20)
                {
                    int exp = ThreadLocalRandom.current().nextInt(50, 100);
                    ProgressionSystem.GiveExperienceToPC(member, exp);
                }
            }

        }


    }

}
