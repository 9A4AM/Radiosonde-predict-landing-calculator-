package b4a.RS_calculator;


import anywheresoftware.b4a.B4AMenuItem;
import android.app.Activity;
import android.os.Bundle;
import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BALayout;
import anywheresoftware.b4a.B4AActivity;
import anywheresoftware.b4a.ObjectWrapper;
import anywheresoftware.b4a.objects.ActivityWrapper;
import java.lang.reflect.InvocationTargetException;
import anywheresoftware.b4a.B4AUncaughtException;
import anywheresoftware.b4a.debug.*;
import java.lang.ref.WeakReference;

public class main extends Activity implements B4AActivity{
	public static main mostCurrent;
	static boolean afterFirstLayout;
	static boolean isFirst = true;
    private static boolean processGlobalsRun = false;
	BALayout layout;
	public static BA processBA;
	BA activityBA;
    ActivityWrapper _activity;
    java.util.ArrayList<B4AMenuItem> menuItems;
	public static final boolean fullScreen = false;
	public static final boolean includeTitle = true;
    public static WeakReference<Activity> previousOne;
    public static boolean dontPause;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        mostCurrent = this;
		if (processBA == null) {
			processBA = new BA(this.getApplicationContext(), null, null, "b4a.RS_calculator", "b4a.RS_calculator.main");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (main).");
				p.finish();
			}
		}
        processBA.setActivityPaused(true);
        processBA.runHook("oncreate", this, null);
		if (!includeTitle) {
        	this.getWindow().requestFeature(android.view.Window.FEATURE_NO_TITLE);
        }
        if (fullScreen) {
        	getWindow().setFlags(android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN,   
        			android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
		
        processBA.sharedProcessBA.activityBA = null;
		layout = new BALayout(this);
		setContentView(layout);
		afterFirstLayout = false;
        WaitForLayout wl = new WaitForLayout();
        if (anywheresoftware.b4a.objects.ServiceHelper.StarterHelper.startFromActivity(this, processBA, wl, false))
		    BA.handler.postDelayed(wl, 5);

	}
	static class WaitForLayout implements Runnable {
		public void run() {
			if (afterFirstLayout)
				return;
			if (mostCurrent == null)
				return;
            
			if (mostCurrent.layout.getWidth() == 0) {
				BA.handler.postDelayed(this, 5);
				return;
			}
			mostCurrent.layout.getLayoutParams().height = mostCurrent.layout.getHeight();
			mostCurrent.layout.getLayoutParams().width = mostCurrent.layout.getWidth();
			afterFirstLayout = true;
			mostCurrent.afterFirstLayout();
		}
	}
	private void afterFirstLayout() {
        if (this != mostCurrent)
			return;
		activityBA = new BA(this, layout, processBA, "b4a.RS_calculator", "b4a.RS_calculator.main");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "b4a.RS_calculator.main", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (main) Create " + (isFirst ? "(first time)" : "") + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (main) Resume **");
        processBA.raiseEvent(null, "activity_resume");
        if (android.os.Build.VERSION.SDK_INT >= 11) {
			try {
				android.app.Activity.class.getMethod("invalidateOptionsMenu").invoke(this,(Object[]) null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	public void addMenuItem(B4AMenuItem item) {
		if (menuItems == null)
			menuItems = new java.util.ArrayList<B4AMenuItem>();
		menuItems.add(item);
	}
	@Override
	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		super.onCreateOptionsMenu(menu);
        try {
            if (processBA.subExists("activity_actionbarhomeclick")) {
                Class.forName("android.app.ActionBar").getMethod("setHomeButtonEnabled", boolean.class).invoke(
                    getClass().getMethod("getActionBar").invoke(this), true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (processBA.runHook("oncreateoptionsmenu", this, new Object[] {menu}))
            return true;
		if (menuItems == null)
			return false;
		for (B4AMenuItem bmi : menuItems) {
			android.view.MenuItem mi = menu.add(bmi.title);
			if (bmi.drawable != null)
				mi.setIcon(bmi.drawable);
            if (android.os.Build.VERSION.SDK_INT >= 11) {
				try {
                    if (bmi.addToBar) {
				        android.view.MenuItem.class.getMethod("setShowAsAction", int.class).invoke(mi, 1);
                    }
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			mi.setOnMenuItemClickListener(new B4AMenuItemsClickListener(bmi.eventName.toLowerCase(BA.cul)));
		}
        
		return true;
	}   
 @Override
 public boolean onOptionsItemSelected(android.view.MenuItem item) {
    if (item.getItemId() == 16908332) {
        processBA.raiseEvent(null, "activity_actionbarhomeclick");
        return true;
    }
    else
        return super.onOptionsItemSelected(item); 
}
@Override
 public boolean onPrepareOptionsMenu(android.view.Menu menu) {
    super.onPrepareOptionsMenu(menu);
    processBA.runHook("onprepareoptionsmenu", this, new Object[] {menu});
    return true;
    
 }
 protected void onStart() {
    super.onStart();
    processBA.runHook("onstart", this, null);
}
 protected void onStop() {
    super.onStop();
    processBA.runHook("onstop", this, null);
}
    public void onWindowFocusChanged(boolean hasFocus) {
       super.onWindowFocusChanged(hasFocus);
       if (processBA.subExists("activity_windowfocuschanged"))
           processBA.raiseEvent2(null, true, "activity_windowfocuschanged", false, hasFocus);
    }
	private class B4AMenuItemsClickListener implements android.view.MenuItem.OnMenuItemClickListener {
		private final String eventName;
		public B4AMenuItemsClickListener(String eventName) {
			this.eventName = eventName;
		}
		public boolean onMenuItemClick(android.view.MenuItem item) {
			processBA.raiseEventFromUI(item.getTitle(), eventName + "_click");
			return true;
		}
	}
    public static Class<?> getObject() {
		return main.class;
	}
    private Boolean onKeySubExist = null;
    private Boolean onKeyUpSubExist = null;
	@Override
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
        if (processBA.runHook("onkeydown", this, new Object[] {keyCode, event}))
            return true;
		if (onKeySubExist == null)
			onKeySubExist = processBA.subExists("activity_keypress");
		if (onKeySubExist) {
			if (keyCode == anywheresoftware.b4a.keywords.constants.KeyCodes.KEYCODE_BACK &&
					android.os.Build.VERSION.SDK_INT >= 18) {
				HandleKeyDelayed hk = new HandleKeyDelayed();
				hk.kc = keyCode;
				BA.handler.post(hk);
				return true;
			}
			else {
				boolean res = new HandleKeyDelayed().runDirectly(keyCode);
				if (res)
					return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	private class HandleKeyDelayed implements Runnable {
		int kc;
		public void run() {
			runDirectly(kc);
		}
		public boolean runDirectly(int keyCode) {
			Boolean res =  (Boolean)processBA.raiseEvent2(_activity, false, "activity_keypress", false, keyCode);
			if (res == null || res == true) {
                return true;
            }
            else if (keyCode == anywheresoftware.b4a.keywords.constants.KeyCodes.KEYCODE_BACK) {
				finish();
				return true;
			}
            return false;
		}
		
	}
    @Override
	public boolean onKeyUp(int keyCode, android.view.KeyEvent event) {
        if (processBA.runHook("onkeyup", this, new Object[] {keyCode, event}))
            return true;
		if (onKeyUpSubExist == null)
			onKeyUpSubExist = processBA.subExists("activity_keyup");
		if (onKeyUpSubExist) {
			Boolean res =  (Boolean)processBA.raiseEvent2(_activity, false, "activity_keyup", false, keyCode);
			if (res == null || res == true)
				return true;
		}
		return super.onKeyUp(keyCode, event);
	}
	@Override
	public void onNewIntent(android.content.Intent intent) {
        super.onNewIntent(intent);
		this.setIntent(intent);
        processBA.runHook("onnewintent", this, new Object[] {intent});
	}
    @Override 
	public void onPause() {
		super.onPause();
        if (_activity == null)
            return;
        if (this != mostCurrent)
			return;
		anywheresoftware.b4a.Msgbox.dismiss(true);
        if (!dontPause)
            BA.LogInfo("** Activity (main) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
        else
            BA.LogInfo("** Activity (main) Pause event (activity is not paused). **");
        if (mostCurrent != null)
            processBA.raiseEvent2(_activity, true, "activity_pause", false, activityBA.activity.isFinishing());		
        if (!dontPause) {
            processBA.setActivityPaused(true);
            mostCurrent = null;
        }

        if (!activityBA.activity.isFinishing())
			previousOne = new WeakReference<Activity>(this);
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        processBA.runHook("onpause", this, null);
	}

	@Override
	public void onDestroy() {
        super.onDestroy();
		previousOne = null;
        processBA.runHook("ondestroy", this, null);
	}
    @Override 
	public void onResume() {
		super.onResume();
        mostCurrent = this;
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (activityBA != null) { //will be null during activity create (which waits for AfterLayout).
        	ResumeMessage rm = new ResumeMessage(mostCurrent);
        	BA.handler.post(rm);
        }
        processBA.runHook("onresume", this, null);
	}
    private static class ResumeMessage implements Runnable {
    	private final WeakReference<Activity> activity;
    	public ResumeMessage(Activity activity) {
    		this.activity = new WeakReference<Activity>(activity);
    	}
		public void run() {
            main mc = mostCurrent;
			if (mc == null || mc != activity.get())
				return;
			processBA.setActivityPaused(false);
            BA.LogInfo("** Activity (main) Resume **");
            if (mc != mostCurrent)
                return;
		    processBA.raiseEvent(mc._activity, "activity_resume", (Object[])null);
		}
    }
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
	      android.content.Intent data) {
		processBA.onActivityResult(requestCode, resultCode, data);
        processBA.runHook("onactivityresult", this, new Object[] {requestCode, resultCode});
	}
	private static void initializeGlobals() {
		processBA.raiseEvent2(null, true, "globals", false, (Object[])null);
	}
    public void onRequestPermissionsResult(int requestCode,
        String permissions[], int[] grantResults) {
        for (int i = 0;i < permissions.length;i++) {
            Object[] o = new Object[] {permissions[i], grantResults[i] == 0};
            processBA.raiseEventFromDifferentThread(null,null, 0, "activity_permissionresult", true, o);
        }
            
    }

public anywheresoftware.b4a.keywords.Common __c = null;
public static double _rearth = 0;
public static double _pi = 0;
public static anywheresoftware.b4a.objects.B4XViewWrapper.XUI _xui = null;
public static String _glat = "";
public static String _glon = "";
public static String _galt = "";
public static String _gground = "";
public static String _gvv = "";
public static String _gh = "";
public static String _gheading = "";
public static String _gdescent = "";
public static boolean _gunitms = false;
public static boolean _gunitkmh = false;
public anywheresoftware.b4a.objects.EditTextWrapper _edtlat = null;
public anywheresoftware.b4a.objects.EditTextWrapper _edtlon = null;
public anywheresoftware.b4a.objects.EditTextWrapper _edtalt = null;
public anywheresoftware.b4a.objects.EditTextWrapper _edtvv = null;
public anywheresoftware.b4a.objects.EditTextWrapper _edth = null;
public anywheresoftware.b4a.objects.EditTextWrapper _edtheading = null;
public anywheresoftware.b4a.objects.EditTextWrapper _edtdescent = null;
public anywheresoftware.b4a.objects.EditTextWrapper _edtground = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblresult = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btncompute = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnopenmaps = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnshare = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btncopy = null;
public anywheresoftware.b4a.objects.CompoundButtonWrapper.CheckBoxWrapper _cbms = null;
public anywheresoftware.b4a.objects.CompoundButtonWrapper.CheckBoxWrapper _cbkmh = null;
public static double _lastresultlat = 0;
public static double _lastresultlon = 0;
public static double _lasttimetoland = 0;
public static double _lasthorizdist = 0;
public anywheresoftware.b4a.objects.ScrollViewWrapper _sv = null;
public b4a.RS_calculator.doubletaptoclose _f = null;
public b4a.RS_calculator.starter _starter = null;

public static boolean isAnyActivityVisible() {
    boolean vis = false;
vis = vis | (main.mostCurrent != null);
return vis;}
public static String  _activity_create(boolean _firsttime) throws Exception{
int _gold = 0;
int _black = 0;
int _panelheight = 0;
int _y = 0;
int _h = 0;
int _w = 0;
anywheresoftware.b4j.object.JavaObject _jo1 = null;
anywheresoftware.b4j.object.JavaObject _jo2 = null;
 //BA.debugLineNum = 37;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 38;BA.debugLine="Activity.Color = Colors.RGB(18,18,18) ' dark back";
mostCurrent._activity.setColor(anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (18),(int) (18),(int) (18)));
 //BA.debugLineNum = 39;BA.debugLine="Dim gold As Int = Colors.RGB(212,175,55) ' gold t";
_gold = anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (212),(int) (175),(int) (55));
 //BA.debugLineNum = 40;BA.debugLine="Dim black As Int = Colors.Black";
_black = anywheresoftware.b4a.keywords.Common.Colors.Black;
 //BA.debugLineNum = 41;BA.debugLine="F.Initialize (\"Tap BACK again to exit\",Me,\"Before";
mostCurrent._f._initialize /*String*/ (processBA,"Tap BACK again to exit",main.getObject(),"BeforeClose",(int) (2),(int) (2000));
 //BA.debugLineNum = 43;BA.debugLine="sv.Initialize(0)";
mostCurrent._sv.Initialize(mostCurrent.activityBA,(int) (0));
 //BA.debugLineNum = 44;BA.debugLine="Activity.AddView(sv, 0, 0, 100%x, 100%y)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._sv.getObject()),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 45;BA.debugLine="Dim panelHeight As Int = 2300dip";
_panelheight = anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (2300));
 //BA.debugLineNum = 46;BA.debugLine="sv.Panel.Height = panelHeight";
mostCurrent._sv.getPanel().setHeight(_panelheight);
 //BA.debugLineNum = 48;BA.debugLine="Dim y As Int = 10dip, h As Int = 60dip, w As Int";
_y = anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10));
_h = anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (60));
_w = anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (90),mostCurrent.activityBA);
 //BA.debugLineNum = 51;BA.debugLine="CreateLabel(\"Latitude last received (°)\", y, gold";
_createlabel("Latitude last received (°)",_y,_gold);
 //BA.debugLineNum = 51;BA.debugLine="CreateLabel(\"Latitude last received (°)\", y, gold";
_y = (int) (_y+_h);
 //BA.debugLineNum = 52;BA.debugLine="edtLat = CreateEditText(y) : y = y + h";
mostCurrent._edtlat = _createedittext(_y);
 //BA.debugLineNum = 52;BA.debugLine="edtLat = CreateEditText(y) : y = y + h";
_y = (int) (_y+_h);
 //BA.debugLineNum = 53;BA.debugLine="AddDivider(y, w) : y = y + 10dip";
_adddivider(_y,_w);
 //BA.debugLineNum = 53;BA.debugLine="AddDivider(y, w) : y = y + 10dip";
_y = (int) (_y+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)));
 //BA.debugLineNum = 55;BA.debugLine="CreateLabel(\"Longitude last received (°)\", y, gol";
_createlabel("Longitude last received (°)",_y,_gold);
 //BA.debugLineNum = 55;BA.debugLine="CreateLabel(\"Longitude last received (°)\", y, gol";
_y = (int) (_y+_h);
 //BA.debugLineNum = 56;BA.debugLine="edtLon = CreateEditText(y) : y = y + h";
mostCurrent._edtlon = _createedittext(_y);
 //BA.debugLineNum = 56;BA.debugLine="edtLon = CreateEditText(y) : y = y + h";
_y = (int) (_y+_h);
 //BA.debugLineNum = 57;BA.debugLine="AddDivider(y, w) : y = y + 10dip";
_adddivider(_y,_w);
 //BA.debugLineNum = 57;BA.debugLine="AddDivider(y, w) : y = y + 10dip";
_y = (int) (_y+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)));
 //BA.debugLineNum = 59;BA.debugLine="CreateLabel(\"Altitude last received (m)\", y, gold";
