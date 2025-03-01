package project.tripplan.domain.admin.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import project.tripplan.domain.admin.dto.ReportedPlanCommentsRes;
import project.tripplan.domain.admin.dto.ReportedPlanListRes;
import project.tripplan.domain.report.repository.planCommentReportReasonRepo.PlanCommentReportReasonRepositoryCustom;
import project.tripplan.domain.report.repository.planReportReasonRepo.PlanReportReasonRepositoryCustom;

@Service
@RequiredArgsConstructor
public class AdminService {

	private final PlanReportReasonRepositoryCustom planReportReasonRepositoryCustom;
	private final PlanCommentReportReasonRepositoryCustom planCommentReportReasonRepositoryCustom;

	@Transactional(readOnly = true)
	public Page<ReportedPlanListRes> getReportedPlanList(int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		return planReportReasonRepositoryCustom.findReportedPlanListByDto(pageable);
	}

	@Transactional(readOnly = true)
	public Page<ReportedPlanCommentsRes> getReportedPlanCommentList(int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		return planCommentReportReasonRepositoryCustom.findReportedPlanCommentsByDto(pageable);
	}

	@Transactional(readOnly = true)
	public Object getReportedSearchList(Long category, Long reasonId, String startDate, String endDate, int page,
		int size) {

	}
}
