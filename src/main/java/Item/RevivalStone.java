package Item;

import Common.IScriptEventHandler;
import Data.Repository.PlayerRepository;
import Entities.PlayerEntity;
import GameObject.PlayerGO;
import org.nwnx.nwnx2.jvm.NWObject;

import static org.nwnx.nwnx2.jvm.NWScript.*;

public class RevivalStone implements IScriptEventHandler {
    @Override
    public void runScript(NWObject oPC) {
        NWObject oItem = getItemActivated();

        if(!getIsPC(oPC) || getIsDM(oPC) || getIsDMPossessed(oPC)) return;

        PlayerGO pcGO = new PlayerGO(oPC);
        PlayerRepository playerRepo = new PlayerRepository();
        PlayerEntity entity = playerRepo.GetByPlayerID(pcGO.getUUID());

        entity.setRevivalStoneCount(entity.getRevivalStoneCount() + 1);
        playerRepo.save(entity);

        destroyObject(oItem, 0.0f);

        String stone = entity.getRevivalStoneCount() == 1 ? "stone" : "stones";
        sendMessageToPC(oPC, "You add a revival stone to your collection. You now have " + entity.getRevivalStoneCount() + " " + stone + ".");
    }
}
