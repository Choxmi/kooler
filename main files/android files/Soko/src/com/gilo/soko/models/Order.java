package com.gilo.soko.models;

import java.io.Serializable;
import java.util.ArrayList;

public class Order implements Serializable{
	
	int id, order_number;
	int customer_id;
	String status, ordered_on, shipped_on;
	double tax, total, subtotal, gift_card_discount, coupon_discount, shipping;
	String shipping_notes, shipping_method, notes, payment_info, referral, company, firstname, lastname, phone, email, ship_company, ship_firstname, ship_lastname, ship_email, ship_phone, ship_address1, ship_address2, ship_city, ship_zip, ship_zone, ship_zone_id, ship_country, ship_country_code, ship_country_id, bill_company,bill_firstname, bill_lastname, bill_email,bill_phone, bill_address1, bill_address2, bill_city, bill_zip, bill_zone, bill_zone_id, bill_country, bill_country_code, bill_country_id;
	
	ArrayList<OrderItem> orderItems;
	
	
	public ArrayList<OrderItem> getOrderItems() {
		return orderItems;
	}
	public void setOrderItems(ArrayList<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getOrder_number() {
		return order_number;
	}
	public void setOrder_number(int order_number) {
		this.order_number = order_number;
	}
	public int getCustomer_id() {
		return customer_id;
	}
	public void setCustomer_id(int customer_id) {
		this.customer_id = customer_id;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getOrdered_on() {
		return ordered_on;
	}
	public void setOrdered_on(String ordered_on) {
		this.ordered_on = ordered_on;
	}
	public String getShipped_on() {
		return shipped_on;
	}
	public void setShipped_on(String shipped_on) {
		this.shipped_on = shipped_on;
	}
	public double getTax() {
		return tax;
	}
	public void setTax(double tax) {
		this.tax = tax;
	}
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	public double getSubtotal() {
		return subtotal;
	}
	public void setSubtotal(double subtotal) {
		this.subtotal = subtotal;
	}
	public double getGift_card_discount() {
		return gift_card_discount;
	}
	public void setGift_card_discount(double gift_card_discount) {
		this.gift_card_discount = gift_card_discount;
	}
	public double getCoupon_discount() {
		return coupon_discount;
	}
	public void setCoupon_discount(double coupon_discount) {
		this.coupon_discount = coupon_discount;
	}
	public double getShipping() {
		return shipping;
	}
	public void setShipping(double shipping) {
		this.shipping = shipping;
	}
	public String getShipping_notes() {
		return shipping_notes;
	}
	public void setShipping_notes(String shipping_notes) {
		this.shipping_notes = shipping_notes;
	}
	public String getShipping_method() {
		return shipping_method;
	}
	public void setShipping_method(String shipping_method) {
		this.shipping_method = shipping_method;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public String getPayment_info() {
		return payment_info;
	}
	public void setPayment_info(String payment_info) {
		this.payment_info = payment_info;
	}
	public String getReferral() {
		return referral;
	}
	public void setReferral(String referral) {
		this.referral = referral;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getShip_company() {
		return ship_company;
	}
	public void setShip_company(String ship_company) {
		this.ship_company = ship_company;
	}
	public String getShip_firstname() {
		return ship_firstname;
	}
	public void setShip_firstname(String ship_firstname) {
		this.ship_firstname = ship_firstname;
	}
	public String getShip_lastname() {
		return ship_lastname;
	}
	public void setShip_lastname(String ship_lastname) {
		this.ship_lastname = ship_lastname;
	}
	public String getShip_email() {
		return ship_email;
	}
	public void setShip_email(String ship_email) {
		this.ship_email = ship_email;
	}
	public String getShip_phone() {
		return ship_phone;
	}
	public void setShip_phone(String ship_phone) {
		this.ship_phone = ship_phone;
	}
	public String getShip_address1() {
		return ship_address1;
	}
	public void setShip_address1(String ship_address1) {
		this.ship_address1 = ship_address1;
	}
	public String getShip_address2() {
		return ship_address2;
	}
	public void setShip_address2(String ship_address2) {
		this.ship_address2 = ship_address2;
	}
	public String getShip_city() {
		return ship_city;
	}
	public void setShip_city(String ship_city) {
		this.ship_city = ship_city;
	}
	public String getShip_zip() {
		return ship_zip;
	}
	public void setShip_zip(String ship_zip) {
		this.ship_zip = ship_zip;
	}
	public String getShip_zone() {
		return ship_zone;
	}
	public void setShip_zone(String ship_zone) {
		this.ship_zone = ship_zone;
	}
	public String getShip_zone_id() {
		return ship_zone_id;
	}
	public void setShip_zone_id(String ship_zone_id) {
		this.ship_zone_id = ship_zone_id;
	}
	public String getShip_country() {
		return ship_country;
	}
	public void setShip_country(String ship_country) {
		this.ship_country = ship_country;
	}
	public String getShip_country_code() {
		return ship_country_code;
	}
	public void setShip_country_code(String ship_country_code) {
		this.ship_country_code = ship_country_code;
	}
	public String getShip_country_id() {
		return ship_country_id;
	}
	public void setShip_country_id(String ship_country_id) {
		this.ship_country_id = ship_country_id;
	}
	public String getBill_company() {
		return bill_company;
	}
	public void setBill_company(String bill_company) {
		this.bill_company = bill_company;
	}
	public String getBill_firstname() {
		return bill_firstname;
	}
	public void setBill_firstname(String bill_firstname) {
		this.bill_firstname = bill_firstname;
	}
	public String getBill_lastname() {
		return bill_lastname;
	}
	public void setBill_lastname(String bill_lastname) {
		this.bill_lastname = bill_lastname;
	}
	public String getBill_email() {
		return bill_email;
	}
	public void setBill_email(String bill_email) {
		this.bill_email = bill_email;
	}
	public String getBill_phone() {
		return bill_phone;
	}
	public void setBill_phone(String bill_phone) {
		this.bill_phone = bill_phone;
	}
	public String getBill_address1() {
		return bill_address1;
	}
	public void setBill_address1(String bill_address1) {
		this.bill_address1 = bill_address1;
	}
	public String getBill_address2() {
		return bill_address2;
	}
	public void setBill_address2(String bill_address2) {
		this.bill_address2 = bill_address2;
	}
	public String getBill_city() {
		return bill_city;
	}
	public void setBill_city(String bill_city) {
		this.bill_city = bill_city;
	}
	public String getBill_zip() {
		return bill_zip;
	}
	public void setBill_zip(String bill_zip) {
		this.bill_zip = bill_zip;
	}
	public String getBill_zone() {
		return bill_zone;
	}
	public void setBill_zone(String bill_zone) {
		this.bill_zone = bill_zone;
	}
	public String getBill_zone_id() {
		return bill_zone_id;
	}
	public void setBill_zone_id(String bill_zone_id) {
		this.bill_zone_id = bill_zone_id;
	}
	public String getBill_country() {
		return bill_country;
	}
	public void setBill_country(String bill_country) {
		this.bill_country = bill_country;
	}
	public String getBill_country_code() {
		return bill_country_code;
	}
	public void setBill_country_code(String bill_country_code) {
		this.bill_country_code = bill_country_code;
	}
	public String getBill_country_id() {
		return bill_country_id;
	}
	public void setBill_country_id(String bill_country_id) {
		this.bill_country_id = bill_country_id;
	}


	

}
