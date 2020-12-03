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
    String command ;

    public JSONObject sendReceiptCommand(ReceiptCommand receiptCommand) {
        JSONObject obj = new JSONObject();
        ITesseract image = new Tesseract();
        image.setDatapath("tessdata");
        image.setLanguage(("eng"));
        pathReceipt = path+receiptCommand.getPathReceipt();
        val = receiptCommand.getReceiptValue();
        command = receiptCommand.getReceiptCommand();
        checkObject = val.split(",");
            try {
                String str = image.doOCR(new File(pathReceipt));
                if (command.equals("check")) {
                    log.info(str);
                    obj.put("image", str);
                }
                  if (str != null) {
                        for (int i = 0; i < checkObject.length; i++) {
                            String s = checkObject[i];
                            if (str.contains(s)) {
                                log.info("valuePass N." + i + " - " + s );
                                obj.put("value N." + i +"-PASS", s);
                            } else {
                                log.info("valueFailed N." + i + " - " + s );
                                obj.put("value N." + i + "-FAILED", s);
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
        return obj;
    }
}








