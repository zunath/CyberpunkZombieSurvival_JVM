package GameSystems;

import Data.Repository.SpawnTableRepository;
import Entities.SpawnTableCreatureEntity;
import Entities.SpawnTableEntity;
import Helper.LocalArray;
import NWNX.NWNX_Funcs;
import NWNX.NWNX_TMI;
import org.nwnx.nwnx2.jvm.NWLocation;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.Scheduler;
import org.nwnx.nwnx2.jvm.constants.ObjectType;

import java.util.Objects;

public class SpawnSystem {

    // The amount of time between spawns, in seconds
    // Default: 120 (2 minutes)
    final int ZSS_SPAWN_DELAY = 120;

    // The name of the variable which determines the name of the spawn points in an area
    // This variable is stored on the area object
    final String ZSS_WAYPOINT_NAME = "ZSS_WAYPOINT_NAME";

    // The name of the variable which determines which group to pull spawn resrefs from.
    // This variable is stored on the area object
    final String ZSS_GROUP_ID = "SPAWN_TABLE";

    // The name of the variable which determines how many spawn points there are in an area
    // This variable is stored on the area object
    final String ZSS_SPAWN_WAYPOINT_COUNT = "ZSS_SPAWN";

    // The name of the variable which determines how many respawn points there are in an area
    // This variable is stored on the area object
    final String ZSS_RESPAWN_WAYPOINT_COUNT = "ZSS_RESPAWN";

    // The name of the array which stores spawn waypoint locations
    final String ZSS_SPAWN_WAYPOINT_LOCATION_ARRAY = "ZSS_SPAWN_WAYPOINT_LOCATION_ARRAY";

    // The name of the array which stores respawn waypoint locations
    final String ZSS_RESPAWN_WAYPOINT_LOCATION_ARRAY = "ZSS_RESPAWN_WAYPOINT_LOCATION_ARRAY";

    // The name of the variable which stores the number of zombies to spawn in an area
    // This variable is stored on the area object
    final String ZSS_ZOMBIE_COUNT = "ZSS_COUNT";

    // Name of the variable which tells the system whether or not a zombie was spawned by the system
    // as opposed to being spawned by a DM.
    final String ZSS_ZOMBIE_SPAWNED = "ZSS_ZOMBIE_SPAWNED";

    // Name of the variable which keeps track of the number of players in an area
    final String ZSS_PLAYER_COUNT = "ZSS_PLAYER_COUNT";

    // Resref of the spawn waypoints.
    final String ZSS_SPAWN_WAYPOINT_RESREF = "zombie_spawn";

    // Resref of the respawn waypoints.
    final String ZSS_RESPAWN_WAYPOINT_RESREF = "zombie_respawn";




    private void ZSS_DelayCreateZombie(String sZombieResref, NWLocation lWaypointLocation, NWObject oArea)
    {
        // Only respawn if there's PCs in the area
        if(NWScript.getLocalInt(oArea, ZSS_PLAYER_COUNT) > 0)
        {
            final NWObject oZombie = NWScript.createObject(ObjectType.CREATURE, sZombieResref, lWaypointLocation, false, "");
            // Just to make sure this zombie was spawned by the spawn system and not by a DM
            NWScript.setLocalInt(oZombie, ZSS_ZOMBIE_SPAWNED, 1);

            Scheduler.assign(oZombie, new Runnable() {
                @Override
                public void run() {
                    NWScript.setFacing(0.01f * NWScript.random(3600));
                    NWScript.actionRandomWalk();
                }
            });
        }
    }

