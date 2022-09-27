package com.fonfella.tak.service;

import com.fonfella.tak.model.ReceiptCommand;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

@Component
public class ReceiptService<i> extends AbstractService {

    @Autowired
    private ProcessExecutor processExecutor;

    //variability
    String viewReceipt = "true";
    String values = null;
    String deleteReicept = "true";


   // int i;

    private static final int FLIP_VERTICAL = 1;

    public JSONObject sendReceiptCommand(ReceiptCommand receiptCommand) throws IOException {
        JSONObject obj = new JSONObject();
        ITesseract image = new Tesseract();
        image.setDatapath("C:\\Users\\mdonzella\\IdeaProjects\\test-automation-framework\\tessdata");
        image.setLanguage("ita");
        //cambiato path verifica funzionamento
       // String folderPath = "C:\\CartellaLavoro\\scontrino\\provaScontrino.jpeg";
        //  String folderPath = "C:\\CartellaLavoro\\Scontrino.jpg";
        //File f = new File ("\\\\10.10.10.123\\Addons\\readme.txt");
        // String folderPath = "\\\\10.130.144.92\\Shared\\TM_Export\\TM004341_BC1941015\\TMROBOT_VisionImages\\P61_TEST_PRG_R05\\ImageLightOn_P\\2020-09-25\\source\\provaScontrino.jpg";

        // creare classe
//parte uso immagine ultima modification
        String dirPath = "C:\\cartellaLavoro\\scontrino\\scontrino";
        File dir = new File(dirPath);
        File[] files = dir.listFiles();
        File lastModifiedFile = files[0];
        if (files == null || files.length == 0) {
           String ultimaModifica = "Nessun File Errore!!!";
           obj.put("ERRORE", ultimaModifica);
        }

        for (int i = 0; i < files.length; i++) {
            if (lastModifiedFile.lastModified() < files[i].lastModified()) {
                lastModifiedFile = files[i];
            }
        }




        //parte per rotazione
        BufferedImage bm = ImageIO.read(new File(String.valueOf(lastModifiedFile)));
        final double rads = Math.toRadians(90);
        final double sin = Math.abs(Math.sin(rads));
        final double cos = Math.abs(Math.cos(rads));
        final int w = (int) Math.floor(bm.getWidth() * cos + bm.getHeight() * sin);
        final int h = (int) Math.floor(bm.getHeight() * cos + bm.getWidth() * sin);
        final BufferedImage rotatedImage = new BufferedImage(w, h, bm.getType());
        final AffineTransform at = new AffineTransform();
        at.translate(w / 2, h / 2);
        at.rotate(rads, 0, 0);
        at.translate(-bm.getWidth() / 2, -bm.getHeight() / 2);
        final AffineTransformOp rotateOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
        rotateOp.filter(bm, rotatedImage);

        File reiceptRotated = new File("C:\\cartellaLavoro\\scontrino\\scontrino\\rotatedImage.jpg");
        ImageIO.write(rotatedImage, "JPG", new File(String.valueOf(reiceptRotated)));
        //fine aggiunta

        values = receiptCommand.getReceiptValue();

        try {
            String str = image.doOCR(new File("C:\\CartellaLavoro\\scontrino\\scontrino\\rotatedImage.jpg"));
            log.info(str);
            if (viewReceipt.equals("false")) {
                log.info("Data from Image is: " + str);
            }
            ArrayList aList = new ArrayList(Arrays.asList(values.split("-")));

            for (int i = 0; i < aList.size(); i++) {
                log.info("[DETAILS] value to check  --> " + aList.get(i));
                ArrayList<String> ar = new ArrayList<String>();
                ar.add((String) aList.get(i));
                if (str.contains((CharSequence) aList.get(i))) {
                    log.info("[DETAILS] check N. " + i + ", IS PRESENT Checked Status Passed");
                    obj.put("Check N. "+i ,"elemento trovato ("+aList.get(i)+") -> PASSED!");
                } else {
                    log.info("[DETAILS] check N. " + i + ", IS NOT PRESENT Checked Status Failed");
                    obj.put("Check N. "+i ,"elemento NON trovato ("+aList.get(i)+") -> FAILED!");
                }
            }

            //cancellare o spostare file controllato???
        } catch (TesseractException e) {
            e.printStackTrace();
        }

        if (deleteReicept.equals("true")) {
            reiceptRotated.delete();
        }

        return obj;
    }

}








