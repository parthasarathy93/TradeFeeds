package com.sarathy.demo.controller;


import com.sarathy.demo.criteria.SearchCriteria;
import com.sarathy.demo.criteria.SearchOperation;
import com.sarathy.demo.criteria.SpecSearchCriteria;
import com.sarathy.demo.repository.ITradeRecordService;
import com.sarathy.demo.repository.TradeRecord;
import com.sarathy.demo.repository.TradeSpecification;
import com.sarathy.demo.repository.TradeSpecificationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/trades")

public class TradeController {


    @Autowired
    private ITradeRecordService tradeRecordService;


    @GetMapping("/all")
    public Page<TradeRecord> getAllTradesWithPagination(
            @RequestParam(name="page",required = false, defaultValue ="0" ) int page)
    {

        Pageable paging = PageRequest.of(page,100, Sort.Direction.DESC,"id");
        //return this.tradeRecordService.searchUser(params);
        return this.tradeRecordService.findAll(paging);

    }

   @GetMapping
    public Page<TradeRecord> getFilteredTrades(
            @RequestParam(name="buyer",required = false,defaultValue = "") String search,
            @RequestParam(name="stock",required = false,defaultValue = "") String  stock,
            @RequestParam(name="tradedate",required = false,defaultValue = "") String date,
            @RequestParam(name="page",required = false,defaultValue = "0") int page) {


       List<SpecSearchCriteria> params = new ArrayList<SpecSearchCriteria>();
       if (!search.isEmpty()) {
           params.add(new SpecSearchCriteria("buyer", SearchOperation.LIKE, search));

       }
       if (!stock.isEmpty()) {
           params.add(new SpecSearchCriteria("stock", SearchOperation.LIKE, stock));

       }


       if (params.size() > 0) {
           Specification result = new TradeSpecification(params.get(0));
           for (int i = 1; i < params.size(); i++) {

               result.and(new TradeSpecification(params.get(i)));
           }
           List<TradeRecord> tradeList= tradeRecordService.findAll(result);

           Predicate<TradeRecord> byDate = x -> x.getTradedate().equals(date);
           List<TradeRecord> fooList= new ArrayList<>();

           if(!date.isEmpty())
           {
               fooList=tradeList.stream().filter(byDate).collect(Collectors.toList());
           }
           else

           {
               fooList=tradeList;
           }
           Pageable paging = PageRequest.of(page, fooList.size(), Sort.Direction.DESC,"id");
          int start = (int) paging.getOffset();
           int end = (int) ((start + paging.getPageSize()) > fooList.size() ? fooList.size()
                   : (start + paging.getPageSize()));
           return  new PageImpl<TradeRecord>(fooList.subList(start, end), paging, fooList.size());
       }
     return null;
   }



}




