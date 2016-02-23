package com.fly.firefly.utils;

import android.app.Application;

import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.BitmapAjaxCallback;
import com.androidquery.util.AQUtility;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class App extends Application {

	public static final String BASE_URL = "http://";
	public static final String FONT_HELVETICA_NEUE = "fonts/HelveticaNeue.ttf";
	public static final String LANG = Locale.getDefault().getLanguage();
	public static final String CURRENCY = "MYR";

	public static final String WIKITUDE_SDK_KEY = "o61KXnIOPpFAXlZ3DVLTnTN2vRWE57IZmYW8RMUbbsh/k13re8CM4tTzBZ3rs/v/rYG7SPUXo3bklsdu6HPJAh+adM1+ANtYBs7MqIxLrU4nMYpWgex0F1Ju8MYWe8+ICd8Htc5G8yoCggJ4GkM0S4rQMXNr2/vmsLQkXOUuJ29TYWx0ZWRfX9zKvlEAsYcwZw8zo9XrLUBmNNSu5pBP1a8d7AGunC1Zlj5soG/bN5/C4u/cWg3985MjE0fOGHKGVHEianLD5b0h4pt06k70D6+WArNauN3PU9i0rBNgdlNOrVd1Gdzs0eGRQvNLj4ynqyP72scrj9cWjjO2pKYAXDcDnu8NO4SetYcO+8utyNCbZQrRuqmxQok/yzswLm7yZxhMPI6BuaWCA5h1nlsbO3wipvuImCepQ9XoWEj+F/egSOgCcxMzwAJwsoYGWlDnsBKAi1UmWFaiN4Tf3xKrZq/gr52HU33UKx3IVMdA3aRl6l8U/CLojyte/pkr1OXfV5ZJaDhTy8TRlGJWwP5fR+YU/mYlOp6+rYCSq+n0YwWoKgPYoc0vxQAgV7pJdoiKLeF6P11gusGwhi1PUZjnZgoygeoFMrXCf7wCh6Ad4Hh+MJKB/NT4VOVk3xhWxI61I2Ks28JUotw/DCRhjNKQcpLEVL3AF87A7YFPSw/qmnE=";
	public static final String KEY = "owNLfnLjPvwbQH3hUmj5Wb7wBIv83pR7";
	public static final String IV = "owNLfnLjPvwbQH3h";


	// code 7 is success code with badge
	public static final List<String> SUCCESS_CODE = Arrays.asList("1", "7");

    public static String deviceID;

	@Override
	public void onCreate()
	{
		super.onCreate();

		AQUtility.setDebug(true);

		// set the max number of concurrent network connections, default is 4
		AjaxCallback.setNetworkLimit(12);

		// set the max number of icons (image width <= 50) to be cached in
		// memory, default is 20
		BitmapAjaxCallback.setIconCacheLimit(20);

		// set the max number of images (image width > 50) to be cached in
		// memory, default is 20
		BitmapAjaxCallback.setCacheLimit(40);

		// set the max size of an image to be cached in memory, default is 1600
		// pixels (ie. 400x400)
		BitmapAjaxCallback.setPixelLimit(500 * 500);

		// set the max size of the memory cache, default is 1M pixels (4MB)
		BitmapAjaxCallback.setMaxPixelLimit(2000000);

		// set timeout, default is 30 seconds (30000)
		BitmapAjaxCallback.setTimeout(120000);
	}

	@Override
	public void onLowMemory()
	{
		// clear all memory cached images when system is in low memory
		// note that you can configure the max image cache count, see
		// CONFIGURATION
		BitmapAjaxCallback.clearCache();
	}


}
