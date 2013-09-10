/*
 * Copyright (C) 2011 Chris Gao <chris@exina.net>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cnm.cnmrc.fragment.vodtvch;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.cnm.cnmrc.MainActivity;
import com.cnm.cnmrc.R;
import com.cnm.cnmrc.fragment.rc.RcBottomMenu;

public class Base extends Fragment {

	public Base newInstance(int selectedCategory, String title, boolean isFirstDepth, Bundle bundle) {
		Base f = new Base();
		return f;
	}
    
	boolean isFirstDepth = true;	// 기본값은 true이다. 즉, findFragmentById(R.id.loading_data_panel)에 의해 생성된것이다.
									// false : findFragmentByTag(((MainActivity) getActivity()).TAG_FRAGMENT_BASE)에 생성된 추가된 depth 화면에 해당한다.
	String[] mClassTypeArray = {"VodList", "VodSemi", "VodDetail", "TvchList", "TvchSemi", "TvchDetail"};
	
	@Override
	public void onDestroyView() {
		{
			// back key가 누르는순간은 2, 그러나 여기로 진입하면서 이미, 즉 destoryview에 오기전에 count가 minus되었다.
			// 따라서 TAG_FRAGMENT_BASE에 해당하는 fragment는 없을 것 같다.  아니다. 존재한다.
			// backstack에 존재하는 transaction의 count는 이미 minus가 되었다.
			Fragment f = getActivity().getSupportFragmentManager().findFragmentByTag(((MainActivity) getActivity()).TAG_FRAGMENT_BASE);
	    	if (f != null) {
	    		Log.i("hwang", "At Base TAG_FRAGMENT_BASE exist!!!");
	    	} else {
	    		Log.i("hwang", "At Base TAG_FRAGMENT_BASE no exist!!!");
	    	}
	    	
	    	f = getActivity().getSupportFragmentManager().findFragmentById(R.id.loading_data_panel);
	    	if (f != null) {
	    		Log.i("hwang", "At Base loading_data_panel exist!!!");
	    	} else {
	    		Log.i("hwang", "At Base loading_data_panel no exist!!!");
	    	}
		}
		
//		{
//			FragmentManager fm = getActivity().getSupportFragmentManager();
//			Log.i("hwang", "before destory base view fragment count --> " + Integer.toString(fm.getBackStackEntryCount()));
//	        Fragment f = getActivity().getSupportFragmentManager().findFragmentByTag(((MainActivity) getActivity()).TAG_FRAGMENT_BASE);
//	        if (f != null) {
//	        	getActivity().getSupportFragmentManager().beginTransaction().remove(f).commit();	// TAG_FRAGMENT_BASE에 해당하는 fragment가 모두한꺼번에 제거된다.
//				fm.executePendingTransactions(); // fragment 내부에서 호출하면 안된다.
//	        }
//			Log.i("hwang", "after destory base view fragment count --> " + Integer.toString(fm.getBackStackEntryCount()));
//			
//			// check
//			// 여기서 entry는 stack에 쌓이는 0부터 시작되는 index이다.
//			for (int entry = 0; entry < fm.getBackStackEntryCount(); entry++) {
//				Log.i("hwang", "Found fragment: " + fm.getBackStackEntryAt(entry).getId());
//			}
//		}
		
//		{
//			// same above, 아니다~ 틀린점은 위의경우 TAG_FRAGMENT_BASE에 연결된 모든 fragment가 한꺼번에 제거된다.
//			FragmentManager fm = getActivity().getSupportFragmentManager();
//			Log.i("hwang", "before destory base view fragment count --> " + Integer.toString(fm.getBackStackEntryCount()));
//	        Fragment f = getActivity().getSupportFragmentManager().findFragmentByTag(((MainActivity) getActivity()).TAG_FRAGMENT_BASE);
//	        if (f != null) {
//				fm.popBackStack(((MainActivity) getActivity()).TAG_FRAGMENT_BASE, 0);	// 이 때도 null 체크해야한다. 				// TAG_FRAGMENT_BASE에 해당하는 fragment가 하나씩 제거된다.
//    			fm.popBackStack(((MainActivity) getActivity()).TAG_FRAGMENT_BASE, FragmentManager.POP_BACK_STACK_INCLUSIVE);	// TAG_FRAGMENT_BASE에 해당하는 fragment가 하나씩 제거된다. 이상하다.....
//				fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);												// stack에 있는 모든 fragment가 제거된다.
//	        }
//			Log.i("hwang", "after destory base view fragment count --> " + Integer.toString(fm.getBackStackEntryCount()));
//			
//			// check
//			// 여기서 entry는 stack에 쌓이는 0부터 시작되는 index이다.
//			for (int entry = 0; entry < fm.getBackStackEntryCount(); entry++) {
//				Log.i("hwang", "Found fragment: " + fm.getBackStackEntryAt(entry).getId());
//			}
//		}
			
		
//		To clear stack you need to call:
//
//			ScreenFragment.sDisableExitAnimation = true;
//			manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//			transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		
		
//		FragmentManager fm = getActivity().getSupportFragmentManager();
//		for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {    
//		    fm.popBackStack();
//		}
//		But could equally have used something like:
//
//		FragmentManager.popBackStack(String name, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//		To go to top simply use: fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE); 
		
		
//		// transaction.replace(R.id.detailFragment, frag1);
//		Transaction.remove(null).add(frag1)  // frag1 on view
//
//		// transaction.replace(R.id.detailFragment, frag2).addToBackStack(null);
//		Transaction.remove(frag1).add(frag2).addToBackStack(null)  // frag2 on view
//
//		// transaction.replace(R.id.detailFragment, frag3);
//		Transaction.remove(frag2).add(frag3)  // frag3 on view
		
//		FragmentManager fm = getActivity().getSupportFragmentManager();
//		for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
//		    fm.popBackStack();
//		}
		
		// Use a name for your initial back stack state and use FragmentManager.popBackStack(String name, FragmentManager.POP_BACK_STACK_INCLUSIVE).
		// Use FragmentManager.getBackStackEntryCount()/getBackStackEntryAt().getId() to retrieve the ID of the first entry on the back stack, 
		// and FragmentManager.popBackStack(int id, FragmentManager.POP_BACK_STACK_INCLUSIVE).
		
		// is supposed to pop the entire back stack...
		//FragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE) 
		
		// Base의 onDestroyView()처리시 2가지 경우가 존재한다.
		// 하나는 ft.replace(R.id.loading_data_panel, base); 경우와
		// 다른 하나는 ft.add(R.id.loading_data_panel, base, ((MainActivity) getActivity()).TAG_FRAGMENT_BASE);
		// getBackStackEntryCount()로 체크하는것은 조금 위험하다... 엄밀히 말해서 정확하게 체크하게 되면 되는데...
		// backstack의 count 처리가 되는시점을 정확히 알면은 된다...
		FragmentManager fm = getActivity().getSupportFragmentManager();
		if(!isFirstDepth) {	// 기본 1 depth이후에 추가된 depth이면...
		//if(fm.getBackStackEntryCount() > 0 && fm.getBackStackEntryCount() < 8) {  	// indicator가 8개 기준으로...
			Log.i("hwang", "before Base onDestroyView() fragment count --> " + Integer.toString(fm.getBackStackEntryCount()));
	        Fragment f = getActivity().getSupportFragmentManager().findFragmentByTag(((MainActivity) getActivity()).TAG_FRAGMENT_BASE);
	        if (f != null) {
	        	// ㅎㅎ 자동으로 지워지네~~~~
	        	// popBackStack()을 명시적으로 하지 않아도 이미 제거된 후이다. 즉, backstack count가 이미 minus된 상태이다.
	        	//fm.popBackStack(((MainActivity) getActivity()).TAG_FRAGMENT_BASE, 0);	// 이 때도 null 체크해야한다.					// TAG_FRAGMENT_BASE에 해당하는 fragment가 하나씩 제거된다.
	        	//fm.popBackStack(((MainActivity) getActivity()).TAG_FRAGMENT_BASE, FragmentManager.POP_BACK_STACK_INCLUSIVE);	// TAG_FRAGMENT_BASE에 해당하는 fragment가 하나씩 제거된다. 이상하다...
	        	//fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);												// stack에 있는 모든 fragment가 제거된다.
	        	
	        	//getActivity().getSupportFragmentManager().beginTransaction().remove(f).commit();								// TAG_FRAGMENT_BASE에 해당하는 fragment가 모두한꺼번에 제거된다.
	        	//fm.executePendingTransactions(); 																				// fragment 내부에서 호출하면 안된다.
	        	
	        	// sidebar 메뉴에 해당하면...
	        	Fragment f1 = fm.findFragmentByTag(((MainActivity) getActivity()).TAG_FRAGMENT_VOD_TVCH);
	    		if (f1 != null) {
	    			if( ((VodTvchMain)f1).mSlidingMenu.isOpening() )  {
	    				getActivity().getSupportFragmentManager().beginTransaction().remove(f).commit();
	    				//fm.getBackStackEntryAt(0);
	    				//fm.popBackStack(((MainActivity) getActivity()).TAG_FRAGMENT_BASE, FragmentManager.POP_BACK_STACK_INCLUSIVE);
	    				//fm.executePendingTransactions(); 	
	    				
	    				for(int i = 0; i < fm.getBackStackEntryCount(); i++) {
//	    				    fm.popBackStack(((MainActivity) getActivity()).TAG_FRAGMENT_BASE, FragmentManager.POP_BACK_STACK_INCLUSIVE);
	    				    fm.popBackStack(i, 0); // OK
	    				    //fm.popBackStack(i, FragmentManager.POP_BACK_STACK_INCLUSIVE); // not OK
	    				}
	    			}
	    		}
	    		
	        }
			Log.i("hwang", "after Base onDestroyView() fragment count --> " + Integer.toString(fm.getBackStackEntryCount()));
			
			// check
			// 여기서 entry는 stack에 쌓이는 0부터 시작되는 index이다.
			for (int entry = 0; entry < fm.getBackStackEntryCount(); entry++) {
				Log.i("hwang", "At Base onDestroyView() Found fragment: " + fm.getBackStackEntryAt(entry).getId());
			}
			
			deleteDepthLevel();
			
			{
		        f = getActivity().getSupportFragmentManager().findFragmentById(R.id.loading_data_panel);
		        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
		    	if (f != null) {
		    		if(f.isHidden()) ft.show(f).commit();	// loading_data_panel와 TAG_FRAGMENT_BASE가 공통으로 사용된다. 분리할 필요가 있나?
		    		// f --> VodList{42626a88 #12 id=0x7f0c0048 base}
		    		// f --> VodList{42624ab0 #11 id=0x7f0c0048 base}
		    		// f --> VodList{42622ad8 #10 id=0x7f0c0048 base}
		    		// f --> VodList{42620b00 #9 id=0x7f0c0048 base}
		    		// f --> VodList{4261ea88 #8 id=0x7f0c0048 base}
		    		// f --> VodList{4261c560 #7 id=0x7f0c0048}
		    	}
			}
			
		}
        
		super.onDestroyView();
	}
	
	/**
	 * 추가되는 depth에 해당하는 fragment는 Base 클래스의 loadingData()에서 addToBackStack에 넣 생성한다.
	 * 이유는 MainActivity onBackPressed()에서 back key처리를 위해서이다.
	 * 여기서 만들어지는 화면은 isFirstDepth를 false로 해야한다. 즉 4개의 메인화면(예고편,최신영화,TV다시보기,장르별)이 아니다.
	 * vod (1st selectedCategory arg : 2nd title arg) : (0:예고편) / (1:최신영화) / (2:TV다시보기) / (3:장르별)
	 */
	protected void loadingData(int selectedCategory, String title, boolean isFirstDepth, Bundle bundle) {
        
		// 시작 기준으로 보면 ft.replace(R.id.loading_data_panel, base)의 base이므로
		// 만들어질 base에 isSldebar의 값을 적용해야하므로, 여기서 값을 설정하면 안되고, newInstance()로 값을 넘겨주어야한다.
		// this.isFirstDepth = isFirstDepth;
		
		// 1 depth에 해당하는 ft.replace(R.id.loading_data_panel, base)의 base이면 본인을 hide시킨다.
		if(this.isFirstDepth) {	// 이건 자신이 만들어질 때 자신이 대분류에 해당하는 화면인지를 확인하는것이다.
	        Fragment f = getActivity().getSupportFragmentManager().findFragmentById(R.id.loading_data_panel);
	        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
	    	if (f != null) {
	    		if(f.isVisible()) ft.hide(f).commit();
	    	}
	    	
	    	if(!isFirstDepth) setTitle(title);
		} else {
	        Fragment f = getActivity().getSupportFragmentManager().findFragmentByTag(((MainActivity) getActivity()).TAG_FRAGMENT_BASE);
	        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
	    	if (f != null) {
	    		if(f.isVisible()) ft.hide(f).commit();
	    	}
	    	
	    	setTitle(title);
		}
		
		FragmentManager fm = getActivity().getSupportFragmentManager();
		Log.i("hwang", "before adding base fragment count --> " + Integer.toString(fm.getBackStackEntryCount()));
		
		FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
		Class<?> classObject;
		try {
			String packageName = this.getClass().getPackage().getName() + ".";
			
			classObject = Class.forName(packageName + mClassTypeArray[selectedCategory]);
			Object obj = classObject.newInstance();

			Base base = ((Base) obj).newInstance(selectedCategory, title, isFirstDepth, bundle);

	        Fragment f = getActivity().getSupportFragmentManager().findFragmentById(R.id.vod_tvch_panel);
	    	if (f != null) {
	    		ft.add(R.id.loading_data_panel, base, ((MainActivity) getActivity()).TAG_FRAGMENT_BASE);
	    		ft.addToBackStack(null);	// push to "fragment stack" for back key operation
	    	}
			ft.commit();
			fm.executePendingTransactions();
			
	        // ------------------------------------
	        // 하단의 bottom menu에서 depth level 설정
	        // ------------------------------------
			addDepthLevel();
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (java.lang.InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		FragmentManager fm1 = getActivity().getSupportFragmentManager();
		Log.i("hwang", "after adding base fragment count --> " + Integer.toString(fm1.getBackStackEntryCount()));
	}
	
	protected void addDepthLevel() {
        // ------------------------------------
        // 하단의 bottom menu에서 depth level 설정
		// 2 depth 부터 추가된다.
        // ------------------------------------
        Fragment f = getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_rc_bottom_menu);
    	if (f != null) {
    		FragmentManager fm = getActivity().getSupportFragmentManager();
    		((RcBottomMenu)f).addDepthLevel(fm.getBackStackEntryCount()-1);	// ft.add() 이후라 이미  backstack count가 증가 되었다. 그러므로 -1 해주어야한다.
    	}
    }
	
	protected void deleteDepthLevel() {
		// ------------------------------------
		// 하단의 bottom menu에서 depth level 설정
		// 2 depth 부터 추가된다.
		// ------------------------------------
		Fragment f = getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_rc_bottom_menu);
		if (f != null) {
			FragmentManager fm = getActivity().getSupportFragmentManager();
			((RcBottomMenu)f).deleteDepthLevel(fm.getBackStackEntryCount());
		}
		
		setTitleBackKey();
	}
	
	// 기본 타이틀설정은 VodTvch와 Base 두 곳에 있음. (2)
	void setTitle(String title) {
		FragmentManager fm = getActivity().getSupportFragmentManager();
		Fragment f = fm.findFragmentByTag(((MainActivity) getActivity()).TAG_FRAGMENT_VOD_TVCH);
		if (f != null) ((VodTvchMain)f).setTitle(title);
	}
	
	private void setTitleBackKey() {
		FragmentManager fm = getActivity().getSupportFragmentManager();
		Fragment f = fm.findFragmentByTag(((MainActivity) getActivity()).TAG_FRAGMENT_VOD_TVCH);
		if (f != null) ((VodTvchMain)f).setTitle();
	}
	
	protected void increaseCurrentDepth() {
		FragmentManager fm = getActivity().getSupportFragmentManager();
		Fragment f = fm.findFragmentByTag(((MainActivity) getActivity()).TAG_FRAGMENT_VOD_TVCH);
		if (f != null) ((VodTvchMain)f).currentDepth++;
	}
	
	protected void decreaseCurrentDepth() {
		FragmentManager fm = getActivity().getSupportFragmentManager();
		Fragment f = fm.findFragmentByTag(((MainActivity) getActivity()).TAG_FRAGMENT_VOD_TVCH);
		if (f != null) ((VodTvchMain)f).currentDepth--;
	}
	
	protected int getCurrentDepth() {
		FragmentManager fm = getActivity().getSupportFragmentManager();
		Fragment f = fm.findFragmentByTag(((MainActivity) getActivity()).TAG_FRAGMENT_VOD_TVCH);
		if (f != null) return ((VodTvchMain)f).currentDepth;
		else return 0;
	}
	
	protected Fragment getVodTvchFragment() {
		FragmentManager fm = getActivity().getSupportFragmentManager();
		Fragment f = fm.findFragmentByTag(((MainActivity) getActivity()).TAG_FRAGMENT_VOD_TVCH);
		
		return f;
	}
	
	

}
