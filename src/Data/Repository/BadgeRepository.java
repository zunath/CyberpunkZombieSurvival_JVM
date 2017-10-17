package Data.Repository;

import Data.DataContext;
import Entities.BadgeEntity;
import Entities.PCBadgeEntity;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class BadgeRepository {

    public List<PCBadgeEntity> GetByUUID(String uuid)
    {
        List<PCBadgeEntity> entities;

        try(DataContext context = new DataContext())
        {
            CriteriaBuilder cb = context.getSession().getCriteriaBuilder();

            CriteriaQuery<PCBadgeEntity> query =
                    cb.createQuery(PCBadgeEntity.class);

            Root<PCBadgeEntity> root = query.from(PCBadgeEntity.class);
            query.select(root)
                    .where(
                            cb.equal(root.get("playerID"), uuid)
                    );

            entities = context.getSession()
                    .createQuery(query)
                    .list();
        }

        return entities;
    }

    public PCBadgeEntity GetByID(String uuid, int badgeID)
    {
        PCBadgeEntity entity;

        try(DataContext context = new DataContext())
        {
            CriteriaBuilder cb = context.getSession().getCriteriaBuilder();

            CriteriaQuery<PCBadgeEntity> query =
                    cb.createQuery(PCBadgeEntity.class);

            Root<PCBadgeEntity> root = query.from(PCBadgeEntity.class);
            query.select(root)
                    .where(
                            cb.equal(root.get("playerID"), uuid),
                            cb.equal(root.get("badgeID"), badgeID)
                    );

            entity = context.getSession()
                    .createQuery(query)
                    .uniqueResult();

        }

        return entity;
    }

    public BadgeEntity GetByID(int badgeID)
    {
        BadgeEntity entity;

        try(DataContext context = new DataContext())
        {
            CriteriaBuilder cb = context.getSession().getCriteriaBuilder();

            CriteriaQuery<BadgeEntity> query =
                    cb.createQuery(BadgeEntity.class);

            Root<BadgeEntity> root = query.from(BadgeEntity.class);
            query.select(root)
                    .where(
                            cb.equal(root.get("badgeID"), badgeID)
                    );

            entity = context.getSession()
                    .createQuery(query)
                    .uniqueResult();
        }

        return entity;
    }

    public void Save(PCBadgeEntity entity)
    {
        try(DataContext context = new DataContext())
        {
            context.getSession().saveOrUpdate(entity);
        }
    }


}
