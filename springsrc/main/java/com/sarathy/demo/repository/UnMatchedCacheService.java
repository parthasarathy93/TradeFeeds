package com.sarathy.demo.repository;

import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

public class UnMatchedCacheService {

        private static UnMatchedCacheService unMatchedCacheService;
        private List<String> feeds;
        public static UnMatchedCacheService getInstance()
        {
            if(unMatchedCacheService==null)
            {
                unMatchedCacheService=new UnMatchedCacheService();
            }
            return  unMatchedCacheService;

        }
        public void setFeeds(List<String> feeds)
        {
            this.feeds=feeds;
        }

        public List<String> getFeeds()
        {
            return this.feeds;
        }


      public List<String> getListOfFeeds(String sym, String price) {
          List<String> toReturnList = new ArrayList<>();
          List<String> unFilteredFeeds = UnMatchedCacheService.getInstance().getFeeds();
          if (sym == "" && price == "") {
              toReturnList = unFilteredFeeds;
          } else {
              for (int i = 0; i < unFilteredFeeds.size(); i++) {
                  String feed = unFilteredFeeds.get(i);
                  boolean symbolfound = true;
                  if (sym != "") {
                      symbolfound = feed.contains(sym);
                  }
                  if (price != "") {
                      symbolfound = symbolfound && feed.contains(price);
                  }

                  if (symbolfound) {
                      toReturnList.add(feed);
                  }

              }
          }
          return toReturnList;
      }

    }

