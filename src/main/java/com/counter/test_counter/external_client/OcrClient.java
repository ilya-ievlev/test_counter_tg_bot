package com.counter.test_counter.external_client;

import com.counter.test_counter.exception.OCRPhotoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class OcrClient {

    public String getResult(File imageFile) {
        ITesseract tesseract = new Tesseract();
        tesseract.setDatapath("/home/ilya/Desktop/java projects active/tg_bot/files_for_bot/");
        tesseract.setLanguage("ukr");
        try {
            return tesseract.doOCR(imageFile);
        } catch (TesseractException e) {
            log.error(e.getMessage(), e);
            throw new OCRPhotoException(e.getMessage(), e);
        }
    }
}
