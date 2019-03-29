package com.carshiring.models;

import java.util.List;

/**
 * Created by Rakhi on 12/19/2018.
 */
public class PassData {
    private static List<SearchData>searchDataList;

    public static List<SearchData> getSearchDataList() {
        return searchDataList;
    }

    public static void setSearchDataList(List<SearchData> searchDataList) {
        PassData.searchDataList = searchDataList;
    }
}