_createlabel("Altitude last received (m)",_y,_gold);
 //BA.debugLineNum = 59;BA.debugLine="CreateLabel(\"Altitude last received (m)\", y, gold";
_y = (int) (_y+_h);
 //BA.debugLineNum = 60;BA.debugLine="edtAlt = CreateEditText(y) : y = y + h";
mostCurrent._edtalt = _createedittext(_y);
 //BA.debugLineNum = 60;BA.debugLine="edtAlt = CreateEditText(y) : y = y + h";
_y = (int) (_y+_h);
 //BA.debugLineNum = 61;BA.debugLine="AddDivider(y, w) : y = y + 10dip";
_adddivider(_y,_w);
 //BA.debugLineNum = 61;BA.debugLine="AddDivider(y, w) : y = y + 10dip";
_y = (int) (_y+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)));
 //BA.debugLineNum = 63;BA.debugLine="CreateLabel(\"Ground level (m)\", y, gold) : y = y";
_createlabel("Ground level (m)",_y,_gold);
 //BA.debugLineNum = 63;BA.debugLine="CreateLabel(\"Ground level (m)\", y, gold) : y = y";
_y = (int) (_y+_h);
 //BA.debugLineNum = 64;BA.debugLine="edtGround = CreateEditText(y) : y = y + h";
mostCurrent._edtground = _createedittext(_y);
 //BA.debugLineNum = 64;BA.debugLine="edtGround = CreateEditText(y) : y = y + h";
_y = (int) (_y+_h);
 //BA.debugLineNum = 65;BA.debugLine="AddDivider(y, w) : y = y + 10dip";
_adddivider(_y,_w);
 //BA.debugLineNum = 65;BA.debugLine="AddDivider(y, w) : y = y + 10dip";
