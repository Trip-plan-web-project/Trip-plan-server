package project.tripplan.global.common.response;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BaseResponseCode {

	/**
	 * 1000 : 성공
	 */

	//user 1000~
	USER_GET_SUCCESS(true, 1001, "회원 정보 가져오기에 성공했습니다.", HttpStatus.OK),
	USER_UPDATE_SUCCESS(true, 1002, "회원 업데이트에 성공했습니다.", HttpStatus.OK),
	LOGIN_SUCCESS(true, 1003, "로그인에 성공했습니다.", HttpStatus.OK),
	REISSUE_TOKEN_SUCCESS(true, 1004, "토큰 재발급에 성공했습니다.", HttpStatus.OK),
	USER_PLAN_GET_SUCCESS(true, 1005, "내 여행 일정 목록 조회에 성공했습니다.", HttpStatus.OK),

	//comment 1200 ~
	ADD_COMMENT_SUCCESS(true, 1201, "댓글을 추가하였습니다.", HttpStatus.OK),
	DELETE_COMMENT_SUCCESS(true, 1202, "댓글을 삭제하였습니다.", HttpStatus.OK),
	UPDATE_COMMENT_SUCCESS(true, 1203, "댓글을 수정하였습니다.", HttpStatus.OK),

	//bookmark 1300 ~
	ADD_BOOKMARK_SUCCESS(true, 1301, "해당 게시글이 즐겨찾기에 추가되었습니다.", HttpStatus.OK),
	DELETE_BOOKMARK_SUCCESS(true, 1302, "해당 게시글을 즐겨찾기에서 삭제하였습니다.", HttpStatus.OK),

	//plan 1400 ~
	ADD_PLAN_SUCCESS(true, 1401, "게시글이 생성되었습니다.", HttpStatus.OK),
	UPDATE_PLAN_STATUS_SUCCESS(true, 1402, "게시글 상태가 변경되었습니다.", HttpStatus.OK),
	GET_PLAN_DETAIL_INFO_SUCCESS(true, 1403, "계획글 상세페이지(여행정보) 불러오기에 성공했습니다.", HttpStatus.OK),
	GET_PLAN_DETAIL_DAY_SUCCESS(true, 1404, "해당 일차 동선을 불러오는데 성공했습니다.", HttpStatus.OK),
	GET_PLAN_SEARCH_INFO_SUCCESS(true, 1405, "계획글 검색 불러오기에 성공했습니다.", HttpStatus.OK),
	GET_PLAN_SEARCH_CONDITION_SUCCESS(true, 1406, "계획글 조건 검색 불러오기에 성공했습니다.", HttpStatus.OK),
	GET_HOME_SUCCESS(true, 1207, "홈화면 불러오기에 성공했습니다.", HttpStatus.OK),

	/**
	 * 2000 ~ 2899: request 오류
	 * 2900 ~ 3000: validation 오류
	 */

	//user 2000 ~
	USER_NOT_EXIST(false, 2001, "존재하지 않는 회원입니다.", HttpStatus.NOT_FOUND),
	REFRESHTOKEN_NOT_EXIST(false, 2002, "리프레시 토큰이 존재하지 않습니다.", HttpStatus.NOT_FOUND),
	INVALID_OAUTH2_REQUEST(false, 2003, "유효하지 않은 OAuth2 요청입니다.", HttpStatus.BAD_REQUEST),
	FILE_DELETE_ERROR(false, 2004, "유저 업데이트에 실패했습니다", HttpStatus.BAD_REQUEST),

	//plan 2100 ~,
	PLAN_NOT_EXIST(false, 2101, "존재하지 않는 게시글입니다.", HttpStatus.NOT_FOUND),
	CATEGORY_NOT_EXIST(false, 2002, "존재하지 않는 카테고리입니다.", HttpStatus.NOT_FOUND),
	PLAN_DAY_DETAIL_NOT_EXIST(false, 2003, "해당 일차의 상세정보가 존재하지 않습니다.", HttpStatus.NOT_FOUND),

	//comment 2200 ~
	COMMENT_NOT_EXIST(false, 2201, "존재하지 않는 댓글입니다.", HttpStatus.NOT_FOUND),

	//bookmark 2300 ~
	BOOKMARK_NOT_EXIST(false, 2301, "존재하지 않는 즐겨찾기항목 입니다.", HttpStatus.NOT_FOUND),
	UNAUTHORIZED_BOOKMARK_DELETE(false, 2302, "자신의 즐겨찾기만 제거할 수 있습니다.", HttpStatus.FORBIDDEN),

	//planPlace & planTrans & planCategory 2400 ~
	GET_PLAN_PLACE_FAIL(false, 2401, "해당 게시글의 장소 카테고리를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
	GET_PLAN_TRANS_FAIL(false, 2402, "해당 게시글의 교통수단을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),

	//valid 2900 ~
	VALIDATION_FAILED(false, 2900, "Bind Exception", HttpStatus.BAD_REQUEST),

	/**
	 * 3000 : response 오류
	 */
	FILE_FORMAT_FAIL(false, 3000, "잘못된 형식의 파일 입니다.", HttpStatus.BAD_REQUEST),

	/**
	 * 4000 : server 오류
	 */
	INTERNAL_SERVER_ERROR(false, 4001, "서버 오류입니다.", HttpStatus.INTERNAL_SERVER_ERROR),
	FILE_UPLOAD_ERROR(false, 4002, "파일 업로드에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
	;

	private final Boolean status;
	private final Integer code;
	private final String message;
	private final HttpStatus httpStatus;
}
