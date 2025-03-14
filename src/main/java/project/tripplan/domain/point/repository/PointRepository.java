package project.tripplan.domain.point.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import project.tripplan.domain.point.entity.Point;

public interface PointRepository extends JpaRepository<Point, Long> {
}
