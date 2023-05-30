package com.weepl.repository;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.weepl.constant.MhinfoCate;
import com.weepl.dto.MhinfoSearchDto;
import com.weepl.entity.Mhinfo;
import com.weepl.entity.QMhinfo;

public class MhinfoRepositoryCustomImpl implements MhinfoRepositoryCustom {

	private JPAQueryFactory queryFactory;

	public MhinfoRepositoryCustomImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}

	private BooleanExpression searchByCategory(String searchBy, String searchQuery) {
		if ("SCHOOL".equals(searchQuery)) {
			return QMhinfo.mhinfo.mhinfoCate.eq(MhinfoCate.SCHOOL);
		} else if ("MIND".equals(searchQuery)) {
			return QMhinfo.mhinfo.mhinfoCate.eq(MhinfoCate.MIND);
		} else if ("RELATIONSHIP".equals(searchQuery)) {
			return QMhinfo.mhinfo.mhinfoCate.eq(MhinfoCate.RELATIONSHIP);
		}
		return null;
	}

	private BooleanExpression searchByLike(String searchBy, String searchQuery) {

		if (StringUtils.equals("title", searchBy)) {
			return QMhinfo.mhinfo.title.like("%" + searchQuery + "%");
		} else if (StringUtils.equals("createdBy", searchBy)) {
			return QMhinfo.mhinfo.createdBy.like("%" + searchQuery + "%");
		}
		return null;
	}

	@Override
	public Page<Mhinfo> getAdminMhinfoPage(MhinfoSearchDto mhinfoSearchDto, Pageable pageable) {
		BooleanExpression categoryExpression = searchByCategory(mhinfoSearchDto.getSearchByCate(),
				mhinfoSearchDto.getSearchQuery());
		BooleanExpression likeExpression = searchByLike(mhinfoSearchDto.getSearchBy(),
				mhinfoSearchDto.getSearchQuery());

		QueryResults<Mhinfo> results = queryFactory.selectFrom(QMhinfo.mhinfo).where(categoryExpression, likeExpression)
				.orderBy(QMhinfo.mhinfo.cd.desc()).offset(pageable.getOffset()).limit(pageable.getPageSize())
				.fetchResults();

		List<Mhinfo> content = results.getResults();
		long total = results.getTotal();
		return new PageImpl<>(content, pageable, total);
	}

	@Override
	public Page<Mhinfo> getUserMhinfoPage(MhinfoSearchDto mhinfoSearchDto, Pageable pageable) {
		BooleanExpression categoryExpression = searchByCategory(mhinfoSearchDto.getSearchBy(),
				mhinfoSearchDto.getSearchQuery());
		BooleanExpression likeExpression = searchByLike(mhinfoSearchDto.getSearchBy(),
				mhinfoSearchDto.getSearchQuery());

		QueryResults<Mhinfo> results = queryFactory.selectFrom(QMhinfo.mhinfo).where(categoryExpression, likeExpression)
				.orderBy(QMhinfo.mhinfo.cd.desc()).offset(pageable.getOffset()).limit(pageable.getPageSize())
				.fetchResults();

		List<Mhinfo> content = results.getResults();
		long total = results.getTotal();
		return new PageImpl<>(content, pageable, total);
	}

}
