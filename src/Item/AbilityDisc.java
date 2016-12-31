package Item;

import Common.IScriptEventHandler;
import Data.Repository.MagicRepository;
import Entities.AbilityEntity;
import GameObject.PlayerGO;
import NWNX.NWNX_Events;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;

public class AbilityDisc implements IScriptEventHandler{
    @Override
    public void runScript(NWObject oPC) {
        PlayerGO pcGO = new PlayerGO(oPC);
        NWObject oItem = NWNX_Events.GetEventItem();
        String resref = NWScript.getResRef(oItem);
        MagicRepository repo = new MagicRepository();

        int abilityID = Integer.parseInt(resref.substring(13));
        AbilityEntity entity = repo.GetAbilityByID(abilityID);
        boolean success = repo.AddAbilityToPC(pcGO.getUUID(), abilityID);

        if(success)
        {
            NWScript.destroyObject(oItem, 0.0f);
            NWScript.sendMessageToPC(oPC, "You learn " + entity.getName() + "!");
        }
        else
        {
            NWScript.sendMessageToPC(oPC, "You already know " + entity.getName() + ".");
        }
    }
}
