package com.cnm.cnmrc.util;

public enum ErrorCode {
	ERROR_CODE_100("100","성공(Success)"), 
	ERROR_CODE_200("200","알수없는 에러(General Error)"),
	ERROR_CODE_201("201","지원하지 않는 프로토콜(Unsupported Protocol)"),
	ERROR_CODE_202("202","인증실패(Authentication Failure)"),
	ERROR_CODE_203("203","지원하지 않는 프로파일(Unsupported Profile)"),
	ERROR_CODE_204("204","잘못된 파라미터값(Invalid Parameter)"),
	ERROR_CODE_205("205","해당아이템 없음(Not Found)"),
	ERROR_CODE_206("206","내부서버에러(Internal Server Error)"),
	ERROR_CODE_207("207","내부프로세싱 에러(Internal Processing Error)"),
	ERROR_CODE_211("211","일반 DB에러(DB General Error)"),
	ERROR_CODE_221("221","이미처리되었음(Already Done)"),
	ERROR_CODE_223("223","이미추가된 항목(Duplicated Insertion)"),
	ERROR_CODE_231("231","인증코드발급실패(AuthCode Issue Failure)"),
	ERROR_CODE_232("232","만료된인증코드(AuthCode Expired)"),
	ERROR_CODE_998("998","네트워크 상태가 불안합니다. 다시 시도해 주십시요!!!!!"),
	ERROR_CODE_999("999","네트워크 상태가 불안합니다. 다시 시도해 주십시요!");

	String code;
	String desc;

	ErrorCode(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public String getCode() {
		return this.code;
	}

	public String getDesc() {
		return this.desc;
	}
}
