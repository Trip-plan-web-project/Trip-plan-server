package project.tripplan.domain.report.repository.reportReasonRepo;

import java.util.List;

import project.tripplan.domain.report.entity.ReportReason;

public interface ReportReasonRepositoryCustom {
	List<ReportReason> findAllByIds(List<Long> ids);
}
