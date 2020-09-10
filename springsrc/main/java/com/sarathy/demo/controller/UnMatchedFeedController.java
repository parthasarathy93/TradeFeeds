package com.sarathy.demo.controller;

import com.sarathy.demo.criteria.SearchCriteria;
import com.sarathy.demo.criteria.SearchOperation;
import com.sarathy.demo.criteria.SpecSearchCriteria;
import com.sarathy.demo.repository.TradeRecord;
import com.sarathy.demo.repository.TradeSpecification;
import com.sarathy.demo.repository.UnMatchedCacheService;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/feeds")

public class UnMatchedFeedController {


    @GetMapping
    public Page<String> getAllUnMatchedOrders(
            @RequestParam(name="page",required = false, defaultValue ="0" ) int page,
            @RequestParam(name="stock", required=false ,defaultValue="")String stock,
            @RequestParam(name="price", required=false,defaultValue = "")String price
    )
    {

        //return this.tradeRecordService.searchUser(params);
        List<String> fooList=UnMatchedCacheService.getInstance().getListOfFeeds(stock,price);
        Pageable paging = PageRequest.of(page, 100, Sort.Direction.DESC,"id");
        int start = (int) paging.getOffset();
        int end = (int) ((start + paging.getPageSize()) > fooList.size() ? fooList.size()
                : (start + paging.getPageSize()));
        return  new PageImpl<String>(fooList.subList(start, end), paging, fooList.size());

    }


}
