package GameSystems;

import Data.Repository.LootTableRepository;
import Data.Repository.SpawnTableRepository;
import Entities.LootTableEntity;
import Entities.LootTableItemEntity;
import Entities.SpawnTableCreatureEntity;
import Entities.SpawnTableEntity;
import GameObject.ItemGO;
import GameSystems.Models.SpawnModel;
import Helper.LocalArray;
import Helper.MathHelper;
import NWNX.NWNX_Funcs;
import NWNX.NWNX_TMI;
import org.nwnx.nwnx2.jvm.NWLocation;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.Scheduler;
import org.nwnx.nwnx2.jvm.constants.ObjectType;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class SpawnSystem {

    // The name of the variable which determines which group to pull spawn resrefs from.
    // This variable is stored on the area object
    private final String SpawnTable = "SPAWN_TABLE";

    // The name of the variable which determines how many spawn points there are in an area
    // This variable is stored on the area object
    private final String SpawnWaypointCount = "ZSS_SPAWN";

    // The name of the variable which determines how many respawn points there are in an area
    // This variable is stored on the area object
    private final String RespawnWaypointCount = "ZSS_RESPAWN";

    // The name of the array which stores spawn waypoint locations
    private final String SpawnWaypointLocationArray = "ZSS_SPAWN_WAYPOINT_LOCATION_ARRAY";

    // The name of the array which stores respawn waypoint locations
    private final String RespawnWaypointLocationArray = "ZSS_RESPAWN_WAYPOINT_LOCATION_ARRAY";

    // Name of the variable which tells the system whether or not a zombie was spawned by the system
    // as opposed to being spawned by a DM.
    private final String IsCreatureSpawned = "ZSS_ZOMBIE_SPAWNED";

    // Name of the variable which keeps track of the number of players in an area
    private final String PlayerCount = "ZSS_PLAYER_COUNT";


    private void CreateCreature(String creatureResref, NWLocation waypointLocation, String areaResref, int lootTableID, int difficultyRating)
    {
        NWObject oArea = null;

        NWObject area = NWNX_Funcs.GetFirstArea();
        while(NWScript.getIsObjectValid(area))
        {
            if(Objects.equals(NWScript.getResRef(area), areaResref))
            {
                oArea = area;
                break;
            }

            oArea = NWNX_Funcs.GetNextArea();
        }

        if(oArea == null) return;

        // Only respawn if there's PCs in the area
        if(NWScript.getLocalInt(oArea, PlayerCount) > 0)
        {
            final NWObject creature = NWScript.createObject(ObjectType.CREATURE, creatureResref, waypointLocation, false, "");
            NWScript.setLocalInt(creature, IsCreatureSpawned, 1);
            CreateLoot(creature, lootTableID);
            NWScript.setLocalInt(creature, "DIFFICULTY_RATING", difficultyRating);

            Scheduler.assign(creature, new Runnable() {
                @Override
                public void run() {
                    NWScript.setFacing(0.01f * NWScript.random(3600));
                    NWScript.actionRandomWalk();
                }
            });
        }
    }

    public void OnAreaEnter(NWObject oArea)
    {
        NWObject oPC = NWScript.getEnteringObject();

        if(!NWScript.getIsPC(oPC)) return;

        int iZombieCount = NWScript.getLocalInt(oArea, "ZSS_COUNT");
        int iWaypointCount = NWScript.getLocalInt(oArea, SpawnWaypointCount);
        int spawnTableID = NWScript.getLocalInt(oArea, SpawnTable);
        if(spawnTableID < 1) spawnTableID = 1;


        // Determine if this is the first PC in the area
        int iPCCount = 0;
        NWObject[] players = NWScript.getPCs();

        for(NWObject pc : players)
        {
            if(oArea.equals(NWScript.getArea(pc)))
            {
                iPCCount++;
            }
        }

        // Only spawn zombies if this is the first PC in the area
        if(iPCCount == 1)
        {
            for(int iCurrentZombie = 1; iCurrentZombie <= iZombieCount; iCurrentZombie++)
            {
                int iWaypoint;

                // Ensure that at least one zombie will be placed at each spawn point
                if(iCurrentZombie > iWaypointCount)
                {
                    iWaypoint = NWScript.random(iWaypointCount) + 1;
                }
                else
                {
                    iWaypoint = iCurrentZombie;
                }

                SpawnModel model = GetCreatureToSpawn(spawnTableID);
                NWLocation lLocation = LocalArray.GetLocalArrayLocation(oArea, SpawnWaypointLocationArray, iWaypoint);

                final NWObject creature = NWScript.createObject(ObjectType.CREATURE, model.getResref(), lLocation, false, "");
                NWScript.setLocalInt(creature, IsCreatureSpawned, 1);
                CreateLoot(creature, model.getLootTableID());
                NWScript.setLocalInt(creature, "DIFFICULTY_RATING", model.getDifficultyRating());

                Scheduler.assign(creature, new Runnable() {
                    @Override
                    public void run() {
                        NWScript.setFacing(0.01f * NWScript.random(3600));
                    }
                });
            }
        }

        // Update PC counter
        NWScript.setLocalInt(oArea, PlayerCount, iPCCount);
    }

    public void OnAreaExit(NWObject oArea)
    {
        NWObject oPC = NWScript.getExitingObject();

        if(!NWScript.getIsPC(oPC)) return;

        int iPCCount = 0;
        NWObject[] players = NWScript.getPCs();

        for(NWObject pc : players)
        {
            if(oArea.equals(NWScript.getArea(pc)))
            {
                iPCCount++;
            }
        }

        // Last PC exited the area. Despawn all zombies.
        if(iPCCount <= 0)
        {
            NWObject[] objects = NWScript.getObjectsInArea(oArea);

            for(NWObject currentObject : objects)
            {
                if(NWScript.getLocalInt(currentObject, IsCreatureSpawned) == 1)
                {
                    NWScript.destroyObject(currentObject, 0.0f);
                }
            }
        }

        // Update PC counter
        NWScript.setLocalInt(oArea, PlayerCount, iPCCount);
    }

    public void OnModuleLoad()
    {
        int tmiLimit = NWNX_TMI.GetTMILimit();
        NWNX_TMI.SetTMILimit(7000000);

        NWObject oArea = NWNX_Funcs.GetFirstArea();

        while(NWScript.getIsObjectValid(oArea))
        {
            String sSpawnID = NWScript.getResRef(oArea);
            int iSpawnCount = 0;
            int iRespawnCount = 0;

            NWObject[] areaObjects = NWScript.getObjectsInArea(oArea);
            for(NWObject obj : areaObjects)
            {
                String sResref = NWScript.getResRef(obj);
                if(Objects.equals(sResref, "zombie_spawn"))
                {
                    iSpawnCount++;
                    LocalArray.SetLocalArrayLocation(oArea, SpawnWaypointLocationArray, iSpawnCount, NWScript.getLocation(obj));
                }
                // Respawn waypoint
                else if(Objects.equals(sResref, "zombie_respawn"))
                {
                    iRespawnCount++;
                    LocalArray.SetLocalArrayLocation(oArea, RespawnWaypointLocationArray, iRespawnCount, NWScript.getLocation(obj));
                }
            }

            // Mark the number of spawn and respawn waypoints for this area
            NWScript.setLocalInt(oArea, SpawnWaypointCount, iSpawnCount);
            NWScript.setLocalInt(oArea, RespawnWaypointCount, iRespawnCount);
            // Mark the unique identifier (the resref)
            NWScript.setLocalString(oArea, "ZSS_WAYPOINT_NAME", sSpawnID);

            oArea = NWNX_Funcs.GetNextArea();
        }

        // Set the TMI limit back to normal
        NWNX_TMI.SetTMILimit(tmiLimit);
    }

    public void OnCreatureDeath(NWObject creature)
    {
        final NWObject oArea = NWScript.getArea(creature);
        final String areaResref = NWScript.getResRef(oArea);
        int iWaypointCount = NWScript.getLocalInt(oArea, RespawnWaypointCount);
        if(NWScript.getLocalInt(creature, IsCreatureSpawned) != 1 || iWaypointCount <= 0) return;

        int iPCCount = NWScript.getLocalInt(oArea, PlayerCount);
        int iGroupID = NWScript.getLocalInt(oArea, SpawnTable);
        if(iGroupID <= 0) iGroupID = 1;

        if(iPCCount > 0)
        {
            int iWaypoint = NWScript.random(iWaypointCount) + 1;
            final SpawnModel model = GetCreatureToSpawn(iGroupID);
            final NWLocation lLocation = LocalArray.GetLocalArrayLocation(oArea, RespawnWaypointLocationArray, iWaypoint);

            // 120 * 1000 = 2 Minutes
            Scheduler.delay(creature, 120 * 1000, new Runnable() {
                @Override
                public void run() {
                    CreateCreature(model.getResref(), lLocation, areaResref, model.getLootTableID(), model.getDifficultyRating());
                }
            });

        }
    }


    private SpawnModel GetCreatureToSpawn(int spawnTableID)
    {
        SpawnTableRepository repo = new SpawnTableRepository();
        SpawnTableEntity entity = repo.GetBySpawnTableID(spawnTableID);
        int weights[] = new int[entity.getSpawnTableCreatures().size()];

        for(int x = 0; x < entity.getSpawnTableCreatures().size(); x++)
        {
            weights[x] = entity.getSpawnTableCreatures().get(x).getWeight();
        }

        int randomIndex = MathHelper.GetRandomWeightedIndex(weights);
        SpawnTableCreatureEntity creatureEntity = entity.getSpawnTableCreatures().get(randomIndex);

        return new SpawnModel(creatureEntity.getResref(), creatureEntity.getLootTableID(), creatureEntity.getDifficultyRating());
    }

    private void CreateLoot(NWObject creature, Integer lootTableID)
    {
        if(lootTableID == null || !NWScript.getIsObjectValid(creature)) return;
        if(ThreadLocalRandom.current().nextInt(0, 100) > 20) return;

        LootTableRepository repo = new LootTableRepository();
        LootTableEntity entity = repo.GetByLootTableID(lootTableID);
        int[] weights = new int[entity.getLootTableItems().size()];

        for(int x = 0; x < entity.getLootTableItems().size(); x++)
        {
            weights[x] = entity.getLootTableItems().get(x).getWeight();
        }

        int randomIndex = MathHelper.GetRandomWeightedIndex(weights);
        LootTableItemEntity itemEntity = entity.getLootTableItems().get(randomIndex);
        int quantity = ThreadLocalRandom.current().nextInt(1, itemEntity.getMaxQuantity());

        if(!itemEntity.getResref().equals("") && quantity > 0)
        {
            NWObject item = NWScript.createItemOnObject(itemEntity.getResref(), creature, quantity, "");
            ItemGO itemGO = new ItemGO(item);
            itemGO.setDurability(ThreadLocalRandom.current().nextInt(1, 20));
        }
    }

}
