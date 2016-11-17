package qantica.com.controles;

import android.content.Context;
import android.view.View.MeasureSpec;
import android.widget.ExpandableListView;

public class CategoriaExpListView extends ExpandableListView {

//	 int intGroupPosition, intChildPosition, intGroupid;
	  
	  public CategoriaExpListView(Context context) 
	  {
	   super(context);     
	  }
	    
	  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) 
	  {
	   widthMeasureSpec = MeasureSpec.makeMeasureSpec(960, MeasureSpec.AT_MOST);
	   heightMeasureSpec = MeasureSpec.makeMeasureSpec(200, MeasureSpec.AT_MOST);
	   super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	  }  
}
