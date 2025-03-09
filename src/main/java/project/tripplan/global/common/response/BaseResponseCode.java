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
	USER_BOOKMARK_GET_SUCCESS(true, 1006, "찜한 게시물 목록 조회에 성공했습니다.", HttpStatus.OK),
	USER_COMMENTS_GET_SUCCESS(true, 1007, "댓글 목록 조회에 성공했습니다.", HttpStatus.OK),
	LOGOUT_SUCCESS(true, 1008, "로그아웃에 성공했습니다.", HttpStatus.OK),
	GET_PlANS_DRAFTS_SUCCESS(true, 1009, "임시저장 목록 조회에 성공했습니다.", HttpStatus.OK),
	GET_USER_POINT_HISTORY_SUCCESS(true, 1010, "포인트 지급 내역 조회에 성공했습니다.", HttpStatus.OK),

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
	GET_HOME_SUCCESS(true, 1407, "홈화면 불러오기에 성공했습니다.", HttpStatus.OK),
	GET_PLAN_COMMENTS_LIST_SUCCESS(true, 1408, "게시글 댓글 목록 불러오기에 성공했습니다.", HttpStatus.OK),
	DELETE_PLAN_SUCCESS(true, 1409, "게시글 삭제에 성공했습니다..", HttpStatus.OK),
	COPY_PLAN_SUCCESS(true, 1410, "게시글 복사에 성공했습니다..", HttpStatus.OK),
	UPDATE_PLAN_SUCCESS(true, 1411, "게시글 수정에 성공했습니다..", HttpStatus.OK),

	// planLike 1500 ~
	ADD_PLAN_LIKE_SUCCESS(true, 1501, "게시글 좋아요가 추가되었습니다.", HttpStatus.OK),
	DELETE_PLAN_LIKE_SUCCESS(true, 1502, "게시글 좋아요가 취소되었습니다.", HttpStatus.OK),

	// report 1600 ~
	REPORT_PLAN_COMMENT_SUCCESS(true, 1601, "여행 계획글 댓글 신고가 완료되었습니다.", HttpStatus.OK),
	REPORT_PLAN_SUCCESS(true, 1602, "여행 계획글 신고가 완료되었습니다.", HttpStatus.OK),
	REPORT_REVIEW_SUCCESS(true, 1603, "리뷰글 신고가 완료되었습니다.", HttpStatus.OK),
	REPORT_REVIEW_COMMENT_SUCCESS(true, 1604, "리뷰글 댓글 신고가 완료되었습니다.", HttpStatus.OK),

	// review 1700 ~
	ADD_REVIEW_SUCCESS(true, 1701, "후기 게시글이 생성되었습니다.", HttpStatus.OK),
	GET_REVIEW_SUCCESS(true, 1702, "후기 게시글 조회에 성공했습니다.", HttpStatus.OK),
	UPLOAD_REVIEW_IMAGE_SUCCESS(true, 1703, "리뷰 이미지 업로드에 성공했습니다..", HttpStatus.OK),
	GET_REVIEW_COMMENTS_LIST_SUCCESS(true, 1704, "리뷰 댓글 목록 불러오기에 성공했습니다.", HttpStatus.OK),
	DELETE_REVIEW_SUCCESS(true, 1705, "리뷰 삭제가 완료되었습니다.", HttpStatus.OK),

	// admin 1800 ~
	GET_REPORTED_PLAN_LIST_SUCCESS(true, 1801, "신고받은 계획글 목록 조회에 성공했습니다", HttpStatus.OK),
	GET_REPORTED_PLAN_COMMENT_LIST_SUCCESS(true, 1802, "신고받은 계획글 댓글 목록 조회에 성공했습니다.", HttpStatus.OK),
	SEARCH_REPORTED_LIST_SUCCESS(true, 1803, "신고목록 필터 검색에 성공했습니다.", HttpStatus.OK),
	GET_REPORTED_REVIEW_LIST_SUCCESS(true, 1804, "신고받은 리뷰글 목록 조회에 성공했습니다.", HttpStatus.OK),
	GET_REPORTED_REVIEW_COMMENT_LIST_SUCCESS(true, 1805, "신고받은 리뷰글 댓글 목록 조회에 성공했습니다.", HttpStatus.OK),
	GET_POINT_HISTORY_SUCCESS(true, 1806, "포인트 적립 대기 목록 조회에 성공했습니다.", HttpStatus.OK),
	ADD_POINT_SUCCESS(true, 1807, "포인트 적립에 성공했습니다.", HttpStatus.OK),

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
	CATEGORY_NOT_EXIST(false, 2102, "존재하지 않는 카테고리입니다.", HttpStatus.NOT_FOUND),
	PLAN_DAY_DETAIL_NOT_EXIST(false, 2103, "해당 일차의 상세정보가 존재하지 않습니다.", HttpStatus.NOT_FOUND),
	UNAUTHORIZED_POST_UPDATE_STATUS(false, 2104, "본인의 게시글상태만 수정할 수 있습니다.", HttpStatus.FORBIDDEN),
	UNAUTHORIZED_POST_DELETE_STATUS(false, 2105, "본인의 게시글만 삭제할 수 있습니다.", HttpStatus.FORBIDDEN),

	//comment 2200 ~
	COMMENT_NOT_EXIST(false, 2201, "존재하지 않는 댓글입니다.", HttpStatus.NOT_FOUND),
	UNAUTHORIZED_DELETE_COMMENT(false, 2202, "본인 댓글만 삭제 가능합니다.", HttpStatus.FORBIDDEN),
	UNAUTHORIZED_UPDATE_COMMENT(false, 2203, "본인 댓글만 수정 가능합니다.", HttpStatus.FORBIDDEN),

	//bookmark 2300 ~
	BOOKMARK_NOT_EXIST(false, 2301, "존재하지 않는 즐겨찾기항목 입니다.", HttpStatus.NOT_FOUND),
	UNAUTHORIZED_BOOKMARK_DELETE(false, 2302, "자신의 즐겨찾기만 제거할 수 있습니다.", HttpStatus.FORBIDDEN),

	//planPlace & planTrans & planCategory 2400 ~
	GET_PLAN_PLACE_FAIL(false, 2401, "해당 게시글의 장소 카테고리를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
	GET_PLAN_TRANS_FAIL(false, 2402, "해당 게시글의 교통수단을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
	POST_PLAN_PLACE_FAIL(false, 2403, "해당 장소의 depth는 찾을 수 없습니다.", HttpStatus.NOT_FOUND),

	//planLike 2500 ~
	PLANLIKE_NOT_EXIST(false, 2501, "게시글 좋아요 ID가 존재하지 않습니다.", HttpStatus.NOT_FOUND),
	UNAUTHORIZED_PLANLIKE_DELETE(false, 2502, "본인이 누른 좋아요만 취소 가능합니다.", HttpStatus.FORBIDDEN),

	//report 2600 ~
	ALREADY_REPORTED_PLAN_COMMENT(false, 2601, "이미 신고한적 있는 게획글 댓글입니다.", HttpStatus.CONFLICT),
	ALREADY_REPORTED_PLAN(false, 2602, "이미 신고한적 있는 계획글입니다.", HttpStatus.CONFLICT),
	ALREADY_REPORTED_REVIEW(false, 2603, "이미 신고한적 있는 리뷰글입니다.", HttpStatus.CONFLICT),
	ALREADY_REPORTED_REVIEW_COMMENT(false, 2604, "이미 신고한적 있는 리뷰글 댓글입니다.", HttpStatus.CONFLICT),

	//review 2700 ~
	REVIEW_NOT_EXIST(false, 2701, "존재하지 않는 후기게시글 입니다.", HttpStatus.NOT_FOUND),
	REVIEW_COMMENT_NOT_EXIST(false, 2702, "존재하지 않는 후기댓글 입니다.", HttpStatus.NOT_FOUND),
	UNAUTHORIZED_DELETE_REVIEW(false, 2703, "본인 후기글만 삭제 가능합니다.", HttpStatus.FORBIDDEN),

	//valid && Token 2900 ~
	VALIDATION_FAILED(false, 2900, "Bind Exception", HttpStatus.BAD_REQUEST),
	EXTRACT_EXPIRATION_FAILED(false, 2901, "토큰 만료시간 추출 실패", HttpStatus.INTERNAL_SERVER_ERROR),
	EXTRACT_ACCESSTOKEN_FAILED(false, 2902, "헤더에서 accessToken 추출 실패", HttpStatus.INTERNAL_SERVER_ERROR),
	EXTRACT_REFRESHTOKEN_FAILED(false, 2903, "헤더에서 refreshToken 추출 실패", HttpStatus.INTERNAL_SERVER_ERROR),

	/**
	 * 3000 : response 오류
	 */
	FILE_FORMAT_FAIL(false, 3000, "잘못된 형식의 파일 입니다.", HttpStatus.BAD_REQUEST),

	/**
	 * 4000 : server 오류
	 */
	INTERNAL_SERVER_ERROR(false, 4001, "서버 오류입니다.", HttpStatus.INTERNAL_SERVER_ERROR),
	FILE_UPLOAD_ERROR(false, 4002, "파일 업로드에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
	JSON_PARSING_ERROR(false, 4003, "JSON 파싱에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
	;

	private final Boolean status;
	private final Integer code;
	private final String message;
	private final HttpStatus httpStatus;
}
