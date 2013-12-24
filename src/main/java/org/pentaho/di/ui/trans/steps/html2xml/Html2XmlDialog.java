package org.pentaho.di.ui.trans.steps.html2xml;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.pentaho.di.core.Const;
import org.pentaho.di.core.exception.KettleStepException;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.BaseStepMeta;
import org.pentaho.di.trans.step.StepDialogInterface;
import org.pentaho.di.trans.steps.html2xml.Html2XmlMeta;
import org.pentaho.di.ui.core.dialog.ErrorDialog;
import org.pentaho.di.ui.core.widget.TextVar;
import org.pentaho.di.ui.trans.step.BaseStepDialog;

public class Html2XmlDialog extends BaseStepDialog implements StepDialogInterface {
  
  private static Class<?> PKG = Html2XmlMeta.class; // for i18n purposes, needed by Translator2!!

  private Label wlFieldName;
  private CCombo wFieldName;
  
  private Label wlResultFieldName;
  private TextVar wResultFieldName;
  
  private RowMetaInterface inputFields;
  
  private Html2XmlMeta input;

  
  public Html2XmlDialog( Shell parent, Object in, TransMeta tr, String sname ) {
    super( parent, (BaseStepMeta) in, tr, sname );
    input = (Html2XmlMeta) in;
  }

