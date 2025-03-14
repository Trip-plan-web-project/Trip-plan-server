package project.tripplan.domain.point.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import project.tripplan.domain.admin.dto.PointHistoryRes;
import project.tripplan.domain.point.entity.Point;

public interface PointRepositoryCustom {
	Page<PointHistoryRes> findPointHistory(Pageable pageable, Integer category, String startDate, String endDate);

	List<Point> findAllByIdWithUserIds(List<Long> ids);

	Page<Point> findAllWithUser(Pageable pageable, Long userId);

}
