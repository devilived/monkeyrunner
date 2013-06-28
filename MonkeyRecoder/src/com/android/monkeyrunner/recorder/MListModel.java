package com.android.monkeyrunner.recorder;

import java.util.List;

import javax.swing.AbstractListModel;

import com.google.common.collect.Lists;

public class MListModel extends AbstractListModel{
	private List<String> mmlist = Lists.newArrayList();
	private List<String> testList = Lists.newArrayList();
	@Override
	public Object getElementAt(int index) {
		// TODO Auto-generated method stub
		if(mmlist.get(index).equals("-1")){
			return 0;
		}else{
//			System.out.println("++++++++++++++++++++++++"+mmlist);
		     return mmlist.get(index);
			
		}
	}

	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return mmlist.size();
	}
	public void newAdd(String a) {
		mmlist.add(a);
		int newIndex = mmlist.size() - 1;
		this.fireIntervalAdded(this, newIndex, newIndex);
	}
	public void remove(int i) {
		mmlist.remove(i);
		int newIndex = mmlist.size() - 1;
		this.fireIntervalAdded(this, newIndex, newIndex);
	}
}
