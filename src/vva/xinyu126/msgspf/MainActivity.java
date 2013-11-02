package vva.xinyu126.msgspf;

import java.util.ArrayList;
import java.util.List;

import vva.xinyu126.msgspf.R;
import vva.xinyu126.msgspf.SMSDatabaseHelper;
import vva.xinyu126.msgspf.SMSEntity;
import vva.xinyu126.msgspf.SMSObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.NotificationManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends Activity {
	private static final int DELETE_MSG = Menu.FIRST;
    /** Called when the activity is first created. */
	ArrayAdapter<String> displayStrs = null;
	List<String> numContents = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	      
	      NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
	      manager.cancel(CooperationNotification.SIMPLE_NOTIFICATION_ID);
	      
	      numContents = getSMSNOAndContent();
	      
	      displayStrs = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,numContents);
	      
	      ListView smsList = (ListView)findViewById(R.id.smsList);
	      smsList.setAdapter(displayStrs);
	      
	      SMSObserver smsObserver = new SMSObserver(new Handler(),this);
	      getContentResolver().registerContentObserver(Uri.parse("content://sms/"), true, smsObserver);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
    	MenuItem itemDel = menu.add(0,DELETE_MSG,Menu.NONE,R.string.deleteAll);
    	
    	itemDel.setIcon(R.drawable.remove_item);
    	itemDel.setShortcut('0', 'D');
		return true;
	}
	
	 
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	super.onOptionsItemSelected(item);
    	switch (item.getItemId()) {
			case DELETE_MSG:{
				removeMessage();
				return true;
			}
		}
    	return false;
    }
    
    private boolean removeMessage(){
    	SMSDatabaseHelper helper = new SMSDatabaseHelper(this,SMSObserver.DB_NAME,null,SMSObserver.VERSION);
		helper.deleteSMS();
		numContents.clear();
		displayStrs.notifyDataSetChanged();
		return true;
    }
    
    private List<String> getSMSNOAndContent(){
    	SMSDatabaseHelper helper = new SMSDatabaseHelper(this,SMSObserver.DB_NAME,null,SMSObserver.VERSION);
    	List<SMSEntity> smsEntitys = helper.getAll();
    	
    	List<String> numContents = new ArrayList<String>();
    	for(SMSEntity smsEntity : smsEntitys){
    		String numContent = smsEntity.getFrom() + "----" + smsEntity.getBody();
    		numContents.add(numContent);
    	}
    	
    	return numContents;
    }

}
