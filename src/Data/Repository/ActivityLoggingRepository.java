package Data.Repository;

import Data.DataContext;
import Entities.ChatChannelEntity;
import Entities.ChatLogEntity;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

public class ActivityLoggingRepository {

    public ChatChannelEntity GetChatChannelByID(int chatChannelID)
    {
        try(DataContext context = new DataContext())
        {
            Criteria criteria = context.getSession()
                    .createCriteria(ChatChannelEntity.class)
                    .add(Restrictions.eq("chatChannelID", chatChannelID));

            return (ChatChannelEntity)criteria.uniqueResult();
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
