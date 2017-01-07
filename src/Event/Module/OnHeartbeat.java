package Event.Module;
import Common.Constants;
import Entities.ActivePlayerEntity;
import Entities.PlayerEntity;
import Enumerations.AbilityType;
import GameObject.PlayerGO;
import Common.IScriptEventHandler;
import Data.Repository.ActivePlayerRepository;
import Data.Repository.PlayerRepository;
import GameSystems.DiseaseSystem;
import GameSystems.FoodSystem;
import GameSystems.MagicSystem;
import GameSystems.ProgressionSystem;
import Helper.ColorToken;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.nwnx.nwnx2.jvm.*;
import org.nwnx.nwnx2.jvm.constants.Ability;
import org.nwnx.nwnx2.jvm.constants.DurationType;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class OnHeartbeat implements IScriptEventHandler {

	PlayerRepository playerRepo;

	@Override
	public void runScript(NWObject objSelf) {

		playerRepo = new PlayerRepository();

		NWObject[] players = NWScript.getPCs();
		for(NWObject pc : players)
		{
			if(!NWScript.getIsDM(pc))
			{
				PlayerGO pcGO = new PlayerGO(pc);
				PlayerEntity entity = playerRepo.getByUUID(pcGO.getUUID());

				if(entity != null)
				{
					entity = HandleRegenerationTick(pc, entity);
					entity = HandleManaRegenerationTick(pc, entity);
					entity = HandleDiseaseTick(pc, entity);
					entity = HandleFoodTick(pc, entity);
					playerRepo.save(entity);
				}
			}
		}

		SaveCharacters();
		RefreshActivePlayers();
	}

	// Export all characters every minute.
	private void SaveCharacters()
	{
		int currentTick = NWScript.getLocalInt(NWObject.MODULE, "SAVE_CHARACTERS_TICK") + 1;

		if(currentTick >= 10)
		{
			NWScript.exportAllCharacters();
			currentTick = 0;
		}

		NWScript.setLocalInt(NWObject.MODULE, "SAVE_CHARACTERS_TICK", currentTick);
	}

	private PlayerEntity HandleRegenerationTick(NWObject oPC, PlayerEntity entity)
	{
		entity.setRegenerationTick(entity.getRegenerationTick() - 1);
		int rate = Constants.BaseHPRegenRate - ProgressionSystem.GetPlayerSkillLevel(oPC, ProgressionSystem.SkillType_NATURAL_REGENERATION);
		int amount = entity.getHpRegenerationAmount();

		if(entity.getRegenerationTick() <= 0)
		{
			if(NWScript.getCurrentHitPoints(oPC) < NWScript.getMaxHitPoints(oPC))
			{
				// Lizard Regeneration ability grants +2 to heal amount.
				if(MagicSystem.IsAbilityEquipped(oPC, AbilityType.LizardRegeneration))
				{
					amount += 2;
				}

				// Safe zones grant +5 HP regen
				boolean isInSafeZone = NWScript.getLocalInt(NWScript.getArea(oPC), "SAFE_ZONE") == 1;
				if(isInSafeZone)
				{
					amount += 5;
				}

				NWScript.applyEffectToObject(DurationType.INSTANT, NWScript.effectHeal(amount), oPC, 0.0f);
			}

			entity.setRegenerationTick(rate);
		}

		return entity;
	}

	private PlayerEntity HandleManaRegenerationTick(NWObject oPC, PlayerEntity entity)
	{
		entity.setCurrentManaTick(entity.getCurrentManaTick() - 1);
		int rate = Constants.BaseManaRegenRate;
		int amount = Constants.BaseManaRegenAmount + ((NWScript.getAbilityScore(oPC, Ability.CHARISMA, false) - 10) / 2);

		if(entity.getCurrentManaTick() <= 0)
		{
			if(entity.getCurrentMana() < entity.getMaxMana())
			{
				// Clarity ability grants +2 to mana regen amount.
				if(MagicSystem.IsAbilityEquipped(oPC, AbilityType.Clarity))
				{
					amount += 2;
				}

				// Safe zones grant +5 mana regen
				boolean isInSafeZone = NWScript.getLocalInt(NWScript.getArea(oPC), "SAFE_ZONE") == 1;
				if(isInSafeZone)
				{
					amount += 5;
				}

				entity.setCurrentMana(entity.getCurrentMana() + amount);
				if(entity.getCurrentMana() > entity.getMaxMana())
					entity.setCurrentMana(entity.getMaxMana());

				NWScript.sendMessageToPC(oPC, ColorToken.Custom(32,223,219) + "Mana: " + entity.getCurrentMana() + " / " + entity.getMaxMana());
			}

			entity.setCurrentManaTick(rate);
		}

		return entity;
	}

	private PlayerEntity HandleDiseaseTick(NWObject oPC, PlayerEntity entity)
	{
		return DiseaseSystem.RunDiseaseRemovalProcess(oPC, entity);
	}

	private PlayerEntity HandleFoodTick(NWObject oPC, PlayerEntity entity)
	{
		return FoodSystem.RunHungerCycle(oPC, entity);
	}

	private void RefreshActivePlayers()
	{
		int activePlayersTick = NWScript.getLocalInt(NWObject.MODULE, "ACTIVE_PLAYERS_TICK") + 1;

		if(activePlayersTick >= 10)
		{
			ActivePlayerRepository repo = new ActivePlayerRepository();
			List<ActivePlayerEntity> entities = new ArrayList<>();

			for(NWObject pc : NWScript.getPCs())
			{
				if(!NWScript.getIsDM(pc))
				{
					PlayerEntity playerEntity = playerRepo.getByUUID(new PlayerGO(pc).getUUID());
					int level = ProgressionSystem.GetPlayerLevel(pc);
					int expPercentage = (int) ((float) playerEntity.getExperience() / (float) ProgressionSystem.GetLevelExperienceRequired(level) * 100.0f);
					ActivePlayerEntity entity = new ActivePlayerEntity();
					entity.setAccountName(NWScript.getPCPlayerName(pc));
					entity.setCharacterName(NWScript.getName(pc, false));
					entity.setLevelPercentage((int) (((float) level / (float) ProgressionSystem.LevelCap) * 100.0f));
					entity.setLevel(level);
					entity.setExpPercentage(expPercentage);
					entity.setAreaName(NWScript.getName(NWScript.getArea(pc), false));
					entity.setCreateDate(new DateTime(DateTimeZone.UTC).toDate());
					entity.setDescription(NWScript.getDescription(pc, false, true));

					entities.add(entity);
				}
			}

			repo.Save(entities);
			activePlayersTick = 0;
		}

		NWScript.setLocalInt(NWObject.MODULE, "ACTIVE_PLAYERS_TICK", activePlayersTick);
	}
}

