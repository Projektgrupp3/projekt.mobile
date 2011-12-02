package tddd36.grupp3.views;

import java.util.ArrayList;

import org.json.JSONException;

import tddd36.grupp3.Sender;
import android.app.Activity;
import android.app.ActivityGroup;
import android.app.AlertDialog;
import android.app.LocalActivityManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;


/**
 * The purpose of this Activity is to manage the activities in a tab.
 * Note: Child Activities can handle Key Presses before they are seen here.
 * @Sjukvården This is a helper method for the SIP Activity Group where history needs to be handled. 
 */
public class TabGroupActivity extends ActivityGroup {

    private ArrayList<String> mIdList;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);       
        if (mIdList == null) mIdList = new ArrayList<String>();
    }
  
    /**
     * This is called when a child activity of this one calls its finish method. 
     * This implementation calls {@link LocalActivityManager#destroyActivity} on the child activity
     * and starts the previous activity.
     * If the last child activity just called finish(),this activity (the parent),
     * calls finish to finish the entire group.
     */
  @Override
  public void finishFromChild(Activity child) {
      LocalActivityManager manager = getLocalActivityManager();
      int index = mIdList.size()-1;
      
      if (index < 1) {
          finish();
          return;
      }
          
      manager.destroyActivity(mIdList.get(index), true);
      mIdList.remove(index); index--;
      String lastId = mIdList.get(index);
      Intent lastIntent = manager.getActivity(lastId).getIntent();
      Window newWindow = manager.startActivity(lastId, lastIntent);
      setContentView(newWindow.getDecorView());
  }
  
  /**
   * Starts an Activity as a child Activity to this.
   * @param Id Unique identifier of the activity to be started.
   * @param intent The Intent describing the activity to be started.
   * @throws android.content.ActivityNotFoundException.
   */
  public void startChildActivity(String Id, Intent intent) {     
      Window window = getLocalActivityManager().startActivity(Id,intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
      if (window != null) {
          mIdList.add(Id);
          setContentView(window.getDecorView()); 
      }    
  }

  /**
   * If a Child Activity handles KeyEvent.KEYCODE_BACK.
   * Simply override and add this method.
   */
  @Override
  public void  onBackPressed  () {
      int length = mIdList.size();
      if ( length > 1) {
          Activity current = getLocalActivityManager().getActivity(mIdList.get(length-1));
          current.finish();
      }else if(length == 1){
    	  Sender.send(Sender.LOG_OUT);
    	  finishActivity();
      }
  }
  public void finishActivity(){
	  int length = mIdList.size();
	  if(length == 1){
	  		AlertDialog logout = new AlertDialog.Builder(this).create();
			logout.setMessage("Är du säker på att du vill avsluta?");
			logout.setButton("Ja", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which){
					finish();
				}
			});
			logout.setButton2("Nej", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();					
				}
			});	
			logout.show();
	      }
  }
}