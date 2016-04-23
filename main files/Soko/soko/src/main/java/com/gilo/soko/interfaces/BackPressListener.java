package com.gilo.soko.interfaces;

import java.util.ArrayList;

public interface BackPressListener  {
	
	public void onPreExecute();
	public void onComplete(Boolean back_pressed);
}
