package com.primeton.client;

import com.sun.javafx.scene.control.skin.NestedTableColumnHeader;
import com.sun.javafx.scene.control.skin.TableColumnHeader;
import com.sun.javafx.scene.control.skin.TableHeaderRow;
import com.sun.javafx.scene.control.skin.TableViewSkin;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import javafx.util.Callback;

import java.util.List;

/**
 * Created by clg on 2018/2/1.
 */
public class JavafxControl {
    public static void tableColumnAutoWidth(final TableView table){
        int columnSize = table.getColumns().size();
        float[] widths = new float[columnSize];
        for(int i=0;i<columnSize;i++){
            widths[i] = 1;
        }
        tableColumnAutoWidth(table,widths);
    }
   public static void tableColumnAutoWidth(final TableView table,float[] widths){
       // To generalize the columns width proportions in relation to the table width,
       // you do not need to put pixel related values, you can use small float numbers if you wish,
       // because it's the relative proportion of each columns width what matters here:


       // whether the first column should be fixed
       final boolean fixFirstColumm = true;

       // fix the first column width when table width is lower than:
       final float fixOnTableWidth = 360; //pixels

       // calulates sum of widths
       float sum = 0;
       for (double i : widths) {
           sum += i;
       }

       // calculates the fraction of the first column proportion in relation to the sum of all column proportions
       float firstColumnProportion = widths[0] / sum;

       // calculate the fitting fix width for the first column, you can change it by your needs, but it jumps to this width
       final float firstColumnFixSize = fixOnTableWidth * firstColumnProportion;

       // set the width to the columns
       for (int i = 0; i < widths.length; i++) {
           ((TableColumn)table.getColumns().get(i)).prefWidthProperty().bind(table.widthProperty().multiply((widths[i] / sum)));
           // ---------The exact width-------------^-------------^
           if (fixFirstColumm)
               if (i == 0) {
                   table.widthProperty().addListener(new ChangeListener<Number>() {
                       @Override
                       public void changed(ObservableValue<? extends Number> arg0, Number oldTableWidth, Number newTableWidth) {

                           if (newTableWidth.intValue() <= fixOnTableWidth) {

                               // before you can set new value to column width property, need to unbind the autoresize binding
                               ((TableColumn)table.getColumns().get(0)).prefWidthProperty().unbind();
                               ((TableColumn)table.getColumns().get(0)).prefWidthProperty().setValue(firstColumnFixSize);

                           } else if (!((TableColumn)table.getColumns().get(0)).prefWidthProperty().isBound()) {

                               // than readd the autoresize binding if condition table.width > x
                               ((TableColumn)table.getColumns().get(0)).prefWidthProperty()
                                       .bind(table.widthProperty().multiply(firstColumnProportion));
                           }

                       }
                   });
               }
       }
   }

}
