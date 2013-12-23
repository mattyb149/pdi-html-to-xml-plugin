package org.pentaho.di.trans.steps.html2xml;

import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.BaseStep;
import org.pentaho.di.trans.step.StepDataInterface;
import org.pentaho.di.trans.step.StepInterface;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.step.StepMetaInterface;

public class Html2Xml extends BaseStep implements StepInterface {
  
  private Html2XmlMeta meta;
  private Html2XmlData data;

  public Html2Xml( StepMeta stepMeta, StepDataInterface stepDataInterface, int copyNr, TransMeta transMeta, Trans trans ) {
    super( stepMeta, stepDataInterface, copyNr, transMeta, trans );
  }

  @Override
  public boolean init( StepMetaInterface arg0, StepDataInterface arg1 ) {
    boolean superInit = super.init( arg0, arg1 );
    if(!superInit) return false;
    // Do stuff here if needed
    return true;
  }

  @Override
  public boolean processRow( StepMetaInterface smi, StepDataInterface sdi ) throws KettleException {
    
    meta = (Html2XmlMeta)smi;
    data = (Html2XmlData)sdi;
    
    Object[] r = getRow();
    
    if ( r == null ) {
      // no more input to be expected...

      setOutputDone();
      return false;
    }
    
    return true;
  }

}
 