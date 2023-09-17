package com.github.chenyuxin.commonframework.test.xml;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;


@XStreamAlias("CityList")
public class CityList {
	
	private City capital;
	
    private List<City> citys = new ArrayList<City>();
    
    
	public List<City> getCitys() {
		return citys;
	}

	public void setCityList(List<City> citys) {
		this.citys = citys;
	}
	
	public City getCapital() {
		return capital;
	}

	public void setCapital(City capital) {
		this.capital = capital;
	}

	public void addCity(City city){
		citys.add(city);
	}
 
}