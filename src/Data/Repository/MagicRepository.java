package Data.Repository;

import Data.DataContext;
import Entities.CraftBlueprintEntity;
import Entities.PCAbilityEntity;
import Entities.PCBlueprintEntity;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.sql.Timestamp;

public class MagicRepository {

    public boolean AddAbilityToPC(String uuid, int featID)
    {
        boolean addedSuccessfully = false;

        try(DataContext context = new DataContext())
        {
            Criteria criteria = context.getSession()
                    .createCriteria(PCAbilityEntity.class)
                    .add(Restrictions.eq("featID", featID))
                    .add(Restrictions.eq("playerID", uuid));
            PCAbilityEntity entity = (PCAbilityEntity) criteria.uniqueResult();

            if(entity == null)
            {
                entity = new PCAbilityEntity();
                entity.setPlayerID(uuid);
                entity.setFeatID(featID);
                DateTime dt = new DateTime(DateTimeZone.UTC);
                entity.setAcquiredDate(new Timestamp(dt.getMillis()));

                context.getSession().saveOrUpdate(entity);
                addedSuccessfully = true;
            }
        }

        return addedSuccessfully;
    }
}
