package project.tripplan.domain.admin.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import project.tripplan.domain.admin.dto.ReportedPlanCommentsRes;
import project.tripplan.domain.admin.dto.ReportedPlanListRes;
import project.tripplan.domain.admin.dto.ReportedReviewCommentsRes;
import project.tripplan.domain.admin.dto.ReportedReviewRes;
import project.tripplan.domain.report.repository.planCommentReportReasonRepo.PlanCommentReportReasonRepositoryCustom;
import project.tripplan.domain.report.repository.planReportReasonRepo.PlanReportReasonRepositoryCustom;
import project.tripplan.domain.report.repository.reviewCommentReportReasonRepo.RevCommentReportReasonCustom;
import project.tripplan.domain.report.repository.reviewReportReasonRepo.ReviewReportReasonRepositoryCustom;

@Service
@RequiredArgsConstructor
public class AdminService {

	private final PlanReportReasonRepositoryCustom planReportReasonRepositoryCustom;
	private final PlanCommentReportReasonRepositoryCustom planCommentReportReasonRepositoryCustom;
	private final ReviewReportReasonRepositoryCustom reviewReportReasonRepositoryCustom;
	private final RevCommentReportReasonCustom revCommentReportReasonCustom;

	@Transactional(readOnly = true)
	public Page<ReportedPlanListRes> getReportedPlanList(int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		return planReportReasonRepositoryCustom.findReportedPlanList(pageable);
	}

	@Transactional(readOnly = true)
	public Page<ReportedPlanCommentsRes> getReportedPlanCommentList(int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		return planCommentReportReasonRepositoryCustom.findReportedPlanComments(pageable);
	}

	@Transactional(readOnly = true)
	public Page<ReportedReviewRes> getReportedReviewList(int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		return reviewReportReasonRepositoryCustom.findReportedReviews(pageable);
	}

	@Transactional(readOnly = true)
	public Page<ReportedReviewCommentsRes> getReportedReviewCommentList(int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		return revCommentReportReasonCustom.findReportedReviewComments(pageable);
	}

	@Transactional(readOnly = true)
	public Object getReportedSearchList(Long category, Long reasonId, String startDate, String endDate, int page,
		int size) {
		return null;
	}
}