_y = (int) (_y+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)));
 //BA.debugLineNum = 67;BA.debugLine="CreateLabel(\"Vertical speed  last received (m/s)";
_createlabel("Vertical speed  last received (m/s) without sign",_y,_gold);
 //BA.debugLineNum = 67;BA.debugLine="CreateLabel(\"Vertical speed  last received (m/s)";
_y = (int) (_y+_h);
 //BA.debugLineNum = 68;BA.debugLine="edtVv = CreateEditText(y) : y = y + h";
mostCurrent._edtvv = _createedittext(_y);
 //BA.debugLineNum = 68;BA.debugLine="edtVv = CreateEditText(y) : y = y + h";
_y = (int) (_y+_h);
 //BA.debugLineNum = 69;BA.debugLine="AddDivider(y, w) : y = y + 10dip";
_adddivider(_y,_w);
 //BA.debugLineNum = 69;BA.debugLine="AddDivider(y, w) : y = y + 10dip";
_y = (int) (_y+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)));
 //BA.debugLineNum = 71;BA.debugLine="CreateLabel(\"Horizontal speed last received\", y,";
_createlabel("Horizontal speed last received",_y,_gold);
 //BA.debugLineNum = 71;BA.debugLine="CreateLabel(\"Horizontal speed last received\", y,";
_y = (int) (_y+_h);
 //BA.debugLineNum = 72;BA.debugLine="edtH = CreateEditText(y) : y = y + h";
mostCurrent._edth = _createedittext(_y);
 //BA.debugLineNum = 72;BA.debugLine="edtH = CreateEditText(y) : y = y + h";
_y = (int) (_y+_h);
 //BA.debugLineNum = 73;BA.debugLine="AddDivider(y, w) : y = y + 10dip";
_adddivider(_y,_w);
 //BA.debugLineNum = 73;BA.debugLine="AddDivider(y, w) : y = y + 10dip";
_y = (int) (_y+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)));
 //BA.debugLineNum = 76;BA.debugLine="CreateLabel(\"Speed unit\", y, gold) : y = y + h";
_createlabel("Speed unit",_y,_gold);
 //BA.debugLineNum = 76;BA.debugLine="CreateLabel(\"Speed unit\", y, gold) : y = y + h";
_y = (int) (_y+_h);
 //BA.debugLineNum = 77;BA.debugLine="cbMs.Initialize(\"cbMs\")";
mostCurrent._cbms.Initialize(mostCurrent.activityBA,"cbMs");
 //BA.debugLineNum = 78;BA.debugLine="cbMs.Text = \"m/s\" : cbMs.TextColor = gold : cbMs.";
mostCurrent._cbms.setText(BA.ObjectToCharSequence("m/s"));
 //BA.debugLineNum = 78;BA.debugLine="cbMs.Text = \"m/s\" : cbMs.TextColor = gold : cbMs.";
mostCurrent._cbms.setTextColor(_gold);
 //BA.debugLineNum = 78;BA.debugLine="cbMs.Text = \"m/s\" : cbMs.TextColor = gold : cbMs.";
mostCurrent._cbms.setChecked(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 79;BA.debugLine="sv.Panel.AddView(cbMs, 5%x, y, 40%x, h)";
mostCurrent._sv.getPanel().AddView((android.view.View)(mostCurrent._cbms.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (5),mostCurrent.activityBA),_y,anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (40),mostCurrent.activityBA),_h);
 //BA.debugLineNum = 80;BA.debugLine="cbKmh.Initialize(\"cbKmh\")";
mostCurrent._cbkmh.Initialize(mostCurrent.activityBA,"cbKmh");
 //BA.debugLineNum = 81;BA.debugLine="cbKmh.Text = \"km/h\" : cbKmh.TextColor = gold";
mostCurrent._cbkmh.setText(BA.ObjectToCharSequence("km/h"));
 //BA.debugLineNum = 81;BA.debugLine="cbKmh.Text = \"km/h\" : cbKmh.TextColor = gold";
mostCurrent._cbkmh.setTextColor(_gold);
 //BA.debugLineNum = 82;BA.debugLine="sv.Panel.AddView(cbKmh, 50%x, y, 40%x, h)";
mostCurrent._sv.getPanel().AddView((android.view.View)(mostCurrent._cbkmh.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (50),mostCurrent.activityBA),_y,anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (40),mostCurrent.activityBA),_h);
 //BA.debugLineNum = 83;BA.debugLine="y = y + h";
_y = (int) (_y+_h);
 //BA.debugLineNum = 84;BA.debugLine="AddDivider(y, w) : y = y + 10dip";
_adddivider(_y,_w);
 //BA.debugLineNum = 84;BA.debugLine="AddDivider(y, w) : y = y + 10dip";
_y = (int) (_y+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)));
 //BA.debugLineNum = 86;BA.debugLine="Dim jo1 As JavaObject = cbMs";
_jo1 = new anywheresoftware.b4j.object.JavaObject();
_jo1 = (anywheresoftware.b4j.object.JavaObject) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4j.object.JavaObject(), (java.lang.Object)(mostCurrent._cbms.getObject()));
 //BA.debugLineNum = 87;BA.debugLine="Dim jo1 As JavaObject = cbMs";
_jo1 = new anywheresoftware.b4j.object.JavaObject();
_jo1 = (anywheresoftware.b4j.object.JavaObject) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4j.object.JavaObject(), (java.lang.Object)(mostCurrent._cbms.getObject()));
 //BA.debugLineNum = 88;BA.debugLine="If jo1.IsInitialized Then";
if (_jo1.IsInitialized()) { 
 //BA.debugLineNum = 89;BA.debugLine="Try";
try { //BA.debugLineNum = 90;BA.debugLine="jo1.RunMethod(\"setButtonTintList\", Array(Create";
_jo1.RunMethod("setButtonTintList",new Object[]{_createcolorstatelist(anywheresoftware.b4a.keywords.Common.Colors.White)});
 } 
       catch (Exception e66) {
			processBA.setLastException(e66); //BA.debugLineNum = 92;BA.debugLine="Log(\"Tint unsupported on this Android version.\"";
anywheresoftware.b4a.keywords.Common.LogImpl("0131127","Tint unsupported on this Android version.",0);
 };
 };
 //BA.debugLineNum = 96;BA.debugLine="Dim jo2 As JavaObject = cbKmh";
_jo2 = new anywheresoftware.b4j.object.JavaObject();
_jo2 = (anywheresoftware.b4j.object.JavaObject) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4j.object.JavaObject(), (java.lang.Object)(mostCurrent._cbkmh.getObject()));
 //BA.debugLineNum = 97;BA.debugLine="If jo2.IsInitialized Then";
if (_jo2.IsInitialized()) { 
 //BA.debugLineNum = 98;BA.debugLine="Try";
try { //BA.debugLineNum = 99;BA.debugLine="jo2.RunMethod(\"setButtonTintList\", Array(Create";
_jo2.RunMethod("setButtonTintList",new Object[]{_createcolorstatelist(anywheresoftware.b4a.keywords.Common.Colors.White)});
 } 
       catch (Exception e74) {
			processBA.setLastException(e74); //BA.debugLineNum = 101;BA.debugLine="Log(\"Tint unsupported on this Android version.\"";
anywheresoftware.b4a.keywords.Common.LogImpl("0131136","Tint unsupported on this Android version.",0);
 };
 };
 //BA.debugLineNum = 106;BA.debugLine="CreateLabel(\"Direction - Course last received (°)";
_createlabel("Direction - Course last received (°)",_y,_gold);
 //BA.debugLineNum = 106;BA.debugLine="CreateLabel(\"Direction - Course last received (°)";
_y = (int) (_y+_h);
 //BA.debugLineNum = 107;BA.debugLine="edtHeading = CreateEditText(y) : y = y + h";
mostCurrent._edtheading = _createedittext(_y);
 //BA.debugLineNum = 107;BA.debugLine="edtHeading = CreateEditText(y) : y = y + h";
_y = (int) (_y+_h);
 //BA.debugLineNum = 108;BA.debugLine="AddDivider(y, w) : y = y + 10dip";
_adddivider(_y,_w);
 //BA.debugLineNum = 108;BA.debugLine="AddDivider(y, w) : y = y + 10dip";
_y = (int) (_y+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)));
 //BA.debugLineNum = 110;BA.debugLine="CreateLabel(\"Correction factor (m)\", y, gold) : y";
_createlabel("Correction factor (m)",_y,_gold);
 //BA.debugLineNum = 110;BA.debugLine="CreateLabel(\"Correction factor (m)\", y, gold) : y";
_y = (int) (_y+_h);
 //BA.debugLineNum = 111;BA.debugLine="edtDescent = CreateEditText(y) : edtDescent.Text";
mostCurrent._edtdescent = _createedittext(_y);
 //BA.debugLineNum = 111;BA.debugLine="edtDescent = CreateEditText(y) : edtDescent.Text";
