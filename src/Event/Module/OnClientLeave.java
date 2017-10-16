package Event.Module;
import Entities.PlayerEntity;
import GameObject.PlayerGO;
import Common.IScriptEventHandler;
import Data.Repository.PlayerRepository;
import GameSystems.RadioSystem;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.nwnx.nwnx2.jvm.*;

@SuppressWarnings("unused")
public class OnClientLeave implements IScriptEventHandler {
	@Override
	public void runScript(NWObject objSelf) {
		RadioSystem radioSystem = new RadioSystem();
		NWObject pc = NWScript.getExitingObject();

		if(NWScript.getIsPC(pc))
        {
            NWScript.exportSingleCharacter(pc);
        }

		SaveCharacter(pc);

		// SimTools
		NWScript.executeScript("fky_chat_clexit", objSelf);
		// Radio GameSystems - Also used for NWNX chat (Different from SimTools)
		radioSystem.OnModuleLeave();

		WriteConnectionDisconnectToConsole();
	}

	private void SaveCharacter(NWObject pc) {

		if(NWScript.getIsDM(pc)) return;

		PlayerGO gameObject = new PlayerGO(pc);
		PlayerRepository repo = new PlayerRepository();
		String uuid = gameObject.getUUID();

		PlayerEntity entity = repo.getByUUID(uuid);
		entity.setCharacterName(NWScript.getName(pc, false));
		entity.setHitPoints(NWScript.getCurrentHitPoints(pc));

		repo.save(entity);
	}

	private void WriteConnectionDisconnectToConsole()
	{
		NWObject oPC = NWScript.getExitingObject();
		String name = NWScript.getName(oPC, false);
		String cdKey = NWScript.getLocalString(oPC, "PC_CD_KEY");
        String account = NWScript.getLocalString(oPC, "PC_ACCOUNT");
        DateTime now = new DateTime(DateTimeZone.UTC);
        String nowString = now.toString("yyyy-MM-dd hh:mm:ss");

		System.out.println(nowString + ": " + name + " (" + account + "/" + cdKey + ") left the server.");
	}
}
