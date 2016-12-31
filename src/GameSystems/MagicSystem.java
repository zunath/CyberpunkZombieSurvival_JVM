package GameSystems;

import Abilities.IAbility;
import Bioware.Position;
import Data.Repository.MagicRepository;
import Data.Repository.PlayerRepository;
import Entities.AbilityEntity;
import Entities.PlayerEntity;
import GameObject.PlayerGO;
import Helper.ScriptHelper;
import NWNX.NWNX_Events;
import NWNX.NWNX_Funcs;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.NWVector;
import org.nwnx.nwnx2.jvm.Scheduler;
import org.nwnx.nwnx2.jvm.constants.Animation;
import org.nwnx.nwnx2.jvm.constants.DurationType;
import org.nwnx.nwnx2.jvm.constants.VfxDur;

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


}
