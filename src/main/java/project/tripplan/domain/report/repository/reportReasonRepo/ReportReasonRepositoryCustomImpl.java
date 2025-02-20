package project.tripplan.domain.report.repository.reportReasonRepo;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import project.tripplan.domain.report.entity.QReportReason;
import project.tripplan.domain.report.entity.ReportReason;

@Repository
@RequiredArgsConstructor
public class ReportReasonRepositoryCustomImpl implements ReportReasonRepositoryCustom {

	private final JPAQueryFactory qf;
	private final QReportReason reportReason = QReportReason.reportReason1;

	@Override
	public List<ReportReason> findAllByIds(List<Long> ids) {
		return qf.selectFrom(reportReason)
			.where(reportReason.id.in(ids))
			.fetch();
	}
}
