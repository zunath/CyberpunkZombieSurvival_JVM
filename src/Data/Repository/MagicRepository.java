package Data.Repository;

import Data.DataContext;
import Entities.AbilityEntity;
import Entities.PCLearnedAbilityEntity;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.sql.Timestamp;

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
                    .add(Restrictions.eq("abilityID", abilityID))
                    .add(Restrictions.eq("playerID", uuid));
            PCLearnedAbilityEntity entity = (PCLearnedAbilityEntity) criteria.uniqueResult();

            if(entity == null)
            {
                entity = new PCLearnedAbilityEntity();
                entity.setPlayerID(uuid);
                entity.setAbilityID(abilityID);
                DateTime dt = new DateTime(DateTimeZone.UTC);
                entity.setAcquiredDate(new Timestamp(dt.getMillis()));

                context.getSession().saveOrUpdate(entity);
                addedSuccessfully = true;
            }
        }

        return addedSuccessfully;
    }
}
