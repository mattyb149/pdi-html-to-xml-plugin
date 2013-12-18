package org.pentaho.di.trans.steps.html2xml;

import org.pentaho.di.core.annotations.Step;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.BaseStepMeta;
import org.pentaho.di.trans.step.StepDataInterface;
import org.pentaho.di.trans.step.StepInterface;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.step.StepMetaInterface;

@Step( id = "Html2Xml", image = "html2xml.png", name = "HTML to XML",
    description = "Converts HTML to proper XML", categoryDescription = "Transform" )
public class Html2XmlMeta extends BaseStepMeta implements StepMetaInterface {

  @Override
  public StepInterface getStep( StepMeta arg0, StepDataInterface arg1, int arg2, TransMeta arg3, Trans arg4 ) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public StepDataInterface getStepData() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void setDefault() {
    // TODO Auto-generated method stub

  }

}
