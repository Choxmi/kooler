package com.gilo.soko.models;

import java.io.Serializable;


public class Country implements Serializable{

	
	String id, sequence, name, iso_code_2, iso_code_3, zip_required, status, tax;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIso_code_2() {
		return iso_code_2;
	}

	public void setIso_code_2(String iso_code_2) {
		this.iso_code_2 = iso_code_2;
	}

	public String getIso_code_3() {
		return iso_code_3;
	}

	public void setIso_code_3(String iso_code_3) {
		this.iso_code_3 = iso_code_3;
	}

	public String getZip_required() {
		return zip_required;
	}

	public void setZip_required(String zip_required) {
		this.zip_required = zip_required;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTax() {
		return tax;
	}

	public void setTax(String tax) {
		this.tax = tax;
	}
	
	
	
	
}
