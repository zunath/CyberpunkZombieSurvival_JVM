package GameSystems;

import Abilities.IAbility;
import Bioware.Position;
import Data.Repository.MagicRepository;
import Data.Repository.PlayerRepository;
import Entities.AbilityEntity;
import Entities.PCEquippedAbilityEntity;
import Entities.PlayerEntity;
import GameObject.PlayerGO;
import Helper.ScriptHelper;
import NWNX.NWNX_Events;
import NWNX.NWNX_Funcs;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.NWVector;
import org.nwnx.nwnx2.jvm.Scheduler;
import org.nwnx.nwnx2.jvm.constants.Ability;
import org.nwnx.nwnx2.jvm.constants.Animation;
import org.nwnx.nwnx2.jvm.constants.DurationType;
import org.nwnx.nwnx2.jvm.constants.VfxDur;
import sun.font.Script;

import java.util.UUID;

public class MagicSystem {

    private static int SPELL_STATUS_STARTED = 1;
    private static int SPELL_STATUS_INTERRUPTED = 2;

    public static void OnFeatUsed(NWObject pc)
    {
        PlayerRepository playerRepo = new PlayerRepository();
        MagicRepository repo = new MagicRepository();
        PlayerGO pcGO = new PlayerGO(pc);
        int featID = NWNX_Events.GetEventSubType();
        NWObject target = NWNX_Events.GetEventTarget();
        AbilityEntity entity = repo.GetAbilityByFeatID(featID);

        if(entity == null) return;
        IAbility ability = (IAbility) ScriptHelper.GetClassByName(entity.getJavaScriptName());
        if(ability == null) return;

        PlayerEntity playerEntity = playerRepo.getByUUID(pcGO.getUUID());

        if(!ability.CanCastSpell())
        {
            NWScript.sendMessageToPC(pc,
                    ability.CannotCastSpellMessage() == null ?
                            "That ability cannot be used at this time." :
                            ability.CannotCastSpellMessage());
            return;
        }

        if(playerEntity.getCurrentMana() < ability.ManaCost(pc, entity.getBaseManaCost()))
        {
            NWScript.sendMessageToPC(pc, "You do not have enough mana.");
            return;
        }

        if(pcGO.isBusy() || pcGO.isBusy())
        {
            NWScript.sendMessageToPC(pc, "You are too busy to activate that ability.");
            return;
        }

        if(ability.CastingTime(pc, entity.getBaseCastingTime()) > 0.0f)
        {
            CastSpell(pc, target, ability, entity.getBaseManaCost(), entity.getBaseCastingTime(), entity.getBaseCooldownTime());
        }
        else
        {
            ability.OnImpact(pc, target);
            playerEntity.setCurrentMana(playerEntity.getCurrentMana() - ability.ManaCost(pc, entity.getBaseManaCost()));
            playerRepo.save(playerEntity);
        }
    }

    private static void CastSpell(final NWObject pc,
                                  final NWObject target,
                                  final IAbility ability,
                                  final int baseManaCost,
                                  final float baseCastingTime,
                                  final float baseCooldownTime)
    {
        final String spellUUID = UUID.randomUUID().toString();
        final PlayerGO pcGO = new PlayerGO(pc);

        NWScript.clearAllActions(false);
        Position.TurnToFaceObject(target, pc);
        NWScript.applyEffectToObject(DurationType.TEMPORARY,
                NWScript.effectVisualEffect(VfxDur.ELEMENTAL_SHIELD, false),
                pc,
                ability.CastingTime(pc, baseManaCost) + 0.2f);
        NWScript.actionPlayAnimation(Animation.LOOPING_CONJURE1, 1.0f, ability.CastingTime(pc, baseCastingTime) - 0.1f);

        pcGO.setIsBusy(true);
        CheckForSpellInterruption(pc, spellUUID, NWScript.getPosition(pc));
        NWScript.setLocalInt(pc, spellUUID, SPELL_STATUS_STARTED);

        NWNX_Funcs.StartTimingBar(pc, (int)ability.CastingTime(pc, baseCastingTime), "");
        Scheduler.delay(pc, (int)(1050 * ability.CastingTime(pc, baseCastingTime)), new Runnable() {
            @Override
            public void run() {
                if(NWScript.getLocalInt(pc, spellUUID) == SPELL_STATUS_INTERRUPTED)
                {
                    NWScript.deleteLocalInt(pc, spellUUID);
                    NWScript.sendMessageToPC(pc, "Your spell has been interrupted.");
                    return;
                }

                NWScript.deleteLocalInt(pc, spellUUID);
                PlayerRepository repo = new PlayerRepository();
                PlayerEntity entity = repo.getByUUID(pcGO.getUUID());

                ability.OnImpact(pc, target);
                entity.setCurrentMana(entity.getCurrentMana() - ability.ManaCost(pc, baseManaCost));
                repo.save(entity);

                pcGO.setIsBusy(false);
            }
        });
    }

