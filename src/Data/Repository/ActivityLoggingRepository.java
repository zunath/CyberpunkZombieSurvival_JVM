package Data.Repository;

import Data.DataContext;
import Entities.ChatChannelEntity;
import Entities.ChatLogEntity;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class ActivityLoggingRepository {

    public ChatChannelEntity GetChatChannelByID(int chatChannelID)
    {
        try(DataContext context = new DataContext())
        {
            CriteriaBuilder cb = context.getSession().getCriteriaBuilder();

            CriteriaQuery<ChatChannelEntity> query =
                    cb.createQuery(ChatChannelEntity.class);

            Root<ChatChannelEntity> root = query.from(ChatChannelEntity.class);
            query.select(root)
                .where(
                    cb.equal(root.get("chatChannelID"), chatChannelID)
                );

            return context.getSession()
                    .createQuery(query)
                    .uniqueResult();
        }
    }


    public void Save(ChatLogEntity entity)
    {
        try(DataContext context = new DataContext())
        {
            context.getSession().saveOrUpdate(entity);
        }
    }

}
