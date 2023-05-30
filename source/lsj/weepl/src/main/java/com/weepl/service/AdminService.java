package com.weepl.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.weepl.constant.MemberStatus;
import com.weepl.constant.RestrictStatus;
import com.weepl.dto.MemberSearchDto;
import com.weepl.dto.ModMemberInfoDto;
import com.weepl.entity.Member;
import com.weepl.entity.MemberRestrict;
import com.weepl.repository.MemberRepository;
import com.weepl.repository.MemberRepositoryCustom;
import com.weepl.repository.MemberRestrictRepository;
import com.weepl.repository.ReserveApplyRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminService {
	private final ReserveApplyRepository reserveApplyRepository;
	
	private final Logger LOGGER = LoggerFactory.getLogger(AdminService.class);
	
	private final MemberRepository memberRepository;

	private final MemberRestrictRepository memberRestrictRepository;

	public List<Member> findMembers() {
		
		return memberRepository.findAll();
	}

	public Member findOne(Long memCd) {
		return memberRepository.findByCd(memCd);
	}

	public Long updateMember(ModMemberInfoDto modMemberInfoDto) throws Exception {
		Member member = memberRepository.findById(modMemberInfoDto.getId());
		member.updateMember(modMemberInfoDto);

		return member.getCd();
	}

	public Member findMember(String id) {
		return memberRepository.findById(id);
	}

	// 회원 삭제
		public void deleteMember(Long memCd) {
			memberRepository.deleteById(memCd);
		}

		// 회원 이용제한
		public void restrictMember(String id) {
			Member member = memberRepository.findById(id);
			member.restrictMember();
		}

		// 일주일 이용제한 Mysql에 저장
		public void restrictMemberForOneWeek(Long memCd) {
			Member member = memberRepository.findById(memCd).orElseThrow(EntityNotFoundException::new);
			member.restrictMember();

			LocalDateTime stdt = LocalDateTime.now();
			LocalDateTime eddt = stdt.plusWeeks(1);

			MemberRestrict memberRestrict = new MemberRestrict();
			memberRestrict.setStdt(stdt);
			memberRestrict.setEddt(eddt);
			memberRestrict.setStatus(RestrictStatus.RESTRICTED);
			memberRestrict.setMember(member);

			if (member.getMemberRestricts() == null) {
				member.setMemberRestricts(new ArrayList<>());
			}

			member.getMemberRestricts().add(memberRestrict); // MemberRestrict 객체를 리스트에 추가

			member.setStatus(MemberStatus.RESTRICT);

			memberRepository.save(member);
		}

		// MemberRestrict 데이터 날리기 ->
		public void cancelMemberRestriction(Long memCd) {
			Member member = memberRepository.findById(memCd).orElseThrow(EntityNotFoundException::new);

			member.setStatus(MemberStatus.GENERAL);

			if (member.getMemberRestricts() != null && !member.getMemberRestricts().isEmpty()) {
				member.getMemberRestricts().clear(); // memberRestricts 리스트에서 모든 MemberRestrict 제거
			}

			memberRepository.save(member);

			// memberRestricts 테이블의 데이터 삭제
			memberRestrictRepository.deleteByMember(member);
		}
		
		@Transactional(readOnly = true)
	    public Page<Member> getAdminMemberInfoPage(MemberSearchDto memberSearchDto, Pageable pageable){
	    	return memberRepository.getAdminMemberInfoPage(memberSearchDto, pageable);
	    }
}