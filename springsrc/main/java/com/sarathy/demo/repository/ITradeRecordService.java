package com.sarathy.demo.repository;

import com.sarathy.demo.criteria.SearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface ITradeRecordService {

    Page<TradeRecord> findAll(Pageable pageable);

    List<TradeRecord> findAll(Specification<TradeRecord> tr);



    List<TradeRecord> searchUser(List<SearchCriteria> params);


}