mostCurrent._edtdescent.setText(BA.ObjectToCharSequence("45"));
 //BA.debugLineNum = 111;BA.debugLine="edtDescent = CreateEditText(y) : edtDescent.Text";
_y = (int) (_y+_h);
 //BA.debugLineNum = 112;BA.debugLine="AddDivider(y, w) : y = y + 10dip";
_adddivider(_y,_w);
 //BA.debugLineNum = 112;BA.debugLine="AddDivider(y, w) : y = y + 10dip";
_y = (int) (_y+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)));
 //BA.debugLineNum = 115;BA.debugLine="btnCompute.Initialize(\"btnCompute\")";
mostCurrent._btncompute.Initialize(mostCurrent.activityBA,"btnCompute");
 //BA.debugLineNum = 116;BA.debugLine="btnCompute.Text = \"Calculate predict landing poin";
mostCurrent._btncompute.setText(BA.ObjectToCharSequence("Calculate predict landing point"));
 //BA.debugLineNum = 116;BA.debugLine="btnCompute.Text = \"Calculate predict landing poin";
mostCurrent._btncompute.setTextColor(_black);
 //BA.debugLineNum = 117;BA.debugLine="sv.Panel.AddView(btnCompute, 5%x, y, w, h) : y =";
mostCurrent._sv.getPanel().AddView((android.view.View)(mostCurrent._btncompute.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (5),mostCurrent.activityBA),_y,_w,_h);
 //BA.debugLineNum = 117;BA.debugLine="sv.Panel.AddView(btnCompute, 5%x, y, w, h) : y =";
_y = (int) (_y+_h);
 //BA.debugLineNum = 119;BA.debugLine="lblResult.Initialize(\"\")";
mostCurrent._lblresult.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 120;BA.debugLine="lblResult.TextColor = gold";
mostCurrent._lblresult.setTextColor(_gold);
 //BA.debugLineNum = 121;BA.debugLine="lblResult.TextSize = 16";
mostCurrent._lblresult.setTextSize((float) (16));
 //BA.debugLineNum = 122;BA.debugLine="lblResult.Gravity = Bit.Or(Gravity.CENTER_HORIZON";
mostCurrent._lblresult.setGravity(anywheresoftware.b4a.keywords.Common.Bit.Or(anywheresoftware.b4a.keywords.Common.Gravity.CENTER_HORIZONTAL,anywheresoftware.b4a.keywords.Common.Gravity.TOP));
 //BA.debugLineNum = 123;BA.debugLine="sv.Panel.AddView(lblResult, 5%x, y, w, 200dip)";
mostCurrent._sv.getPanel().AddView((android.view.View)(mostCurrent._lblresult.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (5),mostCurrent.activityBA),_y,_w,anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (200)));
 //BA.debugLineNum = 124;BA.debugLine="y = y + 200dip";
_y = (int) (_y+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (200)));
 //BA.debugLineNum = 126;BA.debugLine="btnOpenMaps.Initialize(\"btnOpenMaps\") : btnOpenMa";
mostCurrent._btnopenmaps.Initialize(mostCurrent.activityBA,"btnOpenMaps");
 //BA.debugLineNum = 126;BA.debugLine="btnOpenMaps.Initialize(\"btnOpenMaps\") : btnOpenMa";
mostCurrent._btnopenmaps.setText(BA.ObjectToCharSequence("Open Maps"));
 //BA.debugLineNum = 126;BA.debugLine="btnOpenMaps.Initialize(\"btnOpenMaps\") : btnOpenMa";
mostCurrent._btnopenmaps.setTextColor(_black);
 //BA.debugLineNum = 127;BA.debugLine="sv.Panel.AddView(btnOpenMaps, 5%x, y, w, h) : y =";
mostCurrent._sv.getPanel().AddView((android.view.View)(mostCurrent._btnopenmaps.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (5),mostCurrent.activityBA),_y,_w,_h);
 //BA.debugLineNum = 127;BA.debugLine="sv.Panel.AddView(btnOpenMaps, 5%x, y, w, h) : y =";
_y = (int) (_y+_h);
 //BA.debugLineNum = 129;BA.debugLine="btnShare.Initialize(\"btnShare\") : btnShare.Text =";
mostCurrent._btnshare.Initialize(mostCurrent.activityBA,"btnShare");
 //BA.debugLineNum = 129;BA.debugLine="btnShare.Initialize(\"btnShare\") : btnShare.Text =";
mostCurrent._btnshare.setText(BA.ObjectToCharSequence("Share"));
 //BA.debugLineNum = 129;BA.debugLine="btnShare.Initialize(\"btnShare\") : btnShare.Text =";
mostCurrent._btnshare.setTextColor(_black);
 //BA.debugLineNum = 130;BA.debugLine="sv.Panel.AddView(btnShare, 5%x, y, w, h) : y = y";
mostCurrent._sv.getPanel().AddView((android.view.View)(mostCurrent._btnshare.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (5),mostCurrent.activityBA),_y,_w,_h);
 //BA.debugLineNum = 130;BA.debugLine="sv.Panel.AddView(btnShare, 5%x, y, w, h) : y = y";
_y = (int) (_y+_h);
 //BA.debugLineNum = 132;BA.debugLine="btnCopy.Initialize(\"btnCopy\") : btnCopy.Text = \"C";
mostCurrent._btncopy.Initialize(mostCurrent.activityBA,"btnCopy");
 //BA.debugLineNum = 132;BA.debugLine="btnCopy.Initialize(\"btnCopy\") : btnCopy.Text = \"C";
mostCurrent._btncopy.setText(BA.ObjectToCharSequence("Copy coords"));
 //BA.debugLineNum = 132;BA.debugLine="btnCopy.Initialize(\"btnCopy\") : btnCopy.Text = \"C";
mostCurrent._btncopy.setTextColor(_black);
 //BA.debugLineNum = 133;BA.debugLine="sv.Panel.AddView(btnCopy, 5%x, y, w, h)";
mostCurrent._sv.getPanel().AddView((android.view.View)(mostCurrent._btncopy.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (5),mostCurrent.activityBA),_y,_w,_h);
 //BA.debugLineNum = 136;BA.debugLine="If gLat <> \"\" Then edtLat.Text = gLat";
if ((_glat).equals("") == false) { 
mostCurrent._edtlat.setText(BA.ObjectToCharSequence(_glat));};
 //BA.debugLineNum = 137;BA.debugLine="If gLon <> \"\" Then edtLon.Text = gLon";
if ((_glon).equals("") == false) { 
mostCurrent._edtlon.setText(BA.ObjectToCharSequence(_glon));};
 //BA.debugLineNum = 138;BA.debugLine="If gAlt <> \"\" Then edtAlt.Text = gAlt";
if ((_galt).equals("") == false) { 
mostCurrent._edtalt.setText(BA.ObjectToCharSequence(_galt));};
 //BA.debugLineNum = 139;BA.debugLine="If gGround <> \"\" Then edtGround.Text = gGround";
if ((_gground).equals("") == false) { 
mostCurrent._edtground.setText(BA.ObjectToCharSequence(_gground));};
 //BA.debugLineNum = 140;BA.debugLine="If gVv <> \"\" Then edtVv.Text = gVv";
if ((_gvv).equals("") == false) { 
mostCurrent._edtvv.setText(BA.ObjectToCharSequence(_gvv));};
 //BA.debugLineNum = 141;BA.debugLine="If gH <> \"\" Then edtH.Text = gH";
if ((_gh).equals("") == false) { 
mostCurrent._edth.setText(BA.ObjectToCharSequence(_gh));};
 //BA.debugLineNum = 142;BA.debugLine="If gHeading <> \"\" Then edtHeading.Text = gHeading";
if ((_gheading).equals("") == false) { 
mostCurrent._edtheading.setText(BA.ObjectToCharSequence(_gheading));};
 //BA.debugLineNum = 143;BA.debugLine="If gDescent <> \"\" Then edtDescent.Text = gDescent";
if ((_gdescent).equals("") == false) { 
mostCurrent._edtdescent.setText(BA.ObjectToCharSequence(_gdescent));};
 //BA.debugLineNum = 144;BA.debugLine="cbMs.Checked = gUnitMs";
mostCurrent._cbms.setChecked(_gunitms);
 //BA.debugLineNum = 145;BA.debugLine="cbKmh.Checked = gUnitKmh";
mostCurrent._cbkmh.setChecked(_gunitkmh);
 //BA.debugLineNum = 148;BA.debugLine="If cbMs.Checked = False And cbKmh.Checked = False";
