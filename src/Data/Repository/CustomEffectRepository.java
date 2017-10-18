package Data.Repository;

import Data.DataContext;
import Entities.CustomEffectEntity;
import Entities.PCCustomEffectEntity;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class CustomEffectRepository {

    public List<PCCustomEffectEntity> GetPCEffects(String uuid)
    {
        List<PCCustomEffectEntity> entities;

        try(DataContext context = new DataContext())
        {

            CriteriaBuilder cb = context.getSession().getCriteriaBuilder();

            CriteriaQuery<PCCustomEffectEntity> query =
                    cb.createQuery(PCCustomEffectEntity.class);

            Root<PCCustomEffectEntity> root = query.from(PCCustomEffectEntity.class);
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

    public PCCustomEffectEntity GetPCEffectByID(String uuid, int customEffectID)
    {
        PCCustomEffectEntity entity;

        try(DataContext context = new DataContext())
        {
            CriteriaBuilder cb = context.getSession().getCriteriaBuilder();

            CriteriaQuery<PCCustomEffectEntity> query =
                    cb.createQuery(PCCustomEffectEntity.class);

            Root<PCCustomEffectEntity> root = query.from(PCCustomEffectEntity.class);
            query.select(root)
                    .where(
                            cb.equal(root.get("customEffect.customEffectID"), customEffectID),
                            cb.equal(root.get("playerID"), uuid)
                    );

            entity = context.getSession()
                    .createQuery(query)
                    .uniqueResult();

        }

        return entity;
    }

    public CustomEffectEntity GetEffectByID(int customEffectID)
    {
        CustomEffectEntity entity;

        try(DataContext context = new DataContext())
        {
            CriteriaBuilder cb = context.getSession().getCriteriaBuilder();

            CriteriaQuery<CustomEffectEntity> query =
                    cb.createQuery(CustomEffectEntity.class);

            Root<CustomEffectEntity> root = query.from(CustomEffectEntity.class);
            query.select(root)
                    .where(
                            cb.equal(root.get("customEffectID"), customEffectID)
                    );

            entity = context.getSession()
                    .createQuery(query)
                    .uniqueResult();

        }

        return entity;
    }

    public void Save(Object entity)
    {
        try(DataContext context = new DataContext())
        {
            context.getSession().saveOrUpdate(entity);
        }
    }

    public void Delete(Object entity)
    {
        try(DataContext context = new DataContext())
        {
            context.getSession().delete(entity);
        }
    }


}
