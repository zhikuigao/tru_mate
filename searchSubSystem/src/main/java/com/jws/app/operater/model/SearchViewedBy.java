package com.jws.app.operater.model;

public class SearchViewedBy implements Comparable<SearchViewedBy>{
	private SearchInfoAll search;
    private Float simValue;
    
    public SearchViewedBy(SearchInfoAll search,Float simValue) {
		this.search = search;
		this.simValue = simValue;
	}


	public SearchInfoAll getSearch() {
		return search;
	}


	public void setSearch(SearchInfoAll search) {
		this.search = search;
	}


	public Float getSimValue() {
		return simValue;
	}

	public void setSimValue(Float simValue) {
		this.simValue = simValue;
	}
    
	@Override
	public int compareTo(SearchViewedBy o) {
		return o.getSimValue().compareTo(this.simValue);
	}
}