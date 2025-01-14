package project.tripplan.global.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum BaseResponseCode {

	/**
	 * 1000 : 성공
	 */

	//bookmark 1300 ~
	ADD_BOOKMARK_SUCCESS(true, 1301, "해당 게시글이 즐겨찾기에 추가되었습니다.", HttpStatus.OK),
	DELETE_BOOKMARK_SUCCESS(true,1302,"해당 게시글을 즐겨찾기에서 삭제하였습니다.", HttpStatus.OK),

	//plan 1400 ~
	ADD_PLAN_SUCCESS(true, 1401, "게시글이 생성되었습니다.", HttpStatus.OK),

	/**
	 * 2000 ~ 2899: request 오류
	 * 2900 ~ 3000: validation 오류
	 */

	//user 2000 ~
	USER_NOT_EXIST(false, 2001, "존재하지 않는 회원입니다.", HttpStatus.NOT_FOUND),
	REFRESHTOKEN_NOT_EXIST(false, 2002, "리프레시 토큰이 존재하지 않습니다.", HttpStatus.NOT_FOUND),
	INVALID_OAUTH2_REQUEST(false, 2003, "유효하지 않은 OAuth2 요청입니다.", HttpStatus.BAD_REQUEST),

	//valid 2900 ~
	VALIDATION_FAILED(false,2900,"Bind Exception",HttpStatus.BAD_REQUEST),

	//plan 2100 ~
	PLAN_NOT_EXIST(false, 2101, "존재하지 않는 게시글입니다.", HttpStatus.NOT_FOUND),
	CATEGORY_NOT_EXIST(false,2002,"존재하지 않는 카테고리입니다.",HttpStatus.NOT_FOUND),

	//bookmark 2300 ~
	BOOKMARK_NOT_EXIST(false,2301,"존재하지 않는 즐겨찾기항목 입니다.", HttpStatus.NOT_FOUND),
	UNAUTHORIZED_BOOKMARK_DELETE(false,2302, "자신의 즐겨찾기만 제거할 수 있습니다.", HttpStatus.FORBIDDEN),


	/**
	 * 3000 : response 오류
	 */
	FILE_FORMAT_FAIL(false,3000,"잘못된 형식의 파일 입니다.",HttpStatus.BAD_REQUEST),

	/**
	 * 4000 : server 오류
	 */
	INTERNAL_SERVER_ERROR(false, 4001, "서버 오류입니다.", HttpStatus.INTERNAL_SERVER_ERROR),
	FILE_UPLOAD_ERROR(false,4002,"파일 업로드에 실패했습니다.",HttpStatus.INTERNAL_SERVER_ERROR);

	private final Boolean status;
	private final Integer code;
	private final String message;
	private final HttpStatus httpStatus;
}
