package com.cnm.cnmrc.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

//SMApplicationServer는 C&M 모바일서비스에 필요한 메뉴 화면구성정보를 http프로토콜을 이용한 openAPI를 통해 제공한다.
public class UrlAddress {

	public static final int XML_CONNETION_TIME_OUT					= 30000;		// 파서 타임 아웃 시간 설정 값 (30초)
	//----------------XML URL---------------------------
	//Server Address
	private static final String Server_URL		= "http://58.143.243.91/SMApplicationServer/";
	private static final String OldServer_URL	= "http://58.143.243.91/mobile/";

	private static String getURLEncoder(String aStr){
		try {
			return URLEncoder.encode(aStr, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	//----------------XML ADDRESS---------------------------
	public static class Authenticate{
		//Authenticate
		private static final String AuthenticateClient		= Server_URL + "AuthenticateClient.asp?terminalId=%s";				// 클라이언트를 인증하고 TerminalKey를 얻는다. (시스템관리자이용)
		private static final String GetAppVersionInfo		= Server_URL + "GetAppVersionInfo.asp";								// App 버전관리정보를 제공합니다.
		private static final String GetAppContentVersion	= Server_URL + "GetAppContentVersion.asp";							// SMApplication이 제공하는 모든 컨텐츠에대한 버전관리를 담당합니다.
		private static final String ClientSetTopBoxRegist	= Server_URL + "ClientSetTopBoxRegist.asp?deviceId=%s&authKey=%s";	// 사용할 셋탑을 등록합니다..(VOD서버와의 인터페이스 확인)
		private static final String CheckRegistUser			= Server_URL + "CheckRegistUser.asp?deviceId=%s";					// 사용회원으로 등록된 사용자인지 여부를 확인한다.
		private static final String AuthenticateAdult		= Server_URL + "AuthenticateAdult.asp?UserInfo=%s@%s";				// 성인사용자 여부를 인증합니다. (성인인증서버 연동)

		//Authenticate
		public static final String AUTHE_SETTOP_HD			= "HD"; 						//셋톱의 종류 ( HD/ SD/ PVR /SMART)
		public static final String AUTHE_SETTOP_SD			= "SD"; 						
		public static final String AUTHE_SETTOP_PVR			= "PVR"; 						
		public static final String AUTHE_SETTOP_SMART		= "SMART"; 						
		//----------------------------------------------
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
		//Channel
		private static final String GetChannelArea		= Server_URL + "GetChannelArea.asp";												// 채널서비스 지역정보를 보여준다
		private static final String GetChannelProduct	= Server_URL + "GetChannelProduct.asp?areaCode=%s";									// 채널서비스 상품정보를 보여준다
		
		private static final String GetChannelGenre		= Server_URL + "GetChannelGenre.asp";												// 채널서비스 장르정보를 보여준다.
		private static final String GetChannelList		= Server_URL + "GetChannellist.asp?areaCode=%s&productCode=%s&genreCode=%s&mode=%s";// 지역, 상품, 장르별 채널 리스트를 리턴한다.
		private static final String GetChannelSchedule	= Server_URL + "GetChannelSchedule.asp?channelId=%s&DateIndex=%s";					// 한채널의 일자별 편성정보를 제공한다
		private static final String GetChannelMyList	= Server_URL + "구현예정";																// 구현예정
		private static final String SetMyChannel		= Server_URL + "SetMyChannel.asp?deviceId=%s&ChannelId=%s&mode=%s";					// 사용자가자신의 채널을 저장할 때 사용됩니다.
		private static final String SetMyHiddenChannel	= Server_URL + "SetMyHiddenChannel.asp?deviceId=%s&ChannelId=%s&mode=%s";			// 사용자가자신의 히든 채널을 저장할 때 사용됩니다.
		private static final String SetMySchedule		= Server_URL + "SetMySchedule.asp?deviceId=%s&scheduleId=%s&mode=%s";				// 사용자가 스케줄 예약알람을 설정할 때 사용됩니다.
		//////////////////////////////////////////////////////////
		public static class CHANNEL_LSIT{
			public static final String PAY				= "PAY"; 						// 채널검색모드 (PAY : 유료채널, HD: HD채널) 값없음 : 전체채널
			public static final String HD				= "HD"; 						// 채널검색모드 (PAY : 유료채널, HD: HD채널) 값없음 : 전체채널
			public static final String ALL				= ""; 							// 채널검색모드 (PAY : 유료채널, HD: HD채널) 값없음 : 전체채널
		}
		//////////////////////////////////////////////////////////
		public static class LIMITED_AREA_CODE{
			public static final String KANGNAM			= "not supported"; 				// 강남인 경우
			public static final String UlSAN			= "not supported"; 				// 울산인 경우
		}
		//---------------------------------------------
		public static String getGetChannelArea() {
			return GetChannelArea;
		}
		public static String getGetChannelProduct(String areaCode) {
			return String.format(GetChannelProduct, areaCode);
		}
		
		public static String getGetchannelgenre() {
			return GetChannelGenre;
		}
		public static String getGetchannellist(String aAreaCode, String aProductCode, String aGenreCode, String aMode) {
			return String.format(GetChannelList, aAreaCode, aProductCode, aGenreCode, aMode);
		}
		public static String getGetchannelschedule(String aChannelId, String aDateIndexa) {
			return String.format(GetChannelSchedule, aChannelId, aDateIndexa);
		}
		public static String getGetchannelmylist() {
			return GetChannelMyList;
		}
		public static String getSetmychannel(String aDeviceId, String aChannelId, String aMode) {
			return String.format(SetMyChannel, aDeviceId, aChannelId, aMode);
		}
		public static String getSetmyhiddenchannel(String aDeviceId, String aChannelId, String aMode) {
			return String.format(SetMyHiddenChannel, aDeviceId, aChannelId, aMode);
		}
		public static String getSetmyschedule(String aDeviceId, String aScheduleId, String aMode) {
			return String.format(SetMySchedule, aDeviceId, aScheduleId, aMode);
		}
	}

	public static class Vod{
		//Sponsor
		private static final String GetVodGenre				= Server_URL + "GetVodGenre.asp?genreId=%s";								// VOD장르 정보를 제공합니다
		private static final String GetVodGenreInfo			= Server_URL + "GetVodGenreInfo.asp?genreId=%s";							// VOD장르 상세 정보를 제공합니다. ( Genre_More가 NO일경우 장르?의 VOD정보제공)
		private static final String GetVodMovie				= Server_URL + "GetVodMovie.asp";											// VOD 최신영화 리스트를 제공합니다.
		private static final String GetVodTv				= Server_URL + "GetVodTv.asp";												// TV다시보기 리스트를 제공합니다.
		private static final String GetVodTag				= Server_URL + "GetVodTag.asp?vod_tag=%s";									// VOD 중 예고편내의 검색을 통한 tag정보를 제공합니다.
		private static final String GetVodTrailer			= Server_URL + "GetVodTrailer.asp";											// VOD중 예고편 리스트 정보를 리턴합니다.
		// VOD - 찜 관련
		// 찜목록
		// http://58.143.243.91/SMApplicationServer/GetVodMyList.asp?deviceId=FE0873D5-D937-5110-8EFF-F192A13B4529
		private static final String GetVodMyList			= Server_URL + "GetVodMyList.asp?deviceId=%s";								// 사용자 디바이스에서 MyVod에 담은 리스트를 제공합니다.
		private static final String SetMyVod				= Server_URL + "SetMyVod.asp?deviceId=%s&assetId=%s&genreId=%s&mode=%s";	// 사용자가 MyVod로 저장할 때 사용됩니다. (현재 서비스중이므로 테스트시 주의할 것) ( Vod 서버와 연동을 통하여 인터페이스가 구현됨)
		// 1차 인터페이스
		// 찜하기
		// http://58.143.243.91/mobile/MyVod_insert.asp?MemberID=FE0873D5-D937-5110-8EFF-F192A13B4529&AssetID=www.hchoice.co.kr%7CM0121727LSG117227301&Genre_ID=304547
		private static final String InsertMyVod				= OldServer_URL + "MyVod_insert.asp?MemberID=%s&AssetID=%s&Genre_ID=%s";	// 찜 관련은 예전 인터페이스 사용함.
		// 찜해제
		// http://58.143.243.91/mobile/MyVod_DELETE.asp?MemberID=FE0873D5-D937-5110-8EFF-F192A13B4529&AssetID=www.hchoice.co.kr%7CM0121727LSG117227301&Genre_ID=304547
		private static final String DeleteMyVod				= OldServer_URL + "MyVod_DELETE.asp?MemberID=%s&AssetID=%s&Genre_ID=%s";	// 찜 관련은 예전 인터페이스 사용함.
		// 찜목록 초기화
		// http://58.143.243.91/Mobile/myvod_delete.asp?MemberID=FE0873D5-D937-5110-8EFF-F192A13B4529
		private static final String DeleteAllMyVod			= OldServer_URL + "MyVod_DELETE.asp?MemberID=%s";							// 찜 관련은 예전 인터페이스 사용함.		
		
		// VOD 상세정보
		private static final String SetVodSetTopDisplayInfo	= Server_URL + "SetVodSetTopDisplayInfo.asp?deviceId=%s&assetId=%s";		// 셋탑에 VOD상세 정보를 Display 합니다. (STB서버와 연동을 통하여 인터페이스 구현됨)
		private static final String Notification			= Server_URL + "Notification.asp?userId=%s&assetId=%s&type=%s";				// 셋탑박스에서 MyVOD를 삭제할경우 사용됩니다. (VOD서버와 연동을 통하여 인터페이스 구현됨)
		///////////////////////////////////////////////////////////////////
		public static final String NOTIFICATION_WISHITEM_TYPE	= "wishItemRemoved"; 					// userRemoved : device의 모든asset 삭제, deviceId 삭제
		public static final String NOTIFICATION_USER_TYPE		= "userRemoved"; 
		public static final String VOD_MY_MODE_INPUT						= "0"; 						// 0:입력, 1:특정컨텐츠삭제,2:해당유저의 모든커텐츠삭젝 3: 유저 및 모든데이터삭제
		public static final String VOD_MY_MODE_SPECIFIC_CONTENT_DELETE		= "1"; 						
		public static final String VOD_MY_MODE_USER_ALL_CONTENT_DELETE		= "2"; 						
		public static final String VOD_MY_MODE_USER_AND_CONTENT_ALL_DELETE	= "3";
		//----------------------------------------------------------
		public static String getGetvodgenre(String aGenreId) {
			return String.format(GetVodGenre, aGenreId);
		}
		public static String getGetvodgenreinfo(String aGenreId) {
			return String.format(GetVodGenreInfo, aGenreId);
		}
		public static String getGetvodmovie() {
			return GetVodMovie;
		}
		public static String getGetvodtv() {
			return GetVodTv;
		}
		public static String getGetvodtag(String aVod_Tag) {
			return String.format(GetVodTag, getURLEncoder(aVod_Tag));
		}
		public static String getGetvodtrailer() {
			return GetVodTrailer;
		}
		public static String getGetvodmylist(String aDeviceId) {
			return String.format(GetVodMyList, aDeviceId);
		}
		public static String getSetmyvod(String aDeviceId, String aAssetId, String aGenreId, String aMode) {
			return String.format(SetMyVod, aDeviceId, aAssetId, aGenreId, aMode);
		}
		// 찜하기
		public static String getInsertmyvod(String aDeviceId, String aAssetId, String aGenreId) {
			return String.format(InsertMyVod, aDeviceId, aAssetId, aGenreId);
		}
		// 찜해제
		public static String getDeletemyvod(String aDeviceId, String aAssetId, String aGenreId) {
			return String.format(DeleteMyVod, aDeviceId, aAssetId, aGenreId);
		}
		// 찜목록 초기화
		public static String getDeleteallmyvod(String aDeviceId) {
			return String.format(DeleteAllMyVod, aDeviceId);
		}
		public static String getSetvodsettopdisplayinfo(String aDeviceId, String aAssetId) {
			return String.format(SetVodSetTopDisplayInfo, aDeviceId, aAssetId);
		}
		public static String getNotification(String aUserId, String aAssetId, String aType) {
			return String.format(Notification, aUserId, aAssetId, aType);
		}
	}

	public static class Search {
		//Search
		private static final String SearchProgram	= Server_URL + "SearchProgram.asp?Search_String=%s&pageSize=%s&pageIndex=%s&areaCode=%s&productCode=%s";	// 프로그램 정보를 검색합니다.
		private static final String SearchVod		= Server_URL + "SearchVod.asp?search_string=%s&pageSize=%s&pageIndex=%s&sortType=%s";						// VOD정보를 검색합니다.
		private static final String SearchChannel	= Server_URL + "SearchChannel.asp?Search_String=%s&pageSize=%s&pageIndex=%s&areaCode=%s&productCode=%s";	// 채널정보를 검색합니다. (not used!!! in this App)
		
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
		public static String getSearchChannel(String searchStr, String pageSize, String pageIndex, String areaCode, String productCode) {
			return String.format(SearchChannel, getURLEncoder(searchStr), pageSize, pageIndex, areaCode, productCode);
		}
	}
	


}