  public String open() {
    Shell parent = getParent();
    Display display = parent.getDisplay();

    shell = new Shell( parent, SWT.DIALOG_TRIM | SWT.RESIZE | SWT.MIN | SWT.MAX );
    props.setLook( shell );
    setShellImage( shell, input );

    ModifyListener lsMod = new ModifyListener() {
      public void modifyText( ModifyEvent e ) {
        input.setChanged();
      }
    };
    
    FormLayout formLayout = new FormLayout();
    formLayout.marginWidth = Const.FORM_MARGIN;
    formLayout.marginHeight = Const.FORM_MARGIN;

    shell.setLayout( formLayout );
    shell.setText( BaseMessages.getString( PKG, "Html2XmlDialog.Shell.Title" ) );

    int middle = props.getMiddlePct();
    int margin = Const.MARGIN;

    // Stepname line
    wlStepname = new Label( shell, SWT.RIGHT );
    wlStepname.setText( BaseMessages.getString( PKG, "Html2XmlDialog.Stepname.Label" ) );
    props.setLook( wlStepname );
    fdlStepname = new FormData();
    fdlStepname.left = new FormAttachment( 0, 0 );
    fdlStepname.right = new FormAttachment( middle, 0 );
    fdlStepname.top = new FormAttachment( 0, margin );
    wlStepname.setLayoutData( fdlStepname );
    wStepname = new Text( shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER );
    wStepname.setText( stepname );
    props.setLook( wStepname );
    wStepname.addModifyListener( lsMod );
    fdStepname = new FormData();
    fdStepname.left = new FormAttachment( middle, margin );
    fdStepname.top = new FormAttachment( 0, margin );
    fdStepname.right = new FormAttachment( 100, 0 );
    wStepname.setLayoutData( fdStepname );

    // The name of the field to validate
    //
    wlFieldName = new Label( shell, SWT.RIGHT );
    wlFieldName.setText( BaseMessages.getString( PKG, "Html2XmlDialog.FieldName.Label" ) );
    props.setLook( wlFieldName );
    FormData fdlFieldName = new FormData();
    fdlFieldName.left = new FormAttachment( 0, 0 );
    fdlFieldName.right = new FormAttachment( middle, 0 );
    fdlFieldName.top = new FormAttachment( wStepname, margin );
    wlFieldName.setLayoutData( fdlFieldName );
    wFieldName = new CCombo( shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER );
    props.setLook( wFieldName );
    FormData fdFieldName = new FormData();
    fdFieldName.left = new FormAttachment( middle, margin );
    fdFieldName.right = new FormAttachment( 100, 0 );
    fdFieldName.top = new FormAttachment( wStepname, margin );
    wFieldName.setLayoutData( fdFieldName );
    
    // Stepname line
    wlResultFieldName = new Label( shell, SWT.RIGHT );
    wlResultFieldName.setText( BaseMessages.getString( PKG, "Html2XmlDialog.ResultFieldName.Label" ) );
    props.setLook( wlResultFieldName );
    FormData fdlResultFieldName = new FormData();
    fdlResultFieldName.left = new FormAttachment( 0, 0 );
    fdlResultFieldName.right = new FormAttachment( middle, 0 );
    fdlResultFieldName.top = new FormAttachment( wFieldName, margin );
    wlResultFieldName.setLayoutData( fdlResultFieldName );
    wResultFieldName = new TextVar( transMeta, shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER );
    props.setLook( wResultFieldName );
    wResultFieldName.addModifyListener( lsMod );
    FormData fdResultFieldName = new FormData();
    fdResultFieldName.left = new FormAttachment( middle, margin );
    fdResultFieldName.top = new FormAttachment( wFieldName, margin );
    fdResultFieldName.right = new FormAttachment( 100, 0 );
    wResultFieldName.setLayoutData( fdResultFieldName );

    // TODO: grab field list in thread in the background...
    //
    try {
      inputFields = transMeta.getPrevStepFields( stepMeta );
      wFieldName.setItems( inputFields.getFieldNames() );
    } catch ( KettleStepException ex ) {
      new ErrorDialog( shell,
        BaseMessages.getString( PKG, "Html2XmlDialog.Exception.CantGetFieldsFromPreviousSteps.Title" ),
        BaseMessages.getString( PKG, "Html2XmlDialog.Exception.CantGetFieldsFromPreviousSteps.Message" ), ex );
    }
    
    // Add listeners
    lsCancel = new Listener() {
      public void handleEvent( Event e ) {
        cancel();
      }
    };
    lsOK = new Listener() {
      public void handleEvent( Event e ) {
        ok();
      }
    };
    
    // THE BUTTONS
    wOK = new Button( shell, SWT.PUSH );
    wOK.setText( BaseMessages.getString( PKG, "System.Button.OK" ) );
    wCancel = new Button( shell, SWT.PUSH );
    wCancel.setText( BaseMessages.getString( PKG, "System.Button.Cancel" ) );

    setButtonPositions( new Button[] { wOK, wCancel }, margin, wResultFieldName );

    wCancel.addListener( SWT.Selection, lsCancel );
    wOK.addListener( SWT.Selection, lsOK );

    lsDef = new SelectionAdapter() {
      public void widgetDefaultSelected( SelectionEvent e ) {
        ok();
      }
    };

    wStepname.addSelectionListener( lsDef );

    // Detect X or ALT-F4 or something that kills this window...
    shell.addShellListener( new ShellAdapter() {
      public void shellClosed( ShellEvent e ) {
        cancel();
      }
    } );

    // Set the shell size, based upon previous time...
    setSize();

    getData();
    input.setChanged( backupChanged );

    shell.open();
    while ( !shell.isDisposed() ) {
      if ( !display.readAndDispatch() ) {
        display.sleep();
      }
    }
    return stepname;
  }
  
  private void cancel() {
    stepname = null;
    input.setChanged( backupChanged );
    dispose();
  }

  private void ok() {
    if ( Const.isEmpty( wStepname.getText() ) ) {
      return;
    }

    input.setFieldname( wFieldName.getText() );
    input.setResultFieldName( wResultFieldName.getText() );
    
    stepname = wStepname.getText(); // return value

    dispose();
  }
  
  /**
   * Copy information from the meta-data input to the dialog fields.
   */
  public void getData() {
    wFieldName.setText( Const.NVL( input.getFieldname(), "" ) );
    wResultFieldName.setText( Const.NVL( input.getResultFieldName(), "" ) );
   
    wStepname.selectAll();
    wStepname.setFocus();
  }
}
