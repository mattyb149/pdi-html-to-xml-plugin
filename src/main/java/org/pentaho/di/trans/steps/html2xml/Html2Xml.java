package org.pentaho.di.trans.steps.html2xml;

import java.io.StringReader;
import java.io.StringWriter;

import org.pentaho.di.core.Const;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.exception.KettleStepException;
import org.pentaho.di.core.row.RowDataUtil;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.BaseStep;
import org.pentaho.di.trans.step.StepDataInterface;
import org.pentaho.di.trans.step.StepInterface;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.step.StepMetaInterface;
import org.w3c.tidy.Tidy;

public class Html2Xml extends BaseStep implements StepInterface {
  
  private static Class<?> PKG = Html2XmlMeta.class; // for i18n purposes, needed by Translator2!!
  
  private Html2XmlMeta meta;
  private Html2XmlData data;
  
  private Tidy tidy;

  public Html2Xml( StepMeta stepMeta, StepDataInterface stepDataInterface, int copyNr, TransMeta transMeta, Trans trans ) {
    super( stepMeta, stepDataInterface, copyNr, transMeta, trans );
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
    
    if ( first ) {
      
      data.setInputRowMeta( getInputRowMeta() );
      data.setOutputRowMeta( getInputRowMeta().clone() );  
      first = false;
    }
    
    // Create and configure a JTidy instance (the authors recommend a new instance for each parse)
    tidy = new Tidy();
    if(meta.isOutputXHTML()) {
      tidy.setXHTML( true );
      tidy.setXmlOut( false );
    }
    else {
      tidy.setXHTML( false );
      tidy.setXmlOut( true );
    }
    tidy.setOutputEncoding( environmentSubstitute( Const.NVL( meta.getEncoding(), "UTF-8" ) ) );
    tidy.setQuoteNbsp(false);
    
    //read the value of the html input field
    String fieldName = meta.getFieldname();
    if(Const.isEmpty( fieldName )) {
      logError( BaseMessages.getString( PKG, "Html2Xml.Exception.UnableToFindFieldName" ) );
      throw new KettleStepException( BaseMessages.getString( PKG, "Html2Xml.Exception.UnableToFindFieldName" ) );
    }
    
    meta.getFields( data.getOutputRowMeta(), getStepname(), null, null, this, repository, metaStore );
    
    int inputFieldIndex = data.getOutputRowMeta().indexOfValue( meta.getFieldname() );    
    int resultFieldIndex = data.getOutputRowMeta().indexOfValue( meta.getResultFieldName() );
    
    if( inputFieldIndex < 0 || inputFieldIndex > r.length) {
      logError( BaseMessages.getString( PKG, "Html2Xml.Exception.UnableToFindFieldName" ) );
      throw new KettleStepException( BaseMessages.getString( PKG, "Html2Xml.Exception.UnableToFindFieldName" ) );
    }
    
    if( resultFieldIndex < 0 ) {
      logError( BaseMessages.getString( PKG, "Html2Xml.Log.NoResultFieldSpecified" ) );
      throw new KettleStepException( BaseMessages.getString( PKG, "Html2Xml.Log.NoResultFieldSpecified" ) );
    }
    
    // Resize the output row if necessary
    Object[] outputRow = (resultFieldIndex >= r.length) ? RowDataUtil.resizeArray( r, resultFieldIndex+1 ) : r;
    
    // There's a problem with HTML containing the &nbsp; directive as it is not part of the XML spec.
    // Replace this with &#160; before parsing
    // TODO: Use ValueMetaString methods to get the raw input string?
    String rawInputString = (String)outputRow[inputFieldIndex];
    String replacedNbspString = rawInputString.replace("&nbsp;","&#160;");
    
    // Use JTidy to parse HTML to XML
    StringReader html = new StringReader(replacedNbspString);
    StringWriter xml = new StringWriter();
    tidy.parse(html, xml);

    outputRow[resultFieldIndex] = xml.toString();

    //push the output row to the outgoing stream.
    putRow(data.getOutputRowMeta(), outputRow);    
    return true;
  }

}
 