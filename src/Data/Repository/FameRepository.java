package Data.Repository;

import Data.DataContext;
import Data.SqlParameter;
import Entities.FameRegionEntity;
import Entities.PCRegionalFameEntity;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class FameRepository {

    public PCRegionalFameEntity GetPCFameByID(String playerID, int regionID)
    {
        PCRegionalFameEntity entity;

        try(DataContext context = new DataContext())
        {
            entity = context.executeSQLSingle("Fame/GetPCFameByID",
                    PCRegionalFameEntity.class,
                    new SqlParameter("playerID", playerID),
                    new SqlParameter("fameRegionID", regionID));

        }

        if(entity == null)
        {
            try(DataContext context = new DataContext())
            {
                CriteriaBuilder cb = context.getSession().getCriteriaBuilder();

                CriteriaQuery<FameRegionEntity> query =
                        cb.createQuery(FameRegionEntity.class);

                Root<FameRegionEntity> root = query.from(FameRegionEntity.class);
                query.select(root);

                FameRegionEntity fameRegion = context.getSession()
                        .createQuery(query)
                        .uniqueResult();

                entity = new PCRegionalFameEntity();
                entity.setAmount(0);
                entity.setPlayerID(playerID);
                entity.setFameRegion(fameRegion);
                context.getSession().saveOrUpdate(entity);
            }
        }

        return entity;
    }




    public void Save(PCRegionalFameEntity entity)
    {
        try(DataContext context = new DataContext())
        {
            context.getSession().saveOrUpdate(entity);
        }
    }
}
