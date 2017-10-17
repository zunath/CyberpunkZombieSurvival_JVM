package Data.Repository;

import Data.DataContext;
import Entities.AuthorizedDMEntity;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class AuthorizedDMRepository {

    public AuthorizedDMEntity getByCDKey(String cdKey)
    {
        AuthorizedDMEntity entity;

        try(DataContext context = new DataContext())
        {
            CriteriaBuilder cb = context.getSession().getCriteriaBuilder();

            CriteriaQuery<AuthorizedDMEntity> query =
                    cb.createQuery(AuthorizedDMEntity.class);

            Root<AuthorizedDMEntity> root = query.from(AuthorizedDMEntity.class);
            query.select(root)
                    .where(
                            cb.equal(root.get("cdKey"), cdKey),
                            cb.equal(root.get("isActive"), true)
                    );

            entity = context.getSession()
                    .createQuery(query)
                    .uniqueResult();
        }

        return entity;
    }

}
