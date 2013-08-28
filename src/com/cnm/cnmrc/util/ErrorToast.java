package com.cnm.cnmrc.util;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class ErrorToast extends Toast {

	public ErrorToast(Context context) {
		super(context);
		setGravity(Gravity.CENTER, 0, 0);
	}
	
	private void setMessage(String Code) {
		String TempText = null;
		
		// 공용 에러
		if (Code.contains(UrlAddress.ErrorCode.ERROR_200)) {
			TempText = "알수없는 에러(General Error)";
		} else if (Code.contains(UrlAddress.ErrorCode.ERROR_201)) {
			TempText = "지원하지 않는 프로토콜(Unsupported Protocol)";
		} else if (Code.contains(UrlAddress.ErrorCode.ERROR_202)) {
			TempText = "인증실패(Authentication Failure)";
		} else if (Code.contains(UrlAddress.ErrorCode.ERROR_203)) {
			TempText = "지원하지 않는 프로파일(Unsupported Profile)";
		} else if (Code.contains(UrlAddress.ErrorCode.ERROR_204)) {
			TempText = "잘못된 파라미터값(Invalid Parameter)";
		} else if (Code.contains(UrlAddress.ErrorCode.ERROR_205)) {
			TempText = "해당아이템 없음(Not Found)";
		} else if (Code.contains(UrlAddress.ErrorCode.ERROR_206)) {
			TempText = "내부서버에러(Internal Server Error)";
		} else if (Code.contains(UrlAddress.ErrorCode.ERROR_207)) {
			TempText = "내부프로세싱 에러(Internal Processing Error)";
		} else if (Code.contains(UrlAddress.ErrorCode.ERROR_211)) {
			TempText = "일반 DB에러(DB General Error)";
		} else if (Code.contains(UrlAddress.ErrorCode.ERROR_221)) {
			TempText = "이미처리되었음(Already Done)";
		} else if (Code.contains(UrlAddress.ErrorCode.ERROR_223)) {
			TempText = "이미추가된 항목(Duplicated Insertion)";
		} else if (Code.contains(UrlAddress.ErrorCode.ERROR_231)) {
			TempText = "인증코드발급실패(AuthCode Issue Failure)";
		} else if (Code.contains(UrlAddress.ErrorCode.ERROR_232)) {
			TempText = "만료된인증코드(AuthCode Expired)";
		}
		
		// 녹화 요청
		if (Code.contains(UrlAddress.ErrorCode.RecordResponCode.ERROR_001)) {
			TempText = "Mac불일치(Invalid MacAddress)";
		} else if (Code.contains(UrlAddress.ErrorCode.RecordResponCode.ERROR_002)) {
			TempText = "중복 녹화 예약요청 (Duplicated Recording Reserve Request)";
		} else if (Code.contains(UrlAddress.ErrorCode.RecordResponCode.ERROR_003)) {
			TempText = "디스크 용량부족(Disk Capacity Not Enough)";
		} else if (Code.contains(UrlAddress.ErrorCode.RecordResponCode.ERROR_004)) {
			TempText = "튜너를 모두사용하고 있어 녹화/튜닝불가 (Authentication Failure)";
		} else if (Code.contains(UrlAddress.ErrorCode.RecordResponCode.ERROR_005)) {
			TempText = "녹화 불가 채널(Unsupported Recording Channel)";
		} else if (Code.contains(UrlAddress.ErrorCode.RecordResponCode.ERROR_006)) {
			TempText = "이미 녹화 예약됨(Already Recording Reserve Done)";
		} else if (Code.contains(UrlAddress.ErrorCode.RecordResponCode.ERROR_007)) {
			TempText = "프로그램 정보가 없음(Program Not Found)";
		} else if (Code.contains(UrlAddress.ErrorCode.RecordResponCode.ERROR_008)) {
			TempText = "재생중인 녹화물 삭제불가( Recording Delete Error)";
		} else if (Code.contains(UrlAddress.ErrorCode.RecordResponCode.ERROR_009)) {
			TempText = "채널 없음(Channel Not Found)";
		} else if (Code.contains(UrlAddress.ErrorCode.RecordResponCode.ERROR_010)) {
			TempText = "PIP사용중(Using PIP Service)";
		} else if (Code.contains(UrlAddress.ErrorCode.RecordResponCode.ERROR_011)) {
			TempText = "다른채널 녹화중(Already Other Channel Recording)";
		} else if (Code.contains(UrlAddress.ErrorCode.RecordResponCode.ERROR_012)) {
			TempText = "고객님의 셋탑 설정에 의한 시청제한으로 녹화가 불가합니다. 셋탑 설정을 확인해주세요.";//"시청제한 채널(LimitedView Channel)";
		} else if (Code.contains(UrlAddress.ErrorCode.RecordResponCode.ERROR_013)) {
			TempText = "제한 채널(Limited Channel)";
		} else if (Code.contains(UrlAddress.ErrorCode.RecordResponCode.ERROR_014)) {
			TempText = "대기 모드(Hold Mode)";
		} else if (Code.contains(UrlAddress.ErrorCode.RecordResponCode.ERROR_015)) {
			TempText = "이미 녹화중 (Already Recording)";
		} else if (Code.contains(UrlAddress.ErrorCode.RecordResponCode.ERROR_016)) {
			TempText = "삭제 오류 (Delete Processing Error)";
		} else if (Code.contains(UrlAddress.ErrorCode.RecordResponCode.ERROR_017)) {
			TempText = "이름 변경 오류 (Name Replace Error)";
		} else if (Code.contains(UrlAddress.ErrorCode.RecordResponCode.ERROR_018)) {
			TempText = "VOD상세 화면 띄우기 오류 (VOD DetailView Error)";
		} else if (Code.contains(UrlAddress.ErrorCode.RecordResponCode.ERROR_019)) {
			TempText = "개인미디어 재생중 (Private Media Playing)";
		} else if (Code.contains(UrlAddress.ErrorCode.RecordResponCode.ERROR_020)) {
			TempText = "독립형(데이터 서비스) 실행중 (Already Playing DataService )";
		} else if (Code.contains(UrlAddress.ErrorCode.RecordResponCode.ERROR_021)) {
			TempText = "VOD재생중 (VOD Playing)";
		}
		
		//녹화 / 튜닝불가 상세코드
		if (Code.contains(UrlAddress.ErrorCode.RecordRejectCode.ERROR_005_1)) {
			TempText = "셋탑에서 동시화면 기능을 사용중인 경우 원격으로는 즉시녹화가 불가합니다.";//"PIP가 실행중이고 공통 전문의 시청채널과 다른 채널을 즉시 녹화할 경우";
		} else if (Code.contains(UrlAddress.ErrorCode.RecordRejectCode.ERROR_005_2)) {
			TempText = "VOD나 데이터 방송(JOY&LIFE)시청중 PIP가 실행중일경우";
		} else if (Code.contains(UrlAddress.ErrorCode.RecordRejectCode.ERROR_005_3)) {
			TempText = "VOD나 데이터 방송(JOY&LIFE)을 시청중이고 다른채널이 녹화중일경우 또 다른 채널을 녹화 요청시";
		} else if (Code.contains(UrlAddress.ErrorCode.RecordRejectCode.ERROR_005_4)) {
			TempText = "셋탑에서 다른 채널이 녹화중인 경우 원경으로는 즉시녹화가 불가합니다.";//"각기 다른 두채널이 녹화중이고 이때 또 다른채널을 녹화요청 할 경우";
		} else if (Code.contains(UrlAddress.ErrorCode.RecordRejectCode.ERROR_002)) {
			TempText = "셋탑의 저장 공간이 부족합니다. 녹화물 목록을 확인해주세요.";//"디스크 용량이 부족할 경우";
		} else if (Code.contains(UrlAddress.ErrorCode.RecordRejectCode.ERROR_003)) {
			TempText = "중복된 녹화예약일 경우";
		} else if (Code.contains(UrlAddress.ErrorCode.RecordRejectCode.ERROR_020)) {
			TempText = "JOY&LIFE, VOD, 미디어 앨범 실행 중일 경우";
		}
		
		setText(TempText);
	}
	

}
