package Data.Repository;

import Data.DataContext;
import Entities.*;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import java.util.ArrayList;
import java.util.List;

public class ResearchRepository {

    public List<PCTerritoryFlagsStructuresResearchQueueEntity> GetResearchJobsInQueue(int pcStructureID)
    {
        List<PCTerritoryFlagsStructuresResearchQueueEntity> entities;

        try(DataContext context = new DataContext())
        {
            Criteria criteria = context.getSession()
                    .createCriteria(PCTerritoryFlagsStructuresResearchQueueEntity.class)
                    .add(Restrictions.eq("pcStructureID", pcStructureID))
                    .add(Restrictions.eq("isCanceled", false))
                    .add(Restrictions.isNull("deliverDateTime"));

            entities = criteria.list();
        }

        return entities;
    }

    public PCTerritoryFlagsStructuresResearchQueueEntity GetResearchJobInQueueForSlot(int pcStructureID, int slotID)
    {
        try(DataContext context = new DataContext())
        {
            Criteria criteria = context.getSession()
                    .createCriteria(PCTerritoryFlagsStructuresResearchQueueEntity.class)
                    .add(Restrictions.eq("pcStructureID", pcStructureID))
                    .add(Restrictions.eq("isCanceled", false))
                    .add(Restrictions.isNull("deliverDateTime"))
                    .add(Restrictions.eq("researchSlot", slotID));

            return (PCTerritoryFlagsStructuresResearchQueueEntity) criteria.uniqueResult();
        }
    }

    public List<CraftBlueprintCategoryEntity> GetResearchCategoriesAvailableForCraftAndLevel(int craftID, int researchLevel)
    {
        List<CraftBlueprintCategoryEntity> entities = new ArrayList<>();

        try(DataContext context = new DataContext())
        {
            Criteria criteria = context.getSession()
                    .createCriteria(ResearchBlueprintEntity.class)
                    .createAlias("craftBlueprint", "bp")
                    .createAlias("bp.category", "c")
                    .createAlias("bp.craft", "cr")
                    .setProjection(Projections.distinct(Projections.property("c.craftBlueprintCategoryID")))
                    .add(Restrictions.eq("cr.craftID", craftID))
                    .add(Restrictions.le("skillRequired", researchLevel));
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

    public List<ResearchBlueprintEntity> GetResearchBlueprintsForCategory(int craftID, int categoryID, int researchLevel)
    {
        List<ResearchBlueprintEntity> entities;

        try(DataContext context = new DataContext())
        {
            Criteria criteria = context.getSession()
                    .createCriteria(ResearchBlueprintEntity.class)
                    .createAlias("craftBlueprint", "bp")
                    .createAlias("bp.category", "c")
                    .createAlias("bp.craft", "cr")
                    .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
                    .add(Restrictions.eq("cr.craftID", craftID))
                    .add(Restrictions.eq("c.craftBlueprintCategoryID", categoryID))
                    .add(Restrictions.le("skillRequired", researchLevel));

            entities = criteria.list();
        }

        return entities;
    }

    public ResearchBlueprintEntity GetResearchBlueprintByID(int researchBlueprintID)
    {
        try(DataContext context = new DataContext())
        {
            Criteria criteria = context.getSession()
                    .createCriteria(ResearchBlueprintEntity.class)
                    .add(Restrictions.eq("researchBlueprintID", researchBlueprintID));

            return (ResearchBlueprintEntity) criteria.uniqueResult();
        }
    }

    public void Save(PCTerritoryFlagsStructuresResearchQueueEntity entity)
    {
        try(DataContext context = new DataContext())
        {
            context.getSession().saveOrUpdate(entity);
        }
    }
}
