package com.cnm.cnmrc.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

//SMApplicationServer는 C&M 모바일서비스에 필요한 메뉴 화면구성정보를 http프로토콜을 이용한 openAPI를 통해 제공한다.
public class UrlAddress {

	public static final int XML_CONNETION_TIME_OUT					= 30000;		// 파서 타임 아웃 시간 설정 값 (30초)
	
	// Server Address
	private static final String Server_URL		= "http://58.143.243.91/SMApplicationServer/";

	private static String getURLEncoder(String aStr){
		try {
			return URLEncoder.encode(aStr, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	// Authenticate
	public static class Authenticate{
		private static final String AuthenticateClient		= Server_URL + "AuthenticateClient.asp?terminalId=%s";				// 클라이언트를 인증하고 TerminalKey를 얻는다. (시스템관리자이용)
		private static final String GetAppVersionInfo		= Server_URL + "GetAppVersionInfo.asp";								// App 버전관리정보를 제공합니다.
		private static final String GetAppContentVersion	= Server_URL + "GetAppContentVersion.asp";							// SMApplication이 제공하는 모든 컨텐츠에대한 버전관리를 담당합니다.
		private static final String ClientSetTopBoxRegist	= Server_URL + "ClientSetTopBoxRegist.asp?deviceId=%s&authKey=%s";	// 사용할 셋탑을 등록합니다..(VOD서버와의 인터페이스 확인)
		private static final String CheckRegistUser			= Server_URL + "CheckRegistUser.asp?deviceId=%s";					// 사용회원으로 등록된 사용자인지 여부를 확인한다.
		private static final String AuthenticateAdult		= Server_URL + "AuthenticateAdult.asp?UserInfo=%s@%s";				// 성인사용자 여부를 인증합니다. (성인인증서버 연동)

		// 셋톱의 종류 ( HD/ SD/ PVR /SMART)
		public static final String AUTHE_SETTOP_HD			= "HD"; 	
		public static final String AUTHE_SETTOP_SD			= "SD"; 						
		public static final String AUTHE_SETTOP_PVR			= "PVR"; 						
		public static final String AUTHE_SETTOP_SMART		= "SMART"; 	
		
		public static String getAuthenticateClient(String terminalId) {
			return String.format(AuthenticateClient, terminalId);
		}
		public static String getGetAppVersionInfo() {
			return GetAppVersionInfo;
		}
		public static String getGetAppContentVersion() {
			return GetAppContentVersion;
		}
		public static String getClientSetTopBoxRegist(String deviceId, String authKey) {
			return String.format(ClientSetTopBoxRegist, deviceId, authKey);
		}
		public static String getCheckRegistUser(String deviceId) {
			return String.format(CheckRegistUser, deviceId);
		}
		public static String getAuthenticateAdult(String userNumber, String userName) {
			return String.format(AuthenticateAdult, userNumber, getURLEncoder(userName));
		}
	}

	public static class Channel{
		private static final String GetChannelArea		= Server_URL + "GetChannelArea.asp";												// 채널서비스 지역정보를 보여준다
		private static final String GetChannelProduct	= Server_URL + "GetChannelProduct.asp?areaCode=%s";									// 채널서비스 상품정보를 보여준다
		
		private static final String GetChannelGenre		= Server_URL + "GetChannelGenre.asp";												// 채널서비스 장르정보를 보여준다.
		private static final String GetChannelList		= Server_URL + "GetChannellist.asp?areaCode=%s&productCode=%s&genreCode=%s&mode=%s";// 지역, 상품, 장르별 채널 리스트를 리턴한다.
		private static final String GetChannelSchedule	= Server_URL + "GetChannelSchedule.asp?channelId=%s&DateIndex=%s";					// 한채널의 일자별 편성정보를 제공한다
		
		public static class Mode{
			public static final String ALL	= ""; 		// 채널검색모드 (PAY : 유료채널, HD : HD채널, "" : 전체채널)
			public static final String HD	= "HD"; 	// 채널검색모드 (PAY : 유료채널, HD : HD채널, "" : 전체채널)
			public static final String PAY	= "PAY"; 	// 채널검색모드 (PAY : 유료채널, HD : HD채널, "" : 전체채널)
		}
		
		//---------------------------------------------
		public static String getGetChannelArea() {
			return GetChannelArea;
		}
		public static String getGetChannelProduct(String areaCode) {
			return String.format(GetChannelProduct, areaCode);
		}
		
		public static String getGetChannelGenre() {
			return GetChannelGenre;
		}
		public static String getGetChannelList(String aAreaCode, String aProductCode, String aGenreCode, String aMode) {	// 채널정보
			return String.format(GetChannelList, aAreaCode, aProductCode, aGenreCode, aMode);
		}
		public static String getGetChannelSchedule(String aChannelId, String aDateIndexa) {									// 채널편성표 or 채널스케쥴
			return String.format(GetChannelSchedule, aChannelId, aDateIndexa);
		}
	}

	public static class Vod{
		private static final String GetVodTrailer			= Server_URL + "GetVodTrailer.asp";					// VOD중 예고편 리스트 정보를 리턴합니다.
		private static final String GetVodMovie				= Server_URL + "GetVodMovie.asp";					// VOD 최신영화 리스트를 제공합니다.
		private static final String GetVodTv				= Server_URL + "GetVodTv.asp";						// TV다시보기 리스트를 제공합니다.
		private static final String GetVodGenre				= Server_URL + "GetVodGenre.asp?genreId=%s";		// VOD장르 정보를 제공합니다
		private static final String GetVodGenreInfo			= Server_URL + "GetVodGenreInfo.asp?genreId=%s";	// VOD장르 상세 정보를 제공합니다. ( Genre_More가 NO일경우 장르?의 VOD정보제공)
		
		public static String getGetVodTrailer() {
			return GetVodTrailer;
		}
		public static String getGetVodMovie() {
			return GetVodMovie;
		}
		public static String getGetVodTv() {
			return GetVodTv;
		}
		public static String getGetVodGenre(String aGenreId) {
			return String.format(GetVodGenre, aGenreId);
		}
		public static String getGetVodGenreInfo(String aGenreId) {
			return String.format(GetVodGenreInfo, aGenreId);
		}
	}

	public static class Search {
		private static final String SearchProgram	= Server_URL + "SearchProgram.asp?Search_String=%s&pageSize=%s&pageIndex=%s&areaCode=%s&productCode=%s";	// 프로그램 정보를 검색합니다.
		private static final String SearchVod		= Server_URL + "SearchVod.asp?search_string=%s&pageSize=%s&pageIndex=%s&sortType=%s";						// VOD정보를 검색합니다.
		
		/*	Vod 정렬방식 (잘못된 문자가 들어가면 기본으로 처리한다.)
			기본 : 		TitleAsc 
			이름순 : 		TitleAsc
			이름순 역순 : 	TitleDesc			
			퀄러티순 :		QualityAsc
			퀄러티 역순 :	QualityDesc
		*/
		public static final String TitleAsc 	=	"TitleAsc";
		public static final String TitleDesc 	=	"TitleDesc";
		public static final String QualityAsc 	=	"QualityAsc";
		public static final String QualityDesc 	=	"QualityDesc";

		public static String getSearchProgram(String searchStr, String pageSize, String pageIndex, String areaCode, String productCode) {
			return String.format(SearchProgram, getURLEncoder(searchStr), pageSize, pageIndex, areaCode, productCode);
		}
		public static String getSearchVod(String searchStr, String pageSize, String pageIndex, String sortType) {
			return String.format(SearchVod, getURLEncoder(searchStr), pageSize, pageIndex, sortType);
		}
	}
	


}