if (mostCurrent._cbms.getChecked()==anywheresoftware.b4a.keywords.Common.False && mostCurrent._cbkmh.getChecked()==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 149;BA.debugLine="cbMs.Checked = True";
mostCurrent._cbms.setChecked(anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 151;BA.debugLine="End Sub";
return "";
}
public static boolean  _activity_keypress(int _keycode) throws Exception{
 //BA.debugLineNum = 356;BA.debugLine="Sub Activity_KeyPress (KeyCode As Int) As Boolean";
 //BA.debugLineNum = 357;BA.debugLine="If KeyCode = KeyCodes.KEYCODE_BACK Then";
if (_keycode==anywheresoftware.b4a.keywords.Common.KeyCodes.KEYCODE_BACK) { 
 //BA.debugLineNum = 358;BA.debugLine="F.TapToClose";
mostCurrent._f._taptoclose /*String*/ ();
 //BA.debugLineNum = 359;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 };
 //BA.debugLineNum = 361;BA.debugLine="End Sub";
return false;
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 342;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 344;BA.debugLine="gLat = edtLat.Text";
_glat = mostCurrent._edtlat.getText();
 //BA.debugLineNum = 345;BA.debugLine="gLon = edtLon.Text";
_glon = mostCurrent._edtlon.getText();
 //BA.debugLineNum = 346;BA.debugLine="gAlt = edtAlt.Text";
_galt = mostCurrent._edtalt.getText();
 //BA.debugLineNum = 347;BA.debugLine="gGround = edtGround.Text";
_gground = mostCurrent._edtground.getText();
 //BA.debugLineNum = 348;BA.debugLine="gVv = edtVv.Text";
_gvv = mostCurrent._edtvv.getText();
 //BA.debugLineNum = 349;BA.debugLine="gH = edtH.Text";
_gh = mostCurrent._edth.getText();
 //BA.debugLineNum = 350;BA.debugLine="gHeading = edtHeading.Text";
_gheading = mostCurrent._edtheading.getText();
 //BA.debugLineNum = 351;BA.debugLine="gDescent = edtDescent.Text";
_gdescent = mostCurrent._edtdescent.getText();
 //BA.debugLineNum = 352;BA.debugLine="gUnitMs = cbMs.Checked";
_gunitms = mostCurrent._cbms.getChecked();
 //BA.debugLineNum = 353;BA.debugLine="gUnitKmh = cbKmh.Checked";
_gunitkmh = mostCurrent._cbkmh.getChecked();
 //BA.debugLineNum = 354;BA.debugLine="End Sub";
return "";
}
public static String  _activity_restoreinstancestate(anywheresoftware.b4a.objects.collections.Map _savedstate) throws Exception{
 //BA.debugLineNum = 328;BA.debugLine="Sub Activity_RestoreInstanceState (SavedState As M";
 //BA.debugLineNum = 329;BA.debugLine="If SavedState.ContainsKey(\"lat\") Then edtLat.Text";
if (_savedstate.ContainsKey((Object)("lat"))) { 
mostCurrent._edtlat.setText(BA.ObjectToCharSequence(_savedstate.Get((Object)("lat"))));};
 //BA.debugLineNum = 330;BA.debugLine="If SavedState.ContainsKey(\"lon\") Then edtLon.Text";
if (_savedstate.ContainsKey((Object)("lon"))) { 
mostCurrent._edtlon.setText(BA.ObjectToCharSequence(_savedstate.Get((Object)("lon"))));};
 //BA.debugLineNum = 331;BA.debugLine="If SavedState.ContainsKey(\"alt\") Then edtAlt.Text";
if (_savedstate.ContainsKey((Object)("alt"))) { 
mostCurrent._edtalt.setText(BA.ObjectToCharSequence(_savedstate.Get((Object)("alt"))));};
 //BA.debugLineNum = 332;BA.debugLine="If SavedState.ContainsKey(\"ground\") Then edtGroun";
if (_savedstate.ContainsKey((Object)("ground"))) { 
mostCurrent._edtground.setText(BA.ObjectToCharSequence(_savedstate.Get((Object)("ground"))));};
 //BA.debugLineNum = 333;BA.debugLine="If SavedState.ContainsKey(\"vv\") Then edtVv.Text =";
if (_savedstate.ContainsKey((Object)("vv"))) { 
mostCurrent._edtvv.setText(BA.ObjectToCharSequence(_savedstate.Get((Object)("vv"))));};
 //BA.debugLineNum = 334;BA.debugLine="If SavedState.ContainsKey(\"h\") Then edtH.Text = S";
if (_savedstate.ContainsKey((Object)("h"))) { 
mostCurrent._edth.setText(BA.ObjectToCharSequence(_savedstate.Get((Object)("h"))));};
 //BA.debugLineNum = 335;BA.debugLine="If SavedState.ContainsKey(\"heading\") Then edtHead";
if (_savedstate.ContainsKey((Object)("heading"))) { 
mostCurrent._edtheading.setText(BA.ObjectToCharSequence(_savedstate.Get((Object)("heading"))));};
 //BA.debugLineNum = 336;BA.debugLine="If SavedState.ContainsKey(\"descent\") Then edtDesc";
if (_savedstate.ContainsKey((Object)("descent"))) { 
mostCurrent._edtdescent.setText(BA.ObjectToCharSequence(_savedstate.Get((Object)("descent"))));};
 //BA.debugLineNum = 337;BA.debugLine="If SavedState.ContainsKey(\"unitMs\") Then cbMs.Che";
if (_savedstate.ContainsKey((Object)("unitMs"))) { 
mostCurrent._cbms.setChecked(BA.ObjectToBoolean(_savedstate.Get((Object)("unitMs"))));};
 //BA.debugLineNum = 338;BA.debugLine="If SavedState.ContainsKey(\"unitKmh\") Then cbKmh.C";
if (_savedstate.ContainsKey((Object)("unitKmh"))) { 
mostCurrent._cbkmh.setChecked(BA.ObjectToBoolean(_savedstate.Get((Object)("unitKmh"))));};
 //BA.debugLineNum = 339;BA.debugLine="End Sub";
return "";
}
public static String  _activity_saveinstancestate(anywheresoftware.b4a.objects.collections.Map _outstate) throws Exception{
 //BA.debugLineNum = 315;BA.debugLine="Sub Activity_SaveInstanceState (OutState As Map)";
 //BA.debugLineNum = 316;BA.debugLine="OutState.Put(\"lat\", edtLat.Text)";
_outstate.Put((Object)("lat"),(Object)(mostCurrent._edtlat.getText()));
 //BA.debugLineNum = 317;BA.debugLine="OutState.Put(\"lon\", edtLon.Text)";
_outstate.Put((Object)("lon"),(Object)(mostCurrent._edtlon.getText()));
 //BA.debugLineNum = 318;BA.debugLine="OutState.Put(\"alt\", edtAlt.Text)";
_outstate.Put((Object)("alt"),(Object)(mostCurrent._edtalt.getText()));
 //BA.debugLineNum = 319;BA.debugLine="OutState.Put(\"ground\", edtGround.Text)";
_outstate.Put((Object)("ground"),(Object)(mostCurrent._edtground.getText()));
 //BA.debugLineNum = 320;BA.debugLine="OutState.Put(\"vv\", edtVv.Text)";
_outstate.Put((Object)("vv"),(Object)(mostCurrent._edtvv.getText()));
 //BA.debugLineNum = 321;BA.debugLine="OutState.Put(\"h\", edtH.Text)";
_outstate.Put((Object)("h"),(Object)(mostCurrent._edth.getText()));
 //BA.debugLineNum = 322;BA.debugLine="OutState.Put(\"heading\", edtHeading.Text)";
_outstate.Put((Object)("heading"),(Object)(mostCurrent._edtheading.getText()));
 //BA.debugLineNum = 323;BA.debugLine="OutState.Put(\"descent\", edtDescent.Text)";
_outstate.Put((Object)("descent"),(Object)(mostCurrent._edtdescent.getText()));
 //BA.debugLineNum = 324;BA.debugLine="OutState.Put(\"unitMs\", cbMs.Checked)";
_outstate.Put((Object)("unitMs"),(Object)(mostCurrent._cbms.getChecked()));
 //BA.debugLineNum = 325;BA.debugLine="OutState.Put(\"unitKmh\", cbKmh.Checked)";
_outstate.Put((Object)("unitKmh"),(Object)(mostCurrent._cbkmh.getChecked()));
 //BA.debugLineNum = 326;BA.debugLine="End Sub";
return "";
}
public static anywheresoftware.b4a.objects.PanelWrapper  _adddivider(int _y,int _width) throws Exception{
anywheresoftware.b4a.objects.PanelWrapper _p = null;
 //BA.debugLineNum = 177;BA.debugLine="Sub AddDivider(y As Int, width As Int) As Panel";
 //BA.debugLineNum = 178;BA.debugLine="Dim p As Panel";
_p = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 179;BA.debugLine="p.Initialize(\"\")";
_p.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 180;BA.debugLine="p.Color = Colors.White ' White line";
_p.setColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 181;BA.debugLine="sv.Panel.AddView(p, 5%x, y, width, 2dip) ' 2dip h";
mostCurrent._sv.getPanel().AddView((android.view.View)(_p.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (5),mostCurrent.activityBA),_y,_width,anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (2)));
 //BA.debugLineNum = 182;BA.debugLine="Return p";
if (true) return _p;
 //BA.debugLineNum = 183;BA.debugLine="End Sub";
return null;
}
public static String  _btncompute_click() throws Exception{
double _lat = 0;
double _lon = 0;
double _alt = 0;
double _ground = 0;
double _vv = 0;
double _hin = 0;
String _unit = "";
double _heading = 0;
double _descentfactor = 0;
double _effectivealtitude = 0;
double _hspeed = 0;
double[] _res = null;
 //BA.debugLineNum = 199;BA.debugLine="Sub btnCompute_Click";
 //BA.debugLineNum = 200;BA.debugLine="Dim lat As Double = edtLat.Text";
_lat = (double)(Double.parseDouble(mostCurrent._edtlat.getText()));
 //BA.debugLineNum = 201;BA.debugLine="Dim lon As Double = edtLon.Text";
_lon = (double)(Double.parseDouble(mostCurrent._edtlon.getText()));
 //BA.debugLineNum = 202;BA.debugLine="Dim alt As Double = edtAlt.Text";
_alt = (double)(Double.parseDouble(mostCurrent._edtalt.getText()));
 //BA.debugLineNum = 203;BA.debugLine="Dim ground As Double = edtGround.Text";
_ground = (double)(Double.parseDouble(mostCurrent._edtground.getText()));
 //BA.debugLineNum = 204;BA.debugLine="Dim vv As Double = edtVv.Text";
_vv = (double)(Double.parseDouble(mostCurrent._edtvv.getText()));
 //BA.debugLineNum = 205;BA.debugLine="Dim hIn As Double = edtH.Text";
_hin = (double)(Double.parseDouble(mostCurrent._edth.getText()));
 //BA.debugLineNum = 206;BA.debugLine="Dim unit As String = IIf(cbKmh.Checked, \"km/h\", \"";
_unit = BA.ObjectToString(((mostCurrent._cbkmh.getChecked()) ? ((Object)("km/h")) : ((Object)("m/s"))));
 //BA.debugLineNum = 207;BA.debugLine="Dim heading As Double = edtHeading.Text";
_heading = (double)(Double.parseDouble(mostCurrent._edtheading.getText()));
 //BA.debugLineNum = 208;BA.debugLine="Dim descentFactor As Double = edtDescent.Text";
_descentfactor = (double)(Double.parseDouble(mostCurrent._edtdescent.getText()));
 //BA.debugLineNum = 210;BA.debugLine="Dim effectiveAltitude As Double = alt - descentFa";
_effectivealtitude = _alt-_descentfactor-_ground;
 //BA.debugLineNum = 211;BA.debugLine="If effectiveAltitude < 0 Then effectiveAltitude =";
if (_effectivealtitude<0) { 
_effectivealtitude = 0;};
 //BA.debugLineNum = 213;BA.debugLine="Dim hSpeed As Double = IIf(unit = \"km/h\", KmH_To_";
_hspeed = (double)(BA.ObjectToNumber((((_unit).equals("km/h")) ? ((Object)(_kmh_to_mps(_hin))) : ((Object)(_hin)))));
 //BA.debugLineNum = 214;BA.debugLine="If vv <= 0 Then";
if (_vv<=0) { 
 //BA.debugLineNum = 215;BA.debugLine="ToastMessageShow(\"Vert. speed must be > 0 (m/s d";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Vert. speed must be > 0 (m/s downward)"),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 216;BA.debugLine="Return";
if (true) return "";
 };
 //BA.debugLineNum = 219;BA.debugLine="Dim res() As Double = ComputeLanding(lat, lon, ef";
_res = _computelanding(_lat,_lon,_effectivealtitude,_vv,_hspeed,_heading,0);
 //BA.debugLineNum = 220;BA.debugLine="lastResultLat = res(0)";
_lastresultlat = _res[(int) (0)];
 //BA.debugLineNum = 221;BA.debugLine="lastResultLon = res(1)";
_lastresultlon = _res[(int) (1)];
 //BA.debugLineNum = 222;BA.debugLine="lastTimeToLand = res(2)";
_lasttimetoland = _res[(int) (2)];
 //BA.debugLineNum = 223;BA.debugLine="lastHorizDist = res(3)";
_lasthorizdist = _res[(int) (3)];
 //BA.debugLineNum = 225;BA.debugLine="lblResult.Text = $\"Predicted landing point: Lat:";
mostCurrent._lblresult.setText(BA.ObjectToCharSequence(("Predicted landing point:\n"+"Lat: "+anywheresoftware.b4a.keywords.Common.SmartStringFormatter("",(Object)(anywheresoftware.b4a.keywords.Common.NumberFormat2(_lastresultlat,(int) (1),(int) (5),(int) (5),anywheresoftware.b4a.keywords.Common.False)))+"\n"+"Lon: "+anywheresoftware.b4a.keywords.Common.SmartStringFormatter("",(Object)(anywheresoftware.b4a.keywords.Common.NumberFormat2(_lastresultlon,(int) (1),(int) (5),(int) (5),anywheresoftware.b4a.keywords.Common.False)))+"\n"+"Time remaining to landing: "+anywheresoftware.b4a.keywords.Common.SmartStringFormatter("",(Object)(_myround2(_lasttimetoland,(int) (2))))+" s\n"+"Distance remaining to landing: "+anywheresoftware.b4a.keywords.Common.SmartStringFormatter("",(Object)(_myround2(_lasthorizdist,(int) (2))))+" m")));
 //BA.debugLineNum = 230;BA.debugLine="End Sub";
return "";
}
public static String  _btncopy_click() throws Exception{
String _txt = "";
b4a.util.BClipboard _clip = null;
 //BA.debugLineNum = 243;BA.debugLine="Sub btnCopy_Click";
 //BA.debugLineNum = 244;BA.debugLine="If lastResultLat = 0 And lastResultLon = 0 Then";
if (_lastresultlat==0 && _lastresultlon==0) { 
 //BA.debugLineNum = 245;BA.debugLine="ToastMessageShow(\"Calculate first\", True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Calculate first"),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 246;BA.debugLine="Return";
if (true) return "";
 };
 //BA.debugLineNum = 249;BA.debugLine="Dim txt As String = $\"${NumberFormat2(lastResultL";
_txt = (""+anywheresoftware.b4a.keywords.Common.SmartStringFormatter("",(Object)(anywheresoftware.b4a.keywords.Common.NumberFormat2(_lastresultlat,(int) (1),(int) (5),(int) (5),anywheresoftware.b4a.keywords.Common.False)))+","+anywheresoftware.b4a.keywords.Common.SmartStringFormatter("",(Object)(anywheresoftware.b4a.keywords.Common.NumberFormat2(_lastresultlon,(int) (1),(int) (5),(int) (5),anywheresoftware.b4a.keywords.Common.False)))+"");
 //BA.debugLineNum = 253;BA.debugLine="Dim clip As BClipboard";
_clip = new b4a.util.BClipboard();
 //BA.debugLineNum = 254;BA.debugLine="clip.clrText";
_clip.clrText(mostCurrent.activityBA);
 //BA.debugLineNum = 255;BA.debugLine="clip.settext(txt)";
_clip.setText(mostCurrent.activityBA,_txt);
 //BA.debugLineNum = 257;BA.debugLine="ToastMessageShow(\"Copied to Clipboard: \" & txt, T";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Copied to Clipboard: "+_txt),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 258;BA.debugLine="End Sub";
return "";
}
public static String  _btnopenmaps_click() throws Exception{
String _uri = "";
anywheresoftware.b4a.objects.IntentWrapper _i = null;
 //BA.debugLineNum = 232;BA.debugLine="Sub btnOpenMaps_Click";
 //BA.debugLineNum = 233;BA.debugLine="If lastResultLat = 0 And lastResultLon = 0 Then";
if (_lastresultlat==0 && _lastresultlon==0) { 
 //BA.debugLineNum = 234;BA.debugLine="ToastMessageShow(\"Compute first\", True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Compute first"),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 235;BA.debugLine="Return";
if (true) return "";
 };
 //BA.debugLineNum = 237;BA.debugLine="Dim uri As String = $\"geo:${lastResultLat},${last";
_uri = ("geo:"+anywheresoftware.b4a.keywords.Common.SmartStringFormatter("",(Object)(_lastresultlat))+","+anywheresoftware.b4a.keywords.Common.SmartStringFormatter("",(Object)(_lastresultlon))+"?q="+anywheresoftware.b4a.keywords.Common.SmartStringFormatter("",(Object)(_lastresultlat))+","+anywheresoftware.b4a.keywords.Common.SmartStringFormatter("",(Object)(_lastresultlon))+"(PredictedLanding)");
 //BA.debugLineNum = 238;BA.debugLine="Dim i As Intent";
_i = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 239;BA.debugLine="i.Initialize(i.ACTION_VIEW, uri)";
_i.Initialize(_i.ACTION_VIEW,_uri);
 //BA.debugLineNum = 240;BA.debugLine="StartActivity(i)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(_i.getObject()));
 //BA.debugLineNum = 241;BA.debugLine="End Sub";
return "";
}
public static String  _btnshare_click() throws Exception{
String _txt = "";
anywheresoftware.b4a.objects.IntentWrapper _i = null;
 //BA.debugLineNum = 266;BA.debugLine="Sub btnShare_Click";
 //BA.debugLineNum = 267;BA.debugLine="If lastResultLat = 0 And lastResultLon = 0 Then";
if (_lastresultlat==0 && _lastresultlon==0) { 
 //BA.debugLineNum = 268;BA.debugLine="ToastMessageShow(\"Calculate first\", True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Calculate first"),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 269;BA.debugLine="Return";
if (true) return "";
 };
 //BA.debugLineNum = 271;BA.debugLine="Dim txt As String = $\"Predicted landing point: ${";
_txt = ("Predicted landing point: "+anywheresoftware.b4a.keywords.Common.SmartStringFormatter("",(Object)(anywheresoftware.b4a.keywords.Common.NumberFormat2(_lastresultlat,(int) (1),(int) (5),(int) (5),anywheresoftware.b4a.keywords.Common.False)))+", "+anywheresoftware.b4a.keywords.Common.SmartStringFormatter("",(Object)(anywheresoftware.b4a.keywords.Common.NumberFormat2(_lastresultlon,(int) (1),(int) (5),(int) (5),anywheresoftware.b4a.keywords.Common.False)))+"\n"+"Time remaining to landing: "+anywheresoftware.b4a.keywords.Common.SmartStringFormatter("",(Object)(_myround2(_lasttimetoland,(int) (2))))+" s\n"+"Distance remaining to landing: "+anywheresoftware.b4a.keywords.Common.SmartStringFormatter("",(Object)(_myround2(_lasthorizdist,(int) (2))))+" m");
 //BA.debugLineNum = 274;BA.debugLine="Dim i As Intent";
_i = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 275;BA.debugLine="i.Initialize(i.ACTION_SEND, \"\")";
_i.Initialize(_i.ACTION_SEND,"");
 //BA.debugLineNum = 276;BA.debugLine="i.PutExtra(\"android.intent.extra.TEXT\", txt)";
_i.PutExtra("android.intent.extra.TEXT",(Object)(_txt));
 //BA.debugLineNum = 277;BA.debugLine="i.SetType(\"text/plain\")";
_i.SetType("text/plain");
 //BA.debugLineNum = 278;BA.debugLine="StartActivity(i)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(_i.getObject()));
 //BA.debugLineNum = 279;BA.debugLine="End Sub";
return "";
}
public static String  _cbkmh_checkedchange(boolean _checked) throws Exception{
 //BA.debugLineNum = 192;BA.debugLine="Sub cbKmh_CheckedChange(Checked As Boolean)";
 //BA.debugLineNum = 193;BA.debugLine="If Checked Then cbMs.Checked = False";
if (_checked) { 
mostCurrent._cbms.setChecked(anywheresoftware.b4a.keywords.Common.False);};
 //BA.debugLineNum = 194;BA.debugLine="End Sub";
return "";
}
public static String  _cbms_checkedchange(boolean _checked) throws Exception{
 //BA.debugLineNum = 188;BA.debugLine="Sub cbMs_CheckedChange(Checked As Boolean)";
 //BA.debugLineNum = 189;BA.debugLine="If Checked Then cbKmh.Checked = False";
if (_checked) { 
mostCurrent._cbkmh.setChecked(anywheresoftware.b4a.keywords.Common.False);};
 //BA.debugLineNum = 190;BA.debugLine="End Sub";
return "";
}
public static double[]  _computelanding(double _latdeg,double _londeg,double _altmeters,double _vvertical,double _hspeed,double _headingdeg,double _descentfactor) throws Exception{
double[] _res = null;
double _timetoland = 0;
double _horizdist = 0;
double _lat1 = 0;
double _lon1 = 0;
double _brg = 0;
double _d = 0;
double _r = 0;
double _lat2 = 0;
double _lon2 = 0;
 //BA.debugLineNum = 293;BA.debugLine="Sub ComputeLanding(latDeg As Double, lonDeg As Dou";
 //BA.debugLineNum = 294;BA.debugLine="Dim res(4) As Double";
_res = new double[(int) (4)];
;
 //BA.debugLineNum = 295;BA.debugLine="If vVertical <= 0 Then";
if (_vvertical<=0) { 
 //BA.debugLineNum = 296;BA.debugLine="res(0)=latDeg:res(1)=lonDeg:res(2)=0:res(3)=0";
_res[(int) (0)] = _latdeg;
 //BA.debugLineNum = 296;BA.debugLine="res(0)=latDeg:res(1)=lonDeg:res(2)=0:res(3)=0";
_res[(int) (1)] = _londeg;
 //BA.debugLineNum = 296;BA.debugLine="res(0)=latDeg:res(1)=lonDeg:res(2)=0:res(3)=0";
_res[(int) (2)] = 0;
 //BA.debugLineNum = 296;BA.debugLine="res(0)=latDeg:res(1)=lonDeg:res(2)=0:res(3)=0";
_res[(int) (3)] = 0;
 //BA.debugLineNum = 297;BA.debugLine="Return res";
if (true) return _res;
 };
 //BA.debugLineNum = 299;BA.debugLine="Dim timeToLand As Double = altMeters / vVertical";
_timetoland = _altmeters/(double)_vvertical;
 //BA.debugLineNum = 300;BA.debugLine="Dim horizDist As Double = hSpeed * timeToLand";
_horizdist = _hspeed*_timetoland;
 //BA.debugLineNum = 301;BA.debugLine="Dim lat1 As Double = latDeg * PI / 180";
_lat1 = _latdeg*_pi/(double)180;
 //BA.debugLineNum = 302;BA.debugLine="Dim lon1 As Double = lonDeg * PI / 180";
_lon1 = _londeg*_pi/(double)180;
 //BA.debugLineNum = 303;BA.debugLine="Dim brg As Double = headingDeg * PI / 180";
_brg = _headingdeg*_pi/(double)180;
 //BA.debugLineNum = 304;BA.debugLine="Dim d As Double = horizDist";
_d = _horizdist;
 //BA.debugLineNum = 305;BA.debugLine="Dim R As Double = rEarth";
_r = _rearth;
 //BA.debugLineNum = 306;BA.debugLine="Dim lat2 As Double = ASin(Sin(lat1)*Cos(d/R) + Co";
_lat2 = anywheresoftware.b4a.keywords.Common.ASin(anywheresoftware.b4a.keywords.Common.Sin(_lat1)*anywheresoftware.b4a.keywords.Common.Cos(_d/(double)_r)+anywheresoftware.b4a.keywords.Common.Cos(_lat1)*anywheresoftware.b4a.keywords.Common.Sin(_d/(double)_r)*anywheresoftware.b4a.keywords.Common.Cos(_brg));
 //BA.debugLineNum = 307;BA.debugLine="Dim lon2 As Double = lon1 + ATan2(Sin(brg)*Sin(d/";
_lon2 = _lon1+anywheresoftware.b4a.keywords.Common.ATan2(anywheresoftware.b4a.keywords.Common.Sin(_brg)*anywheresoftware.b4a.keywords.Common.Sin(_d/(double)_r)*anywheresoftware.b4a.keywords.Common.Cos(_lat1),anywheresoftware.b4a.keywords.Common.Cos(_d/(double)_r)-anywheresoftware.b4a.keywords.Common.Sin(_lat1)*anywheresoftware.b4a.keywords.Common.Sin(_lat2));
 //BA.debugLineNum = 308;BA.debugLine="lat2 = lat2 * 180 / PI";
_lat2 = _lat2*180/(double)_pi;
 //BA.debugLineNum = 309;BA.debugLine="lon2 = lon2 * 180 / PI";
_lon2 = _lon2*180/(double)_pi;
 //BA.debugLineNum = 310;BA.debugLine="lon2 = ((lon2 + 540) Mod 360) - 180";
_lon2 = ((_lon2+540)%360)-180;
 //BA.debugLineNum = 311;BA.debugLine="res(0)=lat2:res(1)=lon2:res(2)=timeToLand:res(3)=";
_res[(int) (0)] = _lat2;
 //BA.debugLineNum = 311;BA.debugLine="res(0)=lat2:res(1)=lon2:res(2)=timeToLand:res(3)=";
_res[(int) (1)] = _lon2;
 //BA.debugLineNum = 311;BA.debugLine="res(0)=lat2:res(1)=lon2:res(2)=timeToLand:res(3)=";
_res[(int) (2)] = _timetoland;
 //BA.debugLineNum = 311;BA.debugLine="res(0)=lat2:res(1)=lon2:res(2)=timeToLand:res(3)=";
_res[(int) (3)] = _horizdist;
 //BA.debugLineNum = 312;BA.debugLine="Return res";
if (true) return _res;
 //BA.debugLineNum = 313;BA.debugLine="End Sub";
return null;
}
public static Object  _createcolorstatelist(int _color) throws Exception{
anywheresoftware.b4j.object.JavaObject _jo = null;
int[][] _states = null;
 //BA.debugLineNum = 364;BA.debugLine="Sub CreateColorStateList(color As Int) As Object";
 //BA.debugLineNum = 365;BA.debugLine="Dim jo As JavaObject";
_jo = new anywheresoftware.b4j.object.JavaObject();
 //BA.debugLineNum = 366;BA.debugLine="Dim states(1,0) As Int";
_states = new int[(int) (1)][];
{
int d0 = _states.length;
int d1 = (int) (0);
for (int i0 = 0;i0 < d0;i0++) {
_states[i0] = new int[d1];
}
}
;
 //BA.debugLineNum = 367;BA.debugLine="jo.InitializeNewInstance(\"android.content.res.Col";
_jo.InitializeNewInstance("android.content.res.ColorStateList",new Object[]{(Object)(_states),(Object)(anywheresoftware.b4a.keywords.Common.Colors)});
 //BA.debugLineNum = 368;BA.debugLine="Return jo";
if (true) return (Object)(_jo.getObject());
 //BA.debugLineNum = 369;BA.debugLine="End Sub";
return null;
}
public static anywheresoftware.b4a.objects.EditTextWrapper  _createedittext(int _y) throws Exception{
anywheresoftware.b4a.objects.EditTextWrapper _e = null;
anywheresoftware.b4j.object.JavaObject _jo = null;
int _type_class_number = 0;
int _type_number_flag_decimal = 0;
 //BA.debugLineNum = 161;BA.debugLine="Sub CreateEditText(y As Int) As EditText";
 //BA.debugLineNum = 162;BA.debugLine="Dim e As EditText";
_e = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 163;BA.debugLine="e.Initialize(\"\")";
_e.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 164;BA.debugLine="e.TextColor = Colors.White";
_e.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 165;BA.debugLine="e.Color = Colors.RGB(30,30,30)";
_e.setColor(anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (30),(int) (30),(int) (30)));
 //BA.debugLineNum = 166;BA.debugLine="sv.Panel.AddView(e, 5%x, y, 90%x, 60dip)";
mostCurrent._sv.getPanel().AddView((android.view.View)(_e.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (5),mostCurrent.activityBA),_y,anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (90),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (60)));
 //BA.debugLineNum = 169;BA.debugLine="Dim jo As JavaObject = e";
