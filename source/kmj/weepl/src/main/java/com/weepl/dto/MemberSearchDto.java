package com.weepl.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MemberSearchDto {

	private String searchDateType;
	private String searchBy;
	private String searchQuery="";
}