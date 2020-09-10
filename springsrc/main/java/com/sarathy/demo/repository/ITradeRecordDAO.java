package com.sarathy.demo.repository;


import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ITradeRecordDAO extends JpaRepository<TradeRecord, Long> {

    List<TradeRecord> findAll(Specification<TradeRecord> tr);


}
