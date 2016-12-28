package Event.Area;

import Entities.PlayerEntity;
import GameObject.PlayerGO;
import Common.IScriptEventHandler;
import Data.Repository.PlayerRepository;
import GameSystems.KeyItemSystem;
import GameSystems.MigrationSystem;
import GameSystems.SpawnSystem;
import org.nwnx.nwnx2.jvm.*;

@SuppressWarnings("UnusedDeclaration")
public class OnEnter implements IScriptEventHandler {
    @Override
    public void runScript(NWObject oArea) {
        SpawnSystem spawnSystem = new SpawnSystem();

        NWObject oPC = NWScript.getEnteringObject();
        MigrationSystem.OnAreaEnter(oPC);

        // Temporary Sanctuary Effects
        NWScript.executeScript("sanctuary", oArea);
        LoadLocation(oPC, oArea);
        SaveLocation(oPC, oArea);
        spawnSystem.ZSS_OnAreaEnter(oArea);
        // Initialize camera in designated areas.
        NWScript.executeScript("initialize_camer", oArea);

        // Save characters
        if(NWScript.getIsObjectValid(oPC) && NWScript.getIsPC(oPC) && !NWScript.getIsDM(oPC)) NWScript.exportSingleCharacter(oPC);
    }

    private void SaveLocation(NWObject oPC, NWObject oArea)
    {
        if(!NWScript.getIsPC(oPC) || NWScript.getIsDM(oPC)) return;

        String sTag = NWScript.getTag(oArea);
        if(!sTag.equals("ooc_area"))
        {
            PlayerGO pcGO = new PlayerGO(oPC);
            NWLocation location = NWScript.getLocation(oPC);
            PlayerRepository repo = new PlayerRepository();
            PlayerEntity entity = repo.getByUUID(pcGO.getUUID());
            entity.setLocationAreaTag(sTag);
            entity.setLocationX(location.getX());
            entity.setLocationY(location.getY());
            entity.setLocationZ(location.getZ());
            entity.setLocationOrientation(NWScript.getFacing(oPC));

            repo.save(entity);
        }
    }

    private void LoadLocation(NWObject oPC, NWObject oArea)
    {
        if(!NWScript.getIsPC(oPC) || NWScript.getIsDM(oPC)) return;

        if(NWScript.getTag(oArea).equals("ooc_area"))
        {
            PlayerGO pcGO = new PlayerGO(oPC);
            PlayerEntity entity = new PlayerRepository().getByUUID(pcGO.getUUID());
            NWObject area = NWScript.getObjectByTag(entity.getLocationAreaTag(), 0);
            final NWLocation location = new NWLocation(area,
                    entity.getLocationX(),
                    entity.getLocationY(),
                    entity.getLocationZ(),
                    entity.getLocationOrientation());

            Scheduler.assign(oPC, new Runnable() {
                @Override
                public void run() {
                    NWScript.actionJumpToLocation(location);
                }
            });
        }
    }

    private void ShowMap(NWObject oPC, NWObject oArea)
    {
        int keyItemID = NWScript.getLocalInt(oArea, "MAP_ID");
        boolean areaShowsMap = NWScript.getLocalInt(oArea, "SHOW_MAP") == 1;
        boolean hasKeyItem = KeyItemSystem.PlayerHasKeyItem(oPC, keyItemID);

        if(areaShowsMap || hasKeyItem)
        {
            NWScript.exploreAreaForPlayer(oArea, oPC, true);
        }
    }

}
