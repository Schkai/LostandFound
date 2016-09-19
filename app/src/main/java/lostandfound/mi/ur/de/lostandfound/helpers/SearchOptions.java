package lostandfound.mi.ur.de.lostandfound.helpers;

/**
 * Created by Konstantin on 11.09.2016.
 */

import lostandfound.mi.ur.de.lostandfound.helpers.Filter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SearchOptions {
    private String searchText;
    private String sortColumn;
    private Comparator sortComparator;
    private boolean sortAscending;
    private List<Filter> filters = new ArrayList();
    private List<Filter> fixedFilters = new ArrayList();

    public SearchOptions() {
    }

    public SearchOptions(String searchText, String sortColumn, Comparator sortComparator, boolean sortAscending) {
        this.searchText = searchText;
        this.sortColumn = sortColumn;
        this.sortComparator = sortComparator;
        this.sortAscending = sortAscending;
    }

    public String getSearchText() {
        return this.searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public Comparator getSortComparator() {
        return this.sortComparator;
    }

    public void setSortComparator(Comparator sortComparator) {
        this.sortComparator = sortComparator;
    }

    public String getSortColumn() {
        return this.sortColumn;
    }

    public void setSortColumn(String sortColumn) {
        this.sortColumn = sortColumn;
    }

    public boolean isSortAscending() {
        return this.sortAscending;
    }

    public void setSortAscending(boolean sortAscending) {
        this.sortAscending = sortAscending;
    }

    public void addFilter(Filter filter) {
        if (this.filters == null) {
            this.filters = new ArrayList();
        }

        this.filters.add(filter);
    }

    public List<Filter> getFilters() {
        return this.filters;
    }

    public void setFilters(List<Filter> filters) {
        this.filters = filters;
    }

    public void addFixedFilter(Filter filter) {
        if (this.fixedFilters == null) {
            this.fixedFilters = new ArrayList();
        }

        this.fixedFilters.add(filter);
    }

    public List<Filter> getFixedFilters() {
        return this.fixedFilters;
    }
}
