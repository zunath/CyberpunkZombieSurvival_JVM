package Data.Repository;

import Data.DataContext;
import Entities.AbilityCategoryEntity;
import Entities.AbilityEntity;
import Entities.PCEquippedAbilityEntity;
import Entities.PCLearnedAbilityEntity;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.sql.Timestamp;
import java.util.List;

public class MagicRepository {

    public AbilityEntity GetAbilityByFeatID(int featID)
    {
        try(DataContext context = new DataContext())
        {
            Criteria criteria = context.getSession()
                    .createCriteria(AbilityEntity.class)
                    .add(Restrictions.eq("featID", featID));
            return (AbilityEntity)criteria.uniqueResult();
        }
    }

    public AbilityEntity GetAbilityByID(int abilityID)
    {
        try(DataContext context = new DataContext())
        {
            Criteria criteria = context.getSession()
                    .createCriteria(AbilityEntity.class)
                    .add(Restrictions.eq("abilityID", abilityID));
            return (AbilityEntity)criteria.uniqueResult();
        }
    }

    public boolean AddAbilityToPC(String uuid, int abilityID)
    {
        boolean addedSuccessfully = false;

        try(DataContext context = new DataContext())
        {
            Criteria criteria = context.getSession()
                    .createCriteria(PCLearnedAbilityEntity.class)
                    .createAlias("ability", "a")
                    .add(Restrictions.eq("a.abilityID", abilityID))
                    .add(Restrictions.eq("playerID", uuid));
            PCLearnedAbilityEntity entity = (PCLearnedAbilityEntity) criteria.uniqueResult();

            if(entity == null)
            {
                criteria = context.getSession()
                        .createCriteria(AbilityEntity.class)
                        .add(Restrictions.eq("abilityID", abilityID));
                AbilityEntity ability = (AbilityEntity) criteria.uniqueResult();

                entity = new PCLearnedAbilityEntity();
                entity.setPlayerID(uuid);
                entity.setAbility(ability);
                DateTime dt = new DateTime(DateTimeZone.UTC);
                entity.setAcquiredDate(new Timestamp(dt.getMillis()));

                context.getSession().saveOrUpdate(entity);
                addedSuccessfully = true;
            }
        }

        return addedSuccessfully;
    }

    public PCEquippedAbilityEntity GetPCEquippedAbilities(String uuid)
    {
        try(DataContext context = new DataContext())
        {
            Criteria criteria = context.getSession()
                    .createCriteria(PCEquippedAbilityEntity.class)
                    .add(Restrictions.eq("playerID", uuid));
            PCEquippedAbilityEntity entity = (PCEquippedAbilityEntity)criteria.uniqueResult();

            if(entity == null)
            {
                entity = new PCEquippedAbilityEntity();
                entity.setPlayerID(uuid);

                context.getSession().saveOrUpdate(entity);
            }

            return entity;
        }
    }

    public List<AbilityCategoryEntity> GetActiveAbilityCategories()
    {
        List<AbilityCategoryEntity> entities;

        try(DataContext context = new DataContext())
        {
            Criteria criteria = context.getSession()
                    .createCriteria(AbilityCategoryEntity.class)
                    .add(Restrictions.eq("isActive", true));

            entities = criteria.list();
        }

        return entities;
    }

    public List<PCLearnedAbilityEntity> GetPCLearnedAbilitiesByCategoryID(String uuid, int categoryID)
    {
        List<PCLearnedAbilityEntity> entities;

        try(DataContext context = new DataContext())
        {
            Criteria criteria = context.getSession()
                    .createCriteria(PCLearnedAbilityEntity.class)
                    .createAlias("ability", "a")
                    .createAlias("a.category", "c")
                    .add(Restrictions.eq("c.abilityCategoryID", categoryID))
                    .add(Restrictions.eq("playerID", uuid));
            entities = criteria.list();
        }

        return entities;
    }

    public void Save(PCEquippedAbilityEntity entity)
    {
        try(DataContext context = new DataContext())
        {
            context.getSession().saveOrUpdate(entity);
        }
    }

}
