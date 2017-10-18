package Data.Repository;

import Data.DataContext;
import Data.SqlParameter;
import Entities.AuthorizedDMEntity;
import Entities.PCBadgeEntity;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class AuthorizedDMRepository {

    public AuthorizedDMEntity GetDMByCDKey(String cdKey)
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLSingle("AuthorizedDM/GetDMByCDKey", AuthorizedDMEntity.class,
                    new SqlParameter("cdKey", cdKey));
        }
    }

}
