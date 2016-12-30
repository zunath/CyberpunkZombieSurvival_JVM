package Item;

import Abilities.IAbility;
import Common.IScriptEventHandler;
import Data.Repository.MagicRepository;
import GameObject.PlayerGO;
import GameSystems.MagicSystem;
import GameSystems.ProgressionSystem;
import NWNX.NWNX_Events;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;

public class AbilityDisc implements IScriptEventHandler{
    @Override
    public void runScript(NWObject oPC) {
        PlayerGO pcGO = new PlayerGO(oPC);
        NWObject oItem = NWNX_Events.GetEventItem();
        int featID = NWScript.getLocalInt(oItem, "MAGIC_FEAT_ID");
        IAbility ability = MagicSystem.GetAbilityByFeatID(featID);

        if(featID <= 0)
        {
            NWScript.sendMessageToPC(oPC, "Unable to learn ability");
        }

        MagicRepository repo = new MagicRepository();
        boolean success = repo.AddAbilityToPC(pcGO.getUUID(), featID);

        if(success)
        {
            NWScript.destroyObject(oItem, 0.0f);
            NWScript.sendMessageToPC(oPC, "You learn " + ability.Name() + "!");
        }
        else
        {
            NWScript.sendMessageToPC(oPC, "You already know " + ability.Name() + ".");
        }
    }
}