_jo = new anywheresoftware.b4j.object.JavaObject();
_jo = (anywheresoftware.b4j.object.JavaObject) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4j.object.JavaObject(), (java.lang.Object)(_e.getObject()));
 //BA.debugLineNum = 170;BA.debugLine="Dim TYPE_CLASS_NUMBER As Int = 2";
_type_class_number = (int) (2);
 //BA.debugLineNum = 171;BA.debugLine="Dim TYPE_NUMBER_FLAG_DECIMAL As Int = 8192";
_type_number_flag_decimal = (int) (8192);
 //BA.debugLineNum = 172;BA.debugLine="jo.RunMethod(\"setInputType\", Array(Bit.Or(TYPE_CL";
_jo.RunMethod("setInputType",new Object[]{(Object)(anywheresoftware.b4a.keywords.Common.Bit.Or(_type_class_number,_type_number_flag_decimal))});
 //BA.debugLineNum = 174;BA.debugLine="Return e";
if (true) return _e;
 //BA.debugLineNum = 175;BA.debugLine="End Sub";
return null;
}
public static String  _createlabel(String _txt,int _y,int _color) throws Exception{
anywheresoftware.b4a.objects.LabelWrapper _lbl = null;
 //BA.debugLineNum = 153;BA.debugLine="Sub CreateLabel(txt As String, y As Int, color As";
 //BA.debugLineNum = 154;BA.debugLine="Dim lbl As Label";
_lbl = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 155;BA.debugLine="lbl.Initialize(\"\")";
_lbl.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 156;BA.debugLine="lbl.TextColor = color";
_lbl.setTextColor(_color);
 //BA.debugLineNum = 157;BA.debugLine="lbl.Text = txt";
_lbl.setText(BA.ObjectToCharSequence(_txt));
 //BA.debugLineNum = 158;BA.debugLine="sv.Panel.AddView(lbl, 5%x, y, 90%x, 60dip)";
mostCurrent._sv.getPanel().AddView((android.view.View)(_lbl.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (5),mostCurrent.activityBA),_y,anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (90),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (60)));
 //BA.debugLineNum = 159;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 24;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 25;BA.debugLine="Private edtLat, edtLon, edtAlt, edtVv, edtH, edtH";
