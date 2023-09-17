package com.github.chenyuxin.commonframework.dao.common;

import java.util.List;

/**
 * 与CommonDao交互，缓存分页数据的类，分页
 * @param <T>
 */
public class PageBean<T> {
	private int currentPage = 1;//当前页pageNo
	private int pageSize = 10;//分页每页条数
	private int totalPage;//总页数
	private List<T> rows;//当前页的数据
	private int total;//总记录数
	/**
	 * 获取总记录数
	 * @return
	 */
	public int getTotal(){
		return total;
	}
	
	private int startNo;//当前页开始条数号
	/**
	 * 获取当前页开始记录数号
	 * @return
	 */
	public int getStartNo(){
		return startNo;
	}
	/**
	 * 设置当前页开始记录数号
	 */
	private void setStartNo(){
		this.startNo = 1 + this.pageSize * (this.currentPage -1);
	}
	
	private int endNo;//当前页结束条数号
	/**
	 * 获取当前页结束条数号
	 * @return
	 */
	public int getEndNo(){
		return endNo;
	}
	/**
	 * 设置当前页结束条数号
	 */
	private void setEndNo(){
		this.endNo = this.pageSize * this.currentPage;
		if (this.endNo > this.total) {
			this.endNo = (int) this.total;
		}
		if (this.endNo == 0) {
			this.startNo = 0;
		}
	}
	
	//构造方法
	public PageBean() {	}
	
	/**
	  * 分页数据Bean类
	 * @param total 总记录数
	 * @param pageSize 分页每页条数
	 * @param currentPage 当前第几页
	 */
	public PageBean(int total,int pageSize,int currentPage) {
		this.total = total;
		this.pageSize = pageSize;
		if(this.total % pageSize == 0){
			this.totalPage = (int)(total / pageSize);
		}else{
			this.totalPage = (int)(total / pageSize) + 1;
		}
		this.currentPage = currentPage;
		if(this.currentPage > this.totalPage){
			this.currentPage = this.totalPage;
		}
		if(this.currentPage < 1){
			this.currentPage = 1;
		}
		setStartNo();
		setEndNo();
		//System.out.println("开始的记录数： " + this.startNo);
		//System.out.println("结束的记录数： " + this.endNo);
	}
	
	/**
	  * 分页数据Bean类
	 * @param total 总记录数
	 * @param pageSize 分页每页条数
	 * @param currentPage 当前第几页
	 * @param data 当前页的数据
	 */
	public PageBean(int total,int pageSize,int currentPage,List<T> rows) {
		this.total = total;
		this.pageSize = pageSize;
		if(this.total % pageSize == 0){
			this.totalPage = (int)(total / pageSize);
		}else{
			this.totalPage = (int)(total / pageSize) + 1;
		}
		this.currentPage = currentPage;
		if(this.currentPage > this.totalPage){
			this.currentPage = this.totalPage;
		}
		if(this.currentPage < 1){
			this.currentPage = 1;
		}
		setStartNo();
		setEndNo();
		this.rows = rows;
	}
	
	/**
	 * 获取分页每页条数
	 * @return
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * 获取当前页第几页
	 * @return
	 */
	public int getCurrentPage() {
		return currentPage;
	}
	/**
	 * 设置当前页第几页
	 * @param currentPage
	 */
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
		if(this.currentPage > this.totalPage){
			this.currentPage = this.totalPage;
		}
		if(this.currentPage < 1){
			this.currentPage = 1;
		}
		setStartNo();
		setEndNo();
	}
	
	/**
	 * 获取当前页的数据
	 * @return
	 */
	public List<T> getRows() {
		return rows;
	}

	/**
	 * 设置当前页的数据
	 * @param rows
	 */
	public void setRows(List<T> rows) {
		this.rows = rows;
	}
	
	/**
	 * 获取总页数
	 * @return
	 */
	public int getTotalPage() {
		return totalPage;
	}
	
	/*
	public static void main(String[] args) {
		PageBean<Object> pageBean = new PageBean<Object>(20, 5, 5);
		System.out.println(pageBean.startNo);
		System.out.println(pageBean.endNo);
		System.out.println(pageBean.currentPage);
	}
	*/
	
}
