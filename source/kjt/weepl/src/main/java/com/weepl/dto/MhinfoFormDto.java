package com.weepl.dto;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;

import org.modelmapper.ModelMapper;

import com.weepl.entity.Mhinfo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
public class MhinfoFormDto {
	
	private String mhinfoCate;
	
	private Long cd;
	
	@NotBlank(message="제목은 필수 입력 값입니다.")
	private String title;

	@NotBlank(message="내용은 필수 입력 값입니다.")
	private String content;
	
	private int likeCnt;
	
	private List<BoardImgDto> boardImgDtoList = new ArrayList<>();
	
	private List<Long> boardImgCds = new ArrayList<>();
	
	private static ModelMapper modelMapper = new ModelMapper();
	
	public Mhinfo createMhinfo() {
		return modelMapper.map(this, Mhinfo.class);
	} // dto -> entity
	
	public static MhinfoFormDto of(Mhinfo mhinfo) {
		return modelMapper.map(mhinfo, MhinfoFormDto.class);
	} // entity -> dto
	
	public MhinfoFormDto() {
	}
	
	public MhinfoFormDto(String mhinfoCate) {
		this.mhinfoCate = mhinfoCate;
	}
}