mostCurrent._edtlat = new anywheresoftware.b4a.objects.EditTextWrapper();
mostCurrent._edtlon = new anywheresoftware.b4a.objects.EditTextWrapper();
mostCurrent._edtalt = new anywheresoftware.b4a.objects.EditTextWrapper();
mostCurrent._edtvv = new anywheresoftware.b4a.objects.EditTextWrapper();
mostCurrent._edth = new anywheresoftware.b4a.objects.EditTextWrapper();
mostCurrent._edtheading = new anywheresoftware.b4a.objects.EditTextWrapper();
mostCurrent._edtdescent = new anywheresoftware.b4a.objects.EditTextWrapper();
mostCurrent._edtground = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 26;BA.debugLine="Private lblResult As Label";
mostCurrent._lblresult = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 27;BA.debugLine="Private btnCompute, btnOpenMaps, btnShare, btnCop";
mostCurrent._btncompute = new anywheresoftware.b4a.objects.ButtonWrapper();
mostCurrent._btnopenmaps = new anywheresoftware.b4a.objects.ButtonWrapper();
mostCurrent._btnshare = new anywheresoftware.b4a.objects.ButtonWrapper();
mostCurrent._btncopy = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 28;BA.debugLine="Private cbMs, cbKmh As CheckBox";
mostCurrent._cbms = new anywheresoftware.b4a.objects.CompoundButtonWrapper.CheckBoxWrapper();
mostCurrent._cbkmh = new anywheresoftware.b4a.objects.CompoundButtonWrapper.CheckBoxWrapper();
 //BA.debugLineNum = 29;BA.debugLine="Private lastResultLat, lastResultLon, lastTimeToL";
