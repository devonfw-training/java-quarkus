package org.example.app.general.dataaccess;

import org.example.app.general.common.search.SearchCriteria;

class TestSearchCriteria extends SearchCriteria {
    private boolean determineTotal;

    @Override
    public boolean isDetermineTotal() {
        return determineTotal;
    }

    public void setDetermineTotal(boolean determineTotal) {
        this.determineTotal = determineTotal;
    }
}