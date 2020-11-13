package com.vinz.tak.service;

import com.vinz.tak.model.ReceiptCommand;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class ReceiptService<i> extends AbstractService {

    @Autowired
    private ProcessExecutor processExecutor;

    int i;
    String path = "C:\\Scontrini\\";
    String pathReceipt;
    String val;
    String[] checkObject ;
    String val2 = "VERONA ANTICA SAS";


    public JSONObject sendReceiptCommand(ReceiptCommand receiptCommand) {
        JSONObject obj = new JSONObject();
        ITesseract image = new Tesseract();
        image.setDatapath("tessdata");
        image.setLanguage(("eng"));
        pathReceipt = path+receiptCommand.getPathReceipt();
        val = receiptCommand.getReceiptValue();
        checkObject = val.split(",");
        if (receiptCommand.getReceiptCommand().contains("check")) {
            try {
                String str = image.doOCR(new File(pathReceipt));
                log.debug("Data from image: " + str);
                if (str != null) {
                    if (str.contains(val2)) {
                        for (int i = 0; i < checkObject.length; i++) {
                            String s = checkObject[i];
                            if (str.contains(s)) {
                                log.info("objectfinder number=" + i + " value=" + s + " Status: Passed");
                                obj.put("valuePass" + i, s);
                            } else {
                                log.info("objectfinder number=" + i + " value=" + s + " Status: Failed");
                                obj.put("valueFailed" + i, s);
                            }
                        }
                    }
                } else log.info("Error");
            } catch (TesseractException e) {
                e.printStackTrace();
                obj.put("info","Error");
            }

            if (obj.toString().contains("valueFailed")) {
                obj.put("info", "Check your Receipt, something goes wrong");

            } else if (obj.toString().contains("valuePass")) {
                obj.put("info", "Good Check, Receipt Good");

            }

        }
        return obj;
    }
}