    public void ZSS_OnAreaEnter(NWObject oArea)
    {
        NWObject oPC = NWScript.getEnteringObject();

        if(!NWScript.getIsPC(oPC)) return;

        int iZombieCount = NWScript.getLocalInt(oArea, ZSS_ZOMBIE_COUNT);
        int iWaypointCount = NWScript.getLocalInt(oArea, ZSS_SPAWN_WAYPOINT_COUNT);
        int iGroupID = NWScript.getLocalInt(oArea, ZSS_GROUP_ID);
        if(iGroupID < 1) iGroupID = 1;


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

                String sResref = ZSS_GetZombieToSpawn(iGroupID);
                NWLocation lLocation = LocalArray.GetLocalArrayLocation(oArea, ZSS_SPAWN_WAYPOINT_LOCATION_ARRAY, iWaypoint);

                final NWObject oZombie = NWScript.createObject(ObjectType.CREATURE, sResref, lLocation, false, "");

                // Mark zombie as spawned via the system (as opposed from a DM)
                NWScript.setLocalInt(oZombie, ZSS_ZOMBIE_SPAWNED, 1);

                // NWScript.randomize facing
                Scheduler.assign(oZombie, new Runnable() {
                    @Override
                    public void run() {
                        NWScript.setFacing(0.01f * NWScript.random(3600));
                    }
                });
            }
        }

        // Update PC counter
        NWScript.setLocalInt(oArea, ZSS_PLAYER_COUNT, iPCCount);
    }

    public void ZSS_OnAreaExit(NWObject oArea)
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
                if(NWScript.getLocalInt(currentObject, ZSS_ZOMBIE_SPAWNED) == 1)
                {
                    NWScript.destroyObject(currentObject, 0.0f);
                }
            }
        }

        // Update PC counter
        NWScript.setLocalInt(oArea, ZSS_PLAYER_COUNT, iPCCount);
    }

    public void ZSS_OnModuleLoad()
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
                // Spawn waypoint
                if(Objects.equals(sResref, ZSS_SPAWN_WAYPOINT_RESREF))
                {
                    iSpawnCount++;
                    LocalArray.SetLocalArrayLocation(oArea, ZSS_SPAWN_WAYPOINT_LOCATION_ARRAY, iSpawnCount, NWScript.getLocation(obj));
                }
                // Respawn waypoint
                else if(Objects.equals(sResref, ZSS_RESPAWN_WAYPOINT_RESREF))
                {
                    iRespawnCount++;
                    LocalArray.SetLocalArrayLocation(oArea, ZSS_RESPAWN_WAYPOINT_LOCATION_ARRAY, iRespawnCount, NWScript.getLocation(obj));
                }
            }

            // Mark the number of spawn and respawn waypoints for this area
            NWScript.setLocalInt(oArea, ZSS_SPAWN_WAYPOINT_COUNT, iSpawnCount);
            NWScript.setLocalInt(oArea, ZSS_RESPAWN_WAYPOINT_COUNT, iRespawnCount);
            // Mark the unique identifier (the resref)
            NWScript.setLocalString(oArea, ZSS_WAYPOINT_NAME, sSpawnID);

            oArea = NWNX_Funcs.GetNextArea();
        }

        // Set the TMI limit back to normal
        NWNX_TMI.SetTMILimit(tmiLimit);
    }

    public void ZSS_OnZombieDeath(NWObject oZombie)
    {
        final NWObject oArea = NWScript.getArea(oZombie);
        int iWaypointCount = NWScript.getLocalInt(oArea, ZSS_RESPAWN_WAYPOINT_COUNT);
        if(NWScript.getLocalInt(oZombie, ZSS_ZOMBIE_SPAWNED) != 1 || iWaypointCount <= 0) return;

        int iPCCount = NWScript.getLocalInt(oArea, ZSS_PLAYER_COUNT);
        int iGroupID = NWScript.getLocalInt(oArea, ZSS_GROUP_ID);
        if(iGroupID <= 0) iGroupID = 1;

        if(iPCCount > 0)
        {
            int iWaypoint = NWScript.random(iWaypointCount) + 1;
            final String sResref = ZSS_GetZombieToSpawn(iGroupID);
            final NWLocation lLocation = LocalArray.GetLocalArrayLocation(oArea, ZSS_RESPAWN_WAYPOINT_LOCATION_ARRAY, iWaypoint);

            Scheduler.delay(oZombie, ZSS_SPAWN_DELAY * 1000, new Runnable() {
                @Override
                public void run() {
                    ZSS_DelayCreateZombie(sResref, lLocation, oArea);
                }
            });

        }
    }


    private String ZSS_GetZombieToSpawn(int spawnTableID)
    {
        SpawnTableRepository repo = new SpawnTableRepository();
        SpawnTableEntity entity = repo.GetBySpawnTableID(spawnTableID);

        int totalWeight = 0;
        for(SpawnTableCreatureEntity creature : entity.getSpawnTableCreatures())
        {
            totalWeight += creature.getWeight();
        }

        int randomIndex = -1;
        double random = Math.random() * totalWeight;
        for(int i= 0; i < entity.getSpawnTableCreatures().size(); ++i)
        {
            random -= entity.getSpawnTableCreatures().get(i).getWeight();
            if(random <= 0.0d)
            {
                randomIndex = i;
                break;
            }
        }

        SpawnTableCreatureEntity creatureEntity = entity.getSpawnTableCreatures().get(randomIndex);
        return creatureEntity.getResref();
    }
    

}
