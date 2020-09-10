package com.sarathy.demo.repository;

import com.sarathy.demo.criteria.SearchCriteria;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.function.Consumer;

public class TradeQuery implements Consumer<SearchCriteria> {


    private Predicate predicate;
    private CriteriaBuilder builder;
    private Root r;

    public TradeQuery(Predicate predicate, CriteriaBuilder builder, Root r) {
        super();
        this.predicate = predicate;
        this.builder = builder;
        this.r= r;
    }

    @Override
    public void accept(SearchCriteria param) {

        System.out.println(param.getOperation());
        if (param.getOperation().equalsIgnoreCase(">")) {
            predicate = builder.and(predicate, builder.greaterThanOrEqualTo(r.get(param.getKey()), param.getValue().toString()));
        } else if (param.getOperation().equalsIgnoreCase("<")) {
            predicate = builder.and(predicate, builder.lessThanOrEqualTo(r.get(param.getKey()), param.getValue().toString()));
        } else if (param.getOperation().equalsIgnoreCase(":")) {
            if (r.get(param.getKey()).getJavaType() == String.class) {
                predicate = builder.and(predicate, builder.like(r.get(param.getKey()), "%" + param.getValue() + "%"));
                System.out.println(predicate.getExpressions());
            } else {
                predicate = builder.and(predicate, builder.equal(r.get(param.getKey()), param.getValue()));
            }
        }
    }

    public Predicate getPredicate() {
        return predicate;
    }
}
