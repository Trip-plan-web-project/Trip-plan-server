package project.tripplan.domain.auth.refreshToken.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import project.tripplan.domain.auth.refreshToken.entity.BlackList;

public interface BlackListRepository extends CrudRepository<BlackList, String> {
}