    private static void CheckForSpellInterruption(final NWObject pc, final String spellUUID, final NWVector position)
    {
        NWVector currentPosition = NWScript.getPosition(pc);

        if(!currentPosition.equals(position))
        {
            PlayerGO pcGO = new PlayerGO(pc);
            NWNX_Funcs.StopTimingBar(pc, "");
            pcGO.setIsBusy(false);
            NWScript.setLocalInt(pc, spellUUID, SPELL_STATUS_INTERRUPTED);
            return;
        }

        Scheduler.delay(pc, 1000, new Runnable() {
            @Override
            public void run() {
                CheckForSpellInterruption(pc, spellUUID, position);
            }
        });
    }

    public static PCEquippedAbilityEntity EquipAbility(NWObject oPC, int slotID, int abilityID)
    {
        MagicRepository repo = new MagicRepository();
        PCEquippedAbilityEntity entity = UnequipAbility(oPC, slotID);
        AbilityEntity abilityEntity = repo.GetAbilityByID(abilityID);

        if(slotID == 1) entity.setSlot1(abilityEntity);
        else if(slotID == 2) entity.setSlot2(abilityEntity);
        else if(slotID == 3) entity.setSlot3(abilityEntity);
        else if(slotID == 4) entity.setSlot4(abilityEntity);
        else if(slotID == 5) entity.setSlot5(abilityEntity);
        else if(slotID == 6) entity.setSlot6(abilityEntity);
        else if(slotID == 7) entity.setSlot7(abilityEntity);
        else if(slotID == 8) entity.setSlot8(abilityEntity);
        else if(slotID == 9) entity.setSlot9(abilityEntity);
        else if(slotID == 10) entity.setSlot10(abilityEntity);


        IAbility ability = (IAbility) ScriptHelper.GetClassByName("Abilities." + abilityEntity.getJavaScriptName());
        ability.OnEquip(oPC);
        repo.Save(entity);

        return entity;
    }

    public static PCEquippedAbilityEntity UnequipAbility(NWObject oPC, int slotID)
    {
        MagicRepository repo = new MagicRepository();
        PlayerGO pcGO = new PlayerGO(oPC);
        PCEquippedAbilityEntity entity = repo.GetPCEquippedAbilities(pcGO.getUUID());
        IAbility ability;
        String scriptName = null;



        if(slotID == 1)
        {
            if(entity.getSlot1() == null) return entity;

            scriptName = entity.getSlot1().getJavaScriptName();
            entity.setSlot1(null);
        }
        else if(slotID == 2)
        {
            if(entity.getSlot2() == null) return entity;

            scriptName = entity.getSlot2().getJavaScriptName();
            entity.setSlot2(null);
        }
        else if(slotID == 3)
        {
            if(entity.getSlot3() == null) return entity;

            scriptName = entity.getSlot3().getJavaScriptName();
            entity.setSlot3(null);
        }
        else if(slotID == 4)
        {
            if(entity.getSlot4() == null) return entity;

            scriptName = entity.getSlot4().getJavaScriptName();
            entity.setSlot4(null);
        }
        else if(slotID == 5)
        {
            if(entity.getSlot5() == null) return entity;

            scriptName = entity.getSlot5().getJavaScriptName();
            entity.setSlot5(null);
        }
        else if(slotID == 6)
        {
            if(entity.getSlot6() == null) return entity;

            scriptName = entity.getSlot6().getJavaScriptName();
            entity.setSlot6(null);
        }
        else if(slotID == 7)
        {
            if(entity.getSlot7() == null) return entity;

            scriptName = entity.getSlot7().getJavaScriptName();
            entity.setSlot7(null);
        }
        else if(slotID == 8)
        {
            if(entity.getSlot8() == null) return entity;

            scriptName = entity.getSlot8().getJavaScriptName();
            entity.setSlot8(null);
        }
        else if(slotID == 9)
        {
            if(entity.getSlot9() == null) return entity;

            scriptName = entity.getSlot9().getJavaScriptName();
            entity.setSlot9(null);
        }
        else if(slotID == 10)
        {
            if(entity.getSlot10() == null) return entity;

            scriptName = entity.getSlot10().getJavaScriptName();
            entity.setSlot10(null);
        }

        ability = (IAbility) ScriptHelper.GetClassByName("Abilities." + scriptName);
        ability.OnUnequip(oPC);
        repo.Save(entity);

        return entity;
    }

}
