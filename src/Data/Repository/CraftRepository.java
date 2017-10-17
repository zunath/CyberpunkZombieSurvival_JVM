package Data.Repository;

import Data.DataContext;
import Entities.*;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class CraftRepository {

    public boolean AddBlueprintToPC(String uuid, int blueprintID)
    {
        boolean addedSuccessfully = false;

        try(DataContext context = new DataContext())
        {
            CriteriaBuilder cb = context.getSession().getCriteriaBuilder();

            CriteriaQuery<PCBlueprintEntity> query =
                    cb.createQuery(PCBlueprintEntity.class);

            Root<PCBlueprintEntity> root = query.from(PCBlueprintEntity.class);
            query.select(root)
                    .where(
                            cb.equal(root.get("blueprint.craftBlueprintID"), blueprintID),
                            cb.equal(root.get("playerID"), uuid)
                    );

            PCBlueprintEntity entity = context.getSession()
                    .createQuery(query)
                    .uniqueResult();

            if(entity == null)
            {
                CriteriaQuery<CraftBlueprintEntity> pcceQuery =
                        cb.createQuery(CraftBlueprintEntity.class);

                Root<CraftBlueprintEntity> pcceRoot = query.from(CraftBlueprintEntity.class);
                pcceQuery.select(pcceRoot)
                        .where(
                                cb.equal(pcceRoot.get("craftBlueprintID"), blueprintID)
                        );

                CraftBlueprintEntity blueprint = context.getSession()
                        .createQuery(pcceQuery)
                        .uniqueResult();

                entity = new PCBlueprintEntity();
                entity.setPlayerID(uuid);
                entity.setBlueprint(blueprint);
                DateTime dt = new DateTime(DateTimeZone.UTC);
                entity.setAcquiredDate(new Timestamp(dt.getMillis()));

                context.getSession().saveOrUpdate(entity);
                addedSuccessfully = true;
            }
        }

        return addedSuccessfully;
    }

    public PCBlueprintEntity GetPCBlueprintByID(String uuid, int blueprintID)
    {
        PCBlueprintEntity entity;

        try(DataContext context = new DataContext())
        {

            CriteriaBuilder cb = context.getSession().getCriteriaBuilder();

            CriteriaQuery<PCBlueprintEntity> query =
                    cb.createQuery(PCBlueprintEntity.class);

            Root<PCBlueprintEntity> root = query.from(PCBlueprintEntity.class);
            query.select(root)
                    .where(
                            cb.equal(root.get("blueprint.craftBlueprintID"), blueprintID),
                            cb.equal(root.get("playerID"), uuid)
                    );

            entity = context.getSession()
                    .createQuery(query)
                    .uniqueResult();
        }

        return entity;
    }

    public CraftBlueprintEntity GetBlueprintByID(int blueprintID)
    {
        CraftBlueprintEntity entity;

        try(DataContext context = new DataContext())
        {

            CriteriaBuilder cb = context.getSession().getCriteriaBuilder();

            CriteriaQuery<CraftBlueprintEntity> query =
                    cb.createQuery(CraftBlueprintEntity.class);

            Root<CraftBlueprintEntity> root = query.from(CraftBlueprintEntity.class);
            query.select(root)
                    .where(
                            cb.equal(root.get("craftBlueprintID"), blueprintID)
                    );

            entity = context.getSession()
                    .createQuery(query)
                    .uniqueResult();
        }

        return entity;
    }

    public PCCraftEntity GetPCCraftByID(String uuid, int craftID)
    {
        PCCraftEntity entity;

        try(DataContext context = new DataContext())
        {
            CriteriaBuilder cb = context.getSession().getCriteriaBuilder();

            CriteriaQuery<PCCraftEntity> query =
                    cb.createQuery(PCCraftEntity.class);

            Root<PCCraftEntity> root = query.from(PCCraftEntity.class);
            query.select(root)
                    .where(
                            cb.equal(root.get("playerID"), uuid),
                            cb.equal(root.get("craftID"), craftID)
                    );

            entity = context.getSession()
                    .createQuery(query)
                    .uniqueResult();

            if(entity == null)
            {
                entity = new PCCraftEntity();
                entity.setExperience(0);
                entity.setLevel(1);
                entity.setPlayerID(uuid);
                entity.setCraftID(craftID);

                context.getSession().saveOrUpdate(entity);
            }

        }

        return entity;
    }

    public CraftLevelEntity GetCraftLevelByLevel(int craftID, int level)
    {
        CraftLevelEntity entity;

        try(DataContext context = new DataContext())
        {
            CriteriaBuilder cb = context.getSession().getCriteriaBuilder();

            CriteriaQuery<CraftLevelEntity> query =
                    cb.createQuery(CraftLevelEntity.class);

            Root<CraftLevelEntity> root = query.from(CraftLevelEntity.class);
            query.select(root)
                    .where(
                            cb.equal(root.get("craftID"), craftID),
                            cb.equal(root.get("level"), level)
                    );

            entity = context.getSession()
                    .createQuery(query)
                    .uniqueResult();
        }

        return entity;
    }

    public long GetCraftMaxLevel(int craftID)
    {
        long maxLevel;

        try(DataContext context = new DataContext())
        {
            CriteriaBuilder cb = context.getSession().getCriteriaBuilder();

            CriteriaQuery<Long> query =
                    cb.createQuery(Long.class);

            Root<CraftLevelEntity> root = query.from(CraftLevelEntity.class);
            query.select(cb.max(root.get("level")))
                    .where(
                            cb.equal(root.get("craftID"), craftID)
                    );

            maxLevel = context.getSession()
                    .createQuery(query)
                    .getSingleResult();
        }

        return maxLevel;
    }

    public CraftEntity GetCraftByID(int craftID)
    {
        CraftEntity entity;

        try(DataContext context = new DataContext())
        {
            CriteriaBuilder cb = context.getSession().getCriteriaBuilder();

            CriteriaQuery<CraftEntity> query =
                    cb.createQuery(CraftEntity.class);

            Root<CraftEntity> root = query.from(CraftEntity.class);
            query.select(root)
                    .where(
                            cb.equal(root.get("craftID"), craftID)
                    );

            entity = context.getSession()
                    .createQuery(query)
                    .uniqueResult();
        }

        return entity;
    }

    public List<CraftBlueprintCategoryEntity> GetCategoriesAvailableToPCByCraftID(String uuid, int craftID)
    {
        List<CraftBlueprintCategoryEntity> entities = new ArrayList<>();

        try(DataContext context = new DataContext())
        {

            // TODO: Migrate to hibernate 5
            Criteria criteria = context.getSession()
                    .createCriteria(PCBlueprintEntity.class)
                    .createAlias("blueprint", "bp")
                    .createAlias("bp.category", "c")
                    .createAlias("bp.craft", "cr")
                    .setProjection(Projections.distinct(Projections.property("c.craftBlueprintCategoryID")))
                    .add(Restrictions.eq("playerID", uuid))
                    .add(Restrictions.eq("cr.craftID", craftID));
            List<Integer> categories = criteria.list();

            if(categories.size() > 0)
            {
                criteria = context.getSession()
                        .createCriteria(CraftBlueprintCategoryEntity.class)
                        .add(Restrictions.in("craftBlueprintCategoryID", categories));
                entities = criteria.list();
            }
        }

        return entities;
    }

    public List<CraftBlueprintCategoryEntity> GetCategoriesAvailableToPC(String uuid)
    {
        List<CraftBlueprintCategoryEntity> entities = new ArrayList<>();

        try(DataContext context = new DataContext())
        {

            // TODO: Migrate to hibernate 5
            Criteria criteria = context.getSession()
                    .createCriteria(PCBlueprintEntity.class)
                    .createAlias("blueprint", "bp")
                    .createAlias("bp.category", "c")
                    .createAlias("bp.craft", "cr")
                    .setProjection(Projections.distinct(Projections.property("c.craftBlueprintCategoryID")))
                    .add(Restrictions.eq("playerID", uuid));
            List<Integer> categories = criteria.list();

            if(categories.size() > 0)
            {
                criteria = context.getSession()
                        .createCriteria(CraftBlueprintCategoryEntity.class)
                        .add(Restrictions.in("craftBlueprintCategoryID", categories));
                entities = criteria.list();
            }
        }

        return entities;
    }

    public List<PCBlueprintEntity> GetPCBlueprintsByCategoryID(String uuid, int categoryID)
    {
        List<PCBlueprintEntity> entities;

        try(DataContext context = new DataContext())
        {
            // TODO: Migrate to hibernate 5
            Criteria criteria = context.getSession()
                    .createCriteria(PCBlueprintEntity.class)
                    .createAlias("blueprint", "bp")
                    .createAlias("bp.category", "c")
                    .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
                    .add(Restrictions.eq("playerID", uuid))
                    .add(Restrictions.eq("c.craftBlueprintCategoryID", categoryID));

            entities = criteria.list();

        }

        return entities;
    }

    public List<PCBlueprintEntity> GetPCBlueprintsForCraftByCategoryID(String uuid, int craftID, int categoryID)
    {
        List<PCBlueprintEntity> entities;

        try(DataContext context = new DataContext())
        {
            // TODO: Migrate to hibernate 5
            Criteria criteria = context.getSession()
                    .createCriteria(PCBlueprintEntity.class)
                    .createAlias("blueprint", "bp")
                    .createAlias("bp.category", "c")
                    .createAlias("bp.craft", "cr")
                    .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
                    .add(Restrictions.eq("playerID", uuid))
                    .add(Restrictions.eq("cr.craftID", craftID))
                    .add(Restrictions.eq("c.craftBlueprintCategoryID", categoryID))
                    .addOrder(Order.asc("bp.itemName"));

            entities = criteria.list();

        }

        return entities;
    }

    public List<CraftEntity> GetAllCrafts()
    {
        List<CraftEntity> entities;

        try(DataContext context = new DataContext())
        {
            CriteriaBuilder cb = context.getSession().getCriteriaBuilder();

            CriteriaQuery<CraftEntity> query =
                    cb.createQuery(CraftEntity.class);

            Root<CraftEntity> root = query.from(CraftEntity.class);
            query.select(root)
                    .where(
                            cb.equal(root.get("isActive"), true)
                    );

            entities = context.getSession()
                    .createQuery(query)
                    .list();
        }

        return entities;
    }

    public void Save(Object entity)
    {
        try(DataContext context = new DataContext())
        {
            context.getSession().saveOrUpdate(entity);
        }
    }

}
