package Data.Repository;

import Data.DataContext;
import Entities.ForcedSPResetEntity;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;

public class ForcedSPResetRepository {

    public ForcedSPResetEntity GetLatestForcedSPResetDate()
    {
        ForcedSPResetEntity entity;

        try(DataContext context = new DataContext())
        {
            Criteria criteria = context.getSession()
                    .createCriteria(ForcedSPResetEntity.class)
                    .addOrder(Order.desc("dateOfReset"))
                    .setMaxResults(1);

            entity = (ForcedSPResetEntity) criteria.uniqueResult();
        }

        return entity;
    }

}
