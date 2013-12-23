package org.pentaho.di.trans.steps.html2xml;

import java.util.List;

import org.pentaho.di.core.CheckResult;
import org.pentaho.di.core.CheckResultInterface;
import org.pentaho.di.core.Const;
import org.pentaho.di.core.annotations.Step;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.exception.KettleStepException;
import org.pentaho.di.core.exception.KettleXMLException;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.core.variables.VariableSpace;
import org.pentaho.di.core.xml.XMLHandler;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.repository.ObjectId;
import org.pentaho.di.repository.Repository;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.BaseStepMeta;
import org.pentaho.di.trans.step.StepDataInterface;
import org.pentaho.di.trans.step.StepInterface;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.step.StepMetaInterface;
import org.pentaho.metastore.api.IMetaStore;
import org.w3c.dom.Node;

@Step( id = "Html2Xml", image = "html2xml.png", name = "HTML to XML",
    description = "Converts HTML to XHTML", categoryDescription = "Transform" )
public class Html2XmlMeta extends BaseStepMeta implements StepMetaInterface {
  
  private static Class<?> PKG = Html2XmlMeta.class; // for i18n purposes, needed by Translator2!!


  /** The field to convert */
  private String fieldname;

  /** The result field */
  private String resultFieldName;

  @Override
  public void setDefault() {
    fieldname = "";

  }

  public String getFieldname() {
    return fieldname;
  }

  public void setFieldname( String fieldname ) {
    this.fieldname = fieldname;
  }
  
  @Override
  public StepDataInterface getStepData() {
    return new Html2XmlData();
  }

  @Override
  public Object clone() {
    Html2XmlMeta retval = (Html2XmlMeta) super.clone();

    return retval;
  }
  
  public String getXML() {
    StringBuffer retval = new StringBuffer( 200 );

    retval.append( XMLHandler.addTagValue( "fieldname", fieldname ) );
    retval.append( XMLHandler.addTagValue( "resultfieldname", resultFieldName ) );

    return retval.toString();
  }
  
  public void loadXML( Node stepnode, List<DatabaseMeta> databases, IMetaStore metaStore )
    throws KettleXMLException {
    readData( stepnode );
  }

  private void readData( Node stepnode ) throws KettleXMLException {
    try {
      fieldname = XMLHandler.getTagValue( stepnode, "fieldname" );
      resultFieldName = XMLHandler.getTagValue( stepnode, "resultfieldname" );
      
    } catch ( Exception e ) {
      throw new KettleXMLException( BaseMessages.getString(
        PKG, "Html2XmlMeta.Exception.UnableToLoadStepInfoFromXML" ), e );
    }
  }

  public void readRep( Repository rep, IMetaStore metaStore, ObjectId id_step, List<DatabaseMeta> databases )
    throws KettleException {
    try {
      fieldname = rep.getStepAttributeString( id_step, "fieldname" );
      resultFieldName = rep.getStepAttributeString( id_step, "resultfieldname" );
    } catch ( Exception e ) {
      throw new KettleException( BaseMessages.getString(
        PKG, "Html2XmlMeta.Exception.UnexpectedErrorInReadingStepInfoFromRepository" ), e );
    }
  }

  public void saveRep( Repository rep, IMetaStore metaStore, ObjectId id_transformation, ObjectId id_step )
    throws KettleException {
    try {
      rep.saveStepAttribute( id_transformation, id_step, "fieldname", fieldname );
      rep.saveStepAttribute( id_transformation, id_step, "resultfieldname", resultFieldName );
     
    } catch ( Exception e ) {
      throw new KettleException( BaseMessages.getString(
        PKG, "Html2XmlMeta.Exception.UnableToSaveStepInfoToRepository" )
        + id_step, e );
    }
  }

  public void getFields( RowMetaInterface rowMeta, String origin, RowMetaInterface[] info, StepMeta nextStep,
    VariableSpace space, Repository repository, IMetaStore metaStore ) throws KettleStepException {
    // Default: nothing changes to rowMeta
  }

  public void check( List<CheckResultInterface> remarks, TransMeta transMeta, StepMeta stepMeta,
    RowMetaInterface prev, String[] input, String[] output, RowMetaInterface info, VariableSpace space,
    Repository repository, IMetaStore metaStore ) {
    CheckResult cr;

    if ( Const.isEmpty( fieldname ) ) {
      cr =
        new CheckResult( CheckResultInterface.TYPE_RESULT_ERROR, BaseMessages.getString(
          PKG, "Html2XmlMeta.CheckResult.NoFieldSpecified" ), stepMeta );
    } else {
      cr =
        new CheckResult( CheckResultInterface.TYPE_RESULT_OK, BaseMessages.getString(
          PKG, "Html2XmlMeta.CheckResult.FieldSpecified" ), stepMeta );
    }
    remarks.add( cr );

    // See if we have input streams leading to this step!
    if ( input.length > 0 ) {
      cr =
        new CheckResult( CheckResultInterface.TYPE_RESULT_OK, BaseMessages.getString(
          PKG, "Html2XmlMeta.CheckResult.StepReceivingInfoFromOtherSteps" ), stepMeta );
      remarks.add( cr );
    } else {
      cr =
        new CheckResult( CheckResultInterface.TYPE_RESULT_ERROR, BaseMessages.getString(
          PKG, "Html2XmlMeta.CheckResult.NoInputReceivedFromOtherSteps" ), stepMeta );
      remarks.add( cr );
    }
    
    if ( Const.isEmpty( resultFieldName ) ) {
      cr =
        new CheckResult( CheckResultInterface.TYPE_RESULT_ERROR, BaseMessages.getString(
          PKG, "Html2XmlMeta.CheckResult.NoResultFieldSpecified" ), stepMeta );
    } else {
      cr =
        new CheckResult( CheckResultInterface.TYPE_RESULT_OK, BaseMessages.getString(
          PKG, "Html2XmlMeta.CheckResult.ResultFieldSpecified" ), stepMeta );
    }
    remarks.add( cr );
  }

  public StepInterface getStep( StepMeta stepMeta, StepDataInterface stepDataInterface, int cnr, TransMeta tr,
    Trans trans ) {
    return new Html2Xml( stepMeta, stepDataInterface, cnr, tr, trans );
  }

  public String getResultFieldName() {
    return resultFieldName;
  }

  public void setResultFieldName( String resultFieldName ) {
    this.resultFieldName = resultFieldName;
  }
}
