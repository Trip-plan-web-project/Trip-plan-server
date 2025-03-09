package project.tripplan.domain.admin.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import project.tripplan.domain.admin.dto.AddPointListReq;
import project.tripplan.domain.admin.dto.ReportedPlanCommentsRes;
import project.tripplan.domain.admin.dto.ReportedPlanListRes;
import project.tripplan.domain.admin.dto.ReportedReviewCommentsRes;
import project.tripplan.domain.admin.dto.ReportedReviewRes;
import project.tripplan.domain.point.entity.Point;
import project.tripplan.domain.point.enums.PointStatus;
import project.tripplan.domain.point.repository.PointRepositoryCustom;
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
	private final PointRepositoryCustom pointRepositoryCustom;

	@Transactional(readOnly = true)
	public Page<?> getReportedHistory(Integer category, Long reasonId, String startDate, String endDate, int page,
		int size) {
			if (category == null) {
				throw new IllegalArgumentException("카테고리는 필수입니다.");
			}

			Pageable pageable = PageRequest.of(page, size);

			switch (category) {
				case 1: // 게시글 신고 목록 검색
					return planReportReasonRepositoryCustom.searchReportedPlanList(pageable, reasonId, startDate, endDate);
				case 2: // 게시글 댓글 신고 목록 검색:
					return planCommentReportReasonRepositoryCustom.searchReportedPlanComments(pageable, reasonId, startDate,
						endDate);
				case 3: // 리뷰글 신고 목록 검색
					return reviewReportReasonRepositoryCustom.searchReportedReviews(pageable, reasonId, startDate, endDate);
				case 4: // 리뷰글 댓글 신고 목록 검색
					return revCommentReportReasonCustom.searchReportedReviewComments(pageable, reasonId, startDate,
						endDate);
				default:
					throw new IllegalArgumentException("잘못된 카테고리 값입니다: " + category);
			}
	}

	@Transactional(readOnly = true)
	public Page<?> getPointHistory(Integer category, String startDate, String endDate, int page, int size) {
		if (category == null) {
			throw new IllegalArgumentException("카테고리는 필수입니다.");
		} else if (category >= 3) {
			throw new IllegalArgumentException("잘못된 카테고리 값입니다.");
		}

		Pageable pageable = PageRequest.of(page, size);

		return pointRepositoryCustom.findPointHistory(pageable, category, startDate, endDate);
	}

	@Transactional
	public void addPoint(AddPointListReq pointIds) {
		List<Point> findPointList = pointRepositoryCustom.findAllByIdWithUserIds(pointIds.getPointIds());

		for (Point point : findPointList) {
			point.getUser().addPoint(point.getPoint());
			point.changePointStatus(PointStatus.COMPLETED);
		}
	}
}
