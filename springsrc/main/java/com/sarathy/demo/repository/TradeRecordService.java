package com.sarathy.demo.repository;



import com.sarathy.demo.criteria.SearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Service
public class TradeRecordService implements ITradeRecordService {

    @Autowired
    private ITradeRecordDAO dao;

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public Page<TradeRecord> findAll(Pageable pageable) {
        return this.dao.findAll(pageable);
    }

    @Override
    public List<TradeRecord> findAll(Specification<TradeRecord> tr) {
        return dao.findAll(tr);

    }

    @Override
    public List<TradeRecord> searchUser(final List<SearchCriteria> params) {
        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<TradeRecord> query = builder.createQuery(TradeRecord.class);
        final Root r = query.from(TradeRecord.class);

        Predicate predicate = builder.conjunction();
        TradeQuery searchConsumer = new TradeQuery(predicate, builder, r);
        params.stream().forEach(searchConsumer);
        predicate = searchConsumer.getPredicate();
        query.where(predicate);

        return entityManager.createQuery(query).getResultList();
    }
}
