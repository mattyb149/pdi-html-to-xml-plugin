package org.pentaho.di.trans.steps.html2xml;

import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.trans.step.BaseStepData;
import org.pentaho.di.trans.step.StepDataInterface;

public class Html2XmlData extends BaseStepData implements StepDataInterface {

  private RowMetaInterface outputRowMeta;
  private RowMetaInterface inputRowMeta;
  
  public RowMetaInterface getOutputRowMeta() {
    return outputRowMeta;
  }
  public void setOutputRowMeta( RowMetaInterface outputRowMeta ) {
    this.outputRowMeta = outputRowMeta;
  }
  public RowMetaInterface getInputRowMeta() {
    return inputRowMeta;
  }
  public void setInputRowMeta( RowMetaInterface inputRowMeta ) {
    this.inputRowMeta = inputRowMeta;
  }
}