_lastresultlat = 0;
_lastresultlon = 0;
_lasttimetoland = 0;
_lasthorizdist = 0;
 //BA.debugLineNum = 30;BA.debugLine="Private sv As ScrollView";
mostCurrent._sv = new anywheresoftware.b4a.objects.ScrollViewWrapper();
 //BA.debugLineNum = 31;BA.debugLine="Dim F As DoubleTaptoClose";
mostCurrent._f = new b4a.RS_calculator.doubletaptoclose();
 //BA.debugLineNum = 35;BA.debugLine="End Sub";
return "";
}
public static double  _kmh_to_mps(double _kmh) throws Exception{
 //BA.debugLineNum = 284;BA.debugLine="Sub KmH_To_mps(kmh As Double) As Double";
 //BA.debugLineNum = 285;BA.debugLine="Return kmh / 3.6";
if (true) return _kmh/(double)3.6;
 //BA.debugLineNum = 286;BA.debugLine="End Sub";
return 0;
}
public static double  _myround2(double _v,int _digits) throws Exception{
double _mult = 0;
 //BA.debugLineNum = 288;BA.debugLine="Sub MyRound2(v As Double, digits As Int) As Double";
 //BA.debugLineNum = 289;BA.debugLine="Dim mult As Double = Power(10, digits)";
_mult = anywheresoftware.b4a.keywords.Common.Power(10,_digits);
 //BA.debugLineNum = 290;BA.debugLine="Return Round(v * mult) / mult";
if (true) return anywheresoftware.b4a.keywords.Common.Round(_v*_mult)/(double)_mult;
 //BA.debugLineNum = 291;BA.debugLine="End Sub";
return 0;
}

public static void initializeProcessGlobals() {
    
    if (main.processGlobalsRun == false) {
	    main.processGlobalsRun = true;
		try {
		        main._process_globals();
starter._process_globals();
		
        } catch (Exception e) {
			throw new RuntimeException(e);
		}
    }
}public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 16;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 17;BA.debugLine="Private rEarth As Double = 6371000 ' meters";
_rearth = 6371000;
 //BA.debugLineNum = 18;BA.debugLine="Private Const PI As Double = 3.141592653589793";
_pi = 3.141592653589793;
 //BA.debugLineNum = 19;BA.debugLine="Private xui As XUI";
_xui = new anywheresoftware.b4a.objects.B4XViewWrapper.XUI();
 //BA.debugLineNum = 20;BA.debugLine="Dim gLat, gLon, gAlt, gGround, gVv, gH, gHeading,";
_glat = "";
_glon = "";
_galt = "";
_gground = "";
_gvv = "";
_gh = "";
_gheading = "";
_gdescent = "";
 //BA.debugLineNum = 21;BA.debugLine="Dim gUnitMs, gUnitKmh As Boolean";
_gunitms = false;
_gunitkmh = false;
 //BA.debugLineNum = 22;BA.debugLine="End Sub";
return "";
}
}
